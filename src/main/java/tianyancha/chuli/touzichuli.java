package tianyancha.chuli;

import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/26.
 */
public class touzichuli {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        duqu(con);

    }

    public static void duqu(Connection con) throws SQLException {
        String sql="select distinct investors,investors_tid from tyc_investment_event";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        Set<String> set=new HashSet<String>();

        String sql2="insert into linshi(jian_c,t_id) values(?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);
        int p=0;
        while (rs.next()){
            String[] mings=rs.getString(rs.findColumn("investors")).split(";");
            String[] tids=rs.getString(rs.findColumn("investors_tid")).split(";");
            for(int x=0;x<mings.length;x++){
                if(StringUtils.isNotEmpty(mings[x])&&!mings[x].equals("kong")&&!tids[x].equals("0")&&StringUtils.isNotEmpty(tids[x])){
                    set.add(mings[x]+"#####"+tids[x]);
                }
            }
        }

        for(String s:set){
            ps2.setString(1,s.split("#####",2)[0]);
            ps2.setString(2,s.split("#####",2)[1]);
            ps2.executeUpdate();
        }
    }
}
