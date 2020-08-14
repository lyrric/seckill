package com.github.lyrric.model;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
public class BusinessException extends RuntimeException {

    private String code;

    private String errMsg;

    public BusinessException(String code , String  message) {
        super(message);
        this.code = code;
        this.errMsg = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
