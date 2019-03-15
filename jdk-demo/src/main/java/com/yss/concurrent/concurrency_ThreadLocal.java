package com.yss.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  ThreadLocal实现的数据结构：
 *
 *  Stack                             Heap
 * -----------------------          -----------------------------------------------------|
 * | TheadLocal Ref      |  -----------> Thread Local Object ______________________      |
 * |                     |          |                          _________________   |     |
 * | Current Thread Ref  |  -----------> Thread Object  ----> | ThreadLocalMap |   |     |
 * -----------------------          |                         |key :   <-----------|     |
 *                                  |                         |value:          |         |
 *                                  |                         |________________|         |
 *                                  |____________________________________________________|
 */


public class concurrency_ThreadLocal implements Runnable {
    private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return nextId.getAndIncrement();
        }
    };

    public int getThreadId() {
        return threadId.get();
    }

    private static final ThreadLocal<Date> startDate = new ThreadLocal<Date>() {
        protected Date initialValue() {
            return new Date();
        }
    };


    @Override
    public void run() {
        System.out.printf("Starting Thread: %s : %s\n", getThreadId(), startDate.get());
        try {
            TimeUnit.SECONDS.sleep((int) Math.rint(Math.random()*10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread Finished: %s : %s\n", getThreadId(), startDate.get());
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new concurrency_ThreadLocal()).start();
        }
    }
}