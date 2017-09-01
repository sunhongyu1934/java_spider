package tianyancha.XinxiXin;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static Utils.JsoupUtils.*;
import static tianyancha.XinxiXin.XinxiXin.detailget;
import static tianyancha.XinxiXin.XinxiXin.jisuan;

/**
 * Created by Administrator on 2017/7/3.
 */
public class Tyc_quan {
    private static Connection con;
    private static PreparedStatement ps1;
    private static PreparedStatement ps2;
    private static PreparedStatement ps3;
    private static PreparedStatement ps4;
    private static PreparedStatement ps5;
    private static PreparedStatement ps6;
    private static PreparedStatement ps7;
    private static PreparedStatement ps8;
    private static PreparedStatement ps9;
    private static PreparedStatement ps10;
    private static PreparedStatement ps11;
    private static PreparedStatement ps12;
    private static PreparedStatement ps13;
    private static PreparedStatement ps14;
    private static PreparedStatement ps15;
    private static PreparedStatement ps16;
    private static PreparedStatement ps17;
    private static PreparedStatement ps18;
    private static PreparedStatement ps19;
    private static PreparedStatement ps20;
    private static PreparedStatement ps21;
    private static PreparedStatement ps22;
    private static PreparedStatement ps23;
    private static PreparedStatement ps24;
    private static PreparedStatement ps25;
    private static PreparedStatement ps26;
    private static PreparedStatement ps27;
    private static PreparedStatement ps28;
    private static PreparedStatement ps29;
    private static PreparedStatement ps30;
    private static PreparedStatement ps31;
    private static PreparedStatement ps32;
    private static PreparedStatement ps33;
    public Tyc_quan(Connection con,String[] value) throws SQLException {
        this.con=con;
        for(int x=0;x<value.length;x++){
            if(value[x].equals("基本信息")){
                String sql1="insert into tyc_jichu_quan(quan_cheng,ceng_yongming,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,hezhun_riqi,jingying_zhuangtai,nashui_shibie,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,t_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                this.ps1=con.prepareStatement(sql1);
            }else if(value[x].equals("主要成员")){
                String sql2="insert into tyc_main(t_id,zhi_wu,p_name,p_tid) values(?,?,?,?)";
                this.ps2=con.prepareStatement(sql2);
            }else if(value[x].equals("股东信息")){
                String sql6="insert into tyc_gudongxin(t_id,p_name,p_tid,chuzi_bili,renjiao_chuzi) values(?,?,?,?,?)";
                this.ps6=con.prepareStatement(sql6);
            }else if(value[x].equals("对外投资")){
                String sql3="insert into tyc_out_investment(t_id,invest_name,invest_tid,representative,rep_tid,register_amount,register_date,investment_amount,investment_rate,com_status) values(?,?,?,?,?,?,?,?,?,?)";
                this.ps3=con.prepareStatement(sql3);
            }else if(value[x].equals("变更记录")){
                String sql4="insert into tyc_change_record(t_id,change_time,change_item,before_change,after_change) values(?,?,?,?,?)";
                this.ps4=con.prepareStatement(sql4);
            }else if(value[x].equals("分支机构")){
                String sql5="insert into tyc_branch_info(t_id,branch_name,branch_tid,branch_legal,branch_status,reg_time) values(?,?,?,?,?,?)";
                this.ps5=con.prepareStatement(sql5);
            }else if(value[x].equals("融资历史")){
                String sql7="insert into tyc_financing_history(t_id,fin_time,fin_round,com_valuation,fin_amount,fin_rate,fin_investor,fin_investor_tid,news_from) values(?,?,?,?,?,?,?,?,?)";
                this.ps7=con.prepareStatement(sql7);
            }else if(value[x].equals("核心团队")){
                String sql8="insert into tyc_core_team(t_id,m_name,logo_url,m_position,m_experience) values(?,?,?,?,?)";
                this.ps8=con.prepareStatement(sql8);
            }else if(value[x].equals("企业业务")){
                String sql9="insert into tyc_enterprise_business(t_id,p_name,logo_url,p_classfiy,p_desc) values(?,?,?,?,?)";
                this.ps9=con.prepareStatement(sql9);
            }else if(value[x].equals("投资事件")){
                String sql10="insert into tyc_investment_event(t_id,invest_time,invest_round,invest_amount,investors,investors_tid,project_nm,project_nm_logo,invest_area,industry,p_business,project_tid) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                this.ps10=con.prepareStatement(sql10);
            }else if(value[x].equals("竞品信息")){
                String sql11="insert into tyc_competing_information(t_id,compet_name,compet_logo,compet_area,p_round,p_industry,compet_business,create_time,p_valucation) values(?,?,?,?,?,?,?,?,?)";
                this.ps11=con.prepareStatement(sql11);
            }else if(value[x].equals("法律诉讼")){
                String sql12="insert into tyc_legal_proceedings(t_id,legal_time,legal_document,legal_type,legal_num) values(?,?,?,?,?)";
                this.ps12=con.prepareStatement(sql12);
            }else if(value[x].equals("法院公告")){
                String sql13="insert into tyc_court_notice(t_id,notice_time,accuser,defendant,notice_type,court) values(?,?,?,?,?,?)";
                this.ps13=con.prepareStatement(sql13);
            }else if(value[x].equals("被执行人")){
                String sql14="insert into tyc_person_subjected(t_id,sub_time,sub_norm,sub_num,sub_court) values(?,?,?,?,?)";
                this.ps14=con.prepareStatement(sql14);
            }else if(value[x].equals("失信人")){
                String sql15="insert into tyc_dishonest_person(t_id,dish_time,dish_num,dish_court,dish_status,accord_num) values(?,?,?,?,?,?)";
                this.ps15=con.prepareStatement(sql15);
            }else if(value[x].equals("经营异常")){
                String sql16="insert into tyc_abnormal_operation(t_id,abno_time,abno_reason,decision_org,yichu_time,yichu_reason,yichu_org) values(?,?,?,?,?,?,?)";
                this.ps16=con.prepareStatement(sql16);
            }else if(value[x].equals("行政处罚")){
                String sql17="insert into tyc_administrative_sanction(t_id,sanct_time,decision_org,sanct_detail) values(?,?,?,?)";
                this.ps17=con.prepareStatement(sql17);
            }else if(value[x].equals("严重违法")){
                String sql18="insert into tyc_serious_violation(t_id,decide_date,decide_reason,decide_org) values(?,?,?,?)";
                this.ps18=con.prepareStatement(sql18);
            }else if(value[x].equals("股权出质")){
                String sql19="insert into tyc_stock_ownership(t_id,stock_time,stock_num,stock_name,pledgee,stock_status) values(?,?,?,?,?,?)";
                this.ps19=con.prepareStatement(sql19);
            }else if(value[x].equals("动产抵押")){
                String sql20="insert into tyc_chattel_mortgage(t_id,registe_time,registe_num,mortgage_type,registe_org,mortgage_status) values(?,?,?,?,?,?)";
                this.ps20=con.prepareStatement(sql20);
            }else if(value[x].equals("欠税公告")){
                String sql21="insert into tyc_tax_notice(t_id,notice_time,tax_num,tax_type,tax_amount,tax_balance,tax_org) values(?,?,?,?,?,?,?)";
                this.ps21=con.prepareStatement(sql21);
            }else if(value[x].equals("招投标")){
                String sql22="insert into tyc_company_bidding(t_id,release_time,bidd_title,bidd_url,purchase_nm) values(?,?,?,?,?)";
                this.ps22=con.prepareStatement(sql22);
            }else if(value[x].equals("债券信息")){
                String sql23="insert into tyc_bond_information(t_id,bond_time,bond_nm,bond_num,bond_type,bond_grade) values(?,?,?,?,?,?)";
                this.ps23=con.prepareStatement(sql23);
            }else if(value[x].equals("购地信息")){
                String sql24="insert into tyc_purchase_information(t_id,pur_time,pur_nm,begin_date,pur_acreage,pur_district) values(?,?,?,?,?,?)";
                this.ps24=con.prepareStatement(sql24);
            }else if(value[x].equals("招聘")){
                String sql25="insert into tyc_recruit_information(t_id,rec_time,rec_position,rec_salary,work_experience,rec_num,rec_city) values(?,?,?,?,?,?,?)";
                this.ps25=con.prepareStatement(sql25);
            }else if(value[x].equals("税务评级")){
                String sql26="insert into tyc_tax_rating(t_id,tax_year,tax_rating,tax_type,tax_num,evaluate_org) values(?,?,?,?,?,?)";
                this.ps26=con.prepareStatement(sql26);
            }else if(value[x].equals("抽查检查")){
                String sql27="insert into tyc_spot_check(t_id,spot_date,spot_type,spot_result,spot_org) values(?,?,?,?,?)";
                this.ps27=con.prepareStatement(sql27);
            }else if(value[x].equals("产品信息")){
                String sql28="insert into tyc_product_information(t_id,p_name,p_logo,p_shorname,p_classfy,p_industry) values(?,?,?,?,?,?)";
                this.ps28=con.prepareStatement(sql28);
            }else if(value[x].equals("资质证书")){
                String sql29="insert into tyc_qualification_certificate(t_id,equipment_name,cert_type,cert_date,end_date,equipment_num,cert_num) values(?,?,?,?,?,?,?)";
                this.ps29=con.prepareStatement(sql29);
            }else if(value[x].equals("商标信息")){
                String sql30="insert into tyc_trademark_information(t_id,apply_date,trademark,trademark_nm,registe_num,trademark_type,trademark_status) values(?,?,?,?,?,?,?)";
                this.ps30=con.prepareStatement(sql30);
            }else if(value[x].equals("专利")){
                String sql31="insert into tyc_patent_information(t_id,publish_date,patent_nm,apply_num,publish_num) values(?,?,?,?,?)";
                this.ps31=con.prepareStatement(sql31);
            }else if(value[x].equals("著作权")){
                String sql32="insert into tyc_copyright_information(t_id,approve_date,app_name,app_shortname,registe_num,classfy_num,app_version) values(?,?,?,?,?,?,?)";
                this.ps32=con.prepareStatement(sql32);
            }else if(value[x].equals("网站备案")){
                String sql33="insert into tyc_website_filing(t_id,audit_time,website_nm,website_url,domain_name,filing_num,web_status,comp_property) values(?,?,?,?,?,?,?,?)";
                this.ps33=con.prepareStatement(sql33);
            }
        }
    }

