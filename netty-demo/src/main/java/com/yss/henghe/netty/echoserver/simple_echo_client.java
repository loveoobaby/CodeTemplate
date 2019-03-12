package com.yss.henghe.netty.echoserver;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetSocketAddress;


public class simple_echo_client {

    public static void main(String[] args)
            throws Exception {

        new InnerEchoClient("localhost", 8080).start();
    }

    public static class InnerEchoClient {
        private final String host;
        private final int port;

        public InnerEchoClient(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void start()
                throws Exception {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                //创建 Bootstrap
                Bootstrap b = new Bootstrap();
                //指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
                b.group(group)
                        //适用于 NIO 传输的Channel 类型
                        .channel(NioSocketChannel.class)
                        //设置服务器的InetSocketAddress
                        .remoteAddress(new InetSocketAddress(host, port))
                        //在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler实例
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch)
                                    throws Exception {
                                ch.pipeline().addLast(
                                        new EchoClientHandler());
                            }
                        });
                //连接到远程节点，阻塞等待直到连接完成
                ChannelFuture f = b.connect().sync();
                //阻塞，直到Channel 关闭
//                f.channel().closeFuture().sync();

                StringBuilder builder = new StringBuilder();
                builder.append("hello\n");

                for (int i = 0; i < 1000; i++) {
                    Thread.sleep(100);
                    ByteBuf directBuf = Unpooled.directBuffer();
                    directBuf.writeCharSequence(builder.toString(), CharsetUtil.UTF_8);
                    f.channel().writeAndFlush(directBuf.retain()).addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            ReferenceCountUtil.release(directBuf);

                        }
                    });
                }





                f.channel().closeFuture().sync();
            } finally {
                //关闭线程池并且释放所有的资源
                group.shutdownGracefully().sync();
            }
        }


        @ChannelHandler.Sharable
        //标记该类的实例可以被多个 Channel 共享
        public static class EchoClientHandler
                extends SimpleChannelInboundHandler<ByteBuf> {
            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                //当被通知 Channel是活跃的时候，发送一条消息
                ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                        CharsetUtil.UTF_8));
            }

            @Override
            public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
                //记录已接收消息的转储
                System.out.println(
                        "Client received: " + in.toString(CharsetUtil.UTF_8));
            }

            @Override
            //在发生异常时，记录错误并关闭Channel
            public void exceptionCaught(ChannelHandlerContext ctx,
                                        Throwable cause) {
                cause.printStackTrace();
                ctx.close();
            }
        }

    }
}



