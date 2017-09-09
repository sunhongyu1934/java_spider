package itjuzi.zl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/4/18.
 */
public class itjz_l {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, DocumentException, InterruptedException, ParseException {
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



        search(con,str);

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



    public static void search(Connection con,String[] str) throws IOException, DocumentException, SQLException, InterruptedException, ParseException {
        System.out.println("begin login");
        Map<String,String> map=login(str);
        System.out.println("login success");
        int page= Integer.parseInt(jiexi()[0]);
        int day=Integer.parseInt(jiexi()[5]);
        System.out.println("begin search");
        for(int x=1;x<=page;x++) {
            HttpGet get = new HttpGet("https://www.itjuzi.com/investevents?page="+x);
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
                            .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                            .setSocketTimeout(50000).build();

                    HttpClientBuilder builder = HttpClients.custom();

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
            Document doc=Jsoup.parse(tag);
            Elements dateele=getElements(doc,"ul.list-main-eventset li i.cell.date span");
            int pp=1;
            if(dateele!=null){
                Date datee=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.M.d");
                String date1=simpleDateFormat.format(datee);
                long dd1=simpleDateFormat.parse(date1).getTime();
                long dd2=dd1-(day-1)*24*60*60*1000;
                for(Element e:dateele){
                    String date=e.text();
                    long date3=simpleDateFormat.parse(date).getTime();
                    if(date3<dd2){
                        break;
                    }
                    pp++;
                }
            }


            Elements ele=getElements(doc,"ul.list-main-eventset li");
            int oo=1;
            boolean br=false;
            if(ele!=null){
                int a=0;
                for(Element e:ele){
                    if(a>0) {
                        if(oo==pp){
                            br=true;
                            break;
                        }
                        String detailurl = getHref(e, "i.cell.maincell p.title a", "href", 0);
                        String cid = detailurl.split("/", 5)[4];
                        String table[] = new String[]{"it_company_pc", "it_competitor_pc", "it_finacing_pc", "it_founders_pc", "it_news_pc", "it_product_pc", "it_roadmap_pc", "it_tag_pc"};
                        for (int g = 0; g < table.length; g++) {
                            flagdata(con, cid, table[g]);
                        }
                        get(con, cid,map);
                        oo++;
                    }
                    a++;
                }
            }
            if(br){
                break;
            }

        }

    }

    public static void flagdata(Connection con,String cid,String table) throws SQLException {
        String sql1="delete from "+table+" where c_id='"+cid+"'";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static void get(Connection con,String cid,Map<String,String> map) throws IOException, SQLException, InterruptedException {
        HttpGet get = new HttpGet("http://www.itjuzi.com/company/"+cid );
        get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.addHeader("Cache-Control","max-age=0");
        get.addHeader("Host","www.itjuzi.com");
        get.addHeader("Upgrade-Insecure-Requests","1");
        get.addHeader("If-Modified-Since","Tue, 25 Jul 2017 06:33:17 GMT");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        String tag = null;
        //String ip=get();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6I878E110016EVD", "E7D36F817D52CF35"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(100000).setConnectionRequestTimeout(100000)
                .setSocketTimeout(100000).build();

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
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("time out detail reget");
            }
        }
        try {
            Document doc=Jsoup.parse(tag);
            System.out.println("begin store data");
            storedata(con, doc, cid);
            System.out.println("insert mysql success ");
            System.out.println("------------------------------------------------------------");
        }catch (Exception e1){
            System.out.println("error");
        }

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

