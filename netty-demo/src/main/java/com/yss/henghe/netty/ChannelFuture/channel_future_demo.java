package com.yss.henghe.netty.ChannelFuture;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class channel_future_demo {

    public static void main(String[] args) {

        Channel channel = new NioDatagramChannel();
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.101.121", 9956));
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("success");
                } else {
                    future.cause().printStackTrace();
                }
            }
        });


    }

}
