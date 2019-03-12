package com.yss.henghe.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;


public class http_compression_initializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public http_compression_initializer(boolean isClient){
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if(isClient){
            // 如果是客户端，添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
            // HttpContentDecompressor来解压数据
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        }else {
            // 如果是服务端，添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
            // 添加HttpContentCompressor来压缩数据
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}
