package com.yss.henghe.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class transaction_sender {


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        String queueName = "test";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("henghe-125");

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);

        channel.txSelect();
        for (int i = 0; i < 100000; i++) {
            try{
                System.out.println(i);
                String message = "" + System.currentTimeMillis();
                channel.basicPublish("", queueName, null, message.getBytes());
                if(i%2==0){
                    throw new RuntimeException("");
                }
                channel.txCommit();
            }catch (Exception e){
                channel.txRollback();
            }
        }

        channel.close();
        conn.close();

    }

}
