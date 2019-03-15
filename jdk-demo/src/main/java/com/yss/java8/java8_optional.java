package com.yss.java8;

import java.util.Optional;

/**
 * @author yss
 * @date 2019/3/14下午4:38
 * @description: TODO
 */
public class java8_Optional {

    public static void main(String[] args) {

        {
            Optional<Integer> canBeEmpty1 = Optional.of(5);
            canBeEmpty1.isPresent();                    // returns true
            canBeEmpty1.get();                          // returns 5
            canBeEmpty1.ifPresent(System.out::println);
            if(canBeEmpty1.isPresent()){
                System.out.println(canBeEmpty1.get());
            }

            Optional<Integer> canBeEmpty2 = Optional.empty();
            canBeEmpty2.isPresent();                    // returns false
        }

        {
            // Default/absent values
            Optional<Integer> possible = Optional.ofNullable(null);
            System.out.println(possible.orElse(4));
            possible.orElseThrow(IllegalStateException::new);
        }

        /**
         * Optional 的正确使用方式
         *
         * 不建议：
         *    Optional<User> user = ……
         *     if (user.isPresent()) {
         *       return user.getOrders();
         *     } else {
         *       return Collections.emptyList();
         *     }
         *    =================================
         *
         *    if (user.isPresent()) {
         *      System.out.println(user.get());
         *    }
         *    =================================
         *
         *    if(user.isPresent()) {
         *        return user.get().getOrders();
         *    } else {
         *       return Collections.emptyList();
         *    }
         *
         * 建议：
         *    return user.orElse(null);
         *    return user.orElseGet(() -> fetchAUserFromDatabase()); //而不要 return user.isPresent() ? user: fetchAUserFromDatabase();
         *    =================================
         *
         *    user.ifPresent(System.out::println);
         *    =================================
         *
         *    return user.map(u -> u.getOrders()).orElse(Collections.emptyList())
         */



    }
}
