package com.yss.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 接口具有以下主要方法：
 * <p>
 * -- lock(): 获取锁，调用该方法的线程将会阻塞，直到 Lock 实例解锁
 * -- lockInterruptibly(): 线程也会阻塞，但可以响应中断
 * -- tryLock()： 试图获取锁，如果获取失败返回false，非阻塞方法
 * -- tryLock(long timeout, TimeUnit timeUnit): 类似于tryLock，可以指定等待时间
 * -- unlock(): 释放锁
 * <p>
 * <p>
 * 一个 Lock 对象和一个 synchronized 代码块之间的主要不同点是：
 * <p>
 * 1. synchronized 代码块不能够保证进入访问等待的线程的先后顺序。
 * 2. synchronized 不支持设置超时时间。
 * 3. synchronized 块必须被完整地包含在单个方法里。而一个 Lock 对象可以把它的 lock() 和 unlock() 方法的调用放在不同的方法里。
 * 4. synchronized 不响应中断
 * 5. Lock支持更细粒度的锁控制，可以实现读写锁分离，可以支持公平锁
 */
public class concurrency_Lock {

    /**
     * 重入锁（ReentrantLock）是一种可重入同步锁。
     */
    public static void main(String[] args) {
        final ExecutorService exec = Executors.newFixedThreadPool(4);

        final ReentrantLock lock = new ReentrantLock();


        final Runnable add = new Runnable() {

            public void run() {

                System.out.println("Pre " + Thread.currentThread().getName());

                lock.lock();

                try {

                    System.out.println("get lock " + Thread.currentThread().getName());
                    Thread.sleep(10 * 1000);
                    System.out.println("after sleep" + Thread.currentThread().getName());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }

        };

        for (int index = 0; index < 2; index++)

            exec.submit(add);

        exec.shutdown();


    }


}
