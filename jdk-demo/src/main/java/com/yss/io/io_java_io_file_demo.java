package com.yss.io;

import java.io.File;

/**
 * FIle类可以实现访问底层文件系统
 * File只能访问文件以及文件系统的元数据。
 * 如果你想读写文件内容，需要使用FileInputStream、FileOutputStream或者RandomAccessFile
 */
public class io_java_io_file_demo {

    public static void main(String[] args) {

        String filePath = "";
        File file = new File(filePath);

        // 监测文件是否存在
        file.exists();

        // 查询文件长度
        file.length();

        // 重命名文件
        file.renameTo(new File(""));

        // 删除文件
        file.delete();

        //监测是文件还是目录
        file.isDirectory();

        // 读取目录文件
        file.list();
        file.listFiles();

    }

}
