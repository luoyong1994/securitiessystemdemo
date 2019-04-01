package com.ynet.securitiessystem.nettyserve;

import com.alibaba.fastjson.JSON;
import com.ynet.securitiessystem.model.Bond;
import com.ynet.securitiessystem.model.Group;
import com.ynet.securitiessystem.nettyserve.ZipMessage.ZipUtil;
import com.ynet.securitiessystem.redis.JavaOps;
import com.ynet.securitiessystem.redis.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelManager {
    @Autowired
    private RedisUtil redisUtil;

    public static final ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();

    public ChannelGroup channelGroups = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    public void addChannel(String key, Channel channel){
        ChannelManager.channels.put(key,channel);
    }

    public void channelDestory(){
        for (Map.Entry<String, Channel> entry : channels.entrySet()) {
            entry.getValue().close();
        }
    }

    public void writeAndFlash(String temp) {
        for (Map.Entry<String, Channel> entry : channels.entrySet()) {
            Set<String> pids = new HashSet<String>();
            List<Bond> result = JSON.parseArray(temp, Bond.class);
            Map<String, Bond> map = new HashMap<String, Bond>();
            String[] strIdArray = new String[result.size()];
            for (int i = 0; i < result.size(); i++) {
                pids.add(result.get(i).getSymbol());
                map.put(result.get(i).getSymbol(), result.get(i));
            }

            //知道推送用户以及获取推送用户订阅的产品列表
            //        String groupKey="";
            Map<Object, Object> mapGroup = redisUtil.hmget(entry.getKey());
            String groupKey = (String) redisUtil.get(entry.getKey()+"_groupKey");
            Group group = (Group) mapGroup.get(groupKey);
            Set<Bond> productSet = group.getProductList();
            Set<String> userPids = new HashSet<String>();
            for (Bond bond : productSet) {
                userPids.add(bond.getSymbol());
            }
            List<Bond> stockInfoList = new ArrayList<Bond>();
            //取交集
            Set<String> resultSet = JavaOps.getLastByIdsAndUserPids(pids, userPids);
            Map<String, Bond> resultMap = new HashMap<String, Bond>();
            for (String productKey : resultSet) {
                resultMap.put(productKey, map.get(productKey));
                stockInfoList.add(map.get(productKey));
            }
            try {
                System.out.println("推送股票信息条数：" + stockInfoList.size());
                String stockInfos = JSON.toJSONString(stockInfoList);
                String encodedStockInfos = URLEncoder.encode(stockInfos,"UTF-8");
                BigDecimal bigDecimal1 = new BigDecimal(encodedStockInfos.getBytes("UTF-8").length);
                BigDecimal bigDecimal2 = new BigDecimal(1024);
                System.out.println("推送股票消息大小为：" + bigDecimal1.divide(bigDecimal2).toString() + "K");
                String zipMessage = ZipUtil.gzip(encodedStockInfos);
                bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
                System.out.println("推送股票消息压缩后大小：" + (bigDecimal1.divide(bigDecimal2).toString()) + "K");
                TextWebSocketFrame ZipMsg = new TextWebSocketFrame(zipMessage);
                Channel channel = entry.getValue();
                channel.writeAndFlush(ZipMsg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
