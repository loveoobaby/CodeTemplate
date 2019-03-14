package com.yss.java8;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64是一种用64个字符来表示任意二进制数据的方法
 */
public class java8_base64 {

    public static void main(String[] args) {
        Base64.Encoder encoder = Base64.getEncoder();
        String normalString = "username:password";
        String encodedString = encoder.encodeToString(
                normalString.getBytes(StandardCharsets.UTF_8) );
        System.out.println(encodedString);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(encodedString);
        System.out.println(new String(decodedByteArray));
    }

}
