package test;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/22.
 */
public class test9 {
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {


        int x=0;
        ExecutorService pool= Executors.newFixedThreadPool(1);
        for(int i=1;i<=1;i++) {
            final int finalX = x;

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        storedata(finalX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+40;
        }



       /* ExecutorService pool= Executors.newFixedThreadPool(6);
        int y=0;
        for(int i=1;i<=6;i++) {
            final int finaly = y;

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        storedatantb(finaly);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            y=y+2;
        }*/

    }


    public static String getip(int p) throws IOException {
        Document doc=null;
        String ip[]=null;
        doc= Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
               .timeout(100000)
                .get();
        ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        if(p>=ip.length-5){
            doc=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();
            ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        }
        return ip[p];
    }








    public static String get(String code) throws IOException, InterruptedException {
        int p=0;
        int f=0;
        String da=null;
       /* String proxyIpAndPort=getip(p);
        da=proxyIpAndPort;
        p++;
        DesiredCapabilities caps = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        caps.setCapability("takesScreenshot",false);*/
        System.setProperty(Count.phantomjs,  Count.phantomjspath);
        WebDriver driver = null;
        driver=new PhantomJSDriver();
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        try {
            driver.get("http://quote.eastmoney.com/sz"+code+".html");
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
                System.setProperty(Count.phantomjs,  Count.phantomjspath);
                driver = new PhantomJSDriver(caps3);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/sz"+code+".html");
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        Thread.sleep(1000);
        if(driver.getPageSource().length()<80000){
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
                System.setProperty(Count.phantomjs,  Count.phantomjspath);
                driver = new PhantomJSDriver(caps2);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/sz"+code+".html");
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
                        System.setProperty(Count.phantomjs, Count.phantomjspath);
                        driver = new PhantomJSDriver(caps3);
                        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                        try {
                            driver.get("http://quote.eastmoney.com/sz"+code+".html");
                        }catch (Exception e1){
                            driver.quit();
                            flag=1;
                        }
                        if(flag==0){
                            break;
                        }
                    }
                }
                Thread.sleep(1000);
                f++;
                if(driver.getPageSource().length()>80000||f>=8||driver.getPageSource().contains("页面未找到")||driver.getPageSource().contains("服务器错误")){
                    f=0;
                    break;
                }else{
                    driver.quit();
                }
            }
        }

