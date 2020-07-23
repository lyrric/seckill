package com.github.lyrric.model;

import java.util.List;

/**
 * Created on 2020-07-22.
 * 疫苗信息
 * @author wangxiaodong
 */
public class VaccineDetail {

    /**
     * 预约日期列表
     */
    private List<Day> days;

    /**
     * 时间戳，解密会用到
     */
    private Long time;

    /**
     * 开始抢购时间
     */
    private Long startMilliscond;

    /**
     * 医院地址
     */
    private String hospitalName;

    public static class Day{
        /**
         * 预约日期
         */
        private String day;
        /**
         * 预约日期剩余数量
         */
        private Integer total;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Long getStartMilliscond() {
        return startMilliscond;
    }

    public void setStartMilliscond(Long startMilliscond) {
        this.startMilliscond = startMilliscond;
    }
}
