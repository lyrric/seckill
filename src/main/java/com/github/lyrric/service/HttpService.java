package com.github.lyrric.service;

import com.alibaba.fastjson.JSONObject;
import com.github.lyrric.conf.Config;
import com.github.lyrric.model.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private String baseUrl = "https://miaomiao.scmttec.com";

    private final Logger logger = LogManager.getLogger(HttpService.class);


    /***
     * 获取秒杀资格
     * @param seckillId 疫苗ID
     * @param vaccineIndex 固定1
     * @param linkmanId 接种人ID
     * @param idCard 接种人身份证号码
     * @return 返回订单ID
     * @throws IOException
     * @throws BusinessException
     */
    public String secKill(String seckillId, String vaccineIndex, String linkmanId, String idCard, String st) throws IOException, BusinessException {
        String path = baseUrl+"/seckill/seckill/subscribe.do";
        Map<String, String> params = new HashMap<>();
        params.put("seckillId", seckillId);
        params.put("vaccineIndex", vaccineIndex);
        params.put("linkmanId", linkmanId);
        params.put("idCardNo", idCard);
        //加密参数
        Header header = new BasicHeader("ecc-hs", eccHs(seckillId, st));
        return get(path, params, header);
    }

    /**
     * 获取疫苗列表
     * @return
     * @throws BusinessException
     */
    public List<VaccineList> getVaccineList() throws BusinessException, IOException {
        hasAvailableConfig();
        String path = baseUrl+"/seckill/seckill/list.do";
        Map<String, String> param = new HashMap<>();
        //九价疫苗的code
        param.put("offset", "0");
        param.put("limit", "100");
        //这个应该是成都的行政区划前四位
        param.put("regionCode", Config.regionCode);
        String json = get(path, param, null);
        return JSONObject.parseArray(json).toJavaList(VaccineList.class);
    }


    /**
     * 获取接种人信息
     * @return
     */
    public List<Member> getMembers() throws IOException, BusinessException {
        String path = baseUrl + "/seckill/linkman/findByUserId.do";
        String json = get(path, null, null);
        return  JSONObject.parseArray(json, Member.class);
    }
    /***
     * 获取加密参数st
     * @param vaccineId 疫苗ID
     */
    public String getSt(String vaccineId) throws IOException {
        String path = baseUrl+"/seckill/seckill/checkstock2.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
        String json =  get(path, params, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getJSONObject("data").getString("st");
    }
    /***
     * log接口，不知道有何作用，也许调用这个接口后，服务端做了什么处理也未可知
     * @param vaccineId 疫苗ID
     */
    public void log(String vaccineId) throws IOException {
        String path = baseUrl+"/seckill/seckill/log.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
        get(path, params, null);
    }

    private void hasAvailableConfig() throws BusinessException {
        if(StringUtils.isEmpty(Config.cookies)){
            throw new BusinessException("0", "请先配置cookie");
        }
    }

    private String get(String path, Map<String, String> params, Header extHeader) throws IOException, BusinessException {
        if(params != null && params.size() !=0){
            StringBuilder paramStr = new StringBuilder("?");
            params.forEach((key,value)->{
                paramStr.append(key).append("=").append(value).append("&");
            });
            String t = paramStr.toString();
            if(t.endsWith("&")){
                t = t.substring(0, t.length()-1);
            }
            path+=t;
        }
        HttpGet get = new HttpGet(path);
        List<Header> headers = getCommonHeader();
        if(extHeader != null){
            headers.add(extHeader);
        }
        get.setHeaders(headers.toArray(new Header[0]));
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

    private List<Header>getCommonHeader(){
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; SM-N960F Build/JLS36C; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36 MMWEBID/1042 MicroMessenger/7.0.15.1680(0x27000F34) Process/appbrand0 WeChat/arm32 NetType/WIFI Language/zh_CN ABI/arm32"));
        headers.add(new BasicHeader("Referer", "https://servicewechat.com/wxff8cad2e9bf18719/2/page-frame.html"));
        headers.add(new BasicHeader("tk", Config.tk));
        headers.add(new BasicHeader("Accept","application/json, text/plain, */*"));
        headers.add(new BasicHeader("Host","miaomiao.scmttec.com"));
        headers.add(new BasicHeader("Cookie",Config.cookies));
        return headers;
    }

    private String eccHs(String seckillId, String st){
        String salt = "ux$ad70*b";
        final Integer memberId = Config.memberId;
        String md5 = DigestUtils.md5Hex(seckillId + memberId + st);
        return DigestUtils.md5Hex(md5 + salt);
    }


    public static void main(String[] args) {
        String salt = "ux$ad70*b";
        final Integer memberId = Config.memberId;
        //7053c40d6ffd5abddeac7ced36c51e60
        String st = "1";
        String md5 = DigestUtils.md5Hex("976" + "10036065" + st);
        System.out.println(md5);
        System.out.println(DigestUtils.md5Hex(md5 + salt));
    }



    /***
     * 获取接种日期
     * @param vaccineId 疫苗ID
     * @param orderId 订单ID
     */
    public List<SubDate> getSkSubDays(String vaccineId, String orderId) throws IOException, BusinessException {
        String path = baseUrl+"/seckill/seckill/subscribeDays.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
        params.put("sid", orderId);
        String json =  get(path, params, null);
        logger.info("日期格式:{}", json);
        return JSONObject.parseArray(json).toJavaList(SubDate.class);
    }

    /**
     * 根据接种日期，获取接种时间段
     * @param vaccineId
     * @param orderId
     * @param day 接种日期 YYYY-MM-DD
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    public List<SubDateTime> getSkSubDayTime(String vaccineId, String orderId, String day) throws IOException, BusinessException {
        String path = baseUrl+"/seckill/seckill/dayTimes.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
        params.put("sid", orderId);
        params.put("day", day);
        String json = get(path, params, null);
        System.out.println("根据选择的日期，获取的时间格式"+json);
        return JSONObject.parseArray(json).toJavaList(SubDateTime.class);
    }

    /**
     * 提交接种时间
     * @param vaccineId
     * @param orderId
     * @param day 接种日期 YYYY-MM-DD
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    public void subDayTime(String vaccineId, String orderId, String day, String wid) throws IOException, BusinessException {
        String path = baseUrl+"/seckill/seckill/submitDateTime.do";
        Map<String, String> params = new HashMap<>();
        params.put("id", vaccineId);
        params.put("sid", orderId);
        params.put("day", day);
        params.put("wid", wid);
        String json =  get(path, params, null);
        logger.info("提交接种时间，返回数据: {}", json);
    }
}
