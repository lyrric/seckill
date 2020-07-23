package com.github.lyrric.model;

/**
 * Created on 2020-07-23.
 *
 * @author wangxiaodong
 */
public class Member {

    private Integer id;

    private Integer userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号码
     */
    private String idCardNo;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别
     */
    private Integer sex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
