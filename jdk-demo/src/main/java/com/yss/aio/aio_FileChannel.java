package com.yss.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 在IO多路复用模型中，事件循环将文件句柄的状态事件通知给用户线程，由用户线程自行读取数据、处理数据。
 * 异步IO模型中，当用户线程收到通知时，数据已经被内核读取完毕，并放在了用户线程指定的缓冲区内，
 * 内核在IO完成后通知用户线程直接使用即可。
 *
 * 相比于IO多路复用模型，异步IO并不十分常用，不少高性能并发服务程序使用IO多路复用模型+多线程任务处理的架构基本可以满足需求
 */
public class aio_FileChannel {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        /**
         * 使用Future读取文件
         */
        {
            Path path = Paths.get("");

            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.READ);

            ByteBuffer buffer = ByteBuffer.allocate(50);
            long position = 0;

            while (true) {
                // 第一个参数是ByteBuffer，从 AsynchronousFileChannel 中读取的数据先写入这个 ByteBuffer
                // 第二个参数表示从文件读取数据的开始位置
                Future<Integer> operation = fileChannel.read(buffer, position);

                if (operation.get() == -1) {
                    break;
                }else {
                    position += operation.get();
                }

                buffer.flip();

                CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
                CharBuffer charBuffer = decoder.decode(buffer);
                buffer.clear();
                System.out.print(charBuffer.toString());
            }
        }

        /**
         * 使用CompletionHandler读取数据
         */
        {
            Path path = Paths.get("");

            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.READ);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;
            fileChannel.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {

                    attachment.flip();
                    CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
                    CharBuffer charBuffer = null;
                    try {
                        charBuffer = decoder.decode(attachment);
                    } catch (CharacterCodingException e) {
                        e.printStackTrace();
                    }
                    attachment.clear();
                    System.out.print(charBuffer.toString());
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("read failed ");
                    exc.printStackTrace();
                }
            });
        }

        // 使用Future读取数据
        {
            Path path = Paths.get("data/test-write.txt");
            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            buffer.put("test data".getBytes());
            buffer.flip();

            Future<Integer> operation = fileChannel.write(buffer, position);
            buffer.clear();

            while(!operation.isDone());

            System.out.println("Write done");
        }

        // 使用CompletionHandler写入数据
        {
            Path path = Paths.get("/tmp/test-write.txt");
            if(!Files.exists(path)){
                Files.createFile(path);
            }
            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            buffer.put("test data".getBytes());
            buffer.flip();

            fileChannel.write(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("bytes written: " + result);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("Write failed");
                    exc.printStackTrace();
                }
            });
        }

        Thread.sleep(10000);



    }

}
