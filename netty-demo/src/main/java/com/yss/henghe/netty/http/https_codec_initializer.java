package com.yss.henghe.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;


public class https_codec_initializer extends ChannelInitializer<Channel> {

    private final SslContext sslContext;
    private final boolean isClient;

    public https_codec_initializer(SslContext sslContext, boolean isClient){
        this.sslContext = sslContext;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        // 将SslHander添加到ChannelPipeline
        pipeline.addFirst("ssl", new SslHandler(sslEngine));
        if(isClient){
            // 如果是客户端，添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        }else {
            // 如果是服务端，添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
