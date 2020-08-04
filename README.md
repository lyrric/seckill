# 约苗秒杀（单用户版）

## 更新日志
- 2019.08.04约苗秒杀功能转移至小程序，后期看能否抓包实现
- 2019.07.27更新至V1.0版本，优化了秒杀逻辑，加入秒杀结果提示
- 2019.07.24更新至V0.9版本，目前还差秒杀结果未展示
## 界面
### 主界面
![markdown](https://raw.githubusercontent.com/lyrric/seckill/master/images/main.jpg "主界面图")
### 设置抓包数据
![markdown](https://raw.githubusercontent.com/lyrric/seckill/master/images/header.jpg "设置抓包数据")
### 成员选择
![markdown](https://raw.githubusercontent.com/lyrric/seckill/master/images/mamber.jpg "成员选择")

## 描述

约苗的秒杀太坑了，每次秒杀区页面还没打开，就被抢光了，可恨！！！！

## 使用方法

前提：需要会用fiddler抓微信的包，具体操作自信搜索

### 具体步骤
1. 抓取微信的包,格式大概如下：
```
GET https://wx.healthych.com/order/linkman/findByUserId.do HTTP/1.1
Host: wx.healthych.com
Connection: keep-alive
Accept: application/json, text/plain, */*
tk: wxtoken:e0963da6b3e544f461ba1d0563ee2996a3273
User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1295.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2875.116 Safari/537.36 NetType/WIFI MicroMessenger/7.0.5 WindowsWechat
st: 3ce5982852a96b373005752bc1a0a1
Referer: https://wx.healthych.com/index.html
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.5;q=0.4
Cookie: _xzkj_=wxtoken:e0963da6b3ed0563ee2996a3273; _xxhm_=%7B%22address%22%3A%22%22%22awardPoints%22%3A0%2C%22birthday%22%3A835545600000%2C%22createTime%22%3A1574304016000%2C%2eaderImg%22%3A%22http%3A%2F%2Fthirdwx.qlogo.cn%2Fmmopen%2FdH8QVxmk2IXORh7FiapbUSZd3qotRsSW3ibtrP1u6Zf3PQqc84b8PGcHibW76M6zLmnoeYzvSrCliaKAXEXcq%2F132%22%2C%22id%22%3A3922%2C%22idCardNo%22%3A2510727199606244528%22%2C%22isRegisterHistory%22%3A0%2C%2latitude%22%3A30.58738%2C%22longitude%22%3A104.06224%2C%22mobile%22%3odifyTime%22%3A1593757208000%2C%22name%22%3A%22%E9%99%88%E6%9F%B3%E9%9D%92%22%2C%22nickName%22%3A22lyic%22%2C%22openId%22%3A%22oWzsq52mreJ9_E_f2R0QSvwlQl8M2%2C%22rnCod22%3A%22510107%22%%22registerTime%22%3A1593757208000%2C%22sex%22%3A2%2C%22source%22%3A1%2C%22uFrom%22%3A%22cdbdbsy%22%2C%%22%3A%22oiGJMFEuP1AJ1jm1bbcjBzmY%22%2C%22wxSubscribed%22%3A1%2C%22yn%22%3A1%7D; UM_distinctid=1737b7a0-0fb3beac3-8011274-1fa400-17377b7c7ad; CNZZDATA1261985103=697298094-1595230-https%253A%252F%252Fopen.weixin.qq.com%252F%7C1595571230
```
> 注意，这些并不是都是必须的，其实只需要包括tk、st、Cookie开头的的三行即可

2. 运行程序，点击设置Cookie，输入上一步抓取的包，点击验证并保存
3. 点击设置成员，选中列表中的成员，点击确定(需要提前在约苗中填写)
4. 点击刷新疫苗列表，即可在列表中看到秒杀列表
5. 在秒杀开始的前几分钟，获取图片验证码，并输入验证码，然后在秒杀前几十秒点击开始即可

### 注意事项

1. 并未测试微信的Cookie的过期时间，大概是一个小时到三个小时之间
2. 并未测试验证码的有效期，所以提前输入验证码时，不要提前太早导致验证码过期
zhi
