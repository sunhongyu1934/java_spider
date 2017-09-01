package redis;

import tianyancha.XinxiXin.TYCProducer;

import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/7/3.
 */
public class chuli {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, UnsupportedEncodingException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://100.115.97.86:3306/dc_key?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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
        TYCProducer ty=new TYCProducer("bs_tyc_search","10.44.158.42:9092,10.44.137.192:9092,10.44.143.200:9092,10.44.155.195:9092");
        int a=0;
        /*String sql="select t_id from tyc_information";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        while (rs.next()){
            String tid=rs.getString(rs.findColumn("t_id"));
            redisAction.setAllInfoCompanyId(tid,"name");
            a++;
            System.out.println(a+"**********************************************");
        }

        String sql2="select t_id from tyc_information2";
        PreparedStatement ps2=con.prepareStatement(sql2);
        ResultSet rs2=ps2.executeQuery();
        while (rs2.next()){
            String tid=rs2.getString(rs2.findColumn("t_id"));
            redisAction.setAllInfoCompanyId(tid,"name");
            a++;
            System.out.println(a+"*********************************************************");
        }

        String sql3="select t_id from tyc_information3";
        PreparedStatement ps3=con.prepareStatement(sql3);
        ResultSet rs3=ps3.executeQuery();
        while (rs3.next()){
            String tid=rs3.getString(rs3.findColumn("t_id"));
            redisAction.setAllInfoCompanyId(tid,"name");
            a++;
            System.out.println(a+"*******************************************************************************");
        }*/
        /*RedisAction redisAction = new RedisAction("a026.hb2.innotree.org", 6379);
        int q=0;
        for(int x=1;x<=330;x++) {
            String sql4 = "select tyc_id,company_name from key_company_qygx limit "+q+",10000";
            PreparedStatement ps4 = con.prepareStatement(sql4);
            ResultSet rs4 = ps4.executeQuery();
            while (rs4.next()) {
                String tid = rs4.getString(rs4.findColumn("tyc_id"));
                String cname = rs4.getString(rs4.findColumn("company_name"));
                redisAction.setQYGXCompanyId(tid, cname);
                a++;
                System.out.println(a + "********************************************************************************************************");
            }
            q=q+10000;
        }*/

        int p=0;
        for(int x=1;x<=15;x++) {
            String sql5 = "select `cname` from key_company limit "+p+",100000";
            PreparedStatement ps5 = con.prepareStatement(sql5);
            System.out.println("begin read");
            ResultSet rs5 = ps5.executeQuery();
            while (rs5.next()) {
                String tid = rs5.getString(rs5.findColumn("cname"));
                ty.send(tid);
                a++;
                System.out.println(a + "------------------------------------------------------------------------------");
            }
            p=p+100000;
        }
       // ty.close();
    }
}
