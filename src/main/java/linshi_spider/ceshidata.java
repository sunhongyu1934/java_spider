package linshi_spider;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ceshidata {
    private static Connection conn;

    private static int j=0;
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

    public static void main(String args[]){
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int a=1;a<=50;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data() throws SQLException {
        String sql="insert into ceshi(id) values(?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        for(int a=1;a<=1000;a++) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            j++;
            System.out.println(j+"**********************************************");
        }
    }
}
