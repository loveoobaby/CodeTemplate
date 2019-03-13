package com.yss.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Channel类似于流，但又有些不同
 *    1. 既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
 *    2. Channel 可以异步地读写
 *    3. Channel 中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入
 *
 * 最重要的Channel的实现：
 *    1. FileChannel： 从文件中读写数据
 *    2. DatagramChannel： 能通过UDP读写网络中的数据
 *    3. SocketChannel： 能通过TCP读写网络中的数据
 *    4. ServerSocketChannel： 可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 */
public class nio_socket_channel {

    public static void main(String[] args) throws IOException {
        //SocketChannel是一个连接到TCP网络套接字的Channel
        // 打开SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("192.168.0.1", 8080));

        // 配置非阻塞模式
        socketChannel.configureBlocking(false);

        {
            // 读取数据
            ByteBuffer buf = ByteBuffer.allocate(48);
            int bytesRead = socketChannel.read(buf);
        }

        {
            // 写入数据
            String newData = "New String to write to file..." + System.currentTimeMillis();
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(newData.getBytes());

            buf.flip();

            while(buf.hasRemaining()) {
                socketChannel.write(buf);
            }
        }

        // 关闭SocketChannel
        socketChannel.close();

    }

}
