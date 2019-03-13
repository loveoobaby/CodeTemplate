package com.yss.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile允许你来回读写文件，也可以替换文件中的某些部分
 * FileInputStream和FileOutputStream没有这样的功能
 */
public class io_random_access_file {

    public static void main(String[] args) {
        String filePath = "";

        /**
         * 在RandomAccessFile的某个位置读写之前，必须把文件指针指向该位置。
         * 通过seek()方法可以达到这一目标, 可以通过调用getFilePointer()获得当前文件指针的位置。
         */
        // rw 表明以读写方式打开文件
        try(RandomAccessFile file = new RandomAccessFile(filePath, "rw")){
            // 移动文件指针
            file.seek(200);
            // 获取指针位置
            long pointer = file.getFilePointer();
            // 写文件
            file.write("Hello World".getBytes());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
