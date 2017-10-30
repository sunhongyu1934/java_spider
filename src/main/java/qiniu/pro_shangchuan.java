package qiniu;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class pro_shangchuan {
    public static final String ACCESS_KEY = "Wbw_tA3cCzYqakUdU5Xqf-NBbnk80nWMbH4yGr0B"; // 你的access_key
    public static final String SECRET_KEY = "n9ahK41uBFqdKoM-n8EcnwCPJVBOFvKhlnml9qu9"; // 你的secret_key
    public static final String BUCKET_NAME = "innotreelogo"; // 你的secret_key
    private static final String BUCKET_HOST_NAME = "innotreelogo.qiniu.innotree.cn";
    public static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    public static final BucketManager bucketManager = new BucketManager(auth);

    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/dimension_sum?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        conn=con;

    }
    public static void main(String args[]) throws Exception {
        pro_shangchuan p=new pro_shangchuan();
        final Guan g=p.new Guan();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(g);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        for(int a=1;a<=50;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        chuli(g);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public static void data(Guan g) throws SQLException, InterruptedException {
        Map<String,String> map=new HashMap<String, String>();
        map.put("comp_logo_sum","logo_url");

        for(Map.Entry<String,String> entry:map.entrySet()){
            for(int x=0;x<=9;x++){
                String tablename=entry.getKey()+x;
                String field=entry.getValue();
                int a=0;
                while (true) {
                    boolean bo=false;
                    String sql = "select source_y,only_id," + field + " from " + tablename + " where " + field + "!='' and " + field + " is not null limit "+a+",500000";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        bo=true;
                        try {
                            String logo = rs.getString(rs.findColumn(field));
                            String id = rs.getString(rs.findColumn("only_id"));
                            String source = rs.getString(rs.findColumn("source_y"));

                            g.fang(new String[]{id, logo, tablename.split("_", 3)[1], source});
                        } catch (Exception e) {
                            System.out.println("fang error");
                        }
                    }
                    if(!bo){
                        break;
                    }
                    a=a+500000;
                }
            }
        }
    }

    public static void chuli(Guan g) throws Exception {
        String sql="insert into company_logo_qiniu(only_id,qi_logo,source_y,source_b) values(?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        int a=0;
        while (true){
            try {
                String[] value = g.qu();
                if (value == null || value.length < 2) {
                    break;
                }
                String newurl = shangchuan(value[1], "");
                String tailid = value[0];

                if(newurl!=null&&newurl.length()>0) {
                    ps.setString(1, tailid);
                    ps.setString(2, newurl);
                    ps.setString(3,value[2]);
                    ps.setString(4,value[3]);
                    ps.executeUpdate();
                    a++;
                    System.out.println(a + "************************************************");
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }









    public static String shangchuan(String url,String uui) throws Exception {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//        BucketManager bucketManager = new BucketManager(auth);
        String newUrl =null;
        // 要求url可公网正常访问BucketManager.fetch(url, bucketName, key);
        // @param url 网络上一个资源文件的URL
        // @param bucketName 空间名称
        // @param key 空间内文件的key[唯一的]

        try {
            DefaultPutRet putret = bucketManager.fetch(url, BUCKET_NAME);
            newUrl = "https://" + BUCKET_HOST_NAME + "/" + putret.key;
        }catch (Exception e){
//            if(jietu(url,uui)) {
//                UploadManager uploadManager = new UploadManager();
//                Response res = uploadManager.put(uui + ".jpg", null, getUpToken());
//                String json = res.bodyString();
//                Gson gson = new Gson();
//                qijson q = gson.fromJson(json, qijson.class);
//                newUrl = "https://" + BUCKET_HOST_NAME + "/" + q.key;
//            }else{
//                newUrl=null;
//            }
        }

        return newUrl;
    }

    class Guan{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>(10000);
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(100, TimeUnit.SECONDS);
        }
    }
}
