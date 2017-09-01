package tianyancha.XinxiXin;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class TYCProducer
{
    private  Producer <String, String> producer=null;
    private  String topic=null;
    private  String brokers=null;
    private  Properties props = new Properties();

    public TYCProducer(String topic, String brokers)
    {
        // 此处配置的是kafka的broker地址:端口列表
        props.put("metadata.broker.list", brokers);

        //配置value的序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //配置key的序列化类
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        //ack=-1
        props.put("request.required.acks","-1");
        producer = new Producer<String, String>(new ProducerConfig(props));
        this.topic = topic;
        this.brokers=brokers;
    }

    public void send(String data) {
        try {
            long key = System.currentTimeMillis();
            producer.send(new KeyedMessage<String, String>(topic, Long.toString(key), data));
        }catch (Exception e){
        }
    }
    public void close(){
        try{
            producer.close();
        } catch (Exception e){
        }
    }

    public static void main( String[] args )
    {
        TYCProducer ty=new TYCProducer("tianyancha_search","10.44.158.42:9092,10.44.137.192:9092,10.44.143.200:9092,10.44.155.195:9092");
        for (int i=0;i<10;i++)
            ty.send("test"+i);
        ty.close();
    }

}

