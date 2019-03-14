package com.yss.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class java8_forEach {

    public static void main(String[] args) {

        // List ForEach遍历
        {
            ArrayList<Integer> numberList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

            Consumer<Integer> action = System.out::println;

            numberList.forEach(action);
            numberList.stream().filter(n -> n % 2 == 0).forEach(action);
        }

        // Map 遍历
        {
            HashMap<String, Integer> map = new HashMap<>();

            map.put("A", 1);
            map.put("B", 2);
            map.put("C", 3);

            Consumer<Map.Entry<String, Integer>> action = System.out::println;

            map.entrySet().forEach(action);

            Consumer<String> actionOnKeys = System.out::println;

            map.keySet().forEach(actionOnKeys);

            Consumer<Integer> actionOnValues = System.out::println;

            map.values().forEach(actionOnValues);

            map.forEach((k, v) -> {
                System.out.println("key =" + k);
                System.out.println("value = " + v);
            });
        }

        // 自定义Consumer
        {
            HashMap<String, Integer> map = new HashMap<>();

            map.put("A", 1);
            map.put("B", 2);
            map.put("C", 3);

            Consumer<Map.Entry<String, Integer>> action = entry ->
            {
                System.out.println("Key is : " + entry.getKey());
                System.out.println("Value is : " + entry.getValue());
            };

            map.entrySet().forEach(action);
        }

    }
}
