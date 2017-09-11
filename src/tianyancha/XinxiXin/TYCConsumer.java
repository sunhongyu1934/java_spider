package tianyancha.XinxiXin;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by mac on 17/7/3.
 */
public class TYCConsumer {
    private final ConsumerConnector consumer;
    private final String topic;
    private ConsumerIterator<byte[], byte[]> it=null;
    public TYCConsumer(String topic, String group, String ZK)
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", ZK);
        props.put("group.id", group);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("replica.fetch.max.bytes","52428881");

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                new ConsumerConfig(props));
        this.topic = topic;


        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);
        this.it = stream.iterator();
    }


    public String getmessage() throws UnsupportedEncodingException {
        String message=null;
        while(it.hasNext()){
            message=new String(it.next().message(),"UTF-8");
            consumer.commitOffsets();
            break;
        }
        return message;
    }
    public void close(){
        consumer.shutdown();
    }
    public static void main( String[] args ) throws InterruptedException, UnsupportedEncodingException {
        TYCConsumer tyc=new TYCConsumer("tyc_linshi3","web","10.44.51.90:12181,10.44.152.49:12181,10.51.82.74:12181");
        TYCProducer ty=new TYCProducer("tyc_zl","10.44.51.90:19092,10.44.152.49:19092,10.51.82.74:19092");
        int p=0;
        while (true){
            String a=tyc.getmessage();
            System.out.println(a);
            ty.send(a);
            if(p>=100){
                break;
            }
            p++;
            System.out.println(p+"***************************************");
        }

    }
}
