package com.yss.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 利用Java的反射机制你可以检查一个类的构造方法，并且可以在运行期创建一个对象
 */
public class reflection_Constructor {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        {
            // 获取所有public Constructor
            Constructor[] constructors = String.class.getConstructors();

            // 利用Constructor对象实例化一个类
            Constructor constructor = String.class.getConstructor(String.class);
            String myObject = (String)
                    constructor.newInstance("constructor-arg1");
            System.out.println(myObject);
        }

    }
}
