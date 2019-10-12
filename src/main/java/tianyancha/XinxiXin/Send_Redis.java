package tianyancha.XinxiXin;

import Utils.RedisClu;

import java.sql.*;

public class Send_Redis {
    public static Connection conn;
    public static int b=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/innotree_data_financing?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="base";
        String password="imkloKuLiqNMc6Cn";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con=null;
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
        data2();
    }


    public static void data1() throws SQLException {
        String sql="select comp_full_name from comp_name_bulu where mark_time is null or mark_time=''";
        PreparedStatement ps=conn.prepareStatement(sql);
        RedisClu rd=new RedisClu();
        ResultSet rs=ps.executeQuery();

        String sql2="update comp_name_bulu set mark_time='1' where comp_full_name=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        while (rs.next()){
            String compname=rs.getString(rs.findColumn("comp_full_name"));
            rd.set("comp_zl",compname);

            ps2.setString(1,compname);
            ps2.executeUpdate();
            b++;
            System.out.println(b+"******************************************************");
        }
    }

    public static boolean data2() throws SQLException {
        boolean bo=true;
        String sql="select comp_full_name from comp_name_bulu_first where mark_time is null or mark_time=''";
        PreparedStatement ps=conn.prepareStatement(sql);
        RedisClu rd=new RedisClu();
        ResultSet rs=ps.executeQuery();

        String sql2="update comp_name_bulu_first set mark_time='1' where comp_full_name=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        while (rs.next()){
            bo=false;
            String compname=rs.getString(rs.findColumn("comp_full_name"));
            rd.set("comp_zl_first",compname);

            ps2.setString(1,compname);
            ps2.executeUpdate();
            b++;
            System.out.println(b+"******************************************************");
        }
        return bo;
    }
}
