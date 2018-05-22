package tianyancha.XinxiXin;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.Producer;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static Utils.JsoupUtils.*;
import static tianyancha.XinxiXin.XinxiXin.detailget;
import static tianyancha.XinxiXin.XinxiXin.jisuan;

/**
 * Created by Administrator on 2017/7/3.
 */
public class Tyc_quan {
    private  static Connection con;
    private  PreparedStatement ps1;
    private  PreparedStatement ps2;
    private  PreparedStatement ps3;
    private  PreparedStatement ps4;
    private  PreparedStatement ps5;
    private  PreparedStatement ps6;
    private  PreparedStatement ps7;
    private  PreparedStatement ps8;
    private  PreparedStatement ps9;
    private  PreparedStatement ps10;
    private  PreparedStatement ps11;
    private  PreparedStatement ps12;
    private  PreparedStatement ps13;
    private  PreparedStatement ps14;
    private  PreparedStatement ps15;
    private  PreparedStatement ps16;
    private  PreparedStatement ps17;
    private  PreparedStatement ps18;
    private  PreparedStatement ps19;
    private  PreparedStatement ps20;
    private  PreparedStatement ps21;
    private  PreparedStatement ps22;
    private  PreparedStatement ps23;
    private  PreparedStatement ps24;
    private  PreparedStatement ps25;
    private  PreparedStatement ps26;
    private  PreparedStatement ps27;
    private  PreparedStatement ps28;
    private  PreparedStatement ps29;
    private  PreparedStatement ps30;
    private  PreparedStatement ps31;
    private  PreparedStatement ps32;
    private  PreparedStatement ps33;
    private  PreparedStatement ps34;
    private  PreparedStatement ps35;
    private String sql1;
    private String sql2;
    private String sql3;
    private String sql4;
    private String sql5;
    private String sql6;
    private String sql7;
    private String sql8;
    private String sql9;
    private String sql10;
    private String sql11;
    private String sql12;
    private String sql13;
    private String sql14;
    private String sql15;
    private String sql16;
    private String sql17;
    private String sql18;
    private String sql19;
    private String sql20;
    private String sql21;
    private String sql22;
    private String sql23;
    private String sql24;
    private String sql25;
    private String sql26;
    private String sql27;
    private String sql28;
    private String sql29;
    private String sql30;
    private String sql31;
    private String sql32;
    private String sql33;
    private String sql34;
    private String sql35;
    private Producer producer;
    //private static SAXReader saxReader;
    //private static org.dom4j.Document doo;
    public Tyc_quan(Connection con,String[] value) throws SQLException, FileNotFoundException, DocumentException {
        this.con=con;
        this.producer=new Producer(false);
        for(int x=0;x<value.length;x++){
            if(value[x].equals("基本信息")){
                this.sql1="insert into tyc_jichu_quan(quan_cheng,ceng_yongming,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,hezhun_riqi,jingying_zhuangtai,nashui_shibie,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,t_id,c_desc,en_cname) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                this.ps1=con.prepareStatement(sql1);
            }else if(value[x].equals("主要成员")){
                this.sql2="insert into tyc_main(t_id,zhi_wu,p_name,p_tid) values(?,?,?,?)";
                this.ps2=con.prepareStatement(sql2);
            }else if(value[x].equals("股东信息")){
                this.sql6="insert into tyc_gudongxin(t_id,p_name,p_tid,chuzi_bili,renjiao_chuzi) values(?,?,?,?,?)";
                this.ps6=con.prepareStatement(sql6);
            }else if(value[x].equals("对外投资")){
                this.sql3="insert into tyc_out_investment(t_id,invest_name,invest_tid,representative,rep_tid,register_amount,register_date,investment_amount,investment_rate,com_status) values(?,?,?,?,?,?,?,?,?,?)";
                this.ps3=con.prepareStatement(sql3);
            }else if(value[x].equals("变更记录")){
                this.sql4="insert into tyc_change_record(t_id,change_time,change_item,before_change,after_change) values(?,?,?,?,?)";
                this.ps4=con.prepareStatement(sql4);
            }else if(value[x].equals("分支机构")){
                this.sql5="insert into tyc_branch_info(t_id,branch_name,branch_tid,branch_legal,branch_status,reg_time) values(?,?,?,?,?,?)";
                this.ps5=con.prepareStatement(sql5);
            }else if(value[x].equals("融资历史")){
                this.sql7="insert into tyc_financing_history(t_id,fin_time,fin_round,com_valuation,fin_amount,fin_rate,fin_investor,fin_investor_tid,news_from) values(?,?,?,?,?,?,?,?,?)";
                this.ps7=con.prepareStatement(sql7);
            }else if(value[x].equals("核心团队")){
                this.sql8="insert into tyc_core_team(t_id,m_name,logo_url,m_position,m_experience) values(?,?,?,?,?)";
                this.ps8=con.prepareStatement(sql8);
            }else if(value[x].equals("企业业务")){
                this.sql9="insert into tyc_enterprise_business(t_id,p_name,logo_url,p_classfiy,p_desc) values(?,?,?,?,?)";
                this.ps9=con.prepareStatement(sql9);
            }else if(value[x].equals("投资事件")){
                this.sql10="insert into tyc_investment_event(t_id,invest_time,invest_round,invest_amount,investors,investors_tid,project_nm,project_nm_logo,invest_area,industry,p_business,project_tid) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                this.ps10=con.prepareStatement(sql10);
            }else if(value[x].equals("竞品信息")){
                this.sql11="insert into tyc_competing_information(t_id,compet_name,compet_logo,compet_area,p_round,p_industry,compet_business,create_time,p_valucation) values(?,?,?,?,?,?,?,?,?)";
                this.ps11=con.prepareStatement(sql11);
            }else if(value[x].equals("法律诉讼")){
                this.sql12="insert into tyc_legal_proceedings(t_id,legal_time,legal_document,legal_type,legal_num,legal_sf,legal_doctitle) values(?,?,?,?,?,?,?)";
                this.ps12=con.prepareStatement(sql12);
            }else if(value[x].equals("法院公告")){
                this.sql13="insert into tyc_court_notice(t_id,notice_time,accuser,defendant,notice_type,court) values(?,?,?,?,?,?)";
                this.ps13=con.prepareStatement(sql13);
            }else if(value[x].equals("被执行人")){
                this.sql14="insert into tyc_person_subjected(t_id,sub_time,sub_norm,sub_num,sub_court) values(?,?,?,?,?)";
                this.ps14=con.prepareStatement(sql14);
            }else if(value[x].equals("失信人")){
                this.sql15="insert into tyc_dishonest_person(t_id,dish_time,dish_num,dish_court,dish_status,accord_num) values(?,?,?,?,?,?)";
                this.ps15=con.prepareStatement(sql15);
            }else if(value[x].equals("经营异常")){
                this.sql16="insert into tyc_abnormal_operation(t_id,abno_time,abno_reason,decision_org,yichu_time,yichu_reason,yichu_org) values(?,?,?,?,?,?,?)";
                this.ps16=con.prepareStatement(sql16);
            }else if(value[x].equals("行政处罚")){
                this.sql17="insert into tyc_administrative_sanction(t_id,sanct_time,decision_org,sanct_lei,jue_ji,chufa_neirong,fa_ren,bei_zhu) values(?,?,?,?,?,?,?,?)";
                this.ps17=con.prepareStatement(sql17);
            }else if(value[x].equals("严重违法")){
                this.sql18="insert into tyc_serious_violation(t_id,decide_date,decide_reason,decide_org) values(?,?,?,?)";
                this.ps18=con.prepareStatement(sql18);
            }else if(value[x].equals("股权出质")){
                this.sql19="insert into tyc_stock_ownership(t_id,stock_time,stock_num,stock_name,pledgee,stock_status) values(?,?,?,?,?,?)";
                this.ps19=con.prepareStatement(sql19);
            }else if(value[x].equals("动产抵押")){
                this.sql20="insert into tyc_chattel_mortgage(t_id,registe_time,registe_num,mortgage_type,mor_shu,registe_org,mortgage_status) values(?,?,?,?,?,?,?)";
                this.ps20=con.prepareStatement(sql20);
            }else if(value[x].equals("欠税公告")){
                this.sql21="insert into tyc_tax_notice(t_id,notice_time,tax_num,tax_type,tax_amount,tax_balance,tax_org) values(?,?,?,?,?,?,?)";
                this.ps21=con.prepareStatement(sql21);
            }else if(value[x].equals("招投标")){
                this.sql22="insert into tyc_company_bidding(t_id,release_time,bidd_title,bidd_url,purchase_nm) values(?,?,?,?,?)";
                this.ps22=con.prepareStatement(sql22);
            }else if(value[x].equals("债券信息")){
                this.sql23="insert into tyc_bond_information(t_id,bond_time,bond_nm,bond_num,bond_type,bond_grade) values(?,?,?,?,?,?)";
                this.ps23=con.prepareStatement(sql23);
            }else if(value[x].equals("购地信息")){
                this.sql24="insert into tyc_purchase_information(t_id,pur_time,pur_nm,begin_date,pur_acreage,pur_district) values(?,?,?,?,?,?)";
                this.ps24=con.prepareStatement(sql24);
            }else if(value[x].equals("招聘")){
                this.sql25="insert into tyc_recruit_information(t_id,rec_time,rec_position,rec_salary,work_experience,rec_num,rec_city) values(?,?,?,?,?,?,?)";
                this.ps25=con.prepareStatement(sql25);
            }else if(value[x].equals("税务评级")){
                this.sql26="insert into tyc_tax_rating(t_id,tax_year,tax_rating,tax_type,tax_num,evaluate_org) values(?,?,?,?,?,?)";
                this.ps26=con.prepareStatement(sql26);
            }else if(value[x].equals("抽查检查")){
                this.sql27="insert into tyc_spot_check(t_id,spot_date,spot_type,spot_result,spot_org) values(?,?,?,?,?)";
                this.ps27=con.prepareStatement(sql27);
            }else if(value[x].equals("产品信息")){
                this.sql28="insert into tyc_product_information(t_id,p_name,p_logo,p_shorname,p_classfy,p_industry) values(?,?,?,?,?,?)";
                this.ps28=con.prepareStatement(sql28);
            }else if(value[x].equals("资质证书")){
                this.sql29="insert into tyc_qualification_certificate(t_id,equipment_name,cert_type,cert_date,end_date,equipment_num,cert_num) values(?,?,?,?,?,?,?)";
                this.ps29=con.prepareStatement(sql29);
            }else if(value[x].equals("商标信息")){
                this.sql30="insert into tyc_trademark_information(t_id,apply_date,trademark,trademark_nm,registe_num,trademark_type,trademark_status) values(?,?,?,?,?,?,?)";
                this.ps30=con.prepareStatement(sql30);
            }else if(value[x].equals("专利")){
                this.sql31="insert into tyc_patent_information(t_id,publish_date,patent_nm,apply_num,publish_num) values(?,?,?,?,?)";
                this.ps31=con.prepareStatement(sql31);
            }else if(value[x].equals("著作权")){
                this.sql32="insert into tyc_copyright_information(t_id,approve_date,app_name,app_shortname,registe_num,classfy_num,app_version) values(?,?,?,?,?,?,?)";
                this.ps32=con.prepareStatement(sql32);
            }else if(value[x].equals("网站备案")){
                this.sql33="insert into tyc_website_filing(t_id,audit_time,website_nm,website_url,domain_name,filing_num,web_status,comp_property) values(?,?,?,?,?,?,?,?)";
                this.ps33=con.prepareStatement(sql33);
            }else if(value[x].equals("微信公众号")){
                this.sql34="insert into tyc_webcat(t_id,w_logo,w_ming,w_hao,w_erwei,w_desc) values(?,?,?,?,?,?)";
                this.ps34=con.prepareStatement(sql34);
            } else if(value[x].equals("软件著作权")){
                this.sql35="insert into tyc_ruanzhu(t_id,r_pzrq,r_quan,r_jian,r_dengji,r_fenlei,r_banben) values(?,?,?,?,?,?,?)";
                this.ps35=con.prepareStatement(sql35);
            }
        }
    }

