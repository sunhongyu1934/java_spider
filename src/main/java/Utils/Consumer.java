package Utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Consumer {
    private final KafkaConsumer<String,String> kafkaConsumer;
    private final String topic;

    public Consumer(String topic, String group) throws FileNotFoundException, DocumentException {
        String kafkacon="10.64.14.69:9092,10.64.14.70:9092,10.64.14.71:9092";
        Properties props=new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkacon);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer=new KafkaConsumer<String, String>(props);
        this.topic=topic;
        kafkaConsumer.subscribe(Collections.singletonList(this.topic));
    }

    public List<String> Read() throws InterruptedException {
        try {
            String value;
            List<String> list=new ArrayList<>();
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
            if (records == null || records.isEmpty()) {
                return null;
            }
            for (ConsumerRecord<String, String> consumerRecord : records) {
                value = consumerRecord.value();
                list.add(value);
            }
            kafkaConsumer.commitSync();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