    public static void storedata(Connection con,Document doc,String cid) throws SQLException, IOException {
        String sql1="insert into it_company_pc(c_id,`sName`,web_url,company_slogan,company_industry,sub_industry,company_address,company_logo,company_tags,product_logos,company_introduction,company_full_name,found_time,company_scale,company_status,mongo_id,`source_url`,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);

        String sql2="insert into it_competitor_pc(c_id,rc_id,`name`,industry,sub_industry,logo_url,round,money,data_date) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into it_finacing_pc(c_id,f_id,`juzi_name`,`financing_time`,financing_round,financing_money,vc,`dec_url`,data_date) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);

        String sql4="insert into it_founders_pc(c_id,`name`,`position`,introduction,weibo,data_date) values(?,?,?,?,?,?)";
        PreparedStatement ps4=con.prepareStatement(sql4);

        String sql5="insert into it_news_pc(c_id,title,`time`,`type`,news_from,url,data_date) values(?,?,?,?,?,?,?)";
        PreparedStatement ps5=con.prepareStatement(sql5);

        String sql6="insert into it_product_pc(c_id,`name`,`type`,introduction,url,data_date) values(?,?,?,?,?,?)";
        PreparedStatement ps6=con.prepareStatement(sql6);

        String sql7="insert into it_roadmap_pc(c_id,title,`time`,data_date) values(?,?,?,?)";
        PreparedStatement ps7=con.prepareStatement(sql7);

        String sql8="insert into it_tag_pc(c_id,`name`,url,data_date) values(?,?,?,?)";
        PreparedStatement ps8=con.prepareStatement(sql8);

        parse(doc,cid,ps1,ps2,ps3,ps4,ps5,ps6,ps7,ps8);
    }

    public static void parse(Document doc,String cid,PreparedStatement ps1,PreparedStatement ps2,PreparedStatement ps3,PreparedStatement ps4,PreparedStatement ps5,PreparedStatement ps6,PreparedStatement ps7,PreparedStatement ps8) throws IOException, SQLException {
        Map<String,String> map=new HashMap<String,String>();
        String name=getString(doc, "div.picinfo div.line-title span.title h1.seo-important-title", 0);
        String urlguan=getHref(doc, "div.link-line a.weblink", "href", 0);
        String kouhao=getString(doc,"div.info-line h2.seo-slogan",0);
        String hangye=getString(doc,"div.info-line span.scope.c-gray-aset a",0);
        String zihangye=getString(doc,"div.info-line span.scope.c-gray-aset a",1);
        String dizhi=getString(doc,"div.info-line span.loca.c-gray-aset a",0)+"-"+getString(doc,"div.info-line span.loca.c-gray-aset a",1);
        String logo=getHref(doc, "div.rowhead div.pic img", "src", 0);
        String tags=null;
        Elements tagele=getElements(doc,"div.tagset.dbi.c-gray-aset a");
        if(tagele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tag=getString(e, "span.tag", 0);
                String tagurl=e.attr("href");
                map.put(tagurl,tag);
                str.append(getString(e,"span.tag",0)+";");
            }
            tags=str.toString();
        }
        String productlogos=null;
        Elements prolos=getElements(doc,"div.sec ul.list-prodcase.limited-itemnum li div.left");
        if(prolos!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:prolos){
                if(StringUtils.isNotEmpty(getHref(e,"span.uniicon.albumbg","style",0).replace("background-image: url(","").replace(")",""))) {
                    str.append(getHref(e, "span.uniicon.albumbg", "style", 0).replace("background-image: url(", "").replace(")", "") + ";");
                }
            }
            productlogos=str.toString();
        }
        String yewu=getString(doc,"div.block div.des",0);
        String fullname=getString(doc,"div.block div.des-more h2.seo-second-title",0).replace("公司全称：","");
        String chenglitime=getString(doc,"div.block div.des-more h2.seo-second-title",1).replace("成立时间：","");
        String guimo=getString(doc,"div.block div.des-more h2.seo-second-title",2).replace(" 公司规模：","");
        String zhuangtai=getString(doc,"div.block div.des-more span",0);

        String mongo_id="0";

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String d=simpleDateFormat.format(date);

