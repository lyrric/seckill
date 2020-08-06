package com.github.lyrric.model;

/**
 * 预约日期
 */
public class SubDate {

    /**
     * 目测为YYYY-MM-DD格式
     */
    private String day;
    /**
     * 当日还可以预约的数量
     */
    private String total;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
