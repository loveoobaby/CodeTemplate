package com.yss.henghe.netty.ByteToMessageCodec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 0             4
 * +------+------+------------------+
 * | body length |   body           |
 * +-------------+------------------+
 */


public class ByteToMessageCodec_Example extends ByteToMessageCodec {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof String) {
            byte[] msgBytes = ((String) msg).getBytes(StandardCharsets.UTF_8);
            out.writeInt(msgBytes.length);
            out.writeBytes(msgBytes);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        if(in.readableBytes() < 4){
            return;
        }

        do {
            in.markReaderIndex();
            int length = in.readInt();
            if(length > in.readableBytes()){ // 存在半包问题
                in.resetReaderIndex();
                return;
            }

            // 解码数据
            byte[] data = new byte[length];
            in.readBytes(data);
            out.add(new String(data));

        }while (in.readableBytes() > 4);
    }
}
