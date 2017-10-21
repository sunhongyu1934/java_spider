package tianyancha.XinxiXin;

import java.io.UnsupportedEncodingException;
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

public class tyc_xiaofei {
    private static Connection conn;
    static{
        String driver1="com.mysql.cj.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/clean_data?useUnicode=true&characterEncoding=utf-8";
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

    public static void main( String[] args ) throws InterruptedException, UnsupportedEncodingException {
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int x=1;x<=10;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        xiaofei();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void xiaofei() throws UnsupportedEncodingException, SQLException {
        TYCConsumer tyc=new TYCConsumer("tyc_shangxian","web","10.44.51.90:12181,10.44.152.49:12181,10.51.82.74:12181");
        TYCProducer ty=new TYCProducer("tyc_zl","10.44.51.90:19092,10.44.152.49:19092,10.51.82.74:19092");
        int p=0;
        String sql="insert into linshi(c_name) values(?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            String a=tyc.getmessage();
            ps.setString(1,a);
            ps.executeUpdate();
            p++;
            System.out.println(p+"***************************************");
        }
    }
}
