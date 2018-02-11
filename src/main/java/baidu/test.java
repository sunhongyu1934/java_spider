package baidu;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import haosou.haosouBean;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;
import sun.nio.cs.ext.JIS_X_0212_Solaris;
import tianyancha.XinxiXin.XinxiXin;
import tianyancha.XinxiXin.tongji;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;
import static tianyancha.XinxiXin.XinxiXin.suan;
import static waiguo.crunc.de;
import static waiguo.crunc.ren;
import static waiguo.crunc.ss;

/**
 * Created by Administrator on 2017/8/14.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "HP5G1I415085Y7AD";
    final static String ProxyPass = "9CDAD2529F99DC54";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static java.sql.Connection conn;
    private static  Ca c=new Ca();
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.44:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

    public static void ge() throws IOException {
        Document doc=Jsoup.connect("http://epub.sipo.gov.cn/ipce.action")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .data("sic","A")
                .post();
        System.out.println(doc.outerHtml());
    }

    public static void js() throws IOException {
        Document doc=Jsoup.connect("https://www.itjuzi.com/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .get();
    }

    public static Map<String,String> logins(String[] str) throws InterruptedException {
        WebDriver driver=getdriver();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        Map<String,String> map=new HashMap<>();

        while (true){
            try {

                System.out.println("begin qingqiu");
                driver.get("https://www.itjuzi.com/user/login");
                System.out.println("qingqiu success");
                Thread.sleep(5000);
                if (driver.getPageSource().contains("403")) {
                    driver.quit();
                    driver = getdriver();
                    continue;
                }
                driver.findElement(By.name("identity")).sendKeys("13717951934");
                driver.findElement(By.name("password")).sendKeys("123456");
                driver.findElement(By.id("login_btn")).click();
                Thread.sleep(2000);
                if (driver.manage().getCookies().size() > 2) {
                    break;
                }
            }catch (Exception e){
                driver.quit();
                driver = getdriver();
            }
        }

        Set<org.openqa.selenium.Cookie> set1 = driver.manage().getCookies();
        for (org.openqa.selenium.Cookie c : set1) {
            map.put(c.getName(), c.getValue());
        }
        try {
            driver.quit();
        }catch (Exception e){

        }
        System.out.println(map);
        return map;
    }

    public static WebDriver getdriver() throws InterruptedException {
        //String proxyIpAndPort=c.qu();
        DesiredCapabilities caps = new DesiredCapabilities();
        //org.openqa.selenium.Proxy proxy=new org.openqa.selenium.Proxy();
        //proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        //caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        //caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        //caps.setCapability(CapabilityType.PROXY, proxy);
        ChromeOptions options=new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        caps.setCapability(ChromeOptions.CAPABILITY,options);
        System.setProperty("webdriver.chrome.driver",Count.chromepath);
        WebDriver driver=new ChromeDriver(options);
        return driver;
    }

    public static void getip() throws IOException, InterruptedException {
        System.out.println("keishi ip");
        RedisClu rd=new RedisClu();
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

    public static Map<String,String> login(String[] str) throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
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
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=&radar_coupon=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        post.addHeader("Accept-Encoding","gzip, deflate, br");
        post.addHeader("Accept-Language","zh-CN,zh;q=0.9");
        post.addHeader("Content-Type","application/x-www-form-urlencoded");
        post.addHeader("Host","www.itjuzi.com");
        post.addHeader("Origin","https://www.itjuzi.com");
        post.addHeader("Referer","https://www.itjuzi.com/user/login");
        post.addHeader("Upgrade-Insecure-Requests","1");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity",str[6]));
        params.add(new BasicNameValuePair("password",str[7]));
        params.add(new BasicNameValuePair("submit",""));
        params.add(new BasicNameValuePair("page",""));
        params.add(new BasicNameValuePair("url",""));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        List<Cookie> cookies =null;
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(post);
                HttpEntity resEntity = response.getEntity();
                String tag = EntityUtils.toString(resEntity);
                cookies = cookieStore.getCookies();
                System.out.println(cookies);
                if (cookies != null && cookies.size() > 2) {
                    break;
                }
                break;
            }catch (Exception e){

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

    public static void main(String args[]) throws IOException, DocumentException, InterruptedException, SQLException {
        Map<String,String> map=new HashMap<String, String>();
        //CredentialsProvider credsProvider = new BasicCredentialsProvider();

        //credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
        // proxy2 = new HttpHost(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]));
        //DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        HttpClientBuilder builder = HttpClients.custom();

        //builder.setRoutePlanner(routePlanner);

        //builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=&radar_coupon=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        //post.addHeader("Cookie",cookie);
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity","13717951934"));
        params.add(new BasicNameValuePair("password","123456"));
        params.add(new BasicNameValuePair("remember","1"));
        params.add(new BasicNameValuePair("submit",""));
        params.add(new BasicNameValuePair("page",""));
        params.add(new BasicNameValuePair("url",""));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        List<Cookie> cookies =null;
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(post);
                HttpEntity resEntity = response.getEntity();
                String tag = EntityUtils.toString(resEntity);
                cookies = cookieStore.getCookies();
                break;
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
        for (int i = 0; i < cookies.size(); i++) {
            map.put(cookies.get(i).getName(),cookies.get(i).getValue());
        }


    }



    public static class fin{
        public List<Dd> data;
        public static class Dd{
            public String url;
            public String investevents_id;
            public String date;
            public String round;
            public String money;
            public List<ito> investors;
            public static class ito{
                public String name;
            }
        }
    }

    public static void detailget() throws IOException, InterruptedException {
        while (true) {
            Document doc = Jsoup.connect("http://dealer.xcar.com.cn/89567/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8")
                   // .header("Cookie","_fwck_dealer=6c4ef926fac3a0f949a2ee27c150fafd; _appuv_dealer=199ebe970139098d0201f748473e9255; _Xdwnewuv=1; _PVXuv=59fad9b6446cb; _fwck_tools=a08d4c583111ea6e476172753a1974dd; _appuv_tools=4c94cd557a286e6759bee6ba51e19c5c; _locationInfo_=%7Burl%3A%22h%22%2Ccity_id%3A%22475%22%2Cprovince_id%3A%221%22%2C%20city_name%3A%22%25E5%258C%2597%25E4%25BA%25AC%22%7D; _Xdwuv=5096119254125; _fwck_www=68f875399721c944cf0b83e2a0718efe; _appuv_www=e2deea0de15a91b96e23efc33884cb42; BIGipServerpool-c26-xcar-dealerweb1-80=1221136138.20480.0000; ad__city=475; uv_firstv_refers=http%3A//dealer.xcar.com.cn/; _Xdwstime=1509701974; Hm_lvt_53eb54d089f7b5dd4ae2927686b183e0=1509611959,1509701105; Hm_lpvt_53eb54d089f7b5dd4ae2927686b183e0=1509701974")
                    .ignoreHttpErrors(true)
                    .proxy(proxy)
                    .ignoreContentType(true)
                    .get();
            System.out.println(doc);
            if(doc.outerHtml().length()>50) {
                System.out.println(doc.outerHtml());
            }
        }
    }

    public static  Map<String,Object>  jisuan(String tid) throws IOException {
        String pt=tid.substring(0,1);
        int  fl;
        if(String.valueOf(pt.codePointAt(0)).length()>1){
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)).substring(1,2));
        }else{
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)));
        }
        List<String> list=suan(fl);
        long ti=0;
        Gson gson = new Gson();
        tongji s = null;
        String html = null;
        org.apache.http.client.CookieStore cookieStore = new BasicCookieStore();
        Connection.Response doc2;
        while (true) {
            try {
                ti=System.currentTimeMillis();
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                        .setSocketTimeout(50000).build();

                HttpClientBuilder builder = HttpClients.custom();

                builder.setDefaultRequestConfig(requestConfig);
                HttpGet get=new HttpGet("http://www.tianyancha.com/tongji/" + URLEncoder.encode(tid, "utf-8") + ".json?_=" +ti );
                //HttpGet get=new HttpGet("https://www.tianyancha.com/");
                get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
                CloseableHttpResponse response=httpclient.execute(get);
                HttpEntity entity=response.getEntity();
                String doc= EntityUtils.toString(entity);

                doc2 = Jsoup.connect("http://www.tianyancha.com/tongji/" + URLEncoder.encode(tid, "utf-8") + ".json?_=" +ti )
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                html = doc2.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                s = gson.fromJson(html, tongji.class);
                break;
                /*if (doc != null && !doc.contains("http://www.qq.com/404/search_children.js")&& StringUtils.isNotEmpty(doc.replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.contains("abuyun")&&doc.length()>50&&!doc.contains("访问禁止")&&!doc.contains("访问拒绝")) {
                    html = doc2.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                    s = gson.fromJson(html, tongji.class);
                    break;
                }*/
            }catch (Exception e){
                System.out.println(html);
            }
        }
        System.out.println(doc2.cookies());
        System.out.println(cookieStore);
        Map<String,String> map=doc2.cookies();
        String[] chars = s.data.split(",");
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            str.append((char) Integer.parseInt(chars[i]));
        }
        String a = str.toString();
        String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");
        String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
        String[] codes = code.split(",");
        /*String shuzu="\"6\", \"b\", \"t\", \"f\", \"2\", \"z\", \"l\", \"5\", \"w\", \"h\", \"q\", \"i\", \"s\", \"e\", \"c\", \"p\", \"m\", \"u\", \"9\", \"8\", \"y\",\n" +
                "\"k\", \"j\", \"r\", \"x\", \"n\", \"-\", \"0\", \"3\", \"4\", \"d\", \"1\", \"a\", \"o\", \"7\", \"v\", \"g\"],\n" +
                "[\"1\", \"8\", \"o\", \"s\", \"z\", \"u\", \"n\", \"v\", \"m\", \"b\", \"9\", \"f\", \"d\", \"7\", \"h\", \"c\", \"p\", \"y\", \"2\", \"0\", \"3\",\n" +
                "\"j\", \"-\", \"i\", \"l\", \"k\", \"t\", \"q\", \"4\", \"6\", \"r\", \"a\", \"w\", \"5\", \"e\", \"x\", \"g\"],\n" +
                "[\"s\", \"6\", \"h\", \"0\", \"p\", \"g\", \"3\", \"n\", \"m\", \"y\", \"l\", \"d\", \"x\", \"e\", \"a\", \"k\", \"z\", \"u\", \"f\", \"4\", \"r\",\n" +
                "\"b\", \"-\", \"7\", \"o\", \"c\", \"i\", \"8\", \"v\", \"2\", \"1\", \"9\", \"q\", \"w\", \"t\", \"j\", \"5\"],\n" +
                "            [\"x\", \"7\", \"0\", \"d\", \"i\", \"g\", \"a\", \"c\", \"t\", \"h\", \"u\", \"p\", \"f\", \"6\", \"v\", \"e\", \"q\", \"4\", \"b\", \"5\", \"k\",\n" +
                "             \"w\", \"9\", \"s\", \"-\", \"j\", \"l\", \"y\", \"3\", \"o\", \"n\", \"z\", \"m\", \"2\", \"1\", \"r\", \"8\"],\n" +
                "            [\"z\", \"j\", \"3\", \"l\", \"1\", \"u\", \"s\", \"4\", \"5\", \"g\", \"c\", \"h\", \"7\", \"o\", \"t\", \"2\", \"k\", \"a\", \"-\", \"e\", \"x\",\n" +
                "             \"y\", \"b\", \"n\", \"8\", \"i\", \"6\", \"q\", \"p\", \"0\", \"d\", \"r\", \"v\", \"m\", \"w\", \"f\", \"9\"],\n" +
                "            [\"j\", \"h\", \"p\", \"x\", \"3\", \"d\", \"6\", \"5\", \"8\", \"k\", \"t\", \"l\", \"z\", \"b\", \"4\", \"n\", \"r\", \"v\", \"y\", \"m\", \"g\",\n" +
                "             \"a\", \"0\", \"1\", \"c\", \"9\", \"-\", \"2\", \"7\", \"q\", \"e\", \"w\", \"u\", \"s\", \"f\", \"o\", \"i\"],\n" +
                "            [\"8\", \"q\", \"-\", \"u\", \"d\", \"k\", \"7\", \"t\", \"z\", \"4\", \"x\", \"f\", \"v\", \"w\", \"p\", \"2\", \"e\", \"9\", \"o\", \"m\", \"5\",\n" +
                "             \"g\", \"1\", \"j\", \"i\", \"n\", \"6\", \"3\", \"r\", \"l\", \"b\", \"h\", \"y\", \"c\", \"a\", \"s\", \"0\"],\n" +
                "            [\"d\", \"4\", \"9\", \"m\", \"o\", \"i\", \"5\", \"k\", \"q\", \"n\", \"c\", \"s\", \"6\", \"b\", \"j\", \"y\", \"x\", \"l\", \"a\", \"v\", \"3\",\n" +
                "             \"t\", \"u\", \"h\", \"-\", \"r\", \"z\", \"2\", \"0\", \"7\", \"g\", \"p\", \"8\", \"f\", \"1\", \"w\", \"e\"],\n" +
                "            [\"7\", \"-\", \"g\", \"x\", \"6\", \"5\", \"n\", \"u\", \"q\", \"z\", \"w\", \"t\", \"m\", \"0\", \"h\", \"o\", \"y\", \"p\", \"i\", \"f\", \"k\",\n" +
                "             \"s\", \"9\", \"l\", \"r\", \"1\", \"2\", \"v\", \"4\", \"e\", \"8\", \"c\", \"b\", \"a\", \"d\", \"j\", \"3\"],\n" +
                "            [\"1\", \"t\", \"8\", \"z\", \"o\", \"f\", \"l\", \"5\", \"2\", \"y\", \"q\", \"9\", \"p\", \"g\", \"r\", \"x\", \"e\", \"s\", \"d\", \"4\", \"n\",\n" +
                "             \"b\", \"u\", \"a\", \"m\", \"c\", \"h\", \"j\", \"3\", \"v\", \"i\", \"0\", \"-\", \"w\", \"7\", \"k\", \"6\"]]";*/
        String[] shu2 = list.toString().replace("[","").replace("]","").replace(" ","").split(",");
        StringBuffer st = new StringBuffer();
        //String[] shu2 = shu[fl].replace("]", "").replace(",[", "").replace("[", "").split(",");
        for (int bb = 0; bb < codes.length; bb++) {
            st.append(shu2[Integer.parseInt(codes[bb])]);
        }
        String utm=st.toString();
        map.put("_utm",utm);
        map.put("token",token);
        map.put("bannerFlag","true");

        BasicClientCookie cok=new BasicClientCookie("_utm",utm);
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        BasicClientCookie cok2=new BasicClientCookie("token",token);
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        BasicClientCookie cok3=new BasicClientCookie("bannerFlag","true");
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        cookieStore.addCookie(cok);
        cookieStore.addCookie(cok2);
        cookieStore.addCookie(cok3);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("cookie",map);
        maps.put("time",ti-1);
        return maps;
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
}
