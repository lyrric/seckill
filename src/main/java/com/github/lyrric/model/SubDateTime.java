package com.github.lyrric.model;

/**
 * 疫苗预约日期可选择的时间段
 */
public class SubDateTime {


    private String startTime;

    private String endTime;

    /**
     * 重要：参数传递值
     */
    private String wid;

    private Integer maxSub;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Integer getMaxSub() {
        return maxSub;
    }

    public void setMaxSub(Integer maxSub) {
        this.maxSub = maxSub;
    }
}
