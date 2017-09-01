package baidu;

import java.sql.*;

/**
 * Created by Administrator on 2017/7/18.
 */
public class send {
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

        RedisAction redis=new RedisAction("127.0.0.1",6379);
        se(con,redis);
    }

    public static void se(Connection con,RedisAction redis) throws SQLException {
        int a=0;
        String sql="select id,c_shortname from it_leida_company where c_shortname!='' and c_shortname is not null";
        String sql2 = "select ncid,shortname from comp_baseinfo where shortname!='' and shortname is not null";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String kid=rs.getString(rs.findColumn("id"));
            String kname=rs.getString(rs.findColumn("c_shortname"));
            redis.set("baidu_news",kid+"#####"+kname+"#####"+"1");
            a++;
            System.out.println(a+"******************************************");
        }

        ps = con.prepareStatement(sql2);
        rs = ps.executeQuery();
        while (rs.next()) {
            String kid = rs.getString(rs.findColumn("ncid"));
            String kname = rs.getString(rs.findColumn("shortname"));
            redis.set("baidu_news", kid + "#####" + kname+"#####"+"2");
            a++;
            System.out.println(a+"******************************************");
        }
    }

}
