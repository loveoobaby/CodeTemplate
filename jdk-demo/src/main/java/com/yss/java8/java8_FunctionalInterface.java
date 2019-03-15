package com.yss.java8;

/**
 * Functional interfaces 函数式接口只能声明一个抽象函数
 * 如果想声明别的函数必须有默认实现
 */
@FunctionalInterface
public interface java8_FunctionalInterface {

    public void firstWork();

    default void doSomeMoreWork1() {
        //Method body
    }


}
