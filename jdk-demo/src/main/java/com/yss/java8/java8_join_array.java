package com.yss.java8;


import java.util.Arrays;
import java.util.List;

public class java8_join_array {

    public static void main(String[] args) {

        {
            String joinedString = String.join(", ", "How", "To", "Do", "In", "Java");
            System.out.println(joinedString);
        }

        {
            List<String> strList = Arrays.asList("How", "To", "Do", "In", "Java");

            String joinedString = String.join(", ", strList);

            System.out.println(joinedString);
        }


    }

}
