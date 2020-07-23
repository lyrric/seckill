package com.github.lyrric.service;

import com.alibaba.fastjson.JSONObject;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineDetail;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2020-07-22.
 *
 * @author wangxiaodong
 */
public class HttpService {

    private HttpClient httpClient;

    private String baseUrl = "https://wx.healthych.com";
    //private String baseUrl = "https://www.fastmock.site/mock/17f698e18c3482ac4ae855122cedcb34/wx";
    private String cookies;
    private String st;
    private String tk;

    public HttpService(String cookies, String st, String tk) {
        this.cookies = cookies;
        this.st = st;
        this.tk = tk;

//        HttpHost proxy = new HttpHost("127.0.0.1",8888);
//        httpClient = HttpClients.custom().setProxy(proxy).build();
       httpClient = HttpClients.createDefault();
    }

    /**
     * 获取疫苗信息
     */
    public VaccineDetail getVaccineDetail(Integer id) throws IOException, BusinessException {
        String path = baseUrl + "/seckill/vaccine/detailVo.do?id="+id.toString();
        String s = get(path, new HashMap<>());
        System.out.println(s);
        return JSONObject.parseObject(s, VaccineDetail.class);
    }

    /**
     * 获取验证码
     */
    public String getCapture() throws IOException, BusinessException {
        String path = baseUrl+"/seckill/validateCode/vcode.do";
        return get(path, null);
    }

    /**
     * 秒杀
     */
    public void secKill(String departmentVaccineId, String vaccineIndex, String linkmanId, String subscribeDate, String sign, String vcode) throws IOException, BusinessException {
        String path = baseUrl+"/seckill/vaccine/subscribe.do";
        Map<String, String> params = new HashMap<>();
        params.put("departmentVaccineId", departmentVaccineId);
        params.put("vaccineIndex", vaccineIndex);
        params.put("linkmanId", linkmanId);
        params.put("subscribeDate", subscribeDate);
        params.put("sign", sign);
        params.put("vcode", vcode);
        String s = get(path, params);
    }


    private String get(String path, Map<String, String> params) throws IOException, BusinessException {
        if(params != null && params.size() !=0){
            StringBuilder paramStr = new StringBuilder("?");
            params.forEach((key,value)->{
                paramStr.append(key).append("=").append(value).append("&");
            });
            path = path+paramStr.toString();
        }
        HttpGet get = new HttpGet(path);
        get.setHeaders(getCommonHeader());
        HttpEntity httpEntity = httpClient.execute(get).getEntity();
        String json =  EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if("0000".equals(jsonObject.get("code"))){
            return jsonObject.getString("data");
        }else{
            throw new BusinessException(jsonObject.getString("code"), jsonObject.getString("msg"));
        }
    }
    private Header[] getCommonHeader(){
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1295.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2875.116 Safari/537.36 NetType/WIFI MicroMessenger/7.0.5 WindowsWechat"));
        headers.add(new BasicHeader("Referer", "https://wx.healthych.com/index.html"));
        headers.add(new BasicHeader("st", st));
        headers.add(new BasicHeader("tk", tk));
        headers.add(new BasicHeader("Accept","application/json, text/plain, */*"));
        headers.add(new BasicHeader("Host","wx.healthych.com"));
        headers.add(new BasicHeader("Cookie",cookies));

        return headers.toArray(new Header[0]);
    }

}
