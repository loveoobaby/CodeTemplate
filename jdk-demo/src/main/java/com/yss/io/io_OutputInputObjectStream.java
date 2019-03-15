package com.yss.io;

import java.io.*;

/**
 * ObjectInputStream能够让你从输入流中读取Java对象，而不需要每次读取一个字节
 * ObjectOutputStream能够让你把对象写入到输出流中，而不需要每次写入一个字节
 */
public class io_OutputInputObjectStream {

    private static class MyClass implements Serializable {

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        {
            try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("object.data"))){
                MyClass object = new MyClass();
                output.writeObject(object); //etc.
            }
        }

        {
            try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("object.data"))){
                MyClass object = (MyClass) input.readObject(); //etc.
                System.out.println(object.getClass());
            }
        }

    }
}
