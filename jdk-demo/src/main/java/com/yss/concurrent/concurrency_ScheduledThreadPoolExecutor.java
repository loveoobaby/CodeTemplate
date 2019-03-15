package com.yss.concurrent;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yss
 * @date 2019/3/15上午9:56
 * @description: TODO
 */
public class concurrency_ScheduledThreadPoolExecutor {


    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

        executor.scheduleWithFixedDelay(new Task("print timer"), 2, 2, TimeUnit.SECONDS);
    }


    private static class Task implements Runnable {
        private String name;

        public Task(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void run() {
            System.out.println("Executing : " + name + ", Current Seconds : " + new Date().getSeconds());
        }
    }
}
