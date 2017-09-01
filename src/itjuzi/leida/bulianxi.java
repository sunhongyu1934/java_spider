package itjuzi.leida;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/7/11.
 */
public class bulianxi {
    // 代理隧道验证信息
    final static String ProxyUser = "HNW6M4X7VI3L8R4D";
    final static String ProxyPass = "855F207CD3A4AC56";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
                if(con!=null){
                    break;
                }
            }
        }

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        bulianxi b=new bulianxi();
        final Keys k=b.new Keys();
        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    du(finalCon,k);
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
                        data(finalCon,proxy,k);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void data(java.sql.Connection con,Proxy proxy,Keys k) throws IOException {
        Random r=new Random();
        Map<String,String> map=login();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 10800000;
        int j=0;
        while (true){
            try{
                String sid=k.qu();
                get(map,sid,proxy,con);
                System.out.println("buchong ok");
                j++;
                System.out.println(j+"***********************************************************");
                long t = System.currentTimeMillis();
                if(t>(begin+cur)){
                    System.out.println("return login");
                    cur=(r.nextInt(50) * 60 * 1000) + 10800000;
                    map=login();
                    t=System.currentTimeMillis();
                    begin = t;
                }
            }catch (Exception e){
                System.out.println("error");
                e.printStackTrace();
            }
        }
    }

    public static void du(Connection con,Keys k) throws SQLException, InterruptedException {
        String sql="select s_id from it_leida_company where phone is null";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String sid=rs.getString(rs.findColumn("s_id"));
            k.put(sid);
        }
    }

    public static void get(Map<String,String> map,String sid,Proxy proxy,Connection con) throws SQLException {
        String sql="update it_leida_company set phone=?,email=?,de_address=? where s_id="+sid;
        PreparedStatement ps=con.prepareStatement(sql);
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://radar.itjuzi.com/company/" + sid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .cookies(map)
                        .proxy(proxy)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .get();
                if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out gongsi");
            }
        }
        String phone=getString(doc,"div.side-contact p.contact-text:contains(电话) span.text-main",0);
        String email=getString(doc,"div.side-contact p.contact-text:contains(邮箱) span.text-main",0);
        String address=getString(doc,"div.side-contact p.contact-text:contains(地址) span.text-main",0);

        ps.setString(1,phone);
        ps.setString(2,email);
        ps.setString(3,address);
        ps.executeUpdate();
    }

    public static Map<String,String> login() throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("HNW6M4X7VI3L8R4D", "855F207CD3A4AC56"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity","13717951934"));
        params.add(new BasicNameValuePair("password","3961shy"));
        params.add(new BasicNameValuePair("remember","1"));
        params.add(new BasicNameValuePair("page",""));
        params.add(new BasicNameValuePair("url",""));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        List<Cookie> cookies =null;
        while (true) {
            CloseableHttpResponse response = httpclient.execute(post);
            HttpEntity resEntity = response.getEntity();
            String tag = EntityUtils.toString(resEntity);
            cookies=cookieStore.getCookies();
            if (cookies != null && cookies.size()>2) {
                break;
            }
        }
        for (int i = 0; i < cookies.size(); i++) {
            map.put(cookies.get(i).getName(),cookies.get(i).getValue());
        }

        /*Connection.Response res= null;
        while (true) {
            try {
                res = Jsoup.connect("https://www.itjuzi.com/user/login?redirect=&flag=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .header("Content-Type", "text/html;charset=utf-8")
                        .data("identity", "13717951934")
                        .data("password", "3961shy")
                        .data("remember", "1")
                        .data("page", "")
                        .data("url", "")
                        .timeout(100000)
                        .method(Connection.Method.POST)
                        .execute();
                if (res != null && StringUtils.isNotEmpty(res.cookies().toString().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out");
            }
        }
        Map<String,String> map=res.cookies();*/
        System.out.println(map);
        return map;
    }


    class Keys{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }

        public String qu() throws InterruptedException {
            return bo.take();
        }
    }

}
