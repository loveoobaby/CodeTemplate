package com.yss.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用java.lang.reflect.Method类,可以在运行期检查一个方法的信息以及在运行期调用这个方法
 */
public class reflection_Method {

    private static class MyObject {

        public void doSomething(String param){
            System.out.println(param);
        }

    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过Method对象调用方法
        Method method = MyObject.class.getMethod("doSomething", String.class);
        MyObject object = new MyObject();
        Object returnValue = method.invoke(object, "parameter-value1");
    }

}
