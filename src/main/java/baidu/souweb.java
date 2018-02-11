package baidu;

import Utils.Dup;
import Utils.JsoupUtils;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

public class souweb {
    // 代理隧道验证信息
    final static String ProxyUser = "HJGR1T7575J6744D";
    final static String ProxyPass = "109FD50EC1CC22A7";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static int j=0;
    private static Connection conn;
    private static HttpClientBuilder builder;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("http-dyn.abuyun.com",9020),new UsernamePasswordCredentials("HJGR1T7575J6744D", "109FD50EC1CC22A7"));
        HttpHost proxy2 = new HttpHost("http-dyn.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).setRedirectsEnabled(false).build();
        builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);

    }


    public static void main(String args[]) throws IOException {
        souweb s=new souweb();
        Cc c=s.new Cc();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=30;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sousuo2(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Cc c) throws SQLException, InterruptedException {
        String sql="select comp_id,comp_full_name from aaa";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("comp_id"));
            String cname=rs.getString(rs.findColumn("comp_full_name"));
            c.fang(new String[]{id,cname});
        }
    }

    public static void sousuo(Cc c) throws IOException, InterruptedException, SQLException {
        String sql="insert into comp_gaoxin_web_v(comp_id,comp_web,comp_full_name) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true) {
            try {
                String[] value = c.qu();
                if (value == null || value.length < 2) {
                    break;
                }
                Document doc = get("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=" + URLEncoder.encode(value[1], "utf-8") + "%40v&oq=%25E5%258C%2597%25E4%25BA%25AC%25E5%259B%25A0%25E6%259E%259C%25E6%25A0%2591%25E7%25BD%2591%25E7%25BB%259C%25E7%25A7%2591%25E6%258A%2580%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8%2540v&rsv_pq=c87f206d0000692f&rsv_t=eee3hzZHDEWAIPBaavGrvVBsJBlGX3%2BMED3yoKq6oXV4i%2B%2Br%2BocGhnDUoqc&rqlang=cn&rsv_enter=0&rsv_n=2&rsv_sug3=4&rsv_sug2=0&inputT=890&rsv_sug4=890&rsv_sug=1");
                String web = JsoupUtils.getHref(doc, "div.c-row.section.main-section.last div.inner-section table.content-table tbody tr:contains(网站地址) td a", "href", 0);
                ps.setString(1, value[0]);
                ps.setString(2, web);
                ps.setString(3, value[1]);
                ps.executeUpdate();
                j++;
                System.out.println(j + "******************************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static void sousuo2(Cc c) throws IOException, InterruptedException, SQLException {
        String sql="insert into comp_gaoxin_web(comp_id,comp_web,comp_full_name) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true) {
            try {
                String[] value = c.qu();
                if (value == null || value.length < 2) {
                    break;
                }
                Document doc = get("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd="+URLEncoder.encode(value[1],"utf-8")+"&rsv_pq=fd34ad4c000014c0&rsv_t=8948JNqSvJ5tA9GdQ%2BC0rYT3elEpdRycgN7kZRGWIywUpsN7Gzbh0jsXIkA&rqlang=cn&rsv_enter=1&rsv_sug3=1&rsv_sug1=1&rsv_sug7=100&rsv_sug2=0&inputT=636&rsv_sug4=636");
                org.jsoup.select.Elements ele = JsoupUtils.getElements(doc, "div.result.c-container");
                int mmp=0;
                for (Element e : ele) {
                    String url = JsoupUtils.getHref(e, "h3 a", "href", 0);
                    String web = geturl(url);
                    if (Dup.nullor(web)) {
                        ps.setString(1, value[0]);
                        ps.setString(2, web);
                        ps.setString(3, value[1]);
                        ps.executeUpdate();
                    }
                    mmp++;
                    if(mmp>=5){
                        break;
                    }
                }
                j++;
                System.out.println(j + "******************************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }


    public static Document get(String url) throws IOException {
        HttpGet get=new HttpGet(url);
        get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        CloseableHttpClient httpclient = builder.build();
        String tag=null;
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        Document doc=Jsoup.parse(tag);
        return doc;
    }

    public static String geturl(String url) throws IOException {
        HttpGet get=new HttpGet(url.replace("http","https"));
        get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        CloseableHttpClient httpclient = builder.build();
        String tag=null;
        String urls=null;
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                    urls=response.getFirstHeader("Location").getValue();
                    System.out.println(urls);
                    System.out.println("---------------------------------------------");
                    break;
                }else if(response.getStatusLine().getStatusCode()>400&&response.getStatusLine().getStatusCode()!=429){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return urls;
    }

    public static String geturl2(String url) throws IOException {
        org.jsoup.Connection.Response doc =null;
        String urls=null;
        int m=0;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .proxy(proxy)
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                if (doc != null && doc.body().length() > 44&&!doc.body().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        urls=doc.url().toString();
        return urls;
    }


    class Cc{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(100, TimeUnit.SECONDS);
        }
    }

}
