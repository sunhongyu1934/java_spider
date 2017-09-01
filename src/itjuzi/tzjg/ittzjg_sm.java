package itjuzi.tzjg;

/**
 * Created by Administrator on 2017/5/9.
 */

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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/4/19.
 */
public class ittzjg_sm {
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, DocumentException {
        String str[]=jiexi();

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://"+str[1]+":3308/"+str[2]+"?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username=str[3];
        String password=str[4];
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


        //创建认证，并设置认证范围

        //CredentialsProvider credsProvider = new BasicCredentialsProvider();

        //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H4TL2M827AIJ963D", "81C9D64628A60CF9"));


        //builder.setDefaultCredentialsProvider(credsProvider);


        search(con,str);


    }

    public static Map<String,String> login(String[] str) throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6I878E110016EVD", "E7D36F817D52CF35"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity",str[5]));
        params.add(new BasicNameValuePair("password",str[6]));
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

    public static void search(Connection con,String[] str) throws SQLException, IOException, DocumentException {
        System.out.println("begin login");
        Map<String,String> map=login(str);
        System.out.println("login success");
        String sql="select * from it_finacing_pc where moditme>date_sub(now(),interval '1' hour)";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String vcs=rs.getString(rs.findColumn("vc")).substring(0,rs.getString(rs.findColumn("vc")).length()-1);
            String vcss[]=vcs.split(";");
            for(int a=0;a<vcss.length;a++){
                String sql2="select c_id from it_touzijigou where tj_name='"+vcss[a]+"' and (source_url not like '%http://www.itjuzi.com/company%' or source_url is null)";
                PreparedStatement ps2=con.prepareStatement(sql2);
                ResultSet rs2=ps2.executeQuery();
                int flag=0;
                while (rs2.next()){
                    flag=1;
                    String cid=rs2.getString(rs2.findColumn("c_id"));
                    String table[] = new String[]{"it_touzijigou", "it_touzijigou_chengyuan", "it_touzijigou_dt", "it_touzijigou_finacing", "it_touzijigou_news"};
                    String sql3 = "select mongo_id from it_touzijigou where c_id='" + cid + "'";
                    PreparedStatement ps3 = con.prepareStatement(sql3);
                    ResultSet rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        String mongo_id = rs3.getString(rs3.findColumn("mongo_id"));
                        //deletemongo(collection, mongo_id);
                    }
                    for (int g = 0; g < table.length; g++) {
                        flagdata(con, cid, table[g]);
                    }
                    get( con, cid,map);
                }
                if(flag==0){
                    String sqls="select * from it_company_pc where SUBSTRING_INDEX(SUBSTRING_INDEX(sName,'/',1),'(',1)='"+vcss[a]+"'";
                    PreparedStatement pss=con.prepareStatement(sqls);
                    ResultSet rss=pss.executeQuery();
                    while (rss.next()){
                        String sql1="insert into it_touzijigou(c_id,`tj_name`,tj_tag,tj_web,`tj_dec`,touzi_lingyu,touzi_round,tj_logo,tj_weixin,tj_phone,tj_email,tj_address,mongo_id,data_date,source_url) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement ps1=con.prepareStatement(sql1);

                        String sql4="insert into it_touzijigou_finacing(c_id,f_id,`finacing_name`,finacing_bname,`finacing_time`,finacing_industry,finacing_round,finacing_money) values(?,?,?,?,?,?,?,?)";
                        PreparedStatement ps4=con.prepareStatement(sql4);


                        String sname=rss.getString(rss.findColumn("sName"));
                        if(sname.contains("/")){
                            sname=sname.split("/")[0];
                        }
                        if(sname.contains("(")){
                            sname=sname.split("\\(")[0];
                        }
                        String cid=rss.getString(rss.findColumn("c_id"));
                        java.util.Date date=new java.util.Date();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        String d=simpleDateFormat.format(date);
                        String delete1="delete from it_touzijigou where c_id="+cid+" and tj_name='"+sname+"'";
                        String delete2="delete from it_touzijigou_finacing where c_id="+cid+" and finacing_name='"+sname+"' and finacing_bname='"+rs.getString(rs.findColumn("juzi_name"))+"'";
                        PreparedStatement psd1=con.prepareStatement(delete1);
                        PreparedStatement psd2=con.prepareStatement(delete2);
                        psd1.executeUpdate();
                        psd2.executeUpdate();

                        ps1.setString(1,cid);
                        ps1.setString(2,sname);
                        ps1.setString(3,rss.getString(rss.findColumn("company_tags")));
                        ps1.setString(4,rss.getString(rss.findColumn("web_url")));
                        ps1.setString(5,rss.getString(rss.findColumn("company_introduction")));
                        ps1.setString(6,"");
                        ps1.setString(7,"");
                        ps1.setString(8,rss.getString(rss.findColumn("company_logo")));
                        ps1.setString(9,"");
                        ps1.setString(10,"");
                        ps1.setString(11,"");
                        ps1.setString(12,rss.getString(rss.findColumn("company_address")));
                        ps1.setString(13,rss.getString(rss.findColumn("mongo_id")));
                        ps1.setString(14,d);
                        ps1.setString(15,"http://www.itjuzi.com/company/"+cid);
                        ps1.executeUpdate();

                        ps4.setString(1,cid);
                        ps4.setString(2,rs.getString(rs.findColumn("f_id")));
                        ps4.setString(3,sname);
                        ps4.setString(4,rs.getString(rs.findColumn("juzi_name")));
                        ps4.setString(5,rs.getString(rs.findColumn("financing_time")));
                        ps4.setString(6,"");
                        ps4.setString(7,rs.getString(rs.findColumn("financing_round")));
                        ps4.setString(8,rs.getString(rs.findColumn("financing_money")));
                        ps4.executeUpdate();
                    }
                }
            }
        }
    }

    public static void flagdata(Connection con,String cid,String table) throws SQLException {
        String sql1="delete from "+table+" where c_id='"+cid+"'";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static void deletemongo(MongoCollection collection, String mongo_id){
        org.bson.Document document = new org.bson.Document("_id", mongo_id);
        collection.deleteOne(document);
    }

    public static String get() throws IOException, InterruptedException {
        System.out.println("begin get ip");
        Document doc =null;
        while (true){
            try {
                doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=00b1c1dbec239455d92d87b98145951c&sep=3")
                        .ignoreHttpErrors(true)
                        .timeout(100000)
                        .ignoreContentType(true)
                        .get();
                if (doc != null) {
                    break;
                }
            }catch (Exception e){
                System.out.println("get ip error reget");
            }
        }
        String ip = doc.body().toString().replace("<body>", "").replace("</body>", "").replace("\n", "").trim();
        System.out.println("get ip success");
        return ip;
    }


    public static void get(Connection con,String cid,Map<String,String> map) throws IOException, SQLException {
        HttpGet get = new HttpGet("https://www.itjuzi.com/investfirm/"+cid);
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        String tag = null;
        while (true) {
            try {
                //String ip=get();
                CredentialsProvider credsProvider = new BasicCredentialsProvider();

                credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6I878E110016EVD", "E7D36F817D52CF35"));
                HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                        .setSocketTimeout(5000).build();

                CookieStore cookieStore=new BasicCookieStore();
                HttpClientBuilder builder = HttpClients.custom().setDefaultCookieStore(cookieStore);
                for(Map.Entry<String,String> entry:map.entrySet()){
                    BasicClientCookie cookie=new BasicClientCookie(entry.getKey(),entry.getValue());
                    cookie.setDomain("www.itjuzi.com");
                    cookie.setVersion(0);
                    cookie.setPath("/");
                    cookieStore.addCookie(cookie);
                }

                builder.setRoutePlanner(routePlanner);
                builder.setDefaultCredentialsProvider(credsProvider);
                builder.setDefaultRequestConfig(requestConfig);
                CloseableHttpClient httpclient = builder.build();


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
            storedata(con, doc, String.valueOf(cid));
            System.out.println("insert mysql success ");
            System.out.println("------------------------------------------------------------");
        } catch (Exception e1) {
            System.out.println("error");
        }


    }

    public static void storedata(Connection con,Document doc,String cid) throws SQLException {
        String sql1="insert into it_touzijigou(c_id,`tj_name`,tj_tag,tj_web,`tj_dec`,touzi_lingyu,touzi_round,tj_logo,tj_weixin,tj_phone,tj_email,tj_address,mongo_id,data_date,source_url) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);

        String sql2="insert into it_touzijigou_chengyuan(c_id,p_id,`cy_name`,cy_job,`cy_dec`,cy_statu,data_date) values(?,?,?,?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into it_touzijigou_dt(c_id,dt_title,`dt_time`,data_date) values(?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);

        String sql4="insert into it_touzijigou_finacing(c_id,f_id,`finacing_name`,finacing_bname,`finacing_time`,finacing_industry,finacing_round,finacing_money,data_date) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps4=con.prepareStatement(sql4);

        String sql5="insert into it_touzijigou_news(c_id,news_title,`news_time`,news_type,news_url,data_date) values(?,?,?,?,?,?)";
        PreparedStatement ps5=con.prepareStatement(sql5);

        parse(doc,cid,ps1,ps2,ps3,ps4,ps5);
    }


    public static void parse(Document doc,String cid, PreparedStatement ps1, PreparedStatement ps2, PreparedStatement ps3, PreparedStatement ps4, PreparedStatement ps5) throws SQLException {
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
        String mongo_id="0";

        java.util.Date date=new java.util.Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String d=simpleDateFormat.format(date);

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
        ps1.setString(14,d);
        ps1.setString(15,"https://www.itjuzi.com/investfirm/"+cid);
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
                            ps2.setString(7,d);
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
                            ps2.setString(7,d);
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
                ps3.setString(4,d);
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
                ps4.setString(9,d);
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
                ps5.setString(6,d);
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

    public static String[] jiexi() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/home/etl_user/etl/program/src/itjuzi/tzjg/tzjg.xml")));
        org.dom4j.Element root=dom.getRootElement();
        org.dom4j.Element table=root.element("table");
        String page=table.element("page").getText();
        String ip=table.element("ip").getText();
        String database=table.element("database").getText();
        String username=table.element("username").getText();
        String password=table.element("password").getText();
        String zhanghu=table.element("zhanghu").getText();
        String mima=table.element("mima").getText();
        String str[]=new String[]{page,ip,database,username,password,zhanghu,mima};
        return str;
    }
}

