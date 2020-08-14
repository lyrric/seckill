package com.github.lyrric.util;

import com.github.lyrric.conf.Config;
import com.github.lyrric.service.HttpService;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 2020-07-23.
 *
 * @author wangxiaodong
 */
public class ParseUtil {

    public static String[] parseHeader(String reqHeader){
        if(StringUtils.isEmpty(reqHeader)){
            return null;
        }
        String[] data = new String[2];
        reqHeader = reqHeader.replaceAll("cookie: ", "Cookie: ");
//        reqHeader = reqHeader.replaceAll("\n", "");
//        reqHeader = reqHeader.replaceAll("Host:", "");
        int start = reqHeader.indexOf("tk: ");
        int end = reqHeader.indexOf("\n", start);
        if(start == -1 || end == -1){
            return null;
        }
        data[0]  = reqHeader.substring(start+"tk: ".length(), end);
        start = reqHeader.indexOf("Cookie: ");
        end = reqHeader.indexOf("\n", start);
        if(start == -1 || end == -1){
            return null;
        }
       data[1]  = reqHeader.substring(start+"Cookie: ".length(), end);

        return data;
    }

}
