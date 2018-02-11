package tianyancha.chuli;

import Utils.Dup;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;

public class shuzi {
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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
        String sql="select id,quan_cheng,zhuce_ziben,zhuce_shijian,hezhun_riqi from tyc_jichu_quan where id>17272000";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String sql2="update tyc_jichu_quan set zhuce_ziben=?,zhuce_shijian=?,hezhun_riqi=? where id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        int a=0;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String zhuzi=rs.getString(rs.findColumn("zhuce_ziben"));
            String zhushi=rs.getString(rs.findColumn("zhuce_shijian"));
            String heri=rs.getString(rs.findColumn("hezhun_riqi"));
            String quan=rs.getString(rs.findColumn("quan_cheng"));



            if((zhushi!=null&&zhushi.contains("."))||(heri!=null&&heri.contains("."))||(zhushi!=null&&zhushi.substring(0,1).equals("9"))||(heri!=null&&heri.substring(0,1).equals("9"))||(zhushi!=null&&zhushi.substring(0,3).equals("277"))||(heri!=null&&heri.substring(0,3).equals("277"))){
                zhuzi=zi(zhuzi);
                zhushi=zi(zhushi);
                heri=zi(heri);

                ps2.setString(1,zhuzi);
                ps2.setString(2,zhushi);
                ps2.setString(3,heri);
                ps2.setInt(4, Integer.parseInt(id));
                ps2.executeUpdate();
                a++;
                System.out.println(a+"*****************************************************************");
            }
        }
    }

    public static String zi(String key){
        if(Dup.nullor(key)){
            String keys[]=key.split("|");
            StringBuffer str=new StringBuffer();
            for(String s:keys){
                if(s.equals("1")){
                    str.append(s.replace("1","0"));
                }else if(s.equals("2")){
                    str.append(s.replace("2","1"));
                }else if(s.equals("9")){
                    str.append(s.replace("9","2"));
                }else if(s.equals("0")){
                    str.append(s.replace("0","3"));
                }else if(s.equals("8")){
                    str.append(s.replace("8","7"));
                }else if(s.equals(".")){
                    str.append(s.replace(".","5"));
                }else if(s.equals("7")){
                    str.append(s.replace("7","9"));
                }else if(s.equals("6")){
                    str.append(s.replace("6","4"));
                }else if(s.equals("3")){
                    str.append(s.replace("3","8"));
                }else if(s.equals("4")){
                    str.append(s.replace("4","6"));
                }else if(s.equals("5")){
                    str.append(s.replace("5","."));
                }else{
                    str.append(s);
                }
            }

            return str.toString();
        }else {
            return null;
        }
    }
}
