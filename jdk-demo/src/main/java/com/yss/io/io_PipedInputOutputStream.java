package com.yss.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Java IO中的管道为运行在同一个JVM中的两个线程提供了通信的能力。
 *
 * 在概念上，Java的管道不同于Unix/Linux系统中的管道。
 * 在Unix/Linux中，运行在不同地址空间的两个进程可以通过管道通信。
 * 在Java中，通信的双方应该是运行在同一进程中的不同线程。
 */
public class io_PipedInputOutputStream {

    public static void main(String[] args) throws IOException {
        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream input = new PipedInputStream(output);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    output.write("hello pip".getBytes());
                } catch (IOException e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int data = -1;
                    while ((data = input.read())!= -1){
                        System.out.println((char)data);
                    }
                }catch (IOException e){

                }

            }
        }).start();

    }

}
