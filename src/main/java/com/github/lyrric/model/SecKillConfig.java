package com.github.lyrric.model;

import java.util.List;

/**
 * Created on 2020-07-23.
 * 秒杀配置
 * @author wangxiaodong
 */
public class SecKillConfig {

    /**
     * 名称、便于区别
     */
    private String name;
    /**
     * 微信配置
     */
    public static String cookies = "_xzkj_=wxtoken:e0963da68ba2c1df6fe0_b9406e1b";
    public static String st = "eaa880ba74ea50";
    public static String tk = "wxtoken:e0963da6b3e3e8b";

    /**
     * fiddler抓包的header，用于解析cookies、st、tk三个参数
     * 示例： 只需要包含这三行即可
     * tk: wxtoken:e0963da6b3e544f4613e8ba2c1df6fe0_58aba4165f9a3
     * st: 3cbdc887b4e2585bbb888fb3
     * Cookie: _xzkj_=wxtoken:e0963da6b3e5441df6fe0_5258071785d9c98b258165f9a3; _xxhm_=%7B%22address%22%3A%22%22%2C%22awardPoinbirthday%22%3A835545600000%2C%22creat%2C%22headerImg%22%3A%22http%3A%2F%2Fthirdwx.qlogo.cn%2Fmmopen%2FdH8QVxmk2IXORh7FiapbUSZd3qotRsSWktOKjqSI3ibtrP1u6Zf3PQqc84b8PGcHibW76M6zLmnosib9KQeaeYzvSrCliaKAXEXcq%2F132%22%2C%22id%22%3A3926372%2C%22idCardNo%22%3A%22510727199606244528%22%2C%22isRegisterHistory%22%3A0%2C%22latitude%22%3A30.587389%2C%22longitude%22%3A104.06224%2C%22mobile%22%3A%2218608283793%22%2C%22modifyTime%22%3A1593757208000%2C%22name%22%3A%22%E9%99%88%E6%9F%B3%E9%9D%92%22%2C%22nickName%22%3A%22lyrric%22%2C%22openId%22%3A%22oWzsq52mreJ9_E_f2R0QSvwlQl8M%22%2C%22regionCode%22%3A%22510107%22%2C%22registerTime%22%3A1593757208000%2C%22sex%22%3A2%2C%22source%22%3A1%2C%22uFrom%22%3A%22cdbdbsy%22%2C%22unionid%22%3A%22oiGJM6PFEuP1AJ1jmx91bbcjBzmY%22%2C%22wxSubscribed%22%3A1%2C%22yn%22%3A1%7D; UM_distinctid=1737a2b9-0a543d74-1fa400-1737ab175141d; CNZZDATA1261985103=16539089838-https%253A%252F%252Fopen.weixin.qq.com%252F%789838
     * */
    public static String reqHeader = "";

    /**
     * 接种人信息
     */
    private List<Member> members;

    /**
     * 选择的接种人ID（抢疫苗用的）
     */
    private Integer memberId;


}
