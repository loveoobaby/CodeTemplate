package com.yss.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream 是 Java8 中处理集合的关键抽象概念，
 * 它可以指定你希望对集合进行的操作，可以执行非常复杂的查找、过滤和映射数据等操作
 */
public class java8_Stream {
    public static void main(String[] args) {

        //创建Stream
        {
            {
                // Stream.of(val1, val2, val3….)
                Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9);
                stream.forEach(p -> System.out.println(p));
            }

            {
                // Stream.of(arrayOfElements)
                Stream<Integer> stream = Stream.of( new Integer[]{1,2,3,4,5,6,7,8,9} );
                stream.forEach(p -> System.out.println(p));
            }

            {
                // List.stream()
                List<Integer> list = new ArrayList<Integer>();

                for(int i = 1; i< 10; i++){
                    list.add(i);
                }

                Stream<Integer> stream = list.stream();
                stream.forEach(p -> System.out.println(p));
            }


        }

        /**
         * stream转换成collections
         */
        {
            List<Integer> list = new ArrayList<Integer>();
            for(int i = 1; i< 10; i++){
                list.add(i);
            }
            Stream<Integer> stream = list.stream();
            List<Integer> evenNumbersList = stream.filter(i -> i%2 == 0).collect(Collectors.toList());
            System.out.print(evenNumbersList);

//            Integer[] evenNumbersArr = stream.filter(i -> i%2 == 0).toArray(Integer[]::new);
//            System.out.print(evenNumbersArr);
//
//            Set<Integer> evenNumbersSet = stream.filter(i -> i%2 == 0).collect(Collectors.toSet());
//            System.out.println(evenNumbersSet);
        }

        /**
         * 核心API
         */
        {
            List<String> memberNames = new ArrayList<>();
            memberNames.add("Amitabh");
            memberNames.add("Shekhar");
            memberNames.add("Aman");
            memberNames.add("Rahul");
            memberNames.add("Shahrukh");
            memberNames.add("Salman");
            memberNames.add("Yana");
            memberNames.add("Lokesh");

            memberNames.stream()
                    .filter((s) -> s.startsWith("A")) //过滤
                    .map(String::toUpperCase) // 转换映射
                    .sorted()  // 排序，返回一个排序的视图
                    .forEach(System.out::println); // 终止操作，输出结果
            /**
             * 终止的操作有：
             *    1. forEach
             *    2. collect
             *    3. match
             *    4. count
             *    5. reduce
             */

            boolean matchedResult = memberNames.stream()
                    .anyMatch((s) -> s.startsWith("A"));

            System.out.println(matchedResult);

            matchedResult = memberNames.stream()
                    .allMatch((s) -> s.startsWith("A"));

            System.out.println(matchedResult);

            matchedResult = memberNames.stream()
                    .noneMatch((s) -> s.startsWith("A"));

            System.out.println(matchedResult);


        }

        {
            /**
             * 可以使用-Djava.util.concurrent.ForkJoinPool.common.parallelism={线程数量} 来控制fork/join线程池的并发度
             * 使用并行流需要注意线程安全问题
             */
            // Parallelism
            List<Integer> list = new ArrayList<Integer>();
            for(int i = 1; i< 10; i++){
                list.add(i);
            }
            //创建并行流，内部使用了用fork/join框架提供并行执行能力
            Stream<Integer> stream = list.parallelStream();
            Integer[] evenNumbersArr = stream.filter(i -> i%2 == 0).toArray(Integer[]::new);
            System.out.print(evenNumbersArr);
        }
    }
}
