package com.yss.java8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class java8_Date_Api {

    public static void main(String[] args) {
        //  LocalDate用于处理日期, 不能处理精确的时间或时区，是一个不可变的类，默认格式(yyyy-MM-dd)
        {
            LocalDate localDate = LocalDate.now();
            System.out.println(localDate.toString());                //2019-03-14
            System.out.println(localDate.getDayOfWeek().toString()); //THURSDAY
            System.out.println(localDate.getDayOfMonth());           //14
            System.out.println(localDate.getDayOfYear());            //73
            System.out.println(localDate.isLeapYear());              //false
            System.out.println(localDate.plusDays(12).toString());   //2019-03-26
        }

        System.out.println("========================");

        // LocalTime是具体的时间，但没有日期信息
        {
            //LocalTime localTime = LocalTime.now();     //toString() in format 09:57:59.744
            LocalTime localTime = LocalTime.of(12, 20);
            System.out.println(localTime.toString());    //12:20
            System.out.println(localTime.getHour());     //12
            System.out.println(localTime.getMinute());   //20
            System.out.println(localTime.getSecond());   //0
            System.out.println(localTime.MIDNIGHT);      //00:00
            System.out.println(localTime.NOON);          //12:00
        }


        // LocalDateTime既有具体的时间，还有日期信息
        {
//            LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            LocalDateTime localDateTime = LocalDateTime.now();
            System.out.println(localDateTime.toString());      //2013-05-15T10:01:14.911
            System.out.println(localDateTime.getDayOfMonth()); //15
            System.out.println(localDateTime.getHour());       //10
            System.out.println(localDateTime.getNano());       //911000000

            // 时间加减
            localDateTime.plusDays(1);
            localDateTime.minusHours(3);

            // LocalDateTime格式化输出
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String localTime = df.format(time);
            System.out.println(localTime);

            // String转LocalDateTime
            LocalDateTime ldt = LocalDateTime.parse("2018-09-08 17:07:05",df);
            System.out.println(ldt);
        }








    }

}


