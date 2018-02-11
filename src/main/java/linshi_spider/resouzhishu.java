package linshi_spider;

import Utils.Dup;
import com.google.gson.Gson;
import haosou.Project;
import haosou.haosouBean;
import jdk.nashorn.internal.ir.Block;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getString;

public class resouzhishu {
    // 代理隧道验证信息
    final static String ProxyUser = "H37O6V2M6C29YK9D";
    final static String ProxyPass = "2BE6C0719BB6A39B";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    private static Connection conn;
    private static Ca c;
    private static Ha h;
    private static int b=0;
    private static int hao=0;
    private Map<String,String> map=new HashMap<String, String>();
    private static Proxy proxy;
    private static resouzhishu rr;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider";
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

        conn=con;
        c=new Ca();
        h=new Ha();
        rr=new resouzhishu();

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static void main(String args[]) throws InterruptedException {
        String table=args[0];

        String bx=args[1];
        String hx=args[2];
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(table);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int qq=1;qq<=Integer.parseInt(bx);qq++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        baidu(table);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /*for(int ww=1;ww<=Integer.parseInt(hx);ww++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        haosou(table);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/

        /*Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    collor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();*/

        pool.shutdown();
        while (true){
            if(pool.isTerminated()){
                System.exit(0);
            }
            Thread.sleep(2000);
        }
    }

