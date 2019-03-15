package com.yss.io;

import java.io.*;

/**
 * Reader和Writer除了基于字符之外，其他方面都与InputStream和OutputStream非常类似。
 * 他们被用于读写文本。InputStream和OutputStream是基于字节的
 * Reader类是Java IO中所有Reader的基类
 * 子类包括BufferedReader，PushbackReader，InputStreamReader，StringReader和其他Reade
 *
 *  InpuStream的read()方法返回一个字节，意味着这个返回值在0~255之间，Reader的read()方法返回一个字符，
 *  意味着这个返回值在0到65535之间，reader会根据文件编码，一次读取一个或多个字节
 */
public class io_InputStreamReader {

    public static void main(String[] args) throws IOException {

        String filePathString = "";

        {
            try(Reader reader = new FileReader(filePathString)){
                int data = reader.read();
                while(data != -1){
                    char dataChar = (char) data;
                    data = reader.read();
                }
            }
        }

        {
            // InputStream可以用来创建Reader
            //  建议创建Reader时，指定文件编码
            try(InputStreamReader fReader = new InputStreamReader(new FileInputStream(filePathString),"UTF-8");
                //可以进一步将Reader包装成BufferedReader，实现缓冲功能
                BufferedReader reader = new BufferedReader(fReader)){
                String line;
                while ((line = reader.readLine()) != null) {
                    //do something
                }
            }
        }


    }
}
