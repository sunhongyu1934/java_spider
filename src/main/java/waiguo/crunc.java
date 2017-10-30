package waiguo;

import Utils.Dup;
import Utils.JsoupUtils;
import baidu.RedisAction;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class crunc {
    private static Ca c;
    private static Connection conn;
    private static int a=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
    public static void main(String args[]) throws IOException, InterruptedException, SQLException {
        crunc cr=new crunc();
        final Cs cc=cr.new Cs();
        final Ka k=cr.new Ka();
        final Ba b=cr.new Ba();
        Ca ccc=new Ca();
        c=ccc;
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int p=1;p<=10;p++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(cc,b);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int aa=1;aa<=10;aa++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        de(k, b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    du(k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=10;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ren(cc);
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

        for(int pp=1;pp<=1;pp++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        getip();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void du(Ka k) throws SQLException, InterruptedException {
        String sql0="select DISTINCT org_name from crunchbase_org where org_name not in (select org_name from crunchbase_funding)";
        PreparedStatement ps0=conn.prepareStatement(sql0);
        ResultSet rs=ps0.executeQuery();
        while (rs.next()){
            String kk=rs.getString(rs.findColumn("org_name"));
            k.fang(kk);
        }
    }

    public static void de(Ka k,Ba b) throws IOException, InterruptedException {
        while (true) {
            String value=k.qu();
            Document doc = get("https://www.crunchbase.com/textsearch?q="+value);
            Elements ele = JsoupUtils.getElements(doc, "identifier-formatter");
            for (Element e : ele) {
                String url = JsoupUtils.getHref(e, "span.component--field-formatter.field-type-identifier a.cb-link.layout-row.layout-align-start-center", "href", 0);
                String na=JsoupUtils.getString(e, "span.component--field-formatter.field-type-identifier a.cb-link.layout-row.layout-align-start-center", 0);
                if(Dup.nullor(url)&&value.equals(na)) {
                    b.fang(url.replace("/organization/", ""));
                    break;
                }
            }
        }
    }

    public static void serach(Cs c,Ba b) throws SQLException, IOException, InterruptedException {
        String sql = "insert into crunchbase_org(org_name,logo_url,add_ress,Categories,Sub_Organization,Founded_Date,Founders,Operating_Status,Funding_Status,Last_Funding_Type,Employees,Stock_Symbol,Company_Type,Exits_Number,Website,LinkedIn,Facebook,Twitter,Contact_Email,intro) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        String sql2 = "insert into crunchbase_funding(org_name,Announced_Date,Transaction_Name,Investors_Number,Money_Raised,Lead_Investors,funding_number,total_amount) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps2 = conn.prepareStatement(sql2);


        while (true) {
            try {
                String va = b.qu();
                Document doc = get("https://www.crunchbase.com/organization/" + va);
                System.out.println(va);
                Elements ele = JsoupUtils.getElements(doc, "div.component--image-list-card div.imageListWrapper.layout-row.layout-wrap.cb-width-100 div.flex-100.layout-row.layout-align-start-center.imageListItem.flex-gt-sm-50");
                String logo = JsoupUtils.getHref(doc, "meta[property=og:image]", "content", 0);
                String quan = JsoupUtils.getString(doc, "div.mat-toolbar-layout h1.flex.cb-overflow-ellipsis", 0);
                StringBuffer adds = new StringBuffer();
                Elements addele = doc.select("span.component--field-formatter.field-type-identifier-multi").get(0).select("a");
                for (Element e : addele) {
                    adds.append(e.text() + ",");
                }
                String add = ss(adds);
                Elements cateele = JsoupUtils.getElements(doc, "div.component--fields-card div.field-row:contains(Categories) span.component--field-formatter.field-type-identifier-multi a");
                StringBuffer cates = new StringBuffer();
                for (Element e : cateele) {
                    cates.append(e.text() + ",");
                }
                String cate = ss(cates);
                String sub = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Sub-Organization of) span.flex.cb-overflow-ellipsis.identifier-label", 0);
                String fod = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Founded Date) span.component--field-formatter.field-type-date_precision", 0);

                Elements fosele = JsoupUtils.getElements(doc, "div.component--fields-card div.field-row:contains(Founders) span.component--field-formatter.field-type-identifier-multi a");
                StringBuffer foss = new StringBuffer();
                for (Element e : fosele) {
                    foss.append(e.text() + ",");
                }
                String fos = ss(foss);
                String os = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Operating Status) span.component--field-formatter.field-type-enum", 0);
                String fs = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Operating Status) span.component--field-formatter.field-type-enum", 0);
                String lt = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Last Funding Type) span.cb-text-color-medium.flex-100.flex-gt-sm-25", 0);
                String ne = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Number of Employees) a.cb-link.component--field-formatter.field-type-enum", 0);
                String ss = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Stock Symbol) a.cb-link.component--field-formatter.field-type-link", 0);
                String ct = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Company Type) span.component--field-formatter.field-type-enum", 0);
                String noe = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Number of Exits) a.cb-link.component--field-formatter.field-type-integer", 0);
                String web = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Website) a.cb-link.component--field-formatter.field-type-link", 0);
                String fabo = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Facebook) a.cb-link.component--field-formatter.field-type-link", 0);
                String lin = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(LinkedIn) a.cb-link.component--field-formatter.field-type-link", 0);
                String tw = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Twitter) a.cb-link.component--field-formatter.field-type-link", 0);
                String email = JsoupUtils.getString(doc, "div.component--fields-card div.field-row:contains(Contact Email) span.component--field-formatter.field-type-email", 0);
                String desc = JsoupUtils.getString(doc, "div.component--description-card div.cb-display-inline", 0);


                ps.setString(1, quan);
                ps.setString(2, logo);
                ps.setString(3, add);
                ps.setString(4, cate);
                ps.setString(5, sub);
                ps.setString(6, fod);
                ps.setString(7, fos);
                ps.setString(8, os);
                ps.setString(9, fs);
                ps.setString(10, lt);
                ps.setString(11, ne);
                ps.setString(12, ss);
                ps.setString(13, ct);
                ps.setString(14, noe);
                ps.setString(15, web);
                ps.setString(16, lin);
                ps.setString(17, fabo);
                ps.setString(18, tw);
                ps.setString(19, email);
                ps.setString(20, desc);
                //ps.executeUpdate();

            /*for (Element e : ele) {
                String url = JsoupUtils.getHref(e, "div.flex.cb-padding-large-left a.cb-link", "href", 0);
                if (url.contains("person")) {
                    c.fang(new String[]{"https://www.crunchbase.com" + url, quan});
                }
            }*/

                String nr = JsoupUtils.getString(doc, "big-values-card div.component--big-values-card div.bigValueItemsWrapper.layout-row.layout-wrap div.flex-100.flex-gt-sm-50.bigValueItem.layout-column:contains(Number of Funding Rounds) a.cb-link.component--field-formatter.field-type-integer", 0);
                String ta = JsoupUtils.getString(doc, "big-values-card div.component--big-values-card div.bigValueItemsWrapper.layout-row.layout-wrap div.flex-100.flex-gt-sm-50.bigValueItem.layout-column:contains(Total Funding Amount) a.cb-link.component--field-formatter.field-type-money", 0);
                Elements finele = doc.select("list-card:contains(Announced Date) div.grid-body").get(0).select("div.grid-row.layout-row");
                for (Element e : finele) {
                    String de = JsoupUtils.getString(e, "span.component--field-formatter.field-type-date", 0);
                    String tn = JsoupUtils.getString(e, "span.flex.cb-overflow-ellipsis.identifier-label", 0);
                    String ni = JsoupUtils.getString(e, "a.cb-link.component--field-formatter.field-type-integer", 0);
                    String mr = JsoupUtils.getString(e, "span.component--field-formatter.field-type-money", 0);
                    Elements jgele = JsoupUtils.getElements(e, "span.component--field-formatter.field-type-identifier-multi a");
                    StringBuffer str = new StringBuffer();
                    for (Element ee : jgele) {
                        str.append(ee.text() + ",");
                    }
                    String jg = ss(str);

                    ps2.setString(1, quan);
                    ps2.setString(2, de);
                    ps2.setString(3, tn);
                    ps2.setString(4, ni);
                    ps2.setString(5, mr);
                    ps2.setString(6, jg);
                    ps2.setString(7, nr);
                    ps2.setString(8, ta);
                    ps2.executeUpdate();
                }
                a++;
                System.out.println(a + "**************************************************8");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static void ren(Cs c) throws InterruptedException, IOException, SQLException {
        String sql2="insert into crunchbase_org_employees(org_name,logo_url,per_name,per_position,Location,Gender,Investor_Type,Website,LinkedIn,Twitter,intro) values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);


        while (true){
            String[] url=c.qu();
            Document doc=get(url[0]);
            String logo=JsoupUtils.getHref(doc,"meta[property=og:image]","content",0);
            String quan=JsoupUtils.getString(doc,"div.mat-toolbar-layout h1.flex.cb-overflow-ellipsis",0);
            String zhi=JsoupUtils.getString(doc,"span.component--field-formatter.field-type-text_short",1);

            Elements loele=JsoupUtils.getElements(doc,"div.component--fields-card div.field-row:contains(Location) span.component--field-formatter.field-type-identifier-multi a.cb-link");
            StringBuffer los=new StringBuffer();
            for(Element e:loele){
                los.append(e.text()+",");
            }
            String lo=ss(los);
            String gd=JsoupUtils.getString(doc,"div.component--fields-card div.field-row:contains(Gender) span.component--field-formatter.field-type-enum",0);
            String it=JsoupUtils.getString(doc,"div.component--fields-card div.field-row:contains(Investor Type) span.component--field-formatter.field-type-enum_multi",0);
            String web=JsoupUtils.getString(doc,"div.component--fields-card div.field-row:contains(Website) a.cb-link.component--field-formatter.field-type-link",0);
            String lk=JsoupUtils.getString(doc,"div.component--fields-card div.field-row:contains(LinkedIn) a.cb-link.component--field-formatter.field-type-link",0);
            String tt=JsoupUtils.getString(doc,"div.component--fields-card div.field-row:contains(Twitter) a.cb-link.component--field-formatter.field-type-link",0);
            String desc=JsoupUtils.getString(doc,"div.component--description-card div.cb-display-inline",0);

            ps2.setString(1,url[1]);
            ps2.setString(2,logo);
            ps2.setString(3,quan);
            ps2.setString(4,zhi);
            ps2.setString(5,lo);
            ps2.setString(6,gd);
            ps2.setString(7,it);
            ps2.setString(8,web);
            ps2.setString(9,lk);
            ps2.setString(10,tt);
            ps2.setString(11,desc);
            ps2.executeUpdate();
        }
    }


    public static Document get(String url) throws IOException, InterruptedException {
        System.out.println(url);
        Document doc=null;
        int j=0;
        while (true) {
            try {
                j++;
                if(j>=30){
                    break;
                }
                String ip=c.qu();
                doc = Jsoup.connect(url)
                        .timeout(10000)
                        .header("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("upgrade-insecure-requests","1")
                        .header("cache-control","max-age=0")
                        .header("accept-language","zh-CN,zh;q=0.8")
                        .header("accept-encoding","gzip, deflate, br")
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .get();
                String quan = JsoupUtils.getString(doc, "div.mat-toolbar-layout h1.flex.cb-overflow-ellipsis", 0);
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")&&doc.outerHtml().length()>100000) {
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 2; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static void getip() throws IOException, InterruptedException {
        RedisAction rd=new RedisAction("10.44.51.90",6379);
        while (true) {
            try {
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(4000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static String ss(StringBuffer str){
        if(str!=null){
            try{
                return str.substring(0,str.length()-1).toString();
            }catch (Exception e){
                return null;
            }
        }else{
            return null;
        }
    }

    class Cs{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    public class Ka{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>(10);
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    public class Ba{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>(10);
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
