package chelianwang;

import Utils.Dup;
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

public class oledw {
    // 代理隧道验证信息
    final static String ProxyUser = "H0NO3LHXZIB40G8D";
    final static String ProxyPass = "34F572159E991E7C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int su=0;

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

    public static void main(String args[]) throws IOException {
        oledw o=new oledw();
        Ca c=o.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serach(c);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=20;a++){
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

    public static void serach(Ca c) throws IOException, InterruptedException {
        for(int x=1;x<=30;x++){
            for(int y=1;y<=200;y++) {
                try {
                    Document doc = get("http://www.oledw.com/corporation/list-" + x + "-"+y+".html");
                    String dalei=JsoupUtils.getString(doc,"div.m div.m_l_1.f_l div.left_box div.pos a",2);
                    Elements ele = JsoupUtils.getElements(doc, "div.list");
                    if(ele==null||!Dup.nullor(ele.toString())){
                        break;
                    }
                    for (Element e : ele) {
                        try {
                            String quan = JsoupUtils.getString(e, "ul li a strong.px14", 0);
                            String url = JsoupUtils.getHref(e, "ul li a", "href", 0);
                            String logo = JsoupUtils.getHref(e, "a img", "src", 0);
                            String tag = JsoupUtils.getString(e, "ul li.f_gray", 0);

                            if (Dup.nullor(url)) {
                                c.fang(new String[]{quan, url + "introduce/", logo, tag,dalei});
                            } else {
                                System.out.println(x);
                            }
                        } catch (Exception ee) {
                            System.out.println("fang error");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("serach error");
                }
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into oled(comp_full_name,comp_tag,comp_logo,comp_desc,comp_lei,comp_add,comp_zhuzi,comp_zhunian,comp_ziren,comp_bz,comp_jm,comp_jf,comp_xc,comp_zh,comp_guimo,comp_dalei) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String[] value = c.qu();
                Document doc = get(value[1]);
                String desc = JsoupUtils.getString(doc, "div.main_body div.lh18.px13 tbody tr", 0);
                String lei = JsoupUtils.getString(doc, "div.main_body table:nth-child(1) tbody tr:nth-child(1) td", 4);
                String add = JsoupUtils.getString(doc, "div.main_body table:nth-child(1) tbody tr:nth-child(2) td", 1);
                String guimo = JsoupUtils.getString(doc, "div.main_body table:nth-child(1) tbody tr:nth-child(2) td", 3);
                String zhuzi = JsoupUtils.getString(doc, "div.main_body table:nth-child(1) tbody tr:nth-child(3) td", 1);
                String zhunian = JsoupUtils.getString(doc, "div.main_body table:nth-child(1) tbody tr:nth-child(3) td", 3);
                String ziren = JsoupUtils.getString(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(1) td", 1);
                String bao = JsoupUtils.getString(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(2) td", 1);
                String jingmo = JsoupUtils.getString(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(3) td", 1);
                String jingfan = JsoupUtils.getString(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(4) td", 1);
                String xiaochan = JsoupUtils.getString(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(5) td", 1);
                String zhuhang = null;
                StringBuffer str = new StringBuffer();
                Elements zhele = JsoupUtils.getElements(doc, "div.main_body table:nth-child(2) tbody tr:nth-child(6) td a");
                for (Element e : zhele) {
                    str.append(e.text() + ",");
                }
                try {
                    zhuhang = str.substring(0, str.length() - 1).toString();
                } catch (Exception ee) {

                }

                ps.setString(1,value[0]);
                ps.setString(2,value[3]);
                ps.setString(3,value[2]);
                ps.setString(4,desc);
                ps.setString(5,lei);
                ps.setString(6,add);
                ps.setString(7,zhuzi);
                ps.setString(8,zhunian);
                ps.setString(9,ziren);
                ps.setString(10,bao);
                ps.setString(11,jingmo);
                ps.setString(12,jingfan);
                ps.setString(13,xiaochan);
                ps.setString(14,zhuhang);
                ps.setString(15,guimo);
                ps.setString(16,value[4]);
                ps.executeUpdate();

                su++;
                System.out.println(su+"************************************************************");
            }catch (Exception e){
                System.out.println("error");
            }

        }
    }


    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .proxy(proxy)
                        .get();
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out detail");
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

}
