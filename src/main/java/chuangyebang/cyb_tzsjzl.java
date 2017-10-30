package chuangyebang;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Utils.JsoupUtils.*;
import static chuangyebang.cyb_tzsj.storedata;

/**
 * Created by Administrator on 2017/8/11.
 */
public class cyb_tzsjzl {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;

    static{
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ParseException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        get(con);

    }

    public static void get(Connection con) throws IOException, InterruptedException, ParseException, SQLException {
        int p=1;
        while (true) {
            try {
                Document doc = get("http://www.cyzone.cn/event/list-764-0-" + p + "-0-0-0-0/");
                Elements ele = getElements(doc, "div.list-table3 table>tbody>tr.table-plate3");
                int pp = 1;
                if (ele != null) {
                    Date datee = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date1 = simpleDateFormat.format(datee);
                    long dd1 = simpleDateFormat.parse(date1).getTime();
                    long dd2 = dd1 - (2 - 1) * 24 * 60 * 60 * 1000;
                    for (Element e : ele) {
                        String time = getString(e, ">td", 6);
                        long date3 = simpleDateFormat.parse(time).getTime();
                        if (date3 <= dd2) {
                            break;
                        }
                        pp++;
                    }
                }
                Elements dele = getElements(doc, "div.list-table3 table>tbody>tr.table-plate3");
                boolean br = false;
                int oo = 1;
                if (dele != null) {
                    for (Element e : dele) {
                        try {
                            if (pp > 1 && oo == pp - 1 || pp == 1 && oo == pp) {
                                br = true;
                                break;
                            }
                            String detailurl = getHref(e, "td.tp2 span.tp2_tit a", "href", 0);
                            String cid = detailurl.split("/", 6)[3] + "-" + detailurl.split("/", 6)[4] + "-" + detailurl.split("/", 6)[5].replace(".html", "");
                            String[] table = new String[]{"cyb_company", "cyb_dt", "cyb_finacing", "cyb_founders", "cyb_news", "cyb_tag"};
                            for (int g = 0; g < table.length; g++) {
                                flagdata(con, cid, table[g]);
                            }
                            getdetail(detailurl, con);
                        }catch (Exception e1){

                        }
                        oo++;
                    }
                }
                if (br) {
                    break;
                }
                p++;
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static void flagdata(Connection con,String cid,String table) throws SQLException {
        String sql1="delete from "+table+" where cid='"+cid+"'";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static void getdetail(String url,Connection con) throws SQLException, InterruptedException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println(url);
                System.out.println("time out detail");
            }
        }
        System.out.println("request detail success and begin store data");
        storedata(con, doc, url);
        System.out.println("success_cyb-tzsjzl");
        System.out.println("insert mysql success");
        System.out.println("-----------------------------------------------------------------");
    }

    public static Document get(String url) throws IOException, InterruptedException {
        Document doc;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())  && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("time out");
            }
        }
        return doc;
    }
}
