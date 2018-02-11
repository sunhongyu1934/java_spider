package simutong.simutong_jigou;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static Utils.JsoupUtils.*;
import static simutong.simutong_jigou.Qingqiu.*;

/**
 * Created by Administrator on 2017/5/27.
 */
public class simutong_jigou {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.44:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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

        return con;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException, ParseException {

        Connection con=getcon();

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));


        String[] zhanghu=new String[]{"wang.hao@lingweispace.cn","111111","wang.hao@lingweispace.cn","111111"};
        String[] pc=new String[]{"9C-B6-D0-E6-8E-89,A4-4C-C8-10-B4-99,9C-B6-D0-E6-8E-8A","9C-B6-D0-E6-8E-89,A4-4C-C8-10-B4-99,9C-B6-D0-E6-8E-8A"};
        Map<String,String> map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);
        sousuo(con, "", map,proxy);
    }


    public static void data(Connection con,Proxy proxy) throws SQLException, IOException, InterruptedException, ParseException {
        Random r=new Random();
        String sql="select sInstitution from spider.si_inst_bulu";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 1800000;
        String[] zhanghu=new String[]{"wang.hao@lingweispace.cn","111111","wang.hao@lingweispace.cn","111111"};
        String[] pc=new String[]{"9C-B6-D0-E6-8E-89,A4-4C-C8-10-B4-99,9C-B6-D0-E6-8E-8A","9C-B6-D0-E6-8E-89,A4-4C-C8-10-B4-99,9C-B6-D0-E6-8E-8A"};
        Map<String,String> map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);
        int flag=0;
        int p=0;
        while (rs.next()){
            try {
                int th = r.nextInt(5001) + 10000;
                String pid = "0";
                String key = rs.getString(rs.findColumn("sInstitution"));
                sousuo(con, pid, map,proxy);
                Thread.sleep(th);
                long t = System.currentTimeMillis();
                if (t > (begin + cur)) {
                    cur=(r.nextInt(50) * 60 * 1000) + 1800000;
                    /*Thread.sleep(cur);
                    if(con!=null) {
                        con.close();
                    }
                    con=getcon();
                    map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);*/
                    if(flag==0) {
                        map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);
                        flag=1;
                    }else{
                        map = denglu(proxy, zhanghu[2], zhanghu[3],pc[1]);
                    }
                    t=System.currentTimeMillis();
                    begin = t;
                }
                java.util.Date date = new java.util.Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time1 = simpleDateFormat.format(date) + " 07:30:00";
                String time2 = simpleDateFormat.format(date) + " 21:30:00";
                long t2 = simpleDateFormat1.parse(time1).getTime();
                long t3 = simpleDateFormat1.parse(time2).getTime();
                if (t>=t3) {
                    Thread.sleep(36000000);
                    if(con!=null) {
                        con.close();
                    }
                    con=getcon();
                    map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);
                    flag=1;
                    t=System.currentTimeMillis();
                    begin = t;
                }
                p++;
                System.out.println(p);
                System.out.println("------------------------------------------");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }

    }


    public static void sousuo(Connection con,String pid,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException, SQLException {
        Random r=new Random();
        String url="http://pe.pedata.cn/categorySearchIndex.action";
        for(int p=85;p<=203;p++) {
            try {
                Document doc = sousuoget(url, map, proxy, String.valueOf(p));
                Elements souele = getElements(doc, "div.all_search_main ul");
                System.out.println("搜索完成，开始请求");
                if (souele != null) {
                    for (Element e : souele) {
                        try {
                            String sid = getHref(e, "li.all_search_main_title a", "href", 0).replace("getDetailOrg.action?param.org_id=", "");
                            if (sid != null && sid.replaceAll("\\s", "").length() > 0) {
                                del(con, sid);
                                jichu(con, sid, pid, map, proxy);
                                Thread.sleep(r.nextInt(5001) + 5000);
                                guanli(con, sid, pid, map, proxy);
                                Thread.sleep(r.nextInt(5001) + 5000);
                                tuichu(con, sid, pid, map, proxy);
                                Thread.sleep(r.nextInt(5001) + 5000);
                                touzi(con, sid, pid, map, proxy);
                                Thread.sleep(r.nextInt(5001) + 5000);
                                jijin(con, sid, pid, map, proxy);
                                Thread.sleep(r.nextInt(5001) + 5000);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }

    }

    public static void del(Connection con,String sid) throws SQLException {
        String[] tables=new String[]{"si_celue","si_guanli","si_jiben","si_jijin","si_lianxi","si_touzi","si_tuichu"};
        for(int x=0;x<tables.length;x++) {
            String sql = "delete from "+tables[x]+" where s_id="+sid;
            PreparedStatement ps=con.prepareStatement(sql);
            ps.executeUpdate();
        }
    }


    public static void jichu(Connection con,String sid,String pid,Map<String,String> map,Proxy proxy) throws IOException, SQLException, InterruptedException {
        String url="http://pe.pedata.cn/getDetailOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);

        java.util.Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String datadate=simpleDateFormat.format(date);

        String logo="http://pic.pedata.cn/"+getHref(doc,"div.detail_top div.detail_top_logo img","src",0).replace("upload//","");
        String jiancheng=getString(doc,"div.control-group:contains(中文简称) div.controls",0);
        String quancheng=getString(doc,"div.float_left span.detail_title24",0);
        String yingjiancheng=getString(doc,"div.control-group:contains(英文简称) div.controls",0);
        String web=getString(doc,"div.float_left label#freeHidden a",0);
        String chenglishijian=getString(doc,"div.control-group:contains(成立时间) div.controls",0);
        String zibenleixing=getString(doc,"div.control-group:contains(资本类型) div.controls",0);
        String zuzhixingshi=getString(doc,"div.control-group:contains(组织形式) div.controls",0);
        String vc=getString(doc,"div.control-group:contains(VC/PE) div.controls",0);
        String zibenguanliliang=getString(doc,"div.control-group:contains(管理资本量) div.controls",0).replace("&nbsp;", "");
        String zhucediqu=getString(doc, "div.control-group:contains(注册地区) div.controls", 0);
        String gongsizongbu=getString(doc,"div.control-group:contains(公司总部) div.controls",0);
        String shifoubeian=getString(doc,"div.control-group:contains(是否备案) div.controls",0);
        String beianshijian=getString(doc,"div.control-group:contains(备案时间) div.controls",0);
        String gongshang=getString(doc,"div.control-group:contains(工商注册号) div.controls",0);

        String miaoshu=getString(doc,"div.detail_onebox:contains(描述) form.form-horizontal",0);

        String sql1="insert into si_jiben(s_id,logo,company_abbreviation,company_full_name,company_eng_abbreviation,web,establishment_time,capital_type,organization_form,VC,managed_capital,registered_area,corporate_headquarters,keep_on_record,registration_number,`describe`,filing_time,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.setString(1,sid);
        ps1.setString(2,logo);
        ps1.setString(3,jiancheng);
        ps1.setString(4,quancheng);
        ps1.setString(5,yingjiancheng);
        ps1.setString(6,web);
        ps1.setString(7,chenglishijian);
        ps1.setString(8,zibenleixing);
        ps1.setString(9,zuzhixingshi);
        ps1.setString(10,vc);
        ps1.setString(11,zibenguanliliang);
        ps1.setString(12,zhucediqu);
        ps1.setString(13,gongsizongbu);
        ps1.setString(14,shifoubeian);
        ps1.setString(15,gongshang);
        ps1.setString(16,miaoshu);
        ps1.setString(17,beianshijian);
        ps1.setString(18,pid);
        ps1.setString(19,datadate);
        try {
            ps1.executeUpdate();
            System.out.println("success_simutong-jigou");
        }catch (Exception e){
            System.out.println("error_simutong-jigou");
        }
        System.out.println("jibenok");



        String sql2="insert into si_lianxi(s_id,contact_name,contact_address,zip_code,contact_person,contact_phone,contact_fax,email,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);
        Element lele=getElement(doc, "div.detail_onebox", 1);
        Elements leles=getElements(lele,"form.form-horizontal");
        if(lele!=null){
            for(Element e:leles){
                String mingcheng=getString(e,"div.detail_onebox_title_top",0).replace("&nbsp;","");
                String dizhi=getString(e,"div.controls.detail_margin100",0);
                String youbian=getString(e,"div.controls.detail_margin100",1);
                String lianxiren=getString(e,"div.controls.detail_margin100",2);
                String lianxidianhua=getString(e,"div.controls.detail_margin100",3);
                String chuanzhen=getString(e,"div.controls.detail_margin100",4);
                String youxiang=getString(e,"div.controls.detail_margin100",5);

                ps2.setString(1,sid);
                ps2.setString(2,mingcheng);
                ps2.setString(3,dizhi);
                ps2.setString(4,youbian);
                ps2.setString(5,lianxiren);
                ps2.setString(6,lianxidianhua);
                ps2.setString(7,chuanzhen);
                ps2.setString(8,youxiang);
                ps2.setString(9,pid);
                ps2.setString(10,datadate);
                ps2.executeUpdate();
            }
        }

        System.out.println("lianxi ok");




        Element ceele=getElement(doc, "div.detail_onebox", 9);
        String jieduan=getString(ceele,"div.control-group:contains(拟投资阶段) div.controls.detail_org_controls",0);
        String hangye=getString(ceele,"div.control-group:contains(拟投行业) div.controls.detail_org_controls",0);
        String diqu=getString(ceele,"div.control-group:contains(拟投地区) div.controls.detail_org_controls",0);
        String guanlizi=getString(ceele,"div.control-group:contains(管理资本量) div.controls.detail_org_controls",0);
        String toudalu=getString(ceele,"div.control-group:contains(投资大陆资本量) div.controls.detail_org_controls",0);
        String dan=getString(ceele,"div.control-group:contains(单项最大投资额) div.controls.detail_org_controls",0);
        String danxiao=getString(ceele,"div.control-group:contains(单项最小投资额) div.controls.detail_org_controls",0);
        String toubiao=getString(ceele,"div.control-group:contains(投资标准) div.controls",0);
        String zengzhi=getString(ceele,"div.control-group:contains(增值服务) div.controls",0);

        String sql3="insert into si_celue(s_id,investment_stage,investment_industry,investment_area,related_capital,mainland_capital,largest_investment,minimum_investment,investment_standard,added_service,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);
        ps3.setString(1,sid);
        ps3.setString(2,jieduan);
        ps3.setString(3,hangye);
        ps3.setString(4,diqu);
        ps3.setString(5,guanlizi);
        ps3.setString(6,toudalu);
        ps3.setString(7,dan);
        ps3.setString(8,danxiao);
        ps3.setString(9,toubiao);
        ps3.setString(10,zengzhi);
        ps3.setString(11,pid);
        ps3.setString(12,datadate);
        ps3.executeUpdate();
        System.out.println("celue ok");
    }

    public static void guanli(Connection con,String sid,String pid,Map<String,String> map,Proxy proxy) throws IOException, SQLException, InterruptedException {
        String url="http://pe.pedata.cn/getTeamListOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);

        java.util.Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String datadate=simpleDateFormat.format(date);

        if(!doc.outerHtml().contains("找不到和您的查询条件相符的记录")) {
            String page = getString(doc, "div.page>a:nth-last-child(3)", 0);
            Elements ele = getElements(doc, "tbody tr");

            String sql = "insert into si_guanli(s_id,person_name,person_position,person_state,person_phone,email,p_id,data_date) values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            if (ele != null) {
                for (Element e : ele) {
                    String xingming = getString(e, "td a", 0);
                    String zhiwei = getString(e, "td", 1);
                    String zhuantai = getString(e, "td", 2);
                    String dianhua = getString(e, "td", 3);
                    String youxiang = getString(e, "td a", 1);

                    ps.setString(1, sid);
                    ps.setString(2, xingming);
                    ps.setString(3, zhiwei);
                    ps.setString(4, zhuantai);
                    ps.setString(5, dianhua);
                    ps.setString(6, youxiang);
                    ps.setString(7, pid);
                    ps.setString(8,datadate);
                    ps.executeUpdate();
                }
            }

            if(StringUtils.isNotEmpty(page)) {
                if ((Integer.parseInt(page) - 1) > 0) {
                    for (int x = 1; x <= (Integer.parseInt(page) - 1); x++) {
                        Document doc2 = guanliget(url, String.valueOf(x + 1), map,proxy);
                        Elements ele2 = getElements(doc2, "tbody tr");
                        if (ele2 != null) {
                            for (Element e : ele2) {
                                String xingming = getString(e, "td a", 0);
                                String zhiwei = getString(e, "td", 1);
                                String zhuantai = getString(e, "td", 2);
                                String dianhua = getString(e, "td", 3);
                                String youxiang = getString(e, "td a", 1);

                                ps.setString(1, sid);
                                ps.setString(2, xingming);
                                ps.setString(3, zhiwei);
                                ps.setString(4, zhuantai);
                                ps.setString(5, dianhua);
                                ps.setString(6, youxiang);
                                ps.setString(7, pid);
                                ps.setString(8,datadate);
                                ps.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("guanli ok");
    }

    public static void touzi(Connection con,String sid,String pid,Map<String,String> map,Proxy proxy) throws IOException, SQLException, InterruptedException {
        String url="http://pe.pedata.cn/getInvestListOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);

        int j=0;
        java.util.Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String datadate=simpleDateFormat.format(date);

        if(!doc.outerHtml().contains("找不到和您的查询条件相符的记录")) {
            String page = getString(doc, "div.page>a:nth-last-child(3)", 0);
            Elements tele = getElements(doc, "tbody tr");

            String sql = "insert into si_touzi(s_id,investment_enterprise,investment_industry,investment_address,investment_person,investment_time,investment_round,investment_money,investment_detail,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            if (tele != null) {
                for (Element e : tele) {
                    String qiye = getString(e, "td", 0);
                    String hangye = getString(e, "td", 1);
                    String diqu = getString(e, "td", 2);
                    String touziren = getString(e, "td", 3);
                    String shijian = getString(e, "td", 4);
                    String lunci = getString(e, "td", 5);
                    String touzijine = getString(e, "td", 6);
                    String xiangqing = getHref(e, "td a", "href", 1);

                    ps.setString(1, sid);
                    ps.setString(2, qiye);
                    ps.setString(3, hangye);
                    ps.setString(4, diqu);
                    ps.setString(5, touziren);
                    ps.setString(6, shijian);
                    ps.setString(7, lunci);
                    ps.setString(8, touzijine);
                    ps.setString(9, xiangqing);
                    ps.setString(10, pid);
                    ps.setString(11,datadate);
                    j++;
                    System.out.println(j+"******************************************");
                    ps.executeUpdate();
                }
            }

            if(StringUtils.isNotEmpty(page)) {
                if ((Integer.parseInt(page) - 1) > 0) {
                    for (int x = 1; x <= (Integer.parseInt(page) - 1); x++) {
                        Document doc2 = touziget(url, String.valueOf(x + 1), sid, map, proxy);
                        System.out.println(x + 1);
                        Elements ele2 = getElements(doc2, "tbody tr");
                        if (ele2 != null) {
                            for (Element e : ele2) {
                                String qiye = getString(e, "td", 0);
                                String hangye = getString(e, "td", 1);
                                String diqu = getString(e, "td", 2);
                                String touziren = getString(e, "td", 3);
                                String shijian = getString(e, "td", 4);
                                String lunci = getString(e, "td", 5);
                                String touzijine = getString(e, "td", 6);
                                String xiangqing = getHref(e, "td a", "href", 1);

                                ps.setString(1, sid);
                                ps.setString(2, qiye);
                                ps.setString(3, hangye);
                                ps.setString(4, diqu);
                                ps.setString(5, touziren);
                                ps.setString(6, shijian);
                                ps.setString(7, lunci);
                                ps.setString(8, touzijine);
                                ps.setString(9, xiangqing);
                                ps.setString(10, pid);
                                ps.setString(11, datadate);
                                ps.executeUpdate();
                                j++;
                                System.out.println(j+"***************************************");
                            }
                        }
                    }
                }
            }
        }
        System.out.println("touziok");
    }


    public static void tuichu(Connection con,String sid,String pid,Map<String,String> map,Proxy proxy) throws IOException, SQLException, InterruptedException {
        String url="http://pe.pedata.cn/getExitListOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);

        java.util.Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String datadate=simpleDateFormat.format(date);

        if(!doc.outerHtml().contains("找不到和您的查询条件相符的记录")) {
            String page = getString(doc, "div.page>a:nth-last-child(3)", 0);
            Elements tele = getElements(doc, "tbody tr");


            String sql = "insert into si_tuichu(s_id,exit_enterprise,exit_time,exit_mode,return_multiple,return_money,cumulative_money,first_time,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            if (tele != null) {
                for (Element e : tele) {
                    String qiye = getString(e, "td", 0);
                    String tuichushijian = getString(e, "td", 1);
                    String tuichufangshi = getString(e, "td", 2);
                    String huibao = getString(e, "td", 3).replace("&nbsp;", "");
                    String huibaobeishu = getString(e, "td", 4);
                    String leiji = getString(e, "td", 5).replace("&nbsp;", "");
                    String shouci = getString(e, "td", 6);

                    ps.setString(1, sid);
                    ps.setString(2, qiye);
                    ps.setString(3, tuichushijian);
                    ps.setString(4, tuichufangshi);
                    ps.setString(5, huibaobeishu);
                    ps.setString(6, huibao);
                    ps.setString(7, leiji);
                    ps.setString(8, shouci);
                    ps.setString(9, pid);
                    ps.setString(10,datadate);
                    ps.executeUpdate();
                }
            }

            if(StringUtils.isNotEmpty(page)) {
                if ((Integer.parseInt(page) - 1) > 0) {
                    for (int x = 1; x <= (Integer.parseInt(page) - 1); x++) {
                        Document doc2 = tuichuget(url, String.valueOf(x + 1), sid, map,proxy);
                        Elements ele2 = getElements(doc2, "tbody tr");
                        if (ele2 != null) {
                            for (Element e : ele2) {
                                String qiye = getString(e, "td", 0);
                                String tuichushijian = getString(e, "td", 1);
                                String tuichufangshi = getString(e, "td", 2);
                                String huibao = getString(e, "td", 3).replace("&nbsp;", "");
                                String huibaobeishu = getString(e, "td", 4);
                                String leiji = getString(e, "td", 5).replace("&nbsp;", "");
                                String shouci = getString(e, "td", 6);

                                ps.setString(1, sid);
                                ps.setString(2, qiye);
                                ps.setString(3, tuichushijian);
                                ps.setString(4, tuichufangshi);
                                ps.setString(5, huibaobeishu);
                                ps.setString(6, huibao);
                                ps.setString(7, leiji);
                                ps.setString(8, shouci);
                                ps.setString(9, pid);
                                ps.setString(10,datadate);
                                ps.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("tuichu ok");
    }


    public static void jijin(Connection con,String sid,String pid,Map<String,String> map,Proxy proxy) throws IOException, SQLException, InterruptedException {
        String url="http://pe.pedata.cn/getManagedFundListOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);

        java.util.Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String datadate=simpleDateFormat.format(date);

        if(!doc.outerHtml().contains("找不到和您的查询条件相符的记录")) {
            String page = getString(doc, "div.page>a:nth-last-child(3)", 0);
            Elements tele = getElements(doc, "tbody tr");

            String sql = "insert into si_jijin(s_id,fund_name,fund_type,recruitment_status,capital_type,collection_time,collection_money,investment_amount,detail_url,p_id,data_date) values(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            if (tele != null) {
                for (Element e : tele) {
                    String jijin = getString(e, "td", 0);
                    String leixing = getString(e, "td", 1);
                    String zhuangtai = getString(e, "td", 2);
                    String zibenleixing = getString(e, "td", 3);
                    String wanchengshijian = getString(e, "td", 4);
                    String jine = getString(e, "td", 5).replace("&nbsp;", "");
                    String shuliang = getString(e, "td", 6);
                    String xiangqingur = "http://pe.pedata.cn/" + getHref(e, "td a", "href", 0);

                    ps.setString(1, sid);
                    ps.setString(2, jijin);
                    ps.setString(3, leixing);
                    ps.setString(4, zhuangtai);
                    ps.setString(5, zibenleixing);
                    ps.setString(6, wanchengshijian);
                    ps.setString(7, jine);
                    ps.setString(8, shuliang);
                    ps.setString(9, xiangqingur);
                    ps.setString(10, pid);
                    ps.setString(11,datadate);
                    ps.executeUpdate();
                }
            }

            if(StringUtils.isNotEmpty(page)) {
                if ((Integer.parseInt(page) - 1) > 0) {
                    for (int x = 1; x <= (Integer.parseInt(page) - 1); x++) {
                        Document doc2 = jijinget(url, String.valueOf(x + 1), sid, map,proxy);
                        Elements ele2 = getElements(doc2, "tbody tr");
                        if (ele2 != null) {
                            for (Element e : ele2) {
                                String jijin = getString(e, "td", 0);
                                String leixing = getString(e, "td", 1);
                                String zhuangtai = getString(e, "td", 2);
                                String zibenleixing = getString(e, "td", 3);
                                String wanchengshijian = getString(e, "td", 4);
                                String jine = getString(e, "td", 5).replace("&nbsp;", "");
                                String shuliang = getString(e, "td", 6);
                                String xiangqingur = "http://pe.pedata.cn/" + getHref(e, "td a", "href", 0);

                                ps.setString(1, sid);
                                ps.setString(2, jijin);
                                ps.setString(3, leixing);
                                ps.setString(4, zhuangtai);
                                ps.setString(5, zibenleixing);
                                ps.setString(6, wanchengshijian);
                                ps.setString(7, jine);
                                ps.setString(8, shuliang);
                                ps.setString(9, xiangqingur);
                                ps.setString(10, pid);
                                ps.setString(11,datadate);
                                ps.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("jijinok");
    }

}
