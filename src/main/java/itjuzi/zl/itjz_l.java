package itjuzi.zl;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.xpath.operations.Bool;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import spiderKc.kcBean.Count;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static Utils.JsoupUtils.*;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/4/18.
 */
public class    itjz_l {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Ca c=new Ca();
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, DocumentException, InterruptedException, ParseException {
        String str[]=jiexi();

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://"+str[1]+":3306/"+str[2]+"?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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



        Thread thread=new Thread(new Runnable() {
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
        thread.start();
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    over();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
        search(con,str);

    }

    public static void over() throws InterruptedException {
        int a=0;
        while (true){
            Thread.sleep(60000);
            a++;
            if(a>=60){
                System.exit(0);
            }
        }
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

    public static void conip() throws InterruptedException {
        while (true){
            if(c.po.size()>=10) {
                c.qu();
            }
            Thread.sleep(1000);
        }
    }



    public static void search(Connection con,String[] str) throws IOException, DocumentException, SQLException, InterruptedException, ParseException {
        int page= Integer.parseInt(jiexi()[0]);
        int day=Integer.parseInt(jiexi()[5]);
        System.out.println("begin search");
        Map<String,String> map;
        List<Object> list;
        map= login(str);


        for(int x=1;x<=page;x++) {
            String tag = null;
            while (true) {
                try {
                    String proxyIpAndPort=c.qu();
                    HttpGet get = new HttpGet("https://www.itjuzi.com/investevents?page="+x);
                    get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                    get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                    get.addHeader("Accept-Encoding","gzip, deflate, br");
                    get.addHeader("Accept-Language","zh-CN,zh;q=0.9");
                    get.addHeader("Host","www.itjuzi.com");
                    get.addHeader("Referer","https://www.itjuzi.com/investevents");
                    get.addHeader("Cookie",map.toString().replace("{","").replace("}","").replace(",",";"));
                    System.out.println(map.toString().replace("{","").replace("}","").replace(",",";"));
                    //String ip=get();
                    //CredentialsProvider credsProvider = new BasicCredentialsProvider();

                    //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
                    HttpHost proxy2 = new HttpHost(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]));
                    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                            .setSocketTimeout(5000).build();

                    HttpClientBuilder builder = HttpClients.custom();
                    builder.setRoutePlanner(routePlanner);

                    //builder.setDefaultCredentialsProvider(credsProvider);
                    builder.setDefaultRequestConfig(requestConfig);
                    CloseableHttpClient httpclient = builder.build();
                    CloseableHttpResponse response = httpclient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    tag = EntityUtils.toString(resEntity);
                    resEntity.getContent().close();
                    if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")&&!tag.contains("too many request")) {
                        for(int qq=1;qq<=5;qq++) {
                            if(!c.po.contains(proxyIpAndPort)) {
                                c.fang(proxyIpAndPort);
                            }
                        }
                        httpclient.close();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                        System.out.println(detailurl);
                        String table[] = new String[]{"it_company_pc", "it_competitor_pc", "it_tag_pc","it_finacing_pc"};
                        for (int g = 0; g < table.length; g++) {
                            flagdata(con, cid, table[g]);
                        }
                        get(con, cid,map,str);
                        oo++;
                    }
                    a++;
                }
            }
            if(br){
                break;
            }

        }
        System.exit(0);

    }

    public static void flagdata(Connection con,String cid,String table) throws SQLException {
        String sql1="delete from "+table+" where c_id='"+cid+"'";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static void get(Connection con,String cid,Map<String,String> map,String[] str) throws IOException, SQLException, InterruptedException {
        String tag = null;
        int ji=0;
        while (true) {
            try {
                String proxyIpAndPort=c.qu();
                HttpGet get = new HttpGet("http://www.itjuzi.com/company/"+cid );
                get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                get.addHeader("Accept-Encoding","gzip, deflate, br");
                get.addHeader("Accept-Language","zh-CN,zh;q=0.9");
                get.addHeader("Referer","https://www.itjuzi.com/investevents");

                get.addHeader("Host","www.itjuzi.com");
                get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                get.addHeader("Cookie",map.toString().replace("{","").replace("}","").replace(",",";"));

                //String ip=get();
                //CredentialsProvider credsProvider = new BasicCredentialsProvider();

                //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
                HttpHost proxy2 = new HttpHost(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]));
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                        .setSocketTimeout(5000).build();

                HttpClientBuilder builder = HttpClients.custom();
                builder.setRoutePlanner(routePlanner);

                //builder.setDefaultCredentialsProvider(credsProvider);
                builder.setDefaultRequestConfig(requestConfig);
                CloseableHttpClient httpclient = builder.build();


                List<Object> list;
                System.out.println("begin qingqiu detail");
                CloseableHttpResponse response = httpclient.execute(get);
                System.out.println("qingqiu detail success");
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                resEntity.getContent().close();
                ji++;
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")&&!tag.contains("No required SSL certificate")&&!tag.contains("too many request")) {
                    for(int qq=1;qq<=5;qq++) {
                        if(!c.po.contains(proxyIpAndPort)) {
                            c.fang(proxyIpAndPort);
                        }
                    }
                    httpclient.close();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Document doc=Jsoup.parse(tag);
            System.out.println("begin store data");
            storedata(con, doc, cid,map,str);
            System.out.println("insert mysql success ");
            System.out.println("------------------------------------------------------------");
        }catch (Exception e1){
            e1.printStackTrace();
            System.out.println("error");
        }

    }

