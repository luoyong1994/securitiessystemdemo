package com.ynet.securitiessystem;

import com.ynet.securitiessystem.nettyserve.NettyService;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;


@SpringBootApplication
public class SecuritiessystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecuritiessystemApplication.class, args);
    }

    @Autowired
    private NettyService nettyService;


    @Override
    public void run(String... args) throws Exception {

//        final NettyService server = new NettyService();


        ChannelFuture f = nettyService.start(new InetSocketAddress(2048));
        System.out.println("server start................");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                nettyService.destroy();
            }
        });
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                DecimalFormat format;
//                ObjectMapper objectMapper = new ObjectMapper();
//                List<Stocks> stockInfoList = new ArrayList<Stocks>();
//                BigDecimal dig;
//                for(int i=0;i<100;i++) {
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
//                    System.out.println(stockInfoList);
//                    String stockInfos = objectMapper.writeValueAsString(stockInfoList);
//                    BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
//                    BigDecimal bigDecimal2 = new BigDecimal(1024);
//                    System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"K");
//                    String zipMessage = ZipUtil.gzip(stockInfos);
//                    bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
//                    System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"K");
//                    TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);//
//                    group.writeAndFlush(ZipMsg.retain());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        },0,1000, TimeUnit.MILLISECONDS);
        f.channel().closeFuture().syncUninterruptibly();
    }
}
