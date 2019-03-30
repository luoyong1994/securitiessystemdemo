package com.ynet.securitiessystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynet.securitiessystem.httpclientutil.HttpClientUtil;
import com.ynet.securitiessystem.model.Stocks;
import com.ynet.securitiessystem.nettyserve.ZipMessage.ZipUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@ResponseBody
public class DataController {

    @RequestMapping("sendMessage.do")
    public String sendMessage(){

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DecimalFormat format;
                ObjectMapper objectMapper = new ObjectMapper();
                List<Stocks> stockInfoList = new ArrayList<Stocks>();
                BigDecimal dig;
                for(int i=0;i<=2000;i++) {
                    Stocks stocks = new Stocks();
                    stocks.setDate();
                    stocks.setS("EURJPY");
                    stocks.setTick("1553652958");
                    stocks.setP(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setN("EUR/JPY");
                    stocks.setB1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setS1(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setV(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setA("0.0");
                    stocks.setO(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setH(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setL(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stocks.setYC(new BigDecimal(Math.random()*200).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    stockInfoList.add(stocks);
                }
                try {
                    System.out.println("推送股票信息条数："+stockInfoList.size());
                    String stockInfos = objectMapper.writeValueAsString(stockInfoList);
                    BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
                    BigDecimal bigDecimal2 = new BigDecimal(1024);
                    System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"K");
                    String zipMessage = ZipUtil.gzip(stockInfos);
                    bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
                    System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"K");
//                    TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);//
                    String backStir = HttpClientUtil.doPostJson("http://localhost:2048", ZipUtil.gzip(stockInfos));
                    System.out.println(backStir);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },0,1000, TimeUnit.MILLISECONDS);
        return "success";
    }


}
