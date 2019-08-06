package com.yss.concurrent;

import java.util.concurrent.*;

/**
 * @author yss
 * @date 2019/8/6下午1:31
 * @description: TODO
 */
public class concurrency_Semaphore {

        public static void main(String[] args) {
            //阻塞队列
            BlockingQueue<String> parks = new LinkedBlockingQueue<>(5);

            parks.offer("车位一");
            parks.offer("车位二");
            parks.offer("车位三");
            parks.offer("车位四");
            parks.offer("车位五");

            ExecutorService executorService = Executors.newCachedThreadPool();

            //如博文中所说的初始值为5， 专业的说法就是5个许可证
            Semaphore semaphore = new Semaphore(5);

            for (int i = 0; i < 100; i++) {
                final int no = i;
                Thread t1 = new Thread(() -> {
                    try {
                        /**
                         * 获取许可，首先判断semaphore内部的数字是否大于0，如果大于0，
                         * 才能获得许可，然后将初始值5减去1，线程才会接着去执行；如果没有
                         * 获得许可(原因是因为已经有5个线程获得到许可，semaphore内部的数字为0)，
                         * 线程会阻塞直到已经获得到许可的线程，调用release()方法，释放掉许可，
                         * 也就是将semaphore内部的数字加1，该线程才有可能获得许可。
                         */
                        semaphore.acquire();
                        /**
                         *  对应的线程会到阻塞对，对应车辆去获取到车位，如果没有拿到一致阻塞，
                         *  直到其他车辆归还车位。
                         */
                        String park = parks.take();
                        System.out.println("车辆【" + no + "】获取到: " + park);
                        Thread.sleep((long) Math.random() * 6000);
                        parks.offer(park);  //归还车位
                        semaphore.release(); //线程释放掉许可，通俗来将就是将semaphore内部的数字加1
                        System.out.println("车辆【" + no + "】离开 " + park);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                executorService.execute(t1);
            }
        }



}