    public static void data(String table) throws SQLException, InterruptedException {
        String sql="select DISTINCT comp_full_name from "+table;
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String key=rs.getString(rs.findColumn("comp_full_name"));
            c.fang(key);
            h.fang(key);
        }
    }

    public static void baidu(String table) throws InterruptedException {
        while (true){
            try {
                String value = c.qu();
                if (!Dup.nullor(value)) {
                    break;
                }

                int bl = 0;
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://news.baidu.com/ns?ct=1&rn=20&ie=utf-8&bs=intitle%3A%28"+URLEncoder.encode(value,"utf-8")+"%29&rsv_bp=1&sr=0&cl=2&f=8&prevct=no&tn=news&word="+URLEncoder.encode(value,"utf-8"))
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
                                .timeout(5000)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("页面不存在")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out");
                    } finally {
                        bl++;
                        if (bl >= 50) {
                            System.out.println("begin sleep");
                            Thread.sleep(600000);
                        }
                    }
                }
                String sou = getString(doc, "div#header_top_bar span.nums", 0).replace("找到相关新闻约", "").replace("找到相关新闻", "").replace("篇", "").replace(",", "");

                String sql0="update "+table+" set bai_new='"+sou+"' where comp_full_name='"+value+"'";
                PreparedStatement ps0=conn.prepareStatement(sql0);
                ps0.executeUpdate();
                b++;
                System.out.println(b+"*************************************************");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void collor() throws InterruptedException {
        Random r=new Random();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(60) * 60 * 1000) + 7200000;
        rr.map=weblogin();
        while (true){
            long t=System.currentTimeMillis();
            if (t > (begin + cur)) {
                cur = (r.nextInt(60) * 60 * 1000) + 7200000;
                rr.map=weblogin();
                t=System.currentTimeMillis();
                begin = t;
            }
            Thread.sleep(600000);
        }
    }

    public static void haosou(String table) throws InterruptedException, IOException {
        while (true){
            try {
                String value = h.qu();
                if (!Dup.nullor(value)) {
                    break;
                }

                String jiben = "http://trends.so.com/index/overviewJson?area=%E5%85%A8%E5%9B%BD&q=" + URLEncoder.encode(value, "utf-8");
                String json = get(jiben, proxy, rr);
                Gson gson = new Gson();
                String yueguanzhudu = "";

                try {
                    haosouBean.haosous h = gson.fromJson(json, haosouBean.haosous.class);
                    yueguanzhudu = h.data.get(0).data.month_index;

                    String sql = "update " + table + " set hao_sou='" + yueguanzhudu + "' where comp_full_name='" + value+"'";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.executeUpdate();
                    hao++;
                    System.out.println(hao + "--------------------------------------------------------------");
                } catch (Exception e1) {
                    String sql = "update " + table + " set hao_sou='" + 0 + "' where comp_full_name='" + value+"'";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.executeUpdate();
                    hao++;
                    System.out.println(hao + "--------------------------------------------------------------");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Map<String,String> weblogin() throws InterruptedException {
        // "/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs"
        //System.setProperty(Count.phantomjs,"/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        DesiredCapabilities caps = new DesiredCapabilities();
        String winphanpath="D:\\工作\\hy\\资源\\phantomjs-2.1.1-windows/bin/phantomjs.exe";
        String linphanpath="/data1/spider/java_spider/phantomjs-2.1.1-linux-x86_64/bin/phantomjs";
        if(System.getProperty("os.name").contains("Windows")) {
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, winphanpath);
        }else{
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, linphanpath);
        }
        caps.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        //caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--proxy-type=http", "--proxy=proxy.abuyun.com:9020","--proxy-auth=H37O6V2M6C29YK9D:2BE6C0719BB6A39B"});
        WebDriver driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        Map<String,String> map=new HashMap<String, String>();
        while(true) {
            try {
                driver.get("http://trends.so.com/index");
                Thread.sleep(10000);
                /*FileOutputStream out=new FileOutputStream("/home/hongyu.sun/html");
                OutputStreamWriter writer=new OutputStreamWriter(out,"UTF-8");
                writer.write(driver.getPageSource());
                writer.flush();
                writer.close();
                out.close();*/
                driver.findElement(By.xpath("//*[@id=\"index\"]/header/div/ul/li[1]/a")).click();
                Thread.sleep(10000);
                driver.findElement(By.className("quc-third-part-icon-tencent")).click();
                Thread.sleep(5000);
                String handle1=driver.getWindowHandle();
                Set<String> set=driver.getWindowHandles();
                for(String  s:set){
                    if(!s.equals(handle1)){
                        driver.switchTo().window(s);
                        break;
                    }
                }
                driver.switchTo().frame(0);
                Thread.sleep(2000);
                driver.findElement(By.id("switcher_plogin")).click();
                Thread.sleep(5000);
                driver.findElement(By.id("u")).sendKeys("2471264637");
                driver.findElement(By.id("p")).sendKeys("*963.-+//x");
                Thread.sleep(5000);
                Actions action = new Actions(driver);
                action.sendKeys(Keys.ENTER);
                //action.click(driver.findElement(By.xpath("//*[@id=\"login_button\"]")));
                //driver.findElement(By.xpath("//*[@id=\"login_button\"]")).click();
                System.out.println("dianjidenglu********************************************************************************");
                boolean bo=false;
                int a=0;
                while (true) {
                    try {
                        Thread.sleep(3000);
                        action.click(driver.findElement(By.xpath("//*[@id=\"login_button\"]")));
                        driver.findElement(By.xpath("//*[@id=\"login_button\"]")).click();
                        action.sendKeys(Keys.ENTER);
                        a++;
                        if(a>=3){
                            bo=true;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("denglu success&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        break;
                    }
                }
                if(bo){
                    driver.quit();
                    driver=new PhantomJSDriver(caps);
                    driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
                    continue;
                }
                System.out.println("kaishipanduan--------------------------------------------------------------------------------------");
                driver.switchTo().window(handle1);
                while (true) {
                    Thread.sleep(1000);
                    if (driver.getPageSource().contains("878402")) {
                        Set<org.openqa.selenium.Cookie> set1 = driver.manage().getCookies();
                        for (org.openqa.selenium.Cookie c : set1) {
                            map.put(c.getName(), c.getValue());
                        }
                        break;
                    }
                }
                break;
            }catch (Exception e){
                e.printStackTrace();
                driver.quit();
                driver=new PhantomJSDriver(caps);
                driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            }
        }

        driver.quit();
        return map;
    }

    public static String get(String url, Proxy proxy, resouzhishu p) throws IOException {
        Document doc=null;
        while (true) {
            try {
                if(p.map!=null&&p.map.size()>0) {
                    doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .header("Content-Type", "application/json;charset=UTF-8")
                            .cookies(p.map)
                            .proxy(proxy)
                            .timeout(10000)
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .post();
                    if (doc != null && org.apache.commons.lang3.StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                        break;
                    }
                    Thread.sleep(500);
                }else{
                    Thread.sleep(10000);
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        String json=doc.outerHtml().replace("<html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").replace("</html>","").replace("\n","").trim();
        return json;
    }



    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }

    public static class Ha{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
