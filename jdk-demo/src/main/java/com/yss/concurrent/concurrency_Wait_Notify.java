package com.yss.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Wait：
 * wait()使当前线程阻塞，前提是必须先获得锁，配合synchronized 关键字使用，即，
 * 一般在synchronized 同步代码块里使用 wait()、notify/notifyAll() 方法
 * 当线程执行wait()方法时候，会释放当前的锁，然后让出CPU，进入等待状态
 * 只有当 notify/notifyAll() 被执行时候，才会唤醒一个或多个正处于等待状态的线程，
 * 然后继续往下执行，直到执行完synchronized 代码块
 *
 */


public class concurrency_Wait_Notify {
    static class SoulutionTask implements Runnable {
        static boolean isFinished = false;
        static volatile int value = 0;
        CountDownLatch countDownLatch;

        public SoulutionTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            while (value <= 100) {
                synchronized (SoulutionTask.class) {
                    System.out.println(Thread.currentThread().getName() + ":" + value++);
                    SoulutionTask.class.notify();
                    if (value >= 100) {
                        break;
                    }
                    try {
                        SoulutionTask.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            countDownLatch.countDown();

        }
    }

    public static void main(String[] args) throws InterruptedException {

        SoulutionTask.value = 0;
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread thread1 = new Thread(new SoulutionTask(countDownLatch), "偶数");
        Thread thread2 = new Thread(new SoulutionTask(countDownLatch), "奇数");
        thread1.start();
        thread2.start();
        countDownLatch.await();

    }
}
