package com.github.lyrric.service;

import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineDetail;

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


    public SecKillService(String cookies, String st, String tk) {
        httpService = new HttpService(cookies, st, tk);
    }

    /**
     * 多线程秒杀开启
     * @param vcode
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void startSecKill(String vcode, Integer id) {
        AtomicReference<VaccineDetail> vaccineDetail = null;
        for (int i = 0; i < 10; i++) {
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
                MessageDigest md5 = null;
                try {
                    md5 = MessageDigest.getInstance("md5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                String sign =  new BigInteger(1, md5.digest(str.getBytes(StandardCharsets.UTF_8))).toString(16);
                //3.排序可预约日期,按照total从高到低排序
                List<VaccineDetail.Day> days = vaccineDetail.get().getDays().stream().filter(t -> t.getTotal() != 0).sorted(Comparator.comparing(VaccineDetail.Day::getTotal).reversed()).collect(Collectors.toList());
                //4.并发请求秒杀
                days.forEach(day -> {
                    new Thread(()->{
                        for (int j = 0; j < 10; j++) {
                            try {
                                httpService.secKill(id.toString(), "1", "1936032", formatDate(day.getDay()), sign, vcode);
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
            }).start();

        }


    }

    public void testSecKill(String vcode) throws IOException, BusinessException, NoSuchAlgorithmException {
        String departmentVaccineId = "3178";
        String vaccineIndex = "1";
        String linkmanId = "1936032";
        String subscribeDate = "2020-07-24";
        String sign = new BigInteger(1, MessageDigest.getInstance("md5").digest("1595311230242fuckhacker10000times".getBytes(StandardCharsets.UTF_8))).toString(16);
        System.out.println(sign);
        httpService.secKill(departmentVaccineId, vaccineIndex, linkmanId, subscribeDate, sign, vcode);
    }

    public String getCapture() throws IOException, BusinessException {
        return httpService.getCapture();
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
