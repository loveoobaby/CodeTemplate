package com.yss.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * CountDownLatch的作用是一个计数器，每个线程执行完毕后会执行countDown，
 * 表示自己已结束，这对于多个子任务的计算特别有效。例如一个任务可以分成10个子任务执行，
 * 主线程必须要知道子线程任务是否已完成，所有子任务完成后，主线程进行合并计算。
 */
public class concurrency_CountDownLatch {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int num = 10;
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(num);

        ExecutorService es = Executors.newFixedThreadPool(num);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            futures.add(es.submit(new Runner(begin, end)));
        }
        begin.countDown();
        end.await();

        int count = 0;
        for (Future<Integer> f: futures) {
            count += f.get();
        }
        System.out.println("平均分数为：" + count / num);
        es.shutdown();

    }


    private static class Runner implements Callable<Integer> {

        private CountDownLatch begin;
        private CountDownLatch end;

        public Runner(CountDownLatch begin, CountDownLatch end){
            this.begin = begin;
            this.end = end;
        }

        @Override
        public Integer call() throws Exception {
            int score = new Random().nextInt(25);
            begin.await();
            TimeUnit.SECONDS.sleep(score);
            end.countDown();
            return score;
        }
    }



}





