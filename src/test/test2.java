package test;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;
import spiderKc.kcBean.kcBean;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/15.
 */
public class test2 {
    public static void main(String args[]) throws InterruptedException, IOException {

        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

       /* DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));*/



        final HttpClientBuilder builder = HttpClients.custom().setProxy(proxy);

        /*builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);*/


        int x=0;
        ExecutorService pool= Executors.newFixedThreadPool(11);
        for(int i=1;i<=11;i++) {
            final int finalX = x;

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        storedata(finalX,builder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+4000;
        }
    }

    public static String getip(int p) throws IOException {
        Document doc=null;
        String ip[]=null;
        doc=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=988966469901915&num=100&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=2&an_an=1&an_ha=1&sep=1")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .get();
        ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        if(p>=ip.length-5){
            doc=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=988966469901915&num=100&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=2&an_an=1&an_ha=1&sep=1")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                   .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();
            ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        }
        return ip[p];
    }




    public static String get(String packages,String host,HttpClientBuilder builder,String pid) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        int p=0;
        String da=null;
        String proxyIpAndPort=getip(p);
        da=proxyIpAndPort;
        p++;
        DesiredCapabilities caps = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        caps.setCapability("takesScreenshot",false);
        System.setProperty(Count.phantomjs, "/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        WebDriver driver = null;
        driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        try {
            driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
        }catch (Exception e){
            driver.quit();
            while(true){
                int flag=0;
                String proxyIpAndPort3=getip(p);
                da=proxyIpAndPort3;
                p++;
                DesiredCapabilities caps3 = new DesiredCapabilities();
                Proxy proxy3=new Proxy();
                proxy3.setHttpProxy(proxyIpAndPort3).setFtpProxy(proxyIpAndPort3).setSslProxy(proxyIpAndPort3);
                caps3.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
                caps3.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
                caps3.setCapability(CapabilityType.PROXY, proxy3);
                caps3.setCapability("takesScreenshot", false);
                System.setProperty(Count.phantomjs, "/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
                driver = new PhantomJSDriver(caps3);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        Thread.sleep(2000);
        System.out.println(driver.manage().getCookies());
        if(driver.manage().getCookies()==null||!driver.manage().getCookies().toString().contains("kuchuan")){
            driver.quit();
            while(true){
                String proxyIpAndPort2=getip(p);
                da=proxyIpAndPort2;
                p++;
                DesiredCapabilities caps2 = new DesiredCapabilities();
                Proxy proxy2=new Proxy();
                proxy2.setHttpProxy(proxyIpAndPort2).setFtpProxy(proxyIpAndPort2).setSslProxy(proxyIpAndPort2);
                caps2.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
                caps2.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
                caps2.setCapability(CapabilityType.PROXY, proxy2);
                caps2.setCapability("takesScreenshot",false);
                System.setProperty(Count.phantomjs, "/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
                driver = new PhantomJSDriver(caps2);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
                }catch (Exception e){
                    driver.quit();
                    while(true){
                        int flag=0;
                        String proxyIpAndPort3=getip(p);
                        da=proxyIpAndPort3;
                        p++;
                        DesiredCapabilities caps3 = new DesiredCapabilities();
                        Proxy proxy3=new Proxy();
                        proxy3.setHttpProxy(proxyIpAndPort3).setFtpProxy(proxyIpAndPort3).setSslProxy(proxyIpAndPort3);
                        caps3.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
                        caps3.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
                        caps3.setCapability(CapabilityType.PROXY, proxy3);
                        caps3.setCapability("takesScreenshot",false);
                        System.setProperty(Count.phantomjs, "/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
                        driver = new PhantomJSDriver(caps3);
                        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                        try {
                            driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
                        }catch (Exception e1){
                            driver.quit();
                            flag=1;
                        }
                        if(flag==0){
                            break;
                        }
                    }
                }
                Thread.sleep(2000);
                if(driver.manage().getCookies()!=null&&driver.manage().getCookies().toString().contains("kuchuan")){
                    break;
                }else{
                    driver.quit();
                }
            }
        }
        Set<Cookie> set=driver.manage().getCookies();
        String coo=set.toString().replace("[","").replace("]", "");
        String cooo[]=coo.split(";");
        Map map=new HashMap();
        for(int x=0;x<cooo.length;x++){
            if(!cooo[x].contains("expires")) {
                if(cooo[x].contains("domain")&&cooo[x].contains(",")){
                    map.put(cooo[x].split("=", 2)[1].split(",",2)[1].split("=",2)[0],cooo[x].split("=", 2)[1].split(",",2)[1].split("=",2)[1]);
                }
            }
        }

        driver.quit();
        System.out.println(map);

        String driver1="com.mysql.jdbc.Driver";
        String username="tech_spider";
        String password="sPiDer$#@!23";
        String url="jdbc:mysql://101.200.161.221:3306/coolchuan_tmp?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        Class.forName(driver1).newInstance();
        java.sql.Connection con1=null;
        try {
            con1 = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            while(true){
                con1 = DriverManager.getConnection(url, username, password);
                if(con1!=null){
                    break;
                }
            }
        }

        String sql="update app_factor set Cookie=? where pid=?";
        PreparedStatement ps=con1.prepareStatement(sql);
        ps.setString(1,map.toString());
        ps.setString(2,pid);
        ps.executeUpdate();

        HttpHost proxyo = new HttpHost(da.split(":",2)[0], Integer.parseInt(da.split(":",2)[1]),"http");
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setProxy(proxyo).setDefaultCookieStore(cookieStore).build();
        for(Object key : map.keySet()){
            BasicClientCookie cookie = new BasicClientCookie((String) key, (String) map.get(key));
            cookie.setDomain("android.kuchuan.com");
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }



        String tag=null;
        HttpGet get=new HttpGet("http://android.kuchuan.com/histortydailydownload?packagename="+packages+"&start_date=&end_date=&longType=7-d");
        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response=httpclient.execute(get);
        HttpEntity resEntity=response.getEntity();
        if(resEntity!=null){
            tag= EntityUtils.toString(resEntity);
        }

        return tag;
    }








    public static void storedata(int x,HttpClientBuilder builder) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/innotree_data_2.0?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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

        String url="jdbc:mysql://101.200.161.221:3306/coolchuan_tmp?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        Class.forName(driver1).newInstance();
        java.sql.Connection con1=null;
        try {
            con1 = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            while(true){
                con1 = DriverManager.getConnection(url, username, password);
                if(con1!=null){
                    break;
                }
            }
        }

        String insertappsum="insert into app_sum(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pssum=con1.prepareStatement(insertappsum);

        String insertapp360="insert into app_360(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps360=con1.prepareStatement(insertapp360);

        String insertappbaidu="insert into app_baidu(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psbaidu=con1.prepareStatement(insertappbaidu);

        String insertapphw="insert into app_hw(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pshw=con1.prepareStatement(insertapphw);

        String insertapplx="insert into app_lx(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pslx=con1.prepareStatement(insertapplx);

        String insertappmz="insert into app_mz(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psmz=con1.prepareStatement(insertappmz);

        String insertappoppo="insert into app_oppo(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psoppo=con1.prepareStatement(insertappoppo);

        String insertappvivo="insert into app_vivo(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psvivo=con1.prepareStatement(insertappvivo);

        String insertappwdj="insert into app_wdj(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pswdj=con1.prepareStatement(insertappwdj);

        String insertappyyb="insert into app_yyb(pid,`name`,one,two,three,four,five,six,seven) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement psyyb=con1.prepareStatement(insertappyyb);



        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("coolChuan");
        MongoCollection collection = db.getCollection("app");



        String select="select pid,`name`,package,host from app_factor WHERE flag_implement!='1' and package!='' limit "+x+","+4000;
        PreparedStatement ps=con1.prepareStatement(select);
        ResultSet rs=ps.executeQuery();

        String updateFactor="update app_factor set flag=? where pid=?";
        PreparedStatement ps1=con1.prepareStatement(updateFactor);

        String updateFactor2="update app_factor set mongodb_id=? where pid=?";
        PreparedStatement ps2=con1.prepareStatement(updateFactor2);

        String updateFactor3="update app_factor set flag_implement=? where pid=?";
        PreparedStatement ps3=con1.prepareStatement(updateFactor3);





        int i=1;
        while(rs.next()){
            String packages = rs.getString(rs.findColumn("package"));
            String pid = rs.getString(rs.findColumn("pid"));
            String name = rs.getString(rs.findColumn("name"));
            String host=rs.getString(rs.findColumn("host"));
            try {
                System.out.println(packages);
                String Tag = get(packages,host,builder,pid);
                Gson gson = null;
                System.out.println(Tag);
                gson = new Gson();
                kcBean kc = null;
                kc = gson.fromJson(Tag, kcBean.class);
                if (kc.msg.equals("请求应用不允许")||kc.msg.equals("请求不允许")) {
                    ps1.setString(1, "0");
                    ps1.setString(2, pid);
                    ps1.executeUpdate();
                } else {
                    String uuid = UUID.randomUUID().toString();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    DBObject dbObject = (DBObject) JSON.parse(gson.toJson(kc));
                    org.bson.Document document = new org.bson.Document("_id", uuid).
                            append("pid", pid).
                            append("json", dbObject).
                            append("time", time);
                    collection.insertOne(document);
                    ps2.setString(1, uuid);
                    ps2.setString(2, pid);
                    ps2.executeUpdate();
                    for (int a = 0; a < kc.data.series.size(); a++) {
                        if (kc.data.series.get(a).name.equals("汇总")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            pssum.setString(1, pid);
                            pssum.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                pssum.setString(3, downs.get(0));
                            } else {
                                pssum.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                pssum.setString(4, downs.get(1));
                            } else {
                                pssum.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                pssum.setString(5, downs.get(2));
                            } else {
                                pssum.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                pssum.setString(6, downs.get(3));
                            } else {
                                pssum.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                pssum.setString(7, downs.get(4));
                            } else {
                                pssum.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                pssum.setString(8, downs.get(5));
                            } else {
                                pssum.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                pssum.setString(9, downs.get(6));
                            } else {
                                pssum.setString(9, "0");
                            }
                            try {
                                pssum.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("360")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            ps360.setString(1, pid);
                            ps360.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                ps360.setString(3, downs.get(0));
                            } else {
                                ps360.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                ps360.setString(4, downs.get(1));
                            } else {
                                ps360.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                ps360.setString(5, downs.get(2));
                            } else {
                                ps360.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                ps360.setString(6, downs.get(3));
                            } else {
                                ps360.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                ps360.setString(7, downs.get(4));
                            } else {
                                ps360.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                ps360.setString(8, downs.get(5));
                            } else {
                                ps360.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                ps360.setString(9, downs.get(6));
                            } else {
                                ps360.setString(9, "0");
                            }
                            try {
                                ps360.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("百度")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            psbaidu.setString(1, pid);
                            psbaidu.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                psbaidu.setString(3, downs.get(0));
                            } else {
                                psbaidu.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                psbaidu.setString(4, downs.get(1));
                            } else {
                                psbaidu.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                psbaidu.setString(5, downs.get(2));
                            } else {
                                psbaidu.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                psbaidu.setString(6, downs.get(3));
                            } else {
                                psbaidu.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                psbaidu.setString(7, downs.get(4));
                            } else {
                                psbaidu.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                psbaidu.setString(8, downs.get(5));
                            } else {
                                psbaidu.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                psbaidu.setString(9, downs.get(6));
                            } else {
                                psbaidu.setString(9, "0");
                            }
                            try {
                                psbaidu.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("应用宝")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            psyyb.setString(1, pid);
                            psyyb.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                psyyb.setString(3, downs.get(0));
                            } else {
                                psyyb.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                psyyb.setString(4, downs.get(1));
                            } else {
                                psyyb.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                psyyb.setString(5, downs.get(2));
                            } else {
                                psyyb.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                psyyb.setString(6, downs.get(3));
                            } else {
                                psyyb.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                psyyb.setString(7, downs.get(4));
                            } else {
                                psyyb.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                psyyb.setString(8, downs.get(5));
                            } else {
                                psyyb.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                psyyb.setString(9, downs.get(6));
                            } else {
                                psyyb.setString(9, "0");
                            }
                            try {
                                psyyb.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("豌豆荚")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            pswdj.setString(1, pid);
                            pswdj.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                pswdj.setString(3, downs.get(0));
                            } else {
                                pswdj.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                pswdj.setString(4, downs.get(1));
                            } else {
                                pswdj.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                pswdj.setString(5, downs.get(2));
                            } else {
                                pswdj.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                pswdj.setString(6, downs.get(3));
                            } else {
                                pswdj.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                pswdj.setString(7, downs.get(4));
                            } else {
                                pswdj.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                pswdj.setString(8, downs.get(5));
                            } else {
                                pswdj.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                pswdj.setString(9, downs.get(6));
                            } else {
                                pswdj.setString(9, "0");
                            }
                            try {
                                pswdj.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("华为")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            pshw.setString(1, pid);
                            pshw.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                pshw.setString(3, downs.get(0));
                            } else {
                                pshw.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                pshw.setString(4, downs.get(1));
                            } else {
                                pshw.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                pshw.setString(5, downs.get(2));
                            } else {
                                pshw.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                pshw.setString(6, downs.get(3));
                            } else {
                                pshw.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                pshw.setString(7, downs.get(4));
                            } else {
                                pshw.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                pshw.setString(8, downs.get(5));
                            } else {
                                pshw.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                pshw.setString(9, downs.get(6));
                            } else {
                                pshw.setString(9, "0");
                            }
                            try {
                                pshw.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("OPPO")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            psoppo.setString(1, pid);
                            psoppo.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                psoppo.setString(3, downs.get(0));
                            } else {
                                psoppo.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                psoppo.setString(4, downs.get(1));
                            } else {
                                psoppo.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                psoppo.setString(5, downs.get(2));
                            } else {
                                psoppo.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                psoppo.setString(6, downs.get(3));
                            } else {
                                psoppo.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                psoppo.setString(7, downs.get(4));
                            } else {
                                psoppo.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                psoppo.setString(8, downs.get(5));
                            } else {
                                psoppo.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                psoppo.setString(9, downs.get(6));
                            } else {
                                psoppo.setString(9, "0");
                            }
                            try {
                                psoppo.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("vivo")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            psvivo.setString(1, pid);
                            psvivo.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                psvivo.setString(3, downs.get(0));
                            } else {
                                psvivo.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                psvivo.setString(4, downs.get(1));
                            } else {
                                psvivo.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                psvivo.setString(5, downs.get(2));
                            } else {
                                psvivo.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                psvivo.setString(6, downs.get(3));
                            } else {
                                psvivo.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                psvivo.setString(7, downs.get(4));
                            } else {
                                psvivo.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                psvivo.setString(8, downs.get(5));
                            } else {
                                psvivo.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                psvivo.setString(9, downs.get(6));
                            } else {
                                psvivo.setString(9, "0");
                            }
                            try {
                                psvivo.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("魅族")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            psmz.setString(1, pid);
                            psmz.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                psmz.setString(3, downs.get(0));
                            } else {
                                psmz.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                psmz.setString(4, downs.get(1));
                            } else {
                                psmz.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                psmz.setString(5, downs.get(2));
                            } else {
                                psmz.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                psmz.setString(6, downs.get(3));
                            } else {
                                psmz.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                psmz.setString(7, downs.get(4));
                            } else {
                                psmz.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                psmz.setString(8, downs.get(5));
                            } else {
                                psmz.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                psmz.setString(9, downs.get(6));
                            } else {
                                psmz.setString(9, "0");
                            }
                            try {
                                psmz.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (kc.data.series.get(a).name.equals("联想")) {
                            List<String> downs = new ArrayList<String>();
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                downs.add(kc.data.series.get(a).data.get(b).y);
                            }
                            pslx.setString(1, pid);
                            pslx.setString(2, name);
                            if (StringUtils.isNotEmpty(downs.get(0))) {
                                pslx.setString(3, downs.get(0));
                            } else {
                                pslx.setString(3, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(1))) {
                                pslx.setString(4, downs.get(1));
                            } else {
                                pslx.setString(4, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(2))) {
                                pslx.setString(5, downs.get(2));
                            } else {
                                pslx.setString(5, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(3))) {
                                pslx.setString(6, downs.get(3));
                            } else {
                                pslx.setString(6, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(4))) {
                                pslx.setString(7, downs.get(4));
                            } else {
                                pslx.setString(7, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(5))) {
                                pslx.setString(8, downs.get(5));
                            } else {
                                pslx.setString(8, "0");
                            }
                            if (StringUtils.isNotEmpty(downs.get(6))) {
                                pslx.setString(9, downs.get(6));
                            } else {
                                pslx.setString(9, "0");
                            }
                            try {
                                pslx.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            ps3.setString(1, "1");
            ps3.setString(2, pid);
            ps3.executeUpdate();
            System.out.println("this is :"+i+"tiao"+"   "+Thread.currentThread().getName());
            System.out.println("************************************************");
            i++;
        }


    }
}
