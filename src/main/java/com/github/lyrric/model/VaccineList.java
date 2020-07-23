package com.github.lyrric.model;

import java.util.List;

/**
 * Created on 2020-07-23.
 * 疫苗列表
 * @author wangxiaodong
 */
public class VaccineList {

    private String code;
    /**
     * 医院名称
     */
    private String name;
    /**
     * 医院地址
     */
    private String address;
    /**
     * 疫苗信息（一般就一条记录）
     */
    private List<Vaccine> vaccines;

    /**
     * 疫苗信息
     */
    public static class Vaccine{
        private String code;
        /**
         * 疫苗名称
         */
        private String name;
        /**
         * 疫苗ID
         */
        private Integer id;
        /**
         * 开抢时间
         */
        private String subDateStart;
        /**
         * 是否是秒杀？
         */
        private Integer isSeckill;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSubDateStart() {
            return subDateStart;
        }

        public void setSubDateStart(String subDateStart) {
            this.subDateStart = subDateStart;
        }

        public Integer getIsSeckill() {
            return isSeckill;
        }

        public void setIsSeckill(Integer isSeckill) {
            this.isSeckill = isSeckill;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }
}
