package App;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

import static App.xiaomi.detail;
import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/8/16.
 */
public class xiaosousuo {
    // 代理隧道验证信息
    final static String ProxyUser = "HI4Z5PI5D1Y44S2D";
    final static String ProxyPass = "5D698C9C15113ACE";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }
    public static void main(String args[]){
        xiaosousuo s=new xiaosousuo();
        xiaomi w=new xiaomi();
        final xiaomi.Url u=w.new Url();
        final Key k=s.new Key();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int a=1;a<=10;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sou(k,u);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int b=1;b<=10;b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(u);
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


    public static void data(Key k) throws SQLException, InterruptedException {
        String sql="select c_name from linshi";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String mi=rs.getString(rs.findColumn("c_name"));
            k.fang(mi);
        }
    }

    public static void sou(Key k,xiaomi.Url u) throws InterruptedException, IOException {
        Document doc;
        while (true){
            try {
                String key = k.qu();
                if (StringUtils.isEmpty(key)) {
                    break;
                }
                doc = get("http://app.mi.com/search?keywords=" + URLEncoder.encode(key, "utf-8"));
                Elements ele = getElements(doc, "ul.applist li");
                int p = 0;
                boolean ff = false;
                String uu = null;
                if (ele != null) {
                    for (Element e : ele) {
                        String url = getHref(e, "a", "href", 0).replace("/details?id=","");
                        String cname = getString(e, "a", 0);
                        p++;
                        if (p == 1) {
                            uu = url;
                        }
                        if (key.equals(cname)) {
                            u.fang(url);
                            ff = true;
                        }
                    }
                }
                if (StringUtils.isNotEmpty(uu) && !ff) {
                    u.fang(uu);
                }
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }


    class Key{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }

        public String qu() throws InterruptedException {
            return po.poll(60, TimeUnit.SECONDS);
        }
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
    }
}