    /*static{
        saxReader=new SAXReader();
        try {
            shuzichu.xunlian();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            doo = saxReader.read(new FileInputStream("shu.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public static void checkPs(PreparedStatement ps,String sql) throws SQLException {
        try {
            if (ps == null || ps.isClosed()) {
                ps = con.prepareStatement(sql);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void jichu(Document doc, String tid, String cname, String zz, String zs) throws SQLException, ParseException, IOException, InterruptedException, DocumentException {
        Document doc2=doc;
        String quancheng = getString(doc2, "div.company_header_width.ie9Style h1.f18.mt0.mb0.in-block.vertival-middle.sec-c2", 0);
        if(StringUtils.isEmpty(quancheng)){
            System.out.println(doc2);
        }
        String ceng = getString(doc2, "div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4", 0);
        String phone = getString(doc2, "div.f14.sec-c2 div.in-block.vertical-top:contains(电话) span", 1);
        String email = getString(doc2, "div.f14.sec-c2 div.in-block.vertical-top:contains(邮箱) span", 1);
        String web = getString(doc2, "div.f14.sec-c2 div.in-block.vertical-top:contains(网址) a", 0);
        String address = getString(doc2, "div.f14.sec-c2 div.in-block.vertical-top:contains(地址) span", 1);
        String logo = getHref(doc2, "div.b-c-white.over-hide.mr10.ie9Style img", "src", 0);
        String zhucezibe = getString(doc2, "div.new-border-bottom:contains(注册资本) div.pb10 div.baseinfo-module-content-value", 0);
        String zhuceshijia = getString(doc2, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
        String statu = getString(doc2, "div.pt10:contains(公司状态) div.baseinfo-module-content-value", 0);
        String gongshang = getString(doc2, "div.base0910 table tbody td", 1);
        String zuzhijigou = getString(doc2, "div.base0910 table tbody td", 3);
        String tongyixinyong = getString(doc2, "div.base0910 table tbody td", 6);
        String qiyeleixing = getString(doc2, "div.base0910 table tbody td", 8);
        String nashuiren = getString(doc2, "div.base0910 table tbody td", 10);
        String hangye = getString(doc2, "div.base0910 table tbody td", 12);
        String yingyeqixian = getString(doc2, "div.base0910 table tbody td", 14);
        String hezhunriq = getString(doc2, "div.base0910 table tbody td", 16);
        String dengjijiguan = getString(doc2, "div.base0910 table tbody td", 18);
        String zhucedizhi = getString(doc, "div.base0910 table tbody td", 22).replace("附近公司","");
        String yingming=getString(doc, "div.base0910 table tbody td", 20);
        String jingyingfanwei = getString(doc, "div.base0910 table tbody td span.js-full-container.hidden", 0);
        if(!Dup.nullor(jingyingfanwei)){
            jingyingfanwei=getString(doc, "div.base0910 table tbody td span.js-full-container", 0);
        }
        String faren = getString(doc2, "div.in-block.vertical-top.pl15 div.f18.overflow-width a", 0);
        String desc = doc2.select("script#company_base_info_detail").toString().replace("<script type=\"text/html\" id=\"company_base_info_detail\">","").replace("</script>","").replace(" ","").replace("\n","");

        /*String zhuceziben=zi(zhucezibe);
        String zhuceshijian=zi(zhuceshijia);
        String hezhunriqi=zi(hezhunriq);


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        long cur=System.currentTimeMillis();
        long aur=-5364662400000L;

        if(((Dup.nullor(zhuceshijian)&&zhuceshijian.contains("."))||(Dup.nullor(hezhunriqi)&&hezhunriqi.contains("."))||(Dup.nullor(zhuceshijian)&&zhuceshijia.replaceAll("[^0-9]","").length()>8)||(Dup.nullor(hezhunriqi)&&hezhunriqi.replaceAll("[^0-9]","").length()>8)||(Dup.nullor(zhuceshijian)&&!zhuceshijian.contains("未公开")&&simpleDateFormat.parse(zhuceshijian.replaceAll("[^0-9]","")).getTime()>cur)||(Dup.nullor(hezhunriqi)&&!hezhunriqi.contains("未公开")&&simpleDateFormat.parse(hezhunriqi.replaceAll("[^0-9]","")).getTime()>cur)||(Dup.nullor(zhuceshijian)&&!zhuceshijian.contains("未公开")&&simpleDateFormat.parse(zhuceshijian.replaceAll("[^0-9]","")).getTime()<aur)||(Dup.nullor(hezhunriqi)&&!hezhunriqi.contains("未公开")&&simpleDateFormat.parse(hezhunriqi.replaceAll("[^0-9]","")).getTime()<aur))&&!zhuceshijian.contains("2099")&&!hezhunriqi.contains("2099")){
            Thread.sleep(7200000);
            int d=0;
            while (true) {
                try {
                    synchronized (this){
                        System.out.println("kaishi xunlian");
                        shuzichu.xunlian();
                        saxReader=new SAXReader();
                        doo = saxReader.read(new FileInputStream("shu.xml"));
                    }

                    zhuceziben = zi(zhucezibe);
                    zhuceshijian = zi(zhuceshijia);
                    hezhunriqi = zi(hezhunriq);
                    boolean bo=true;
                    System.out.println(zhuceshijian+"       "+hezhunriqi+"      "+quancheng);
                    if(((Dup.nullor(zhuceshijian)&&zhuceshijian.contains("."))||(Dup.nullor(hezhunriqi)&&hezhunriqi.contains("."))||(Dup.nullor(zhuceshijian)&&zhuceshijia.replaceAll("[^0-9]","").length()>8)||(Dup.nullor(hezhunriqi)&&hezhunriqi.replaceAll("[^0-9]","").length()>8)||(Dup.nullor(zhuceshijian)&&!zhuceshijian.contains("未公开")&&simpleDateFormat.parse(zhuceshijian.replaceAll("[^0-9]","")).getTime()>cur)||(Dup.nullor(hezhunriqi)&&!hezhunriqi.contains("未公开")&&simpleDateFormat.parse(hezhunriqi.replaceAll("[^0-9]","")).getTime()>cur)||(Dup.nullor(zhuceshijian)&&!zhuceshijian.contains("未公开")&&simpleDateFormat.parse(zhuceshijian.replaceAll("[^0-9]","")).getTime()<aur)||(Dup.nullor(hezhunriqi)&&!hezhunriqi.contains("未公开")&&simpleDateFormat.parse(hezhunriqi.replaceAll("[^0-9]","")).getTime()<aur))&&!zhuceshijian.contains("2099")&&!hezhunriqi.contains("2099")){
                        bo=false;
                    }
                    if(bo){
                        break;
                    }
                    }catch (Exception ee){

                }
                Thread.sleep(10000);
                d++;
                if(d>=10){
                    break;
                }
            }
        }*/

