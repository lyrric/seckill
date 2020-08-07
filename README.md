# 约苗秒杀（单用户版）

## 更新日志
- 2019.08.07小程序版秒杀V1.0，基本测试99.99%（如果有BUG，那就是那0.01%）
- 2019.08.07小程序版秒杀V0.9，未测试，测试后转V1.0
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

前提：需要会用fiddler抓微信小程序的包，具体操作自信搜索

### 具体步骤
1. 抓取微信小程序的包header,格式大概如下：
```
GET https://miaomiao.scmttec.com/seckill/seckill/list.do?offset=0&limit=10&regionCode=5101 HTTP/1.1
Host: miaomiao.scmttec.com
Connection: keep-alive
accept: application/json, text/plain, */*
tk: wxapptoken:10:56ce0d6ad845798561edd70a30d200d2207
cookie: _xzkj_=wxapptoken:10:56ce0db70a30dD
charset: utf-8
x-requested-with: XMLHttpRequest
content-type: application/json
User-Agent: Mozilla/5.0 (Linux; Android 5.1.1; OPPO R17 Pro Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36 MMWEBID/1042 MicroMessenger/7.0.17.1720(0x27001134) Process/appbrand0 WeChat/arm32 NetType/WIFI Language/zh_CN ABI/arm32
Accept-Encoding: gzip,compress,br,deflate
Referer: https://servicewechat.com/wxff8cad2e9bf18719/4/page-frame.html
```
> 注意，这些并不是都是必须的，其实只需要包括tk、Cookie开头的的两行即可

2. 运行程序，点击设置Cookie，输入上一步抓取的包，点击验证并保存
3. 点击设置成员，选中列表中的成员，点击确定(需要提前在约苗中填写)
4. 点击刷新疫苗列表，即可在列表中看到秒杀列表
5. 在秒杀开始的前一段时间（要保证cookie在有效期内），点击开始即可，程序会在距开始时间五秒内启动并发秒杀

### 注意事项

1. 并未测试小程序的Cookie的过期时间，大概是一个小时到两个小时之间
2. 随机提交预约时间和时间段（并不清楚获取到秒杀资格后， 还是否可以手动在约苗小程序里面选择预约时间）
