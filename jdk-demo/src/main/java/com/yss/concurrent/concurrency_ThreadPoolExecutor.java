package com.yss.concurrent;


import java.util.concurrent.*;


public class concurrency_ThreadPoolExecutor {

    /**
     * ThreadPoolExecutor构造方法：
     * public ThreadPoolExecutor(int corePoolSize,
     *                           int maximumPoolSize,
     *                           long keepAliveTime,
     *                           TimeUnit unit,
     *                           BlockingQueue<Runnable> workQueue,
     *                           ThreadFactory threadFactory,
     *                           RejectedExecutionHandler handler)
     *
     *
     * - corePoolSize：核心线程数。线程启动时，在池中保持线程的最小数量。但线程数量是逐步增加到corePoolSize值的
     * - maximumPoolSize：池中允许的最大线程数量，如果超出，则使用RejectedExecutionHandler拒绝策略处理。
     * - keepAliveTime：如果线程数量超过了corePoolSize，并且有处于非运行状态的线程，如果等待状态的时间超过了keepAliveTime指定的时间，则结束这部分线程。
     * - workQueue：任务队列。当线程池中的线程都处于运行状态，而此时任务数量继续增加，这部分任务会放入任务队列。
     * - threadFactory：线程工厂，定义如何创建一个线程。
     * - handler：拒绝任务处理器：当超出线程数量和队列容量时继续增加任务会时的处理策略。
     *
     *
     * 线程池的工作过程：首先创建线程池，然后根据任务的数量逐步将线程增大到corePoolSize数量，
     * 如果此时仍有任务增加，则放置到workQueue中，直到workQueue爆满为止，然后继续增加池中的线程数量，
     * 最终达到maximumPoolSize，如果继续还有新任务添加，这时需要handler处理，或丢弃新任务，或拒绝新任务，或挤占已有任务。
     */

    public static void main(String[] args) {
        /**
         *  Executors提供了创建线程池的便捷方法
         *  线程池不建议使用Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样
         *  的处理方式让作者更加明确线程池的运行规则，规避资源耗尽的风险
          */
        // 单线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //线程数量没有限制的线程池
        Executors.newCachedThreadPool();

        // 固定线程数量的线程池
        Executors.newFixedThreadPool(5);


        // 自定义ThreadPoolExecutor
        {
            Integer threadCounter = 0;
            BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(50);

            ThreadPoolExecutor executor = new ThreadPoolExecutor(10,
                    20, 5000, TimeUnit.MILLISECONDS, blockingQueue);

            executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r,
                                              ThreadPoolExecutor executor) {
                    System.out.println("DemoTask Rejected : " + r);
                    System.out.println("Waiting for a second !!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Lets add another time : ");
                    executor.execute(r);
                }
            });
            // Let start all core threads initially
            // 预启动所有核心线程
            executor.prestartAllCoreThreads();
        }


    }


}
