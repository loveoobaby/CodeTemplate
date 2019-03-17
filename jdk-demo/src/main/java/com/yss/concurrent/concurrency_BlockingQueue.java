package com.yss.concurrent;

import java.util.concurrent.*;

/**
 * BlockingQueue 具有 4 组不同的方法用于插入、移除以及对队列中的元素进行检查。
 * 如果请求的操作不能得到立即执行的话，每个方法的表现也不同。这些方法如下：
 *
 *   | 方法 | 抛异常     | 特定值    | 阻塞    | 超时
 *   |————————————————————————————————————————————————————————————————————
 *   | 插入 | add(o)    | offer(o)  | put(o) | offer(o, timeout, timeunit)
 *   | 移除 | remove(o) | offer(o)  | put(o) | offer(o, timeout, timeunit)
 *   | 检查 | element(o)| peek(o)
 *   -----------------------------------------------------------------------
 *
 *  实现类有：
 *    ArrayBlockingQueue
 *    DelayQueue
 *    LinkedBlockingQueue
 *    PriorityBlockingQueue
 *    SynchronousQueue
 *
 */
public class concurrency_BlockingQueue {


    public static void main(String[] args) throws InterruptedException {
        /**
         * ArrayBlockingQueue
         */
        // ArrayBlockingQueue 是一个有界的阻塞队列，其内部实现是将对象放到一个数组里。
        // 它不能够存储无限多数量的元素。在对其初始化的时候设定这个上限，但之后就无法对这个上限进行修改了
        ArrayBlockingQueue arrayQueue = new ArrayBlockingQueue(100);

        /**
         * DelayQueue
         */
        {
            //DelayQueue 实现了 BlockingQueue 接口。
            // DelayQueue 对元素进行持有直到一个特定的延迟到期。
            // 注入其中的元素必须实现 java.util.concurrent.Delayed 接口,
            DelayQueue queue = new DelayQueue();
            Delayed element1 = new DelayedElement(10);
            queue.put(element1);
            // 线程会死循环检查对象是否过期，比较浪费CPU
            Delayed element2 = queue.take();
        }


        /**
         * 具有优先级的阻塞队列 PriorityBlockingQueue
         *
         * PriorityBlockingQueue 是一个无界的并发队列。
         * 它使用了和类 java.util.PriorityQueue 一样的排序规则。
         * 你无法向这个队列中插入 null 值。所有插入到 PriorityBlockingQueue 的元素必须实现 java.lang.Comparable 接口
         */

        {
            BlockingQueue<String> queue   = new PriorityBlockingQueue<String>();

            //String implements java.lang.Comparable
            queue.put("Value");

            String value = queue.take();
        }

        /**
         * 同步队列 SynchronousQueue
         *
         * SynchronousQueue 是一个特殊的队列，它的内部同时只能够容纳单个元素。
         * 如果该队列已有一元素的话，试图向队列中插入一个新元素的线程将会阻塞，直到另一个线程将该元素从队列中抽走。
         * 同样，如果该队列为空，试图向队列中抽取一个元素的线程将会阻塞，直到另一个线程向队列中插入了一条新的元素。
         */



    }




    private static class DelayedElement implements Delayed {

        long delaySecond;
        long startSecond;

        public DelayedElement(long delaySecond){
            this.delaySecond = delaySecond;
            this.startSecond = System.currentTimeMillis() /1000;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            if((System.currentTimeMillis()/1000 - startSecond) > delaySecond){
                return -1;
            }
            return 1;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }



}
