package tianyancha.quanxinxi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/6/14.
 */
public class Tyc_sousuo {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        Tyc_sousuo ty=new Tyc_sousuo();
        final cangku c=ty.new cangku();
        final Ipchi i=ty.new Ipchi();

        ExecutorService pool= Executors.newFixedThreadPool(11);

        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=10;x++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(c, finalCon,i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /*pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getip(i);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }



    public static void get(cangku c,Connection con,Ipchi ips) throws InterruptedException, IOException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        WebDriver driver=getdriver(cap,ips);
        while (true) {
            String value[]=c.qu();
            if(value==null){
                break;
            }
            String quancheng=value[0];
            String xuhao=value[1];
            String keys= URLEncoder.encode(quancheng,"UTF-8");
            Document doc = null;

            while (true) {
                int sleep = 0;
                try {
                    driver.get("http://www.tianyancha.com/search?key="+keys+"&checkFrom=searchBox");
                    while (true){
                        Thread.sleep(500);
                        sleep=sleep+500;
                        if(driver.getPageSource().contains(quancheng)){
                            break;
                        }else if(sleep>=20000){
                            break;
                        }
                    }
                    doc = Jsoup.parse(driver.getPageSource());
                    if (doc.outerHtml().contains(quancheng)) {
                        storedata(con,doc,quancheng,xuhao);
                        break;
                    }
                } catch (Exception e) {
                    try {
                        driver.quit();
                    }catch (Exception e1){
                        System.out.println("driver quit");
                    }
                    while (true) {
                        try {
                            driver=getdriver(cap,ips);
                            break;
                        }catch (Exception e1){
                            System.out.println("error");
                        }
                    }
                }
            }
            try {
                driver.quit();
            }catch (Exception e){
                System.out.println("driver quit");
            }
            while (true) {
                try {
                    driver=getdriver(cap,ips);
                    break;
                }catch (Exception e1){
                    System.out.println("error");
                }
            }
        }



    }

    public static void getip(Ipchi ips) throws IOException, InterruptedException {
        Document doc=null;
        while (true) {
            try {
                 doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=00b1c1dbec239455d92d87b98145951c&sep=3")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                if(doc!=null&&!doc.outerHtml().contains("too many")){
                    String ip=doc.outerHtml().replace("<html>","").replace(" <head></head>","").replace("</body>","").replace("<body>","").replace("</html>","").replace("\n","").trim();
                    ips.fang(ip);
                }
            }catch (Exception e){

            }
            Thread.sleep(1000);
        }
    }



    public static void storedata(Connection con,Document doc,String yuancheng,String xuhao) throws SQLException {
        String sql="insert into tyc_jichu_sh(quan_cheng,logo,fa_ren,zhuce_ziben,zhuce_shijian,jingying_zhuangtai,t_id,xu_hao,yuan_cheng) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        Elements liebiao=getElements(doc,"div.search_result_single.search-2017.pb20.pt20.pl30.pr30.ng-scope");
        if(liebiao!=null){
            for(Element ele:liebiao){
                String logo=getHref(ele,"div.mr20.search_left_icon img","src",0);
                String quancheng=getString(ele,"div.col-xs-10.search_repadding2.f18 a",0);
                String tid=getHref(ele,"div.col-xs-10.search_repadding2.f18 a","href",0).split("/",5)[4];
                String statu=getString(ele,"div.position-abs.statusTypeNor.ng-binding.statusType1",0);
                String faren=getString(ele,"div.title.overflow-width:contains(法定代表人) span",0);
                String zhuceziben=getString(ele,"div.title.overflow-width:contains(注册资本) span",0);
                String zhuceshijian=getString(ele,"div.title.overflow-width:contains(注册时间) span",0);

                ps.setString(1,quancheng);
                ps.setString(2,logo);
                ps.setString(3,faren);
                ps.setString(4,zhuceziben);
                ps.setString(5,zhuceshijian);
                ps.setString(6,statu);
                ps.setString(7,tid);
                ps.setString(8,xuhao);
                ps.setString(9,yuancheng);
                ps.addBatch();
            }
            System.out.println("begin");
            ps.executeBatch();
            System.out.println("ok**************************************************************");
        }

    }



    public static void data(Connection con,cangku c) throws SQLException, IOException, InterruptedException {
        int p=0;
        for(int x=1;x<=20;x++) {
            String sql = "select id,`Name` from qichacha_search where Province='SH' and `id` not in (select xu_hao from tyc_jichu_sh) limit "+p+",100000";
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("kaishi duqu");
            ResultSet rs = ps.executeQuery();
            System.out.println("duqu success");
            while (rs.next()) {
                String quancheng = rs.getString(rs.findColumn("Name"));
                String xuhao = rs.getString(rs.findColumn("id"));

                String st[] = new String[]{ quancheng, xuhao};
                c.fang(st);
            }
            p=p+100000;
        }

    }


    public static WebDriver  getdriver(DesiredCapabilities cap,Ipchi ips) throws IOException {
        cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--proxy-type=http", "--proxy=proxy.abuyun.com:9020","--proxy-auth=H4XGPM790E93518D:2835A47D56143D62"});
        System.setProperty(Count.phantomjs, Count.linuxph);
        WebDriver driver = new PhantomJSDriver(cap);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        return driver;
    }




    class cangku{
        BlockingQueue<String[]> blo=new LinkedBlockingQueue<String[]>(10000);
        public void fang(String key[]) throws InterruptedException {
            blo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return blo.poll(600, TimeUnit.SECONDS);
        }
    }

    class Ipchi{
        BlockingQueue<String> blo=new LinkedBlockingQueue<String>(10000);
        public void fang(String key) throws InterruptedException {
            blo.put(key);
        }
        public String qu() throws InterruptedException {
            return blo.poll(60, TimeUnit.SECONDS);
        }
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