    public static String get2(String cid,Map<String,String> map,String[] str) throws InterruptedException, IOException {
        String tag = null;
        int ji=0;
        while (true) {
            try {
                String proxyIpAndPort=c.qu();
                HttpGet get = new HttpGet("https://www.itjuzi.com/company/ajax_load_com_invse/"+cid );
                get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                get.addHeader("Cache-Control","max-age=0");
                get.addHeader("Host","www.itjuzi.com");
                get.addHeader("Upgrade-Insecure-Requests","1");
                get.addHeader("If-Modified-Since","Tue, 25 Jul 2017 06:33:17 GMT");
                get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
                get.addHeader("Cookie",map.toString().replace("{","").replace("}","").replace(",",";"));

                //String ip=get();
                //CredentialsProvider credsProvider = new BasicCredentialsProvider();

                //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
                HttpHost proxy2 = new HttpHost(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]));
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                        .setSocketTimeout(5000).build();

                HttpClientBuilder builder = HttpClients.custom();
                builder.setRoutePlanner(routePlanner);

                //builder.setDefaultCredentialsProvider(credsProvider);
                builder.setDefaultRequestConfig(requestConfig);

                CloseableHttpClient httpclient = builder.build();


                List<Object> list;
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                resEntity.getContent().close();
                ji++;
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")&&!tag.contains("找不到您访问的页面")&&!tag.contains("No required SSL certificate")&&!tag.contains("too many request")) {
                    for(int qq=1;qq<=5;qq++) {
                        if(!c.po.contains(proxyIpAndPort)) {
                            c.fang(proxyIpAndPort);
                        }
                    }
                    httpclient.close();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tag;
    }

    public static List<Object> logins(String[] str) throws InterruptedException, IOException {
        Map<String,String> map = new HashMap<>();
        String ip=c.qu();
       /* List<Object> list;
        list=getdriver();
        WebDriver driver= (WebDriver) list.get(0);
        ip= (String) list.get(1);
        
        while (true){
            try {

                System.out.println("begin qingqiu");
                driver.get("https://www.itjuzi.com/user/login");
                System.out.println("qingqiu success");
                Thread.sleep(5000);;
                System.out.println(driver.manage().getCookies());
                Set<org.openqa.selenium.Cookie> set1 = driver.manage().getCookies();
                String ac = null;
                String va = null;
                for (org.openqa.selenium.Cookie c : set1) {
                    if(c.getName().equals("acw_sc__")){
                        ac=c.getName();
                        va=c.getValue();
                    }
                    map.put(c.getName(), c.getValue());
                }
                map=login(str,map.toString().replace("{","").replace("}","").replace(",",";"),ip);
                if(map.size()>3){
                    map.put(ac,va);
                    try {
                        driver.quit();
                    }catch (Exception e){

                    }
                    break;
                }else{
                    driver.quit();
                    list=getdriver();
                    driver = (WebDriver) list.get(0);
                    ip= (String) list.get(1);
                }
                *//*Thread.sleep(5000);
                System.out.println("sleep over");
                System.out.println(driver.getPageSource().length());
                if (driver.getPageSource().length()<10000) {
                    driver.quit();
                    list=getdriver();
                    driver = (WebDriver) list.get(0);
                    ip= (String) list.get(1);
                    continue;
                }
                driver.findElement(By.name("identity")).sendKeys(str[6]);
                driver.findElement(By.name("password")).sendKeys(str[7]);
                System.out.println("dian ji denglu");
                driver.findElement(By.id("login_btn")).click();

                Thread.sleep(2000);
                System.out.println("kaishi huoqu cookie");
                if (driver.manage().getCookies().size() > 2) {
                    Set<org.openqa.selenium.Cookie> set1 = driver.manage().getCookies();
                    for (org.openqa.selenium.Cookie c : set1) {
                        System.out.println(c.getPath());
                        System.out.println(c.getDomain());
                        System.out.println(c.getExpiry());
                        map.put(c.getName(), c.getValue());
                        driver.quit();
                    }
                    break;
                }
                driver.quit();
                list=getdriver();
                driver = (WebDriver) list.get(0);
                ip= (String) list.get(1);*//*
            }catch (Exception e){
                e.printStackTrace();
                try {
                    driver.quit();
                }catch (Exception e1){

                }
                list=getdriver();
                driver = (WebDriver) list.get(0);
                ip= (String) list.get(1);
            }
        }*/
        //map=login(str,"",ip);
        System.out.println(map);
        List<Object> lists=new ArrayList<>();
        lists.add(map);
        lists.add(ip);
        return lists;
    }


