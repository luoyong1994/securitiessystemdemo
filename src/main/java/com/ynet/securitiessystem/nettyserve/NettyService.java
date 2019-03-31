package com.ynet.securitiessystem.nettyserve;

import com.ynet.securitiessystem.nettyserve.inboundhandler.ServeInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


@Component
public class NettyService {

    @Autowired
    public ServeInitializer serveInitializer;

    @Autowired
    public ChannelManager channelManager;



    private final static EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address){
        ServerBootstrap boot = new ServerBootstrap();
        boot.childOption(ChannelOption.SO_KEEPALIVE,true);
        boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(serveInitializer);
        ChannelFuture f = boot.bind(address).syncUninterruptibly();
        channel = f.channel();
        return f;
    }

    public void destroy(){
        if(channel != null)
            channel.close();
        channelManager.channelDestory();
        workerGroup.shutdownGracefully();
    }




}
