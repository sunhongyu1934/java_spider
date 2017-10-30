package qiniu;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.concurrent.*;

public class linshi_url {
    public static final String ACCESS_KEY = "Wbw_tA3cCzYqakUdU5Xqf-NBbnk80nWMbH4yGr0B"; // 你的access_key
    public static final String SECRET_KEY = "n9ahK41uBFqdKoM-n8EcnwCPJVBOFvKhlnml9qu9"; // 你的secret_key
    public static final String BUCKET_NAME = "innotreelogo"; // 你的secret_key
    private static final String BUCKET_HOST_NAME = "innotreelogo.qiniu.innotree.cn";
    public static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    public static final BucketManager bucketManager = new BucketManager(auth);

    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://47.95.31.183:3306/innotree_data_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="test";
        String password="123456";
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
        System.out.println(shangchuan("http://qxb-img.oss-cn-hangzhou.aliyuncs.com/logo/e3796230-029b-4c5d-a9e1-4194b71d26c3.jpg",""));
        ExecutorService pool= Executors.newCachedThreadPool();
        linshi_url l=new linshi_url();
        final Ca c=l.new Ca();
        final String k="";
        final String o="";
        String x="20";
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c,k,o);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=Integer.parseInt(x);a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        chuli2(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Ca c,String k,String o) throws SQLException, InterruptedException {
        String sql="select id,comp_logo_tmp from comp_linshi where comp_logo_tmp!='' and comp_logo_tmp is not null and comp_logo_tmp not like '%qiniu%'";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String onid=rs.getString(rs.findColumn("id"));
            String logo=rs.getString(rs.findColumn("comp_logo_tmp"));
            c.fang(new String[]{onid,logo});
        }
    }

    public static void chuli(Ca c) throws Exception {
        String sql="insert into logo_qiniu(only_id,qiniu_url) values(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        int p=0;
        while (true){
            String[] value=c.qu();
            if(value==null||value.length<2){
                break;
            }
            String url=shangchuan(value[1],"");
            ps.setString(1,value[0]);
            ps.setString(2,url);
            ps.executeUpdate();
            p++;
            System.out.println(p+"********************************************");
        }
    }

    public static void chuli2(Ca c) throws Exception {
        String sql="update comp_linshi set comp_logo_tmp=? where id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        int p=0;
        while (true){
            String[] value=c.qu();
            if(value==null||value.length<2){
                break;
            }

            String url=shangchuan(value[1],"");
            ps.setString(1,url);
            ps.setString(2,value[0]);
            ps.executeUpdate();
            p++;
            System.out.println(p+"********************************************");
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

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(60, TimeUnit.SECONDS);
        }
    }
}