    public static List<Object> getdriver() throws InterruptedException {
        String proxyIpAndPort=c.qu();
        DesiredCapabilities caps = new DesiredCapabilities();
        org.openqa.selenium.Proxy proxy=new org.openqa.selenium.Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        ChromeOptions options=new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        options.addArguments("--disable-plugins","--disable-images","--disable-javascript");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        caps.setCapability(ChromeOptions.CAPABILITY,options);
        System.setProperty(Count.chrome, "/data1/spider/java_spider/chrome/chromedriver");
        WebDriver driver=new ChromeDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        //driver.manage().timeouts().setScriptTimeout(20,TimeUnit.SECONDS);
        List<Object> list=new ArrayList<>();
        list.add(driver);
        list.add(proxyIpAndPort);
        return list;
    }

    public static Map<String,String> login(String[] str) throws IOException, InterruptedException {
        List<Cookie> cookies =null;
        Map<String,String> map=new HashMap<String, String>();
        while (true) {
            try {
                //创建认证，并设置认证范围

                String proxyIpAndPort=c.qu();
                //CredentialsProvider credsProvider = new BasicCredentialsProvider();
                //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
                HttpHost proxy2 = new HttpHost(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]));
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                        .setSocketTimeout(5000).build();

                HttpClientBuilder builder = HttpClients.custom();

                builder.setRoutePlanner(routePlanner);

                //builder.setDefaultCredentialsProvider(credsProvider);
                builder.setDefaultRequestConfig(requestConfig);
                CookieStore cookieStore = new BasicCookieStore();
                CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
                HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=&radar_coupon=");
                post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                //post.addHeader("Cookie",cookie);
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("identity",str[6]));
                params.add(new BasicNameValuePair("password",str[7]));
                params.add(new BasicNameValuePair("remember","1"));
                params.add(new BasicNameValuePair("submit",""));
                params.add(new BasicNameValuePair("page",""));
                params.add(new BasicNameValuePair("url",""));
                post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
                //post.setEntity(new StringEntity(json, Charset.forName("UTF-8")));

