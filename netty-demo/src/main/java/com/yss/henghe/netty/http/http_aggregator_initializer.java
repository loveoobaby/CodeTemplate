package com.yss.henghe.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;


public class http_aggregator_initializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public http_aggregator_initializer(boolean isClient){
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if(isClient){
            // 如果是客户端，添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        }else {
            // 如果是服务端，添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
        // 消息最长设为512KB， HttpObjectAggregator加入ChannelPipeline
        pipeline.addLast("aggregator", new HttpObjectAggregator(512*1024));
    }
}
