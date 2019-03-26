package com.yss.henghe.rabbit.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class topic_mode {

    private static class BasicConnection {
        private String userName;
        private String passwd;
        private String host;
        protected Connection connection;
        protected Channel channel;

        protected BasicConnection(String userName, String passwd, String host){
            this.userName = userName;
            this.passwd = passwd;
            this.host = host;
        }

        protected void init() throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(userName);
            factory.setPassword(passwd);
            factory.setHost(host);

            connection = factory.newConnection();
            channel = connection.createChannel();
        }

        public void close() throws IOException, TimeoutException {
            this.channel.close();
            this.connection.close();
        }


    }

    private static class Sender extends BasicConnection {
        private String exchangeName;
        private String routingKey;

        public Sender(String userName, String passwd, String host, String exchangeName, String routingKey) throws IOException, TimeoutException {
            super(userName, passwd, host);
            this.exchangeName = exchangeName;
            super.init();
            channel.exchangeDeclare(exchangeName, "topic");
            this.routingKey = routingKey;
        }

        public Sender send() throws IOException {
            for(int i=0;i<1;i++){
                String message = "" + i;
                channel.basicPublish(this.exchangeName, routingKey, null, message.getBytes("UTF-8"));
            }
            return this;
        }


    }


    private static class Consumer extends BasicConnection {
        private  String queueName;


        public Consumer(String userName, String passwd, String host, String queueName, String exChangeName, String routingKey) throws IOException, TimeoutException {
            super(userName, passwd, host);
            super.init();
            this.queueName = queueName;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exChangeName, routingKey);
        }


        public Consumer consum() throws IOException {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    System.out.println("==>" + new String(delivery.getBody()));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                }catch (Exception e){
                    /** requeue: 消息是否重入队列  */
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
            return this;
        }

    }




    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        /**
         * 如果MQ没有相应的交换机请先建立
         */
        System.out.println("begin consume");
        new Consumer("admin", "admin", "henghe-125", "test1", "topic_exhange", "log.error").consum();
        new Consumer("admin", "admin", "henghe-125", "test2", "topic_exhange", "log.info").consum();
        new Consumer("admin", "admin", "henghe-125", "test3", "topic_exhange", "log.debug").consum();
        new Consumer("admin", "admin", "henghe-125", "test4", "topic_exhange", "log.error").consum();

        Sender sender = new Sender("admin", "admin", "henghe-125", "topic_exhange", "log.error");
        sender.send().close();
    }
}
