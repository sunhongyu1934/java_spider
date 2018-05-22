package tianyancha.XinxiXin;

import Utils.RedisClu;
import baidu.RedisAction;
import org.apache.commons.lang.StringUtils;

import java.sql.*;

/**
 * Created by Administrator on 2017/8/21.
 */
public class send_redis {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String sql=args[0];
        String field=args[1];
        String key=args[2];
        String li=args[3];
        RedisClu rs=new RedisClu();
        duqu10(con, rs,sql,field,key,li);
    }
    public static void duqu(Connection con,RedisAction r) throws SQLException, InterruptedException {
        String sql="select ";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("invest_name"));
            String tid=rs.getString(rs.findColumn("invest_tid"));
            r.set("tyc_linshi",tid+"*****"+cname);
            a++;
            System.out.println(a+"*********************************************");
        }
    }

    public static void duqu2(Connection con,RedisAction r) throws SQLException, InterruptedException {
        String sql="select DISTINCT invest_name,invest_tid from tyc_out_investment where t_id in (select DISTINCT invest_tid from wuji_duiwai where round='2') and invest_name!='' and invest_name is not null";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("invest_name")).trim();
            String tid=rs.getString(rs.findColumn("invest_tid")).trim();
            r.set("tyc_linshi",tid+"*****"+cname);
            a++;
            System.out.println(a+"*********************************************");
        }
    }

    public static void data3(RedisAction r) throws InterruptedException {
        for(int x=65000;x<=100000;x++){
            r.set("itjuzi", String.valueOf(x));
            System.out.println(x+"****************************************");
        }
    }

    public static void duqu3(Connection con, RedisAction r) throws SQLException {
        String sql="select distinct investors,investors_tid from tyc_investment_event";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String[] mings=rs.getString(rs.findColumn("investors")).split(";");
            String[] tids=rs.getString(rs.findColumn("investors_tid")).split(";");
            for(int x=0;x<mings.length;x++){
                if(StringUtils.isNotEmpty(mings[x])&&!mings[x].equals("kong")&&!tids[x].equals("0")&&StringUtils.isNotEmpty(tids[x])){
                    r.set("tyc_touzi",tids[x]+"*****"+mings[x]);
                    p++;
                    System.out.println(p+"**************************************************************");
                }
            }
        }
    }

    public static void duqu4(Connection con,RedisAction r) throws SQLException {
        String sql="select jian_c,t_id from linshi where quan_c='' or quan_c is null";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String cn=rs.getString(rs.findColumn("jian_c"));
            String ti=rs.getString(rs.findColumn("t_id"));
            r.set("tyc_touzi",ti+"*****"+cn);
            p++;
            System.out.println(p+"**********************************");
        }
    }

    public static void duqu5(Connection con,RedisAction r) throws SQLException {
        String sql="select t_id from linshi";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String cn="aaa";
            String ti=rs.getString(rs.findColumn("t_id"));
            r.set("tyc_buchong",ti+"*****"+cn);
            p++;
            System.out.println(p+"**********************************");
        }
    }

    public static void duqu6(Connection con,RedisAction r) throws SQLException {
        String sql="select distinct quan_cheng from spider.tyc_jichu_chuisou";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int p=0;
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("quan_cheng"));
            r.set("tyc_logo",cname);
            p++;
            System.out.println(p+"****************************************");
        }
    }

    public static void duqu7(Connection con,RedisAction r) throws SQLException {
        int sum=0;
        int p = 0;
        for(int a=1;a<=18;a++) {
            String sql = "select distinct only_id from dimension_sum.com_dictionaries where register_or=0 limit "+sum+",500000";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("only_id"));
                r.set("zhuce", cname);
                p++;
                System.out.println(p + "****************************************");
            }
            sum=sum+500000;
        }
    }

    public static void duqu8(Connection con,RedisAction r) throws SQLException {
        int sum=0;
        int p = 0;
        for(int a=1;a<=15;a++) {
            String sql = "select distinct t_id,quan_cheng from tianyancha.tyc_jichu_quan limit "+sum+",500000";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("quan_cheng"));
                String t_id=rs.getString(rs.findColumn("t_id"));
                r.setstr(t_id, cname);
                p++;
                System.out.println(p + "****************************************");
            }
            sum=sum+500000;
        }
    }

    public static void duqu9(Connection con,RedisAction r) throws SQLException {
        int sum=0;
        int p = 0;
        for(int a=1;a<=10;a++) {
            String sql = "select distinct quan_cheng from tyc.tyc_jichu_quan1 limit "+sum+",500000";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("quan_cheng"));
                r.set("com_name", cname);
                p++;
                System.out.println(p + "****************************************");
            }
            sum=sum+500000;
        }
    }


    public static void duqu10(Connection con,RedisClu r,String sql,String field,String key,String li) throws SQLException {
        int sum=0;
        int p = 0;
        for(int a=1;a<=10;a++) {
            PreparedStatement ps = con.prepareStatement(sql+" limit "+sum+","+li);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    String cname = rs.getString(rs.findColumn(field));
                    r.set(key, cname);
                    p++;
                    System.out.println(p + "****************************************");
                }catch (Exception e){
                    System.out.println("error");
                }
            }
            sum=sum+Integer.parseInt(li);
        }
    }

    public static void da(Connection con,RedisAction rd) throws SQLException {
        String sql="select comp_id from company_base_info";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String cid=rs.getString(rs.findColumn("comp_id"));
            rd.set("xianshang",cid);
            a++;
            System.out.println(a+"***************************************");
        }
    }
}