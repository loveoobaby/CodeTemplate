package com.yss.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
public class nio_file_channel {
    public static void main(String[] args) throws IOException {


        /**
         * 使用FileChannel读取文件
         */
        {
            try(RandomAccessFile aFile = new RandomAccessFile("", "rw");
                FileChannel inChannel = aFile.getChannel()){

                ByteBuffer buf = ByteBuffer.allocate(48);

                int bytesRead = inChannel.read(buf);
                while (bytesRead != -1) {

                    System.out.println("Read " + bytesRead);
                    buf.flip();

                    while(buf.hasRemaining()){
                        System.out.print((char) buf.get());
                    }

                    buf.clear();
                    bytesRead = inChannel.read(buf);
                }


            }
        }

        /**
         * FileChannel其他API
         */
        {
            try(RandomAccessFile aFile = new RandomAccessFile("/tmp/umi.js", "rw");
                FileChannel inChannel = aFile.getChannel()){

                // 截取指定长度的文件
                inChannel.truncate(100);

                //有时需要在FileChannel的某个特定位置进行数据的读/写操作。可以通过调用position()方法获取FileChannel的当前位置。
                //也可以通过调用position(long pos)方法设置FileChannel的当前位置。
                inChannel.position();
                inChannel.position(50);

                // 获取与Channel关联的文件大小
                inChannel.size();

                // 强制将写入Channel的数据写入磁盘
                // force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上
                inChannel.force(true);

            }

        }





    }
}
