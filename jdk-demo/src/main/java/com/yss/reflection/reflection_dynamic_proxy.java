package com.yss.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 可以通过使用Proxy.newProxyInstance()方法创建动态代理。
 * newProxyInstance()方法有三个参数：
 *   1、类加载器（ClassLoader）用来加载动态代理类。
 *   2、一个要实现的接口的数组。
 *   3、一个InvocationHandler把所有方法的调用都转到代理上。
 */
public class reflection_dynamic_proxy {

    public static interface MyInterface {

        String sayHello();

        void printName();

    }

    public static class RealSubject implements MyInterface {

        @Override
        public String sayHello() {
            System.out.println("I am real");
            return "I am Real";
        }

        @Override
        public void printName() {
            System.out.println("My name is .....");
        }


    }

    public static class MyInvocationHandler implements InvocationHandler {

        private MyInterface realSubject;

        public MyInvocationHandler(MyInterface realSubject){
            this.realSubject = realSubject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //　　在代理真实对象前我们可以添加一些自己的操作
            System.out.println("before invoke method");

            //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
            method.invoke(realSubject, args);

            //　　在代理真实对象后我们也可以添加一些自己的操作
            System.out.println("after invoke method");
            return null;
        }
    }

    public static void main(String[] args) {
        MyInterface real = new RealSubject();
        InvocationHandler handler = new MyInvocationHandler(real);
        MyInterface proxy = (MyInterface) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                new Class[] { MyInterface.class },
                handler);

        proxy.sayHello();
        proxy.printName();

    }
}
