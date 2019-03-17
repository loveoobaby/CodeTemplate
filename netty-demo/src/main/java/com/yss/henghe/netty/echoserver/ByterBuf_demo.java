package com.yss.henghe.netty.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;


public class ByterBuf_demo {

    public static void main(String[] args) {
        /**
         * ByteBuf 内部数据结构
         * +-------------------+------------------+------------------+
         *        | discardable bytes |  readable bytes  |  writable bytes  |
         *        +-------------------+------------------+------------------+
         *        |                   |                  |                  |
         *        0      <=      readerIndex   <=   writerIndex    <=    capacity
         */


        /**
         * 最常用的ByteBuf模式是将数据存储在JVM的堆空间中，这种模式称为支撑数组
         * 优点：由于数据存储在JVM堆上，创建与释放较快
         * 缺点：每次进行IO传输时，都需要将数据拷贝到直接缓存区
         */
        {
            ByteBuf buf = Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8);
            if(buf.hasArray()){
                byte[] array = buf.array();
                int offset = buf.arrayOffset() + buf.readerIndex();
                int length = buf.readableBytes();
                System.out.println(new String(array, offset, length));
            }
        }

        /**
         * 直接缓冲区模式
         * Direct Bufffer属于堆外直接内存，不占用堆空间
         * 优点：使用Socket传输性能很好，避免了数据从JVM堆内存拷贝到直接内存
         * 缺点: 分配释放比较昂贵
         */
        {
            ByteBuf directBuf = Unpooled.directBuffer();
            directBuf.writeCharSequence("hello", CharsetUtil.UTF_8);
            if (!directBuf.hasArray()) {
                int length = directBuf.readableBytes();
                byte[] array = new byte[length];
                directBuf.getBytes(directBuf.readerIndex(), array);
                System.out.println(new String(array, 0, length));
            }
        }

    }
}
