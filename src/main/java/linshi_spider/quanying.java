package linshi_spider;

import Utils.Dup;
import Utils.JsoupUtils;
import com.google.gson.Gson;
import org.jsoup.nodes.Document;

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

public class quanying {
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
    public static void main(String args[]) throws IOException, SQLException {
        get();
    }

    public static void get() throws IOException, SQLException {
        String sql="insert into quanying_linshi(Title,Phone,Address,Special) values(?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Gson gson=new Gson();
        int a=0;
        for(int p=1;p<=55;p++) {
            Document doc = JsoupUtils.get("https://www.yaofangwang.com/Handler/Handler.ashx?method=Get_Hospital&region_id=2212&para=&page_index="+p+"&page_size=20");
            String json= Dup.qujson(doc);
            yiyuan y=gson.fromJson(json,yiyuan.class);
            for(yiyuan.ite i:y.items){
                ps.setString(1,i.Title);
                ps.setString(2,i.Phone);
                ps.setString(3,i.Address);
                ps.setString(4,i.Special);
                ps.executeUpdate();
                a++;
                System.out.println(a+"************");
            }
        }


    }

    public static class yiyuan{
        public List<ite> items;
        public static class ite{
            public String Title;
            public String Phone;
            public String Address;
            public String Special;
        }

    }
}
