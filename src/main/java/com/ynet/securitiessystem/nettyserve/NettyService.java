package com.ynet.securitiessystem.nettyserve;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynet.securitiessystem.nettyserve.inboundhandler.ServeInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class NettyService {

    public final static ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final static EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address){
        ServerBootstrap boot = new ServerBootstrap();
        boot.childOption(ChannelOption.SO_KEEPALIVE,true);
        boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(group));
        ChannelFuture f = boot.bind(address).syncUninterruptibly();
        channel = f.channel();
        return f;
    }

    protected ChannelHandler createInitializer(ChannelGroup group2) {
        return new ServeInitializer(group2);
    }

    public void destroy(){
        if(channel != null)
            channel.close();
        group.close();
        workerGroup.shutdownGracefully();
    }

    public void heartCheckOtime(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(group.size());
                ObjectMapper objectMapper = new ObjectMapper();
                String pingCode = null;
                try {
                    pingCode = objectMapper.writeValueAsString("1 << 8 | 220");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                group.writeAndFlush(new TextWebSocketFrame(pingCode).retain());
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {

        final NettyService server = new NettyService();
        ChannelGroup group = NettyService.group;
        ChannelFuture f = server.start(new InetSocketAddress(2048));
        System.out.println("server start................");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.destroy();
            }
        });
        f.channel().closeFuture().syncUninterruptibly();
    }



}
