package tianyancha.XinxiXin;

import java.sql.*;

/**
 * Created by Administrator on 2017/9/6.
 */
public class send_kafka {
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
    public static void main(String args[]) throws SQLException {
        data();
    }

    public static void data() throws SQLException {
        TYCProducer ty=new TYCProducer("tyc_linshi","10.44.51.90:19092,10.44.152.49:19092,10.51.82.74:19092");
        int a=0;
        int jishu=0;
        for(int x=1;x<=30;x++) {
            String sql = "select c_name from linshi_company limit "+x+",1000000";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String name=rs.getString(rs.findColumn("c_name"));
                ty.send(name);
                jishu++;
                System.out.println(jishu+"*****************************************************");
            }
            a=a+1000000;
        }
    }

}
