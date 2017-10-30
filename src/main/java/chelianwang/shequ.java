package chelianwang;

import Utils.Dup;
import Utils.JsoupUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class shequ {
    private static Map<String,String> map;

    // 代理隧道验证信息
    final static String ProxyUser = "H88A4Q10G0V31YCD";
    final static String ProxyPass = "C2E28C06C89836C4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static java.sql.Connection conn;
    private static int su=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }
    public static void main(String args[]) throws IOException {
        shequ s=new shequ();
        final Ca c=s.new Ca();
        final Se ss=s.new Se();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    controller();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serach(ss);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int y=1;y<=10;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ses(c,ss);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void controller() throws IOException, InterruptedException {
        Random r=new Random();
        while (true){
            login();
            Thread.sleep((r.nextInt(20)+30)*60*1000);
        }
    }

    public static void serach(Se s) throws IOException, InterruptedException {
        Document doc=gets("http://i.gasgoo.com/supplier/");
        Elements ele= JsoupUtils.getElements(doc,"div.proCat li");
        for(Element e:ele){
            String ur=JsoupUtils.getHref(e,"a","href",0);
            String lei=JsoupUtils.getString(e,"a",0);
            s.fang(new String[]{ur,lei});
        }
    }

    public static void ses(Ca c,Se s) throws InterruptedException, IOException {
        while (true) {
            String[] va=s.qu();
            String ur=va[0];
            String lei=va[1];
            for (int p = 1; p <= 1000; p++) {
                Document docs = null;
                if (!ur.contains("http")) {
                    docs = gets("http://i.gasgoo.com"+ur);
                    Elements eles=JsoupUtils.getElements(docs,"div.companyInfo");
                    for(Element es:eles){
                        String url=JsoupUtils.getHref(es,"dl dt h3 a","href",0);
                        c.fang(new String[]{url,lei});
                    }
                    break;
                } else {
                    docs = get(ur.replace(".html", "") + "/index-" + p + ".html");
                    Elements eles = JsoupUtils.getElements(docs, "div.searchSupplierContent dl.companyDl");
                    for (Element es : eles) {
                        String url = JsoupUtils.getHref(es, "h3 a", "href", 0);
                        c.fang(new String[]{url, lei});
                    }
                }
                String xn=JsoupUtils.getHref(docs,"div.pageNav a.next","href",0);
                if(!Dup.nullor(xn)){
                    break;
                }
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into chelianwang_shequ(comp_full_name,comp_logo,comp_xing,comp_diqu,comp_web,comp_ct,comp_yf,comp_yuan,comp_faren,comp_zhuzi,comp_nc,comp_zz,comp_zl,comp_nx,comp_dizhi,comp_desc,comp_zy,comp_pt,comp_ck,comp_sc,comp_zb,comp_fenlei) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String[] va = c.qu();
                Document doc = get(va[0]);
                String quan = JsoupUtils.getString(doc, "div.ordinaryBanenr", 0);
                if (!Dup.nullor(quan)) {
                    quan = JsoupUtils.getString(doc, "div.banner1420 div.banenr h1", 0);
                }
                String logo = "http://i.gasgoo.com" + JsoupUtils.getHref(doc, "div.major div.left div.infoA ul.firmA li:contains(公司标识) a img", "src", 0);
                String xingzhi = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(公司性质) label", 0);
                String diqu = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(公司地区) label", 0);
                String web = JsoupUtils.getHref(doc, "div.major div.left div.infoA ul.firmA li:contains(公司网址) div.qua a", "href", 0);
                String chenglishijian = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(成立时间) label", 0);
                String yanfa = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(研发人数) label", 0);
                String yuangong = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(员工人数) label", 0);
                String faren = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(法人代表) label", 0);
                String zhuce = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(注册资金) label", 0);
                String nianchan = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(年 产 值) label", 0);
                String zongzichan = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(总 资 产) label", 0);
                String zhiliang = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(质量体系) div.qua label", 0);
                String nianxiao = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(年销售额) label", 0);
                String dizhi = JsoupUtils.getString(doc, "div.vehicle.topBar ul li", 3);
                String jieshao = JsoupUtils.getString(doc, "div.right div.vehicle dl.presentation dd div", 0);
                String zhuying = JsoupUtils.getString(doc, "div.right div.vehicle ul.form li:contains(主营产品) label", 0);
                String peitao = JsoupUtils.getString(doc, "div.right div.vehicle ul.form li:contains(配套客户) label", 0);
                String chukou = JsoupUtils.getString(doc, "div.right div.vehicle ul.form li:contains(出口市场) label", 0);
                String shengche = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(生产车型) label", 0);
                String ziben = JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(资本构成) label", 0);
                String fenlei=JsoupUtils.getString(doc, "div.major div.left div.infoA ul.firmA li:contains(公司分类) label", 0);

                ps.setString(1, quan);
                ps.setString(2, logo);
                ps.setString(3, xingzhi);
                ps.setString(4, diqu);
                ps.setString(5, web);
                ps.setString(6, chenglishijian);
                ps.setString(7, yanfa);
                ps.setString(8, yuangong);
                ps.setString(9, faren);
                ps.setString(10, zhuce);
                ps.setString(11, nianchan);
                ps.setString(12, zongzichan);
                ps.setString(13, zhiliang);
                ps.setString(14, nianxiao);
                ps.setString(15, dizhi);
                ps.setString(16, jieshao);
                ps.setString(17, zhuying);
                ps.setString(18, peitao);
                ps.setString(19, chukou);
                ps.setString(20, shengche);
                ps.setString(21, ziben);
                if(Dup.nullor(fenlei)){
                    ps.setString(22,fenlei);
                }else{
                    ps.setString(22,va[1]);
                }
                ps.executeUpdate();
                su++;
                System.out.println(su + "*******************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }


    public static Document gets(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(5000)
                        .cookies(map)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && doc.outerHtml().length() > 44) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time our error");
            }
        }
        return doc;
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(10000)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && doc.outerHtml().length() > 44) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time our error");
            }
        }
        return doc;
    }

    public static void login() throws IOException {
        Connection.Response doc= Jsoup.connect("http://i.gasgoo.com/gasgoo/cn/sns20/webmodel/handler/loginhandler.ajax")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .header("Accept","*/*")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.8")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Referer","http://i.gasgoo.com/login.aspx?return=http://i.gasgoo.com/index.aspx")
                .header("Origin","http://i.gasgoo.com")
                .header("Host","i.gasgoo.com")
                .data("txtUserName","2471264637@qq.com")
                .data("txtPassword","linshi123456")
                .data("checkAutoLogin","true")
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute();
        System.out.println(doc.cookies());
        map=doc.cookies();
    }


    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Se{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
