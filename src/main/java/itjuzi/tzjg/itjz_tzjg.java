package itjuzi.tzjg;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/4/19.
 */
public class itjz_tzjg {
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.51.120.107:3306/finacing?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
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

        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("10.44.51.90",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("itjuzi");
        final MongoCollection collection = db.getCollection("touzijigou");





        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

         builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        final CloseableHttpClient httpclient = builder.build();

        get(httpclient, con, collection,0);


    }


    public static Map<String,String> login(String[] str) throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H4TL2M827AIJ963D", "81C9D64628A60CF9"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();

        //builder.setRoutePlanner(routePlanner);

        //builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity",str[6]));
        params.add(new BasicNameValuePair("password",str[7]));
        params.add(new BasicNameValuePair("remember","1"));
        params.add(new BasicNameValuePair("page",""));
        params.add(new BasicNameValuePair("url",""));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        List<Cookie> cookies =null;
        while (true) {
            CloseableHttpResponse response = httpclient.execute(post);
            HttpEntity resEntity = response.getEntity();
            String tag = EntityUtils.toString(resEntity);
            cookies=cookieStore.getCookies();
            if (cookies != null && cookies.size()>2) {
                break;
            }
        }
        for (int i = 0; i < cookies.size(); i++) {
            map.put(cookies.get(i).getName(),cookies.get(i).getValue());
        }

