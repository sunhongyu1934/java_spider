package Utils;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer{
    private final KafkaProducer<Integer, String> producer;
    //private final String topic;
    private final Boolean isAsync;

    public Producer(Boolean isAsync) throws FileNotFoundException, DocumentException {
        String kafkacon="10.64.0.112:9092,10.64.0.113:9092,10.64.0.114:9092,10.64.0.115:9092,10.64.0.116:9092,10.64.0.117:9092,10.64.0.118:9092,10.64.0.119:9092,10.64.0.120:9092,10.64.0.121:9092,10.64.0.122:9092,10.64.0.123:9092,10.64.0.124:9092";
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkacon);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "Spider");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("max.request.size","52427800");
        props.put("compression.type","lz4");
        producer = new KafkaProducer<>(props);
        //this.topic = topic;
        this.isAsync = isAsync;
    }

    public void send(String topic,String messageStr){
        long startTime = System.currentTimeMillis();
        if (isAsync) { // Send asynchronously
            producer.send(new ProducerRecord<>(topic,
                    messageStr), new DemoCallBack(startTime, messageStr));
        } else { // Send synchronously
            try {
                producer.send(new ProducerRecord<>(topic,
                        messageStr)).get();
                //System.out.println("Sent message: (" + messageStr + ")");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}

class DemoCallBack implements Callback {

    private final long startTime;
    private final String message;

    public DemoCallBack(long startTime, String message) {
        this.startTime = startTime;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                    "message(" + message + ") sent to partition(" + metadata.partition() +
                            "), " +
                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}
