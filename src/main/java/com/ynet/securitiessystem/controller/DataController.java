package com.ynet.securitiessystem.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ynet.securitiessystem.httpclientutil.HttpClientUtil;
import com.ynet.securitiessystem.model.Bond;
import com.ynet.securitiessystem.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/data/")
public class DataController {

    @Value("${spring.data.queryDataUrl}")
    private String queryDataUrl;

    @Autowired
    private  RedisUtil redisUtil;



    @GetMapping("queryProduct.do")
    public List<Bond> queryProduct(String page, String num){
        Map<String,String> map = new HashMap<String, String>();
        map.put("page",page);
        map.put("num",num);
        String data = HttpClientUtil.doGet(queryDataUrl,map);
        List<Bond>  list=  JSON.parseArray(data, Bond.class);
        return list;
    }
    @PostMapping("addProductGroup.do")
    public String setProductGroupToUser(String data){
        System.out.println(data);
        JSONObject jsonObject = JSON.parseObject(data);
        System.out.println(jsonObject.get("groupName"));
        System.out.println(jsonObject.get("productList"));
//        HttpSession session = request.getSession();
//        String sessionId = session.getId();
//        String uuid = UUID.randomUUID().toString().replaceAll("-","");
//        String productGroupStr = JSON.toJSONString(productGroup);
//        redisUtil.hset(sessionId,uuid,productGroupStr);//放入用户分组
        return "success";
    }

    @RequestMapping("sendMessage.do")
    public String sendMessage(){
        redisUtil.set("name123","1111111111111");
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                DecimalFormat format;
//                ObjectMapper objectMapper = new ObjectMapper();
//                List<Stocks> stockInfoList = new ArrayList<Stocks>();
//                BigDecimal dig;
//                for(int i=0;i<=2000;i++) {
//                    Stocks stocks = new Stocks();
//                    stocks.setDate();
//                    stocks.setS("EURJPY");
//                    stocks.setTick("1553652958");
//                    stocks.setP(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setN("EUR/JPY");
//                    stocks.setB1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setS1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setV(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setA("0.0");
//                    stocks.setO(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setH(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setL(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stocks.setYC(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//                    stockInfoList.add(stocks);
//                }
//                try {
//                    System.out.println("推送股票信息条数："+stockInfoList.size());
//                    String stockInfos = objectMapper.writeValueAsString(stockInfoList);
//                    BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
//                    BigDecimal bigDecimal2 = new BigDecimal(1024);
//                    System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"K");
//                    String zipMessage = ZipUtil.gzip(stockInfos);
//                    bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
//                    System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"K");
////                    TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);//
//                    String backStir = HttpClientUtil.doPostJson("http://localhost:2048", ZipUtil.gzip(stockInfos));
//                    System.out.println(backStir);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        },0,1000, TimeUnit.MILLISECONDS);
        return "success";
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
