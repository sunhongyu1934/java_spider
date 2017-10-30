package etl.itjz_etl;

import etl.itjz_etl.bean.finacbean;
import etl.itjz_etl.bean.insert_financingbean;
import etl.itjz_etl.bean.insert_projectbean;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/24.
 */
public class zl_sc {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ParseException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/finacing?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
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


        String url2="jdbc:mysql://101.200.161.221:3306/innotree_data_panshi?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        Class.forName(driver1).newInstance();
        java.sql.Connection con2=null;
        String username2="tech_spider";
        String password2="sPiDer$#@!23";
        try {
            con2 = DriverManager.getConnection(url2, username2, password2);
        }catch (Exception e){
            while(true){
                con2 = DriverManager.getConnection(url2, username2, password2);
                if(con2!=null){
                    break;
                }
            }
        }



        data(con,con2);

    }

    public static void data(Connection con,Connection con2) throws SQLException, ParseException {
        String select="select * from it_company_pc where moditme>date_sub(now(),interval '1' hour)";
        PreparedStatement pss=con.prepareStatement(select);

        String insert_company="insert into company(sName,sCreateDate,sAddress,sUrl,sLogoUrl,sIntroduction,sTag,sTeamSize,sFromUrl) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psi=con2.prepareStatement(insert_company);

        String update_company="update company set fid=? where id=?";
        PreparedStatement psuc=con2.prepareStatement(update_company);

        ResultSet rss=pss.executeQuery();
        while (rss.next()){
            String cid=rss.getString(rss.findColumn("cid"));
            String cnamez=rss.getString(rss.findColumn("company_full_name"));
            String name=rss.getString(rss.findColumn("name")).split("/",2)[0].split("\\(",2)[0];
            if(cnamez.contains("暂未收录")){
                cnamez=name;
            }
            List<finacbean> finacbeanList=getfin(con,cid);
            String flag=duibi(con2,cnamez);


            if(StringUtils.isEmpty(flag)){
                psi.setString(1,cnamez);
                psi.setString(2,rss.getString(rss.findColumn("found_time")));
                psi.setString(3,rss.getString(rss.findColumn("address")));
                psi.setString(4,rss.getString(rss.findColumn("url")));
                psi.setString(5,rss.getString(rss.findColumn("logo")));
                psi.setString(6,rss.getString(rss.findColumn("introduction")));
                psi.setString(7,rss.getString(rss.findColumn("tags")).substring(0, rss.getString(rss.findColumn("tags")).length() - 1));
                psi.setString(8,rss.getString(rss.findColumn("scale")).replace("公司规模：", "").replace("暂未收录", ""));
                psi.setString(9,"http://www.itjuzi.com/company/"+cid);
                psi.executeUpdate();

                ResultSet rsid = psi.getGeneratedKeys();
                String id="";
                if(rsid.next())
                {
                    id = String.valueOf(rsid.getInt(1));
                }





                String statr=rss.getString(rss.findColumn("status"));
                String statu="";
                if(statr.contains("运营")){
                   statu="0";
                }else if(statr.contains("关闭")){
                    statu="10";
                }else{
                    statu=null;
                }

                insert_projectbean insertProjectbean=new insert_projectbean();
                insertProjectbean.setsName(name);
                insertProjectbean.setCid(id);
                insertProjectbean.setsAddress(rss.getString(rss.findColumn("address")));
                insertProjectbean.setsTag(rss.getString(rss.findColumn("tags")).substring(0,rss.getString(rss.findColumn("tags")).length()-1));
                insertProjectbean.setiOpreateState(statu);
                insertProjectbean.setsLogoUrl(rss.getString(rss.findColumn("logo")));
                insertProjectbean.setsFromUrl("http://www.itjuzi.com/company/"+cid);
                String pid=insertProject(con2,insertProjectbean);


                Map<Long, String> map=new HashMap<Long, String>();
                for(finacbean f:finacbeanList){
                    String vc=f.getVc();
                    if(vc.substring(vc.length()-1,vc.length()).equals(";")){
                        vc=vc.substring(0,vc.length()-1);
                    }

                    insert_financingbean insertFinancingbean=new insert_financingbean();
                    insertFinancingbean.setCid(id);
                    insertFinancingbean.setsDate(f.getTime());
                    insertFinancingbean.setsName(name);
                    insertFinancingbean.setsAmount(f.getMoney());
                    insertFinancingbean.setsStage(f.getRound());
                    insertFinancingbean.setsInstitution(vc);
                    insertFinancingbean.setsFromUrl("http://www.itjuzi.com/company/"+cid);
                    insertFinancingbean.setPid(pid);
                    String fid=insertFinancing(con2,insertFinancingbean);


                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.M.d");
                    java.util.Date date=simpleDateFormat.parse(f.getTime());
                    long time=date.getTime();
                    map.put(time,fid);
                }
                List<Long> ll=new ArrayList<Long>();
                for(Map.Entry<Long,String> entry:map.entrySet()){
                    ll.add(entry.getKey());
                }
                long max=Collections.max(ll);
                String fids=map.get(max);

                psuc.setString(1,fids);
                psuc.setString(2,id);
                psuc.executeUpdate();

            }else{
                String select_company="select id from company where sName='"+flag+"'";
                PreparedStatement pss_company=con2.prepareStatement(select_company);
                ResultSet rss_company=pss_company.executeQuery();
                String id="";
                while (rss_company.next()){
                    id=rss_company.getString(rss_company.findColumn("id"));
                }

                String statr=rss.getString(rss.findColumn("status"));
                String statu="";
                if(statr.contains("运营")){
                    statu="0";
                }else if(statr.contains("关闭")){
                    statu="10";
                }else{
                    statu=null;
                }
                insert_projectbean insertProjectbean=new insert_projectbean();
                insertProjectbean.setsName(name);
                insertProjectbean.setCid(id);
                insertProjectbean.setsAddress(rss.getString(rss.findColumn("address")));
                insertProjectbean.setsTag(rss.getString(rss.findColumn("tags")).substring(0,rss.getString(rss.findColumn("tags")).length()-1));
                insertProjectbean.setiOpreateState(statu);
                insertProjectbean.setsLogoUrl(rss.getString(rss.findColumn("logo")));
                insertProjectbean.setsFromUrl("http://www.itjuzi.com/company/"+cid);

                for(finacbean f:finacbeanList) {
                    boolean fin=true;
                    String pid="";

                    String vc=f.getVc();
                    if(vc.substring(vc.length()-1,vc.length()).equals(";")){
                        vc=vc.substring(0,vc.length()-1);
                    }
                    insert_financingbean insertFinancingbean=new insert_financingbean();
                    insertFinancingbean.setCid(id);
                    insertFinancingbean.setsDate(f.getTime());
                    insertFinancingbean.setsName(name);
                    insertFinancingbean.setsAmount(f.getMoney());
                    insertFinancingbean.setsStage(f.getRound());
                    insertFinancingbean.setsInstitution(vc);
                    insertFinancingbean.setsFromUrl("http://www.itjuzi.com/company/" + cid);

                    String select_financing="select * from financing where cid='"+id+"'";
                    PreparedStatement pss_financing=con2.prepareStatement(select_financing);
                    ResultSet rss_financing=pss_financing.executeQuery();
                    while (rss_financing.next()) {
                        String tsDate = rss_financing.getString(rss_financing.findColumn("tsDate"));
                        String istage = rss_financing.getString(rss_financing.findColumn("sStage"));
                        String sAmount = rss_financing.getString(rss_financing.findColumn("sAmount"));
                        String sName = rss_financing.getString(rss_financing.findColumn("sName"));
                        pid=rss_financing.getString(rss_financing.findColumn("pid"));

                        String sDate=rss_financing.getString(rss_financing.findColumn("sDate"));

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd");
                        Date date=simpleDateFormat.parse(f.getTime());
                        long time1=date.getTime();
                        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date1=simpleDateFormat1.parse(tsDate);
                        long time2=date1.getTime();
                        if((sName.contains(f.getName().split("/",2)[0].split("\\(",2)[0])||f.getName().split("/",2)[0].split("\\(",2)[0].contains(sName))&&flageq(sAmount,f.getMoney())&&flageq(istage,f.getRound())&&Math.abs((time1-time2)/(24*60*60*1000))<=30){
                            fin=false;
                        }

                    }
                    if(fin&&StringUtils.isNotEmpty(pid)){
                        insertFinancingbean.setPid(pid);
                        String fid=insertFinancing(con2, insertFinancingbean);

                        psuc.setString(1,fid);
                        psuc.setString(2,id);
                        psuc.executeUpdate();
                    }
                    if(fin&&StringUtils.isEmpty(pid)){
                        boolean pro=true;

                        String select_project="select * from project where cid='"+id+"'";
                        PreparedStatement pss_project=con2.prepareStatement(select_project);
                        ResultSet rss_project=pss_project.executeQuery();
                        while (rss_project.next()){
                            String sName=rss_project.getString(rss_project.findColumn("sName"));
                            if(sName.contains(f.getName().split("/",2)[0].split("\\(",2)[0])||f.getName().split("/",2)[0].split("\\(",2)[0].contains(sName)){
                                pid=rss_project.getString(rss_project.findColumn("id"));
                                pro=false;
                            }
                        }
                        if(!pro&&StringUtils.isNotEmpty(pid)){
                            insertFinancingbean.setPid(pid);
                            String fid=insertFinancing(con2, insertFinancingbean);

                            psuc.setString(1,fid);
                            psuc.setString(2,id);
                            psuc.executeUpdate();
                        }
                        if(pro&&StringUtils.isEmpty(pid)){
                            pid=insertProject(con2,insertProjectbean);
                            insertFinancingbean.setPid(pid);

                            String fid=insertFinancing(con2, insertFinancingbean);

                            psuc.setString(1,fid);
                            psuc.setString(2,id);
                            psuc.executeUpdate();
                        }
                    }
                }
            }
        }
    }


    public static String insertProject(Connection con2,insert_projectbean in) throws SQLException {
        String insert_project="insert into project(sName,cid,sAddress,sTag,iOpreateState,sLogoUrl,sFromUrl) values(?,?,?,?,?,?,?)";
        PreparedStatement psi2=con2.prepareStatement(insert_project);

        psi2.setString(1,in.getsName());
        psi2.setString(2,in.getCid());
        psi2.setString(3,in.getsAddress());
        psi2.setString(4,in.getsTag());
        psi2.setString(5,in.getiOpreateState());
        psi2.setString(6,in.getsLogoUrl());
        psi2.setString(7,in.getsFromUrl());
        psi2.executeUpdate();

        ResultSet rspid = psi2.getGeneratedKeys();
        String pid="";
        if(rspid.next())
        {
            pid = String.valueOf(rspid.getInt(1));
        }
        return pid;
    }

    public static String insertFinancing(Connection con2,insert_financingbean in) throws SQLException {
        String insert_finacing="insert into financing(cid,sDate,sName,sAmount,sStage,sInstitution,sFromUrl,pid) values(?,?,?,?,?,?,?,?)";
        PreparedStatement psi3=con2.prepareStatement(insert_finacing);

        psi3.setString(1,in.getCid());
        psi3.setString(2,in.getsDate());
        psi3.setString(3,in.getsName());
        psi3.setString(4,in.getsAmount());
        psi3.setString(5,in.getsStage());
        psi3.setString(6,in.getsInstitution());
        psi3.setString(7,in.getsFromUrl());
        psi3.setString(8,in.getPid());
        psi3.executeUpdate();


        ResultSet rsfid = psi3.getGeneratedKeys();
        String fid="";
        if(rsfid.next())
        {
            fid = String.valueOf(rsfid.getInt(1));
        }
        return fid;
    }



    public static boolean flageq(String key,String value){
        if(StringUtils.isEmpty(key)||StringUtils.isEmpty(value)){
            return false;
        }
        boolean flag=true;
        if(key.equals(value)){
            return flag;
        }else{
            flag=false;
            return flag;
        }
    }


    public static List<finacbean> getfin(Connection con,String cid) throws SQLException {
        String select2="select * from it_finacing_pc where cid='"+cid+"' order by time";
        PreparedStatement pss2=con.prepareStatement(select2);
        ResultSet rss2=pss2.executeQuery();
        List<finacbean> list=new ArrayList<finacbean>();
        while (rss2.next()){
            finacbean fin=new finacbean();
            fin.setFid(rss2.getString(rss2.findColumn("fid")));
            fin.setMoney(rss2.getString(rss2.findColumn("money")));
            fin.setName(rss2.getString(rss2.findColumn("name")));
            fin.setRound(rss2.getString(rss2.findColumn("round")));
            fin.setTime(rss2.getString(rss2.findColumn("time")));
            fin.setVc(rss2.getString(rss2.findColumn("vc")));
            list.add(fin);
        }
        return list;
    }



    public static String duibi(Connection con2,String cname) throws SQLException {
        String flag="";
        List<String> list=getcompany(con2);
        for(int x=0;x<list.size();x++){
            String cname1=list.get(x);
            if(cname.equals(cname1)){
                flag=cname;
                return flag;
            }
        }
        return flag;
    }




    public static List<String> getcompany(Connection con2) throws SQLException {
        String sql="select sName from company";
        PreparedStatement ps=con2.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String> list=new ArrayList<String>();
        while (rs.next()){
            list.add(rs.getString(rs.findColumn("sName")));
        }
        return list;
    }


}
