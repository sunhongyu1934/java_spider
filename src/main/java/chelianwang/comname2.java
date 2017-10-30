package chelianwang;

import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class comname2 {
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

    public static void main(String args[]) throws SQLException {
        detail();
    }

    public static void detail() throws SQLException {
        String sql="insert into chuangan(comp_full_name,comp_logo,comp_desc) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Document doc=get("http://www.8339.org/yp8339.asp");
        Elements ele= JsoupUtils.getElements(doc,"div.link_content div.text_link ul li");
        int a=0;
        for(Element e:ele){
            String uid=JsoupUtils.getHref(e,"a","href",0).replace("http://www.8339.org/co.asp?id=","");
            Document docd=get("http://www.8339.org/co/co1/introduce.asp?id="+uid);
            String full_name=JsoupUtils.getString(docd,"div.head_center h1.company_name span",0).replace(" ","");
            String logo=JsoupUtils.getHref(docd,"div.logo img","src",0);
            String desc=JsoupUtils.getString(docd,"span#ctl00_ContentPlaceHolder1_MuliteLabel1",0);

            ps.setString(1,full_name);
            ps.setString(2,logo);
            ps.setString(3,desc);
            ps.executeUpdate();
            a++;
            System.out.println(a+"*******************************************");
        }
    }

    public static Document get(String url){
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
}