        /*checkPs(ps1,sql1);

        ps1.setString(1,quancheng);
        ps1.setString(2,ceng);
        ps1.setString(3,logo);
        ps1.setString(4,phone);
        ps1.setString(5,email);
        ps1.setString(6,address);
        ps1.setString(7,web);
        ps1.setString(8,faren);
        ps1.setString(9,zz);
        ps1.setString(10,zs);
        ps1.setString(11, "");
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
        ps1.setString(24,desc);
        ps1.setString(25,yingming);
        ps1.executeUpdate();*/

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("comp_id","createid=comp_full_name");
        jsonObject.put("comp_full_name",quancheng);
        jsonObject.put("comp_english_name",yingming);
        jsonObject.put("comp_credit_code",tongyixinyong);
        jsonObject.put("comp_type",qiyeleixing);
        jsonObject.put("comp_corporation",faren);
        jsonObject.put("comp_reg_captital",zz);
        jsonObject.put("comp_create_date",zs);
        jsonObject.put("comp_reg_authority",dengjijiguan);
        jsonObject.put("comp_org_code",zuzhijigou);
        jsonObject.put("comp_registe_num",gongshang);
        jsonObject.put("comp_bus_duration",yingyeqixian);
        jsonObject.put("comp_issue_date","");
        jsonObject.put("comp_bus_range",jingyingfanwei);
        jsonObject.put("comp_taxpayer_num",nashuiren);
        jsonObject.put("comp_opreat_status",statu);
        jsonObject.put("comp_off_addr",address);
        jsonObject.put("comp_industry",hangye);
        jsonObject.put("rowkey","comp_full_name+comp_full_name###familyname");
        jsonObject.put("tablename","company_base_info");
        jsonObject.put("familyname","tyc");

        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("comp_id","createid=comp_full_name");
        jsonObject1.put("comp_full_name",quancheng);
        jsonObject1.put("comp_alias",ceng);
        jsonObject1.put("rowkey","comp_full_name+comp_full_name###comp_alias###familyname");
        jsonObject1.put("tablename","company_alias");
        jsonObject1.put("familyname","tyc");

        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("comp_id","createid=comp_full_name");
        jsonObject2.put("comp_full_name",quancheng);
        jsonObject2.put("comp_address",zhucedizhi);
        jsonObject2.put("rowkey","comp_full_name+comp_full_name###familyname");
        jsonObject2.put("tablename","company_area_info");
        jsonObject2.put("familyname","tyc");

