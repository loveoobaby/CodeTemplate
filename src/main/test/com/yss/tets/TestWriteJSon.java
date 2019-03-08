package com.yss.tets;

/**
 * @author yss
 * @date 2019/3/7下午5:56
 * @description: TODO
 */
public class TestWriteJSon {

    public static void main(String[] args) {
        String name = "";
        String template = "        String QUEUE_NAME = ;\n" +
                "        ConnectionFactory factory = new ConnectionFactory();\n" +
                "        factory.setUsername();\n" +
                "        factory.setPassword();\n" +
                "        factory.setHost();\n" +
                "\n" +
                "        Connection conn = factory.newConnection();\n" +
                "        Channel channel = conn.createChannel();\n" +
                "\n" +
                "        channel.queueDeclare(QUEUE_NAME, true, false, false, null);\n" +
                "        \n" +
                "        String message = ;\n" +
                "        channel.basicPublish(\"\", QUEUE_NAME, null, message.getBytes());\n" +
                "        \n" +
                "        channel.close();\n" +
                "        conn.close();";

    }

}
