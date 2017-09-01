package tianyancha.XinxiXin;

import java.io.UnsupportedEncodingException;

/**
 * Created by mac on 17/7/4.
 */
public class abuyun {
    private String user=null;
    private String pass=null;
    private String topic=null;
    private String group=null;
    private String ZK=null;
    private String brokers=null;
    public  abuyun(String topic,String group,String ZK,String brokers) throws UnsupportedEncodingException {
        this.topic=topic;
        this.group=group;
        this.ZK=ZK;
        this.brokers=brokers;
        doShutDownWork();
        get_abuyun();
        System.out.println("get abuyun user:"+user+" pass"+pass);
    }
    private void get_abuyun() throws UnsupportedEncodingException {
        TYCConsumer tycc=new TYCConsumer(topic,group,ZK);
        String user_pass=tycc.getmessage();
        tycc.close();
        user=user_pass.split("\t")[0];
        pass=user_pass.split("\t")[1];
    }
    private void doShutDownWork() {
        Runtime run=Runtime.getRuntime();
        run.addShutdownHook(new Thread(){ //注册新的虚拟机来关闭钩子
            @Override
            public void run() {
                //程序结束时进行的操作
                if (user!=null&&pass!=null)
                {
                    TYCProducer tycp=new TYCProducer("abuyun_test",brokers);
                    tycp.send(user+"\t"+pass);
                    tycp.close();
                }
            }
        });
    }
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        new abuyun("abuyun_test","test1","10.44.155.195:2181,10.44.143.200:2181,10.45.146.248:2181","10.44.158.42:9092,10.44.137.192:9092,10.44.143.200:9092,10.44.155.195:9092");
        Thread.sleep(10000);
    }
}
