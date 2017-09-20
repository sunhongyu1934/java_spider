package simutong.simutong_jigou;

import Utils.JsoupUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import simutong.simutong_jijin.qingQiu;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/23.
 */
public class si_shijianzuixin {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;

    static{
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException, ParseException {


        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Map<String,String> map=new HashMap<String, String>();
        map=deng();

        get(map,con);

    }

    public static Map<String,String> deng() throws IOException {
        Connection.Response doc= null;
        while (true) {
            try {
                doc = Jsoup.connect("http://pe.pedata.cn/ajaxLoginMember.action")
                        .data("param.loginName", "wang.hao@lingweispace.cn")
                        .data("param.pwd", "111111")
                        .data("param.iscs", "true")
                        .data("param.macaddress", "20-68-9D-31-CD-3C,04-7D-7B-FB-55-BA")
                        .data("param.language", "zh_CN")
                        .data("request_locale", "zh_CN")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .method(Connection.Method.POST)
                        .execute();
                if (!doc.body().contains("abuyun") && StringUtils.isNotEmpty(doc.body().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.body().contains("Forbidden")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("denglu timeout");
            }
        }
        System.out.println(doc.body());
        return doc.cookies();
    }

    public static void get(Map<String,String> map, java.sql.Connection con) throws IOException, SQLException, InterruptedException, ParseException {
        String sql="insert into si_touzi_zuixin(touzi_jigou,jigou_sid,beitou_gongsi,beitou_sid,hang_ye,diqu,jie_duan,touzi_time,touzi_jine_m,touzi_jine_usdm,gu_quan,lun_ci,gu_zhi) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";


        for(int x=1;x<=40;x++) {
            Document doc = null;
            while (true) {
                try {
                    doc = Jsoup.connect("http://pe.pedata.cn/getListInvestV2.action")
                            .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .data("param.quick", "")
                            .data("param.orderBy", "desc")
                            .data("param.orderByField", "invest_date")
                            .data("param.search_type", "base")
                            .data("param.showInfo", "true")
                            .data("param.search_type_check", "")
                            .data("param.invest_money_begin", "")
                            .data("param.invest_money_end", "")
                            .data("param.invest_stake_start", "")
                            .data("param.invest_stake_end", "")
                            .data("param.invest_enterprise_start", "")
                            .data("param.invest_enterprise_end", "")
                            .data("param.invest_date_begin", "yyyy-mm-dd")
                            .data("param.invest_date_end", "yyyy-mm-dd")
                            .data("param.epValue__start", "")
                            .data("param.epValue__end", "")
                            .data("param.epSetupDate_begin", "yyyy-mm-dd")
                            .data("param.epSetupDate_end", "yyyy-mm-dd")
                            .data("param.isTargetIpo", "")
                            .data("param.orgBackground", "")
                            .data("param.orgRecord", "")
                            .data("param.fund_record", "")
                            .data("param.fund_setup_date_begin", "yyyy-mm-dd")
                            .data("param.fund_setup_date_end", "yyyy-mm-dd")
                            .data("param.column", "1")
                            .data("param.column", "4")
                            .data("param.column", "5")
                            .data("param.column", "6")
                            .data("param.column", "7")
                            .data("param.column", "8")
                            .data("param.column", "9")
                            .data("param.column", "10")
                            .data("param.column", "11")
                            .data("param.column", "12")
                            .data("param.column", "13")
                            .data("param.currentPage", String.valueOf(x))
                            .cookies(map)
                            .proxy(proxy)
                            .post();
                    if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                        break;
                    }
                }catch (Exception e){
                    System.out.println("time out");
                }
            }

            Elements touzijigou = doc.select("div.leftTableDivID.float_left.search_width20 table.table.table-hover tbody tr");

            Elements detailele=JsoupUtils.getElements(doc,"div.leftTableDivID.float_right.search_width20 table.table.table-hover tbody tr");
            for(Element e:detailele){
                String deurl=JsoupUtils.getHref(e,"td a","href",0);
                det("http://pe.pedata.cn/"+deurl,map,proxy,con);
            }


            int p = 1;
            boolean bo = false;
            for (Element e : touzijigou) {
                PreparedStatement ps = con.prepareStatement(sql);

                Elements ele = e.select("td").get(0).select("a");
                StringBuffer str1 = new StringBuffer();
                StringBuffer str2 = new StringBuffer();
                for (Element ee : ele) {
                    str1.append(ee.text() + ";");
                    str2.append(ee.attr("href").replace("getDetailOrg.action?param.org_id=", "") + ";");
                }
                String jigou = str1.toString();
                String sid = str2.toString();
                String beitou = e.select("td").get(1).text();
                String beitouid = e.select("td").get(1).select("a").attr("href").replace("getDetailEp.action?param.ep_id=", "");

                ps.setString(1, jigou);
                ps.setString(2, sid);
                ps.setString(3, beitou);
                ps.setString(4, beitouid);


                bo=data(ps, doc, p);
                p++;
            }

            System.out.println(x+"***************************************************");
            if(bo){
                break;
            }
            Thread.sleep(5000);
        }


    }


    public static boolean data(PreparedStatement ps,Document doc,int q) throws SQLException, ParseException {
        int p=1;
        Elements eles=doc.select("div.search_width80 table.table.table-hover tbody tr");
        boolean bo=false;
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Long time=simpleDateFormat.parse(simpleDateFormat.format(date)).getTime();
        for(Element e:eles){
            String hangye=e.select("td").get(4).text();
            String diqu=e.select("td").get(5).text();
            String touzijieduan=e.select("td").get(6).text();
            String touzitime=e.select("td").get(7).text();
            String touzijinem=e.select("td").get(8).text();
            String touzijineusdm=e.select("td").get(9).text();
            String guquan=e.select("td").get(10).text();
            String lunci=e.select("td").get(11).text();
            String guzhi=e.select("td").get(12).text();
            String pe=e.select("td").get(13).text();

            if(simpleDateFormat.parse(touzitime).getTime()<time){
                bo=true;
            }

            if(p==q){
                ps.setString(5,hangye);
                ps.setString(6,diqu);
                ps.setString(7,touzijieduan);
                ps.setString(8,touzitime);
                ps.setString(9,touzijinem);
                ps.setString(10,touzijineusdm);
                ps.setString(11,guquan);
                ps.setString(12,lunci);
                ps.setString(13,guzhi);
                ps.executeUpdate();
                System.out.println("success_simutong-zuixin");
                break;
            }

            p++;
        }
        return bo;
    }

    public static void det(String url, Map<String,String> map, Proxy proxy, java.sql.Connection con) throws IOException, InterruptedException, SQLException {
        String sql="insert into si_touzizuixin_detail(s_id,s_ming,s_jijin,s_touziren,touzi_jine,gu_quan,s_touzileixing,s_tuichu,s_tuichufang,s_zhanghui,s_zhangbei) values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Document doc= qingQiu.jichuget(url,map,proxy);
        if(doc.outerHtml().contains("负债合计")){
            System.out.println(doc);
            System.exit(0);
        }

        Elements ele=JsoupUtils.getElements(doc,"form.form-horizontal table.table.table-hover tbody: tr");
        if(ele!=null) {
            int x = 0;
            for (Element e : ele) {
                String jigouming = JsoupUtils.getString(e, "td:nth-child(1)", 0);
                String jigousid = JsoupUtils.getHref(e, "td:nth-child(1) a", "href", 0).replace("getDetailOrg.action?param.org_id=", "");
                String jijin = JsoupUtils.getString(e, "td:nth-child(2)", 0);
                String touziren = JsoupUtils.getString(e, "td:nth-child(3)", 0);
                String touzijine = JsoupUtils.getString(e, "td:nth-child(4)", 0);
                String guquan = JsoupUtils.getString(e, "td:nth-child(5)", 0);
                String touzileixing = JsoupUtils.getString(e, "td:nth-child(6)", 0);
                String tuichushijian = JsoupUtils.getString(e, "td:nth-child(7)", 0);
                String tuichufangshi = JsoupUtils.getString(e, "td:nth-child(8)", 0);
                String zhangmian = JsoupUtils.getString(e, "td:nth-child(9)", 0);
                String zhangbei = JsoupUtils.getString(e, "td:nth-child(10)", 0);

                ps.setString(1, jigousid);
                ps.setString(2, jigouming);
                ps.setString(3, jijin);
                ps.setString(4, touziren);
                ps.setString(5, touzijine);
                ps.setString(6, guquan);
                ps.setString(7, touzileixing);
                ps.setString(8, tuichushijian);
                ps.setString(9, tuichufangshi);
                ps.setString(10, zhangmian);
                ps.setString(11, zhangbei);
                ps.executeUpdate();
                x++;
                System.out.println(x + "*******************************************************");
            }
        }
    }

}
