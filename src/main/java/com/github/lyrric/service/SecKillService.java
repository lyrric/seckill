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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
public class SecKillService {

    private HttpService httpService;


    public SecKillService() {
        httpService = new HttpService();
    }

    /**
     * 多线程秒杀开启
     * @param vcode
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void startSecKill(String vcode, Integer id) {
        AtomicReference<VaccineDetail> vaccineDetail = new AtomicReference<>();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                //1.获取疫苗信息
                do {
                    try {
                        vaccineDetail.set(httpService.getVaccineDetail(id));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BusinessException e) {
                        System.out.println(e.getErrMsg());
                    }
                } while (vaccineDetail.get() == null);
                //2.加密time串
                Long time = vaccineDetail.get().getTime();
                String str = time + "fuckhacker10000times";
                try {
                    MessageDigest md5 = MessageDigest.getInstance("md5");
                    String sign =  new BigInteger(1, md5.digest(str.getBytes(StandardCharsets.UTF_8))).toString(16);
                    List<VaccineDetail.Day> days = vaccineDetail.get().getDays();
                    //3.并发请求秒杀，此请求受验证码影响，最多只会成功一次
                    days.forEach(day -> {
                        new Thread(()->{
                            for (int j = 0; j < 2; j++) {
                                try {
                                    httpService.secKill(id.toString(), "1", Config.memberId.toString(), formatDate(day.getDay()), sign, vcode);
                                    System.out.println("预约成功！");
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (BusinessException e) {
                                    System.out.println("失败:"+e.getErrMsg());
                                }
                            }
                        }).start();
                    });
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }).start();
        }

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