        String html=null;
        html=driver.getPageSource();
        if(!html.contains("页面未找到")&&!html.contains("服务器错误")){
            driver.quit();
        }else{
            driver.quit();
            html=getsh(code);
        }
        return html;
    }





    public static String getsanban(String code) throws IOException, InterruptedException {
        int p=0;
        int f=0;
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
        System.setProperty(Count.phantomjs, Count.phantomjspath);
        WebDriver driver = null;
        driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        try {
            driver.get("http://quote.eastmoney.com/3ban/sz"+code+".html?");
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
                System.setProperty(Count.phantomjs, Count.phantomjspath);
                driver = new PhantomJSDriver(caps3);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/3ban/sz"+code+".html?");
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        Thread.sleep(1000);
        if(driver.getPageSource().length()<80000){
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
                System.setProperty(Count.phantomjs,Count.phantomjspath);
                driver = new PhantomJSDriver(caps2);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/3ban/sz"+code+".html?");
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
                        System.setProperty(Count.phantomjs,Count.phantomjspath);
                        driver = new PhantomJSDriver(caps3);
                        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                        try {
                            driver.get("http://quote.eastmoney.com/3ban/sz"+code+".html?");
                        }catch (Exception e1){
                            driver.quit();
                            flag=1;
                        }
                        if(flag==0){
                            break;
                        }
                    }
                }
                Thread.sleep(1000);
                f++;
                if(driver.getPageSource().length()>80000||f>=50||driver.getPageSource().contains("页面未找到")||driver.getPageSource().contains("服务器错误")){
                    f=0;
                    break;
                }else{
                    driver.quit();
                }
            }
        }

        String html=null;
        html=driver.getPageSource();
        if(!html.contains("页面未找到")&&!html.contains("服务器错误")){
            driver.quit();
        }else{
            driver.quit();
            html=getsanbansh(code);
        }
        return html;
    }



    public static String getsh(String code) throws IOException, InterruptedException {
        int p=0;
        int f=0;
        String da=null;
        /*String proxyIpAndPort=getip(p);
        da=proxyIpAndPort;
        p++;
        DesiredCapabilities caps = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        caps.setCapability("takesScreenshot",false);*/
        System.setProperty(Count.phantomjs, Count.phantomjspath);
        WebDriver driver = null;
        driver=new PhantomJSDriver();
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        try {
            driver.get("http://quote.eastmoney.com/sh"+code+".html?");
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
                System.setProperty(Count.phantomjs,  Count.phantomjspath);
                driver = new PhantomJSDriver(caps3);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/sh"+code+".html?");
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        Thread.sleep(1000);
        if(driver.getPageSource().length()<80000){
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
                System.setProperty(Count.phantomjs,  Count.phantomjspath);
                driver = new PhantomJSDriver(caps2);
                driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                try {
                    driver.get("http://quote.eastmoney.com/sh"+code+".html?");
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
                        System.setProperty(Count.phantomjs, Count.phantomjspath);
                        driver = new PhantomJSDriver(caps3);
                        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                        try {
                            driver.get("http://quote.eastmoney.com/sh"+code+".html?");
                        }catch (Exception e1){
                            driver.quit();
                            flag=1;
                        }
                        if(flag==0){
                            break;
                        }
                    }
                }
                Thread.sleep(1000);
                f++;
                if(driver.getPageSource().length()>80000||f>=8||driver.getPageSource().contains("页面未找到")||driver.getPageSource().contains("服务器错误")){
                    f=0;
                    break;
                }else{
                    driver.quit();
                }
            }
        }



        String html=driver.getPageSource();
        driver.quit();
        return html;
    }



    public static String getsanbansh(String code) throws IOException, InterruptedException {
        int p=0;
        int f=0;
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
            driver.get("http://quote.eastmoney.com/3ban/sh"+code+".html?");
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
                    driver.get("http://quote.eastmoney.com/3ban/sh"+code+".html?");
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        Thread.sleep(1000);
        if(driver.getPageSource().length()<80000){
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
                    driver.get("http://quote.eastmoney.com/3ban/sh"+code+".html?");
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
                        System.setProperty(Count.phantomjs,"/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
                        driver = new PhantomJSDriver(caps3);
                        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                        try {
                            driver.get("http://quote.eastmoney.com/3ban/sh"+code+".html?");
                        }catch (Exception e1){
                            driver.quit();
                            flag=1;
                        }
                        if(flag==0){
                            break;
                        }
                    }
                }
                Thread.sleep(1000);
                f++;
                if(driver.getPageSource().length()>80000||f>=50||driver.getPageSource().contains("页面未找到")||driver.getPageSource().contains("服务器错误")){
                    f=0;
                    break;
                }else{
                    driver.quit();
                }
            }
        }



        String html=driver.getPageSource();

        driver.quit();
        return html;
    }










    public static void storedata(int x) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/dc_cscyl?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String select="select ncid,stockcode from comp_baseinfo_a where flag=0 limit "+x+","+40;
        PreparedStatement ps=con.prepareStatement(select);

        String update="update comp_baseinfo_a set flag=? where ncid=?";
        PreparedStatement psu=con.prepareStatement(update);


        String insert="insert into comp_baseinfo_shares(ncid,today_open,`max`,`limit`,`turnover`,`volume`,`earnings`,`total_market_value`,`zuoshou`,`min`,`limit_zuo`,`volume_ratio`,`turnover_zuo`,shijing,capitalization,quarter,profit,PE,assets,rate,revenue,revenue_tongbi,profit_gu,profit_gu_tongbi,margin,interest,ROE,ratio,total_share_capital,gross_value,tradable_shares,flow_value,earnings_per_share,listing,`mongo_id`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss=con.prepareStatement(insert);

        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("gupiao");
        MongoCollection collection = db.getCollection("gupiao");


        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            String stockcode = rs.getString(rs.findColumn("stockcode"));
            String ncid = rs.getString(rs.findColumn("ncid"));
            try {
                String html = get("600237");
                System.out.println(html);
                Document doc = Jsoup.parse(html);
                if (!doc.select("title").text().contains("页面未找到") && !doc.select("h1").text().contains("服务器错误")) {
                    String jinkai = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt1").text().replace("-", "00");
                    String zuigao = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt2").text().replace("-", "00");
                    String zhangting = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt3").text().replace("-", "00");
                    String huanshou = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt4").text().replace("-", "");
                    String chengjiaoliang = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt5").text().replace("-", "");
                    String shiying = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt6").text().replace("-", "00");
                    String zongshizhi = doc.select("div.data-middle table.yfw tbody tr").get(0).select("td#gt7").text().replace("亿", "").replace("-", "");

                    String zuoshou = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt8").text().replace("-", "00");
                    String zuidi = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt9").text().replace("-", "00");
                    String zhangting2 = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt10").text().replace("-", "00");
                    String liangbi = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt11").text().replace("-", "00");
                    String chengjiaoe = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt12").text().replace("亿", "").replace("-", "00");
                    String shijing = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt13").text().replace("-", "00");
                    String liutongshizhi = doc.select("div.data-middle table.yfw tbody tr").get(1).select("td#gt14").text().replace("亿", "").replace("-", "00");

                    String jidu = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(0).select("td").get(0).select("span").text().replace("-", "00");
                    String shouyi = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(0).select("td").get(0).text().replace("：", "").replace("收益(" + jidu + ")", "").replace("-", "00");
                    String PE = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(0).select("td").get(1).select("span").text().replace("PE(动)：", "").replace("亿", "").replace("-", "00");
                    String jingzichan = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(1).select("td").get(0).text().replace("净资产", "").replace("亿", "").replace("：", "").replace("-", "00");
                    String shijinglv = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(1).select("td").get(1).select("span").text().replace("-", "00");
                    String yingshou = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(2).select("td").get(0).text().replace("营收：", "").replace("亿", "").replace("-", "00");
                    String tongbi = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(2).select("td").get(1).text().replace("同比", "").replace("：", "").replace("亿", "");
                    String jinglirun = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(3).select("td").get(0).text().replace("净利润：", "").replace("亿", "").replace("-", "00");
                    String tongbijinglirun = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(3).select("td").get(1).text().replace("-", "00").replace("同比：","").replace("-", "");
                    String maolilv = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(4).select("td").get(0).text().replace("毛利率", "").replace("：", "").replace("亿", "").replace("-", "");
                    String jinglilv = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(4).select("td").get(1).text().replace("净利率：", "").replace("-", "");
                    String jingzichanshouyilv = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(5).select("td").get(0).text().replace("：", "").replace("亿", "").replace("ROE", "").replace("-", "");
                    String fuzhailv = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(5).select("td").get(1).text().replace("负债率：", "").replace("亿", "").replace("-", "00");
                    String zongguben = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(6).select("td").get(0).text().replace("总股本：", "").replace("亿", "").replace("-", "");
                    String zongzhi = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(6).select("td").get(1).select("span").text().replace("总值：", "").replace("亿", "").replace("-", "");
                    String liutonggu = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(7).select("td").get(0).text().replace("流通股：", "").replace("亿", "").replace("-", "00");
                    String liuzhi = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(7).select("td").get(1).select("span").text().replace("流值：", "").replace("亿", "").replace("-", "00");
                    String meiguweifenpeilirun = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(8).select("td").get(0).text().replace("每股未分配利润：", "").replace("元", "").replace("-", "00");
                    String shangshishijian = doc.select("div.box-x1.mb10 div.pad5 table#rtp2 tbody tr").get(9).select("td").get(0).text().replace("上市时间：", "").replace("-", "00");

                    String uuid = UUID.randomUUID().toString();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    org.bson.Document document = new org.bson.Document("_id", uuid).
                            append("ncid", ncid).
                            append("html", html).
                            append("time", time);
                    collection.insertOne(document);


                    pss.setString(1, ncid);
                    pss.setString(2, jinkai);
                    pss.setString(3, zuigao);
                    pss.setString(4, zhangting);
                    pss.setString(5, huanshou);
                    pss.setString(6, chengjiaoliang);
                    pss.setString(7, shiying);
                    pss.setString(8, zongshizhi);
                    pss.setString(9, zuoshou);
                    pss.setString(10, zuidi);
                    pss.setString(11, zhangting2);
                    pss.setString(12, liangbi);
                    pss.setString(13, chengjiaoe);
                    pss.setString(14, shijing);
                    pss.setString(15, liutongshizhi);
                    pss.setString(16, jidu);
                    pss.setString(17, shouyi);
                    pss.setString(18, PE);
                    pss.setString(19, jingzichan);
                    pss.setString(20, shijinglv);
                    pss.setString(21, yingshou);
                    pss.setString(22, tongbi);
                    pss.setString(23, jinglirun);
                    pss.setString(24, tongbijinglirun);
                    pss.setString(25, maolilv);
                    pss.setString(26, jinglilv);
                    pss.setString(27, jingzichanshouyilv);
                    pss.setString(28, fuzhailv);
                    pss.setString(29, zongguben);
                    pss.setString(30, zongzhi);
                    pss.setString(31, liutonggu);
                    pss.setString(32, liuzhi);
                    pss.setString(33, meiguweifenpeilirun);
                    pss.setString(34, shangshishijian);
                    pss.setString(35,uuid);
                    pss.executeUpdate();

                    psu.setString(1,"1");
                    psu.setString(2,ncid);
                    psu.executeUpdate();
                }else{
                    psu.setString(1,"0");
                    psu.setString(2,ncid);
                    psu.executeUpdate();
                }
            }catch (Exception e){
                psu.setString(1,"3");
                psu.setString(2,ncid);
                psu.executeUpdate();
                System.out.println("this data error");
            }
        }
    }




    public static void storedatantb() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/dc_cscyl?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String select="select ncid,stockcode from comp_baseinfo_ntb  where flag is null or flag!=1";
        PreparedStatement ps=con.prepareStatement(select);

        String select2="select ncid from comp_base_info_sanbanshares";
        PreparedStatement ps2=con.prepareStatement(select2);

        List<String> list=new ArrayList<String>();
        ResultSet rs2=ps2.executeQuery();
        while(rs2.next()){
            list.add(rs2.getString(rs2.findColumn("ncid")));
        }


        String update="update comp_baseinfo_ntb set flag=? where ncid=?";
        PreparedStatement psu=con.prepareStatement(update);

        String insert="insert into comp_base_info_sanbanshares(`max`,`min`,`transaction_price`,`total_share_capital`,`flow_of_equity`,`price_earnings_ratio`,`turnover_amount`,`number_of_shares`,`total_market_value`,`market_capitalization`,`mongo_id`,`ncid`) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pss=con.prepareStatement(insert);


        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("gupiao");
        MongoCollection collection = db.getCollection("gupiao");


        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            int flag=0;
            String stockcode = rs.getString(rs.findColumn("stockcode"));
            String ncid = rs.getString(rs.findColumn("ncid"));
            try {
                for(int p=0;p<list.size();p++){
                    if(ncid.equals(list.get(p))){
                        flag=1;
                    }
                }


                if(flag==0) {
                    String html = getsanban(stockcode);
                    Document doc = Jsoup.parse(html);
                    if (!doc.select("title").text().contains("页面未找到") && !doc.select("h1").text().contains("服务器错误")) {
                        String zuigaojia = doc.select("div.data-right table.yfw tbody tr").get(0).select("td#gt1").select("span").text();
                        String zuidijia = doc.select("div.data-right table.yfw tbody tr").get(0).select("td#gt2").select("span").text();
                        String chengjiaojunjia = doc.select("div.data-right table.yfw tbody tr").get(0).select("td#gt3").select("span").text();
                        String zongguben = doc.select("div.data-right table.yfw tbody tr").get(0).select("td#gt4").text();
                        String liutongguben = doc.select("div.data-right table.yfw tbody tr").get(0).select("td#gt5").text();

                        String shiyinglv = doc.select("div.data-right table.yfw tbody tr").get(1).select("td#gt6").text();
                        String chengjiaojine = doc.select("div.data-right table.yfw tbody tr").get(1).select("td#gt7").text();
                        String chengjiaogushu = doc.select("div.data-right table.yfw tbody tr").get(1).select("td#gt8").text();
                        String zongshizhi = doc.select("div.data-right table.yfw tbody tr").get(1).select("td#gt9").text();
                        String liutongshizhi = doc.select("div.data-right table.yfw tbody tr").get(1).select("td#gt10").text();


                        String uuid = UUID.randomUUID().toString();
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = simpleDateFormat.format(date);
                        org.bson.Document document = new org.bson.Document("_id", uuid).
                                append("ncid", ncid).
                                append("html", html).
                                append("time", time);
                        collection.insertOne(document);

                        pss.setString(1, zuigaojia);
                        pss.setString(2, zuidijia);
                        pss.setString(3, chengjiaojunjia);
                        pss.setString(4, zongguben);
                        pss.setString(5, liutongguben);
                        pss.setString(6, shiyinglv);
                        pss.setString(7, chengjiaojine);
                        pss.setString(8, chengjiaogushu);
                        pss.setString(9, zongshizhi);
                        pss.setString(10, liutongshizhi);
                        pss.setString(11, uuid);
                        pss.setString(12,ncid);
                        pss.executeUpdate();

                        psu.setString(1, "1");
                        psu.setString(2, ncid);
                        psu.executeUpdate();
                    } else {
                        psu.setString(1, "0");
                        psu.setString(2, ncid);
                        psu.executeUpdate();
                    }
                }else{
                    psu.setString(1, "1");
                    psu.setString(2, ncid);
                    psu.executeUpdate();
                }
            }catch (Exception e){
                psu.setString(1,"3");
                psu.setString(2,ncid);
                psu.executeUpdate();
                e.printStackTrace();
                System.out.println("this data is error");
            }
        }
    }



}
