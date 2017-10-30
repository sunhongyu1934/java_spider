package App;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.*;
import java.sql.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/8/31.
 */
public class quanying {
    // 代理隧道验证信息
    final static String ProxyUser = "H689K6HN8Z46666D";
    final static String ProxyPass = "3E0DCB86C35C5504";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.51.120.107:3306/nlp_company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="sunhongyu";
        String password="yJdviIeSuicKn8xX";
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
    public static void main(String args[]) throws SQLException {
        quanying q=new quanying();
        final Ca c=q.new Ca();

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
        for(int x=1;x<=20;x++){
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
        }

    }

    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select id,q_name from quanying_linshi";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String q_name=rs.getString(rs.findColumn("q_name"));
            c.jin(new String[]{id,q_name});
        }
    }

    public static void get(Ca c) throws InterruptedException, SQLException {
        String sql="update quanying_linshi set q_source=? where id=?";
        PreparedStatement ps= conn.prepareStatement(sql);
        Gson gson=new Gson();
        while (true) {
            try {
                String[] value = c.qu();
                if (value == null || value.length < 2) {
                    break;
                }
                String id = value[0];
                String key = value[1];
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://index.iresearch.com.cn/app/GetDataList/?name=" + URLEncoder.encode(key, "UTF-8") + "&timeId=54&orderBy=-2&pageIndex=1&pageSize=99999")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
                                .proxy(proxy)
                                .timeout(5000)
                                .get();
                        if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out");
                    }
                }

                String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                Ja j = gson.fromJson(json, Ja.class);
                if (j.List != null && j.List.size()>0) {
                    ps.setString(1, String.valueOf(Integer.parseInt(j.List.get(0).DayMachineNum.replace(".00",""))*10000));
                    ps.setString(2, id);
                    ps.executeUpdate();
                }
                System.out.println(c.po.size() + "*****************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void jin(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }

    static class Ja{
        public List<De> List;
        public static class De{
            public String DayMachineNum;
            @Override
            public String toString() {
                return "De{" +
                        "DayMachineNum='" + DayMachineNum + '\'' +
                        '}';
            }
        }

    }

}


