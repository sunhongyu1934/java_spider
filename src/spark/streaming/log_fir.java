/*
package spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

*/
/**
 * Created by Administrator on 2017/8/17.
 *//*

public class log_fir {
    //private static Connection conn;
    private static JdbcUtils_DBCP db=new JdbcUtils_DBCP();
    static{
        */
/*String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
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
        conn=con;*//*

    }
    public static void main(String args[]) throws SQLException, InterruptedException {
        exec();
    }

    public static void exec() throws SQLException, InterruptedException {
        Map<String,Integer> kafkamap=new HashMap<String, Integer>();
        kafkamap.put("spider_log",1);
        SparkConf sparkConf = new SparkConf().setAppName("JavaNetworkWordCount").setMaster("spark://h101:7077");
        JavaStreamingContext ssc=new JavaStreamingContext(sparkConf, Durations.seconds(60));
        JavaPairReceiverInputDStream<String, String> lines = KafkaUtils.createStream(ssc, "10.44.51.90:12181,10.44.152.49:12181,10.51.82.74:12181", "log", kafkamap);
        final String sql="insert into spider_log_new(sp_name,ty_pe,c_ount,count_time,data_date) values(?,?,?,?,?)";


        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<Tuple2<String,String>, String>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Iterator<String> call(Tuple2<String,String> tuple) throws Exception {
                System.out.println(tuple._2.toString());
                return Arrays.asList(tuple._2.split("\n")).iterator(); //按行进行分隔
            }
        });

        JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        JavaPairDStream<String, Integer> wordcounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        wordcounts.print();
        wordcounts.foreachRDD(new VoidFunction<JavaPairRDD<String,Integer>>() {
            private static final long serialVersionUID = 1L;
            @Override
            public void call(JavaPairRDD<String, Integer> wordcountsRDD) throws Exception {
                //foreachPartition这个方法好像和kafka的topic的分区个数有关系，如果你topic有两个分区，则这个方法会执行两次
                wordcountsRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void call(Iterator<Tuple2<String, Integer>> wordcounts) throws Exception {
                        Connection con=db.getConnection();
                        PreparedStatement ps= con.prepareStatement(sql);
                        Tuple2<String,Integer> wordcount = null;
//注意：这里是利用了在hbase中对同一rowkey同一列再查入数据会覆盖前一次值的特征，所以hbase中linecount表的版本号必须是1，建表的时候如果你不修改版本号的话默认是1
                        while(wordcounts.hasNext()){
                            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat time2=new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date date =new java.util.Date();
                            String ti=time.format(date);
                            String data=time2.format(date);
                            wordcount = wordcounts.next();
                            if(wordcount._1.contains("success_")||wordcount._1.contains("error_")) {
                                ps.setString(1, wordcount._1.split("_", 2)[1]);
                                ps.setString(2, wordcount._1.split("_", 2)[0]);
                                ps.setString(3, wordcount._2.toString());
                                ps.setString(4, ti);
                                ps.setString(5, data);
                                ps.executeUpdate();
                            }
                            System.out.println(wordcount._1 + "   " + wordcount._2);
                        }
                        con.close();
                    }
                });
            }
        });
        ssc.start();
        ssc.awaitTermination();
    }

}
*/
