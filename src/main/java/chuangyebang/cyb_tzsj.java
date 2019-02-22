package chuangyebang;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static chuangyebang.cyb_tzsjzl.flagdata;

/**
 * Created by Administrator on 2017/4/19.
 */
public class cyb_tzsj {
    // 代理隧道验证信息
    final static String ProxyUser = "H4KKF9EHDF26260D";
    final static String ProxyPass = "2A64AB23C97FCA79";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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

        /*MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("10.44.51.90",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("chuangyebang");
        final MongoCollection collection = db.getCollection("company");*/

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        ExecutorService pool= Executors.newFixedThreadPool(20);
        final Connection finalCon = con;
        int p=1;
        for(int a=1;a<=20;a++){
            final int finalP = p;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        getlb(finalP,finalCon,proxy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            p=p+130;
        }

    }

    public static void getlb(int p,Connection con,Proxy proxy) throws SQLException {
        for(int x=p;x<p+130;x++){
            try {
                Document doc = null;
                System.out.println("begin request search");
                while (true) {
                    try {
                        doc = Jsoup.connect("http://www.cyzone.cn/vcompany/list-0-0-"+x+"/")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                                .proxy(proxy)
                                .timeout(5000)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("time out serach");
                    }
                }
                Elements urlele = getElements(doc, "div.list-table table tbody tr.table-plate.item");
                if (urlele != null) {
                    for (Element e : urlele) {
                        String url = getHref(e, "td.table-company a", "href", 0);
                        if(StringUtils.isNotEmpty(url)) {
                            try {
                                System.out.println("begin request detail");
                                getdetail(url, con, proxy);
                            } catch (Exception e1) {
                                System.out.println("detail error");
                            }
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("search error");
            }
        }
    }


    public static void getdetail(String url,Connection con,Proxy proxy) throws SQLException, InterruptedException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                       .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println(url);
                System.out.println("time out detail");
            }
        }

        System.out.println("request detail success and begin store data");
        storedata(con, doc, url);
        System.out.println("insert mysql success");
        System.out.println("-----------------------------------------------------------------");
    }

    public static void storedata(Connection con,Document doc,String url) throws SQLException {
        String sql1="insert into cyb_company(cid,`name`,full_name,web,found_time,address,tag,logo,`dec`,industry,zhucehao,statu,per,gudong,`type`,zcziben) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);

        String sql2="insert into cyb_dt(cid,title,`time`,`dec`) values(?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into cyb_finacing(cid,`name`,`time`,`round`,money,vc) values(?,?,?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);

        String sql4="insert into cyb_founders(cid,`name`,`position`,url) values(?,?,?,?)";
        PreparedStatement ps4=con.prepareStatement(sql4);

        String sql5="insert into cyb_news(cid,title,url,`time`) values(?,?,?,?)";
        PreparedStatement ps5=con.prepareStatement(sql5);

        String sql6="insert into cyb_tag(cid,tag,url) values(?,?,?)";
        PreparedStatement ps6=con.prepareStatement(sql6);

        parse(doc,url,ps1,ps2,ps3,ps4,ps5,ps6,con);
    }


    public static void parse(Document doc,String detailurl,PreparedStatement ps1,PreparedStatement ps2,PreparedStatement ps3,PreparedStatement ps4,PreparedStatement ps5,PreparedStatement ps6,Connection con) throws SQLException {
        String cid=detailurl.split("/", 5)[4].replace(".html","");
        String name=getString(doc,"div.top-info.clearfix div.ti-left.pull-left li.name",0);
        String fullname=getString(doc,"div.top-info.clearfix div.ti-left.pull-left li.time",0).replace("公司全称：","");
        String logo=getHref(doc,"div.top-info.clearfix div.ti-left.pull-left div.tl-img-bar img","src",0);
        String web=getString(doc,"div.top-info.clearfix div.ti-left.pull-left div.com-url a",0);
        String zch=getString(doc,"div.qcc p",0).replace("注册号:","");
        String statu=getString(doc,"div.qcc p",1).replace("经营状态:","");
        String fddb=getString(doc,"div.qcc p",2).replace("法定代表:","");
        String gd=getString(doc,"div.qcc p.clearfix span.gd.pull-left",0).replace(" ", ";");
        String type=getString(doc,"div.qcc p",4).replace("公司类型:", "");
        String clrq=getString(doc,"div.qcc p",5).replace("成立日期:", "");
        String zczb=getString(doc,"div.qcc p",6).replace("注册资本:", "");
        String address=getString(doc,"div.qcc p",7).replace("住所:", "");
        String dec=getString(doc,"div.info-box p",0)+"\r\n"+getString(doc,"div.info-box p",1);

        String tag="";
        String industry="";
        Elements tagele=getElements(doc,"div.info-tag.clearfix ul li:has(i.i6) span");
        if(tagele!=null){
            int a=0;
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tags=getString(e,"a",0);
                String tagurl=getHref(e,"a","href",0);
                str.append(tags+";");
                if(a==0){
                    industry=tags;
                }
                a++;

                ps6.setString(1,cid);
                ps6.setString(2,tags);
                ps6.setString(3,tagurl);
                ps6.addBatch();
            }
            ps6.executeBatch();
            tag=str.toString();
        }

