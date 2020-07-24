package com.github.lyrric.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineDetail;
import com.github.lyrric.model.VaccineList;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

    private HttpClient httpClient;

    private String baseUrl = "https://wx.healthych.com";

    public HttpService(){
//        HttpHost proxy = new HttpHost("127.0.0.1",8888);
//        httpClient = HttpClients.custom().setProxy(proxy).build();
       httpClient = HttpClients.createDefault();
    }

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

    /**
     * 识别图形验证码
     * @return
     */
    public String analyseCode(String imageBase64) throws IOException, BusinessException {
        imageBase64 = "data:image/png;base64,"+imageBase64;
        HttpPost post = new HttpPost("http://apigateway.jianjiaoshuju.com/api/v_1/fzyzm.html");

        post.setHeader("appcode", Config.appCode);
        post.setHeader("appKey", Config.appKey);
        post.setHeader("appSecret", Config.appSecret);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("v_pic", imageBase64));
        nameValuePairList.add(new BasicNameValuePair("v_type", "js"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
        formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        post.setEntity(formEntity);
        HttpEntity entity = httpClient.execute(post).getEntity();
        String result = EntityUtils.toString(entity);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(jsonObject.getInteger("errCode") == 0){
            return jsonObject.getString("v_code");
        }else{
            throw new BusinessException(jsonObject.getString("errCode"), jsonObject.getString("msg"));
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

        //{"offset":0,"end":1,"total":2,"limit":10,"pageNumber":1,"pageListSize":9,"pageNumList":[1],"rows":[{"code":"5101080912","name":"成都市成华区跳蹬河社区卫生服务中心","imgUrl":"https://adultvacc-1253522668.file.myqcloud.com/thematic%20pic/%E6%88%90%E9%83%BD%E5%B8%82%E6%88%90%E5%8D%8E%E5%8C%BA%E8%B7%B3%E8%B9%AC%E6%B2%B3%E7%A4%BE%E5%8C%BA%E5%8D%AB%E7%94%9F%E6%9C%8D%E5%8A%A1%E4%B8%AD%E5%BF%83%E5%AE%A3%E4%BC%A0%E5%9B%BE_1591085056981.png","address":"崔家店路45号","worktimeDesc":"周一至周五 上午8：30-11：30","total":0,"isSeckill":1,"depaCodes":[],"vaccines":[{"code":"8803","name":"九价HPV疫苗（进口）","id":5352,"subDateStart":"2020-07-23 14:00:00","isSeckill":1}]},{"code":"5101080905","name":"成都市成华区妇幼保健院","imgUrl":"https://adultvacc-1253522668.file.myqcloud.com/thematic%20pic/1_1510664490902.jpg","address":"四川省成都市成华大道新鸿路6号","worktimeDesc":"周一至五上午8：30-11：00 下午14:30-16:30。","total":0,"isSeckill":1,"depaCodes":[],"vaccines":[{"code":"8803","name":"九价人乳头瘤病毒疫苗","id":6165,"subDateStart":"2020-07-26 15:00:00","isSeckill":1}]}],"pages":1}
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray rows = jsonObject.getJSONArray("rows");
        return rows.toJavaList(VaccineList.class);
    }

    private void hasAvailableConfig() throws BusinessException {
        if(StringUtils.isEmpty(Config.cookies)){
            throw new BusinessException("0", "请先配置cookie");
        }
    }
}
