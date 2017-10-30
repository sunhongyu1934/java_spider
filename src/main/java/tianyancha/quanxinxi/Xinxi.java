package tianyancha.quanxinxi;

import com.google.gson.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;



/**
 * Created by Administrator on 2017/5/20.
 */
public class Xinxi {
    // 代理隧道验证信息
    final static String ProxyUser = "H4TL2M827AIJ963D";
    final static String ProxyPass = "81C9D64628A60CF9";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Xinxi x=new Xinxi();
        final Lujing lu=x.new Lujing();
        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon1 = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon1, lu);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Connection finalCon = con;
        for(int i=1;i<=20;i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        huoqu(lu, proxy, finalCon);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }




    public static String qingqiu(Proxy proxy,String url){
        Document doc= null;
        System.out.println(url);
        while (true){
            try{
                doc = Jsoup.connect(url)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .proxy(proxy)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<body>", "").replace("</html>", "").replace("</body>", "").replace("<head>", "").replace("</head>", "").replace(" ", "").replace("\n","").trim())) {
                    break;
                }
            }catch (Exception e){
            }
        }

        return doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head>","").replace("</head>","").replace("<body>","").replace("</body>","").replace(" ", "");
    }


    class Json{
        Map<String,String> map=new HashMap<String, String>();
        public void jin(String key,String value){
            map.put(key,value);
        }
        public Map<String,String> quchu(){
            return map;
        }

    }

    class Lujing{
        BlockingQueue<String> lujing=new LinkedBlockingQueue<String>(100);
        public void chuan(String key) throws InterruptedException {
            lujing.put(key);
        }
        public String quchu() throws InterruptedException {
            return lujing.poll(600, TimeUnit.SECONDS);
        }
    }

    public static void duqu(Lujing lu) throws InterruptedException {
        File file=new File("/data1/spider/java_spider/tyc");
        while (true) {
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    File file2 = new File(tempList[i].toString());
                    try {
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file2), "UTF-8"));
                        String link = null;
                        StringBuffer str = new StringBuffer();
                        while ((link = buffer.readLine()) != null) {
                            str.append(link + "\r\n");
                        }
                        String html = str.toString();
                        lu.chuan(html);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Thread.sleep(60000);
        }
    }

    public static void data(Connection con2,Lujing lu) throws SQLException, InterruptedException {
        int p=270000;
        for(int x=1;x<=1;x++) {
            //String sql = "select distinct quan_cheng,t_id,gongshang_hao from tyc_jichu_sh_baidu  where t_id not in (select t_id from tyc_information_sh) and quan_cheng!='' and t_id not like '%?%'  limit "+p+",850000";
            String sql="select quan_cheng,t_id,gongshang_hao from tyc_jichu_bj where t_id not in (select t_id from tyc_information) and t_id not in (select t_id from tyc_information2) and t_id not in (select t_id from tyc_information3) and t_id not in (select t_id from tyc_information4) limit 540000,270000";
            PreparedStatement ps = con2.prepareStatement(sql);
            System.out.println("begin duqu");
            ResultSet rs = ps.executeQuery();
            System.out.println("duqu success");
            while (rs.next()) {
                lu.chuan(rs.getString(rs.findColumn("t_id")) + "#####" + rs.getString(rs.findColumn("quan_cheng")) + "#####" + rs.getString(rs.findColumn("gongshang_hao")));
            }
            p=p+270000;
        }
    }



    public static void huoqu(Lujing lu,Proxy proxy,Connection con) throws SQLException {
        while (true) {
            try {
                String html=lu.quchu();
                if(html==null){
                    break;
                }

                String tid=html.split("#####",3)[0];
                String quancheng=html.split("#####",3)[1];
                String gongshang=html.split("#####",3)[2];



                /*Document doc = Jsoup.parse(html.substring(0,html.lastIndexOf("\r\n")-1));
                String tid = doc.select("div.shareBox.pt8.pb8.mt20.text-right div.text-left.in-block a").attr("ng-href").split(";")[0].split("&")[0].split("\\?")[1].split("/", 5)[4];
                String quancheng=getString(doc,"span.f18.in-block.vertival-middle.ng-binding",0);
                String cengyongming=getString(doc,"div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4.ng-binding",0);
                String logo=getHref(doc,"div.b-c-white.new-border.over-hide.mr10.ie9Style img","ng-src",0);
                String phone=getString(doc,"div.f14.new-c3.mt10 span.ng-binding",0);
                String email=getString(doc,"div.in-block.vertical-top span.in-block.vertical-top.overflow-width.emailWidth.ng-binding",0);
                String web=getString(doc,"div.f14.new-c3 div.in-block.vertical-top.overflow-width.mr20 a.c9.ng-binding.ng-scope",0);
                String address=getString(doc,"div.in-block.vertical-top span.in-block.overflow-width.vertical-top.emailWidth.ng-binding",1);
                String faren=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td a.in-block.vertival-middle.overflow-width.f14.mr20.ng-binding.ng-scope.ng-isolate-scope",0);
                String zhuceziben=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",0);
                String zhuceshijian=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",1);
                String zhuangtai=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",2);
                String gongshang=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",0);
                String zuzhijigou=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",1);
                String tongyixinxyong=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",2);
                String qiyeleixing=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",3);
                String hangye=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",4);
                String yingyenianxian=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",5);
                String hezhunriqi=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",6);
                String dengjijiguan=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",7);
                String zhucedizhi=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",8);
                String jingyingfanwei=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",9);*/


                /*Map<String,String> map=new HashMap<String, String>();
                map.put("quancheng",quancheng);
                map.put("cengyongming",cengyongming);
                map.put("logo",logo);
                map.put("phone",phone);
                map.put("email",email);
                map.put("web",web);
                map.put("address",address);
                map.put("faren",faren);
                map.put("zhuceziben",zhuceziben);
                map.put("zhuceshijian",zhuceshijian);
                map.put("zhuangtai",zhuangtai);
                map.put("gongshang",gongshang);
                map.put("zuzhijigou",zuzhijigou);
                map.put("tongyixinxyong",tongyixinxyong);
                map.put("qiyeleixing",qiyeleixing);
                map.put("hangye",hangye);
                map.put("yingyenianxian",yingyenianxian);
                map.put("hezhunriqi",hezhunriqi);
                map.put("dengjijiguan",dengjijiguan);
                map.put("zhucedizhi",zhucedizhi);
                map.put("jingyingfanwei",jingyingfanwei);*/

                //String json=JSONObject.fromObject(map).toString();
                String[] kk=new String[]{tid,quancheng};
                Xinxi xi=new Xinxi();
                Json js=xi.new Json();
                js.jin("t_id",tid);
                js.jin("gongshang_hao",gongshang);
                js.jin("essential_information","");
                get(kk, proxy, js,con);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("huo qu wanbi ");
    }


    public static void ruku(Connection con,Json js) throws SQLException, IOException {
        Map<String,String> map=js.quchu();

        List<String> ff=new ArrayList<String>();
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        Map<String,Object> TablePro=new HashMap<String, Object>();
        Map<String,Object> maps=new HashMap<String, Object>();

        ff.add("t_id");
        ff.add("essential_information");
        ff.add("leading_member");
        ff.add("shareholder_Information");
        ff.add("outbound_investment");
        ff.add("change_record");
        ff.add("b_ranch");
        ff.add("financing_history");
        ff.add("core_team");
        ff.add("enterprise_business");
        ff.add("investment_event");
        ff.add("competing_information");
        ff.add("legal_proceedings");
        ff.add("court_notice");
        ff.add("person_subjected");
        ff.add("dishonest_person");
        ff.add("abnormal_operation");
        ff.add("administrative_sanction");
        ff.add("serious_violation");
        ff.add("stock_ownership");
        ff.add("chattel_mortgage");
        ff.add("tax_notice");
        ff.add("b_idding");
        ff.add("bond_information");
        ff.add("purchase_information");
        ff.add("r_ecruit");
        ff.add("tax_rating");
        ff.add("spot_check");
        ff.add("product_information");
        ff.add("qualification_certificate");
        ff.add("trademark_information");
        ff.add("p_atent");
        ff.add("c_opyright");
        ff.add("website_filing");
        ff.add("gongshang_hao");

        for(int x=0;x<ff.size();x++){
            Map<String,String> detail=new HashMap<String, String>();
            detail.put("field",ff.get(x));
            detail.put("content","\""+map.get(ff.get(x))+"\"");
            list.add(detail);
        }

        TablePro.put("fields",ff);
        TablePro.put("table","tyc_information4");
        maps.put("tablePro",TablePro);
        maps.put("detail",list);
        JSONObject jsonObject=JSONObject.fromObject(maps);
        try {
            Document doc =null;
            int j=0;
            while (true) {
                try {
                    doc = Jsoup.connect("http://59.110.52.96:8080/java_web/servlet/insertServlet")
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .data("json", jsonObject.toString())
                            .timeout(5000)
                            .post();
                    if(doc.outerHtml().contains("insert spider success and insert log success")){
                        break;
                    }
                }catch (Exception e){
                    System.out.println("time out reget");
                }
                j++;
                if(j>=10){
                    break;
                }
            }
            String sql="insert into tyc_information4(t_id,essential_information,leading_member,shareholder_Information,outbound_investment,change_record,b_ranch,financing_history,core_team,enterprise_business,investment_event,competing_information,legal_proceedings,court_notice,person_subjected,dishonest_person,abnormal_operation,administrative_sanction,serious_violation,stock_ownership,chattel_mortgage,tax_notice,b_idding,bond_information,purchase_information,r_ecruit,tax_rating,spot_check,product_information,qualification_certificate,trademark_information,p_atent,c_opyright,website_filing,gongshang_hao) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,map.get("t_id"));
            ps.setString(2,map.get("essential_information"));
            ps.setString(3,map.get("leading_member"));
            ps.setString(4,map.get("shareholder_Information"));
            ps.setString(5,map.get("outbound_investment"));
            ps.setString(6,map.get("change_record"));
            ps.setString(7,map.get("b_ranch"));
            ps.setString(8,map.get("financing_history"));
            ps.setString(9,map.get("core_team"));
            ps.setString(10,map.get("enterprise_business"));
            ps.setString(11,map.get("investment_event"));
            ps.setString(12,map.get("competing_information"));
            ps.setString(13,map.get("legal_proceedings"));
            ps.setString(14,map.get("court_notice"));
            ps.setString(15,map.get("person_subjected"));
            ps.setString(16,map.get("dishonest_person"));
            ps.setString(17,map.get("abnormal_operation"));
            ps.setString(18,map.get("administrative_sanction"));
            ps.setString(19,map.get("serious_violation"));
            ps.setString(20,map.get("stock_ownership"));
            ps.setString(21,map.get("chattel_mortgage"));
            ps.setString(22,map.get("tax_notice"));
            ps.setString(23,map.get("b_idding"));
            ps.setString(24,map.get("bond_information"));
            ps.setString(25,map.get("purchase_information"));
            ps.setString(26,map.get("r_ecruit"));
            ps.setString(27,map.get("tax_rating"));
            ps.setString(28,map.get("spot_check"));
            ps.setString(29,map.get("product_information"));
            ps.setString(30,map.get("qualification_certificate"));
            ps.setString(31,map.get("trademark_information"));
            ps.setString(32,map.get("p_atent"));
            ps.setString(33,map.get("c_opyright"));
            ps.setString(34,map.get("website_filing"));
            ps.setString(35,map.get("gongshang_hao"));
            System.out.println("kaishi");
            ps.executeUpdate();
            System.out.println("ok");
            System.out.println("------------------------------------------------------------------------------");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void pinjie(List<Object> list,Json js,Connection con) throws SQLException, IOException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map;
        Gson gson=new Gson();
        if(list!=null&&list.size()>0) {
            for (Object obj : list) {
                List<JSONObject> jsonlist=new ArrayList<JSONObject>();
                if (obj instanceof Bean.Duiwaitouzi) {
                    Bean.Duiwaitouzi dui = (Bean.Duiwaitouzi) obj;
                    for (Bean.Duiwaitouzi.Da.detail dd : dui.data.result) {
                        String beitouziqiyeming = dd.name;
                        String beitouzifading = dd.legalPersonName;
                        String zhuceziben2 = dd.regCapital;
                        String touzishue = dd.amount;
                        String touzizhanbi = dd.percent;
                        String beitouzitid=dd.id;
                        String duizhuceshijian ="";
                        if(StringUtils.isNotEmpty(dd.estiblishTime)) {
                            long zhuceshijian2 = Long.parseLong(dd.estiblishTime);
                            duizhuceshijian = simpleDateFormat.format(zhuceshijian2);
                        }
                        String zhuangtai = dd.regStatus;
                        String jingyingfanwei2 = dd.business_scope;
                        map = new HashMap<String, String>();
                        map.put("beitouziqiyemingcheng", beitouziqiyeming);
                        map.put("fadingdaibiaoren", beitouzifading);
                        map.put("zhuceziben", zhuceziben2);
                        map.put("touzishue", touzishue);
                        map.put("touzizhanbi", touzizhanbi);
                        map.put("zhuceshijian", duizhuceshijian);
                        map.put("zhuangtai", zhuangtai);
                        map.put("jingyingfanwei", jingyingfanwei2);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("outbound_investment", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Biangeng) {
                    Bean.Biangeng bian = (Bean.Biangeng) obj;
                    for (Bean.Biangeng.Da.detail bb : bian.data.result) {
                        String biangengxiangmu = bb.changeItem;
                        String biangengtime = bb.changeTime;
                        String biangengqian = bb.contentBefore;
                        String biangenghou = bb.contentAfter;
                        map = new HashMap<String, String>();
                        map.put("biangengxiangmu", biangengxiangmu);
                        map.put("biangengshijian", biangengtime);
                        map.put("biangengqian", biangengqian);
                        map.put("biangenghou", biangenghou);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("change_record", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Fenzhi) {
                    Bean.Fenzhi fen = (Bean.Fenzhi) obj;
                    for (Bean.Fenzhi.Da.detail ff : fen.data.result) {
                        String qiyemingcheng = ff.name;
                        String fadingdaibiao = ff.legalPersonName;
                        String zhuangtai = ff.regStatus;
                        String fenzhuceshijian="";
                        if(StringUtils.isNotEmpty(ff.estiblishTime)) {
                            long zhuceshijian2 = Long.parseLong(ff.estiblishTime);
                             fenzhuceshijian = simpleDateFormat.format(zhuceshijian2);
                        }
                        String jingying = ff.category;
                        map = new HashMap<String, String>();
                        map.put("qiyemingcheng", qiyemingcheng);
                        map.put("fadingdaibiaoren", fadingdaibiao);
                        map.put("zhuangtai", zhuangtai);
                        map.put("zhuceshijian", fenzhuceshijian);
                        map.put("jingyingfanwei", jingying);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("b_ranch", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Finacning) {
                    Bean.Finacning fin = (Bean.Finacning) obj;
                    for (Bean.Finacning.Da.Pa.detail ff : fin.data.page.rows) {
                        String rongzitime ="";
                        if(StringUtils.isNotEmpty(ff.date)) {
                            long rongzishijian = Long.parseLong(ff.date);
                             rongzitime = simpleDateFormat.format(rongzishijian);
                        }
                        String rongzilunci = ff.round;
                        String guzhi = ff.value;
                        String rongzijine = ff.money;
                        String bili = ff.share;
                        String touzifang = ff.rongziMap;
                        map = new HashMap<String, String>();
                        map.put("rongzishijian", rongzitime);
                        map.put("lunci", rongzilunci);
                        map.put("guzhi", guzhi);
                        map.put("rongzijine", rongzijine);
                        map.put("bili", bili);
                        map.put("touzifang", touzifang);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("financing_history", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Hexintuandui) {
                    Bean.Hexintuandui hexin = (Bean.Hexintuandui) obj;
                    for (Bean.Hexintuandui.Da.Pa.detail hh : hexin.data.page.rows) {
                        String hexinlogo = hh.icon;
                        String hexinming = hh.name;
                        String hexinzhiwei = hh.title;
                        String hexinjianli = hh.desc;
                        map = new HashMap<String, String>();
                        map.put("logo", hexinlogo);
                        map.put("mingcheng", hexinming);
                        map.put("zhiwei", hexinzhiwei);
                        map.put("jianli", hexinjianli);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("core_team", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Qiyeyewu) {
                    Bean.Qiyeyewu qi = (Bean.Qiyeyewu) obj;
                    for (Bean.Qiyeyewu.Da.Pa.detail qq : qi.data.page.rows) {
                        String qilogo = qq.logo;
                        String qiming = qq.product;
                        String qifenlei = qq.hangye;
                        String qimiaoshu = qq.yewu;
                        map = new HashMap<String, String>();
                        map.put("logo", qilogo);
                        map.put("mingcheng", qiming);
                        map.put("fenlei", qifenlei);
                        map.put("miaoshu", qimiaoshu);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("enterprise_business", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Touzishijian) {
                    Bean.Touzishijian touzi = (Bean.Touzishijian) obj;
                    for (Bean.Touzishijian.Da.Pa.detail tt : touzi.data.page.rows) {
                        String touzishijiantime ="";
                        if(StringUtils.isNotEmpty(tt.tzdate)) {
                            long touzishijianshijian = Long.parseLong(tt.tzdate);
                            touzishijiantime = simpleDateFormat.format(touzishijianshijian);
                        }
                        String touzishijianlunci = tt.lunci;
                        String touzishijianjine = tt.money;
                        String touzishijiantouzifang = tt.rongzi_map;
                        String touzishijianchanpin = tt.product;
                        String touzishijiandiqu = tt.location;
                        String touzishijianhangye = tt.hangye1;
                        String touzishijianyewu = tt.yewu;
                        map = new HashMap<String, String>();
                        map.put("shijian", touzishijiantime);
                        map.put("lunci", touzishijianlunci);
                        map.put("jine", touzishijianjine);
                        map.put("touzifang", touzishijiantouzifang);
                        map.put("chanpin", touzishijianchanpin);
                        map.put("diqu", touzishijiandiqu);
                        map.put("hangye", touzishijianhangye);
                        map.put("yewu", touzishijianyewu);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("investment_event", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Jingpinxinxi) {
                    Bean.Jingpinxinxi jingpin = (Bean.Jingpinxinxi) obj;
                    for (Bean.Jingpinxinxi.Da.Pa.detail tt : jingpin.data.page.rows) {
                        String jingpinchenglitime="";
                        if(StringUtils.isNotEmpty(tt.setupDate)){
                            long jingpinchenglishijian = Long.parseLong(tt.setupDate);
                            jingpinchenglitime = simpleDateFormat.format(jingpinchenglishijian);
                        }
                        String jingpinchanpin = tt.jingpinProduct;
                        String jingpindiqu = tt.location;
                        String jingpinlunci = tt.round;
                        String jingpinhangye = tt.hangye;
                        String jingpinyewu = tt.yewu;
                        String jingpinguzhi = tt.value;
                        map = new HashMap<String, String>();
                        map.put("chenglishijian", jingpinchenglitime);
                        map.put("chanpin", jingpinchanpin);
                        map.put("diqu", jingpindiqu);
                        map.put("lunci", jingpinlunci);
                        map.put("hangye", jingpinhangye);
                        map.put("yewu", jingpinyewu);
                        map.put("guzhi", jingpinguzhi);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("competing_information", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Falvsusong) {
                    Bean.Falvsusong falv = (Bean.Falvsusong) obj;
                    for (Bean.Falvsusong.Da.detail ff : falv.data.items) {
                        String falvsusongriqi="";
                        if(StringUtils.isNotEmpty(ff.submittime)) {
                            long submittime = Long.parseLong(ff.submittime);
                             falvsusongriqi = simpleDateFormat.format(submittime);
                        }
                        String falvsusongwenshu = ff.uuid;
                        String falvsusonganjianleixing = ff.casetype;
                        String falvsusonganjianhao = ff.caseno;
                        map = new HashMap<String, String>();
                        map.put("riqi", falvsusongriqi);
                        map.put("wenshu", falvsusongwenshu);
                        map.put("anjianleixing", falvsusonganjianleixing);
                        map.put("anjianhao", falvsusonganjianhao);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("legal_proceedings", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Fayuangonggao) {
                    Bean.Fayuangonggao fayuan = (Bean.Fayuangonggao) obj;
                    for (Bean.Fayuangonggao.detail ff : fayuan.courtAnnouncements) {
                        String fayuangonggaoshijian = ff.publishdate;
                        String fayuanshangsufang = ff.party1;
                        String fayuanbeisufang = ff.party2;
                        String fayuangonggaoleixing = ff.bltntypename;
                        String fayuanfayuan = ff.courtcode;
                        String fayuanxiangqing = ff.content;
                        map = new HashMap<String, String>();
                        map.put("shijian", fayuangonggaoshijian);
                        map.put("shangsufang", fayuanshangsufang);
                        map.put("beisufang", fayuanbeisufang);
                        map.put("gonggaoleixing", fayuangonggaoleixing);
                        map.put("fayuan", fayuanfayuan);
                        map.put("xiangqing", fayuanxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("court_notice", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Beizhixingren) {
                    Bean.Beizhixingren bei = (Bean.Beizhixingren) obj;
                    for (Bean.Beizhixingren.Da.detail bb : bei.data.items) {
                        String beilihantime ="";
                        if(StringUtils.isNotEmpty(bb.caseCreateTime)) {
                            long beilihanriqi = Long.parseLong(bb.caseCreateTime);
                             beilihantime = simpleDateFormat.format(beilihanriqi);
                        }
                        String beizhixingbiaode = bb.execMoney;
                        String beianhao = bb.caseCode;
                        String beizhixingfayuan = bb.execCourtName;
                        map = new HashMap<String, String>();
                        map.put("lianriqi", beilihantime);
                        map.put("zhixingbiaode", beizhixingbiaode);
                        map.put("anhao", beianhao);
                        map.put("zhixingfayuan", beizhixingfayuan);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("person_subjected", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Shixinren) {
                    Bean.Shixinren shi = (Bean.Shixinren) obj;
                    for (Bean.Shixinren.Da.detail ss : shi.data.items) {
                        String shiliantime ="";
                        if(StringUtils.isNotEmpty(ss.publishdate)) {
                            long shilianriqi = Long.parseLong(ss.publishdate);
                             shiliantime = simpleDateFormat.format(shilianriqi);
                        }
                        String shianhao = ss.gistid;
                        String shizhixingfayuan = ss.courtname;
                        String shilvxingzhuangtai = ss.performance;
                        String shizhixingyijuwenhao = ss.casecode;
                        String shixiangqing = ss.duty;
                        map = new HashMap<String, String>();
                        map.put("lianriqi", shiliantime);
                        map.put("anhao", shianhao);
                        map.put("zhixingfayuan", shizhixingfayuan);
                        map.put("lvxingzhuangtai", shilvxingzhuangtai);
                        map.put("zhixingyijuwenhao", shizhixingyijuwenhao);
                        map.put("xiangqing", shixiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("dishonest_person", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Jingyingyichang) {
                    Bean.Jingyingyichang jing = (Bean.Jingyingyichang) obj;
                    for (Bean.Jingyingyichang.Da.detail jj : jing.data.result) {
                        String jinglieruriqi = jj.putDate;
                        String jinglieruyuanyin = jj.putReason;
                        String jingjuedingjiguan = jj.putDepartment;
                        String jingyichuriqi = jj.removeDate;
                        String jingyichuyuanyin = jj.removeReason;
                        String jingyichujiguan = jj.removeDepartment;
                        map = new HashMap<String, String>();
                        map.put("lieruriqi", jinglieruriqi);
                        map.put("lieruyuanyin", jinglieruyuanyin);
                        map.put("juedingjiguan", jingjuedingjiguan);
                        map.put("yichuriqi", jingyichuriqi);
                        map.put("yichuyuanyin", jingyichuyuanyin);
                        map.put("yichujiguan", jingyichujiguan);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("abnormal_operation", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Xingzhengchufa) {
                    Bean.Xingzhengchufa xing = (Bean.Xingzhengchufa) obj;
                    for (Bean.Xingzhengchufa.Da.detail xx : xing.data.items) {
                        String xingjuedingriqi = xx.decisionDate;
                        String xingjuedingwenshuhao = xx.punishNumber;
                        String xingleixing = xx.type;
                        String xingjuedingjiguan = xx.departmentName;
                        String xingxiangqing = xx.type;
                        map = new HashMap<String, String>();
                        map.put("juedingriqi", xingjuedingriqi);
                        map.put("juedingwenshuhao", xingjuedingwenshuhao);
                        map.put("leixing", xingleixing);
                        map.put("juedingjiguan", xingjuedingjiguan);
                        map.put("xiangqing", xingxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("administrative_sanction", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Yanzhongweifa) {
                    Bean.Yanzhongweifa yan = (Bean.Yanzhongweifa) obj;
                    for (Bean.Yanzhongweifa.Da.detail yy : yan.data.items) {
                        String yanlieritime ="";
                        if(StringUtils.isNotEmpty(yy.putDate)) {
                            long yanlieruriqi = Long.parseLong(yy.putDate);
                             yanlieritime = simpleDateFormat.format(yanlieruriqi);
                        }
                        String yanlieruyuanyin = yy.putReason;
                        String yanjuedingjiguan = yy.putDepartment;
                        map = new HashMap<String, String>();
                        map.put("lieruriqi", yanlieritime);
                        map.put("lieruyuanyin", yanlieruyuanyin);
                        map.put("juedingjiguan", yanjuedingjiguan);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("serious_violation", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Guquanchuzhi) {
                    Bean.Guquanchuzhi gu = (Bean.Guquanchuzhi) obj;
                    for (Bean.Guquanchuzhi.Da.detail gg : gu.data.items) {
                        String gugonggaotime="";
                        if(StringUtils.isNotEmpty(gg.regDate)) {
                            long gugonggaoshijian = Long.parseLong(gg.regDate);
                             gugonggaotime = simpleDateFormat.format(gugonggaoshijian);
                        }
                        String gudengjibianhao = gg.regNumber;
                        String guchuzhiren = gg.pledgor;
                        String guzhiquanren = gg.pledgee;
                        String guzhuangtai = gg.state;
                        String guxiangqing = gg.equityAmount;
                        map = new HashMap<String, String>();
                        map.put("dengjibianhao", gudengjibianhao);
                        map.put("chuzhiren", guchuzhiren);
                        map.put("zhiquanren", guzhiquanren);
                        map.put("gonggaoshijian", gugonggaotime);
                        map.put("zhuangtai", guzhuangtai);
                        map.put("xiangqing", guxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("stock_ownership", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Dongchandiya) {
                    Bean.Dongchandiya dongchan = (Bean.Dongchandiya) obj;
                    Bean.Dongchandiya.Dongchan dong = gson.fromJson(dongchan.data, Bean.Dongchandiya.Dongchan.class);
                    if (dong.items != null && dong.items.size() > 0) {
                        for (Bean.Dongchandiya.Dongchan.details dd : dong.items) {
                            String dongdengjiriqi = dd.baseInfo.regDate;
                            String dongdengjihao = dd.baseInfo.regNum;
                            String dongbeidanbao = dd.baseInfo.type;
                            String dongdengjijiguan = dd.baseInfo.regDepartment;
                            String dongzhuangtai = dd.baseInfo.status;
                            String dongxiangqing = dongchan.data;
                            map = new HashMap<String, String>();
                            map.put("dengjiriqi", dongdengjiriqi);
                            map.put("dengjihao", dongdengjihao);
                            map.put("beidanbaozhaiquanleixing", dongbeidanbao);
                            map.put("dengjijiguan", dongdengjijiguan);
                            map.put("zhuangtai", dongzhuangtai);
                            map.put("xiangqing", dongxiangqing);
                            jsonlist.add(JSONObject.fromObject(map));
                        }
                    }
                    js.jin("chattel_mortgage", JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Qianshuigonggao) {
                    Bean.Qianshuigonggao qian = (Bean.Qianshuigonggao) obj;
                    for (Bean.Qianshuigonggao.Da.detail qq : qian.data.items) {
                        String qianfaburiqi = qq.publishDate;
                        String qiannashuiren = qq.personIdNumber;
                        String qianqianshuizhong = qq.taxCategory;
                        String qianshuiyue = qq.ownTaxAmount;
                        map=new HashMap<String, String>();
                        map.put("faburiqi",qianfaburiqi);
                        map.put("nashuirenshibiehao",qiannashuiren);
                        map.put("qianshuibizhong",qianqianshuizhong);
                        map.put("qianshuiyue", qianshuiyue);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("tax_notice",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zhaotoubiao) {
                    Bean.Zhaotoubiao zhao = (Bean.Zhaotoubiao) obj;
                    for (Bean.Zhaotoubiao.Da.detail zz : zhao.data.items) {
                        String zhaofabutime ="";
                        if(StringUtils.isNotEmpty(zz.publishTime)) {
                            long zhaofabushijian = Long.parseLong(zz.publishTime);
                             zhaofabutime = simpleDateFormat.format(zhaofabushijian);
                        }
                        String zhaobiaoti = zz.title;
                        String zhaocaijiren = zz.purchaser;
                        String zhaoxiangqing = zz.content;
                        String zhaogonggaozhairen = zz.abs;
                        String zhaozongjie = zz.intro;
                        map=new HashMap<String, String>();
                        map.put("fabushijian",zhaofabutime);
                        map.put("biaoti",zhaobiaoti);
                        map.put("caigouren",zhaocaijiren);
                        map.put("xiangqing",zhaoxiangqing);
                        map.put("gonggaozhairen",zhaogonggaozhairen);
                        map.put("zongjie",zhaozongjie);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("b_idding",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zhaiquanxinxi) {
                    Bean.Zhaiquanxinxi zhai = (Bean.Zhaiquanxinxi) obj;
                    for (Bean.Zhaiquanxinxi.Da.detail zz : zhai.data.bondList) {
                        String zhaifaxingtime ="";
                        if(StringUtils.isNotEmpty(zz.publishTime)) {
                            long zhaifaxingriqi = Long.parseLong(zz.publishTime);
                             zhaifaxingtime = simpleDateFormat.format(zhaifaxingriqi);
                        }
                        String zhaizhaiquanming = zz.bondName;
                        String zhaiquandaima = zz.bondNum;
                        String zhaiquanleixing = zz.bondType;
                        String zhaizuixinpingji = zz.debtRating;
                        String zhaixiangqing = zz.tip;
                        map=new HashMap<String, String>();
                        map.put("faxingriqi",zhaifaxingtime);
                        map.put("zhaiquanmingcheng",zhaizhaiquanming);
                        map.put("zhaiquandaima",zhaiquandaima);
                        map.put("zhaiquanleixing",zhaiquanleixing);
                        map.put("zuixinpingji",zhaizuixinpingji);
                        map.put("xiangqing",zhaixiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("bond_information",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Goudixinxi) {
                    Bean.Goudixinxi gou = (Bean.Goudixinxi) obj;
                    for (Bean.Goudixinxi.Da.detail gg : gou.data.companyPurchaseLandList) {
                        String gouqiandingtime ="";
                        String gouyuedingdongtime ="";
                        if(StringUtils.isNotEmpty(gg.signedDate)){
                            long gouqiandingriqi = Long.parseLong(gg.signedDate);
                             gouqiandingtime = simpleDateFormat.format(gouqiandingriqi);
                        }
                        if(StringUtils.isNotEmpty(gg.startTime)){
                            long gouyuedingdong = Long.parseLong(gg.startTime);
                             gouyuedingdongtime = simpleDateFormat.format(gouyuedingdong);
                        }
                        String goudianzijianguanhao = gg.elecSupervisorNo;
                        String gougongdimian = gg.totalArea;
                        String gouxingzhengqu = gg.location;
                        String gouxiangqing = gg.purpose;
                        map=new HashMap<String, String>();
                        map.put("qiandingriqi",gouqiandingtime);
                        map.put("dianzijianguanhao",goudianzijianguanhao);
                        map.put("donggongri",gouyuedingdongtime);
                        map.put("zongmianji",gougongdimian);
                        map.put("xingzhengqu",gouxingzhengqu);
                        map.put("xiangqing",gouxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("purchase_information",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zhaopin) {
                    Bean.Zhaopin zhao = (Bean.Zhaopin) obj;
                    for (Bean.Zhaopin.Da.detail zz : zhao.data.companyEmploymentList) {
                        String zhaofabutime ="";
                        if(StringUtils.isNotEmpty(zz.updateTime)) {
                            long zhaofabushijian = Long.parseLong(zz.updateTime);
                             zhaofabutime = simpleDateFormat.format(zhaofabushijian);
                        }
                        String zhaopinzhiwei = zz.title;
                        String zhaoxinzi = zz.oriSalary;
                        String gongzuojingyan = zz.experience;
                        String zhaopinrenshu = zz.employerNumber;
                        String zhaochegnshi = zz.city;
                        String zhaoxiangqing = zz.description;
                        map=new HashMap<String, String>();
                        map.put("fabushijian",zhaofabutime);
                        map.put("zhaopinzhiwei",zhaopinzhiwei);
                        map.put("xinzi",zhaoxinzi);
                        map.put("gongzuojingyan",gongzuojingyan);
                        map.put("zhaopinrenshu",zhaopinrenshu);
                        map.put("suozaichengshi",zhaochegnshi);
                        map.put("xiangqing",zhaoxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("r_ecruit",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Shuiwupingji) {
                    Bean.Shuiwupingji shui = (Bean.Shuiwupingji) obj;
                    for (Bean.Shuiwupingji.Da.detail ss : shui.data.items) {
                        String shuinianfen = ss.year;
                        String nashuipingji = ss.grade;
                        String shuileixing = ss.type;
                        String nashuirenshibiehao = ss.idNumber;
                        String shuipingjia = ss.evalDepartment;
                        map=new HashMap<String, String>();
                        map.put("nianfe",shuinianfen);
                        map.put("nashuipingji",nashuipingji);
                        map.put("leixing",shuileixing);
                        map.put("shibiehao",nashuirenshibiehao);
                        map.put("pingjiadanwei",shuipingjia);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("tax_rating",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Choucha) {
                    Bean.Choucha chou = (Bean.Choucha) obj;
                    for (Bean.Choucha.Da.detail cc : chou.data.items) {
                        String chouriqi = cc.checkDate;
                        String chouleixing = cc.checkType;
                        String choujieguo = cc.checkResult;
                        String choujiancha = cc.checkOrg;
                        map=new HashMap<String, String>();
                        map.put("riqi",chouriqi);
                        map.put("leixing",chouleixing);
                        map.put("jieguo",choujieguo);
                        map.put("jianchashishijiguan",choujiancha);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("spot_check",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Chanpin) {
                    Bean.Chanpin chan = (Bean.Chanpin) obj;
                    for (Bean.Chanpin.Da.detail cc : chan.data.items) {
                        String chantubiao = cc.icon;
                        String chanmingcheng = cc.name;
                        String chanjiancheng = cc.filterName;
                        String chanfenlei = cc.type;
                        String chanlingyu = cc.classes;
                        String chanxiangqing = cc.brief;
                        map=new HashMap<String, String>();
                        map.put("tubiao",chantubiao);
                        map.put("chanpinmingcheng",chanmingcheng);
                        map.put("chanpinjiancheng",chanjiancheng);
                        map.put("chanpinfenlei",chanfenlei);
                        map.put("lingyu",chanlingyu);
                        map.put("xiangqing",chanxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("product_information",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zizhizhzengshu) {
                    Bean.Zizhizhzengshu zi = (Bean.Zizhizhzengshu) obj;
                    for (Bean.Zizhizhzengshu.Da.detail zz : zi.data.items) {
                        String zifazhengtime ="";
                        if(StringUtils.isNotEmpty(zz.issueDate)) {
                            long zifazhengriqi = Long.parseLong(zz.issueDate);
                             zifazhengtime = simpleDateFormat.format(zifazhengriqi);
                        }
                        String zijiezhitime ="";
                        if(StringUtils.isNotEmpty(zz.toDate)) {
                            long zizijiezhiriqi = Long.parseLong(zz.toDate);
                             zijiezhitime = simpleDateFormat.format(zizijiezhiriqi);
                        }
                        String zishebeiming = zz.deviceName;
                        String zizhengshuleixing = zz.licenceType;
                        String zishebeibianhao = zz.deviceType;
                        String zixukebianhao = zz.licenceNum;
                        map=new HashMap<String, String>();
                        map.put("shebeimingcheng",zishebeiming);
                        map.put("zhengshuleixing",zizhengshuleixing);
                        map.put("fazhengriqi",zifazhengtime);
                        map.put("jiezhiriqi",zijiezhitime);
                        map.put("shebeibianhao",zishebeibianhao);
                        map.put("xukebianhao",zixukebianhao);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("qualification_certificate",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Shangbiaoxinxi) {
                    Bean.Shangbiaoxinxi shang = (Bean.Shangbiaoxinxi) obj;
                    for (Bean.Shangbiaoxinxi.Da.detail ss : shang.data.items) {
                        String shangshenqingtime ="";
                        if(StringUtils.isNotEmpty(ss.appDate)) {
                            long shangshenqingriqi = Long.parseLong(ss.appDate);
                             shangshenqingtime = simpleDateFormat.format(shangshenqingriqi);
                        }
                        String shangbiao = ss.tmPic;
                        String shangbiaoming = ss.tmName;
                        String shangzhucehao = ss.regNo;
                        String shangleibie = ss.intCls;
                        String shangzhuangtai = ss.status;
                        map=new HashMap<String, String>();
                        map.put("shenqingriqi",shangshenqingtime);
                        map.put("shangbiao",shangbiao);
                        map.put("shangbiaomingcheng",shangbiaoming);
                        map.put("zhucehao",shangzhucehao);
                        map.put("leibie",shangleibie);
                        map.put("zhuangtai",shangzhuangtai);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("trademark_information",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zhuanli) {
                    Bean.Zhuanli zhuan = (Bean.Zhuanli) obj;
                    for (Bean.Zhuanli.Da.detail zz : zhuan.data.items) {
                        String zhuanshenqinggongburi = zz.applicationPublishTime;
                        String zhuanliming = zz.patentName;
                        String zhuanshengqinghao = zz.patentNum;
                        String zhuanshenqinggongbuhao = zz.applicationPublishNum;
                        String zhuandailijigou = zz.agency;
                        String zhuanfamingren = zz.inventor;
                        String zhuandailiren = zz.agent;
                        String zhuanxiangqingtu = zz.imgUrl;
                        String zhuanfenleihao = zz.allCatNum;
                        String zhuanzhaiyao = zz.abstracts;
                        String zhuandizhi = zz.address;
                        String zhuanlileixing = zz.patentType;
                        map=new HashMap<String, String>();
                        map.put("gongburi",zhuanshenqinggongburi);
                        map.put("zhuanlimingcheng",zhuanliming);
                        map.put("shenqinghao",zhuanshengqinghao);
                        map.put("shenqinggongbuhao",zhuanshenqinggongbuhao);
                        map.put("dailijigou",zhuandailijigou);
                        map.put("famingren",zhuanfamingren);
                        map.put("dailiren",zhuandailiren);
                        map.put("xiangqingtu",zhuanxiangqingtu);
                        map.put("fenleihao",zhuanfenleihao);
                        map.put("zhaiyao",zhuanzhaiyao);
                        map.put("dizhi",zhuandizhi);
                        map.put("leixing",zhuanlileixing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("p_atent",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Zhuzuoquan) {
                    Bean.Zhuzuoquan zhu = (Bean.Zhuzuoquan) obj;
                    for (Bean.Zhuzuoquan.Da.detail zz : zhu.data.items) {
                        String zhupizhuntime ="";
                        if(StringUtils.isNotEmpty(zz.regtime)) {
                            long zhupizhunriqi = Long.parseLong(zz.regtime);
                             zhupizhuntime = simpleDateFormat.format(zhupizhunriqi);
                        }
                        String zhuruanjianquan = zz.fullname;
                        String zhuruanjianjian = zz.simplename;
                        String zhudengjihao = zz.regnum;
                        String zhufenleihao = zz.catnum;
                        String zhubanbenhao = zz.version;
                        String zhuxiangqing = zz.authorNationality;
                        map=new HashMap<String, String>();
                        map.put("pizhunriqi",zhupizhuntime);
                        map.put("ruanjianquancheng",zhuruanjianquan);
                        map.put("ruanjianjiancheng",zhuruanjianjian);
                        map.put("dengjihao",zhudengjihao);
                        map.put("fenlaihao",zhufenleihao);
                        map.put("banbenhao",zhubanbenhao);
                        map.put("xiangqing",zhuxiangqing);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("c_opyright",JSONArray.fromObject(jsonlist).toString());
                } else if (obj instanceof Bean.Wangzhanbeian) {
                    Bean.Wangzhanbeian wang = (Bean.Wangzhanbeian) obj;
                    for (Bean.Wangzhanbeian.detail ww : wang.data) {
                        String wangshenheshijian = ww.examineDate;
                        String wangzhanming = ww.webName;
                        String wangzhanshouye = ww.webSite.toString();
                        String wangyuming = ww.ym;
                        String wangbeianhao = ww.liscense;
                        String wangzhuangtai = "正常";
                        String wangdanweixingzhi = ww.companyType;
                        map=new HashMap<String, String>();
                        map.put("shenheshijian",wangshenheshijian);
                        map.put("wangzhanmingcheng",wangzhanming);
                        map.put("wangzhanshouye",wangzhanshouye);
                        map.put("yuming",wangyuming);
                        map.put("beianhao",wangbeianhao);
                        map.put("zhuangtai",wangzhuangtai);
                        map.put("danweixingzhi",wangdanweixingzhi);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("website_filing",JSONArray.fromObject(jsonlist).toString());
                }else if(obj instanceof Bean.Zhuyaorenyuan){
                    Bean.Zhuyaorenyuan zhu= (Bean.Zhuyaorenyuan) obj;
                    for(Bean.Zhuyaorenyuan.Da.detail zz:zhu.data.result){
                        String zhiwu=zz.typeJoin.toString();
                        String ming=zz.name;
                        map=new HashMap<String, String>();
                        map.put("zhiwu",zhiwu);
                        map.put("ming",ming);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("leading_member",JSONArray.fromObject(jsonlist).toString());
                }else if(obj instanceof Bean.Gudongxinxi){
                    Bean.Gudongxinxi gu= (Bean.Gudongxinxi) obj;
                    for(Bean.Gudongxinxi.Da.detail gg:gu.data.result){
                        String ming=gg.name;
                        String chuzibili="";
                        String renjiaochuzi="";
                        if(gg.capital!=null){
                             chuzibili=gg.capital.get(0).percent;
                             renjiaochuzi=gg.capital.get(0).amomon+" "+gg.capital.get(0).time;
                        }
                        map=new HashMap<String, String>();
                        map.put("ming",ming);
                        map.put("chuzibili",chuzibili);
                        map.put("renjiaochuzi",renjiaochuzi);
                        jsonlist.add(JSONObject.fromObject(map));
                    }
                    js.jin("shareholder_Information",JSONArray.fromObject(jsonlist).toString());
                }
            }
            ruku(con,js);
        }
    }




    public static void get(String[] str,Proxy proxy, final Json js, final Connection con) throws UnsupportedEncodingException, SQLException {
        Gson gson=new Gson();
        String tid = str[0];
        String quancheng=str[1];

        final List<Object> list=new ArrayList<Object>();

        for (int i = 1; i <= 1000; i++) {
            try {
                String duiwaitouzi = qingqiu(proxy, "http://www.tianyancha.com/expanse/inverst.json?id=" + tid + "&ps=20&pn="+i);
                Bean.Duiwaitouzi duiwai = gson.fromJson(duiwaitouzi, Bean.Duiwaitouzi.class);
                if (StringUtils.isEmpty(duiwai.message) &&duiwai.data!=null&& duiwai.data.result != null && duiwai.data.result.size() > 0) {
                    list.add(duiwai);
                } else {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        for (int i = 1; i <= 1000; i++) {
            try {
                String zhongyaorenyuan = qingqiu(proxy, "http://www.tianyancha.com/expanse/staff.json?id="+tid+"&ps=20&pn="+i);
                Bean.Zhuyaorenyuan zhuyao = gson.fromJson(zhongyaorenyuan, Bean.Zhuyaorenyuan.class);
                if (StringUtils.isEmpty(zhuyao.message) &&zhuyao.data!=null&& zhuyao.data.result != null && zhuyao.data.result.size() > 0) {
                    list.add(zhuyao);
                } else {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        for (int i = 1; i <= 1000; i++) {
            try {
                String gudongxinxi = qingqiu(proxy, "http://www.tianyancha.com/expanse/holder.json?id="+tid+"&ps=20&pn="+i);
                Bean.Gudongxinxi gu = gson.fromJson(gudongxinxi, Bean.Gudongxinxi.class);
                if (StringUtils.isEmpty(gu.message) &&gu.data!=null&& gu.data.result != null && gu.data.result.size() > 0) {
                    list.add(gu);
                } else {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        for(int i=1;i<=1000;i++) {
            try {
                String biangeng = qingqiu(proxy, "http://www.tianyancha.com/expanse/changeinfo.json?id=" + tid + "&ps=20&pn=" + i);
                Bean.Biangeng bian = gson.fromJson(biangeng, Bean.Biangeng.class);
                if (StringUtils.isEmpty(bian.message) &&bian.data!=null&& bian.data.result != null && bian.data.result.size() > 0) {
                    list.add(bian);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++) {
            try {
                String fenzhi = qingqiu(proxy, "http://www.tianyancha.com/expanse/branch.json?id=" + tid + "&ps=20&pn=" + i);
                Bean.Fenzhi fen = gson.fromJson(fenzhi, Bean.Fenzhi.class);
                if (StringUtils.isEmpty(fen.message) &&fen.data!=null&& fen.data.result != null && fen.data.result.size() > 0) {
                    list.add(fen);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++) {
            try {
                String rongzilishi = qingqiu(proxy, "http://www.tianyancha.com/expanse/findHistoryRongzi.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Finacning fin = gson.fromJson(rongzilishi, Bean.Finacning.class);
                if (fin.data!=null&&fin.data.page!=null&&fin.data.page.rows != null && fin.data.page.rows.size() > 0) {
                    list.add(fin);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++) {
            try {
                String hexintuandui = qingqiu(proxy, "http://www.tianyancha.com/expanse/findTeamMember.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Hexintuandui hexin = gson.fromJson(hexintuandui, Bean.Hexintuandui.class);
                if (hexin.data!=null&&hexin.data.page!=null&&hexin.data.page.rows != null && hexin.data.page.rows.size() > 0) {
                    list.add(hexin);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++) {
            try {
                String qiyeyewu = qingqiu(proxy, "http://www.tianyancha.com/expanse/findProduct.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Qiyeyewu qi = gson.fromJson(qiyeyewu, Bean.Qiyeyewu.class);
                if (qi.data!=null&&qi.data.page!=null&&qi.data.page.rows != null && qi.data.page.rows.size() > 0) {
                    list.add(qi);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }



        for(int i=1;i<=1000;i++){
            try {
                String touzishijian = qingqiu(proxy, "http://www.tianyancha.com/expanse/findTzanli.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Touzishijian touzi = gson.fromJson(touzishijian, Bean.Touzishijian.class);
                if (touzi.data!=null&&touzi.data.page!=null&&touzi.data.page.rows != null && touzi.data.page.rows.size() > 0) {
                    list.add(touzi);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }

        for(int i=1;i<=1000;i++){
            try {
                String jingpinxinxi = qingqiu(proxy, "http://www.tianyancha.com/expanse/findJingpin.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Jingpinxinxi jingpin = gson.fromJson(jingpinxinxi, Bean.Jingpinxinxi.class);
                if (jingpin.data!=null&&jingpin.data.page!=null&&jingpin.data.page.rows != null && jingpin.data.page.rows.size() > 0) {
                    list.add(jingpin);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }

        for(int i=1;i<=1000;i++){
            try {
                String falvsusong = qingqiu(proxy, "http://www.tianyancha.com/v2/getlawsuit/" + URLEncoder.encode(quancheng, "UTF-8") + ".json?page=" + i + "&ps=20");
                Bean.Falvsusong falv = gson.fromJson(falvsusong, Bean.Falvsusong.class);
                if (falv.data!=null&&falv.data.items != null && falv.data.items.size() > 0) {
                    list.add(falv);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }

        String fayuangonggao = qingqiu(proxy, "http://www.tianyancha.com/v2/court/" + URLEncoder.encode(quancheng, "UTF-8") + ".json?ps=20");
        try {
            Bean.Fayuangonggao fayuan = gson.fromJson(fayuangonggao, Bean.Fayuangonggao.class);
            if (fayuan.courtAnnouncements != null && fayuan.courtAnnouncements.size() > 0) {
                list.add(fayuan);
            }
        }catch (Exception e){

        }

        for(int i=1;i<=1000;i++){
            try {
                String beizhixingren = qingqiu(proxy, "http://www.tianyancha.com/expanse/zhixing.json?id=" + tid + "&pn=" + i + "&ps=20");
                Bean.Beizhixingren bei = gson.fromJson(beizhixingren, Bean.Beizhixingren.class);
                if (StringUtils.isEmpty(bei.message) &&bei.data!=null&& bei.data.items != null && bei.data.items.size() > 0) {
                    list.add(bei);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        String shixinren = qingqiu(proxy, "http://www.tianyancha.com/v2/dishonest/" + URLEncoder.encode(quancheng, "UTF-8") + ".json");
        try {
            Bean.Shixinren shi = gson.fromJson(shixinren, Bean.Shixinren.class);
            if (shi.data != null && shi.data.items != null && shi.data.items.size() > 0) {
                list.add(shi);
            }
        }catch (Exception e){

        }


        String jingyingyichang = qingqiu(proxy, "http://www.tianyancha.com/expanse/abnormal.json?id=" + tid + "&ps=20&pn=1");
        try {
            Bean.Jingyingyichang jing = gson.fromJson(jingyingyichang, Bean.Jingyingyichang.class);
            if (jing.data != null && jing.data.result != null && jing.data.result.size() > 0) {
                list.add(jing);
            }
        }catch (Exception e){

        }

        for(int i=1;i<=1000;i++){
            try {
                String xingzhengchufa = qingqiu(proxy, "http://www.tianyancha.com/expanse/punishment.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Xingzhengchufa xing = gson.fromJson(xingzhengchufa, Bean.Xingzhengchufa.class);
                if (xing.data!=null&&xing.data.items != null && xing.data.items.size() > 0) {
                    list.add(xing);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        String yanzhongweifa = qingqiu(proxy, "http://www.tianyancha.com/expanse/illegal.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=1");
        try {
            Bean.Yanzhongweifa yan = gson.fromJson(yanzhongweifa, Bean.Yanzhongweifa.class);
            if (yan.data != null && yan.data.items != null && yan.data.items.size() > 0) {
                list.add(yan);
            }
        }catch (Exception e){

        }


        for(int i=1;i<=1000;i++){
            try {
                String guquanchuzhi = qingqiu(proxy, "http://www.tianyancha.com/expanse/companyEquity.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Guquanchuzhi gu = gson.fromJson(guquanchuzhi, Bean.Guquanchuzhi.class);
                if (gu.data!=null&&gu.data.items != null && gu.data.items.size() > 0) {
                    list.add(gu);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        String dongchandiya = qingqiu(proxy, "http://www.tianyancha.com/expanse/mortgageInfo.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=1&ps=20");
        try {
            Bean.Dongchandiya dongchan = gson.fromJson(dongchandiya, Bean.Dongchandiya.class);
            if (StringUtils.isNotEmpty(dongchan.data)) {
                list.add(dongchan);
            }
        }catch (Exception e){

        }

        for(int i=1;i<=1000;i++){
            try {
                String qianshuigonggao = qingqiu(proxy, "http://www.tianyancha.com/expanse/owntax.json?id=" + tid + "&ps=5&pn=" + i);
                Bean.Qianshuigonggao qian = gson.fromJson(qianshuigonggao, Bean.Qianshuigonggao.class);
                if (qian.data!=null&&qian.data.items != null && qian.data.items.size() > 0) {
                    list.add(qian);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String zhaotoubiao = qingqiu(proxy, "http://www.tianyancha.com/expanse/bid.json?id=" + tid + "&pn=" + i + "&ps=20");
                Bean.Zhaotoubiao zhao = gson.fromJson(zhaotoubiao, Bean.Zhaotoubiao.class);
                if (zhao.data!=null&&zhao.data.items != null && zhao.data.items.size() > 0) {
                    list.add(zhao);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String zhaiquanxinxi = qingqiu(proxy, "http://www.tianyancha.com/extend/getBondList.json?companyName=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=20");
                Bean.Zhaiquanxinxi zhai = gson.fromJson(zhaiquanxinxi, Bean.Zhaiquanxinxi.class);
                if (zhai.data!=null&&zhai.data.bondList != null && zhai.data.bondList.size() > 0) {
                    list.add(zhai);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }



        for(int i=1;i<=1000;i++){
            try {
                String goudixinxi = qingqiu(proxy, "http://www.tianyancha.com/expanse/purchaseland.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=20&pn=" + i);
                Bean.Goudixinxi gou = gson.fromJson(goudixinxi, Bean.Goudixinxi.class);
                if (gou.data!=null&&gou.data.companyPurchaseLandList != null && gou.data.companyPurchaseLandList.size() > 0) {
                    list.add(gou);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String zhaopin = qingqiu(proxy, "http://www.tianyancha.com/extend/getEmploymentList.json?companyName=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=20");
                Bean.Zhaopin zhao = gson.fromJson(zhaopin, Bean.Zhaopin.class);
                if (zhao.data!=null&&zhao.data.companyEmploymentList != null && zhao.data.companyEmploymentList.size() > 0) {
                    list.add(zhao);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String shuiwupingji = qingqiu(proxy, "http://www.tianyancha.com/expanse/taxcredit.json?id=" + tid + "&ps=5&pn=" + i);
                Bean.Shuiwupingji shui = gson.fromJson(shuiwupingji, Bean.Shuiwupingji.class);
                if (shui.data!=null&&shui.data.items != null && shui.data.items.size() > 0) {
                    list.add(shui);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String choucha = qingqiu(proxy, "http://www.tianyancha.com/expanse/companyCheckInfo.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=20");
                Bean.Choucha chou = gson.fromJson(choucha, Bean.Choucha.class);
                if (chou.data!=null&&chou.data.items != null && chou.data.items.size() > 0) {
                    list.add(chou);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String chanpin = qingqiu(proxy, "http://www.tianyancha.com/expanse/appbkinfo.json?id=" + tid + "&ps=5&pn=" + i);
                Bean.Chanpin chan = gson.fromJson(chanpin, Bean.Chanpin.class);
                if (chan.data!=null&&chan.data.items != null && chan.data.items.size() > 0) {
                    list.add(chan);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String zizhizhengshu = qingqiu(proxy, "http://www.tianyancha.com/expanse/qualification.json?id=" + tid + "&ps=5&pn=" + i);
                Bean.Zizhizhzengshu zi = gson.fromJson(zizhizhengshu, Bean.Zizhizhzengshu.class);
                if (zi.data!=null&&zi.data.items != null && zi.data.items.size() > 0) {
                    list.add(zi);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String shangbiaoxinxi = qingqiu(proxy, "http://www.tianyancha.com/tm/getTmList.json?id=" + tid + "&pageNum=" + i + "&ps=20");
                Bean.Shangbiaoxinxi shang = gson.fromJson(shangbiaoxinxi, Bean.Shangbiaoxinxi.class);
                if (shang.data!=null&&shang.data.items != null && shang.data.items.size() > 0) {
                    list.add(shang);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        for(int i=1;i<=1000;i++){
            String zhuanli ="";
            try {
                String zhuanlis = qingqiu(proxy, "http://www.tianyancha.com/expanse/patent.json?id=" + tid + "&pn=" + i + "&ps=20").replace("\n","");
                zhuanli=jsonString(zhuanlis);
                Bean.Zhuanli zhuan = gson.fromJson(zhuanli, Bean.Zhuanli.class);
                if (zhuan.data!=null&&zhuan.data.items != null && zhuan.data.items.size() > 0) {
                    list.add(zhuan);
                } else {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("**************************"+tid+"*******"+i);
            }
        }


        for(int i=1;i<=1000;i++){
            try {
                String zhuzuoquan = qingqiu(proxy, "http://www.tianyancha.com/expanse/copyReg.json?id=" + tid + "&pn=" + i + "&ps=20");
                Bean.Zhuzuoquan zhu = gson.fromJson(zhuzuoquan, Bean.Zhuzuoquan.class);
                if (zhu.data != null && zhu.data.items!=null&&zhu.data.items.size() > 0) {
                    list.add(zhu);
                } else {
                    break;
                }
            }catch (Exception e){

            }
        }


        String wangzhanbeian = qingqiu(proxy, "http://www.tianyancha.com/v2/IcpList/" + tid + ".json");
        try {
            Bean.Wangzhanbeian wang = gson.fromJson(wangzhanbeian, Bean.Wangzhanbeian.class);
            if (wang.data != null && wang.data.size() > 0) {
                list.add(wang);
            }
        }catch (Exception e){

        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pinjie(list,js,con);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }


    private static String jsonString(String s){
        char[] temp = s.toCharArray();
        int n = temp.length;
        for(int i =0;i<n;i++){
            if(temp[i]==':'&&temp[i+1]=='"'){
                for(int j =i+2;j<n;j++){
                    if(temp[j]=='"'){
                        if(temp[j+1]!=',' &&  temp[j+1]!='}'){
                            temp[j]='”';
                        }else if(temp[j+1]==',' ||  temp[j+1]=='}'){
                            break ;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }




    public static String getString(Document doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getString(Element doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static String getHref(Document doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getHref(Element doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static Elements getElements(Document doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select,int a,String select2){
        Elements ele=null;
        try{
            ele=doc.select(select).get(a).select(select2);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }


}
