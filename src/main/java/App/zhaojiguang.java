package App;

import Utils.Dup;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class zhaojiguang {
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

        conn=con;

    }
    public static void main(String args[]) throws IOException, SQLException {
        get();
    }

    public static void get() throws IOException, SQLException {
        String sql="insert into ygqf(r_ank,app_name,a_hy,a_kaifa,a_hr,a_hb) values(?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Gson gson=new Gson();
        Document doc= Jsoup.parse(new File("C:\\Users\\13434\\Desktop\\a.txt"),"utf-8");
        String json= Dup.qujson(doc);
        de dd=gson.fromJson(json,de.class);
        int a=0;
        for(de.Data.Dd d:dd.datas.topRankList){
            ps.setString(1,d.rank);
            ps.setString(2,d.appName);
            ps.setString(3,d.cateNames);
            ps.setString(4,d.developCompanyFullName);
            ps.setString(5,d.activeNums);
            ps.setString(6,d.activeNumsRatio);
            ps.executeUpdate();
            a++;
            System.out.println(a+"***************************************************");
        }
    }


    public static class de{
        public Data datas;
        public static class Data{
            public List<Dd> topRankList;
            public static class Dd{
                public String rank;
                public String appName;
                public String cateNames;
                public String developCompanyFullName;
                public String activeNums;
                public String activeNumsRatio;
            }
        }
    }
}
