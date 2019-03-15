package com.yss.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。
 * 事实上，可以只用一个线程处理所有的通道。
 * 对于操作系统来说，线程之间上下文切换的开销很大，而且每个线程都要占用系统的一些资源（如内存）, 因此使用的线程越少越好
 */
public class nio_Selector {

    public void beginSelect(SocketChannel channel) throws IOException {
        // 创建Selector
        Selector selector = Selector.open();
        //
        /**
         * Channel必须是非阻塞模式
         *  将Channel注册到Selector，同时设置监听事件类型
         *  监听事件的类型有：
         *     SelectionKey.OP_CONNECT
         *     SelectionKey.OP_ACCEPT
         *     SelectionKey.OP_READ
         *     SelectionKey.OP_WRITE
         */
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        while(true) {
            // Select返回的有多少通道已经就绪, 亦既自上次调用select()方法后有多少通道变成就绪状态
            // select()阻塞到至少有一个通道在你注册的事件上就绪了
            // select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)
            // selectNow()此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零
            int readyChannels = selector.select();
            if(readyChannels == 0) continue;
            // selectedKeys()方法获取“已选择键集（selected key set）”中的就绪通道
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {

                    // a connection was accepted by a ServerSocketChannel.
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                /**
                 * Selector不会自己从已选择键集中移除SelectionKey实例。
                 * 必须在处理完通道时自己移除。下次该通道变成就绪时，
                 * Selector会再次将其放入已选择键集中。
                 */
                keyIterator.remove();
            }
        }
    }

}
