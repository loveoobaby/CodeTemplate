package com.yss.henghe.netty.echoserver;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

/**
 * 代码清单 2-2 EchoServer 类
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */

public class simple_echo_server {

    /**
     * Netty自带的拆包拆包解决方案：
     * LineBasedFrameDecoder 可以基于换行符解决。
     * DelimiterBasedFrameDecoder可基于分隔符解决。
     * FixedLengthFrameDecoder可指定长度解决
     */

    public static void main(String[] args) throws Exception {
        //调用服务器的 start()方法
        new InnerEchoServer(8080).start();
    }

    public static class InnerEchoServer {
        private final int port;

        public InnerEchoServer(int port) {
            this.port = port;
        }

        public void start() throws Exception {
            final EchoServerHandler serverHandler = new EchoServerHandler();
            //(1) 创建EventLoopGroup
            EventLoopGroup group = new NioEventLoopGroup(2);
            try {
                //(2) 创建ServerBootstrap
                ServerBootstrap b = new ServerBootstrap();
                b.group(group)
                        //(3) 指定所使用的 NIO 传输 Channel
                        .channel(NioServerSocketChannel.class)
                        //(4) 使用指定的端口设置套接字地址
                        .localAddress(new InetSocketAddress(port))
                        //(5) 添加一个EchoServerHandler到于Channel的 ChannelPipeline
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(serverHandler);
                            }
                        });
                //(6) 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
                ChannelFuture f = b.bind().sync();
                System.out.println(" started and listening for connections on " + f.channel().localAddress());
                //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
                f.channel().closeFuture().sync();
            } finally {
                //(8) 关闭 EventLoopGroup，释放所有的资源
                group.shutdownGracefully().sync();
            }
        }
    }


    @ChannelHandler.Sharable
    public static class EchoServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf in = (ByteBuf) msg;
            //将消息记录到控制台
            System.out.println(
                    Thread.currentThread() + "Server received: " + in.toString(CharsetUtil.UTF_8));
            //将接收到的消息写给发送者，而不冲刷出站消息
            ctx.write(in);
//            ReferenceCountUtil.release(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx)
                throws Exception {
            //将未决消息冲刷到远程节点，并且关闭该 Channel
//            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                    .addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx,
                                    Throwable cause) {
            //打印异常栈跟踪
            cause.printStackTrace();
            //关闭该Channel
            ctx.close();
        }
    }
}