        ps1.setString(1,cid);
        ps1.setString(2,name);
        ps1.setString(3,urlguan);
        ps1.setString(4,kouhao);
        ps1.setString(5,hangye);
        ps1.setString(6,zihangye);
        ps1.setString(7,dizhi);
        ps1.setString(8,logo);
        ps1.setString(9,tags);
        ps1.setString(10,productlogos);
        ps1.setString(11,yewu);
        ps1.setString(12,fullname);
        ps1.setString(13,chenglitime);
        ps1.setString(14,guimo);
        ps1.setString(15,zhuangtai);
        ps1.setString(16,mongo_id);
        ps1.setString(17,"http://www.itjuzi.com/company/"+cid);
        ps1.setString(18,d);
        ps1.executeUpdate();


        Elements jpele=getElements(doc,"ul.list-main-icnset.list-compete-info li");
        if(jpele!=null){
            for(Element e:jpele){
                String rcid=getHref(e,"a","href",0).split("/",5)[4];
                String jpname=getString(e,"p.title a span",0);
                String jphy=getString(e,"i.cell.date span",0);
                String jpzhy=getString(e,"i.cell.date span",1);
                String jplogo=getHref(e,"span.incicon","style",0).replace("background-image: url(","").replace("?imageView2/0/w/58/q/100)","");
                String jprzlc=getString(e,"i.cell.action span",0);
                String jprzje=getString(e,"i.cell.action span",2);

                ps2.setString(1, cid);
                ps2.setString(2,rcid);
                ps2.setString(3,jpname);
                ps2.setString(4,jphy);
                ps2.setString(5,jpzhy);
                ps2.setString(6,jplogo);
                ps2.setString(7,jprzlc);
                ps2.setString(8,jprzje);
                ps2.setString(9,d);
                ps2.addBatch();
            }
            ps2.executeBatch();
        }


