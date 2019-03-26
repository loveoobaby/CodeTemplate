package com.yss.henghe.rabbit.confirm;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class confirm_sender {

    public static void main(String[] args) throws IOException, TimeoutException {
        String queueName = "test";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("henghe-125");

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();

        for (int i = 0; i < 100000; i++) {
            String message = "" + System.currentTimeMillis();
            channel.basicPublish("", queueName, null, message.getBytes());
            try {
                boolean confirm = channel.waitForConfirms(100);
                System.out.println("confirm result : " + confirm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        channel.close();
        conn.close();
    }

}
