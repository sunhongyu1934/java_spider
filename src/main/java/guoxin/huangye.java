package guoxin;

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

public class huangye {
    // 代理隧道验证信息
    final static String ProxyUser = "H88A4Q10G0V31YCD";
    final static String ProxyPass = "C2E28C06C89836C4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int a=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/tyc_xin?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        huangye h=new huangye();
        final Ca c=h.new Ca();
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

        /*for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c);
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
    }

    public static void serach(Ca c) throws IOException, InterruptedException {
        System.out.println("a");
        Document doc=get("http://www.yellowpage.com.cn/ypm/edt/index.html");
        System.out.println("b");
        System.out.println(doc.outerHtml());
        Elements ele=JsoupUtils.getElements(doc,"div#main dd");
        System.out.println(ele.size());
        for(Element e:ele){
            System.out.println(JsoupUtils.getHref(e,"a","href",0));
            c.fang(JsoupUtils.getHref(e,"a","href",0));
        }
    }

    public static void detail(Ca c) throws IOException, InterruptedException, SQLException {
        String sql="insert into kunming_name(comp_full_name,c_url) values(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true) {
            try {
                String url = c.qu();
                for(int p=1;p<=1000;p++) {
                    Document doc = get(url+"pn"+p);
                    Elements ele = JsoupUtils.getElements(doc, "ul.companylist li");
                    for (Element e : ele) {
                        String cname = JsoupUtils.getString(e, "h4 a", 0);
                        String curl = JsoupUtils.getHref(e, "h4 a", "href", 0);
                        if (Dup.nullor(cname)) {
                            ps.setString(1, cname);
                            ps.setString(2, curl);
                            ps.executeUpdate();
                            a++;
                            System.out.println(a + "*****************************************************");
                        }
                    }
                    if(ele==null||ele.size()<50){
                        break;
                    }
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            doc= Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .timeout(5000)
                    .get();
            if(doc!=null&&doc.outerHtml().length()>44&&!doc.outerHtml().contains("abuyun")){
                break;
            }
        }
        return doc;
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