        //String mongo_id=storemongo(collection,doc.outerHtml());

        ps1.setString(1,cid);
        ps1.setString(2,name);
        ps1.setString(3,fullname);
        ps1.setString(4,web);
        ps1.setString(5,clrq);
        ps1.setString(6,address);
        ps1.setString(7,tag);
        ps1.setString(8,logo);
        ps1.setString(9,dec);
        ps1.setString(10,industry);
        ps1.setString(11,zch);
        ps1.setString(12,statu);
        ps1.setString(13,fddb);
        ps1.setString(14,gd);
        ps1.setString(15,type);
        ps1.setString(16,zczb);
        ps1.executeUpdate();




        Elements dtele=getElements(doc,"div.trends.clearfix.look div.list div.item");
        if(dtele!=null){
            for(Element e:dtele){
                String time=getString(e,"span.time",0);
                String title=getString(e,"div.title",0);
                String xq=getString(e,"div.content p",0);

                ps2.setString(1,cid);
                ps2.setString(2,title);
                ps2.setString(3,time);
                ps2.setString(4,xq);
                ps2.addBatch();
            }
            ps2.executeBatch();
        }

        Elements rzele=getElements(doc,"div.live>table>tbody>tr:not(.live-title)");
        if(rzele!=null){
            for(Element e:rzele){
                String lunshu=getString(e,"td",0);
                String jine=getString(e,"div.money",0);
                String tzf="";
                try{
                    Elements tzfs=e.select(">td").get(2).select("a");
                    StringBuffer str=new StringBuffer();
                    for(Element ee:tzfs){
                        str.append(ee.text()+";");
                    }
                    tzf=str.toString().substring(0,str.toString().length()-1).replace("····","");
                }catch (Exception e1){
                }
                String time=getString(e, ">td", 3);

                ps3.setString(1,cid);
                ps3.setString(2,name);
                ps3.setString(3,time);
                ps3.setString(4,lunshu);
                ps3.setString(5,jine);
                ps3.setString(6,tzf);
                ps3.addBatch();
            }
            ps3.executeBatch();
        }

        Elements csele=getElements(doc,"div.team.clearfix ul li");
        if(csele!=null){
            for(Element e:csele){
                String csname=getString(e,"div.team-info p.name a",0);
                String csjob=getString(e,"div.team-info p.job",0);
                String csurl=getHref(e,"div.team-info p.name a","href",0);

                ps4.setString(1,cid);
                ps4.setString(2,csname);
                ps4.setString(3,csjob);
                ps4.setString(4,csurl);
                ps4.addBatch();
            }
            ps4.executeBatch();
        }

        Elements newele=getElements(doc,"div.about.clearfix ul.clearfix li");
        if(newele!=null){
            for(Element e:newele){
                String title=getString(e,"span a",0);
                String newurl= getHref(e, "span a", "href",0);
                String newtime=getString(e,"span.time",0);

                ps5.setString(1,cid);
                ps5.setString(2,title);
                ps5.setString(3,newurl);
                ps5.setString(4,newtime);
                ps5.addBatch();
            }
            ps5.executeBatch();
        }

    }


    /*public static String storemongo(MongoCollection collection, String html){
        String uuid= UUID.randomUUID().toString();
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("html", html).
                append("time", time);
        collection.insertOne(document);
        return uuid;
    }*/


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
