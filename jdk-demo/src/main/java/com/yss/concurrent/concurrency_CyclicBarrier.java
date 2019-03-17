package com.yss.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier通过它可以实现让一组线程等待至某个状态之后再全部同时执行
 */
public class concurrency_CyclicBarrier {

    public static void main(String[] args) {
        // 定义所有线程达到同一汇合点之后，线程唤醒之前，执行的指令
        Runnable barrier1Action = new Runnable() {
            public void run() {
                System.out.println("BarrierAction 1 executed ");
            }
        };

        CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
        CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(barrier1);
//        CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(barrier1);

        new Thread(barrierRunnable1).start();
//        new Thread(barrierRunnable2).start();
    }

    public static class CyclicBarrierRunnable implements Runnable {
        CyclicBarrier barrier1 = null;

        public CyclicBarrierRunnable(CyclicBarrier barrier1) {
            this.barrier1 = barrier1;
        }

        public void run() {
            try {
//                Thread.sleep(Math.round(Math.random()*1000*1000));
                System.out.println(Thread.currentThread().getName() + " waiting at barrier 1");
                this.barrier1.await();
                System.out.println(Thread.currentThread().getName() + " done!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
