package com.github.lyrric.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.Member;
import com.github.lyrric.model.VaccineDetail;
import com.github.lyrric.model.VaccineList;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
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

    private String baseUrl = "https://wx.healthych.com";

    /**
     * 获取疫苗信息
     */
    public VaccineDetail getVaccineDetail(Integer id) throws IOException, BusinessException {
        hasAvailableConfig();
        String path = baseUrl + "/seckill/vaccine/detailVo.do?id="+id.toString();
        String s = get(path, new HashMap<>());
        System.out.println(s);
        return JSONObject.parseObject(s, VaccineDetail.class);
    }

    /**
     * 获取验证码
     */
    public String getCapture() throws IOException, BusinessException {
        hasAvailableConfig();
        String path = baseUrl+"/seckill/validateCode/vcode.do";
        return get(path, null);
    }

    /**
     * 开始秒杀
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

    /**
     * 获取疫苗列表
     * @return
     * @throws BusinessException
     */
    public List<VaccineList> getVaccineList() throws BusinessException, IOException {
        hasAvailableConfig();
        String path = baseUrl+"/seckill/department/pageList.do";
        Map<String, String> param = new HashMap<>();
        //九价疫苗的code
        param.put("vaccineCode", "8803");
        param.put("cityName", "");
        param.put("offset", "0");
        param.put("limit", "100");
        //这个应该是成都的行政区划前四位
        param.put("regionCode", "5101");
        param.put("isSeckill", "1");
        String json = get(path, param);

        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray rows = jsonObject.getJSONArray("rows");
        return rows.toJavaList(VaccineList.class);
    }

    /**
     * 获取接种人信息
     * @return
     */
    public List<Member> getMembers() throws IOException, BusinessException {
        String path = baseUrl + "/seckill/linkman/findByUserId.do";
        String json = get(path, null);
        return  JSONObject.parseArray(json, Member.class);
    }

    private void hasAvailableConfig() throws BusinessException {
        if(StringUtils.isEmpty(Config.cookies)){
            throw new BusinessException("0", "请先配置cookie");
        }
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
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
        headers.add(new BasicHeader("st", Config.st));
        headers.add(new BasicHeader("tk", Config.tk));
        headers.add(new BasicHeader("Accept","application/json, text/plain, */*"));
        headers.add(new BasicHeader("Host","wx.healthych.com"));
        headers.add(new BasicHeader("Cookie",Config.cookies));
        return headers.toArray(new Header[0]);
    }
}