        Elements rzele=getElements(doc,"table.list-round-v2 tbody tr");
        if(rzele!=null){
            for(Element e:rzele){
                String rzjjurl=getHref(e,"td.mobile-none span.round a","href",0);
                String fid=getHref(e,"td.mobile-none span.round a","href",0).split("/",5)[4];
                String rztime=getString(e,"td span.date.c-gray",0);
                String rzlc=getString(e,"td.mobile-none span.round a",0);
                String rzje=getString(e,"td span.finades a",0);
                String tzfs=getString(e,"td span.c-gray:not(.date)",0);
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",1))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",1);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",2))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",2);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",3))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",3);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",4))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",4);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",5))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",5);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",6))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",6);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",7))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",7);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",8))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",8);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",9))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",9);
                }
                if(StringUtils.isNotEmpty(getString(e,"td span.c-gray:not(.date)",10))){
                    tzfs=tzfs+";"+getString(e,"td span.c-gray:not(.date)",10);
                }
                String tzf=tzfs;
                Elements tzs=getElements(e,"td",3,"a");
                if(tzs!=null){
                    StringBuffer st=new StringBuffer();
                    for(Element te:tzs){
                        st.append(te.text()+";");
                    }
                    tzf=st.toString()+tzfs;
                }



                ps3.setString(1, cid);
                ps3.setString(2, fid);
                ps3.setString(3, name);
                ps3.setString(4,rztime);
                ps3.setString(5,rzlc);
                ps3.setString(6,rzje);
                ps3.setString(7,tzf);
                ps3.setString(8,rzjjurl);
                ps3.setString(9,d);
                ps3.addBatch();
            }
            ps3.executeBatch();
        }


        Elements csrele=getElements(doc,"ul.list-prodcase.limited-itemnum li");
        if(csrele!=null){
            for(Element e:csrele){
                String csrname=getString(e,"div.right h4.person-name a.title b span.c",0);
                String csrzw=getString(e,"div.right h4.person-name a.title b span.c-gray",0);
                String csrjj=getString(e,"p.mart10.person-des",0);
                String weibo=getHref(e, "div.right h4.person-name span.links.flr a", "href", 0);

                ps4.setString(1, cid);
                ps4.setString(2,csrname);
                ps4.setString(3,csrzw);
                ps4.setString(4,csrjj);
                ps4.setString(5,weibo);
                ps4.setString(6,d);
                ps4.addBatch();
            }
            ps4.executeBatch();
        }


        Elements xwele=getElements(doc,"ul.list-news.timelined.limited-itemnum li.ugc-block-item.bgpink");
        if(xwele!=null){
            for(Element e:xwele){
                String xwtitle=getString(e,"div.on-edit-hide p.title.lihoverc a",0);
                String xwtime=getString(e,"div.on-edit-hide p span.t-small.c-gray.marr10",0);
                String xwtype=getString(e,"div.on-edit-hide p span.tag.lower.gray",0);
                String xwly=getString(e,"div.on-edit-hide p span.from.c-gray",0);
                String xwlj=getHref(e, "div.on-edit-hide p.title.lihoverc a", "href", 0);

                ps5.setString(1, cid);
                ps5.setString(2,xwtitle);
                ps5.setString(3,xwtime);
                ps5.setString(4,xwtype);
                ps5.setString(5,xwly);
                ps5.setString(6,xwlj);
                ps5.setString(7,d);
                ps5.addBatch();
            }
            ps5.executeBatch();
        }


        Elements proele=getElements(doc,"ul.list-prod.limited-itemnum li.ugc-block-item");
        if(proele!=null){
            for(Element e:proele){
                String proname=getString(e,"div.on-edit-hide h4 b a",0);
                String protype=getString(e,"div.on-edit-hide h4 span.tag.yellow",0);
                String prodes=getString(e,"div.on-edit-hide p",0);
                String prourl=getHref(e, "div.on-edit-hide h4 b a","href",0);

                ps6.setString(1, cid);
                ps6.setString(2,proname);
                ps6.setString(3, protype);
                ps6.setString(4,prodes);
                ps6.setString(5,prourl);
                ps6.setString(6,d);
                ps6.addBatch();
            }
            ps6.executeBatch();
        }


        Elements lcbele=getElements(doc,"ul.list-milestone.timelined.limited-itemnum li.ugc-block-item.bgpink");
        if(lcbele!=null){
            for(Element e:lcbele){
                String lcbtitle=getString(e,"div.on-edit-hide p",0);
                String lcbtime=getString(e,"div.on-edit-hide p span.t-small.c-gray",0);
                ps7.setString(1, cid);
                ps7.setString(2,lcbtitle);
                ps7.setString(3,lcbtime);
                ps7.setString(4,d);
                ps7.addBatch();
            }
            ps7.executeBatch();
        }

        for(Map.Entry<String,String> entry:map.entrySet()){
            String tagurl=entry.getKey();
            String tag=entry.getValue();
            ps8.setString(1, cid);
            ps8.setString(2,tag);
            ps8.setString(3,tagurl);
            ps8.setString(4,d);
            ps8.addBatch();
        }
        ps8.executeBatch();

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

    public static void deletemongo(MongoCollection collection, String mongo_id){
        org.bson.Document document = new org.bson.Document("_id", mongo_id);
        collection.deleteOne(document);
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


    public static String[] jiexi() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/home/etl_user/etl/program/config/itjzzl.xml")));
        org.dom4j.Element root=dom.getRootElement();
        org.dom4j.Element table=root.element("table");
        String page=table.element("page").getText();
        String ip=table.element("ip").getText();
        String database=table.element("database").getText();
        String username=table.element("username").getText();
        String password=table.element("password").getText();
        String zhanghu=table.element("zhanghu").getText();
        String mima=table.element("mima").getText();
        String day=table.element("day").getText();
        String str[]=new String[]{page,ip,database,username,password,day,zhanghu,mima};
        /*XMLWriter writer = new XMLWriter(new FileWriter("output.xml"));
        writer.write(doc);
        writer.close();*/
        return str;
    }


}
