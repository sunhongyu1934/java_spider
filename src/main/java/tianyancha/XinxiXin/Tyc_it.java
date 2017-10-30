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
 * Created by Administrator on 2017/7/13.
 */
public class Tyc_it {
    // 代理隧道验证信息
    final static String ProxyUser = "HAE90176N56R531D";
    final static String ProxyPass = "5F0997AE8D03856B";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
            o=o+220;
        }


    }


    public static void data(Connection con,Proxy proxy,int o) throws SQLException {
        String sql="select id,comp_name_q from si_bulu limit "+o+",220";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String yid=rs.getString(rs.findColumn("id"));
            String key=rs.getString(rs.findColumn("comp_name_q"));
            serach(proxy,key,yid,con);
            p++;
            System.out.println(p+"*************************************************");
        }
    }

    public static void serach(Proxy proxy,String key,String yid,Connection con) throws SQLException {
        String sql="insert into tyc_si_company(`keys`,`values`,tid,beisou_name,g_id) values(?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("https://www.tianyancha.com/search/p" + 1 + "?key=" + URLEncoder.encode(key, "utf-8"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
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
                String cname=getString(e, "div.col-xs-10.search_repadding2.f18 a", 0);
                String keys=getString(e,"div.search_row_new div.add span",0);
                String value=getString(e,"div.search_row_new div.add span",2);

                ps.setString(1,keys);
                ps.setString(2,value);
                ps.setString(3,tid);
                ps.setString(4,cname);
                ps.setString(5,yid);
                ps.executeUpdate();
            }
        }
        ps.close();
    }


}
