package com.github.lyrric.ui;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.Member;
import com.github.lyrric.model.VaccineList;
import com.github.lyrric.service.HttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created on 2020-08-14.
 * 控制台模式
 * @author wangxiaodong
 */
public class ConsoleMode {

    private final Logger log = LogManager.getLogger(ConsoleMode.class);

    private ExecutorService service = Executors.newFixedThreadPool(100);

    private HttpService httpService = new HttpService();

    public void start() throws IOException, ParseException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        log.info("请输入tk：");
        Config.tk = sc.nextLine().trim();
        log.info("请输入Cookie：");
        Config.cookies = sc.nextLine().trim();
        log.info("获取接种人员......");
        List<Member> members = httpService.getMembers();
        for (int i = 0; i < members.size(); i++) {
            log.info("{}-{}-{}", i, members.get(i).getName(), members.get(i).getIdCardNo());
        }
        log.info("请输入接种人员序号：");
        int no = Integer.parseInt(sc.nextLine());
        Config.memberId = members.get(no).getId();
        Config.idCard = members.get(no).getIdCardNo();

        log.info("获取疫苗列表......");
        List<VaccineList> vaccineList = httpService.getVaccineList();
        for (int i = 0; i < vaccineList.size(); i++) {
            VaccineList item = vaccineList.get(i);
            log.info("{}-{}-{}-{}-{}", i, item.getName(), item.getVaccineName(), item.getAddress(), item.getStartTime());
        }
        log.info("请输入疫苗序号：");
        no = Integer.parseInt(sc.nextLine());
        int code = vaccineList.get(no).getId();
        String startTime = vaccineList.get(no).getStartTime();
        log.info("按回车键开始秒杀：");
        sc.nextLine();
        secKill(code, startTime);
    }

    public void secKill(Integer vaccineId, String startDateStr) throws ParseException, InterruptedException {
        long startDate = convertDateToInt(startDateStr);

        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> orderId = new AtomicReference<>(null);
        Runnable task = ()-> {
            do {
                try {
                    //1.直接秒杀、获取秒杀资格
                    long id = Thread.currentThread().getId();
                    log.info("Thread ID：{}，发送请求", id);
                    orderId.set(httpService.secKill(vaccineId.toString(), "1", Config.memberId.toString(), Config.idCard));
                    success.set(true);
                    log.info("Thread ID：{}，抢购成功", id);
                } catch (BusinessException e) {
                    log.info("Thread ID: {}, 抢购失败: {}",Thread.currentThread().getId(), e.getErrMsg());
                    //如果离开始时间30秒后，或者已经成功抢到则不再继续
                    if(System.currentTimeMillis() > startDate+1000*30 || success.get()){
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("Thread ID: {}，未知异常", Thread.currentThread().getId());
                }
            } while (orderId.get() == null);
        };
        long now = System.currentTimeMillis();
        if(now + 1000 < startDate){
            log.info("还未到开始时间，等待中......");
            Thread.sleep(startDate-now-1000);
        }
        //如何保证能在秒杀时间点瞬间并发？

        //提前200毫秒开始秒杀
        do {
            now = System.currentTimeMillis();
        }while (now + 200 < startDate);
        log.info("###########第一波 开始秒杀###########");
        for (int i = 0; i < 10; i++) {
            service.submit(task);
        }

        //准点（提前20毫秒）秒杀
        do {
            now = System.currentTimeMillis();
        }while (now + 20 < startDate);
        log.info("###########第二波 开始秒杀###########");
        for (int i = 0; i < 20; i++) {
            service.submit(task);
        }
        service.shutdown();
        //等待线程结束
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            if(success.get()){
                log.info("抢购成功，请登录约苗小程序查看");
            }else{
                log.info("抢购失败");
            }
        } catch (InterruptedException e) {
            log.info("抢购失败:",e.getCause());
        }
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
