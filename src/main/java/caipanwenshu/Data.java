package caipanwenshu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Data {
    public static Connection conn;
    public static Connection connhive;
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

        String driver2="org.apache.hive.jdbc.HiveDriver";
        String url2="jdbc:hive2://10.64.14.69:10000/dw_online?useSSL=false&autoReconnect=true";
        String username2="etl";
        String password2="innotree_etl";
        try {
            Class.forName(driver2).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con2=null;
        try {
            con2 = DriverManager.getConnection(url2, username2, password2);
        }catch (Exception e){
            while(true){
                try {
                    con2 = DriverManager.getConnection(url2, username2, password2);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con2!=null){
                    break;
                }
            }
        }
        conn=con;
        connhive=con2;
    }
    public static void getCon(){
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

    public static void getConhive(){
        String driver2="org.apache.hive.jdbc.HiveDriver";
        String url2="jdbc:hive2://10.64.14.69:10000/dw_online?useSSL=false&autoReconnect=true";
        String username2="etl";
        String password2="innotree_etl";
        try {
            Class.forName(driver2).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con2=null;
        try {
            con2 = DriverManager.getConnection(url2, username2, password2);
        }catch (Exception e){
            while(true){
                try {
                    con2 = DriverManager.getConnection(url2, username2, password2);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con2!=null){
                    break;
                }
            }
        }
        connhive=con2;
    }

    public static void heart(){
        while (true){
            try{
                String sql="select 1";
                PreparedStatement ps=conn.prepareStatement(sql);
                ps.executeQuery();
                Thread.sleep(10000);
            }catch (Exception e){
                getCon();
            }
        }
    }

    public static void hearthive(){
        while (true){
            try{
                String sql="select 1";
                PreparedStatement ps=connhive.prepareStatement(sql);
                ps.executeQuery();
                Thread.sleep(10000);
            }catch (Exception e){
                getCon();
            }
        }
    }
}
