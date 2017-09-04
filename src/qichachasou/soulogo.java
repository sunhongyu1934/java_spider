package qichachasou;

import Utils.JsoupUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/9/1.
 */
public class soulogo {
    // 代理隧道验证信息
    final static String ProxyUser = "H689K6HN8Z46666D";
    final static String ProxyPass = "3E0DCB86C35C5504";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int jishu=0;
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
        ExecutorService pool= Executors.newCachedThreadPool();
        soulogo s=new soulogo();
        final Ca c=s.new Ca();
        final Url u=s.new Url();
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

        for(int x=1;x<=5;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(c,u);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int y=1;y<=15;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ruku(u);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select comp_name from tyc.comp_car_name where data_date is not null and data_date!=''";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("comp_name"));
            c.jin(cname);
        }
    }

    public static void get(Ca c,Url u) throws InterruptedException, UnsupportedEncodingException {
        while (true) {
            try {
                String key = c.qu();
                if (StringUtils.isEmpty(key)) {
                    break;
                }
                Document doc = qichaqing("http://www.qichacha.com/search?key=" + URLEncoder.encode(key, "utf-8"));
                Elements ele = JsoupUtils.getElements(doc, "section#searchlist tbody tr");
                if (ele != null) {
                    for (Element e : ele) {
                        String url = JsoupUtils.getHref(e, "td:nth-child(2) a", "href", 0);
                        u.jin("http://www.qichacha.com" + url);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void ruku(Url u) throws InterruptedException, SQLException {
        String sql="insert into linshi_tyc(c_name,c_logo,c_web) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String url = u.qu();
                if (StringUtils.isEmpty(url)) {
                    break;
                }

                Document doc = qichaqing(url);
                String logo = JsoupUtils.getHref(doc, "div.col-md-12.m-b-md.m-t-md.no-padding.m-l span.pull-left.thumb-lg.m-r-lg.company-logo img", "src", 0);
                String cname = JsoupUtils.getString(doc, "div.col-md-12.m-b-md.m-t-md.no-padding.m-l div.text-big.font-bold.company-top-name", 0);
                String web = JsoupUtils.getString(doc, "a.c_mainblue.c_a.company-top-url", 0);

                ps.setString(1, cname);
                ps.setString(2, logo);
                ps.setString(3, web);
                ps.executeUpdate();
                jishu++;
                System.out.println("success_qichacha");
                System.out.println(jishu + "*********************************************************************");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static Document qichaqing(String url){
        Document doc = null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun")&&doc.outerHtml().length()>200) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void jin(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }

    class Url{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void jin(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
