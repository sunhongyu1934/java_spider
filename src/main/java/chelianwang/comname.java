package chelianwang;

import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class comname {
    // 代理隧道验证信息
    final static String ProxyUser = "H71K6773EZ870E0D";
    final static String ProxyPass = "341C5939CFF9F8B5";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int j=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/tyc_xin?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        comname c=new comname();
        final Ca cc=c.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serach(cc);
                } catch (IOException e) {
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
                        detail(cc);
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

    public static void serach(Ca c) throws IOException, InterruptedException {
        String ss[]=new String[]{"yiliaoyiqi","2296","2297","2298","2299","yaliyibiao","2301","diangongyibiao"};

        for(String s: ss) {
            for (int x = 1; x <= 100; x++) {
                try {
                    Document doc = get("http://www.chinasensor.cn/company/"+s+"/company_list_" + x + ".html");
                    Elements ele = JsoupUtils.getElements(doc, "div.list");
                    for (Element e : ele) {
                        try {
                            String url = JsoupUtils.getHref(e, "ul li a", "href", 0);
                            String add = JsoupUtils.getString(e, "td.f_orange", 0);
                            String logo = JsoupUtils.getHref(e, "a img", "src", 0);
                            c.fang(new String[]{url, add, logo});
                        } catch (Exception ee) {
                            System.out.println("fang error");
                        }
                    }
                    System.out.println(x + "#########################################################");
                } catch (Exception e) {
                    System.out.println("ser error");
                }
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into chuangan(comp_full_name,comp_logo,comp_tag,comp_desc,comp_lei,comp_gui,comp_zhuzi,comp_zhushi,comp_ziren,comp_jm,comp_jf,comp_sx,comp_cc,comp_zy,comp_add) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String[] va = c.qu();
                Document doc = get(va[0]+"&file=introduce");
                String fullname = JsoupUtils.getString(doc, "div.head div h1", 0);
                String tag = JsoupUtils.getString(doc, "div.head div h4", 0);
                String desc = JsoupUtils.getString(doc, "div.main_body div.lh18.px13 td", 0);
                String lei = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 4);
                String guimo = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 8);
                String zhuci = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 10);
                String zhunian = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 12);
                String ziren = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 14);
                String jingmo = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 16);
                String jingfan = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 18);
                String xiaochan = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 20);
                String caichan = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 22);
                String zhuhang = JsoupUtils.getString(doc, "div.main_body div.px13.lh18 td", 24);

                ps.setString(1, fullname);
                ps.setString(2, va[2]);
                ps.setString(3, tag);
                ps.setString(4, desc);
                ps.setString(5, lei);
                ps.setString(6, guimo);
                ps.setString(7, zhuci);
                ps.setString(8, zhunian);
                ps.setString(9, ziren);
                ps.setString(10, jingmo);
                ps.setString(11, jingfan);
                ps.setString(12, xiaochan);
                ps.setString(13, caichan);
                ps.setString(14, zhuhang);
                ps.setString(15, va[1]);
                ps.executeUpdate();
                j++;
                System.out.println(c.po.size() + "************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(5000)
                        .get();
                if (doc != null) {
                    break;
                }
            }catch (Exception e){
                System.out.println("get error");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
