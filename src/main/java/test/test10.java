package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/27.
 */
public class test10 {
    public static void main(String args[]) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
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

        int a=1;
        String select="select assignee,id from com_en_cn";
        PreparedStatement ps=con.prepareStatement(select);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            String assignee=rs.getString(rs.findColumn("assignee"));
            String id=rs.getString(rs.findColumn("id"));
            get(assignee,id);
            System.out.println(a);
            System.out.println("-------------------------------------------");
            a++;
        }
    }

    public static void get(String so,String id) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
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


        String insert="insert into com_em_cn_title(`title`,`bid`) values(?,?)";
        PreparedStatement ps=con.prepareStatement(insert);



        String a= URLEncoder.encode(so,"UTF-8");
        Document doc= Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd="+a+"%91&rsv_pq=9f10cddb0001e646&rsv_t=5beftbnx1Zd7ngn2gPgoCkAq9RroF4KKr%2FWksqV%2BlEdV1CM1mmhloz%2F81bU&rqlang=cn&rsv_enter=1&rsv_sug3=3&rsv_sug1=1&rsv_sug7=100&rsv_sug2=0&inputT=1423&rsv_sug4=2006")
                .header("ie","utf-8")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .header("Referer","https://www.baidu.com/")
                .timeout(100000)
                .get();

        Elements ele=doc.select("div.result.c-container ");
        for(Element e:ele){
            String title=e.select("h3 a").get(0).text();
            ps.setString(1,title);
            ps.setString(2,id);
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