    public static void jichu(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        String quancheng=getString(doc2,"div.company_header_width.ie9Style span.f18.in-block.vertival-middle",0);
        if(StringUtils.isEmpty(quancheng)){
            System.out.println(doc2);
        }
        String ceng=getString(doc2,"div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4",0);
        String phone=getString(doc2,"div.f14.new-c3.mt10 div.in-block.vertical-top:contains(电话) span",1);
        String email=getString(doc2,"div.f14.new-c3.mt10 div.in-block.vertical-top:contains(邮箱) span",1);
        String web=getString(doc2,"div.f14.new-c3 div.in-block.vertical-top:contains(网址) a",0);
        String address=getString(doc2,"div.f14.new-c3 div.in-block.vertical-top:contains(地址) span",1);
        String logo=getHref(doc2,"div.b-c-white.new-border.over-hide.mr10.ie9Style img","src",0);
        String zhuceziben=getString(doc2,"div.new-border-bottom:contains(注册资本) div.pb10",0);
        String zhuceshijian=getString(doc2,"div.new-border-bottom:contains(注册时间) div.pb10",0);
        String statu=getString(doc2,"div.pt10:contains(企业状态) div.baseinfo-module-content-value.statusType1",0);
        String gongshang=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(工商注册号) span",0);
        String zuzhijigou=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(组织机构代码) span",0);
        String tongyixinyong=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(统一信用代码) span",0);
        String qiyeleixing=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(企业类型) span",0);
        String nashuiren=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(纳税人识别号) span",0);
        String hangye=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(行业) span",0);
        String yingyeqixian=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(营业期限) span",0);
        String hezhunriqi=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(核准日期) span",0);
        String dengjijiguan=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(登记机关) span",0);
        String zhucedizhi=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(注册地址) span",0);
        String jingyingfanwei=getString(doc2,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(经营范围) span.js-full-container",0);
        String faren=getString(doc2,"div.in-block.vertical-top.pl15 div.new-c3.f18.overflow-width a",0);

        ps1.setString(1,quancheng);
        ps1.setString(2,ceng);
        ps1.setString(3,logo);
        ps1.setString(4,phone);
        ps1.setString(5,email);
        ps1.setString(6,address);
        ps1.setString(7,web);
        ps1.setString(8,faren);
        ps1.setString(9,zhuceziben);
        ps1.setString(10,zhuceshijian);
        ps1.setString(11, hezhunriqi);
        ps1.setString(12,statu);
        ps1.setString(13,nashuiren);
        ps1.setString(14,gongshang);
        ps1.setString(15,zuzhijigou);
        ps1.setString(16,tongyixinyong);
        ps1.setString(17,qiyeleixing);
        ps1.setString(18,hangye);
        ps1.setString(19,yingyeqixian);
        ps1.setString(20, dengjijiguan);
        ps1.setString(21,zhucedizhi);
        ps1.setString(22,jingyingfanwei);
        ps1.setString(23,tid);
        ps1.executeUpdate();
        System.out.println("success_tyc-quan");
    }

    public static void zhuyao(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements zele=getElements(doc2,"div#_container_staff div.clearfix div.staffinfo-module-container");
        if(zele!=null){
            for(Element e:zele){
                String zhiwu=getString(e,"div.in-block.f14.new-c5.pt9.pl10.overflow-width.vertival-middle",0).replace("\n","").replace(" ","");
                String ming=getString(e,"a.overflow-width.in-block.vertival-middle.pl15.mb4",0);
                String mtid=getHref(e,"a.overflow-width.in-block.vertival-middle.pl15.mb4","href",0).replace("/human/","");

                ps2.setString(1,tid);
                ps2.setString(2,zhiwu);
                ps2.setString(3,ming);
                ps2.setString(4,mtid);
                ps2.executeUpdate();
            }
        }
    }
    public static void duiwaitouzi(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_invest div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n","");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++) {
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/invest.xhtml?ps=20&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    Elements duiwai = getElements(doc2, "div.out-investment-container tbody tr");
                    if(duiwai!=null&&StringUtils.isNotEmpty(duiwai.toString())){
                        break;
                    }
                }
            }
            Elements duiwai = getElements(doc2, "div.out-investment-container tbody tr");
            if (duiwai != null) {
                for (Element e : duiwai) {
                    String gongsiming = getString(e, "td a.query_name span.text-click-color", 0);
                    String gongsitid = getHref(e, "td a.query_name", "href", 0).replace("/company/", "");
                    String beitouren = getString(e, "td span a.point.new-c4", 0);
                    String beitourentid = getHref(e, "td span a.point.new-c4", "href", 0).replace("/human/", "");
                    String zhuceziben = getString(e, "td", 2);
                    String touzishue = getString(e, "td", 3);
                    String touzizhanbi = getString(e, "td", 4);
                    String zhuceshijian = getString(e, "td", 5);
                    String zhuangtai = getString(e, "td", 6);

                    if(StringUtils.isNotEmpty(gongsiming)) {
                        ps3.setString(1, tid);
                        ps3.setString(2, gongsiming);
                        ps3.setString(3, gongsitid);
                        ps3.setString(4, beitouren);
                        ps3.setString(5, beitourentid);
                        ps3.setString(6, zhuceziben);
                        ps3.setString(7, zhuceshijian);
                        ps3.setString(8, touzishue);
                        ps3.setString(9, touzizhanbi);
                        ps3.setString(10, zhuangtai);
                        ps3.executeUpdate();
                    }
                }
            }
        }
    }

    public static void biangeng(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_changeinfo div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/changeinfo.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements bian=getElements(doc2,"div#_container_changeinfo tbody tr");
            if(x>=2){
                bian=getElements(doc2,"tbody tr");
            }
            if(bian!=null){
                for(Element e:bian){
                    String biantime=getString(e,"td",0);
                    String bianxiang=getString(e,"td",1);
                    String qian=getString(e,"td",2);
                    String hou=getString(e,"td",3);

                    ps4.setString(1,tid);
                    ps4.setString(2,biantime);
                    ps4.setString(3,bianxiang);
                    ps4.setString(4,qian);
                    ps4.setString(5,hou);
                    ps4.executeUpdate();
                }
            }
        }
    }

    public static void fenzhi(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_branch div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/branch.xhtml?ps=10&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements fen=getElements(doc2,"div#_container_branch tbody tr");
            if(x>=2){
                fen=getElements(doc2,"tbody tr");
            }
            if(fen!=null){
                for(Element e:fen){
                    String qiming=getString(e,"td",0);
                    String qitid=getHref(e,"td a","href",0).replace("/company/","");
                    String faren=getString(e,"td",1);
                    String zhuang=getString(e,"td",2);
                    String zhushi=getString(e,"td",3);

                    ps5.setString(1,tid);
                    ps5.setString(2,qiming);
                    ps5.setString(3,qitid);
                    ps5.setString(4,faren);
                    ps5.setString(5,zhuang);
                    ps5.setString(6,zhushi);
                    ps5.executeUpdate();
                }
            }
        }
    }

    public static void gudong(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_holder div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/holder.xhtml?ps=20&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements gele=getElements(doc2,"div#_container_holder table.table tbody tr");
            if(x>=2){
                gele=getElements(doc2,"tbody tr");
            }
            if(gele!=null){
                for(Element e:gele){
                    String guming=getString(e,"a.in-block.vertival-middle.overflow-width",0);
                    String gtid=getHref(e,"a.in-block.vertival-middle.overflow-width","href",0).replace("/human/","").replace("/company/","");
                    String bili=getString(e,"span.c-money-y",0);
                    String renjiao=getString(e,"td",2).replace("\n","").replace(" ","");

                    ps6.setString(1,tid);
                    ps6.setString(2,guming);
                    ps6.setString(3,gtid);
                    ps6.setString(4,bili);
                    ps6.setString(5,renjiao);
                    ps6.executeUpdate();
                }
            }
        }
    }

    public static void rongzi(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements rong=getElements(doc2,"div#_container_rongzi tbody tr");
        if(rong!=null){
            for(Element e:rong){
                String shijian=getString(e,"td",0);
                String lunci=getString(e,"td",1);
                String guzhi=getString(e,"td",2);
                String jine=getString(e,"td",3);
                String bili=getString(e,"td",4);
                String touzifang=getString(e,"td",5);
                String touzitid="";
                try {
                    touzitid=e.select("td").get(5).select("a").attr("href").replace("/company/", "");
                }catch (Exception e1){
                    touzitid="";
                }
                String xinlianjie="";
                try{
                    xinlianjie=e.select("td").get(6).select("a").attr(" nofollow href");
                }catch (Exception e1){
                    xinlianjie="";
                }
                ps7.setString(1,tid);
                ps7.setString(2,shijian);
                ps7.setString(3,lunci);
                ps7.setString(4,guzhi);
                ps7.setString(5,jine);
                ps7.setString(6,bili);
                ps7.setString(7,touzifang);
                ps7.setString(8,touzitid);
                ps7.setString(9,xinlianjie);
                ps7.executeUpdate();
            }
        }
    }

    public static void hexin(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_teamMember div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/teamMember.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements hexin=getElements(doc2,"div#_container_teamMember div.team-item");
            if(x>=2){
                hexin=getElements(doc2,"div.team-item");
            }
            if(hexin!=null){
                for(Element e:hexin){
                    String logo=getHref(e,"div.img-outer img","src",0);
                    String ming=getString(e,"div.team-name",0);
                    String zhiwu=getString(e,"div.team-title",0);
                    String jianjie=getString(e,"ul",0);

                    ps8.setString(1,tid);
                    ps8.setString(2,ming);
                    ps8.setString(3,logo);
                    ps8.setString(4,zhiwu);
                    ps8.setString(5,jianjie);
                    ps8.executeUpdate();
                }
            }
        }
    }

    public static void yewu(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_firmProduct div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/firmProduct.xhtml?ps=15&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements yewu=getElements(doc2,"div#_container_firmProduct div.product-item");
            if(x>=2){
                yewu=getElements(doc2,"div.product-item");
            }
            if(yewu!=null){
                for(Element e:yewu){
                    String logo=getHref(e,"div.product-left img","src",0);
                    String pming=getString(e,"span.title",0);
                    String hangye=getString(e,"div.hangye",0);
                    String leibie=getString(e,"div.yeweu.overflow-width",0);

                    ps9.setString(1,tid);
                    ps9.setString(2,pming);
                    ps9.setString(3,logo);
                    ps9.setString(4,hangye);
                    ps9.setString(5,leibie);
                    ps9.executeUpdate();
                }
            }
        }
    }

    public static void touzishi(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_touzi div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/touzi.xhtml?ps=10&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements touzi=getElements(doc2,"div#_container_touzi tbody tr");
            if(x>=2){
                touzi=getElements(doc2,"tbody tr");
            }
            if(touzi!=null){
                for(Element e:touzi){
                    String time = getString(e, "td", 0);
                    String lunci = getString(e, "td", 1);
                    String jine = getString(e, "td", 2);
                    Elements tele = getElements(e, "td:nth-child(4) a");
                    String touzifang="";
                    String touzifangtid="";
                    StringBuffer str = new StringBuffer();
                    StringBuffer str2 = new StringBuffer();
                    if (tele != null) {
                        for (Element ee : tele) {
                            String href = getHref(ee, "a", "href", 0);
                            String mi = getString(ee, "a", 0);
                            if (StringUtils.isEmpty(mi)) {
                                mi = "kong";
                            }
                            if (StringUtils.isEmpty(href)) {
                                href = "0";
                            }
                            str.append(mi + ";");
                            str2.append(href.replace("/company/", "") + ";");
                        }
                    }
                    if(str!=null&&StringUtils.isNotEmpty(str.toString())) {
                        touzifang = str.substring(0, str.length() - 1);
                    }
                    if(str2!=null&&StringUtils.isNotEmpty(str2.toString())){
                        touzifangtid = str2.substring(0, str2.length() - 1);
                    }
                    String chanlogo = getHref(e, "td img", "src", 0);
                    String chanming = getString(e, "td", 4);
                    String chantid=getHref(e,"td:nth-child(5) a","href",0).replace("/company/","");
                    String diqu = getString(e, "td", 5);
                    String hangye = getString(e, "td", 6);
                    String yewu = getString(e, "td", 7);

                    ps10.setString(1, tid);
                    ps10.setString(2, time);
                    ps10.setString(3, lunci);
                    ps10.setString(4, jine);
                    ps10.setString(5, touzifang);
                    ps10.setString(6, touzifangtid);
                    ps10.setString(7, chanming);
                    ps10.setString(8, chanlogo);
                    ps10.setString(9, diqu);
                    ps10.setString(10, hangye);
                    ps10.setString(11, yewu);
                    ps10.setString(12,chantid);
                    ps10.executeUpdate();
                }
            }
        }
    }

    public static void jingpin(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_jingpin div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/jingpin.xhtml?ps=10&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements jing=getElements(doc2,"div#_container_jingpin tbody tr");
            if(x>=2){
                jing=getElements(doc2,"tbody tr");
            }
            if(jing!=null){
                for(Element e:jing){
                    String chanp=getString(e,"td",0);
                    String chanlogo=getHref(e,"td img","src",0);
                    String diqu=getString(e,"td",1);
                    String lunci=getString(e,"td",2);
                    String hangye=getString(e,"td",3);
                    String yewu=getString(e,"td",4);
                    String chengli=getString(e,"td",5);
                    String guzhi=getString(e,"td",6);

                    ps11.setString(1,tid);
                    ps11.setString(2,chanp);
                    ps11.setString(3,chanlogo);
                    ps11.setString(4,diqu);
                    ps11.setString(5,lunci);
                    ps11.setString(6,hangye);
                    ps11.setString(7,yewu);
                    ps11.setString(8,chengli);
                    ps11.setString(9,guzhi);
                    ps11.executeUpdate();
                }
            }
        }
    }

    public static void susong(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_lawsuit div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/lawsuit.xhtml?ps=10&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements su=getElements(doc2,"div#_container_lawsuit tbody tr");
            if(x>=2){
                su=getElements(doc2,"tbody tr");
            }
            if(su!=null){
                for(Element e:su){
                    String riqi=getString(e,"td",0);
                    String caiwen=getString(e,"td",1);
                    String cailian=getHref(e,"td a","href",0);
                    String leixing=getString(e,"td",2);
                    String hao=getString(e,"td",3);

                    ps12.setString(1,tid);
                    ps12.setString(2,riqi);
                    ps12.setString(3,cailian);
                    ps12.setString(4,leixing);
                    ps12.setString(5,hao);
                    ps12.executeUpdate();
                }
            }
        }
    }

    public static void fagong(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_court div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/court.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements gong=getElements(doc2,"div#_container_court tbody tr");
            if(x>=2){
                gong=getElements(doc2,"tbody tr");
            }
            if(gong!=null){
                for(Element e:gong){
                    String shijian=getString(e,"td",0);
                    String shangsu=getString(e,"td",1);
                    String beisu=getString(e,"td",2);
                    String leixing=getString(e,"td",3);
                    String fayuan=getString(e,"td",4);

                    ps13.setString(1,tid);
                    ps13.setString(2,shijian);
                    ps13.setString(3,shangsu);
                    ps13.setString(4,beisu);
                    ps13.setString(5,leixing);
                    ps13.setString(6,fayuan);
                    ps13.executeUpdate();
                }
            }
        }
    }

    public static void beizhixing(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_zhixing div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/zhixing.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements bei=getElements(doc2,"div#_container_zhixing tbody tr");
            if(x>=2){
                bei=getElements(doc2,"tbody tr");
            }
            if(bei!=null){
                for(Element e:bei){
                    String riqi=getString(e,"td",0);
                    String biaode=getString(e,"td",1);
                    String anhao=getString(e,"td",2);
                    String fayuan=getString(e,"td",3);

                    ps14.setString(1,tid);
                    ps14.setString(2,riqi);
                    ps14.setString(3,biaode);
                    ps14.setString(4,anhao);
                    ps14.setString(5,fayuan);
                    ps14.executeUpdate();
                }
            }
        }
    }

    public static void shixin(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements shi=getElements(doc2,"div[tyc-event-ch=CompangyDetail.shixinren] tbody tr");
        if(shi!=null){
            for(Element e:shi){
                String riqi=getString(e,"td",0);
                String anhao=getString(e,"td",1);
                String fayuan=getString(e,"td",2);
                String zhuang=getString(e,"td",3);
                String wenhao=getString(e,"td",4);

                ps15.setString(1,tid);
                ps15.setString(2,riqi);
                ps15.setString(3,anhao);
                ps15.setString(4,fayuan);
                ps15.setString(5,zhuang);
                ps15.setString(6,wenhao);
                ps15.executeUpdate();
            }
        }
    }

    public static void jingyi(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements jy=getElements(doc2,"div#_container_abnormal tbody tr");
        if(jy!=null){
            for(Element e:jy){
                String riqi=getString(e,"td",0);
                String yuanyin=getString(e,"td",1);
                String jiguan=getString(e,"td",2);
                String yiri=getString(e,"td",3);
                String yiyuan=getString(e,"td",4);
                String yijiguan=getString(e,"td",5);

                ps16.setString(1,tid);
                ps16.setString(2,riqi);
                ps16.setString(3,yuanyin);
                ps16.setString(4,jiguan);
                ps16.setString(5,yiri);
                ps16.setString(6,yiyuan);
                ps16.setString(7,yijiguan);
                ps16.executeUpdate();
            }
        }
    }

    public static void xingchu(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_punish div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/punish.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements xing=getElements(doc2,"div#_container_punish tbody tr");
            if(x>=2){
                xing=getElements(doc2,"tbody tr");
            }
            if(xing!=null){
                for(Element e:xing){
                    String riqi=getString(e,"td",0);
                    String shuwen=getString(e,"td",1);
                    String leixing=getString(e,"td",2);
                    String jiguan=getString(e,"td",3);

                    ps17.setString(1,tid);
                    ps17.setString(2,riqi);
                    ps17.setString(3,jiguan);
                    ps17.setString(4,shuwen);
                    ps17.executeUpdate();
                }
            }
        }
    }

    public static void yanwei(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements yan=getElements(doc2,"div#_container_illegal tbody tr");
        if(yan!=null){
            for(Element e:yan){
                String riqi=getString(e,"td",0);
                String yuanyin=getString(e,"td",1);
                String jiguan=getString(e,"td",2);

                ps18.setString(1,tid);
                ps18.setString(2,riqi);
                ps18.setString(3,yuanyin);
                ps18.setString(4,jiguan);
                ps18.executeUpdate();
            }
        }
    }

    public static void guquan(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements gu=getElements(doc2,"div#_container_equity tbody tr");
        if(gu!=null){
            for(Element e:gu){
                String shijian=getString(e,"td",0);
                String bianhao=getString(e,"td",1);
                String chuzhiren=getString(e,"td",2);
                String zhiquan=getString(e,"td",3);
                String zhuang=getString(e,"td",4);

                ps19.setString(1,tid);
                ps19.setString(2,shijian);
                ps19.setString(3,bianhao);
                ps19.setString(4,chuzhiren);
                ps19.setString(5,zhiquan);
                ps19.setString(6,zhuang);
                ps19.executeUpdate();
            }
        }
    }

    public static void dongchan(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_mortgage div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/mortgage.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements dong=getElements(doc2,"div#_container_mortgage tbody tr");
            if(x>=2){
                dong=getElements(doc2,"tbody tr");
            }
            if(dong!=null){
                for(Element e:dong){
                    String riqi=getString(e,"td",0);
                    String dengji=getString(e,"td",1);
                    String leixing=getString(e,"td",2);
                    String jiguan=getString(e,"td",3);
                    String zhuang=getString(e,"td",4);

                    ps20.setString(1,tid);
                    ps20.setString(2,riqi);
                    ps20.setString(3,dengji);
                    ps20.setString(4,leixing);
                    ps20.setString(5,jiguan);
                    ps20.setString(6,zhuang);
                    ps20.executeUpdate();
                }
            }
        }
    }

    public static void qianshui(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_towntax div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/towntax.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements qian=getElements(doc2,"div#_container_towntax tbody tr");
            if(x>=2){
                qian=getElements(doc2,"tbody tr");
            }
            if(qian!=null){
                for(Element e:qian){
                    String riqi=getString(e,"td",0);
                    String shibiehao=getString(e,"td",1);
                    String shuizhong=getString(e,"td",2);
                    String shuie=getString(e,"td",3);
                    String yue=getString(e,"td",4);
                    String jiguan=getString(e,"td",5);

                    ps21.setString(1,tid);
                    ps21.setString(2,riqi);
                    ps21.setString(3,shibiehao);
                    ps21.setString(4,shuizhong);
                    ps21.setString(5,shuie);
                    ps21.setString(6,yue);
                    ps21.setString(7,jiguan);
                    ps21.executeUpdate();
                }
            }
        }
    }

    public static void zhaotou(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_bid div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/bid.xhtml?ps=10&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zhaotou=getElements(doc2,"div#_container_bid tbody tr");
            if(x>=2){
                zhaotou=getElements(doc2,"tbody tr");
            }
            if(zhaotou!=null){
                for(Element e:zhaotou){
                    String shijian=getString(e,"td",0);
                    String title=getString(e,"td",1);
                    String titlelian=getHref(e, "td a", "href", 0);
                    String caigou=getString(e,"td",2);

                    ps22.setString(1,tid);
                    ps22.setString(2,shijian);
                    ps22.setString(3,title);
                    ps22.setString(4,titlelian);
                    ps22.setString(5,caigou);
                    ps22.executeUpdate();
                }
            }
        }
    }

    public static void zhaiquan(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_bond div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/bond.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zhai=getElements(doc2,"div#_container_bond tbody tr");
            if(x>=2){
                zhai=getElements(doc2,"tbody tr");
            }
            if(zhai!=null){
                for(Element e:zhai){
                    String riqi=getString(e,"td",0);
                    String ming=getString(e,"td",1);
                    String daima=getString(e,"td",2);
                    String leixing=getString(e,"td",3);
                    String pingji=getString(e,"td",4);

                    ps23.setString(1,tid);
                    ps23.setString(2,riqi);
                    ps23.setString(3,ming);
                    ps23.setString(4,daima);
                    ps23.setString(5,leixing);
                    ps23.setString(6,pingji);
                    ps23.executeUpdate();
                }
            }
        }
    }

    public static void goudi(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_purchaseland div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/purchaseland.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements goudi=getElements(doc2,"div#_container_purchaseland tbody tr");
            if(x>=2){
                goudi=getElements(doc2,"tbody tr");
            }
            if(goudi!=null){
                for(Element e:goudi){
                    String riqi=getString(e,"td",0);
                    String jianguan=getString(e,"td",1);
                    String donggong=getString(e,"td",2);
                    String mianji=getString(e,"td",3);
                    String xingzheng=getString(e,"td",4);

                    ps24.setString(1,tid);
                    ps24.setString(2,riqi);
                    ps24.setString(3,jianguan);
                    ps24.setString(4,donggong);
                    ps24.setString(5,mianji);
                    ps24.setString(6,xingzheng);
                    ps24.executeUpdate();
                }
            }
        }
    }

    public static void zhaopin(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_recruit div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/recruit.xhtml?ps=10&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zhao=getElements(doc2,"div#_container_recruit tbody tr");
            if(x>=2){
                zhao=getElements(doc2,"tbody tr");
            }
            if(zhao!=null){
                for(Element e:zhao){
                    String shijian=getString(e,"td",0);
                    String zhiwei=getString(e,"td",1);
                    String xinzi=getString(e,"td",2);
                    String jingyan=getString(e,"td",3);
                    String renshu=getString(e,"td",4);
                    String chengshi=getString(e,"td",5);

                    ps25.setString(1,tid);
                    ps25.setString(2,shijian);
                    ps25.setString(3,zhiwei);
                    ps25.setString(4,xinzi);
                    ps25.setString(5,jingyan);
                    ps25.setString(6,renshu);
                    ps25.setString(7,chengshi);
                    ps25.executeUpdate();
                }
            }
        }
    }

    public static void shuiwu(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements shui=getElements(doc2,"div#_container_taxcredit tbody tr");
        if(shui!=null){
            for(Element e:shui){
                String nian=getString(e,"td",0);
                String pingji=getString(e,"td",1);
                String leixing=getString(e,"td",2);
                String shibiehao=getString(e,"td",3);
                String danwei=getString(e,"td",4);

                ps26.setString(1,tid);
                ps26.setString(2,nian);
                ps26.setString(3,pingji);
                ps26.setString(4,leixing);
                ps26.setString(5,shibiehao);
                ps26.setString(6,danwei);
                ps26.executeUpdate();
            }
        }
    }

    public static void choujian(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements chou=getElements(doc2,"div#_container_check tbody tr");
        if(chou!=null){
            for(Element e:chou){
                String riqi=getString(e,"td",0);
                String leixing=getString(e,"td",1);
                String jieguo=getString(e,"td",2);
                String jiguan=getString(e,"td",3);

                ps27.setString(1,tid);
                ps27.setString(2,riqi);
                ps27.setString(3,leixing);
                ps27.setString(4,jieguo);
                ps27.setString(5,jiguan);
                ps27.executeUpdate();
            }
        }
    }

    public static void chanpin(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_product div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/product.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements chan=getElements(doc2,"div#_container_product tbody tr");
            if(x>=2){
                chan=getElements(doc2,"tbody tr");
            }
            if(chan!=null){
                for(Element e:chan){
                    String tubiao=getHref(e,"td img","src",0);
                    String ming=getString(e,"td",1);
                    String jian=getString(e,"td",2);
                    String fen=getString(e,"td",3);
                    String lingyu=getString(e,"td",4);

                    ps28.setString(1,tid);
                    ps28.setString(2,ming);
                    ps28.setString(3,tubiao);
                    ps28.setString(4,jian);
                    ps28.setString(5,fen);
                    ps28.setString(6,lingyu);
                    ps28.executeUpdate();
                }
            }
        }
    }

    public static void zizhi(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_qualification div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/qualification.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zi=getElements(doc2,"div#_container_qualification tbody tr");
            if(x>=2){
                zi=getElements(doc2,"tbody tr");
            }
            if(zi!=null){
                for(Element e:zi){
                    String ming=getString(e,"td",0);
                    String leixing=getString(e,"td",1);
                    String riqi=getString(e,"td",2);
                    String jieriqi=getString(e,"td",3);
                    String shebian=getString(e,"td",4);
                    String xubian=getString(e,"td",5);

                    ps29.setString(1,tid);
                    ps29.setString(2,ming);
                    ps29.setString(3,leixing);
                    ps29.setString(4,riqi);
                    ps29.setString(5,jieriqi);
                    ps29.setString(6,shebian);
                    ps29.setString(7,xubian);
                    ps29.executeUpdate();
                }
            }
        }
    }

    public static void shang(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_tmInfo div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/tmInfo.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements shang=getElements(doc2,"div#_container_tmInfo tbody tr");
            if(x>=2){
                shang=getElements(doc2,"tbody tr");
            }
            if(shang!=null){
                for(Element e:shang){
                    String riqi=getString(e,"td",0);
                    String biao=getHref(e,"td img","src",0);
                    String biaoming=getString(e,"td",2);
                    String zhuhao=getString(e,"td",3);
                    String leibie=getString(e,"td",4);
                    String zhuang=getString(e,"td",5);

                    ps30.setString(1,tid);
                    ps30.setString(2,riqi);
                    ps30.setString(3,biao);
                    ps30.setString(4,biaoming);
                    ps30.setString(5,zhuhao);
                    ps30.setString(6,leibie);
                    ps30.setString(7,zhuang);
                    ps30.executeUpdate();
                }
            }
        }
    }

    public static void zhuanli(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_patent div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/patent.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zhuan=getElements(doc2,"div#_container_patent tbody tr");
            if(x>=2){
                zhuan=getElements(doc2,"tbody tr");
            }
            if(zhuan!=null){
                for(Element e:zhuan){
                    String gong=getString(e,"td",0);
                    String ming=getString(e,"td",1);
                    String shenhao=getString(e,"td",2);
                    String shengh=getString(e,"td",3);

                    ps31.setString(1,tid);
                    ps31.setString(2,gong);
                    ps31.setString(3,ming);
                    ps31.setString(4,shenhao);
                    ps31.setString(5,shengh);
                    ps31.executeUpdate();
                }
            }
        }
    }

    public static void zhuzuo(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_copyright div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/copyright.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements zhu=getElements(doc2,"div#_container_copyright tbody tr");
            if(x>=2){
                zhu=getElements(doc2,"tbody tr");
            }
            if(zhu!=null){
                for(Element e:zhu){
                    String riqi=getString(e,"td",0);
                    String quan=getString(e,"td",1);
                    String jian=getString(e,"td",2);
                    String dengji=getString(e,"td",3);
                    String fenlei=getString(e,"td",4);
                    String banben=getString(e,"td",5);

                    ps32.setString(1,tid);
                    ps32.setString(2,riqi);
                    ps32.setString(3,quan);
                    ps32.setString(4,jian);
                    ps32.setString(5,dengji);
                    ps32.setString(6,fenlei);
                    ps32.setString(7,banben);
                    ps32.executeUpdate();
                }
            }
        }
    }

    public static void beian(Document doc,String tid,String cname) throws IOException, SQLException {
        Document doc2=doc;
        String page=getString(doc2, "div#_container_icp div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/icp.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                }
            }

            Elements bei=getElements(doc2,"div#_container_icp tbody tr");
            if(x>=2){
                bei=getElements(doc2,"tbody tr");
            }
            if(bei!=null){
                for(Element e:bei){
                    String shijian=getString(e,"td",0);
                    String ming=getString(e,"td",1);
                    String shou=getString(e,"td",2);
                    String yuming=getString(e,"td",3);
                    String beihao=getString(e,"td",4);
                    String zhuang=getString(e,"td",5);
                    String danxing=getString(e,"td",6);

                    ps33.setString(1,tid);
                    ps33.setString(2,shijian);
                    ps33.setString(3,ming);
                    ps33.setString(4,shou);
                    ps33.setString(5,yuming);
                    ps33.setString(6,beihao);
                    ps33.setString(7,zhuang);
                    ps33.setString(8,danxing);
                    ps33.executeUpdate();
                }
            }
        }
    }





}
