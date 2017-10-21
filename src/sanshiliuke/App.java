package sanshiliuke;

import Utils.Dup;
import com.google.gson.Gson;
import com.sun.research.ws.wadl.Doc;
import org.apache.poi.ss.formula.functions.T;
import org.eclipse.jetty.util.StringUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class App {
    // 代理隧道验证信息
    final static String ProxyUser = "H37O6V2M6C29YK9D";
    final static String ProxyPass = "2BE6C0719BB6A39B";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static java.sql.Connection conn;
    private static int a=0;
    private static Map<String,String> map;
    private static String user;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://47.95.31.183:3306/innotree_data_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }

    public static void main(String args[]) throws IOException, SQLException, InterruptedException {
        App a=new App();
        final Ca c=a.new Ca();
        final Cc cc=a.new Cc();
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

        for(int x=1;x<=1;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(c,cc);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int y=1;y<=20;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        deta(cc);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    controll();
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

    public static Map<String,String> login(String s, String s1) throws IOException {
        Connection.Response doc=Jsoup.connect("https://rong.36kr.com/api/passport/v1/ulogin")
                .userAgent("36kr-Tou-iOS/3.3 (iPhone5S) (UID:1499348043); iOS 10.3.3; Scale/2.0")
                .header("Host","rong.36kr.com")
                .header("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
                .header("Accept-Language","zh-cn")
                .header("Accept","*/*")
                .header("Accept-Encoding","gzip, deflate")
                .method(Connection.Method.POST)
                .data("phone",s)
                .data("type","PHONE")
                .data("password",s1)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute();
        System.out.println(s+"      "+s1);
        System.out.println(doc.body());
        return doc.cookies();

    }


    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select `ci` from 36ke_reci where id>100";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("ci"));
            c.fang(cname);
        }
    }

    public static void controll() throws InterruptedException, IOException, SQLException {
        Random r=new Random();
        while (true) {
            String sql = "select * from 36ke_zhanghu where iflag is null";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Map<String, String> mm = new HashMap<String, String>();
            while (rs.next()) {
                String us = rs.getString(rs.findColumn("user"));
                String pas = rs.getString(rs.findColumn("pas"));
                mm.put(us, pas);
            }

            for (Map.Entry<String, String> entry : mm.entrySet()) {
                map = login(entry.getKey(), entry.getValue());
                user = entry.getKey();
                Thread.sleep(r.nextInt(20000) + 30000);
            }
            Thread.sleep(10000);
        }
    }

    public static void zhuxiao() throws SQLException {
        String sql="update 36ke_zhanghu set iflag=1 where user=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,user);
        ps.executeUpdate();
    }

    public static void chongzhi() throws SQLException {
        String sql="update 36ke_zhanghu set iflag=null";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.executeUpdate();
    }

    public static void zhuxiaoquan() throws SQLException {
        String sql="update 36ke_zhanghu set iflag=1";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.executeUpdate();
    }

    public static void serach(Ca c,Cc cc) throws InterruptedException, UnsupportedEncodingException, SQLException {
        int dd=0;
        while (true){
            Thread.sleep(10000);
            if(map!=null){
                break;
            }
            dd++;
            if(dd>=100){
                zhuxiaoquan();
                Thread.sleep(43200000);
                chongzhi();
                dd=0;
            }
        }
        Gson gson=new Gson();
        Random r=new Random();
        int da=0;
        while (true){
            try {
                String cname = c.qu();
                for(int p=1;p<=1000;p++) {
                    try {
                        String page = null;
                        while (true) {
                            try {
                                if(da>=20){
                                    zhuxiaoquan();
                                    Thread.sleep(43200000);
                                    chongzhi();
                                    da=0;
                                }
                                Document doc = serget("https://rong.36kr.com/api/mobi-investor/search/v2/company?keyword=" + URLEncoder.encode(cname, "utf-8") + "&page=" + p);
                                int th = r.nextInt(4) + 4;
                                Thread.sleep(th * 1000);
                                String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "").replaceAll("</.+>","");
                                AppBean.serach s = gson.fromJson(json, AppBean.serach.class);
                                for (AppBean.serach.Da.det d : s.data.data) {
                                    cc.fang(new String[]{d.id, d.fullName, d.intro});
                                }
                                page = s.data.totalPages;
                                if (s.msg.contains("搜索太频繁了")) {
                                    da++;
                                    zhuxiao();
                                    Thread.sleep(50000);
                                } else {
                                    break;
                                }
                            }catch (Exception eee){
                                System.out.println("haishi error");
                                break;
                            }
                        }
                        if(!Dup.nullor(page)){
                            break;
                        }else if(Integer.parseInt(page)==p){
                            break;
                        }else if(Integer.parseInt(page)==0){
                            break;
                        }
                    }catch (Exception ee){
                        System.out.println("fangye error");
                    }
                }
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }


    public static void deta(Cc c) throws InterruptedException, SQLException {
        String sql="insert into 36ke_company(ke_id,pro_name,com_st,com_address,com_intro,com_logo,com_tag,com_web,com_full_name,com_weixin) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Gson gson=new Gson();
        int a=0;
        while (true){
            try {
                String[] kid = c.qu();
                Document doc = get("https://rong.36kr.com/api/mobi-investor/v3/company/" + kid[0] + "?stats_refer=search_list");
                String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                AppBean.detail d = gson.fromJson(json, AppBean.detail.class);
                ps.setString(1, kid[0]);
                ps.setString(2, d.data.basic.name);
                ps.setString(3, d.data.basic.startAt);
                ps.setString(4, d.data.basic.address1);
                ps.setString(5, kid[2]);
                ps.setString(6, d.data.basic.logo);
                String tag = null;
                StringBuffer str = new StringBuffer();
                for (AppBean.detail.Da.Ta.dd t : d.data.tags.data) {
                    str.append(t.name + ";");
                }
                tag = str.toString();
                ps.setString(7, tag);
                ps.setString(8, d.data.relatedLink.website);
                ps.setString(9, kid[1]);
                ps.setString(10, d.data.relatedLink.weixin);
                ps.executeUpdate();
                a++;
                System.out.println(a + "*******************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }


    public static Document get(String url){
        System.out.println(url);
        Document doc;
        while (true){
            try {
                doc = Jsoup.connect(url.replace("https","http"))
                        .userAgent("36kr-Tou-iOS/3.3 (iPhone5S) (UID:1499348043); iOS 10.3.3; Scale/2.0")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .header("Host","rong.36kr.com")
                        .header("endpoint","login")
                        .header("Accept","*/*")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-cn")
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && doc.outerHtml().length() > 50) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static Document serget(String url){
        System.out.println(url);
        Document doc;
        while (true){
            try {
                doc = Jsoup.connect(url.replace("https","http"))
                        .userAgent("36kr-Tou-iOS/3.3 (iPhone5S) (UID:1499348043); iOS 10.3.3; Scale/2.0")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .header("Host","rong.36kr.com")
                        .header("endpoint","login")
                        .header("Accept","*/*")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-cn")
                        .proxy(proxy)
                        .cookies(map)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && doc.outerHtml().length() > 50) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String> bo=new LinkedBlockingQueue();
        public void fang(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.take();
        }
    }

    class Cc{
        BlockingQueue<String[]> bo=new LinkedBlockingQueue();
        public void fang(String[] key) throws InterruptedException {
            bo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return bo.take();
        }
    }
}