                CloseableHttpResponse response = httpclient.execute(post);
                HttpEntity resEntity = response.getEntity();
                String tag = EntityUtils.toString(resEntity);
                cookies = cookieStore.getCookies();
                if(cookies!=null&&cookies.size()>2){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
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

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                /*Document doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=552166bfe40bf4f7af05ae2b6c6ccd2a&sep=3")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String[] ips = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").trim().split(" ");
                for(String s:ips){
                    if (s.contains("requests") || s.contains("请控制")) {
                        continue;
                    }
                    c.fang(s.trim());
                }*/
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

    public static void storedata(Connection con,Document doc,String cid,Map<String,String> map,String[] str) throws SQLException, IOException, InterruptedException {
        String sql1="insert into it_company_pc(c_id,`sName`,web_url,company_slogan,company_industry,sub_industry,company_address,company_logo,company_tags,product_logos,company_introduction,company_full_name,found_time,company_scale,company_status,mongo_id,`source_url`,data_date,e_mail,phone) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

        parse(doc,cid,ps1,ps2,ps3,ps4,ps5,ps6,ps7,ps8,map,str);
    }

    public static void parse(Document doc,String cid,PreparedStatement ps1,PreparedStatement ps2,PreparedStatement ps3,PreparedStatement ps4,PreparedStatement ps5,PreparedStatement ps6,PreparedStatement ps7,PreparedStatement ps8,Map<String,String> mapd,String[] strr) throws IOException, SQLException, InterruptedException {
        Map<String,String> map=new HashMap<String,String>();
        Gson gson=new Gson();
        String name=getString(doc, "div.picinfo div.line-title span.title h1.seo-important-title", 0);
        String urlguan=getHref(doc, "div.link-line a", "href", 2);
        String kouhao=getString(doc,"div.info-line h2.seo-slogan",0);
        String hangye=getString(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a.one-level-tag",0);
        String zihangye=getString(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a.two-level-tag",0);
        String dizhi= JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-address-o) span",0);
        String logo=getHref(doc, "div.rowhead div.pic img", "src", 0);
        String tags=null;
        Elements tagele=getElements(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a");
        if(tagele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tag=getString(e, "a", 0);
                String tagurl=e.attr("href");
                map.put(tagurl,tag);
                str.append(tag+";");
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
        String yewu=JsoupUtils.getHref(doc,"meta[name='Description']","content",0);

        String fullname=getString(doc,"div.block div.des-more h2.seo-second-title",0).replace("公司全称：","");
        String chenglitime=getString(doc,"div.block div.des-more h3.seo-secand-tilte span",0);
        String guimo=getString(doc,"div.block div.des-more h3.seo-secand-tilte span",1);
        String zhuangtai=getString(doc,"div.block div.des-more span.tag.green",0);
        String email=JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-email-o) span",0);
        String phone=JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-phone-o) span",0);

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
        ps1.setString(19,email);
        ps1.setString(20,phone);
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


        int pp=0;
        Elements rzele=getElements(doc,"div#invest-portfolio table.list-round-v2 tbody tr.feedback-btn-parent");
        if(rzele!=null){
            for(Element e:rzele){
                String rzjjurl=getHref(e,"td:contains(详情) a","href",0);
                String rzlc=getString(e,"td.mobile-none span.round a",0);
                String fid=getHref(e,"td:contains(详情) a","href",0).split("/",5)[4];
                String rztime=getString(e,"td span.date",0);
                String rzje=getString(e,"td span.finades a",0);
                Elements vceles=JsoupUtils.getElements(getElement(e,"td",3),"a");
                String vc=null;
                StringBuffer str=new StringBuffer();
                if(vceles!=null){
                    for(Element ee:vceles){
                        str.append(ee.text()+";");
                    }
                }
                try{
                    vc=str.substring(0,str.length()-1).toString();
                }catch (Exception eee){

                }



                ps3.setString(1, cid);
                ps3.setString(2, fid);
                ps3.setString(3, name);
                ps3.setString(4,rztime);
                ps3.setString(5,rzlc);
                ps3.setString(6,rzje);
                ps3.setString(7,vc);
                ps3.setString(8,rzjjurl);
                ps3.setString(9,d);
                ps3.addBatch();
                pp++;
            }
            ps3.executeBatch();
        }

        if(pp>=3) {
            String json = get2(cid, mapd, strr);
            if (Dup.nullor(json)) {
                try {
                    fin f = gson.fromJson(json, fin.class);
                    for (fin.Dd dd : f.data) {
                        String rzjjurl = dd.url;
                        String rzlc = dd.round;
                        String fid = dd.investevents_id;
                        if (!Dup.nullor(fid)) {
                            fid = dd.acquisition_id;
                        }
                        String rztime = dd.date;
                        String rzje = dd.money;
                        String vc = null;
                        StringBuffer str = new StringBuffer();
                        for (fin.Dd.ito i : dd.investors) {
                            str.append(i.name + ";");
                        }
                        try {
                            vc = str.substring(0, str.length() - 1).toString();
                        } catch (Exception ee) {

                        }
                        ps3.setString(1, cid);
                        ps3.setString(2, fid);
                        ps3.setString(3, name);
                        ps3.setString(4, rztime);
                        ps3.setString(5, rzlc);
                        ps3.setString(6, rzje);
                        ps3.setString(7, vc);
                        ps3.setString(8, rzjjurl);
                        ps3.setString(9, d);
                        ps3.addBatch();
                    }
                    ps3.executeBatch();
                } catch (Exception es) {

                }
            }
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

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }


    public static String[] jiexi() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/data1/spider/java_spider/implement/itjz/itjzzl.xml")));
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

    public static class fin{
        public List<Dd> data;
        public static class Dd{
            public String url;
            public String investevents_id;
            public String acquisition_id;
            public String date;
            public String round;
            public String money;
            public List<ito> investors;
            public static class ito{
                public String name;
            }
        }
    }

}
