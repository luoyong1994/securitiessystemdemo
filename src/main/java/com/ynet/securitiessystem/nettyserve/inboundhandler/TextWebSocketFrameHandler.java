package com.ynet.securitiessystem.nettyserve.inboundhandler;

import com.alibaba.fastjson.JSON;
import com.ynet.securitiessystem.nettyserve.ChannelManager;
import com.ynet.securitiessystem.nettyserve.ZipMessage.ZipUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    public ChannelManager channelManager;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);

            channelManager.writeAndFlash("Client " + ctx.channel() + " joined!");
            String uuid = UUID.randomUUID().toString().replaceAll("-","");

            channelManager.addChannel(uuid,ctx.channel());

            channelManager.channelGroups.add(ctx.channel());


            Map<String,String> map = new HashMap<String, String>();
            map.put("userId",uuid);
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(ZipUtil.gzip(JSON.toJSONString(map)));
            if(ctx.channel().isWritable()){
                System.out.println(textWebSocketFrame.text());
                ctx.channel().writeAndFlush(textWebSocketFrame.retain());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
//        String message = msg.text();
//        System.out.println(message);
//        Gson gson = new Gson();
//        List stockInfoList = createStockInfoList();
//        System.out.println("推送股票信息条数："+stockInfoList.size());
//        String stockInfos = gson.toJson(stockInfoList);
//        System.out.println(stockInfos);
//        BigDecimal bigDecimal1 = new BigDecimal(stockInfos.getBytes("UTF-8").length);
//        BigDecimal bigDecimal2 = new BigDecimal(1024*1024);
//        System.out.println("推送股票消息大小为："+bigDecimal1.divide(bigDecimal2).toString()+"M");
//        String zipMessage = ZipUtil.gzip(stockInfos);
//        bigDecimal1 = new BigDecimal(zipMessage.getBytes("UTF-8").length);
//        System.out.println("推送股票消息压缩后大小："+(bigDecimal1.divide(bigDecimal2).toString())+"M");
//        TextWebSocketFrame ZipMsg = new TextWebSocketFrame("123123");
//        group.writeAndFlush(ZipMsg.retain());



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }




    public static void main(String[] args) throws UnsupportedEncodingException {

    }
}
