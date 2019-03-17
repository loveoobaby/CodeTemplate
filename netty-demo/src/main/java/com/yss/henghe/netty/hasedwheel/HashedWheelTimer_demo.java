package com.yss.henghe.netty.hasedwheel;

import io.netty.util.HashedWheelTimer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author yss
 * @date 2019/3/17上午9:38
 * @description: TODO
 */
public class HashedWheelTimer_demo {

    public static void main(String[] args) throws InterruptedException {

        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);
            System.out.println("start1:" + LocalDateTime.now().format(formatter));
            hashedWheelTimer.newTimeout(timeout -> {
                System.out.println("task1 :" + LocalDateTime.now().format(formatter));
            }, 3, TimeUnit.SECONDS);
        }


        /**
         * 当前一个任务执行时间过长的时候，会影响后续任务的到期执行时间的。也就是说其中的任务是串行执行的。所以，要求里面的任务都要短平快
         */
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);
            System.out.println("start2:" + LocalDateTime.now().format(formatter));
            hashedWheelTimer.newTimeout(timeout -> {
                Thread.sleep(3000);
                System.out.println("task3:" + LocalDateTime.now().format(formatter));
            }, 3, TimeUnit.SECONDS);
            hashedWheelTimer.newTimeout(timeout -> System.out.println("task4:" + LocalDateTime.now().format(
                    formatter)), 4, TimeUnit.SECONDS);
        }
    }
}
