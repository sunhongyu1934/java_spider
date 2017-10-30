package tianyancha.XinxiXin;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/7/25.
 */
public class Tyc_bulu {
    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        // 代理隧道验证信息
        final  String ProxyUser = args[0];
        final  String ProxyPass = args[1];

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        final String lo=args[2];
        String xian=args[3];
        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection fincon=con;
        Tyc_bulu t=new Tyc_bulu();
        final Cang c=t.new Cang();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(fincon,c,lo);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=Integer.parseInt(xian);x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(proxy,c,fincon);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void data(Connection con,Cang c,String lo) throws SQLException, InterruptedException {
        String sql="select cname from tyc_cs_bulu where id not in (select a.id from tyc_cs_bulu a join tyc_jichu_chuisou b on a.cname=b.quan_cheng) limit "+lo+",2746";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("cname"));
            c.fang(cname);
        }
    }

    public static void get(Proxy proxy,Cang c,Connection con) throws InterruptedException, SQLException {
        while (true) {
            try {
                String cname = c.qu();
                System.out.println(c.c.size());
                if (cname == null) {
                    break;
                }
                String tid=serach(cname,proxy);
                if(tid!=null) {
                    Document doc;
                    while (true) {
                        try {
                            doc = Jsoup.connect("http://www.tianyancha.com/company/" + tid)
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                    .timeout(10000)
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .proxy(proxy)
                                    .get();
                            if (!doc.outerHtml().contains("获取验证码") && !doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("time out");
                        }
                    }
                    ruku(con, doc, tid);
                }else{
                    System.out.println("000000000000000000000000000000000000000");
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static String serach(String cname,Proxy proxy){
        Document doc = null;
        while (true) {
            try {
                doc = Jsoup.connect("http://www.tianyancha.com/search/p" + 1 + "?key=" + URLEncoder.encode(cname, "utf-8"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(10000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&& !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out");
            }
        }
        Elements eles = getElements(doc, "div.search_result_single.search-2017.pb20.pt20.pl30.pr30");
        if(eles!=null){
            for(Element e:eles){
                String tid=getHref(e, "div.col-xs-10.search_repadding2.f18 a", "href", 0).replace("https://www.tianyancha.com/company/", "");
                return tid;
            }
        }
        return null;
    }

    public static void  ruku(Connection con,Document doc,String tid) throws SQLException {
        String sql="insert into tyc_jichu_chuisou(quan_cheng,ceng_yongming,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,hezhun_riqi,jingying_zhuangtai,nashui_shibie,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,t_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        String sql2="insert into tyc_mainchengyuan(t_id,zhi_wu,p_name,p_tid) values(?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into tyc_gudong(t_id,p_name,p_tid,chuzi_bili,renjiao_chuzi) values(?,?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);
        int a=0;
        try{
            String quancheng=getString(doc,"div.company_header_width.ie9Style span.f18.in-block.vertival-middle",0);
            String ceng=getString(doc,"div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4",0);
            String phone=getString(doc,"div.f14.new-c3.mt10 div.in-block.vertical-top:contains(电话) span",1);
            String email=getString(doc,"div.f14.new-c3.mt10 div.in-block.vertical-top:contains(邮箱) span",1);
            String web=getString(doc,"div.f14.new-c3 div.in-block.vertical-top:contains(网址) a",0);
            String address=getString(doc,"div.f14.new-c3 div.in-block.vertical-top:contains(地址) span",1);
            String logo=getHref(doc,"div.b-c-white.new-border.over-hide.mr10.ie9Style img","src",0);
            String zhuceziben=getString(doc,"div.new-border-bottom:contains(注册资本) div.pb10",0);
            String zhuceshijian=getString(doc,"div.new-border-bottom:contains(注册时间) div.pb10",0);
            String statu=getString(doc,"div.pt10:contains(企业状态) div.baseinfo-module-content-value.statusType1",0);
            String gongshang=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(工商注册号) span",0);
            String zuzhijigou=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(组织机构代码) span",0);
            String tongyixinyong=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(统一信用代码) span",0);
            String qiyeleixing=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(企业类型) span",0);
            String nashuiren=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(纳税人识别号) span",0);
            String hangye=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(行业) span",0);
            String yingyeqixian=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(营业期限) span",0);
            String hezhunriqi=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(核准日期) span",0);
            String dengjijiguan=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(登记机关) span",0);
            String zhucedizhi=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(注册地址) span",0);
            String jingyingfanwei=getString(doc,"div.row.b-c-white.base2017 table tbody td.basic-td:contains(经营范围) span.js-full-container",0);
            String faren=getString(doc,"div.in-block.vertical-top.pl15 div.new-c3.f18.overflow-width a",0);

            ps.setString(1,quancheng);
            ps.setString(2,ceng);
            ps.setString(3,logo);
            ps.setString(4,phone);
            ps.setString(5,email);
            ps.setString(6,address);
            ps.setString(7,web);
            ps.setString(8,faren);
            ps.setString(9,zhuceziben);
            ps.setString(10,zhuceshijian);
            ps.setString(11,hezhunriqi);
            ps.setString(12,statu);
            ps.setString(13,nashuiren);
            ps.setString(14,gongshang);
            ps.setString(15,zuzhijigou);
            ps.setString(16,tongyixinyong);
            ps.setString(17,qiyeleixing);
            ps.setString(18,hangye);
            ps.setString(19,yingyeqixian);
            ps.setString(20,dengjijiguan);
            ps.setString(21,zhucedizhi);
            ps.setString(22,jingyingfanwei);
            ps.setString(23,tid);
            ps.executeUpdate();;

            Elements zele=getElements(doc,"div#_container_staff div.clearfix div.staffinfo-module-container");
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

            Elements gele=getElements(doc,"div#_container_holder table.table tbody tr");
            if(gele!=null){
                for(Element e:gele){
                    String guming=getString(e,"a.in-block.vertival-middle.overflow-width",0);
                    String gtid=getHref(e,"a.in-block.vertival-middle.overflow-width","href",0).replace("/human/","").replace("/company/","");
                    String bili=getString(e,"span.c-money-y",0);
                    String renjiao=getString(e,"td",2).replace("\n","").replace(" ","");

                    ps3.setString(1,tid);
                    ps3.setString(2,guming);
                    ps3.setString(3,gtid);
                    ps3.setString(4,bili);
                    ps3.setString(5,renjiao);
                    ps3.executeUpdate();
                }
            }
            a++;
            System.out.println(a+"***********************************************************");
        }catch (Exception e){
            System.out.println("ruku error");
        }

    }


    class Cang{
        BlockingQueue<String> c=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            c.put(key);
        }
        public String qu() throws InterruptedException {
            return c.poll(60, TimeUnit.SECONDS);
        }
    }
}
