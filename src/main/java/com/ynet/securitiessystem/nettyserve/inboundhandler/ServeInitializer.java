package com.ynet.securitiessystem.nettyserve.inboundhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ServeInitializer extends ChannelInitializer<Channel> {

    @Autowired
    public TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Autowired
    public HttpRequestHandler httpRequestHandler;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());//http消息编码解码

        pipeline.addLast(new ChunkedWriteHandler());//

        pipeline.addLast(new HttpObjectAggregator(64*1024));// Http消息组装

        pipeline.addLast(httpRequestHandler);

        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));//WebSocket通信支持

        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));

        pipeline.addLast("encoder",  new StringEncoder(CharsetUtil.UTF_8));

        pipeline.addLast(textWebSocketFrameHandler);


    }
}
