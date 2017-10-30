package sanshiliuke;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
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

public class ke_list {
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

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
    public static void main(String args[]) throws InterruptedException, SQLException, IOException {
        ke_list k=new ke_list();
        final Ca c=k.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
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

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serach(c);
                } catch (IOException e) {
                    e.printStackTrace();
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
                        detail(c);
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

    public static void serach(Ca c) throws IOException, SQLException, InterruptedException {
        Random r=new Random();
        Gson gson=new Gson();
        String indus[]=new String[]{"E_COMMERCE","SOCIAL_NETWORK","INTELLIGENT_HARDWARE","MEDIA","SOFTWARE","CONSUMER_LIFESTYLE","FINANCE","MEDICAL_HEALTH","SERVICE_INDUSTRIES","TRAVEL_OUTDOORS","PROPERTY_AND_HOME_FURNISHINGS","EDUCATION_TRAINING","AUTO","LOGISTICS","AI","UAV","ROBOT","VR_AR","SPORTS","FARMING","SHARE_BUSINESS","CHU_HAI","CONSUME"};
        for(String s:indus){
            try {
                Document doc = get("https://rong.36kr.com/n/api/column/0/company?industry=" + s + "&sortField=HOT_SCORE&p=1");
                String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                kelistbean.one o = gson.fromJson(json, kelistbean.one.class);
                for (kelistbean.one.Da.Lab l : o.data.label) {
                    try {
                        c.fang("https://rong.36kr.com/n/api/column/0/company?industry=" + s + "&label=" + l.id + "&sortField=HOT_SCORE&p=");
                    }catch (Exception e){
                        System.out.println("bianli error");
                    }
                }
                Thread.sleep((r.nextInt(4)+1) * 1000);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("indus error");
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        Gson gson=new Gson();
        Random r=new Random();
        String sql="insert into 36ke_serach(k_id,p_name,p_logo,p_tag) values(?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        int a=0;
        while (true){
            try {
                String url = c.qu();
                for(int p=1;p<=50;p++) {
                    Document doc = get(url+p);
                    String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                    kelistbean.detail d = gson.fromJson(json, kelistbean.detail.class);

                    for (kelistbean.detail.Da.Page.De s : d.data.pageData.data) {
                        try {
                            String tag = null;
                            StringBuffer str = new StringBuffer();
                            for (String ss : s.tags) {
                                str.append(ss + ";");
                            }
                            tag = str.toString();
                            String kid = s.id;
                            String logo = s.logo;
                            String pname = s.name;

                            ps.setString(1, kid);
                            ps.setString(2, pname);
                            ps.setString(3, logo);
                            ps.setString(4, tag);
                            ps.executeUpdate();
                            a++;
                            System.out.println(a + "***************************************************");
                        } catch (Exception ee) {
                            System.out.println("detail error");
                        }
                    }
                    Thread.sleep((r.nextInt(4)+1) * 1000);
                    String ye = d.data.pageData.totalPages;
                    if(Integer.parseInt(ye)==p){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }



    public static Document get(String url) throws IOException {
        Document doc;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
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

    public static void controll() throws InterruptedException, IOException, SQLException {
        Random random = new Random();
        String sql="select * from 36ke_zhanghu";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        Map<String,String> mm=new HashMap<String, String>();
        int a=1;
        while (rs.next()){
            String us=rs.getString(rs.findColumn("user"));
            String pas=rs.getString(rs.findColumn("pas"));
            mm.put(String.valueOf(a),us+"###"+pas);
            a++;
        }

        int b=1;
        while (true){
            map=login(mm.get(String.valueOf(b)).split("###",2)[0],mm.get(String.valueOf(b)).split("###",2)[1]);
            Thread.sleep(30000);
            b++;
            if(b==13){
                b=1;
            }
        }
    }

    public static Map<String,String> login(String usr,String pas) throws IOException {
        Connection.Response doc= Jsoup.connect("http://passport.36kr.com/passport/sign_in")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .header("X-Tingyun-Id","Dio1ZtdC5G4;r=31637955")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Encoding","gzip, deflate, br")
                .header("Host","passport.36kr.com")
                .header("Origin","https://passport.36kr.com")
                .header("Referer","https://passport.36kr.com/pages/?ok_url=https%3A%2F%2Frong.36kr.com%2Flist%2Fdetail%26%3FsortField%3DHOT_SCORE")
                .header("Accept-Language","zh-CN,zh;q=0.8")
                .data("type","login")
                .data("bind","false")
                .data("needCaptcha","false")
                .data("username",usr)
                .data("password",pas)
                .data("ok_url","https%3A%2F%2Frong.36kr.com%2Flist%2Fdetail%26%3FcolumnId%3D0%26sortField%3DHOT_SCORE")
                .data("ktm_reghost","null")
                .method(Connection.Method.POST)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .execute();
        return doc.cookies();
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
