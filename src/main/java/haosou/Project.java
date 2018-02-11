package haosou;

import Utils.RedisClu;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import org.apache.commons.io.FileUtils;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;


/**
 * Created by Administrator on 2017/7/6.
 */
public class Project {
    private Map<String,String> map=new HashMap<String, String>();
    private static Ca cc;
    // 代理隧道验证信息
    final static String ProxyUser = "H37O6V2M6C29YK9D";
    final static String ProxyPass = "2BE6C0719BB6A39B";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    /*private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="spider";
        String password="spider";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        final Project p=new Project();
        final Cangku c=p.new Cangku();
        Ca ccc=new Ca();
        cc=ccc;

        ExecutorService pool= Executors.newCachedThreadPool();
        final java.sql.Connection finalCon = con;
        /*pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(finalCon, c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    p.collor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        for(int x=1;x<=10;x++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        jiexi(finalCon,c,proxy,p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
        while (true){
            if (pool.isTerminated()) {
                System.out.println("结束了！");
                System.exit(0);
            }
            Thread.sleep(2000);
        }
    }

    public static void data(java.sql.Connection con,Cangku c) throws SQLException, InterruptedException {
        String sql="select comp_name from gaoxin_qiyemingdan";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            try {
                String pname = rs.getString(rs.findColumn("comp_name"));

                c.fang(new String[]{ pname});
            }catch (Exception e){
                System.out.println("fang error");
            }
        }
    }

    public static void data2(java.sql.Connection con2,Cangku c) throws SQLException, InterruptedException {
        String sql="select project_nm from baidu_news_project";
        PreparedStatement ps=con2.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            try {
                String pid = rs.getString(rs.findColumn("project_nm"));

                c.fang(new String[]{pid});
            }catch (Exception e){
                System.out.println("fang error");
            }
        }
    }

    class Cangku{
        BlockingQueue<String[]> p=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            p.put(key);
        }
        public String[] qu() throws InterruptedException {
            return p.poll(300, TimeUnit.SECONDS);
        }
    }


    public void collor() throws InterruptedException {
        Random r=new Random();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(60) * 60 * 1000) + 7200000;
        map=weblogin();
        while (true){
            long t=System.currentTimeMillis();
            if (t > (begin + cur)) {
                cur = (r.nextInt(60) * 60 * 1000) + 7200000;
                map=weblogin();
                t=System.currentTimeMillis();
                begin = t;
            }
            Thread.sleep(600000);
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

    public static String get(String url,Proxy proxy,Project p) throws IOException {
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
                    if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("abuyun")) {
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

    public static void jiexi(java.sql.Connection con,Cangku c,Proxy proxy,Project pp) throws IOException, SQLException, InterruptedException {
        Random r=new Random();
        String sql="insert into haosou_new(p_name,guan_zhu_seven,guan_zhu_t,guanzhu_huanbi_s,guanzhu_huanbi_t,guanzhu_tongbi_s,guanzhu_tongbi_t,man,woman,haixihuan,diyu,yearf) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        String tr=null;
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(60) * 60 * 1000) + 7200000;
        int p=0;
        while (true) {
            try {
                String[] values = c.qu();
                if(values==null){
                    Thread.sleep(300000);
                    System.exit(0);
                }
                String pname = values[0];
                String jiben = "http://trends.so.com/index/overviewJson?area=%E5%85%A8%E5%9B%BD&q=" + URLEncoder.encode(pname, "utf-8");
                String json = get(jiben,proxy,pp);
                tr=json;
                Gson gson = new Gson();
                String zhoutongbi = "";
                String yuetongbi = "";
                String zhouhuanbi = "";
                String yuehuanbi = "";
                String zhouguanzhudu = "";
                String yueguanzhudu = "";
                try {
                    haosouBean.haosous h = gson.fromJson(json, haosouBean.haosous.class);
                    zhoutongbi = h.data.get(0).data.week_year_ratio;
                    yuetongbi = h.data.get(0).data.month_year_ratio;
                    zhouhuanbi = h.data.get(0).data.week_chain_ratio;
                    yuehuanbi = h.data.get(0).data.month_chain_ratio;
                    zhouguanzhudu = h.data.get(0).data.week_index;
                    yueguanzhudu = h.data.get(0).data.month_index;
                }catch (Exception e1){

                }

                String huaxiang = "http://trends.so.com/index/indexquerygraph?t=30&area=%E5%85%A8%E5%9B%BD&q=" + URLEncoder.encode(pname, "utf-8");
                String json2 = get( huaxiang,proxy,pp);
                String nan = "";
                String nv = "";
                String haixi ="";
                String diyu ="";
                String age ="";
                try {
                    haosouBean.quantu q = gson.fromJson(json2, haosouBean.quantu.class);
                    for (haosouBean.quantu.Data.SSS s : q.data.sex) {
                        if (s.entity.equals("01")) {
                            nan = s.percent;
                        } else if (s.entity.equals("02")) {
                            nv = s.percent;
                        }
                    }
                    List<haosouBean.quantu.Data.Int> haixihuan = q.data.interest;
                    JSONArray js = JSONArray.fromObject(haixihuan.toString());
                    haixi = js.toString();

                    List<haosouBean.quantu.Data.Pro> diyus = q.data.province;
                    JSONArray js2 = JSONArray.fromObject(diyus.toString());
                    diyu = js2.toString();

                    List<haosouBean.quantu.Data.AAA> agss = q.data.age;
                    JSONArray js3 = JSONArray.fromObject(agss.toString());
                    age = js3.toString();
                }catch (Exception e){

                }


                ps.setString(1, pname);
                ps.setString(2, zhouguanzhudu);
                ps.setString(3, yueguanzhudu);
                ps.setString(4, zhouhuanbi);
                ps.setString(5, yuehuanbi);
                ps.setString(6, zhoutongbi);
                ps.setString(7, yuetongbi);
                ps.setString(8, nan);
                ps.setString(9, nv);
                ps.setString(10, haixi);
                ps.setString(11, diyu);
                ps.setString(12, age);
                ps.executeUpdate();
                System.out.println("success_haosou");
                p++;
                System.out.println(p+"******************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                String ip=rd.get("ip");
                if(cc.po.size()<=5) {
                    cc.fang(ip);
                }
                System.out.println(cc.po.size()+"    ip***********************************************");
                Thread.sleep(4000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }



    public static void login() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Map<String,String> map=new HashMap<String, String>();
        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H37O6V2M6C29YK9D", "2BE6C0719BB6A39B"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();
        HttpClientBuilder builder= HttpClients.custom();
        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).setSSLSocketFactory(createSSLConnSocketFactory()).build();
        HttpPost post=new HttpPost("https://login.360.cn/");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("src","pcw_360index"));
        params.add(new BasicNameValuePair("from","pcw_360index"));
        params.add(new BasicNameValuePair("charset","UTF-8"));
        params.add(new BasicNameValuePair("requestScema","https"));
        params.add(new BasicNameValuePair("o","sso"));
        params.add(new BasicNameValuePair("m","login"));
        params.add(new BasicNameValuePair("lm","0"));
        params.add(new BasicNameValuePair("captFlag","1"));
        params.add(new BasicNameValuePair("rtype","data"));
        params.add(new BasicNameValuePair("validatelm","0"));
        params.add(new BasicNameValuePair("isKeepAlive","1"));
        params.add(new BasicNameValuePair("captchaApp","i360"));
        params.add(new BasicNameValuePair("userName","2471264637@qq.com"));
        params.add(new BasicNameValuePair("type","normal"));
        params.add(new BasicNameValuePair("account","2471264637@qq.com"));
        params.add(new BasicNameValuePair("password","0d2c260ea50b5190eb8f243c31c856b9"));
        params.add(new BasicNameValuePair("captcha",""));
        params.add(new BasicNameValuePair("token","6b0e6dce0c50bfab"));
        params.add(new BasicNameValuePair("proxy","http://trends.so.com/psp_jump.html"));
        params.add(new BasicNameValuePair("callback","QiUserJsonp308500917"));
        params.add(new BasicNameValuePair("func","QiUserJsonp308500917"));
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

        System.out.println(map);
    }

    public static void denglu() throws IOException {
        Connection.Response res= Jsoup.connect("https://login.360.cn/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .header("Content-Type","application/x-www-form-urlencoded")
                .data("src", "pcw_360index")
                .data("from","pcw_360index")
                .data("charset","UTF-8")
                .data("requestScema","https")
                .data("o","sso")
                .data("m","login")
                .data("lm","0")
                .data("captFlag","1")
                .data("rtype","data")
                .data("validatelm","0")
                .data("isKeepAlive","1")
                .data("captchaApp","i360")
                .data("userName","2471264637@qq.com")
                .data("type","normal")
                .data("account","2471264637@qq.com")
                .data("password","0d2c260ea50b5190eb8f243c31c856b9")
                .data("captcha","")
                .data("token","6b0e6dce0c50bfab")
                .data("proxy","http://trends.so.com/psp_jump.html")
                .data("callback","QiUserJsonp308500917")
                .data("func", "QiUserJsonp308500917")
                .method(Connection.Method.POST)
                .execute();
        System.out.println(res.headers());

    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }
    public static void jietu(WebDriver driver,String screenPath) throws IOException {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE); // 关键代码，执行屏幕截图，默认会把截图保存到temp目录
        FileUtils.copyFile(scrFile, new File(screenPath));
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
