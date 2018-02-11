package tianyancha.Guoxin;

import Utils.JsoupUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.text.html.parser.Element;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Serach {
    private static Connection conn;
    // 代理隧道验证信息
    final static String ProxyUser = "H88A4Q10G0V31YCD";
    final static String ProxyPass = "C2E28C06C89836C4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Map<String,String> map=new HashMap<String, String>();

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

    }
    public static void main(String args[]) throws IOException, SQLException, InterruptedException {
        String sql="insert into tyc_seurl(se_url) values(?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Document doc=Jsoup.connect("https://www.tianyancha.com/search?key=%E5%85%AC%E5%85%B1%E5%B0%B1%E4%B8%9A%E6%9C%8D%E5%8A%A1&checkFrom=searchBox")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .header("Cookie","TYCID=e25abfa0f9e311e7af0fd7c3005ea0e5; undefined=e25abfa0f9e311e7af0fd7c3005ea0e5; ssuid=1465301810; tyc-user-info=%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzcxNzk1MTkzNCIsImlhdCI6MTUxNzc5NjE5MCwiZXhwIjoxNTMzMzQ4MTkwfQ.mgoWASBscyQtPi9GkQstV8_rbISjx3ugfdTFeQjSW9MMLXBD7GcgiraBMXEexUlQc1esIGQVg2fDPN5PhmuGBw%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vipManager%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25222%2522%252C%2522mobile%2522%253A%252213717951934%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzcxNzk1MTkzNCIsImlhdCI6MTUxNzc5NjE5MCwiZXhwIjoxNTMzMzQ4MTkwfQ.mgoWASBscyQtPi9GkQstV8_rbISjx3ugfdTFeQjSW9MMLXBD7GcgiraBMXEexUlQc1esIGQVg2fDPN5PhmuGBw; aliyungf_tc=AQAAAJ2+uFQXswMAyhD53AZwMY02ArGI; csrfToken=DQf5-A3ZGgD6FOCrwrcqAhFY; bannerFlag=true; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1517796173,1517807408,1517972441,1517989753; _csrf=ac0Ugb3TqNwizRzca0lnaA==; OA=Yvnw1322juwTykiMUMxYfyifNheD81wZl20pGIatFQMY3d4UF97/a0qyFw1BvrHWlo6ITclkV/xh1c5O82QOvg==; _csrf_bk=412643650362e458f6ca3b3c87143d25; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1517990095")
                .get();
        /*Elements ele=JsoupUtils.getElements(doc,"a.new-list");
        System.out.println(ele.size());
        int a=0;
        for(org.jsoup.nodes.Element e:ele){
            ps.setString(1, e.attr("href"));
            ps.executeUpdate();
            a++;
            System.out.println(a + "*******************************************");
        }*/

        Elements ele=JsoupUtils.getElements(doc,"div.position-abs.b-c-white.new-border.cate_sub_item a");

        String[] zhu=new String[]{"","-r0100","-r100200","-r200500","-r5001000","-r1000"};
        String[] shi=new String[]{"","-e01","-e015","-e510","-e1015","-e15"};
        String[] zhuang=new String[]{"","-s1","-s2","-s3","-s4","-s5"};
        String[] pai=new String[]{"","-la1","-la2","-la3","-la4"};
        int a=0;
        for(org.jsoup.nodes.Element e:ele){
            String url=e.attr("href");
            for(String z:zhu){
                for(String s:shi){
                    for(String zh:zhuang){
                        for(String p:pai) {
                            //for(int page=1;page<=5;page++) {
                                //ps.setString(1, url.replace("?key=%E4%BA%BA%E6%89%8D","") + z + s + zh + p+"/p"+page+"?key=%E4%BA%BA%E6%89%8D");
                            ps.setString(1, url.replace("?key=%E5%85%AC%E5%85%B1%E5%B0%B1%E4%B8%9A%E6%9C%8D%E5%8A%A1","") + z + s + zh + p+"?key=%E5%85%AC%E5%85%B1%E5%B0%B1%E4%B8%9A%E6%9C%8D%E5%8A%A1");
                                ps.executeUpdate();
                                a++;
                                System.out.println(a + "*******************************************");
                           // }
                        }
                    }
                }
            }
        }
    }
    public static Document detailget(String url,Ca c) throws IOException, InterruptedException {
        System.out.println(url);
        Document doc=null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .ignoreHttpErrors(true)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreContentType(true)
                        .get();
                if (doc!=null&&!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("Unauthorized") && !doc.outerHtml().contains("访问禁止")) {
                    System.out.println(doc);
                }
                if(false){
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        System.out.println(doc);
        return doc;
    }

    public static void getip(Ca c) throws IOException, InterruptedException {
        while (true) {
            try {
                Document doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=00b1c1dbec239455d92d87b98145951c&sep=3")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String ip = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();

                if (ip.contains("requests") || ip.contains("请控制")) {
                    continue;
                }
                for(int x=1;x<=20;x++) {
                    c.fang(ip);
                }
                System.out.println(ip);
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println("ip error");
            }
        }
    }

    public static void check(){
        while (true){
            if(map!=null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if ((System.currentTimeMillis() - Long.parseLong(entry.getValue())) >= 30000) {
                        map.remove(entry.getKey());
                    }
                }
            }
        }
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
