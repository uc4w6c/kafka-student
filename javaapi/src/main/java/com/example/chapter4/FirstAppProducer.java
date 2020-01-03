package com.example.chapter4;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class FirstAppProducer {
    private static String topicName = "first-app";
    // ここはdocker-compose のkafka側のポートを指定
    private static String localIp = "192.168.11.5:32769";

    public static void main(String[] args) {
        Properties conf = new Properties();
        conf.setProperty("bootstrap.servers", localIp);
        conf.setProperty("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        conf.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<Integer, String> producer = new KafkaProducer<>(conf);

        int key;
        String value;
        for(int i = 1; i <= 100; i++) {
            key = i;
            value = String.valueOf(i);

            ProducerRecord<Integer, String> record = new ProducerRecord<>(topicName, key, value);

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (metadata != null) {
                        // 送信に成功
                        String infoString = String.format("Success partition:%d, offset:%d",
                                                            metadata.partition(),
                                                            metadata.offset());
                        System.out.println(infoString);
                    } else {
                        // 送信に失敗
                        String infoString = String.format("Faild:%s", e.getMessage());
                        System.err.println(infoString);
                    }
                }
            });
        }

        producer.close();
    }
}
