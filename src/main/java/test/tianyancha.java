package test;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.File;
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
 * Created by Administrator on 2017/4/6.
 */
public class tianyancha {
    final static private int thread=500;
    public static void main(String args[]) throws InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        //final String proxyIpAndPorts[]=new String[]{"10.44.152.49:13128","10.44.50.206:13128","10.51.82.74:13128","10.44.169.75:13128","10.44.158.42:13128","10.44.137.192:13128","10.44.143.200:13128","10.44.155.195:13128"};
        //
        //
        //
        // mysql();

        int x=0;
        ExecutorService pool= Executors.newFixedThreadPool(20);
        for(int a=1;a<=20;a++){
            final int finalX = x;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        mysql(finalX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+22500;
        }

    }

    public static void mysql(int x) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        MongoDatabase db = client.getDatabase("tianYanCha");
        MongoCollection collection = db.getCollection("Html");


        String updatem="update tyc_detail set mongo_id=? where id=?";
        PreparedStatement psm=con.prepareStatement(updatem);

        String updatef="update tyc_detail set flag=? where id=?";
        PreparedStatement psf=con.prepareStatement(updatef);


        int a=1;
        String select="select com_id,id from tyc_detail where id>'2593' limit "+x+",22500";
        PreparedStatement ps=con.prepareStatement(select);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            int id= rs.getInt(rs.findColumn("id"));
            String tid=rs.getString(rs.findColumn("com_id"));
            try {
                String mongo_id = get(tid,collection);
                Thread.sleep(2000);
                psm.setString(1, mongo_id);
                psm.setInt(2, id);
                psm.executeUpdate();

                psf.setInt(1, 0);
                psf.setInt(2, id);
                psf.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
                psf.setInt(1, 1);
                psf.setInt(2, id);
                psf.executeUpdate();
            }
            System.out.println(a);
            a++;
            System.out.println("-----------------------------------------------------------------------------------");
        }

    }




    public static String get(String tianid,MongoCollection collection) throws InterruptedException, IOException {
        System.setProperty(Count.phantomjs,Count.linuxph);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=http","--proxy=proxy.abuyun.com:9020","--proxy-auth=H112205236B5G2PD:E9484DB291BFC579"});
        WebDriver driver=null;
        driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);

        int sum=0;
        Document doc=null;
        while (true){
            while (true){
                boolean b=true;
                try {
                    driver.get("http://www.tianyancha.com/company/2373229881");
                }catch (Exception e1){
                    b=false;
                    driver.quit();
                    driver=new PhantomJSDriver(caps);
                    driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                    System.out.println("get time out agein");
                }
                if(b) {
                    break;
                }
            }
            while(true) {
                Thread.sleep(thread);
                sum=sum+thread;
                if (driver.getPageSource().length() > 60000) {
                    break;
                }
                if(sum%5000==0){
                    sum=0;
                    break;
                }
            }
            if (driver.getPageSource().length() > 60000) {
                doc= Jsoup.parse(driver.getPageSource());
                break;
            }
            driver.quit();
            driver=new PhantomJSDriver(caps);
            driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        }
        driver.quit();
        String mongo_id = storemongo(doc.outerHtml(), tianid, collection);
        return mongo_id;
    }


    public static String storemongo(String html,String tianid,MongoCollection collection){
        String uuid = UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);

        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("tid", tianid).
                append("html", html).
                append("time", time);
        collection.insertOne(document);
        System.out.println("insert mongo success");
        return uuid;
    }

    public static void jietu(WebDriver driver,String screenPath) throws IOException {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE); // 关键代码，执行屏幕截图，默认会把截图保存到temp目录
        FileUtils.copyFile(scrFile, new File(screenPath));
    }
}
