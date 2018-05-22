package qichacha;

import java.sql.PreparedStatement;

public class Data {
    public  String sql1;
    public  PreparedStatement ps1;
    public  String sql2;
    public  PreparedStatement ps2;
    public  String sql3;
    public  PreparedStatement ps3;
    public  String sql4;
    public  PreparedStatement ps4;
    public  String sql5;
    public  PreparedStatement ps5;
    public  String sql6;
    public  PreparedStatement ps6;
    public  String sql7;
    public  PreparedStatement ps7;
    public  String sql8;
    public  PreparedStatement ps8;
    public  String sql9;
    public  PreparedStatement ps9;
    public  String sql10;
    public  PreparedStatement ps10;
    public String sql11;
    public PreparedStatement ps11;


    public Data(){
        try {
            sql1 = "insert into tyc_gudongxin(comp_full_name,p_name,chuzi_bili,renjiao_chuzi,renjiao_riqi,gudong_type) values(?,?,?,?,?,?)";
            ps1 = Detail.conn.prepareStatement(sql1);

            sql2="insert into tyc_out_investment(comp_full_name,invest_name,representative,register_amount,register_date,investment_rate,com_status) values(?,?,?,?,?,?,?)";
            ps2=Detail.conn.prepareStatement(sql2);

            sql3="insert into tyc_recruit_information(comp_full_name,rec_time,rec_position,rec_salary,rec_city,rec_source) values(?,?,?,?,?,?)";
            ps3=Detail.conn.prepareStatement(sql3);

            sql4="insert into tyc_webcat(comp_full_name,w_logo,w_ming,w_hao,w_erwei,w_desc) values(?,?,?,?,?,?)";
            ps4=Detail.conn.prepareStatement(sql4);

            sql5="insert into tyc_company_bidding(comp_full_name,release_time,bidd_title,suoshu_diqu,xiangmu_fenlei) values(?,?,?,?,?)";
            ps5=Detail.conn.prepareStatement(sql5);

            sql6="insert into tyc_administrative_sanction(comp_full_name,sanct_time,gongshi_time,decision_org,sanct_detail,weifa_leixing,jueding_wenshuhao) values(?,?,?,?,?,?,?)";
            ps6=Detail.conn.prepareStatement(sql6);

            sql7="insert into tyc_administrative_sanction_zh(comp_full_name,jueding_wenshuhao,chufa_detail,chufa_ming,di_yu,jueding_time) values(?,?,?,?,?,?)";
            ps7=Detail.conn.prepareStatement(sql7);

            sql8="insert into tyc_change_record(comp_full_name,change_time,change_item,before_change,after_change) values(?,?,?,?,?)";
            ps8=Detail.conn.prepareStatement(sql8);

            sql9="insert into tyc_branch_info(comp_full_name,branch_name,branch_url) values(?,?,?)";
            ps9=Detail.conn.prepareStatement(sql9);

            sql10="insert into tyc_shuiwuxinyong(comp_full_name,pingjia_nian,nashui_hao,nashui_ji,ping_dan) values(?,?,?,?,?)";
            ps10=Detail.conn.prepareStatement(sql10);

            sql11="insert into tyc_caipanwenshu(comp_full_name,anjian_ming,anjian_url,fabu_time,anjian_bianhao,anjian_shen,zhixing_fayuan) values(?,?,?,?,?,?,?)";
            ps11=Detail.conn.prepareStatement(sql11);
        }catch (Exception e){

        }
    }
}
