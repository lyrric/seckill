package com.github.lyrric.service;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineList;
import com.github.lyrric.ui.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    ExecutorService service = Executors.newFixedThreadPool(20);

    public SecKillService() {
        httpService = new HttpService();
    }

    /**
     * 多线程秒杀开启
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void startSecKill(Integer vaccineId, String startDateStr, MainFrame mainFrame) throws ParseException, InterruptedException {
        long startDate = convertDateToInt(startDateStr);

        AtomicBoolean success = new AtomicBoolean(false);
        long now = System.currentTimeMillis();
        if(now + 2000 < startDate){
            logger.info("还未到开始时间，等待中......");
            Thread.sleep(startDate - now - 2000);
        }
        AtomicReference<String> orderId = new AtomicReference<>();
        AtomicReference<String> st = new AtomicReference<>();
        while (true){
            //获取服务器时间戳接口，计算加密用
            try {
                st.set(httpService.getSt(vaccineId.toString()));
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(now + 500 < startDate){
            logger.info("还未到开始时间，等待中......");
            Thread.sleep(startDate - now - 500);
        }
        Runnable runnable = ()->{
            do {
                try {
                    //1.直接秒杀、获取秒杀资格
                    long id = Thread.currentThread().getId();
                    logger.info("Thread ID：{}，发送请求", id);
                    orderId.set(httpService.secKill(vaccineId.toString(), "1", Config.memberId.toString(),
                            Config.idCard, st.get()));
                    success.set(true);
                    logger.info("Thread ID：{}，抢购成功", id);
                    break;
                } catch (BusinessException e) {
                    logger.info("Thread ID: {}, 抢购失败: {}",Thread.currentThread().getId(), e.getErrMsg());
                    //如果离开始时间120秒后，或者已经成功抢到则不再继续
                    if (System.currentTimeMillis() > startDate + 1000 * 60 * 2 || success.get()) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("Thread ID: {}，未知异常", Thread.currentThread().getId());
                }
            } while (orderId.get() == null);
        };

        for (int i = 0; i < 1; i++) {
            service.submit(runnable);
        }
        service.shutdown();

        //等待线程结束
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            if (success.get()) {
                if (mainFrame != null) {
                    mainFrame.appendMsg("抢购成功，请登录约苗小程序查看");
                }
                logger.info("抢购成功，请登录约苗小程序查看");
            } else {
                if (mainFrame != null) {
                    mainFrame.appendMsg("抢购失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mainFrame != null) {
                mainFrame.setStartBtnEnable();
            }
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
