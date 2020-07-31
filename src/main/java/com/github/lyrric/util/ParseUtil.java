package com.github.lyrric.util;

import com.github.lyrric.conf.Config;
import com.github.lyrric.service.HttpService;

/**
 * Created on 2020-07-23.
 *
 * @author wangxiaodong
 */
public class ParseUtil {

    public static boolean parseHeader(String reqHeader){
        Config.reqHeader = reqHeader;
        reqHeader = reqHeader.replaceAll(" ", "");
        reqHeader = reqHeader.replaceAll("\n", "");
        reqHeader = reqHeader.replaceAll("Host:", "");
        int start = reqHeader.indexOf("tk:");
        int end = reqHeader.indexOf("User-Agent", start);
        if(start == -1 || end == -1){
            return false;
        }
        Config.tk  = reqHeader.substring(start+"tk:".length(), end);
        start = reqHeader.indexOf("st:");
        end = reqHeader.indexOf("Referer:", start);
        if(start == -1 || end == -1){
            return false;
        }
        Config.st  = reqHeader.substring(start+"st:".length(), end);
        start = reqHeader.indexOf("Cookie:");
        if(start == -1){
            return false;
        }
        Config.cookies  = reqHeader.substring(start+"Cookie:".length());
        try {
            new HttpService().getCapture();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
