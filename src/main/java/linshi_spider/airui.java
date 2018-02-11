package linshi_spider;

import Utils.Dup;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;

public class airui {
    // 代理隧道验证信息
    final static String ProxyUser = "HJGR1T7575J6744D";
    final static String ProxyPass = "109FD50EC1CC22A7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        airui ai=new airui();
        Ca c=ai.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int p=1;p<=20;p++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public static void data(Ca c) throws InterruptedException {
        for(int x=1;x<=100;x++){
            c.fang(String.valueOf(x));
        }
    }
    public static void serach(Ca c) throws InterruptedException, IOException, SQLException {
        Gson gson=new Gson();
        String sql="insert into airui(app_name,app_logo,app_type,app_ri,app_huan) values(?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            String ye=c.qu();
            if(!Dup.nullor(ye)){
                break;
            }
            String url="http://index.iresearch.com.cn/app/GetDataList/?classId=0&classLevel=0&timeId=56&orderBy=-2&pageIndex="+ye+"&pageSize=undefined";
            Document doc=get(url);
            String json=doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
            Ai a=gson.fromJson(json,Ai.class);
            for(Ai.De d:a.List){
                ps.setString(1,d.AppName);
                ps.setString(2,"http://index.iresearch.com.cn"+d.AppLogo);
                ps.setString(3,d.FclassName+","+d.KclassName);
                ps.setString(4,d.DayMachineNum);
                ps.setString(5,d.DmGrowth);
                ps.executeUpdate();
                aa++;
                System.out.println(aa+"************************************************");
            }
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
                        .timeout(5000)
                        .proxy(proxy)
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


    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(2, TimeUnit.SECONDS);
        }
    }

    public static class Ai{
        public List<De> List;
        public class De{
            public String AppName;
            public String AppLogo;
            public String FclassName;
            public String KclassName;
            public String DayMachineNum;
            public String DmGrowth;
        }
    }
}
