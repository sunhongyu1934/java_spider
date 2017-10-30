package itjuzi;

import java.sql.*;

/**
 * Created by Administrator on 2017/4/19.
 */
public class chuli {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.51.120.107:3306/finacing_copy?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        data(con);

    }

    public static void data(Connection con) throws SQLException {
        String select="select `name`,id from it_finacing";
        PreparedStatement ps1=con.prepareStatement(select);
        ResultSet rs=ps1.executeQuery();

        String update="update it_finacing set `name`=? where id=?";
        PreparedStatement ps2=con.prepareStatement(update);
        int a=1;
        while(rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String name=rs.getString(rs.findColumn("name"));
            if(name.contains("/")){
                String n=name.split("/",2)[0];
                ps2.setString(1,n);
                ps2.setString(2,id);
                ps2.executeUpdate();
            }
            System.out.println(a);
            a++;
            System.out.println("---------------------------------");
        }

        String selectc="select `name`,id from it_company";
        PreparedStatement ps1c=con.prepareStatement(selectc);
        ResultSet rsc=ps1c.executeQuery();

        String updatec="update it_company set `name`=? where id=?";
        PreparedStatement ps2c=con.prepareStatement(updatec);
        while(rsc.next()){
            String id=rsc.getString(rsc.findColumn("id"));
            String name=rsc.getString(rsc.findColumn("name"));
            if(name.contains("/")){
                String n=name.split("/",2)[0];
                ps2c.setString(1,n);
                ps2c.setString(2,id);
                ps2c.executeUpdate();
            }
            System.out.println(a);
            a++;
            System.out.println("---------------------------------");
        }


    }

}
