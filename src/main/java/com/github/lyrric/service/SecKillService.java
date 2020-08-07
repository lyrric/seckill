package com.github.lyrric.service;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.SubDate;
import com.github.lyrric.model.SubDateTime;
import com.github.lyrric.model.VaccineList;
import com.github.lyrric.ui.MainFrame;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.lang.model.element.VariableElement;
import javax.swing.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
public class SecKillService {

    private HttpService httpService;

    private final Logger logger = LogManager.getLogger(SecKillService.class);

    private ExecutorService service = Executors.newFixedThreadPool(50);

    public SecKillService() {
        httpService = new HttpService();
    }

    /**
     * 多线程秒杀开启
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void startSecKill(Integer vaccineId, String startDateStr, MainFrame mainFrame) throws ParseException, InterruptedException {
        long startDate = convertDateToInt(startDateStr);
        long now = System.currentTimeMillis();
        if(now+5000 < startDate){
            logger.info("距离开始时间大于5秒，等待中......");
            Thread.sleep(startDate-now-5000);
        }
        logger.info("###########开始抢购###########");
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> orderId = new AtomicReference<>(null);
        Runnable task = ()-> {
            do {
                try {
                    List<SubDate> skSubDays = null;
                    //1.直接秒杀、获取秒杀资格
                    orderId.set(httpService.secKill(vaccineId.toString(), "1", Config.memberId.toString(), Config.idCard));
                    do {
                        try {
                            //2.秒杀成功后，获取接种日期
                            skSubDays = httpService.getSkSubDays(vaccineId.toString(), orderId.get());
                        } catch (BusinessException e) {
                            logger.info("获取接种日期，失败: {}",e.getErrMsg());
                        } catch (IOException e) {
                            logger.warn("获取接种日期，未知异常：", e.getCause());
                        }
                    } while (skSubDays == null);

                    for (SubDate day : skSubDays) {
                        Runnable getTimeTask = () -> {
                            try {
                                //3.根据接种日期，获取接种时间段
                                List<SubDateTime> skSubDayTime = httpService.getSkSubDayTime(vaccineId.toString(), orderId.toString(), day.getDay());
                                for (SubDateTime time : skSubDayTime) {
                                    //4.提交接种时间
                                    Runnable subDayTimeTask = () -> {
                                        try {
                                            httpService.subDayTime(vaccineId.toString(), orderId.get(), day.getDay(), time.getWid());
                                            success.set(true);
                                        } catch (BusinessException e) {
                                            logger.info("提交接种时间，失败: {}",e.getErrMsg());
                                        } catch (IOException e) {
                                            logger.warn("提交接种时间，未知异常：", e.getCause());
                                        }
                                    };
                                    service.submit(subDayTimeTask);
                                }
                            } catch (BusinessException e) {
                                logger.info("获取接种时间段，失败: {}",e.getErrMsg());
                            } catch (IOException e) {
                                logger.warn("获取接种时间段，未知异常：", e.getCause());
                            }
                        };
                        service.submit(getTimeTask);
                    }
                } catch (BusinessException e) {
                    logger.info("抢购失败: {}",e.getErrMsg());
                    //如果离开始时间六十秒后，都没有抢到，则判定失败
                    if(System.currentTimeMillis() > startDate+1000*60){
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("未知异常：", e.getCause());
                }
            } while (orderId.get() == null);
        };

        for (int i = 0; i < 20; i++) {
            service.submit(task);
        }

        service.shutdown();
        //等待线程结束
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            if(success.get()){
                mainFrame.appendMsg("抢购成功，请登录约苗小程序查看");
            }else{
                mainFrame.appendMsg("抢购失败");
            }
        } catch (InterruptedException e) {
            mainFrame.appendMsg("未知异常");
            logger.info("抢购失败:",e.getCause());
        }

    }
    public List<VaccineList> getVaccines() throws IOException, BusinessException {
        return httpService.getVaccineList();
    }
    /**
     *  将时间字符串转换为时间戳
     * @param dateStr yyyy-mm-dd格式
     * @return
     */
    public long convertDateToInt(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateStr);
        return date.getTime();
    }
}
