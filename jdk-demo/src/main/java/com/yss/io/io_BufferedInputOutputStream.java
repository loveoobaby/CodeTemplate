package com.yss.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class io_BufferedInputOutputStream {
    public static void main(String[] args) throws IOException {

        {
            /**
             * BufferedInputStream是套在某个其他的InputStream外，起着缓存的功能，用来改善里面那个InputStream的性能
             */
            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            try (InputStream input = new BufferedInputStream(new FileInputStream("/tmp/a.txt"))){
                //从文件中按字节读取内容，到文件尾部时read方法将返回-1
                while ((bytesRead = input.read(buffer)) != -1) {
                    //将读取的字节转为字符串对象
                    String chunk = new String(buffer, 0, bytesRead);
                    System.out.print(chunk);
                }
            }
        }

        {

        }






    }
}
