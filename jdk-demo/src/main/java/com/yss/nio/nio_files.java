package com.yss.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *  java.nio.file.Files类提供了操作文件的API
 */
public class nio_files {

    {
        // 检查文件是否存在
        Path path = Paths.get("data/logging.properties");
        // 本例中第二个参数表示不跟踪链接检查文件是否存在
        boolean pathExists = Files.exists(path, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
    }

    // 创建目录
    {
        Path path = Paths.get("data/subdir");

        try {
            Path newDir = Files.createDirectory(path);
        } catch(FileAlreadyExistsException e){
            // the directory already exists.
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }
    }

    // 复制文件
    {
        Path sourcePath      = Paths.get("data/logging.properties");
        Path destinationPath = Paths.get("data/logging-copy.properties");

        try {
            Files.copy(sourcePath, destinationPath);
            // 覆盖已存在的文件
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch(FileAlreadyExistsException e) {
            //destination file already exists
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }
    }

    // 删除文件
    {
        Path path = Paths.get("data/subdir/logging-moved.properties");

        try {
            Files.delete(path);
        } catch (IOException e) {
            //deleting file failed
            e.printStackTrace();
        }
    }

    // 目录遍历
    {
        Path path = Paths.get("/tmp");
        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                // 在访问目录之前调用
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("pre visit dir:" + dir);
                    return FileVisitResult.CONTINUE;
                }

                // 访问文件时调用
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("visit file: " + file);
                    return FileVisitResult.CONTINUE;
                }

                // 访问文件失败时调用
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("visit file failed: " + file);
                    return FileVisitResult.CONTINUE;
                }

                // 访问文件夹完成后调用
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    System.out.println("post visit directory: " + dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
