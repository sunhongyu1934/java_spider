package itjuzi.linshi;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getString;
import static itjuzi.zl.itjz_l.getHref;

public class Tag {
    private static HttpClientBuilder builder;
    private static Connection conn;
    static{
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        builder = HttpClients.custom();
        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://47.95.31.183:3306/innotree_data_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

    public static void main(String args[]) throws InterruptedException, SQLException {
        Tag t=new Tag();
        final Ca c=t.new Ca();
        /*ExecutorService pool= Executors.newCachedThreadPool();
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

        for(int a=1;a<=25;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
        c.fang("4");
        get(c);
    }

    public static void get(Ca c) throws InterruptedException, SQLException {
        CloseableHttpClient httpClient = builder.build();
        int a=0;
        while (true) {
            String cid=c.qu();
            HttpGet get = new HttpGet("https://www.itjuzi.com/company/" + cid);
            get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            get.addHeader("Cache-Control", "max-age=0");
            get.addHeader("Host", "www.itjuzi.com");
            get.addHeader("Upgrade-Insecure-Requests", "1");
            get.addHeader("If-Modified-Since", "Tue, 25 Jul 2017 06:33:17 GMT");
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            String tag = null;

            int ji=0;
            while (true) {
                try {
                    CloseableHttpResponse response = httpClient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    tag = EntityUtils.toString(resEntity);
                    ji++;
                    if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")&&!tag.contains("找不到您访问的页面")&&!tag.contains("No required SSL certificate")) {
                        break;
                    }
                    if(ji>=20){
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("time out detail reget");
                }
            }
            ruku(tag,cid);
            a++;
            System.out.println(c.po.size()+"*******************************************");
        }
    }

    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select c_id from it_company_pc_tmp where sName!='' and sName is not null and (company_full_name='' or company_full_name is null)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cid=rs.getString(rs.findColumn("c_id"));
            c.fang(cid);
        }
    }

    public static void ruku(String html,String cid) throws SQLException {
        String sql="update it_company_pc_tmp set company_tags=?,company_slogan=?,company_introduction=?,company_full_name=? where c_id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        Document doc= Jsoup.parse(html);
        String tags=null;
        Elements tagele=getElements(doc,"div.tagset.dbi.c-gray-aset a");
        if(tagele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tag=getString(e, "span.tag", 0);
                str.append(getString(e,"span.tag",0)+";");
            }
            tags=str.toString();
        }
        String yewu=getString(doc,"div.block",2);
        String kouhao=getString(doc,"div.info-line h2.seo-slogan",0);
        String fullname=getString(doc,"div.block div.des-more h2.seo-second-title",0).replace("公司全称：","");
        String urlguan=getHref(doc, "div.link-line a.weblink", "href", 1);
        System.out.println(doc.outerHtml());
        System.out.println(urlguan);
        System.exit(0);

        ps.setString(1,tags);
        ps.setString(2,kouhao);
        ps.setString(3,yewu);
        ps.setString(4,fullname);
        ps.setString(5,cid);
        ps.executeUpdate();

    }

    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
