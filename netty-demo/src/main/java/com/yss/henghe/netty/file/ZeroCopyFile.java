package com.yss.henghe.netty.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;

/**
 * Server that accept the path of a file an echo back its content.
 */
public final class ZeroCopyFile {

    static final boolean SSL = System.getProperty("ssl") != null;
    // Use the same default port with the telnet example so that we can use the telnet client example to access it.
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8992" : "8023"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     if (sslCtx != null) {
                         p.addLast(sslCtx.newHandler(ch.alloc()));
                     }
                     p.addLast(
                             new StringEncoder(CharsetUtil.UTF_8),
                             new LineBasedFrameDecoder(8192),
                             new StringDecoder(CharsetUtil.UTF_8),
                             new ChunkedWriteHandler(),
                             new FileServerHandler());
                 }
             });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static class FileServerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.writeAndFlush("HELLO: Type the path of the file to retrieve.\n");
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            RandomAccessFile raf = null;
            long length = -1;
            try {
                raf = new RandomAccessFile(msg, "r");
                length = raf.length();
            } catch (Exception e) {
                ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
                return;
            } finally {
                if (length < 0 && raf != null) {
                    raf.close();
                }
            }

            ctx.write("OK: " + raf.length() + '\n');
            // 如果没有启用SSL，直接传输文件
            if (ctx.pipeline().get(SslHandler.class) == null) {
                // SSL not enabled - can use zero-copy file transfer.
                ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
            } else {
                // SSL enabled - cannot use zero-copy file transfer.
                // 如果启用SSL，不能使用零拷贝，传输之前将会由SslHandler加密
                ctx.write(new ChunkedFile(raf));
            }
            ctx.writeAndFlush("\n");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();

            if (ctx.channel().isActive()) {
                ctx.writeAndFlush("ERR: " +
                        cause.getClass().getSimpleName() + ": " +
                        cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}