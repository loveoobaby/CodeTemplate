package com.yss.io;

import java.io.*;


public class io_byte_array_input_output_stream {

    public static void main(String[] args) throws IOException {
        {
            /**
             * 用ByteArrayInputStream或者CharArrayReader封装字节或者字符数组从数组中读取数据
             * 通过这种方式字节和字符就可以从数组中以流的方式读出了。
             * 以下代码同样的方式也可以用于读取字符数组，只要把字符数组封装在CharArrayReader上就行了。
             */
            byte[] bytes = new byte[1024];

            //把数据写入字节数组...
            InputStream input = new ByteArrayInputStream(bytes);
            //读取第一个字节
            int data = input.read();

            while(data != -1) {
                //操作数据
                // 读下一个字节
                data = input.read();
            }
        }

        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write("This text is converted to bytes".getBytes("UTF-8"));
            byte[] bytes = output.toByteArray();
        }




    }

}
