package com.yss.tets;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author yss
 * @date 2019/3/11上午9:24
 * @description: TODO
 */
public class TestWriteNetty {

    public static void main(String[] args) throws IOException {

        JSONArray array = new JSONArray();

        {
            String name = "netty_echo_tcp_server";
            String template = "    public static void main(String[] args) throws Exception {\n" +
                    "        //调用服务器的 start()方法\n" +
                    "        new InnerEchoServer(8080).start();\n" +
                    "    }\n" +
                    "\n" +
                    "    public static class InnerEchoServer {\n" +
                    "        private final int port;\n" +
                    "\n" +
                    "        public InnerEchoServer(int port) {\n" +
                    "            this.port = port;\n" +
                    "        }\n" +
                    "        \n" +
                    "        public void start() throws Exception {\n" +
                    "            final EchoServerHandler serverHandler = new EchoServerHandler();\n" +
                    "            //(1) 创建EventLoopGroup\n" +
                    "            EventLoopGroup group = new NioEventLoopGroup();\n" +
                    "            try {\n" +
                    "                //(2) 创建ServerBootstrap\n" +
                    "                ServerBootstrap b = new ServerBootstrap();\n" +
                    "                b.group(group)\n" +
                    "                        //(3) 指定所使用的 NIO 传输 Channel\n" +
                    "                        .channel(NioServerSocketChannel.class)\n" +
                    "                        //(4) 使用指定的端口设置套接字地址\n" +
                    "                        .localAddress(new InetSocketAddress(port))\n" +
                    "                        //(5) 添加一个EchoServerHandler到于Channel的 ChannelPipeline\n" +
                    "                        .childHandler(new ChannelInitializer<SocketChannel>() {\n" +
                    "                            @Override\n" +
                    "                            public void initChannel(SocketChannel ch) throws Exception {\n" +
                    "                                ch.pipeline().addLast(serverHandler);\n" +
                    "                            }\n" +
                    "                        });\n" +
                    "                //(6) 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成\n" +
                    "                ChannelFuture f = b.bind().sync();\n" +
                    "                System.out.println(\" started and listening for connections on \" + f.channel().localAddress());\n" +
                    "                //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成\n" +
                    "                f.channel().closeFuture().sync();\n" +
                    "            } finally {\n" +
                    "                //(8) 关闭 EventLoopGroup，释放所有的资源\n" +
                    "                group.shutdownGracefully().sync();\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    @ChannelHandler.Sharable\n" +
                    "    public static class EchoServerHandler extends ChannelInboundHandlerAdapter {\n" +
                    "        @Override\n" +
                    "        public void channelRead(ChannelHandlerContext ctx, Object msg) {\n" +
                    "            ByteBuf in = (ByteBuf) msg;\n" +
                    "            //将消息记录到控制台\n" +
                    "            System.out.println(\n" +
                    "                    \"Server received: \" + in.toString(CharsetUtil.UTF_8));\n" +
                    "            //将接收到的消息写给发送者，而不冲刷出站消息\n" +
                    "            ctx.write(in);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void channelReadComplete(ChannelHandlerContext ctx)\n" +
                    "                throws Exception {\n" +
                    "            //将未决消息冲刷到远程节点，并且关闭该 Channel\n" +
                    "            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)\n" +
                    "                    .addListener(ChannelFutureListener.CLOSE);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void exceptionCaught(ChannelHandlerContext ctx,\n" +
                    "                                    Throwable cause) {\n" +
                    "            //打印异常栈跟踪\n" +
                    "            cause.printStackTrace();\n" +
                    "            //关闭该Channel\n" +
                    "            ctx.close();\n" +
                    "        }\n" +
                    "    }";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "netty_echo_tcp_client";
            String template = " public static void main(String[] args)\n" +
                    "            throws Exception {\n" +
                    "\n" +
                    "        new InnerEchoClient(\"localhost\", 8080).start();\n" +
                    "    }\n" +
                    "\n" +
                    "    public static class InnerEchoClient {\n" +
                    "        private final String host;\n" +
                    "        private final int port;\n" +
                    "\n" +
                    "        public InnerEchoClient(String host, int port) {\n" +
                    "            this.host = host;\n" +
                    "            this.port = port;\n" +
                    "        }\n" +
                    "\n" +
                    "        public void start()\n" +
                    "                throws Exception {\n" +
                    "            EventLoopGroup group = new NioEventLoopGroup();\n" +
                    "            try {\n" +
                    "                //创建 Bootstrap\n" +
                    "                Bootstrap b = new Bootstrap();\n" +
                    "                //指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现\n" +
                    "                b.group(group)\n" +
                    "                        //适用于 NIO 传输的Channel 类型\n" +
                    "                        .channel(NioSocketChannel.class)\n" +
                    "                        //设置服务器的InetSocketAddress\n" +
                    "                        .remoteAddress(new InetSocketAddress(host, port))\n" +
                    "                        //在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler实例\n" +
                    "                        .handler(new ChannelInitializer<SocketChannel>() {\n" +
                    "                            @Override\n" +
                    "                            public void initChannel(SocketChannel ch)\n" +
                    "                                    throws Exception {\n" +
                    "                                ch.pipeline().addLast(\n" +
                    "                                        new EchoClientHandler());\n" +
                    "                            }\n" +
                    "                        });\n" +
                    "                //连接到远程节点，阻塞等待直到连接完成\n" +
                    "                ChannelFuture f = b.connect().sync();\n" +
                    "                //阻塞，直到Channel 关闭\n" +
                    "//                f.channel().closeFuture().sync();\n" +
                    "\n" +
                    "                StringBuilder builder = new StringBuilder();\n" +
                    "                builder.append(\"hello\");\n" +
                    "\n" +
                    "                ByteBuf directBuf = Unpooled.directBuffer();\n" +
                    "                directBuf.writeCharSequence(builder.toString(), CharsetUtil.UTF_8);\n" +
                    "                f.channel().writeAndFlush(directBuf.retain()).addListener(new GenericFutureListener<Future<? super Void>>() {\n" +
                    "                    @Override\n" +
                    "                    public void operationComplete(Future<? super Void> future) throws Exception {\n" +
                    "                        ReferenceCountUtil.release(directBuf);\n" +
                    "\n" +
                    "                    }\n" +
                    "                });\n" +
                    "\n" +
                    "\n" +
                    "                f.channel().closeFuture().sync();\n" +
                    "            } finally {\n" +
                    "                //关闭线程池并且释放所有的资源\n" +
                    "                group.shutdownGracefully().sync();\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "\n" +
                    "        @ChannelHandler.Sharable\n" +
                    "        //标记该类的实例可以被多个 Channel 共享\n" +
                    "        public static class EchoClientHandler\n" +
                    "                extends SimpleChannelInboundHandler<ByteBuf> {\n" +
                    "            @Override\n" +
                    "            public void channelActive(ChannelHandlerContext ctx) {\n" +
                    "                //当被通知 Channel是活跃的时候，发送一条消息\n" +
                    "                ctx.writeAndFlush(Unpooled.copiedBuffer(\"Netty rocks!\",\n" +
                    "                        CharsetUtil.UTF_8));\n" +
                    "            }\n" +
                    "\n" +
                    "            @Override\n" +
                    "            public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {\n" +
                    "                //记录已接收消息的转储\n" +
                    "                System.out.println(\n" +
                    "                        \"Client received: \" + in.toString(CharsetUtil.UTF_8));\n" +
                    "            }\n" +
                    "\n" +
                    "            @Override\n" +
                    "            //在发生异常时，记录错误并关闭Channel\n" +
                    "            public void exceptionCaught(ChannelHandlerContext ctx,\n" +
                    "                                        Throwable cause) {\n" +
                    "                cause.printStackTrace();\n" +
                    "                ctx.close();\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "    }";

            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "ByteBuf_head&direct_demo";
            String template = "        /**\n" +
                    "         * ByteBuf 内部数据结构\n" +
                    "         * +-------------------+------------------+------------------+\n" +
                    "         *        | discardable bytes |  readable bytes  |  writable bytes  |\n" +
                    "         *        +-------------------+------------------+------------------+\n" +
                    "         *        |                   |                  |                  |\n" +
                    "         *        0      <=      readerIndex   <=   writerIndex    <=    capacity\n" +
                    "         */\n" +
                    "\n" +
                    "\n" +
                    "        /**\n" +
                    "         * 最常用的ByteBuf模式是将数据存储在JVM的堆空间中，这种模式称为支撑数组\n" +
                    "         * 优点：由于数据存储在JVM堆上，创建与释放较快\n" +
                    "         * 缺点：每次进行IO传输时，都需要将数据拷贝到直接缓存区\n" +
                    "         */\n" +
                    "        {\n" +
                    "            ByteBuf buf = Unpooled.copiedBuffer(\"Netty rocks!\", CharsetUtil.UTF_8);\n" +
                    "            if(buf.hasArray()){\n" +
                    "                byte[] array = buf.array();\n" +
                    "                int offset = buf.arrayOffset() + buf.readerIndex();\n" +
                    "                int length = buf.readableBytes();\n" +
                    "                System.out.println(new String(array, offset, length));\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        /**\n" +
                    "         * 直接缓冲区模式\n" +
                    "         * Direct Bufffer属于堆外直接内存，不占用堆空间\n" +
                    "         * 优点：使用Socket传输性能很好，避免了数据从JVM堆内存拷贝到直接内存\n" +
                    "         * 缺点: 分配释放比较昂贵\n" +
                    "         */\n" +
                    "        {\n" +
                    "            ByteBuf directBuf = Unpooled.directBuffer();\n" +
                    "            directBuf.writeCharSequence(\"hello\", CharsetUtil.UTF_8);\n" +
                    "            if (!directBuf.hasArray()) {\n" +
                    "                int length = directBuf.readableBytes();\n" +
                    "                byte[] array = new byte[length];\n" +
                    "                directBuf.getBytes(directBuf.readerIndex(), array);\n" +
                    "                System.out.println(new String(array, 0, length));\n" +
                    "            }\n" +
                    "        }";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        writeDataToFile(array.toJSONString());



    }


    private static void writeDataToFile(String jsonFormatData) throws IOException {
        FileUtil.writeToFile(new File("/Users/yss/work/CodeTemplate/src/main/resources/template/netty.json"), jsonFormatData);

    }

}
