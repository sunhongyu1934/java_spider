package linshi_spider;

import Utils.JsoupUtils;
import org.apache.commons.lang.StringUtils;
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

public class atubo {
    // 代理隧道验证信息
    final static String ProxyUser = "HP5G1I415085Y7AD";
    final static String ProxyPass = "9CDAD2529F99DC54";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.44:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        atubo a=new atubo();
        Ca c=a.new Ca();
        Ye y=a.new Ye();
        String[] str=new String[]{"-q3088","-q3095","-q3091","-q3096","-q3090","-q3094","-q3092","-q3085","-q3089","-q3086","-q3083","-q3093","-q3084","-q3082","-q3097","-q3087"};
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int q=0;q<str.length;q++){
            int finalQ = q;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(c,y,str[finalQ]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    da(y);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int w=1;w<=20;w++){
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

    public static void da(Ye y) throws InterruptedException {
        for(int a=1;a<=703;a++){
            y.fang(String.valueOf(a));
        }
    }

    public static void data(Ca c,Ye y,String q) throws IOException, InterruptedException {

        for(int p=1;p<=50;p++) {
            Document doc = get("http://www.atobo.com.cn/Companys/s-p1-s870"+q+"-k41432-y" + p + "/");
            Elements ele = JsoupUtils.getElements(doc, "div.product_contextlist.bplist li.product_box");
            for (Element e : ele) {
                String coname = JsoupUtils.getString(e, "li.pp_name a", 0);
                String add = JsoupUtils.getString(e, "li.pp_address", 0).replace("地址：", "");
                String url = JsoupUtils.getHref(e, "li.pp_name a", "href", 0);

                c.fang(new String[]{coname, add, url});
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into atubo(comp_full_name,comp_add,comp_desc) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            String[] value=c.qu();
            Document doc=get(value[2]);
            String desc=JsoupUtils.getString(doc,"div.homep-list div.aboutus",0);

            ps.setString(1,value[0]);
            ps.setString(2,value[1]);
            ps.setString(3,desc);
            ps.executeUpdate();
            aa++;
            System.out.println(aa+"*************************************************");
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
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Ye{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String ye) throws InterruptedException {
            po.put(ye);
        }
        public String  qu() throws InterruptedException {
            return po.take();
        }
    }

}
