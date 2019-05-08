package com.yss.henghe.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author ttylang
 * @Description TODO
 * @Date 2019-04-22
 */
public class ConsumerDemo {
    static Properties props = new Properties();

    static void init() {
        //broker server 地址列表
        props.put("bootstrap.servers", "henghe-101-21:9092,henghe-101-22:9092");
        //group.id 同一条消息，只被同一个group.id消费一次
        props.put("group.id", "g1");

        //key value反序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    }

    public static void main(String[] args) {

        init();
        cosumer();

    }

    /**
     * @return
     * @Description 消息订阅消费，定时自动提交
     * @Date 2019-04-22 15:21
     * @Param
     */
    public static void cosumerAutoCommit() {
        //自动提交
        props.put("enable.auto.commit", "true");
        //自动提交时间时隔
        props.put("auto.commit.interval.ms", "1000");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        consumer.subscribe(Arrays.asList("title_A"));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));
        for (ConsumerRecord<String, String> record : records) {
            buffer.add(record);
            System.out.println("offset" + record.offset());
        }

    }

    /**
     * @return
     * @Description 消息订阅消费,手动提交，同步提交或者异步提交
     * @Date 2019-04-22 15:21
     * @Param
     */
    public static void cosumerAndUnCommit() {
        //自动提交
        props.put("enable.auto.commit", "false");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        consumer.subscribe(Arrays.asList("title_A"));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));
        for (ConsumerRecord<String, String> record : records) {
            buffer.add(record);
            System.out.println("offset" + record.offset());
        }
        //异步提交
        //consumer.commitAsync();

        //同步提交
        consumer.commitSync();
    }

    /**
     * @return
     * @Description 指定offset进行消息，手动消费或消息重新消息
     * @Date 2019-04-22 15:21
     * @Param
     */
    public static void cosumerSeekOffset() {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        int count = 5;

        TopicPartition topicPartition1 = new TopicPartition("title_A", 0);
        consumer.assign(Arrays.asList(topicPartition1));
        consumer.seek(topicPartition1, 500);

        while (count > 0) {
            count--;
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
                System.out.println("offset" + record.offset());
            }

        }
    }

}
