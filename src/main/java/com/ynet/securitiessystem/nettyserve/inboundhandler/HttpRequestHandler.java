package com.ynet.securitiessystem.nettyserve.inboundhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynet.securitiessystem.nettyserve.ChannelManager;
import com.ynet.securitiessystem.requestparser.RequestParse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Autowired
    public ChannelManager channelManager;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg)
            throws Exception {
        if("/ws".equalsIgnoreCase(msg.uri())){
            ctx.fireChannelRead(msg.retain());
        }else{
            System.out.println(msg.uri());
            RequestParse requestParse = new RequestParse();
            FullHttpResponse response = null;
            if (msg.method() == HttpMethod.GET) {
                System.out.println(requestParse.getGetParamsFromChannel(msg));
                String data = "GET method over";
                ByteBuf buf = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
                response = requestParse.responseOK(HttpResponseStatus.OK, buf);
            } else if (msg.method() == HttpMethod.POST) {
                Map<String,Object> map = requestParse.getPostParamsFromChannel(msg);
//                System.out.println(map);
                String data = "POST method over";
                ByteBuf content = Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);
                response = requestParse.responseOK(HttpResponseStatus.OK, content);
                ObjectMapper objectMapper = new ObjectMapper();
                String reciveMesg = (String)map.get("data");
                channelManager.writeAndFlash(reciveMesg);
            } else {
                response = requestParse.responseOK(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
            }
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            // 发送响应



//
//            if(HttpHeaders.is100ContinueExpected(msg)){
//                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
//                ctx.writeAndFlush(response);
//            }
//
//            RandomAccessFile file = new RandomAccessFile(HttpRequestHandler.class.getResource("/").getPath()+"/index.html", "r");
//            HttpResponse response = new DefaultHttpResponse(msg.getProtocolVersion(), HttpResponseStatus.OK);
//            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
//
//            boolean isKeepAlive = HttpHeaders.isKeepAlive(msg);
//            if(isKeepAlive){
//                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
//                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            }
//
//            ctx.write(response);
//            if(ctx.pipeline().get(SslHandler.class) == null){
//                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
//            }else{
//                ctx.write(new ChunkedNioFile(file.getChannel()));
//            }
//
//            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
//            if(isKeepAlive == false){
//                future.addListener(ChannelFutureListener.CLOSE);
//            }
//
//            file.close();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace(System.err);
    }
}
