package tianyancha.XinxiXin;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mac on 17/7/3.
 */
public class TYCConsumer {
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.252.0.52:3306/tianyancha?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="etl_tmp";
        String password="UsF4z5HE771KQpra";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con!=null){
                    break;
                }
            }
        }
        conn=con;

    }

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
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int x=1;x<=10;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        xiaofei();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void xiaofei() throws UnsupportedEncodingException, SQLException {
        TYCConsumer tyc=new TYCConsumer("tyc_zl","web","10.44.51.90:12181,10.44.152.49:12181,10.51.82.74:12181");
        TYCProducer ty=new TYCProducer("tyc_zl","10.44.51.90:19092,10.44.152.49:19092,10.51.82.74:19092");
        int p=0;
        String sql="insert into linshi_com(c_name) values(?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            String a=tyc.getmessage();
            System.out.println(a);
            ps.setString(1,a);
            ps.executeUpdate();
            p++;
            System.out.println(p+"***************************************");
        }
    }
}
