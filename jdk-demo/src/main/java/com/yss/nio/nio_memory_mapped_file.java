package com.yss.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件内存映射，读写性能极高
 * 内存映射是将用户空间的一段内存区域映射到内核空间，映射成功后，用户对这段内存区域的修改可以直接反映到内核空间，
 * 同样，内核空间对这段区域进行修改也会直接反应用户空间。对于内核空间与用户空间之间需要大量数据传输的话效率很高。
 * 内存映射对应于Linux的mmap系统调用
 */
public class nio_memory_mapped_file {

    static int length = 0x8FFFFFF;

    public static void main(String[] args) throws Exception
    {
        try(RandomAccessFile file = new RandomAccessFile("howtodoinjava.dat", "rw"))
        {
            MappedByteBuffer out = file.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, length);

            for (int i = 0; i < length; i++)
            {
                // 写文件
                out.put((byte) 'x');
                // 读文件
                out.get();
            }

            System.out.println("Finished writing");
        }
    }
}

