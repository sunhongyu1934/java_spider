package jinrong;

import Utils.JsoupUtils;
import Utils.RedisClu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class cyp {
    // 代理隧道验证信息
    final static String ProxyUser = "H4KKF9EHDF26260D";
    final static String ProxyPass = "2A64AB23C97FCA79";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int jishu=0;
    private static int pp=0;
    private static Cc cp;

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

    public static void heart() throws SQLException, InterruptedException {
        while (true) {
            String sql = "select 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            Thread.sleep(60000);
        }
    }


    public static void main(String args[]) throws IOException {
        cyp ch=new cyp();
        Ca c=ch.new Ca();
        Da d=ch.new Da();
        Cc cl=new Cc();
        cp=cl;
        ExecutorService pool= Executors.newCachedThreadPool();

        String aa=args[0];
        String bb=args[1];
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

        /*for(int a=1;a<=Integer.parseInt(aa);a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(d,c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/

        for(int b=1;b<=Integer.parseInt(bb);b++){
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

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    heart();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void data(Da d) throws InterruptedException, SQLException {
        String sql="select ye from cyp_ye";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String> list=new ArrayList<>();
        while (rs.next()){
            list.add(rs.getString(rs.findColumn("ye")));
        }

        for(int a=1;a<=3740;a++){
            if(!list.contains(String.valueOf(a))) {
                d.fang(String.valueOf(a));
            }
        }
    }

    public static void data(Ca c) throws InterruptedException, SQLException {
        String sql="select * from cyp_sousuo where c_jian not in (select c_jian from cyp)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            c.fang(new String[]{rs.getString(rs.findColumn("c_url")),rs.getString(rs.findColumn("c_jian")),rs.getString(rs.findColumn("c_logo")),rs.getString(rs.findColumn("c_hy")),rs.getString(rs.findColumn("c_lj")),rs.getString(rs.findColumn("c_lc")),rs.getString(rs.findColumn("c_zjr")),rs.getString(rs.findColumn("c_clsj")),rs.getString(rs.findColumn("c_szqy"))});
        }
    }

    public static void serach(Da d,Ca c) throws IOException, InterruptedException, SQLException {
        String sql1="insert into cyp_ye(ye) values(?)";
        PreparedStatement ps1=conn.prepareStatement(sql1);

        String sql="select c_jian from cyp";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String> list=new ArrayList<>();
        while (rs.next()){
            list.add(rs.getString(rs.findColumn("c_jian")));
        }

        String sql2="insert into cyp_sousuo(c_url,c_jian,c_logo,c_hy,c_lj,c_lc,c_zjr,c_clsj,c_szqy) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        while (true) {
            try {
                String of = d.qu();
                Document doc=get("http://chuangyepu.com/startups.html?page="+of+"&per_page=10&q%5Bindustry_name%5D=");

                Elements ele=JsoupUtils.getElements(doc,"div.startups-playlists table.startups-playlists-table tbody tr");

                boolean bo=true;
                for(Element e:ele){
                    try {
                        String url = "http://chuangyepu.com" + JsoupUtils.getHref(e, "span.name.text-flow a", "href", 0);
                        String jian = JsoupUtils.getString(e, "span.name.text-flow a", 0);

                        if(list.contains(jian)){
                            continue;
                        }

                        bo=false;
                        String logo = JsoupUtils.getHref(e, "img.avatar", "src", 0);
                        String hy = null;
                        StringBuffer str = new StringBuffer();
                        Elements hyele = JsoupUtils.getElements(e, "div.industry span");
                        if(hyele!=null) {
                            for (Element he : hyele) {
                                str.append(JsoupUtils.getString(he, "a", 0) + ";");
                            }
                        }
                        hy = str.toString();
                        String lj = JsoupUtils.getString(e, "td", 1);
                        String lc = JsoupUtils.getString(e, "td", 2);
                        String zjr = JsoupUtils.getString(e, "td", 3);
                        String clsj = JsoupUtils.getString(e, "td", 4);
                        String szqy = JsoupUtils.getString(e, "td", 5);

                        ps2.setString(1,url);
                        ps2.setString(2,jian);
                        ps2.setString(3,logo);
                        ps2.setString(4,hy);
                        ps2.setString(5,lj);
                        ps2.setString(6,lc);
                        ps2.setString(7,zjr);
                        ps2.setString(8,clsj);
                        ps2.setString(9,szqy);
                        ps2.executeUpdate();

                        //c.fang(new String[]{url, jian, logo, hy, lj, lc, zjr, clsj, szqy});
                    }catch (Exception ee){
                        System.out.println("fang error");
                    }
                }

                if(bo){
                    ps1.setString(1,of);
                    ps1.executeUpdate();
                }
                pp++;
                System.out.println(pp+"---------------------------------------------------------------------------");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into cyp(c_jian,c_logo,c_hy,c_lj,c_lc,c_zj,c_cre,c_shd,c_kh,c_di,c_desc,c_web,c_add,c_phone,c_quan,c_faren,c_gudong,c_zhizi,c_zhushi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String[] value = c.qu();
                Document doc = get(value[0]);

                String kh=JsoupUtils.getString(doc,"ul.base_info li.pitch",0);
                String di=JsoupUtils.getString(doc,"ul.base_info li",3);
                String desc=JsoupUtils.getString(doc,"div.container div.section-introduce div.show-content.text-center",0);
                String web=JsoupUtils.getString(doc,"div.col-md-3 div.section-contact div.message div.web p",0);
                String add=JsoupUtils.getString(doc,"div.col-md-3 div.section-contact div.message div.location p",0);
                String phone=JsoupUtils.getString(doc,"div.col-md-3 div.section-contact div.message div.phone p",0);
                String quan=JsoupUtils.getString(doc,"div.section-icmessage table tbody td",1);
                String faren=JsoupUtils.getString(doc,"div.section-icmessage table tbody td",3);
                String gudong=JsoupUtils.getString(doc,"div.section-icmessage table tbody td",5);
                String zhuzi=JsoupUtils.getString(doc,"div.section-icmessage table tbody td",7);
                String zhushi=JsoupUtils.getString(doc,"div.section-icmessage table tbody td",9);

                ps.setString(1,value[1]);
                ps.setString(2,value[2]);
                ps.setString(3,value[3]);
                ps.setString(4,value[4]);
                ps.setString(5,value[5]);
                ps.setString(6,value[6]);
                ps.setString(7,value[7]);
                ps.setString(8,value[8]);
                ps.setString(9,kh);
                ps.setString(10,di);
                ps.setString(11,desc);
                ps.setString(12,web);
                ps.setString(13,add);
                ps.setString(14,phone);
                ps.setString(15,quan);
                ps.setString(16,faren);
                ps.setString(17,gudong);
                ps.setString(18,zhuzi);
                ps.setString(19,zhushi);
                ps.executeUpdate();

                jishu++;
                System.out.println(jishu + "*********************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static Document get(String url) throws IOException, InterruptedException {
        Document doc= null;
        while (true) {
            try {
                String ip=cp.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                       // .proxy(proxy)
                        .timeout(5000)
                        .get();
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    if (!cp.po.contains(ip)) {
                        for (int x = 1; x <= 5; x++) {
                            cp.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Da{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                String ip=rd.get("ip");
                cp.fang(ip);
                System.out.println(cp.po.size()+"    ip***********************************************");
                Thread.sleep(1000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }


    public static class Cc{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }


    public static class serach{
        public List<co> company_list;
        public static class co{
            public String url;
            public String name;
            public String companyImg;
            public String setupTime;
            public String financingStep;
            public String city;
        }
    }
}
