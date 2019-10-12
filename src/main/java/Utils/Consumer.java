package Utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Consumer implements Serializable{
    private final KafkaConsumer<String,String> kafkaConsumer;
    private final String topic;

    public Consumer(String topic,String group) {
        String kafkacon="10.64.0.112:9092,10.64.0.113:9092,10.64.0.114:9092,10.64.0.115:9092,10.64.0.116:9092,10.64.0.117:9092,10.64.0.118:9092,10.64.0.119:9092,10.64.0.120:9092,10.64.0.121:9092,10.64.0.122:9092,10.64.0.123:9092,10.64.0.124:9092";
        Properties props=new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkacon);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"500");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"6000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"209715200");
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
            ConsumerRecords<String, String> records = kafkaConsumer.poll(5000);
            kafkaConsumer.commitSync();
            if (records == null || records.isEmpty()) {
                return null;
            }
            for (ConsumerRecord<String, String> consumerRecord : records) {
                value = consumerRecord.value();
                list.add(value);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        kafkaConsumer.close();
    }
}
