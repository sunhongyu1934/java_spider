package tianyancha.XinxiXin;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/7/10.
 */
public class Linshi_web {
    // 代理隧道验证信息
    final static String ProxyUser = "H0QCBTTB7675S1XD";
    final static String ProxyPass = "26A1FF9238C9050D";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
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
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        ExecutorService pool= Executors.newCachedThreadPool();
        int o=0;
        final Connection finalCon = con;
        for(int x=1;x<=20;x++){
            final int finalO = o;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(finalCon,proxy, finalO);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            o=o+900;
        }

    }

    public static void data(Connection con,Proxy proxy,int o) throws SQLException {
        String sql="select id,cName from company_name_2w limit "+o+",900";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String yid=rs.getString(rs.findColumn("id"));
            String key=rs.getString(rs.findColumn("cName"));
            serach(proxy,key,yid,con);
            p++;
            System.out.println(p+"*************************************************");
        }
    }

    public static void serach(Proxy proxy,String key,String yid,Connection con) throws SQLException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://www.tianyancha.com/search/p" + 1 + "?key=" + URLEncoder.encode(key, "utf-8"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(10000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out");
            }
        }

        Elements eles = getElements(doc, "div.search_result_single.search-2017.pb20.pt20.pl30.pr30");

        if (eles != null) {
            for (Element e : eles) {
                String tid = getHref(e, "div.col-xs-10.search_repadding2.f18 a", "href", 0).replace("http://www.tianyancha.com/company/", "");
                detail(proxy,tid,con,yid);
            }
        }
    }

    public static void detail(Proxy proxy,String tid,Connection con,String yid) throws SQLException {
        String sql="insert into linshi_web_three(y_id,c_name,web) values(?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Document doc = null;
        while (true) {
            try {
                doc = Jsoup.connect("http://www.tianyancha.com/company/" + tid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(10000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out");
            }
        }
        String web=getString(doc,"div.f14.new-c3 div.in-block.vertical-top:contains(网址) a",0);
        String quancheng=getString(doc,"div.company_header_width.ie9Style span.f18.in-block.vertival-middle",0);
        ps.setString(1,yid);
        ps.setString(2,quancheng);
        ps.setString(3,web);
        ps.executeUpdate();
        ps.close();
    }
}
