package qichacha;


import Utils.JsoupUtils;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Detail {
    public static Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/qichacha?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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

    public static void main(String args[]) throws SQLException, UnsupportedEncodingException, InterruptedException {
        parse();
    }

    public static void source(){

    }

    public static void parse() throws SQLException, UnsupportedEncodingException, InterruptedException {
        Modular modular=new Modular();
        String sql="insert into tyc_jichu_quan(quan_cheng,ceng_yongming,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,hezhun_riqi,jingying_zhuangtai,nashui_shibie,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,c_desc,en_cname,faren_logo,shijiao_ziben,gongsi_guimo,jingying_fangshi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        String url="http://www.qichacha.com/firm_9cce0780ab7644008b73bc2120479d31.html";
        Document doc=spider.getDetail("http://www.qichacha.com/firm_9cce0780ab7644008b73bc2120479d31.html");
        String quan_cheng= JsoupUtils.getString(doc,"div.content div.row.title h1",0);
        String p_hone=JsoupUtils.getString(doc,"div.content div.row:contains(电话) span.cvlu span",0);
        String logo=JsoupUtils.getHref(doc,"div.panel.padder.n-s.nheader div.logo img","src",0);
        String e_mail=JsoupUtils.getString(doc,"div.content div.row:contains(邮箱) span.cvlu:nth-child(2) a",0);
        String w_eb=JsoupUtils.getString(doc,"div.content div.row:contains(官网) span.cvlu:nth-child(4) a",0);
        String a_ddress=JsoupUtils.getString(doc,"div.content div.row:contains(地址) span.cvlu a",0);
        String fa_ren=JsoupUtils.getString(doc,"#Cominfo > table:nth-child(3) > tbody > tr:nth-child(2) > td.ma_left > div > div.clearfix > div:nth-child(2) > a.bname",0);
        String faren_logo=JsoupUtils.getHref(doc,"#Cominfo > table:nth-child(3) > tbody > tr:nth-child(2) > td.ma_left > div > div.clearfix > div:nth-child(1) > img","src",0);
        String zhuce_ziben=JsoupUtils.getString(doc,"td:contains(注册资本)+td",0);
        String shijiao_ziben=JsoupUtils.getString(doc,"td:contains(实缴资本)+td",0);
        String jingying_zhuangtai=JsoupUtils.getString(doc,"td:contains(经营状态)+td",0);
        String zhuce_shijian=JsoupUtils.getString(doc,"td:contains(成立日期)+td",0);
        String gongshang_hao=JsoupUtils.getString(doc,"td:contains(注册号)+td",0);
        String zuzhijigou_daima=JsoupUtils.getString(doc,"td:contains(组织机构代码)+td",0);
        String nashui_shibie=JsoupUtils.getString(doc,"td:contains(纳税人识别号)+td",0);
        String tongyi_xinyong=JsoupUtils.getString(doc,"td:contains(统一社会信用代码)+td",0);
        String qiye_leixing=JsoupUtils.getString(doc,"td:contains(公司类型)+td",0);
        String hang_ye=JsoupUtils.getString(doc,"td:contains(所属行业)+td",0);
        String hezhun_riqi=JsoupUtils.getString(doc,"td:contains(核准日期)+td",0);
        String dengji_jiguan=JsoupUtils.getString(doc,"td:contains(登记机关)+td",0);
        String zhuce_dizhi=JsoupUtils.getString(doc,"td:contains(所属地区)+td",0);
        String en_cname=JsoupUtils.getString(doc,"td:contains(英文名)+td",0);
        String ceng_yongming=JsoupUtils.getString(doc,"td:contains(曾用名)+td",0);
        String jingying_fangshi=JsoupUtils.getString(doc,"td:contains(经营方式)+td",0);
        String gongsi_guimo=JsoupUtils.getString(doc,"td:contains(人员规模)+td",0);
        String yingye_nianxian=JsoupUtils.getString(doc,"td:contains(营业期限)+td",0);
        String jingying_fanwei=JsoupUtils.getString(doc,"td:contains(经营范围)+td",0);
        String c_desc=JsoupUtils.getString(doc,"section#Comintroduce:contains(公司简介)",0).replace("公司简介","");

        ps.setString(1,quan_cheng);
        ps.setString(2,ceng_yongming);
        ps.setString(3,logo);
        ps.setString(4,p_hone);
        ps.setString(5,e_mail);
        ps.setString(6,a_ddress);
        ps.setString(7,w_eb);
        ps.setString(8,fa_ren);
        ps.setString(9,zhuce_ziben);
        ps.setString(10,zhuce_shijian);
        ps.setString(11,hezhun_riqi);
        ps.setString(12,jingying_zhuangtai);
        ps.setString(13,nashui_shibie);
        ps.setString(14,gongshang_hao);
        ps.setString(15,zuzhijigou_daima);
        ps.setString(16,tongyi_xinyong);
        ps.setString(17,qiye_leixing);
        ps.setString(18,hang_ye);
        ps.setString(19,yingye_nianxian);
        ps.setString(20,dengji_jiguan);
        ps.setString(21,zhuce_dizhi);
        ps.setString(22,jingying_fanwei);
        ps.setString(23,c_desc);
        ps.setString(24,en_cname);
        ps.setString(25,faren_logo);
        ps.setString(26,shijiao_ziben);
        ps.setString(27,gongsi_guimo);
        ps.setString(28,jingying_fangshi);
        ps.executeUpdate();

        //modular.zhaopin(url,quan_cheng);
        //modular.biangeng(doc,quan_cheng);
        //modular.caipan(url,quan_cheng);
        //modular.duiwaitouzi(doc,url,quan_cheng);
        //modular.fenzhi(doc,url,quan_cheng);
        //modular.shuixin(url,quan_cheng);
        //modular.weixin(url,quan_cheng);
        //modular.xingchugong(url,quan_cheng);
        //modular.xingchuzh(url,quan_cheng);
        //modular.zhaotoubiao(url,quan_cheng);
        //modular.gudong(doc,quan_cheng);
    }
}
