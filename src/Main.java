package spiderKc.kcBean;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.kcBean;

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
 * Created by Administrator on 2017/3/9.
 */
public class Main {
    private static Logger logger1 = Logger.getLogger("logger1");
    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, InterruptedException, IllegalAccessException, IOException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/nlp_company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        final List<Integer> list2=new ArrayList<Integer>();
        String select2="select pid from plabel where industry='12'";
        PreparedStatement ps2=con.prepareStatement(select2);
        ResultSet rs2=ps2.executeQuery();
        while(rs2.next()){
            list2.add(Integer.parseInt(rs2.getString(rs2.findColumn("pid"))));
        }

        int x=0;
        ExecutorService pool= Executors.newFixedThreadPool(1);
        for(int i=1;i<=1;i++) {
            final int finalX = x;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        spider( list2, finalX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+1700;
        }

    }

    public static void spider(List<Integer> list,int x) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException, IOException {
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

        String insertappsum="insert into app_download_thirty(pid,`name`,app_download,`sum`,mongodb_id) values(?,?,?,?,?)";
        PreparedStatement pssum=con1.prepareStatement(insertappsum);




        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("coolChuan");
        MongoCollection collection = db.getCollection("app_download_thirty");



        String select="select pid,`name`,package,host from app_factor where pid in"+list.toString().replace("[","(").replace("]",")"+" order by rand()"+" limit "+x+","+1700);
        PreparedStatement ps=con1.prepareStatement(select);
        ResultSet rs=ps.executeQuery();

        String updateFactor="update app_factor set flag=? where pid=?";
        PreparedStatement ps1=con1.prepareStatement(updateFactor);

        String updatep="update app_factor set test=? where pid=?";
        PreparedStatement psp=con1.prepareStatement(updatep);


        Document docr=null;
        String ip[]=null;
        docr=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1").get();
        ip=docr.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        logger1.info("begin ergodic data");
        int i=1;
        int flag=1;
        WebDriver driver=null;
        while(rs.next()){
            int test=0;
            if(flag>=(ip.length-3)){
                docr=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1").get();
                ip=docr.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
                flag=1;
            }
            String proxyIpAndPort= ip[flag];
            DesiredCapabilities cap = new DesiredCapabilities();
            Proxy proxy=new Proxy();
            proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
            cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
            cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
            cap.setCapability(CapabilityType.PROXY, proxy);
            System.setProperty(Count.phantomjs,Count.phantomjspath);
            driver=new PhantomJSDriver(cap);
            driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
            String packages = rs.getString(rs.findColumn("package"));
            String pid = rs.getString(rs.findColumn("pid"));
            String host = rs.getString(rs.findColumn("host"));
            String name = rs.getString(rs.findColumn("name"));
            try {
                driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
                Thread.sleep(3000);
                driver.get("http://android.kuchuan.com/histortydailydownload?packagename=" + packages + "&start_date=&end_date=&longType=30-d");
                logger1.info("get dmo，the pid is："+pid);
                Document doc = null;
                doc = Jsoup.parse(driver.findElement(By.xpath("/html")).getAttribute("outerHTML"));
                if(doc==null|| org.apache.commons.lang3.StringUtils.isEmpty(doc.outerHtml().toString())||!doc.outerHtml().toString().contains("请求")){
                    while(true){
                        logger1.info("ip invalid begin while");
                        flag++;
                        driver.quit();
                        String proxyIpAndPort1= ip[flag];
                        DesiredCapabilities cap1 = new DesiredCapabilities();
                        Proxy proxy1=new Proxy();
                        proxy1.setHttpProxy(proxyIpAndPort1).setFtpProxy(proxyIpAndPort1).setSslProxy(proxyIpAndPort1);
                        cap1.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
                        cap1.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
                        cap1.setCapability(CapabilityType.PROXY, proxy1);
                        System.setProperty(Count.phantomjs,Count.phantomjspath);
                        driver=new PhantomJSDriver(cap1);
                        driver.get("http://android.kuchuan.com/page/detail/download?package=" + packages + "&infomarketid=7&site=0#!/day/" + host);
                        Thread.sleep(3000);
                        driver.get("http://android.kuchuan.com/histortydailydownload?packagename=" + packages + "&start_date=&end_date=&longType=30-d");
                        logger1.info("get dmo，the pid is："+pid);
                        doc = Jsoup.parse(driver.findElement(By.xpath("/html")).getAttribute("outerHTML"));
                        if(doc!=null&& org.apache.commons.lang3.StringUtils.isNotEmpty(doc.outerHtml().toString())&&doc.outerHtml().toString().contains("请求")){
                            break;
                        }
                    }
                }
                String Tag = null;
                Tag = doc.body().toString().replace("<body>", "").replace("</body>", "").trim().replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "").replace("</pre>", "");
                Gson gson = null;
                gson = new Gson();
                kcBean kc = null;
                kc = gson.fromJson(Tag, kcBean.class);
                System.out.println(Tag);
                if (kc.msg.equals("请求应用不允许")) {
                    ps1.setString(1, "0");
                    ps1.setString(2, pid);
                    ps1.executeUpdate();
                }else {
                    logger1.info("Get valid data and start loading");
                    System.out.println(Tag);
                    long sum = 0;
                    StringBuffer str = new StringBuffer();
                    String uuid = UUID.randomUUID().toString();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    for (int a = 0; a < kc.data.series.size(); a++) {
                        if (kc.data.series.get(a).name.equals("汇总")) {
                            for (int b = 0; b < kc.data.series.get(a).data.size(); b++) {
                                str.append(kc.data.series.get(a).data.get(b).y + ",");
                                sum = sum + Integer.parseInt(kc.data.series.get(a).data.get(b).y);
                            }
                        }
                    }
                    logger1.info("begin");
                    pssum.setString(1, pid);
                    pssum.setString(2, name);
                    pssum.setString(3, str.toString());
                    pssum.setLong(4, sum);
                    pssum.setString(5, uuid);
                    try {
                        pssum.executeUpdate();
                        logger1.info("success");
                        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(kc));
                        org.bson.Document document = new org.bson.Document("_id", uuid).
                                append("pid", pid).
                                append("json", dbObject).
                                append("time", time);
                        collection.insertOne(document);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logger1.info("Data warehousing success");
                }
            }catch (Exception e){
                test=1;
                driver.quit();
                e.printStackTrace();
            }
            psp.setString(1, "1");
            psp.setString(2, pid);
            psp.executeUpdate();
            if(test==0) {
                driver.quit();
            }
            logger1.info("Current section："+i+"strip");
            System.out.println("************************************************");
            flag++;
            i++;
        }

    }

}