        /*Connection.Response res= null;
        while (true) {
            try {
                res = Jsoup.connect("https://www.itjuzi.com/user/login?redirect=&flag=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .header("Content-Type", "text/html;charset=utf-8")
                        .data("identity", "13717951934")
                        .data("password", "3961shy")
                        .data("remember", "1")
                        .data("page", "")
                        .data("url", "")
                        .timeout(100000)
                        .method(Connection.Method.POST)
                        .execute();
                if (res != null && StringUtils.isNotEmpty(res.cookies().toString().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out");
            }
        }
        Map<String,String> map=res.cookies();*/
        System.out.println(map);
        return map;
    }

    public static void get(CloseableHttpClient httpclient,Connection con,MongoCollection collection,int p) throws IOException, SQLException {
        for(int x=p;x<p+350;x++) {
            HttpGet get = new HttpGet("https://www.itjuzi.com/investfirm/"+x);
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
            String tag = null;
            while (true) {
                try {
                    CloseableHttpResponse response = httpclient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    tag = EntityUtils.toString(resEntity);
                    if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("time out reget");
                }
            }
            try {
                Document doc = Jsoup.parse(tag);
                System.out.println("begin store data");
                storedata(con, collection, doc, String.valueOf(x));
                System.out.println("insert mysql success ");
                System.out.println("------------------------------------------------------------");
            } catch (Exception e1) {
                System.out.println("error");
            }
        }

    }

    public static void storedata(Connection con,MongoCollection collection,Document doc,String cid) throws SQLException {
        String sql1="insert into it_touzijigou(cid,`name`,tag,web,`dec`,touzi_lingyu,touzi_round,logo,weixin,phone,emain,address,mongo_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);

        String sql2="insert into it_touzijigou_chengyuan(cid,pid,`name`,job,`dec`,statu) values(?,?,?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into it_touzijigou_dt(cid,title,`time`) values(?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);

        String sql4="insert into it_touzijigou_finacing(cid,fid,`name`,bname,`time`,industry,round,money) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps4=con.prepareStatement(sql4);

        String sql5="insert into it_touzijigou_news(cid,title,`time`,type,url) values(?,?,?,?,?)";
        PreparedStatement ps5=con.prepareStatement(sql5);

        parse(doc,cid,collection,ps1,ps2,ps3,ps4,ps5);
    }


    public static void parse(Document doc,String cid,MongoCollection collection, PreparedStatement ps1, PreparedStatement ps2, PreparedStatement ps3, PreparedStatement ps4, PreparedStatement ps5) throws SQLException {
        String name=getString(doc,"div.picinfo h1.seo-company-title",0);
        String tag="";
        Elements tagele=getElements(doc,"div.picinfo p span.tag");
        if(tagele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tags=e.text();
                str.append(tags+";");
            }
            tag=str.toString();
        }
        String web=getHref(doc, "div.picinfo p.mart10 span.links a", "href", 0);
        String dec=getString(doc,"div.block.block-inc-info",0);
        String ly="";
        Elements lyele=getElements(doc,"div.list-tags.darkblue a");
        if(lyele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:lyele){
                String lys=getString(e,"b.tag",0);
                str.append(lys+";");
            }
            ly=str.toString();
        }
        String lc="";
        Elements lcele=getElements(doc,"div.right div.list-tags.lightblue a");
        if(lcele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:lcele){
                String lcs=getString(e,"b.tag",0);
                str.append(lcs+";");
            }
            lc=str.toString();
        }
        String logo=getHref(doc,"div.boxed.rel div.pic img","src",0);
        String weixin=getString(doc,"ul.list-block.aboutus li:has(i.fa.fa-wechat) span",0);
        String phone=getString(doc,"ul.list-block.aboutus li:has(i.fa.fa-phone) span",0);
        String email=getString(doc,"ul.list-block.aboutus li:has(i.fa.fa-envelope) span",0);
        String address="";
        Elements adele=getElements(doc,"ul.list-block.aboutus li:has(i.fa.fa-map-marker) span.c-gray.t-small");
        if(adele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:adele){
                String adds=e.text();
                str.append(adds+";");
            }
            address=str.toString();
        }
        String mongo_id=storemongo(collection,doc.outerHtml());

        ps1.setString(1,cid);
        ps1.setString(2,name);
        ps1.setString(3,tag);
        ps1.setString(4,web);
        ps1.setString(5,dec);
        ps1.setString(6,ly);
        ps1.setString(7,lc);
        ps1.setString(8,logo);
        ps1.setString(9,weixin);
        ps1.setString(10,phone);
        ps1.setString(11,email);
        ps1.setString(12,address);
        ps1.setString(13,mongo_id);
        ps1.executeUpdate();


        Elements jscyele=getElements(doc,"div.sec.institu-member ul.list-prodcase.limited-itemnum.width100");
        if(jscyele!=null){
            int a=0;
            for(Element e:jscyele){


                if(a==0){
                    Elements zzele=getElements(e,"div.right");
                    if(zzele!=null){
                        for(Element ee:zzele){
                            String pid=getHref(ee,"p.person-name a.title","href",0).split("/",5)[4];
                            String cyname=getString(ee,"p.person-name a.title b",0);
                            String cyjob=getString(ee,"p.person-name a.title span.c-gray",0);
                            String cydec=getString(ee,"p.person-intro.mart10",0);

                            ps2.setString(1,cid);
                            ps2.setString(2,pid);
                            ps2.setString(3,cyname);
                            ps2.setString(4,cyjob);
                            ps2.setString(5,cydec);
                            ps2.setString(6,"0");
                            ps2.addBatch();
                        }
                    }
                }else{
                    Elements zzele=getElements(e,"div.right");
                    if(zzele!=null){
                        for(Element ee:zzele){
                            String pid=getHref(ee,"p.person-name a.title","href",0).split("/",5)[4];
                            String cyname=getString(ee,"p.person-name a.title b",0);
                            String cyjob=getString(ee,"p.person-name a.title span.c-gray",0);
                            String cydec=getString(ee,"p.person-intro.mart10",0);

                            ps2.setString(1,cid);
                            ps2.setString(2,pid);
                            ps2.setString(3,cyname);
                            ps2.setString(4,cyjob);
                            ps2.setString(5,cydec);
                            ps2.setString(6,"1");
                            ps2.addBatch();
                        }
                    }
                }
                a++;
            }
            ps2.executeBatch();
        }

        Elements dtele=getElements(doc,"div.wp100.ofa.hscroll ul.list-milestone.timelined.limited-itemnum li");
        if(dtele!=null){
            for(Element e:dtele){
                String dttitle=getString(e,"p",0);
                String dttime=getString(e,"p span.t-small.c-gray",0);

                ps3.setString(1,cid);
                ps3.setString(2,dttitle);
                ps3.setString(3,dttime);
                ps3.addBatch();
            }
            ps3.executeBatch();
        }

        Elements tzele=getElements(doc,"table.list-invecase.limited-itemnum.haslogin.needfilter tbody tr");
        if(tzele!=null){
            for(Element e:tzele){
                String fid=getHref(e,"td.icon.mobile-none a","href",0).split("/",5)[4];
                String bname=getString(e,"td.title a b",0);
                String tztime=getString(e,"td.date span.verdana",0);
                String bhy=getString(e,"td[class=mobile-none]",0);
                String tzlc=getString(e,"td",4);
                String tzje=getString(e,"td",5);

                ps4.setString(1,cid);
                ps4.setString(2,fid);
                ps4.setString(3,name);
                ps4.setString(4,bname);
                ps4.setString(5,tztime);
                ps4.setString(6,bhy);
                ps4.setString(7,tzlc);
                ps4.setString(8,tzje);
                ps4.addBatch();
            }
            ps4.executeBatch();
        }

        Elements newele=getElements(doc,"ul.list-timeline.limited-itemnum li");
        if(newele!=null){
            for(Element e:newele){
                String newtitle=getString(e,"a",0);
                String newtime=getString(e,"i.sub",0);
                String newtype=getString(e,"i.tag.red",0).replace("#","");
                String newurl=getHref(e,"a","href",0);

                ps5.setString(1,cid);
                ps5.setString(2,newtitle);
                ps5.setString(3,newtime);
                ps5.setString(4,newtype);
                ps5.setString(5,newurl);
                ps5.addBatch();
            }
            ps5.executeBatch();
        }




    }









    public static String storemongo(MongoCollection collection, String html){
        Gson gson=new Gson();
        String uuid= UUID.randomUUID().toString();
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("html", html).
                append("time", time);
        collection.insertOne(document);
        return uuid;
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
