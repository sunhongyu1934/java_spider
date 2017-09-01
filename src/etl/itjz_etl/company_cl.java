package etl.itjz_etl;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
public class company_cl {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.166.12:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        storedata(con);



    }

    public static void storedata(Connection con) throws SQLException {
        String insert_company="insert into company_intermediate(sName,sCreateDate,sAddress,sUrl,sLogoUrl,sIntroduction,sTag,sTeamSize,sFromUrl) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psi=con.prepareStatement(insert_company);


        String select="select * from it_company_pc where moditme>date_sub(now(),interval '1' hour)";
        PreparedStatement pss=con.prepareStatement(select);
        ResultSet rss=pss.executeQuery();
        while (rss.next()){
            String cid=rss.getString(rss.findColumn("c_id"));
            String cnamez=rss.getString(rss.findColumn("company_full_name"));
            String name=rss.getString(rss.findColumn("sName")).split("/",2)[0].split("\\(",2)[0];
            if(cnamez.contains("暂未收录")){
                cnamez=name;
            }
            String flag=duibi(con,cnamez);
            if(StringUtils.isEmpty(flag)) {
                psi.setString(1, cnamez);
                psi.setString(2, rss.getString(rss.findColumn("found_time")));
                psi.setString(3, rss.getString(rss.findColumn("company_address")));
                psi.setString(4, rss.getString(rss.findColumn("web_url")));
                psi.setString(5, rss.getString(rss.findColumn("company_logo")));
                psi.setString(6, rss.getString(rss.findColumn("company_introduction")));
                psi.setString(7, rss.getString(rss.findColumn("company_tags")).substring(0, rss.getString(rss.findColumn("company_tags")).length() - 1));
                psi.setString(8, rss.getString(rss.findColumn("company_scale")).replace("公司规模：", "").replace("暂未收录", ""));
                psi.setString(9, "http://www.itjuzi.com/company/" + cid);
                psi.executeUpdate();
            }
        }
    }

    public static String duibi(Connection con,String cname) throws SQLException {
        String flag="";
        List<String> list=getcompany(con);
        for(int x=0;x<list.size();x++){
            String cname1=list.get(x);
            if(cname.equals(cname1)){
                flag=cname;
                return flag;
            }
        }
        return flag;
    }

    public static List<String> getcompany(Connection con) throws SQLException {
        String sql="select sName from company_intermediate";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String> list=new ArrayList<String>();
        while (rs.next()){
            list.add(rs.getString(rs.findColumn("sName")));
        }
        return list;
    }

}
