package com.github.lyrric.model;

/**
 * Created on 2020-07-24.
 * 接种人信息
 * @author wangxiaodong
 */
public class Member {


    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idCardNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
}
