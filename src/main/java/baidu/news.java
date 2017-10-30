package baidu;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/7/10.
 */
public class news {


    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        final RedisAction redis=new RedisAction("127.0.0.1",6379);
// 代理隧道验证信息
         final String ProxyUser = args[0];
         final String ProxyPass = args[1];

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        news n=new news();
        final Cang c=n.new Cang();

        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c,redis);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=25;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(c,proxy);
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

    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        return con;
    }


    public static void data(Cang c,RedisAction redis) throws SQLException, InterruptedException {
        while (true){
            try {
                String value[] = redis.get("baidu_news").split("#####");
                if(value==null){
                    Thread.sleep(600000);
                }else{
                    c.fang(new String[]{value[0], value[1],value[2]});
                }
            }catch (Exception e){
                Thread.sleep(600000);
            }
        }
    }

    public static void get(Cang c,Proxy proxy) throws IOException, InterruptedException, SQLException {
        String sql="insert into baidu_news_xin(key_name,key_id,flag_source,sou_liang,data_date) values(?,?,?,?,?)";
        PreparedStatement ps=null;
        Connection con=null;
        int p=0;
        while (true) {
            try {
                String[] value = c.qu();
                if(con==null||con.isClosed()){
                    con=getcon();
                }
                ps=con.prepareStatement(sql);
                String flag = value[2];
                String kid = value[0];
                String kname = value[1];
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://news.baidu.com/ns?ct=1&rn=20&ie=utf-8&bs=" + URLEncoder.encode(kname, "utf-8") + "&rsv_bp=1&sr=0&cl=2&f=8&prevct=no&tn=newstitle&word=" + URLEncoder.encode(kname, "utf-8"))
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
                                .timeout(5000)
                                .proxy(proxy)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                            break;
                        }
                    }catch (Exception e){
                        System.out.println("time out");
                    }
                }
                String sou = getString(doc, "div#header_top_bar span.nums", 0).replace("找到相关新闻约", "").replace("找到相关新闻","").replace("篇", "").replace(",", "");
                java.util.Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String datadate=simpleDateFormat.format(date);

                ps.setString(1, kname);
                ps.setString(2, kid);
                ps.setString(3, flag);
                ps.setString(4, sou);
                ps.setString(5,datadate);
                ps.executeUpdate();
                p++;
                System.out.println(p + "******************************************************");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class Cang{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>(100);
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