        JSONObject jsonObject3=new JSONObject();
        jsonObject3.put("comp_id","createid=comp_full_name");
        jsonObject3.put("comp_full_name",quancheng);
        jsonObject3.put("comp_logo",logo);
        jsonObject3.put("rowkey","comp_full_name+comp_full_name###familyname");
        jsonObject3.put("tablename","company_logo");
        jsonObject3.put("familyname","tyc");

        JSONObject jsonObject4=new JSONObject();
        jsonObject4.put("comp_id","createid=comp_full_name");
        jsonObject4.put("comp_full_name",quancheng);
        jsonObject4.put("comp_phone",phone);
        jsonObject4.put("comp_email",email);
        jsonObject4.put("rowkey","comp_full_name+comp_full_name###comp_phone###comp_email###comp_fax###comp_linkman###familyname");
        jsonObject4.put("tablename","company_contact");
        jsonObject4.put("familyname","tyc");

        JSONObject jsonObject5=new JSONObject();
        jsonObject5.put("comp_id","createid=comp_full_name");
        jsonObject5.put("comp_full_name",quancheng);
        jsonObject5.put("comp_introduction",desc);
        jsonObject5.put("rowkey","comp_full_name+comp_full_name###familyname");
        jsonObject5.put("tablename","company_introduction");
        jsonObject5.put("familyname","tyc");

        JSONObject jsonObject6=new JSONObject();
        jsonObject6.put("comp_id","createid=comp_full_name");
        jsonObject6.put("comp_full_name",quancheng);
        jsonObject6.put("comp_web",web);
        jsonObject6.put("rowkey","comp_full_name+comp_full_name###familyname");
        jsonObject6.put("tablename","company_web");
        jsonObject6.put("familyname","tyc");


