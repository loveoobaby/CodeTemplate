package com.yss.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 */
public class reflection_Class_Api {

    public static void main(String[] args) throws ClassNotFoundException {

        /**
         * Class对象的获取
         */
        Class aClass = StringBuilder.class;
        {
            // 如果编译期知道类名称，可以这样获取
            Class stringClass = String.class;

            // 如果在编译期不知道类名，但可以获取类名称字符串
            String className = "java.lang.String";
            Class stringClassO = Class.forName(className);
        }

        /**
         * 类名称
         */
        {
            String className = aClass.getName();
            String simpleName = aClass.getSimpleName();
        }

        /**
         * 类修饰符，即public,private,static等等的关键字
         * 每个修饰符都是一个位标识，最终包装成int类型
         */
        {
            int modifiers = aClass.getModifiers();
            Modifier.isAbstract(modifiers);
            Modifier.isFinal(modifiers);
            Modifier.isInterface(modifiers);
            Modifier.isNative(modifiers);
            Modifier.isPrivate(modifiers);
            Modifier.isProtected(modifiers);
            Modifier.isPublic(modifiers);
            Modifier.isStatic(modifiers);
            Modifier.isStrict(modifiers);
            Modifier.isSynchronized(modifiers);
            Modifier.isTransient(modifiers);
            Modifier.isVolatile(modifiers);


        }

        {
            // 包信息
            Package p = aClass.getPackage();
            // 父类
            aClass.getSuperclass();
            // 实现的接口
            Class[] interfaces = aClass.getInterfaces();
            // 构造器
            Constructor[] constructors = aClass.getConstructors();
            // 获取所有方法
            Method[] methods = aClass.getMethods();
            // 获取字段
            Field[] fields = aClass.getFields();

            // 注解
            Annotation[] annotations = aClass.getAnnotations();
        }



    }
}
