package com.yss.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * ServerSocketChannel 是一个可以监听新进来的TCP连接的通道
 */
public class nio_ServerSocketChannel {

    public static void main(String[] args) throws IOException {
        // 打开ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        // 设置非阻塞模式
        serverSocketChannel.configureBlocking(false);

        while(true){
            // 在非阻塞模式下，accept会立即返回
            SocketChannel socketChannel =
                    serverSocketChannel.accept();

            if(socketChannel != null){
                //do something with socketChannel...
            }
        }

    }

}
