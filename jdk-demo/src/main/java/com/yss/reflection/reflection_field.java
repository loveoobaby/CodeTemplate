package com.yss.reflection;

import java.lang.reflect.Field;

/**
 * 可以运行期检查一个类的变量信息(成员变量)或者获取或者设置变量的值
 */
public class reflection_Field {

    private static class MyObject {
        private String first;
        public String second;

        @Override
        public String toString() {
            return "MyObject{" +
                    "first='" + first + '\'' +
                    ", second='" + second + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class  aClass = MyObject.class;

        // 只能获取公有字段
        Field publicFiled = aClass.getField("second");
        // 可以获取私有字段
        Field field = aClass.getDeclaredField("first");

        // 用field给对象字段赋值
        MyObject objectInstance = new MyObject();
        Object value = "test";
        field.setAccessible(true);

        field.set(objectInstance, value);
        publicFiled.set(objectInstance, "second");
        System.out.println(objectInstance);
    }

}
