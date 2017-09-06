package java_image;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/9/6.
 */
public class login {
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://100.115.97.86:3306/dc_cscyl?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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
    public static void main(String args[]) throws IOException {
        login l=new login();
        final Uu u=l.new Uu();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(u);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        xiazai(u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Uu u) throws SQLException, InterruptedException {
        String sql="select ncid,logourl from comp_baseinfo_innotree where logourl!='' and logourl is not null";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            try {
                String id = rs.getString(rs.findColumn("ncid"));
                String logo = rs.getString(rs.findColumn("logourl"));
                u.fang(new String[]{id, logo});
            }catch (Exception e){
                System.out.println("fang error");
            }
        }
    }

    public static void xiazai(Uu u) throws IOException, InterruptedException {
        HttpClientBuilder builder= HttpClients.custom();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H689K6HN8Z46666D", "3E0DCB86C35C5504"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        //builder.setRoutePlanner(routePlanner);

        //builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CloseableHttpClient httpclient = builder.build();

        while (true) {
            try {
                String[] value = u.qu();
                if (value == null && value.length < 2) {
                    break;
                }
                HttpGet get = new HttpGet(value[1]);
                while (true) {
                    try {
                        CloseableHttpResponse response = httpclient.execute(get);
                        int code = response.getStatusLine().getStatusCode();
                        if (code == 200) {
                            HttpEntity entity = response.getEntity();
                            InputStream in = entity.getContent();
                            FileOutputStream fos = new FileOutputStream("/home/etl_user/etl/logo/" + value[0] + ".png");
                            byte[] buf = new byte[1024];
                            int len = 0;
                            while ((len = in.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            fos.close();
                            in.close();
                            System.out.println(u.po.size() + "***************************************************************");
                            break;
                        }else{
                            HttpEntity entity = response.getEntity();
                            String str= EntityUtils.toString(entity);
                            if (StringUtils.isNotEmpty(str) && !str.contains("abuyun")&&str.contains("Error")) {
                                break;
                            }
                        }
                    }catch (Exception e1){
                        System.out.println("detail error");
                    }
                }
                System.out.println(u.po.size()+"----------------------------------------------------------------");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    class Uu{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
