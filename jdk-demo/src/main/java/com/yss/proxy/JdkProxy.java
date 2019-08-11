package com.yss.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yss
 * @date 2019/8/11下午3:58
 * @description: TODO
 */
public class JdkProxy {

    public interface IHello {
        void say(String name);
    }

    public static class Hello implements IHello{

        @Override
        public void say(String name) {
            System.out.println("hello " + name);
        }
    }

    /**
     * 真正实现类代理功能的是实现InvocationHandler接口的类
     */
    public static class JdkProxyDemo implements InvocationHandler {

        private Object target;

        public JdkProxyDemo(Object target){
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before");
            Object result = method.invoke(target, args);
            System.out.println("end");
            return result;
        }

        public <T> T getProxy(){
            return (T)Proxy.newProxyInstance(this.target.getClass().getClassLoader(), this.target.getClass().getInterfaces(), this);
        }
    }

    public static void main(String[] args) {
        JdkProxyDemo demo = new JdkProxyDemo(new Hello());
        IHello  hello = demo.getProxy();
        hello.say("yss");
    }



}
