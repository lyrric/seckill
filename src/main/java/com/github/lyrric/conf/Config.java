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
    public static String tk = "";

    public static String reqHeader = "GET https://miaomiao.scmttec.com/seckill/seckill/list.do?offset=0&limit=10&regionCode=5101 HTTP/1.1\n" +
            "Host: miaomiao.scmttec.com\n" +
            "Connection: keep-alive\n" +
            "accept: application/json, text/plain, */*\n" +
            "tk: wxapptoken:10:56ce0d6ad845798561edd50a30d200d2207\n" +
            "cookie: _xzkj_=wxapptoken:10:56ce0d6ad845798561edd5c\n" +
            "charset: utf-8\n" +
            "x-requested-with: XMLHttpRequest\n" +
            "content-type: application/json\n" +
            "User-Agent: Mozilla/5.0 (Linux; Android 5.1.1; OPPO R17 Pro Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36 MMWEBID/1042 MicroMessenger/7.0.17.1720(0x27001134) Process/appbrand0 WeChat/arm32 NetType/WIFI Language/zh_CN ABI/arm32\n" +
            "Accept-Encoding: gzip,compress,br,deflate\n" +
            "Referer: https://servicewechat.com/wxff8cad2e9bf18719/4/page-frame.html\n" +
            "\n";

    /**
     * 接种成员ID
     */
    public static Integer memberId;
    /**
     * 接种成员身份证号码
     */
    public static String idCard;
}
