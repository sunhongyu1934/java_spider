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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class th_ke {
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

    public static void main(String args[]) throws IOException {
        th_ke t=new th_ke();
        final Ca c=t.new Ca();
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
                    data(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=1;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void controll() throws InterruptedException, IOException, SQLException {
        Random random = new Random();
        String sql="select * from 36ke_zhanghu";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        Map<String,String> mm=new HashMap<String, String>();
        while (rs.next()){
            String us=rs.getString(rs.findColumn("user"));
            String pas=rs.getString(rs.findColumn("pas"));
            mm.put(us,pas);
        }

        String[] keys = mm.keySet().toArray(new String[0]);
        String uu= keys[random.nextInt(keys.length)];
        String pp=mm.get(uu);
        while (true){
            map=login(uu,pp);
            Thread.sleep(30000);
            uu= keys[random.nextInt(keys.length)];
            pp=mm.get(uu);
        }
    }

    public static void serach(Ca c) throws IOException, InterruptedException {
        Thread.sleep(10000);
        Gson gson=new Gson();
        for(int x=1;x<=4956;x++) {
            try {
                Document doc = get("http://rong.36kr.com/n/api/column/0/company?sortField=HOT_SCORE&p=" + x);
                System.out.println(doc.body());
                String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                SanBean s = gson.fromJson(json, SanBean.class);
                for (SanBean.Da.Page.De d : s.data.pageData.data) {
                    try {
                        c.fang(d.id);
                    }catch (Exception e1){
                        System.out.println("fang error");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("seracg error");
            }
        }
    }

    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select 36k_id from 36ke";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String kid=rs.getString(rs.findColumn("36k_id"));
            c.fang(kid);
        }
    }

    public static void detail(Ca c) throws IOException, SQLException, InterruptedException {
        Gson gson=new Gson();

        String sql="insert into 36ke_company(ke_id,pro_name,start_date,com_scale,com_address,com_weibo,com_intro,com_logo,com_tag,com_biref,com_web,com_prodesc,com_full_name,com_weixin) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="insert into 36ke_fin(ke_id,am_out,am_unit,fin_time,fin_name,fin_round) values(?,?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        String aa = null;
        while (true) {
            try {
                String kid = c.qu();
                Document doc = get("http://rong.36kr.com/n/api/company/" + kid);
                aa=doc.outerHtml();
                Document docfin = get("http://rong.36kr.com/n/api/company/" + kid + "/finance");
                String detail = doc.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                String fin = docfin.body().toString().replace("<body>", "").replace("</body>", "").replace(" ", "").replace("\n", "");
                SanDetailBean.Detail d = gson.fromJson(detail, SanDetailBean.Detail.class);
                SanDetailBean.Finac f = gson.fromJson(fin, SanDetailBean.Finac.class);

                ps.setString(1, kid);
                ps.setString(2, d.data.name);
                ps.setString(3, d.data.startDateDesc);
                ps.setString(4, d.data.scale);
                ps.setString(5, d.data.address1Desc + " " + d.data.address2Desc);
                ps.setString(6, d.data.weibo);
                ps.setString(7, d.data.intro);
                ps.setString(8, d.data.logo);
                String tag = null;
                StringBuffer str = new StringBuffer();
                for (SanDetailBean.Detail.Da.Itag i : d.data.industryTag) {
                    str.append(i.name + ";");
                }
                for (SanDetailBean.Detail.Da.Optag o : d.data.operationTag) {
                    str.append(o.name + ";");
                }
                tag = str.toString();
                ps.setString(9, tag);
                ps.setString(10, d.data.brief);
                ps.setString(11, d.data.website);
                try {
                    ps.setString(12, d.data.companyIntroduce.productService);
                }catch (Exception e){
                    ps.setString(12, null);
                }
                ps.setString(13, d.data.fullName);
                ps.setString(14, d.data.weixin);
                ps.executeUpdate();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (SanDetailBean.Finac.Da dd : f.data) {
                    ps2.setString(1, kid);
                    ps2.setString(2, dd.financeAmount);
                    ps2.setString(3, dd.financeAmountUnit);
                    Long ftime = Long.valueOf(dd.financeDate);
                    ps2.setString(4, simpleDateFormat.format(new Date(ftime)));
                    String vc = null;
                    try {
                        StringBuffer str2 = new StringBuffer();
                        for (SanDetailBean.Finac.Da.Tou t : dd.participantVos) {
                            str2.append(t.entityName + ";");
                        }
                        vc = str2.toString();
                    }catch (Exception ee){

                    }
                    ps2.setString(5, vc);
                    ps2.setString(6, dd.phase);
                    ps2.addBatch();
                }
                ps2.executeBatch();
                a++;
                System.out.println(a + "*******************************************************");
            }catch (Exception e){
                System.out.println(aa);
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
                e.printStackTrace();
                System.out.println("time out detail");
            }
        }
        return doc;
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
