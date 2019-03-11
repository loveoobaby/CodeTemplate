package com.yss.tets;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.util.io.FileUtil;
import com.yss.codetemplate.CodeTemplateSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author yss
 * @date 2019/3/7下午5:56
 * @description: TODO
 */
public class TestWriteJSon {

    public static void main(String[] args) throws IOException {


        JSONArray array = new JSONArray();

        {
            String name = "rabbit_auto_ack_product";
            String template = "        // 要发送的队列\n" +
                    "        String QUEUE_NAME = ;\n" +
                    "        \n" +
                    "        // 创建连接工厂\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        // 创建连接\n" +
                    "        Connection conn = factory.newConnection();\n" +
                    "        Channel channel = conn.createChannel();\n" +
                    "        \n" +
                    "        // 声明队列\n" +
                    "        channel.queueDeclare(QUEUE_NAME, true, false, false, null);\n" +
                    "\n" +
                    "        // 发送消息\n" +
                    "        String message = ;\n" +
                    "        channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes());\n" +
                    "\n" +
                    "        // 关闭连接\n" +
                    "        channel.close();\n" +
                    "        conn.close();";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "rabbit_auto_ack_consumer";
            String template = "        // 消费的队列\n" +
                    "        String QUEUE_NAME = ;\n" +
                    "\n" +
                    "        // 创建连接工厂\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setHost();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        // 创建连接\n" +
                    "        Connection connection = factory.newConnection();\n" +
                    "        Channel channel = connection.createChannel();\n" +
                    "\n" +
                    "        // 声明消费的队列\n" +
                    "        channel.queueDeclare(QUEUE_NAME, true, false, false, null);\n" +
                    "\n" +
                    "        // 消费者CallBack\n" +
                    "        DeliverCallback deliverCallback = (consumerTag, delivery) -> {\n" +
                    "            String message = new String(delivery.getBody(), \"UTF-8\");\n" +
                    "            System.out.println(\" [x] Received '\" + message + \"'  \" + Thread.currentThread());\n" +
                    "            try {\n" +
                    "                Thread.sleep(1000);\n" +
                    "            } catch (InterruptedException e) {\n" +
                    "                e.printStackTrace();\n" +
                    "            }\n" +
                    "        };\n" +
                    "        // 注册消费者\n" +
                    "        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }


        {
            String name = "rabbit_confirm_product";
            String template = "        // 创建ConnectionFactory\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        // 创建连接\n" +
                    "        Connection conn = factory.newConnection();\n" +
                    "        Channel channel = conn.createChannel();\n" +
                    "\n" +
                    "        // 声明队列，并切换到confirm模式\n" +
                    "        channel.queueDeclare(QUEUE_NAME, true, false, false, null);\n" +
                    "        channel.confirmSelect();\n" +
                    "\n" +
                    "        // 发送消息\n" +
                    "        String message = ;\n" +
                    "        channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes());\n" +
                    "        try {\n" +
                    "            // 等待Confirm结果\n" +
                    "//                boolean confirm = channel.waitForConfirms();\n" +
                    "            boolean confirm = channel.waitForConfirms(100);\n" +
                    "\n" +
                    "            System.out.println(\"confirm result : \" + confirm);\n" +
                    "        } catch (Exception e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        }\n" +
                    "\n" +
                    "        channel.close();\n" +
                    "        conn.close();";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "rabbit_transaction_product";
            String template = "        // 发送的队列\n" +
                    "        String QUEUE_NAME = ;\n" +
                    "        // 创建连接工厂\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        // 创建连接\n" +
                    "        Connection conn = factory.newConnection();\n" +
                    "        Channel channel = conn.createChannel();\n" +
                    "\n" +
                    "        // 声明消费队列\n" +
                    "        channel.queueDeclare(QUEUE_NAME, true, false, false, null);\n" +
                    "\n" +
                    "        // 切换到事物模式\n" +
                    "        channel.txSelect();\n" +
                    "\n" +
                    "        String message = \"\" + System.currentTimeMillis();\n" +
                    "        channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes());\n" +
                    "        // 提交事物\n" +
                    "        channel.txCommit();\n" +
                    "\n" +
                    "        channel.close();\n" +
                    "        conn.close();";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }



        {
            String name = "rabbit_exclusive_product";
            String template = "        String QUEUE_NAME = ;\n" +
                    "\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        Connection conn = factory.newConnection();\n" +
                    "        Channel channel = conn.createChannel();\n" +
                    "\n" +
                    "        // 声明排他性队列，是针对首次建立连接的，一个连接下面多个通道也是可见的， 对于其他连接是不可见的，\n" +
                    "        // 客户端程序将一个排他性的队列声明成了Durable的，只要调用了连接的Close方法或者客户端程序退出了，RabbitMQ都会删除这个队列\n" +
                    "        channel.queueDeclare(QUEUE_NAME, true, true, false, null);\n" +
                    "\n" +
                    "        String message = \"\" + System.currentTimeMillis();\n" +
                    "        channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes());\n" +
                    "\n" +
                    "        channel.close();\n" +
                    "        conn.close();";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "rabbit_publish_product";
            String template = "      // 发布订阅的消费者与普通消费者类似，只需手动或自动绑定生产者发送的交换机上即可\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        try (Connection connection = factory.newConnection();\n" +
                    "             Channel channel = connection.createChannel()) {\n" +
                    "            channel.exchangeDeclare(EXCHANGE_NAME, \"fanout\");\n" +
                    "\n" +
                    "            String message = argv.length < 1 ? \"info: Hello World!\" :\n" +
                    "                    String.join(\" \", argv);\n" +
                    "\n" +
                    "            channel.basicPublish(EXCHANGE_NAME, \"\", null, message.getBytes(\"UTF-8\"));\n" +
                    "            System.out.println(\" [x] Sent '\" + message + \"'\");\n" +
                    "        }";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }


        {
            String name = "rabbit_topic_product";
            String template = "        String EXCHANGE_NAME = ;\n" +
                    "        String routingKey = ;\n" +
                    "\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "\n" +
                    "        try (Connection connection = factory.newConnection();\n" +
                    "             Channel channel = connection.createChannel()) {\n" +
                    "            channel.exchangeDeclare(EXCHANGE_NAME, \"topic\");\n" +
                    "\n" +
                    "            String message = argv.length < 1 ? \"info: Hello World!\" :\n" +
                    "                    String.join(\" \", argv);\n" +
                    "\n" +
                    "            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(\"UTF-8\"));\n" +
                    "            System.out.println(\" [x] Sent '\" + message + \"'\");\n" +
                    "        }";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }



        {
            String name = "rabbit_RPC_Client";
            String template = "     private static class RPCClient implements AutoCloseable {\n" +
                    "\n" +
                    "            private Connection connection;\n" +
                    "            private Channel channel;\n" +
                    "            private String requestQueueName = \"rpc_queue\";\n" +
                    "            private String rpcResponseQueue = \"rpc_response\";\n" +
                    "            private final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);\n" +
                    "\n" +
                    "            public RPCClient() throws IOException, TimeoutException {\n" +
                    "                ConnectionFactory factory = new ConnectionFactory();\n" +
                    "                factory.setUsername(\"admin\");\n" +
                    "                factory.setPassword(\"admin\");\n" +
                    "                factory.setHost(\"henghe-125\");\n" +
                    "\n" +
                    "                connection = factory.newConnection();\n" +
                    "                channel = connection.createChannel();\n" +
                    "            }\n" +
                    "\n" +
                    "            public void init(){\n" +
                    "                try {\n" +
                    "                    // 初始化两个队列，一个用于发送请求，一个用于接受响应\n" +
                    "                    channel.queueDeclare(rpcResponseQueue, true, false, false, null);\n" +
                    "                    channel.basicConsume(rpcResponseQueue, true, (consumerTag, delivery) -> {\n" +
                    "                        response.offer(new String(delivery.getBody(), \"UTF-8\"));\n" +
                    "                    }, consumerTag -> {\n" +
                    "                    });\n" +
                    "                } catch (IOException e) {\n" +
                    "                    e.printStackTrace();\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            public static void main(String[] argv) {\n" +
                    "                try (RPCClient fibonacciRpc = new RPCClient()) {\n" +
                    "                    fibonacciRpc.init();\n" +
                    "                    long init = System.currentTimeMillis();\n" +
                    "                    for (int i = 0; i < 10000; i++) {\n" +
                    "                        long start = System.currentTimeMillis();\n" +
                    "                        String i_str = Integer.toString(8);\n" +
                    "                        System.out.println(\" [x] Requesting fib(\" + i_str + \")\");\n" +
                    "                        String response = fibonacciRpc.call(i_str);\n" +
                    "                        System.out.println(\" [.] Got '\" + response + \"' \" + (System.currentTimeMillis() - start));\n" +
                    "                    }\n" +
                    "                    System.out.println(\" [.] Got '\" + \"' \" + (System.currentTimeMillis() - init));\n" +
                    "                } catch (IOException | TimeoutException | InterruptedException e) {\n" +
                    "                    e.printStackTrace();\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            public String call(String message) throws IOException, InterruptedException {\n" +
                    "                final String corrId = UUID.randomUUID().toString();\n" +
                    "\n" +
                    "                AMQP.BasicProperties props = new AMQP.BasicProperties\n" +
                    "                        .Builder()\n" +
                    "                        .correlationId(corrId)\n" +
                    "                        .replyTo(rpcResponseQueue)\n" +
                    "                        .build();\n" +
                    "\n" +
                    "                channel.basicPublish(\"\", requestQueueName, props, message.getBytes(\"UTF-8\"));\n" +
                    "                String result = response.take();\n" +
                    "                return result;\n" +
                    "            }\n" +
                    "\n" +
                    "            public void close() throws IOException {\n" +
                    "                connection.close();\n" +
                    "            }\n" +
                    "        }";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }


        {
            String name = "rabbit_RPC_Server";
            String template = "public class RPCServer {\n" +
                    "\n" +
                    "    private static final String RPC_QUEUE_NAME = \"rpc_queue\";\n" +
                    "\n" +
                    "    private static int fib(int n) {\n" +
                    "        if (n == 0) return 0;\n" +
                    "        if (n == 1) return 1;\n" +
                    "        return fib(n - 1) + fib(n - 2);\n" +
                    "    }\n" +
                    "\n" +
                    "    public static void main(String[] argv) throws Exception {\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername(\"admin\");\n" +
                    "        factory.setPassword(\"admin\");\n" +
                    "        factory.setHost(\"henghe-125\");\n" +
                    "\n" +
                    "        try (Connection connection = factory.newConnection();\n" +
                    "             Channel channel = connection.createChannel()) {\n" +
                    "            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);\n" +
                    "            channel.queuePurge(RPC_QUEUE_NAME);\n" +
                    "\n" +
                    "            channel.basicQos(1);\n" +
                    "\n" +
                    "            System.out.println(\" [x] Awaiting RPC requests\");\n" +
                    "\n" +
                    "            Object monitor = new Object();\n" +
                    "            DeliverCallback deliverCallback = (consumerTag, delivery) -> {\n" +
                    "                AMQP.BasicProperties replyProps = new AMQP.BasicProperties\n" +
                    "                        .Builder()\n" +
                    "                        .correlationId(delivery.getProperties().getCorrelationId())\n" +
                    "                        .build();\n" +
                    "\n" +
                    "                String response = \"\";\n" +
                    "\n" +
                    "                try {\n" +
                    "//                    System.out.println(delivery.getProperties());\n" +
                    "                    String message = new String(delivery.getBody(), \"UTF-8\");\n" +
                    "                    int n = Integer.parseInt(message);\n" +
                    "\n" +
                    "                    System.out.println(\" [.] fib(\" + message + \")\");\n" +
                    "                    response += fib(n);\n" +
                    "                } catch (RuntimeException e) {\n" +
                    "                    System.out.println(\" [.] \" + e.toString());\n" +
                    "                } finally {\n" +
                    "                    System.out.println(response);\n" +
                    "                    channel.basicPublish(\"\", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(\"UTF-8\"));\n" +
                    "                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);\n" +
                    "                    // RabbitMq consumer worker thread notifies the RPC server owner thread\n" +
                    "                    synchronized (monitor) {\n" +
                    "                        monitor.notify();\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            };\n" +
                    "\n" +
                    "            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));\n" +
                    "            // Wait and be prepared to consume the message from RPC client.\n" +
                    "            while (true) {\n" +
                    "                synchronized (monitor) {\n" +
                    "                    try {\n" +
                    "                        monitor.wait();\n" +
                    "                    } catch (InterruptedException e) {\n" +
                    "                        e.printStackTrace();\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }


        {
            String name = "rabbit_nio_product";
            String template = "        String EXCHANGE_NAME = ;\n" +
                    "        String routingKey = ;\n" +
                    "\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "        factory.useNio();\n" +
                    "        factory.setRequestedHeartbeat(60);\n" +
                    "\n" +
                    "        factory.setAutomaticRecoveryEnabled(true);\n" +
                    "        factory.setNetworkRecoveryInterval(1000);\n" +
                    "\n" +
                    "        factory.setNioParams(new NioParams().setNbIoThreads(4));\n" +
                    "\n" +
                    "        try (Connection connection = factory.newConnection();\n" +
                    "             Channel channel = connection.createChannel()) {\n" +
                    "            channel.exchangeDeclare(EXCHANGE_NAME, \"topic\");\n" +
                    "\n" +
                    "            String message = argv.length < 1 ? \"info: Hello World!\" :\n" +
                    "                    String.join(\" \", argv);\n" +
                    "            for (int i =0; i<100000; i++) {\n" +
                    "                try {\n" +
                    "                    channel.basicPublish(EXCHANGE_NAME, \"log.error\", null, message.getBytes(\"UTF-8\"));\n" +
                    "                    System.out.println(\" [x] Sent '\" + i + \"'\");\n" +
                    "                }catch (Exception e){\n" +
                    "                    e.printStackTrace();\n" +
                    "                    Thread.sleep(1000);\n" +
                    "                }\n" +
                    "\n" +
                    "            }\n" +
                    "\n" +
                    "        }";

            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "rabbit_nio_consumer";
            String template = "        String QUEUE_NAME = ;\n" +
                    "\n" +
                    "        ConnectionFactory factory = new ConnectionFactory();\n" +
                    "        factory.setUsername();\n" +
                    "        factory.setPassword();\n" +
                    "        factory.setHost();\n" +
                    "        factory.useNio();\n" +
                    "\n" +
                    "        Connection connection = factory.newConnection();\n" +
                    "        Channel channel = connection.createChannel();\n" +
                    "\n" +
                    "        System.out.println(\" [*] Waiting for messages. To exit press CTRL+C\");\n" +
                    "\n" +
                    "\n" +
                    "        DeliverCallback deliverCallback = (consumerTag, delivery) -> {\n" +
                    "            String message = new String(delivery.getBody(), \"UTF-8\");\n" +
                    "            System.out.println(\" [x] Received '\" + message + \"'\");\n" +
                    "        };\n" +
                    "        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });";

            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }

        {
            String name = "spring_kafka_create_topic";
            String template = "@Bean\n" +
                    "            public NewTopic topic1() {\n" +
                    "            return new NewTopic(\"thing1\", 10, (short) 2);";

            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        {
            String name = "spring_kafka_create_admin";
            String template = "            @Bean\n" +
                    "            public KafkaAdmin admin() {\n" +
                    "            Map<String, Object> props = new HashMap<>();\n" +
                    "            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, \"henghe-122:9092,henghe-123:9092,henghe-124:9092\");\n" +
                    "            return new KafkaAdmin(props);";

            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);

        }

        writeDataToFile(array.toJSONString());


    }

    private static void writeDataToFile(String jsonFormatData) throws IOException {
        FileUtil.writeToFile(new File("/Users/yss/work/CodeTemplate/src/main/resources/template/default.json"), jsonFormatData);

    }

}