        producer.send("ControlTotal",jsonObject.toString());
        producer.send("ControlTotal",jsonObject1.toString());
        producer.send("ControlTotal",jsonObject2.toString());
        producer.send("ControlTotal",jsonObject3.toString());
        producer.send("ControlTotal",jsonObject4.toString());
        producer.send("ControlTotal",jsonObject5.toString());
        producer.send("ControlTotal",jsonObject6.toString());
        System.out.println("success_tyc-quan");
    }

    /*public static String zi(String key) throws FileNotFoundException, DocumentException {
        List<org.dom4j.Element> list=doo.getRootElement().elements("shu");

        if(Dup.nullor(key)){
            String keys[]=key.split("|");
            StringBuffer str=new StringBuffer();
            for(String s:keys){
                boolean bo=true;
                for(org.dom4j.Element e:list){
                    if(s.equals(e.getText())){
                        str.append(s.replace(e.getText(),e.attributeValue("yuan")));
                        bo=false;
                    }
                }
                if(bo){
                    str.append(s);
                }
            }

            return str.toString();
        }else {
            return null;
        }
    }*/

    public void zhuyao(Document doc,String tid,String cname) throws SQLException, UnsupportedEncodingException {
        Document doc2=doc;
        Elements zele=getElements(doc2,"div#_container_staff tbody tr");
        if(zele!=null){
            for(Element e:zele){
                try {
                    String zhiwu = getString(e, "td", 2);
                    String ming = getString(e, "td", 1).split(" ")[0];

                /*checkPs(ps2,sql2);

                ps2.setString(1,tid);
                ps2.setString(2,zhiwu);
                ps2.setString(3,ming);
                ps2.setString(4,mtid);
                ps2.executeUpdate();*/

                    String cname2 = URLDecoder.decode(cname, "utf-8");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("comp_id", "createid=comp_full_name");
                    jsonObject.put("comp_full_name", cname2);
                    jsonObject.put("member_name", ming);
                    jsonObject.put("member_position", zhiwu);
                    jsonObject.put("rowkey", "comp_full_name+comp_full_name###member_name###familyname");
                    jsonObject.put("tablename", "company_team_info");
                    jsonObject.put("familyname", "tyc");
                    producer.send("ControlTotal", jsonObject.toString());
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }
    }
    public void duiwaitouzi(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_invest div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n","").replace("\"","").replaceAll("[^0-9]","");;
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++) {
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/invest.xhtml?ps=20&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    Elements duiwai = getElements(doc2, "tbody tr");
                    if(duiwai!=null&&StringUtils.isNotEmpty(duiwai.toString())){
                        break;
                    }
                    ai++;
                    if(ai>=20){
                        break;
                    }
                }
            }
            Elements duiwai = getElements(doc, "div#_container_invest tbody tr");
            if (duiwai != null) {
                for (Element e : duiwai) {
                    try {
                        String gongsiming = getString(e, "td", 1);
                        String gongsitid = getHref(e, "td a.query_name", "href", 0).replace("/company/", "");
                        String beitouren = getString(e, "td", 2).split("他")[0];
                        String beitourentid = getHref(e, "td span a.point.new-c4", "href", 0).replace("/human/", "");
                        String zhuceziben = getString(e, "td", 3);
                        String touzishue = getString(e, "td", 4);
                        String touzizhanbi = getString(e, "td", 5);
                        String zhuceshijian = getString(e, "td", 6);
                        String zhuangtai = getString(e, "td", 7);

                        if (StringUtils.isNotEmpty(gongsiming)) {
                        /*checkPs(ps3,sql3);

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
                        ps3.executeUpdate();*/

                            String cname2 = URLDecoder.decode(cname, "utf-8");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("comp_id", "createid=comp_full_name");
                            jsonObject.put("comp_full_name", cname2);
                            jsonObject.put("invest_name", gongsiming);
                            jsonObject.put("invest_comp_id", "createid=invest_name");
                            jsonObject.put("invest_amount", touzishue);
                            jsonObject.put("invest_rate", touzizhanbi);
                            jsonObject.put("regist_date", zhuceshijian);
                            jsonObject.put("regist_amount", zhuceziben);
                            jsonObject.put("invest_corporation", beitouren);
                            jsonObject.put("invest_comp_status", zhuangtai);
                            jsonObject.put("rowkey", "comp_full_name+comp_full_name###invest_name###familyname");
                            jsonObject.put("tablename", "company_invest_info");
                            jsonObject.put("familyname", "tyc");
                            producer.send("ControlTotal", jsonObject.toString());
                        }
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void biangeng(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_changeinfo div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/changeinfo.xhtml?ps=10&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
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
                    try {
                        String biantime = getString(e, "td", 1);
                        String bianxiang = getString(e, "td", 2);
                        String qian = getString(e, "td", 3);
                        String hou = getString(e, "td", 4);

                    /*checkPs(ps4,sql4);

                    ps4.setString(1,tid);
                    ps4.setString(2,biantime);
                    ps4.setString(3,bianxiang);
                    ps4.setString(4,qian);
                    ps4.setString(5,hou);
                    ps4.executeUpdate();*/

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("change_date", biantime);
                        jsonObject.put("change_content", bianxiang);
                        jsonObject.put("before_change", qian);
                        jsonObject.put("after_change", hou);
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###change_date###change_content###familyname");
                        jsonObject.put("tablename", "company_business_change");
                        jsonObject.put("familyname", "tyc");
                        producer.send("ControlTotal", jsonObject.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void fenzhi(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps5,sql5);

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

    public void gudong(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_holder div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "").replace("\"","").replaceAll("[^0-9]","");;
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/holder.xhtml?ps=20&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
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
                    try {
                        String guming = getString(e, "td", 1).split(" ")[0];
                        String gtid = getHref(e, "a.in-block.vertival-middle.overflow-width", "href", 0).replace("/human/", "").replace("/company/", "");
                        String bili = getString(e, "td", 2);
                        String renjiao = getString(e, "td", 3).replace("\n", "").replace(" ", "");

                    /*checkPs(ps6,sql6);

                    ps6.setString(1,tid);
                    ps6.setString(2,guming);
                    ps6.setString(3,gtid);
                    ps6.setString(4,bili);
                    ps6.setString(5,renjiao);
                    ps6.executeUpdate();*/

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("shareholder_name", guming);
                        jsonObject.put("subscribe_amount", renjiao);
                        jsonObject.put("contribution_rate", bili);
                        jsonObject.put("shareholder_id", "createid=shareholder_name");
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###shareholder_name###familyname");
                        jsonObject.put("tablename", "company_shareholder_info");
                        jsonObject.put("familyname", "tyc");
                        producer.send("ControlTotal", jsonObject.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void rongzi(Document doc,String tid,String cname) throws SQLException {
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

                checkPs(ps7,sql7);

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

    public void hexin(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_teamMember div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "").replace("\"","").replaceAll("[^0-9]","");;
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/teamMember.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
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
                    try {
                        String logo = getHref(e, "div.img-outer img", "src", 0);
                        String ming = getString(e, "div.team-name", 0);
                        String zhiwu = getString(e, "div.team-title", 0);
                        String jianjie = getString(e, "ul", 0);

                    /*checkPs(ps8,sql8);

                    ps8.setString(1,tid);
                    ps8.setString(2,ming);
                    ps8.setString(3,logo);
                    ps8.setString(4,zhiwu);
                    ps8.setString(5,jianjie);
                    ps8.executeUpdate();*/

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("member_name", ming);
                        jsonObject.put("member_position", zhiwu);
                        jsonObject.put("member_intro", jianjie);
                        jsonObject.put("member_logo", logo);
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###member_name###familyname");
                        jsonObject.put("tablename", "company_team_info");
                        jsonObject.put("familyname", "tyc");
                        producer.send("ControlTotal", jsonObject.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void yewu(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps9,sql9);

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

    public void touzishi(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_touzi div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("http://www.tianyancha.com/pagination/touzi.xhtml?ps=10&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
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

                    checkPs(ps10,sql10);

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

    public void jingpin(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps11,sql11);

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

    public void susong(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    String hao=getString(e,"td",4);
                    String sf=getString(e,"td",3);

                    checkPs(ps12,sql12);

                    ps12.setString(1,tid);
                    ps12.setString(2,riqi);
                    ps12.setString(3,cailian);
                    ps12.setString(4,leixing);
                    ps12.setString(5,hao);
                    ps12.setString(6,sf);
                    ps12.setString(7,caiwen);
                    ps12.executeUpdate();
                }
            }
        }
    }

    public void fagong(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps13,sql13);

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

    public void beizhixing(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps14,sql14);

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

    public void shixin(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements shi=getElements(doc2,"div[tyc-event-ch=CompangyDetail.shixinren] tbody tr");
        if(shi!=null){
            for(Element e:shi){
                String riqi=getString(e,"td",0);
                String anhao=getString(e,"td",1);
                String fayuan=getString(e,"td",2);
                String zhuang=getString(e,"td",3);
                String wenhao=getString(e,"td",4);

                checkPs(ps15,sql15);

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

    public void jingyi(Document doc,String tid,String cname) throws SQLException {
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

                checkPs(ps16,sql16);

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

    public void xingchu(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    try {
                        String json = JsoupUtils.getElement(e, "td script", 0).toString().replace("<script type=\"text/html\">", "").replace("</script>", "");
                        JSONObject jsonObject = new JSONObject(json);
                        String wenshu = getValue(jsonObject,"punishNumber");
                        String lei = getValue(jsonObject,"type");
                        String chufaneirong = getValue(jsonObject,"content");
                        String juedingri = getValue(jsonObject,"decisionDate");
                        String juedingji = getValue(jsonObject,"departmentName");
                        String faren = getValue(jsonObject,"legalPersonName");
                        String beizhu = "所报材料真实合法，一切责任由当事人自负。";

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("rowkey", "comp_full_name+comp_full_name###decision_document###familyname");
                        jsonObject1.put("familyname", "tyc");
                        jsonObject1.put("tablename", "company_adm_sanction");
                        jsonObject1.put("comp_id", "createid=comp_full_name");
                        jsonObject1.put("comp_full_name", cname2);
                        jsonObject1.put("sanct_time", juedingri);
                        jsonObject1.put("decision_document", wenshu);
                        jsonObject1.put("sanct_type", lei);
                        jsonObject1.put("decision_org", juedingji);
                        jsonObject1.put("sanct_content", chufaneirong);
                        jsonObject1.put("comp_corporation", faren);
                        jsonObject1.put("sanct_remark", beizhu);
                        producer.send("ControlTotal", jsonObject1.toString());



                    /*checkPs(ps17,sql17);

                    ps17.setString(1,tid);
                    ps17.setString(2,juedingri);
                    ps17.setString(3,wenshu);
                    ps17.setString(4,lei);
                    ps17.setString(5,juedingji);
                    ps17.setString(6,chufaneirong);
                    ps17.setString(7,faren);
                    ps17.setString(8,beizhu);
                    ps17.executeUpdate();*/
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void yanwei(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements yan=getElements(doc2,"div#_container_illegal tbody tr");
        if(yan!=null){
            for(Element e:yan){
                String riqi=getString(e,"td",0);
                String yuanyin=getString(e,"td",1);
                String jiguan=getString(e,"td",2);

                checkPs(ps18,sql18);

                ps18.setString(1,tid);
                ps18.setString(2,riqi);
                ps18.setString(3,yuanyin);
                ps18.setString(4,jiguan);
                ps18.executeUpdate();
            }
        }
    }

    public void guquan(Document doc,String tid,String cname) throws SQLException, IOException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div#_container_equity div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "");
        if(StringUtils.isEmpty(page)){
            page="1";
        }
        for(int x=1;x<=Integer.parseInt(page);x++) {
            if (x >= 2) {
                while (true) {
                    Map<String, Object> map = jisuan(cname);
                    doc2 = detailget("https://www.tianyancha.com/pagination/equity.xhtml?ps=5&pn=" + x + "&name=" + cname + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if (doc2 != null && !doc2.outerHtml().contains("Unauthorized")) {
                        break;
                    }
                }
            }
            Elements gu = getElements(doc2, "div#_container_equity tbody tr");
            if(x>=2){
                gu = getElements(doc2, "tbody tr");
            }
            if (gu != null) {
                for (Element e : gu) {
                    try {
                        String json = JsoupUtils.getElement(e, "td script", 0).toString().replace("<script type=\"text/html\">", "").replace("</script>", "");
                        JSONObject jsonObject1 = new JSONObject(json);
                        String shijian = getString(e, "td", 1);
                        String dengjihao = getValue(jsonObject1, "regNumber");
                        String zhuang = getValue(jsonObject1, "state");
                        String shue = getValue(jsonObject1, "equityAmount");
                        String churen = getValue(jsonObject1, "pledgorStr");
                        String churenhao = getValue(jsonObject1, "certifNumberR");
                        String zhuquanren = Dup.nullor(getValue(jsonObject1, "pledgeeStr"))
                                ? JsoupUtils.getString(Jsoup.parse(getValue(jsonObject1, "pledgeeStr")), "a", 0)
                                : null;
                        String zhurenhao = getValue(jsonObject1, "certifNumber");
                        String beizhu = "所报材料真实合法，一切责任由当事人自负。";

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###stock_num###familyname");
                        jsonObject.put("familyname", "tyc");
                        jsonObject.put("tablename", "company_stock_ownership");
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("stock_num", dengjihao);
                        jsonObject.put("regist_date", shijian);
                        jsonObject.put("stock_name", churen);
                        jsonObject.put("stock_certificate_num", churenhao);
                        jsonObject.put("stock_time", shijian);
                        jsonObject.put("pledgee", zhuquanren);
                        jsonObject.put("stock_status", zhuang);
                        jsonObject.put("stock_amount", shue);
                        jsonObject.put("remark", beizhu);
                        jsonObject.put("pledgee_num", zhurenhao);
                        producer.send("ControlTotal", jsonObject.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }

                    /*checkPs(ps19, sql19);

                    ps19.setString(1, tid);
                    ps19.setString(2, shijian);
                    ps19.setString(3, bianhao);
                    ps19.setString(4, chuzhiren);
                    ps19.setString(5, zhiquan);
                    ps19.setString(6, zhuang);
                    ps19.executeUpdate();*/
                }
            }
        }
    }

    public static String getValue(JSONObject jsonObject,String key){
        try{
            return jsonObject.get(key).toString();
        }catch (Exception e){
            return "";
        }
    }

    public static JSONObject getValueArray(JSONArray jsonObject, int a){
        try{
            return jsonObject.getJSONObject(a);
        }catch (Exception e){
            return null;
        }
    }

    public static JSONObject getValueObject(JSONObject jsonObject, String key){
        try{
            return jsonObject.getJSONObject(key);
        }catch (Exception e){
            return null;
        }
    }

    public void dongchan(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    try {
                        String riqi = getString(e, "td", 1);
                        String json = JsoupUtils.getElement(e, "td script", 0).toString().replace("<script type=\"text/html\">", "").replace("</script>", "");
                        JSONObject jsonObject1 = new JSONObject(json);

                        String dengjihao = getValue(getValueObject(jsonObject1,"baseInfo"), "regNum");
                        String dengjiji = getValue(getValueObject(jsonObject1,"baseInfo"), "regDepartment");
                        String zhuang = getValue(getValueObject(jsonObject1,"baseInfo"), "status");
                        String beiquanlei = getValue(getValueObject(jsonObject1,"baseInfo"), "type");
                        String beiquanshu = getValue(getValueObject(jsonObject1,"baseInfo"), "overviewAmount");
                        String qixian = getValue(getValueObject(jsonObject1,"baseInfo"), "term");
                        String fanwei = getValue(getValueObject(jsonObject1,"baseInfo"), "scope");
                        String beizhu = "所报材料真实合法，一切责任由当事人自负。";
                        String diyaren = getValue(getValueArray(jsonObject1.getJSONArray("peopleInfo"),0), "peopleName");
                        String diyalei = getValue(getValueArray(jsonObject1.getJSONArray("peopleInfo"),0), "liceseType");
                        String diyahao = getValue(getValueArray(jsonObject1.getJSONArray("peopleInfo"),0), "licenseNum");
                        String mingcheng = getValue(getValueArray(jsonObject1.getJSONArray("pawnInfoList"),0), "pawnName");
                        String suiquangui = getValue(getValueArray(jsonObject1.getJSONArray("pawnInfoList"),0), "ownership");
                        String shuzhiz = getValue(getValueArray(jsonObject1.getJSONArray("pawnInfoList"),0), "detail");
                        String beizhu2 = getValue(getValueArray(jsonObject1.getJSONArray("pawnInfoList"),0), "remark");
                        String zhuxiao = getValue(getValueObject(jsonObject1,"baseInfo"),"cancelReason");


                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###registe_num###familyname");
                        jsonObject.put("familyname", "tyc");
                        jsonObject.put("tablename", "company_chattel_mortgage");
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("registe_time", riqi);
                        jsonObject.put("registe_num", dengjihao);
                        jsonObject.put("mortgage_type", beiquanlei);
                        jsonObject.put("mortgage_amount", beiquanshu);
                        jsonObject.put("registe_org", dengjiji);
                        jsonObject.put("mortgage_status", zhuang);
                        jsonObject.put("debt_maturity", qixian);
                        jsonObject.put("scope_security", fanwei);
                        jsonObject.put("remark1", beizhu);
                        jsonObject.put("mortgage_name", diyaren);
                        jsonObject.put("card_type", diyalei);
                        jsonObject.put("card_number", diyahao);
                        jsonObject.put("mortgage_content", mingcheng);
                        jsonObject.put("ownership", suiquangui);
                        jsonObject.put("mortgage_desc", shuzhiz);
                        jsonObject.put("remark2", beizhu2);
                        jsonObject.put("cancel", zhuxiao);
                        producer.send("ControlTotal", jsonObject.toString());
                    /*checkPs(ps20,sql20);

                    ps20.setString(1,tid);
                    ps20.setString(2,riqi);
                    ps20.setString(3,dengji);
                    ps20.setString(4,leixing);
                    ps20.setString(5,shue);
                    ps20.setString(6,jiguan);
                    ps20.setString(7,zhuang);
                    ps20.executeUpdate();*/
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void qianshui(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps21,sql21);

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

    public void zhaotou(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    try {
                        String shijian = getString(e, "td", 1);
                        String title = getString(e, "td", 2);
                        String titlelian = "https://www.tianyancha.com" + getHref(e, "td a", "href", 0);
                        String caigou = getString(e, "td", 3);

                    /*checkPs(ps22,sql22);

                    ps22.setString(1,tid);
                    ps22.setString(2,shijian);
                    ps22.setString(3,title);
                    ps22.setString(4,titlelian);
                    ps22.setString(5,caigou);
                    ps22.executeUpdate();*/

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("title", title);
                        jsonObject.put("title_url", titlelian);
                        jsonObject.put("purchasing_unit", caigou);
                        jsonObject.put("Issue_date", shijian);
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###bidd_title###familyname");
                        jsonObject.put("tablename", "company_bidding_news");
                        jsonObject.put("familyname", "tyc");
                        producer.send("ControlTotal", jsonObject.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void zhaiquan(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps23,sql23);

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

    public void goudi(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps24,sql24);

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

    public void zhaopin(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps25,sql25);

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

    public void shuiwu(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements shui=getElements(doc2,"div#_container_taxcredit tbody tr");
        if(shui!=null){
            for(Element e:shui){
                String nian=getString(e,"td",0);
                String pingji=getString(e,"td",1);
                String leixing=getString(e,"td",2);
                String shibiehao=getString(e,"td",3);
                String danwei=getString(e,"td",4);

                checkPs(ps26,sql26);

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

    public void choujian(Document doc,String tid,String cname) throws SQLException {
        Document doc2=doc;
        Elements chou=getElements(doc2,"div#_container_check tbody tr");
        if(chou!=null){
            for(Element e:chou){
                String riqi=getString(e,"td",0);
                String leixing=getString(e,"td",1);
                String jieguo=getString(e,"td",2);
                String jiguan=getString(e,"td",3);

                checkPs(ps27,sql27);

                ps27.setString(1,tid);
                ps27.setString(2,riqi);
                ps27.setString(3,leixing);
                ps27.setString(4,jieguo);
                ps27.setString(5,jiguan);
                ps27.executeUpdate();
            }
        }
    }

    public void chanpin(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    try {
                        String tubiao = getHref(e, "td img", "src", 0);
                        String ming = getString(e, "td", 1);
                        String jian = getString(e, "td", 2);
                        String fen = getString(e, "td", 3);
                        String lingyu = getString(e, "td", 4);

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rowkey", "app_name+app_name###develop_name###app_package###familyname");
                        jsonObject.put("familyname", "tyc");
                        jsonObject.put("tablename", "app_base_info");
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("app_id", "createid=app_name###develop_name");
                        jsonObject.put("app_name", ming);
                        jsonObject.put("app_short_name", jian);
                        jsonObject.put("develop_name", cname2);
                        jsonObject.put("app_logo", tubiao);
                        jsonObject.put("app_sort", fen);
                        jsonObject.put("app_tag", lingyu);
                        producer.send("ControlTotal", jsonObject.toString());

                    /*checkPs(ps28,sql28);

                    ps28.setString(1,tid);
                    ps28.setString(2,ming);
                    ps28.setString(3,tubiao);
                    ps28.setString(4,jian);
                    ps28.setString(5,fen);
                    ps28.setString(6,lingyu);
                    ps28.executeUpdate();*/
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void zizhi(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps29,sql29);

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

    public void shang(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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
                    try {
                        String riqi = getString(e, "td", 0);
                        String biao = getHref(e, "td img", "src", 0);
                        String biaoming = getString(e, "td", 2);
                        String zhuhao = getString(e, "td", 3);
                        String leibie = getString(e, "td", 4);
                        String zhuang = getString(e, "td", 5);

                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###registe_num###familyname");
                        jsonObject.put("familyname", "tyc");
                        jsonObject.put("tablename", "company_trademark_information");
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("apply_date", riqi);
                        jsonObject.put("trademark", biao);
                        jsonObject.put("trademark_nm", biaoming);
                        jsonObject.put("registe_num", zhuhao);
                        jsonObject.put("trademark_type", leibie);
                        jsonObject.put("trademark_status", zhuang);
                        producer.send("ControlTotal", jsonObject.toString());


                    /*checkPs(ps30,sql30);

                    ps30.setString(1,tid);
                    ps30.setString(2,riqi);
                    ps30.setString(3,biao);
                    ps30.setString(4,biaoming);
                    ps30.setString(5,zhuhao);
                    ps30.setString(6,leibie);
                    ps30.setString(7,zhuang);
                    ps30.executeUpdate();*/
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }

    public void zhuanli(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps31,sql31);

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

    public void zhuzuo(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps32,sql32);

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

    public void beian(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
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

                    checkPs(ps33,sql33);

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


    public void gongzhonghao(Document doc,String tid,String cname) throws IOException, SQLException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div.wechat div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "").replace("\"","").replaceAll("[^0-9]","");;
        if(StringUtils.isEmpty(page)){
            page="1";
        }

        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/wechat.xhtml?ps=10&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
                        break;
                    }
                }
            }

            Elements bei=getElements(doc2,"div.wechat div.mb10.in-block");
            if(x>=2){
                bei=getElements(doc2,"div.mb10.in-block");
            }
            if(bei!=null){
                for(Element e:bei){
                    try {
                        String logo = getHref(e, "div.in-block.vertical-top.wechatImg img", "src", 0);
                        String ming = getString(e, "div.in-block.vertical-top.itemRight div.mb5", 0);
                        String hao = getString(e, "div.in-block.vertical-top.itemRight div.mb5:nth-child(2) span.in-block.vertical-top", 1);
                        String erweima = getHref(e, "div.in-block.vertical-top.itemRight div.mb5:nth-child(2) div.position-abs.erweimaBox img", "src", 0);
                        String jieshao = getString(e, "span.overflow-width.in-block.vertical-top", 0);

                    /*checkPs(ps34,sql34);

                    ps34.setString(1,tid);
                    ps34.setString(2,logo);
                    ps34.setString(3,ming);
                    ps34.setString(4,hao);
                    ps34.setString(5,erweima);
                    ps34.setString(6,jieshao);
                    ps34.executeUpdate();*/


                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("comp_id", "createid=comp_full_name");
                        jsonObject2.put("pub_id", "createid=pub_name");
                        jsonObject2.put("pub_name", ming);
                        jsonObject2.put("comp_full_name", cname2);
                        jsonObject2.put("pub_intro", jieshao);
                        jsonObject2.put("pub_logo", logo);
                        jsonObject2.put("wechart_num", hao);
                        jsonObject2.put("qr_code_url", erweima);
                        jsonObject2.put("rowkey", "comp_full_name+comp_full_name###pub_name###familyname");
                        jsonObject2.put("tablename", "public_account_info");
                        jsonObject2.put("familyname", "tyc");
                        producer.send("ControlTotal", jsonObject2.toString());
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }

    }

    public void ruanzhu(Document doc,String tid,String cname) throws SQLException, IOException, InterruptedException {
        Document doc2=doc;
        String page=getString(doc2,"div.copyright div.total:contains(共)",0).replace("共","").replace("页","").replace(" ","").replace("\n", "").replace("\"","").replaceAll("[^0-9]","");
        if(StringUtils.isEmpty(page)){
            page="1";
        }

        for(int x=1;x<=Integer.parseInt(page);x++){
            if(x>=2){
                int ai=0;
                while (true) {
                    Map<String, Object> map = jisuan(tid);
                    doc2 = detailget("http://www.tianyancha.com/pagination/copyright.xhtml?ps=5&pn=" + x + "&id=" + tid + "&_=" + map.get("time"), (Map<String, String>) map.get("cookie"));
                    if(doc2!=null&&!doc2.outerHtml().contains("Unauthorized")){
                        break;
                    }
                    ai++;
                    if(ai>=20){
                        break;
                    }
                }
            }

            Elements ruele= JsoupUtils.getElements(doc,"div.copyright tbody tr");
            if(x>=2){
                ruele=getElements(doc2,"tbody tr");
            }
            if(ruele!=null){
                for(Element e:ruele){
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String json = JsoupUtils.getElement(e, "td script", 0).toString().replace("<script type=\"text/html\">", "").replace("</script>", "");
                        JSONObject jsonObject1 = new JSONObject(json);
                        String ruanquan = getValue(jsonObject1, "fullname");
                        String ruanjian = "";
                        String fenhao = getValue(jsonObject1, "catnum");
                        String dengjihao = getValue(jsonObject1, "regnum");
                        String banben = getValue(jsonObject1, "version");
                        String zhuren = getValue(jsonObject1, "authorNationality");
                        String shoufa = "";
                        String dengri = "";
                        try {
                            shoufa = simpleDateFormat.format(new Date(Long.parseLong(getValue(jsonObject1, "publishtime"))));
                        } catch (Exception ee) {

                        }
                        try {
                            dengri = simpleDateFormat.format(new Date(Long.parseLong(getValue(jsonObject1, "regtime"))));
                        } catch (Exception ee) {

                        }


                        String cname2 = URLDecoder.decode(cname, "utf-8");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rowkey", "comp_full_name+comp_full_name###regist_num###familyname");
                        jsonObject.put("familyname", "tyc");
                        jsonObject.put("tablename", "company_software_copyright");
                        jsonObject.put("comp_id", "createid=comp_full_name");
                        jsonObject.put("comp_full_name", cname2);
                        jsonObject.put("approve_date", dengri);
                        jsonObject.put("copyrigth_owner", zhuren);
                        jsonObject.put("publish_date", shoufa);
                        jsonObject.put("soft_full_name", ruanquan);
                        jsonObject.put("soft_short_name", ruanjian);
                        jsonObject.put("regist_num", dengjihao);
                        jsonObject.put("classify_num", fenhao);
                        jsonObject.put("soft_version", banben);
                        jsonObject.put("regist_date", dengri);
                        producer.send("ControlTotal", jsonObject.toString());


                    /*checkPs(ps35,sql35);

                    ps35.setString(1,tid);
                    ps35.setString(2,piri);
                    ps35.setString(3,rquan);
                    ps35.setString(4,rjian);
                    ps35.setString(5,dengji);
                    ps35.setString(6,fenhao);
                    ps35.setString(7,banben);
                    ps35.executeUpdate();*/
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }
            }
        }
    }





}
