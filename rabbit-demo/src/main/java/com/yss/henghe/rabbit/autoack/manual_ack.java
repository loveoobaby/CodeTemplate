package com.yss.henghe.rabbit.autoack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class manual_ack {

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
        private String queueName;

        public Sender(String userName, String passwd, String host, String queueName) throws IOException, TimeoutException {
            super(userName, passwd, host);
            this.queueName = queueName;
            super.init();
            /**
             * 队列声明：
             *     queue：队列名称
             *     durable： 是否持久化，默认数据放入内存，RabbitMQ重启将丢失数据
             *     exclusive： 是否排他队列，如果是排他队列：1. 当连接关闭时，队列将被删除；2. 其他消费者不能访问该队列
             *     autoDelete：当队列的消费者变为0时是否自动删除队列
             *     arguments：其他的配置参数
             */
            channel.queueDeclare(queueName, true, false, false, null);
        }


        public Sender send() throws IOException {
            for(int i=0;i<10000;i++){
                String message = "" + i;
                channel.basicPublish("", queueName, null, message.getBytes());
            }
            return this;
        }


    }


    private static class Consumer extends BasicConnection {
        private  String queueName;


        public Consumer(String userName, String passwd, String host, String queueName) throws IOException, TimeoutException {
            super(userName, passwd, host);
            super.init();
            this.queueName = queueName;
            channel.queueDeclare(queueName, true, false, false, null);
        }


        public Consumer consum() throws IOException {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    if(Integer.valueOf(new String(delivery.getBody()))%2 == 0){
                        throw new RuntimeException("");
                    }else {
                        /** 手动应答:
                         *      deliveryTag: 当前消费消息的Tag
                         *      multiple: 是否对小于该Tag的消息进行确认，有助于减少确认量，加快消费速度
                         */
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                }catch (Exception e){
                    /** requeue: 消息是否重入队列  */
                    System.out.println("==>" + new String(delivery.getBody()));
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
            return this;
        }

    }




    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Sender sender = new Sender("admin", "admin", "henghe-125", "lixj");
        sender.send().close();

        System.out.println("begin consume");
        new Consumer("admin", "admin", "henghe-125", "lixj").consum();
    }


}
