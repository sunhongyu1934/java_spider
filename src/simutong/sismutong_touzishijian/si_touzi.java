package simutong.sismutong_touzishijian;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/5.
 */
public class si_touzi {
    // 代理隧道验证信息
    final static String ProxyUser = "H4TL2M827AIJ963D";
    final static String ProxyPass = "81C9D64628A60CF9";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        serach(con,proxy);

    }

    public static Map<String,String> denglu(Proxy proxy) throws IOException {
        Connection.Response doc= null;
        while (true) {
            try {
                doc = Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/user/login?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone5S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3")
                        .userAgent("AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Host", "app.pedata.cn")
                        .header("Accept-Language", "zh-Hans-CN;q=1")
                        .data("username", "13717951934")
                        .data("password", "3961shy")
                        .timeout(5000)
                        .proxy(proxy)
                        .method(Connection.Method.POST)
                        .execute();
                if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out reget");
            }
        }
        Map<String,String> map=doc.cookies();
        return map;
    }

    public static void serach(java.sql.Connection con,Proxy proxy) throws IOException, SQLException {
        String sql="insert into si_touzishijian(invest_industry,invest_date,invest_round,invest_tor,invest_ep,invest_eplogo,invest_tag,invest_money,invest_add,invest_desc,ep_address,ep_email,ep_person,ep_phone,ep_desc,ep_setudate,ep_shortname,ep_web,ep_dic,ep_dicen,ep_dic_desc,ep_dic_descen,org_desc,org_name,org_setupdate,org_shortname,org_shortnameen,org_dic,VC,VC_en) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        System.out.println("begin denglu");
        Map<String,String> map=denglu(proxy);
        int p=0;
        for(int x=1;x<=8600;x++) {
            System.out.println("begin serach");
            Document doc = null;
            while (true) {
                try {
                    doc = Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/event/investList?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone5S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3")
                            .userAgent("AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Host", "app.pedata.cn")
                            .header("Accept-Language", "zh-Hans-CN;q=1")
                            .cookies(map)
                            .proxy(proxy)
                            .timeout(5000)
                            .data("limit", "10")
                            .data("start", String.valueOf(p))
                            .post();
                    if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                        break;
                    }
                }catch (Exception e){
                    System.out.println("time out reget");
                }
            }
            System.out.println("serach success");
            String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").replace("\n", "");
            Gson gspn = new Gson();
            SerBean ser = gspn.fromJson(json, SerBean.class);
            for (SerBean.Result re : ser.result) {
                try {
                    String invest_industry = re.epIndustryNameCn;
                    String invest_date = re.eventDate;
                    String invest_round = re.investRoundNameCn;
                    String invest_tor = re.investor;
                    String invest_ep = re.epSname;
                    String invest_eplogo = re.epLogo;
                    String invest_tag = re.tags.toString();
                    String invest_money = re.packInfo.replace("|", "#####").split("#####", 2)[1];
                    String eveid = re.eventId;
                    System.out.println("begin detail");
                    String detail = detail(eveid, map,proxy);
                    System.out.println("detail success");
                    DetBean det = gspn.fromJson(detail, DetBean.class);
                    String invest_add = "";
                    try {
                        invest_add = det.result.get(0).enterprise.epRegistrationPlace.dicNameCn;
                    } catch (Exception e) {
                        invest_add = "";
                    }
                    String ep_address = "";
                    try {
                        ep_address = det.result.get(0).enterprise.epContact.get(0).epContactAddressCn;
                    } catch (Exception e) {
                        ep_address = "";
                    }
                    String invest_desc = "";
                    try {
                        invest_desc = det.result.get(0).descData.get(0).descCn;
                    } catch (Exception e) {
                        invest_desc = "";
                    }
                    String ep_email = "";
                    try {
                        ep_email = det.result.get(0).enterprise.epContact.get(0).epContactEmail;
                    } catch (Exception e) {
                        ep_email = "";
                    }
                    String ep_person = "";
                    try {
                        ep_person = det.result.get(0).enterprise.epContact.get(0).epContactPersonCn;
                    } catch (Exception e) {
                        ep_person = "";
                    }
                    String ep_phone = "";
                    try {
                        ep_phone = det.result.get(0).enterprise.epContact.get(0).epContactTel;
                    } catch (Exception e) {
                        ep_phone = "";
                    }
                    String ep_desc = "";
                    try {
                        ep_desc = det.result.get(0).enterprise.epDescData.get(0).descCn;
                    } catch (Exception e) {
                        ep_desc = "";
                    }
                    String ep_setudate = "";
                    try {
                        ep_setudate = det.result.get(0).enterprise.epSetupDate;
                    } catch (Exception e) {
                        ep_setudate = "";
                    }
                    String ep_shortname = "";
                    try {
                        ep_shortname = det.result.get(0).enterprise.epShortnameCn;
                    } catch (Exception e) {
                        ep_shortname = "";
                    }
                    String ep_web = "";
                    try {
                        ep_web = det.result.get(0).enterprise.epWeb;
                    } catch (Exception e) {
                        ep_web = "";
                    }
                    String ep_dic = "";
                    String ep_dicen = "";
                    String ep_dic_desc = "";
                    String ep_dic_descen = "";
                    try {
                        StringBuffer str1 = new StringBuffer();
                        StringBuffer str2 = new StringBuffer();
                        StringBuffer str3 = new StringBuffer();
                        StringBuffer str4 = new StringBuffer();
                        for (DetBean.Result.Ene.Ind i : det.result.get(0).enterprise.industry) {
                            str1.append(i.dicNameCn + ";");
                            str2.append(i.dicNameEn + ";");
                            str3.append(i.dicPathNameCn + ";");
                            str4.append(i.dicPathNameEn + ";");
                        }
                        ep_dic = str1.toString();
                        ep_dicen = str2.toString();
                        ep_dic_desc = str3.toString();
                        ep_dic_descen = str4.toString();
                    } catch (Exception e) {

                    }
                    String org_desc = "";
                    try {
                        org_desc = det.result.get(0).investInves.get(0).org.orgDesc.get(0).descCn;
                    } catch (Exception e) {
                        org_desc = "";
                    }
                    String org_name = "";
                    try {
                        org_name = det.result.get(0).investInves.get(0).org.orgNameCn;
                    } catch (Exception e) {
                        org_name = "";
                    }
                    String org_setupdate = "";
                    try {
                        org_setupdate = det.result.get(0).investInves.get(0).org.orgSetupDate;
                    } catch (Exception e) {
                        org_setupdate = "";
                    }
                    String org_shortname = "";
                    try {
                        org_shortname = det.result.get(0).investInves.get(0).org.orgShortnameCn;
                    } catch (Exception e) {
                        org_shortname = "";
                    }
                    String org_shortnameen = "";
                    try {
                        org_shortnameen = det.result.get(0).investInves.get(0).org.orgShortnameEn;
                    } catch (Exception e) {
                        org_shortnameen = "";
                    }
                    String org_dic = "";
                    try {
                        org_dic = det.result.get(0).investStage.dicNameCn;
                    } catch (Exception e) {
                        org_dic = "";
                    }
                    String VC = "";
                    try {
                        VC = det.result.get(0).investInves.get(0).org.orgType.dicNameCn;
                    } catch (Exception e) {
                        VC = "";
                    }
                    String VC_en = "";
                    try {
                        VC_en = det.result.get(0).investInves.get(0).org.orgType.dicNameEn;
                    } catch (Exception e) {
                        VC_en = "";
                    }

                    ps.setString(1, invest_industry);
                    ps.setString(2, invest_date);
                    ps.setString(3, invest_round);
                    ps.setString(4, invest_tor);
                    ps.setString(5, invest_ep);
                    ps.setString(6, invest_eplogo);
                    ps.setString(7, invest_tag);
                    ps.setString(8, invest_money);
                    ps.setString(9, invest_add);
                    ps.setString(10, invest_desc);
                    ps.setString(11, ep_address);
                    ps.setString(12, ep_email);
                    ps.setString(13, ep_person);
                    ps.setString(14, ep_phone);
                    ps.setString(15, ep_desc);
                    ps.setString(16, ep_setudate);
                    ps.setString(17, ep_shortname);
                    ps.setString(18, ep_web);
                    ps.setString(19, ep_dic);
                    ps.setString(20, ep_dicen);
                    ps.setString(21, ep_dic_desc);
                    ps.setString(22, ep_dic_descen);
                    ps.setString(23, org_desc);
                    ps.setString(24, org_name);
                    ps.setString(25, org_setupdate);
                    ps.setString(26, org_shortname);
                    ps.setString(27, org_shortnameen);
                    ps.setString(28, org_dic);
                    ps.setString(29, VC);
                    ps.setString(30, VC_en);
                    ps.executeUpdate();
                    System.out.println("*************************************");
                }catch (Exception e){
                    System.out.println("error");
                }
            }
            p=p+10;
            if(x%100==0){
                map=denglu(proxy);
            }
            System.out.println(p);
            System.out.println("---------------------------------------------------------------------");
        }
    }

    public static String detail(String eveid,Map<String,String> map,Proxy proxy) throws IOException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/event/eventDetail?eventId=" + eveid + "&typeName=invest&callback=Ext.data.JsonP.callback2&_dc=1496643103855")
                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27")
                        .header("Host", "app.pedata.cn")
                        .header("Accept-Language", "zh-cn")
                        .cookies(map)
                        .timeout(5000)
                        .proxy(proxy)
                        .get();
                if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        String json=doc.outerHtml().replace("<html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").replace("</html>","").replace("Ext.data.JsonP.callback2(","").replace(");","").replace("\n","");
        return json;
    }
}
