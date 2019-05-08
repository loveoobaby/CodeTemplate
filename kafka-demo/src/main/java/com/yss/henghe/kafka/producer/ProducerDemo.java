package com.yss.henghe.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author ttylang
 * @Description TODO
 * @Date 2019-04-22
 */
public class ProducerDemo {
    static Properties props = new Properties();

    static void init() {
        //broker server 地址列表
        props.put("bootstrap.servers", "henghe-101-21:9092,henghe-101-22:9092");
        // 消息确认机制： all是分leader与isr复本全部写成功才确认消息，性能最低，0 时不用确认消息是否收到，1 分区的leader写入成功就会确认
        props.put("acks", "all");
        // 失败重试
        props.put("retries", 0);
        // 消息批量发送，与linger.ms相互配置，如果达到batch.size大小马上发送，如果一直没有达到batch.size但linger.ms超时也会把缓存的消息发送
        props.put("batch.size", 16384);
        // 等待指消息最长延时
        props.put("linger.ms", 500);
        // 缓存使用限制
        props.put("buffer.memory", 33554432);
        // 消息Key的序列化、value序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }


    public static void main(String[] args) {
        init();

        sendAsynInTransaction();
    }

    /**
     * @return
     * @Description 同步发送，每次发送一条
     * @Date 2019-04-22 13:28
     * @Param * @param null
     */
    public static void sendSync() {

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            RecordMetadata meta = null;
            try {

                meta = producer.send(new ProducerRecord<String, String>("title_A", Integer.toString(i), Integer.toString(i))).get();
                System.out.println("key:	" + i + "partion:	" + meta.partition() + "offset:	" + meta.offset());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        producer.flush();
        producer.close();
    }

    /**
     * @return
     * @Description 异步，批量消息发送
     * @Date 2019-04-22 13:27
     * @Param null
     */
    public static void sendAsyn() {

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            System.out.println("key:" + i + "   value:" + i);
            producer.send(new ProducerRecord<String, String>("title_A", Integer.toString(i), Integer.toString(i)));
        }
        producer.flush();
        producer.close();

    }

    /**
     * @return null
     * @Description 发异步送消息，消息成功通过回调方法进行确认消息
     * @Date 2019-04-22 14:03
     * @Param null
     */
    static void sendAsyn$Callback() {
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("title_A", Integer.toString(i), Integer.toString(i)), new Callback() {
                @Override
                public void onCompletion(RecordMetadata meta, Exception exception) {
                    if (exception == null) {
                        System.out.println("partion :	" + meta.partition() + "    offset:	" + meta.offset());
                    } else {
                        System.out.println(exception.getMessage());
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }

    /**
     * @Description 事务transactional.id不能为空，retries要大于0
     * @Date 2019-04-22 14:50
     * @Param  * @param null
     * @return
     */
    static void sendAsynInTransaction() {
        props.put("transactional.id","tc1");
        props.put("retries",2);
        Producer<String, String> producer = new KafkaProducer<>(props);
        try {
            producer.initTransactions();
            producer.beginTransaction();
            for (int i = 0; i < 100; i++) {
                producer.send(new ProducerRecord<String, String>("title_A", Integer.toString(i), Integer.toString(i)), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata meta, Exception exception) {
                        if (exception == null) {
                            System.out.println("partion :	" + meta.partition() + "    offset:	" + meta.offset());
                        } else {
                            System.out.println(exception.getMessage());
                        }
                    }
                });
            }
            producer.commitTransaction();
        }catch (Exception ex){
            producer.abortTransaction();
        }
        producer.close();
    }
}
