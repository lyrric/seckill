package com.github.lyrric.service;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineDetail;
import com.github.lyrric.model.VaccineList;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
public class SecKillService {

    private HttpService httpService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public SecKillService() {
        httpService = new HttpService();
    }

    /**
     * 多线程秒杀开启
     * @param vcode
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public boolean startSecKill(String vcode, Integer id) {
        AtomicBoolean success = new AtomicBoolean(false);
        Runnable task = ()->{
            try {
                //1.获取疫苗信息
                VaccineDetail vaccineDetail = null;
                do {
                    try {
                        vaccineDetail = httpService.getVaccineDetail(id);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }while (vaccineDetail == null);
                //2.加密time串
                Long time = vaccineDetail.getTime();
                String str = time + "fuckhacker10000times";
                MessageDigest md5 = MessageDigest.getInstance("md5");
                String sign =  new BigInteger(1, md5.digest(str.getBytes(StandardCharsets.UTF_8))).toString(16);
                List<VaccineDetail.Day> days = vaccineDetail.getDays();
                //3.并发请求秒杀，此请求受验证码影响，最多只会成功一次
                days.forEach(day -> {
                    new Thread(()->{
                        try {
                            httpService.secKill(id.toString(), "1", Config.memberId.toString(), formatDate(day.getDay()), sign, vcode);
                            success.set(true);
                            System.out.println("预约成功！");
                        } catch (BusinessException e) {
                            System.out.println("失败:"+e.getErrMsg());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        //等待线程结束
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return success.get();
    }


    public String getCapture() throws IOException, BusinessException {
        return httpService.getCapture();
    }

    public List<VaccineList> getVaccines() throws IOException, BusinessException {
        return httpService.getVaccineList();
    }
    /**
     * 将19981231变成1998-12-31
     * @param date
     * @return
     */
    public String formatDate(String date){
        return date.substring(0,3)+"-"+date.substring(3,5)+"-"+date.substring(6,8);
    }

}
