package simutong.simutong_jijin;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import simutong.simutong_jigou.Qingqiu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

import static Utils.JsoupUtils.*;
import static simutong.simutong_jigou.gongsi_ming.get;
import static simutong.simutong_jijin.qingQiu.denglu;
import static simutong.simutong_jijin.qingQiu.jichuget;

/**
 * Created by Administrator on 2017/6/27.
 */
public class Jijin {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, ParseException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        Connection con=getcon();
        data(con,proxy);

    }


    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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


    public static void data(Connection con,Proxy proxy) throws SQLException, IOException, InterruptedException, ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Random r=new Random();
        String sql="select DISTINCT detail_url,s_id from dw_online.si_jijin where s_id not in (select DISTINCT g_id from si_jijin_detail)";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 1800000;
        String[] zhanghu=new String[]{"simutong3@gaiyachuangxin.cn","111111","xiaohang.qu@lingweispace.cn","111111"};
        String[] pc=new String[]{"50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7","50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7"};
        Map<String,String> map= denglu(proxy, zhanghu[0], zhanghu[1], pc[0]);
        int flag=0;
        int p=0;
        while (rs.next()){
            try {
                int th = r.nextInt(5001) + 10000;
                String s_id = rs.getString(rs.findColumn("detail_url")).replace("http://pe.pedata.cn/getDetailFund.action?param.fund_id=", "").replace("http://pe.pedata.cn/","");
                String g_id = rs.getString(rs.findColumn("s_id"));
                if(StringUtils.isNotEmpty(s_id)) {
                    if (getJichu(proxy, con, s_id, g_id, map)) {
                        Thread.sleep(r.nextInt(5001) + 5000);
                        getlp(proxy, s_id, con, map);
                        Thread.sleep(r.nextInt(5001) + 5000);
                        gettouzi(proxy, s_id, con, map);
                        Thread.sleep(r.nextInt(5001) + 5000);
                        gettuichu(proxy, s_id, con, map);
                    }
                }
                Thread.sleep(th);
                long t = System.currentTimeMillis();
                if (t > (begin + cur)) {
                    cur = (r.nextInt(50) * 60 * 1000) + 1800000;
                    /*Thread.sleep(cur);
                    if(con!=null) {
                        con.close();
                    }
                    con=getcon();
                    map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);*/
                    if (flag == 0) {
                        map = Qingqiu.denglu(proxy, zhanghu[2], zhanghu[3], pc[1]);
                        flag = 1;
                    } else {
                        map = Qingqiu.denglu(proxy, zhanghu[0], zhanghu[1], pc[0]);
                        flag = 0;
                    }
                    t = System.currentTimeMillis();
                    begin = t;
                }
                java.util.Date date = new java.util.Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time1 = simpleDateFormat.format(date) + " 07:30:00";
                String time2 = simpleDateFormat.format(date) + " 21:30:00";
                long t3 = simpleDateFormat1.parse(time2).getTime();
                if (t >= t3) {
                    Thread.sleep(36000000);
                    if (con != null) {
                        con.close();
                    }
                    con = getcon();
                    map = Qingqiu.denglu(proxy, zhanghu[2], zhanghu[3], pc[1]);
                    flag = 1;
                    t = System.currentTimeMillis();
                    begin = t;
                }
                p++;
                System.out.println(p);
                System.out.println("------------------------------------------");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }




    public static boolean getJichu(Proxy proxy,Connection con,String s_id,String g_id,Map<String,String> map) throws IOException, InterruptedException, SQLException {
        Document doc=jichuget("http://pe.pedata.cn/getDetailFund.action?param.fund_id="+s_id,map,proxy);
        if(doc!=null) {
            String zhongquan = getString(doc, "div.detail_top div.float_left span.detail_title24", 0);
            String zhongjian = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(中文简称) div.controls", 0);
            String yingjian = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(英文简称) div.controls", 0);
            String guanlijigou = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(管理机构) div.controls", 0);
            String weituo = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(是否政府委托管理) div.controls", 0);
            String chenglitime = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(成立时间) div.controls", 0);
            String zongbu = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(基金总部) div.controls", 0);
            String zibenleixing = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(资本类型) div.controls", 0);
            String zuzhixingshi = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(组织形式) div.controls", 0);
            String jijinleixing = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(基金类型) div.controls", 0);
            String mujizhuangtai = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(募集状态) div.controls", 0);
            String beian = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(是否备案) div.controls", 0);
            String shenbaozhuangtai = getString(doc, "div.detail_right div.detail_onebox div.control-group:contains(申报状态) div.controls", 0);
            String miaoshu = getString(doc, "div.detail_onebox:contains(描述) div.detail_bewrite", 0);


            String sql1 = "insert into si_jijin_detail(s_id,g_id,zhong_quan,zhong_jian,ying_jian,guanli_jigou,zhengfu_flag,chengli_time,zong_bu,ziben_type,zuzhi_xingshi,jijin_type,muji_statue,bei_an,shenbao_statue,de_sc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps1 = con.prepareStatement(sql1);

            ps1.setString(1, s_id);
            ps1.setString(2, g_id);
            ps1.setString(3, zhongquan);
            ps1.setString(4, zhongjian);
            ps1.setString(5, yingjian);
            ps1.setString(6, guanlijigou);
            ps1.setString(7, weituo);
            ps1.setString(8, chenglitime);
            ps1.setString(9, zongbu);
            ps1.setString(10, zibenleixing);
            ps1.setString(11, zuzhixingshi);
            ps1.setString(12, jijinleixing);
            ps1.setString(13, mujizhuangtai);
            ps1.setString(14, beian);
            ps1.setString(15, shenbaozhuangtai);
            ps1.setString(16, miaoshu);
            ps1.executeUpdate();
            System.out.println("jiben ok");

            String sql2 = "insert into si_jijin_lianxi(s_id,lianxi_ming,a_ddress,you_bian,lianxi_ren,p_hone,chuan_zhen,e_mail) values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            Elements ele = getElements(doc, "div.detail_onebox:contains(联系方式) form.form-horizontal");
            if (ele != null) {
                for (Element e : ele) {
                    String ming = getString(e, "div.detail_onebox_title_top", 0);
                    String dizhi = getString(e, "div.control-group:contains(地址) div.controls", 0);
                    String youbian = getString(e, "div.control-group:contains(邮编) div.controls", 0);
                    String lianxiren = getString(e, "div.control-group:contains(联系人) div.controls", 0);
                    String dianhua = getString(e, "div.control-group:contains(联系电话) div.controls", 0);
                    String chuanzhen = getString(e, "div.control-group:contains(传真) div.controls", 0);
                    String email = getString(e, "div.control-group:contains(邮箱) div.controls", 0);

                    ps2.setString(1, s_id);
                    ps2.setString(2, ming);
                    ps2.setString(3, dizhi);
                    ps2.setString(4, youbian);
                    ps2.setString(5, lianxiren);
                    ps2.setString(6, dianhua);
                    ps2.setString(7, chuanzhen);
                    ps2.setString(8, email);
                    ps2.executeUpdate();
                }
            }
            System.out.println("lianxi ok");

            String sql3 = "insert into si_jijin_muji(s_id,r_ound,s_tatue,shi_jian,m_oney,lp_geshu) values(?,?,?,?,?,?)";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            Elements elem = getElements(doc, "div.detail_onebox:contains(基金募集) table.table.table-hover tbody tr");
            if (elem != null) {
                for (Element e : elem) {
                    String lunci = getString(e, "td", 0);
                    String zhuangtai = getString(e, "td", 1);
                    String shijian = getString(e, "td", 2);
                    String jine = getString(e, "td", 3);
                    String lpgeshu = getString(e, "td", 4);

                    ps3.setString(1, s_id);
                    ps3.setString(2, lunci);
                    ps3.setString(3, zhuangtai);
                    ps3.setString(4, shijian);
                    ps3.setString(5, jine);
                    ps3.setString(6, lpgeshu);
                    ps3.executeUpdate();
                }
            }
            System.out.println("muji ok");
            return true;
        }else{
            return false;
        }

    }


    public static void getlp(Proxy proxy,String s_id,Connection con,Map<String,String> map) throws IOException, InterruptedException, SQLException {
        Document doc=jichuget("http://pe.pedata.cn/getLp3rdFund.action?param.fund_id="+s_id, map,proxy);

        String sql="insert into si_jiji_lp(s_id,lp_ming,guoyou_flag,shu_xing,lei_xing,chengnuo_jine,lun_ci,chuzi_time,detail_url) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Elements ele=getElements(doc,"div.detail_onebox:contains(有限合伙人) table.table.table-hover tbody tr");
        if(ele!=null){
            for(Element e:ele){
                String ming=getString(e,"td",0);
                String detailurl=getHref(e,"td a","href",0);
                String guoyou=getString(e,"td",1);
                String lpshuxing=getString(e,"td",2);
                String lpleixing=getString(e,"td",3);
                String jine=getString(e,"td",4);
                String lunci=getString(e,"td",5);
                String chuzitime=getString(e,"td",6);

                ps.setString(1,s_id);
                ps.setString(2,ming);
                ps.setString(3,guoyou);
                ps.setString(4,lpshuxing);
                ps.setString(5,lpleixing);
                ps.setString(6,jine);
                ps.setString(7,lunci);
                ps.setString(8,chuzitime);
                ps.setString(9,detailurl);
                ps.executeUpdate();
            }
        }
        System.out.println("lp ok");
    }


    public static void gettouzi(Proxy proxy,String s_id,Connection con,Map<String,String> map) throws IOException, InterruptedException, SQLException {
        Random r=new Random();
        Document doc=jichuget("http://pe.pedata.cn/getInvest3rdFund.action?param.fund_id="+s_id, map,proxy);

        String sql="insert into si_jijin_touzi(s_id,ji_jin,qi_ye,hang_ye,di_qu,touzi_ren,touzi_time,lun_ci,jin_e,qiye_sid) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Elements ele=getElements(doc,"div.detail_onebox:contains(投资事件) table.table.table-hover tbody tr");
        if(ele!=null){
            for(Element e:ele){
                String jijin=getString(e,"td",0);
                String qiye=getString(e,"td",1);
                String qiyesid=getHref(e,"td a","href",1).replace("getDetailEp.action?param.ep_id=","");
                String hangye=getString(e,"td",2);
                String diqu=getString(e,"td",3);
                String touziren=getString(e,"td",4);
                String touzishijian=getString(e,"td",5);
                String lunci=getString(e,"td",6);
                String jine=getString(e,"td",7);

                ps.setString(1,s_id);
                ps.setString(2,jijin);
                ps.setString(3,qiye);
                ps.setString(4,hangye);
                ps.setString(5,diqu);
                ps.setString(6,touziren);
                ps.setString(7,touzishijian);
                ps.setString(8,lunci);
                ps.setString(9,jine);
                ps.setString(10,qiyesid);
                ps.executeUpdate();

                int th = r.nextInt(3001) + 3000;
                Thread.sleep(th);
            }
        }
        System.out.println("touzi ok");
    }


    public static void gettuichu(Proxy proxy,String s_id,Connection con,Map<String,String> map) throws IOException, InterruptedException, SQLException {
        Random r=new Random();
        Document doc=jichuget("http://pe.pedata.cn/getExit3rdFund.action?param.fund_id="+s_id, map,proxy);

        String sql="insert into si_jijin_tuichu(s_id,qi_ye,qiye_sid,tuichu_time,tuichu_fangshi,tuichu_huibao,huibao_beishu,leiji_jine,shouci_time,detail_url) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Elements ele=getElements(doc,"div.detail_onebox:contains(退出事件) table.table.table-hover tbody tr");
        if(ele!=null){
            for(Element e:ele){
                String qiye=getString(e,"td",0);
                String qiyesid=getHref(e,"td a","href",0).replace("getDetailEp.action?param.ep_id=","");
                String tuichutime=getString(e,"td",1);
                String fangshi=getString(e,"td",2);
                String zhangm=getString(e,"td",3);
                String beishu=getString(e,"td",4);
                String leijijine=getString(e,"td",5);
                String shouci=getString(e,"td",6);
                String siqng=getHref(e,"td a","href",1);

                ps.setString(1,s_id);
                ps.setString(2,qiye);
                ps.setString(3,qiyesid);
                ps.setString(4,tuichutime);
                ps.setString(5,fangshi);
                ps.setString(6,zhangm);
                ps.setString(7,beishu);
                ps.setString(8,leijijine);
                ps.setString(9,shouci);
                ps.setString(10,siqng);
                ps.executeUpdate();

                int th = r.nextInt(3001) + 3000;
                Thread.sleep(th);
            }
        }
        System.out.println("tuichu ok");
    }




}
