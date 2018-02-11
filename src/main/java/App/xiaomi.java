package App;

import com.google.gson.Gson;
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
import java.util.List;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/8/16.
 */
public class xiaomi {
    // 代理隧道验证信息
    final static String ProxyUser = "H4KKF9EHDF26260D";
    final static String ProxyPass = "2A64AB23C97FCA79";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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
    public static void main(String args[]) throws IOException, InterruptedException {
        ExecutorService pool= Executors.newCachedThreadPool();
        xiaomi x=new xiaomi();
        final Fk f=x.new Fk();
        final Url u=x.new Url();
        serach(f);

        for(int a=1;a<=5;a++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach2(f,u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for(int b=1;b<=20;b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(u);
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

    public static void serach(Fk f) throws IOException, InterruptedException {
        Document doc=get("http://app.mi.com/");
        Elements ele=getElements(doc,"div.sidebar-mod ul.category-list li");
        if(ele!=null){
            for(Element e:ele){
                String lie=getHref(e,"a","href",0).replace("/category/","");
                if(StringUtils.isNotEmpty(lie)) {
                    f.fang(lie);
                }
            }
        }
    }

    public static void serach2(Fk f,Url u) throws IOException, InterruptedException {
        Gson gson=new Gson();
        while (true) {
            String value=f.qu();
            if(StringUtils.isEmpty(value)){
                break;
            }
            Document doc;
            for (int x = 0; x <= 1000; x++) {
                try {
                    doc = get("http://app.mi.com/categotyAllListApi?page=" + x + "&categoryId=" + value + "&pageSize=30");
                    String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").replace(" ", "").trim();
                    xiaomijson xx = gson.fromJson(json, xiaomijson.class);
                    if (xx.data == null || xx.data.size() == 0) {
                        break;
                    }
                    for (xiaomijson.Da s : xx.data) {
                        String baoming = s.packageName;
                        if (StringUtils.isNotEmpty(baoming)) {
                            u.fang(baoming);
                        }
                    }
                }catch (Exception e){
                    System.out.println("serach error");
                }
            }
        }
    }

    public static void detail(Url u) throws InterruptedException, IOException, SQLException {
        String sql="insert into xiaomi_xin(a_name,a_logo,a_bao,a_ping,a_size,a_fen,a_update,a_ban,a_kaifa,a_jietu,a_desc,a_all) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Document doc;
        while (true){
            try {
                String value = u.qu();
                if (StringUtils.isEmpty(value)) {
                    break;
                }
                doc = get("http://app.mi.com/details?id=" + value);
                String logo = getHref(doc, "div.app-intro.cf div.app-info img", "src", 0);
                String ming = getString(doc, "div.app-intro.cf div.app-info div.intro-titles h3", 0);
                String pingfen = getString(doc, "span.app-intro-comment", 0).replace("( ", "").replace("次评分 )", "");
                String fenlei = getElement(doc, "div.app-intro.cf div.app-info p.special-font.action", 0).toString().split("<span style=", 2)[0].replace("<p class=\"special-font action\"><b>分类：</b>", "");
                String kaifa = getString(doc, "div.app-intro.cf div.app-info p", 0);
                String size = getString(doc, "div.look-detail div.details.preventDefault li", 1);
                String banben = getString(doc, "div.look-detail div.details.preventDefault li", 3);
                String gengxin = getString(doc, "div.look-detail div.details.preventDefault li", 5);
                String jietu = "";
                Elements jele = getElements(doc, "div.img-view div.img-list.height-auto img");
                StringBuffer str = new StringBuffer();
                if (jele != null) {
                    for (Element e : jele) {
                        str.append(getHref(e, "img", "src", 0) + ";");
                    }
                }
                if(str!=null&&str.length()>1) {
                    jietu = str.substring(0, str.length() - 1);
                }
                String jieshao = getString(doc, "div.app-text p.pslide", 0);
                String tongkai = "";
                StringBuffer str1 = new StringBuffer();
                Elements tele = getElements(doc, "div.second-imgbox ul li");
                if (tele != null) {
                    for (Element e : tele) {
                        str1.append(getString(e, "a", 1));
                    }
                }
                if(str1!=null&&str1.length()>1) {
                    tongkai = str1.substring(0, str1.length() - 1);
                }

                ps.setString(1, ming);
                ps.setString(2, logo);
                ps.setString(3, value);
                ps.setString(4, pingfen);
                ps.setString(5, size);
                ps.setString(6, fenlei);
                ps.setString(7, gengxin);
                ps.setString(8, banben);
                ps.setString(9, kaifa);
                ps.setString(10, jietu);
                ps.setString(11, jieshao);
                ps.setString(12, tongkai);
                ps.executeUpdate();
                System.out.println(u.bo.size() + "************************************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
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
                        .proxy(proxy)
                        .timeout(5000)
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



    class Url {
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.poll(60, TimeUnit.SECONDS);
        }
    }

    class Fk{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.poll(10,TimeUnit.SECONDS);
        }
    }

    static class xiaomijson{
        public List<Da> data;
        public static class Da{
            public String packageName;
        }
    }
}
