package com.yss.java8;


import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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

        {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");

            joiner.add("How")
                    .add("To")
                    .add("Do")
                    .add("In")
                    .add("Java");
            System.out.println(joiner);
        }

        {
            List<String> numbers = Arrays.asList("How", "To", "Do", "In", "Java");

            String joinedString =   numbers
                    .stream()
                    .collect(Collectors.joining(", ","[","]"));

            System.out.println(joinedString);
        }


    }

}
