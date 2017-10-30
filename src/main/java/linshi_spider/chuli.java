package linshi_spider;

import java.sql.*;

/**
 * Created by Administrator on 2017/6/17.
 */
public class chuli {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        du(con);


    }

    public static void du(Connection con) throws SQLException {
        String sql="select id,web from linshi_web";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String sql2="update linshi_web set web=? where id=?";
        PreparedStatement ps2=con.prepareStatement(sql2);

        int a=0;
        while (rs.next()){
            try {
                String web = rs.getString(rs.findColumn("web"));
                String id = rs.getString(rs.findColumn("id"));
                String webs = web.replaceAll("[\\u4e00-\\u9fa5]", "");

                ps2.setString(1, webs);
                ps2.setString(2, id);
                ps2.executeUpdate();
                a++;
                System.out.println(a+"********************");
            }catch (Exception e){

            }
        }

    }





}
