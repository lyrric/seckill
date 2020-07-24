package com.github.lyrric.conf;

import com.github.lyrric.model.Member;

/**
 * Created on 2020-07-23.
 *
 * @author wangxiaodong
 */
public class Config {

    /**
     * 微信配置
     */
    public static String cookies = "";
    public static String st = "";
    public static String tk = "";

    public static String reqHeader = "GET https://wx.healthych.com/order/linkman/findByUserId.do HTTP/1.1\n" +
            "Host: wx.healthych.com\n" +
            "Connection: keep-alive\n" +
            "Accept: application/json, text/plain, */*\n" +
            "tk: wxtoken:e0963da6b3e544ee2996a3273\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1295.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2875.116 Safari/537.36 NetType/WIFI MicroMessenger/7.0.5 WindowsWechat\n" +
            "st: 3ce5985752bc1a0a1\n" +
            "Referer: https://wx.healthych.com/index.html\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.5;q=0.4\n" +
            "Cookie: _xzkj_=wxtoken:e0963da6b22%2" +
            "\n";

    /**
     * 接种成员ID
     */
    public static Integer memberId;

}
