package com.github.lyrric.service;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.*;

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
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public boolean startSecKill(Integer vaccineId) {
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> orderId = new AtomicReference<>(null);
        //Runnable task = ()-> {
            do {
                try {
                    //1.直接秒杀、获取秒杀资格
                    orderId.set(httpService.secKill(vaccineId.toString(), "1", Config.memberId.toString(), Config.idCard));
                    //2.秒杀成功后，获取接种日期
                    List<SubDate> skSubDays = httpService.getSkSubDays(vaccineId.toString(), orderId.get());
                    skSubDays.forEach(day -> {
                        Runnable getTimeTask = () -> {
                            try {
                                //3.根据接种日期，获取接种时间段
                                List<SubDateTime> skSubDayTime = httpService.getSkSubDayTime(vaccineId.toString(), orderId.toString(), day.getDay());
                                skSubDayTime.forEach(time -> {
                                    //4.提交接种时间
                                    Runnable subDayTimeTask = () -> {
                                        try {
                                            httpService.subDayTime(vaccineId.toString(), orderId.get(), day.getDay(), time.getWid());
                                            success.set(true);
                                        } catch (IOException | BusinessException e) {
                                            e.printStackTrace();
                                        }
                                    };
                                    new Thread(subDayTimeTask).start();
                                });
                            } catch (IOException | BusinessException e) {
                                e.printStackTrace();
                            }
                        };
                        new Thread(getTimeTask).start();
                    });
                } catch (BusinessException e) {
                    System.out.println(e.getErrMsg());
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (orderId.get() == null);
        //};

/*        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        //等待线程结束
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return success.get();
    }


//    public String getCapture() throws IOException, BusinessException {
//        return httpService.getCapture();
//    }

    public List<VaccineList> getVaccines() throws IOException, BusinessException {
        return httpService.getVaccineList();
    }
    /**
     * 将19981231变成1998-12-31
     * @param date
     * @return
     */
//    public String formatDate(String date){
//        return date.substring(0,3)+"-"+date.substring(3,5)+"-"+date.substring(6,8);
//    }

}
