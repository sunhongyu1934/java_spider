package test;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/4/6.
 */
public class ceshitian {
    final static private int thread=500;
    // 代理隧道验证信息
    final static String ProxyUser = "H4TL2M827AIJ963D";
    final static String ProxyPass = "81C9D64628A60CF9";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    public static void main(String args[]) throws InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        /*String ip="182.254.134.33:16816";
        System.setProperty(Count.chrome,Count.chromepath);
        DesiredCapabilities caps = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(ip).setFtpProxy(ip).setSslProxy(ip);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        WebDriver driver=new ChromeDriver(caps);
        driver.get("http://www.gsxt.gov.cn/index.html");
        Thread.sleep(100000);*/
        /*Document doc=Jsoup.connect("http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=spot&from=webmap&c=131&wd=%E4%BC%81%E4%B8%9A&wd2=&pn=0&nn=0&db=0&sug=0&addr=0&&da_src=pcmappg.poi.page&on_gel=1&src=7&gr=3&l=11&rn=50&tn=B_NORMAL_MAP&u_loc=12948056,4836866&ie=utf-8&b=(12833368,4788866;13081176,4875906)&t=1491630892893")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .header("Referer","http://map.baidu.com/")
                .header("Host","map.baidu.com")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        String str=doc.outerHtml();
        Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
        Matcher mat= pattern.matcher(str);


        while(mat.find()){
            StringBuffer string = new StringBuffer();
            int data = Integer.parseInt(mat.group(0).replace("\\u",""), 16);
            string.append((char) data);
            str=str.replace(mat.group(0),string.toString());
        }
        System.out.println(str);*/

        get();

    }


    public static String getip(int x) throws IOException {
        Document doc= Jsoup.connect("http://dps.kuaidaili.com/api/getdps/?orderid=959161787758597&num=50&ut=1&sep=1").get();
        String ips[]=doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim().split(" ");
        return ips[x];
    }


    public static void tian() throws InterruptedException, IOException {
        int x=0;
        String ip=getip(x);
        x++;
        System.setProperty(Count.phantomjs,Count.phantomjspath);
        DesiredCapabilities caps = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(ip).setFtpProxy(ip).setSslProxy(ip);
        caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        caps.setCapability(CapabilityType.PROXY, proxy);
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        //caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=http","--proxy=182.254.134.33:16816","--proxy-auth=1343490516:8m6g8rd0"});
        WebDriver driver=null;
        driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
        driver.get("https://www.baidu.com/");
        System.out.println(driver.getPageSource());
        int a=1;
        while (true){
            driver.quit();
            ip=getip(x);
            x++;
            proxy.setHttpProxy(ip).setFtpProxy(ip).setSslProxy(ip);
            caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
            caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
            caps.setCapability(CapabilityType.PROXY, proxy);
            driver=new PhantomJSDriver(caps);
            driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
            while (true){
                boolean b=true;
                try {
                    driver.get("http://www.tianyancha.com/company/2373229881");
                }catch (Exception e1){
                    b=false;
                    driver.quit();
                    ip=getip(x);
                    x++;
                    proxy.setHttpProxy(ip).setFtpProxy(ip).setSslProxy(ip);
                    caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
                    caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
                    caps.setCapability(CapabilityType.PROXY, proxy);
                    driver=new PhantomJSDriver(caps);
                    driver.manage().timeouts().pageLoadTimeout(8, TimeUnit.SECONDS);
                    System.out.println("get time out agein");
                }
                if(b) {
                    break;
                }
            }
            Thread.sleep(10000);
            if(driver.getPageSource().length()>60000){
                System.out.println(driver.getPageSource());
                break;

            }
            System.out.println(driver.getPageSource());
            System.out.println(driver.getPageSource().length());
            System.out.println(a);
            a++;
        }
    }


    public static void get() throws IOException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final java.net.Proxy proxy1 = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        Connection.Response doc= null;
        while(true) {
            doc=Jsoup.connect("http://yd.gsxt.gov.cn/QuerySummary")
                    //.header("Hose", "yd.gsxt.gov.cn")
                    //.header("Origin", "file://")
                    .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .data("pageSize", "10")
                    .data("userID", "id001")
                    .data("userIP", "192.123.123.13")
                    .data("keywords", "110107015374159")
                    .data("topic", "1")
                            //.data("nodenum","110000")
                    .data("mobileAction", "entSearch")
                    .data("pageNum", "1")
                    .proxy(proxy1)
                    .timeout(100000)
                    .method(Connection.Method.POST)
                            // .data("pripid","E4AACF8882E14EFC2F0428CB18A1E7D1594EF1C54A2F1309CC84DF564601C0D817932D83D33C5ED3AD5FC031969B48ABADD2112B972C78637859DAFF901E79256EE5FF8930DA71D407BE1E05A3106893")
                            //.data("enttype","6150")
                            //.data("nodenum","310000")
                            //.data("mobileAction","entDetail")
                            //.data("userID","id001")
                            //  .data("userIP", "192.123.123.13")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .execute();
            System.out.println(doc.body());
            if(StringUtils.isNotEmpty(doc.body())){
                break;
            }
        }


        String keys="id";
        String key= URLEncoder.encode(keys,"UTF-8");
        System.out.println(key);
        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H4TL2M827AIJ963D", "81C9D64628A60CF9"));



        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

       builder.setDefaultCredentialsProvider(credsProvider);
        CloseableHttpClient httpclient = builder.build();


        HttpPost post = new HttpPost("http://yd.gsxt.gov.cn/QuerySummary");
        List<NameValuePair> params = Lists.newArrayList();
        //params.add(new BasicNameValuePair("pripid", "8D5C50CB046C0E8423F9B0C4296E8BEE290CFB9375589F834A95729F6C003F276387AF9279E6086213CA9DA6DCF59C256A922C8057A046D582AEA5A7A290AE88"));
        //params.add(new BasicNameValuePair("enttype", "6150"));
        //params.add(new BasicNameValuePair("nodenum", "310000"));
        //params.add(new BasicNameValuePair("mobileAction", "entDetail"));
        params.add(new BasicNameValuePair("pageSize","10"));
        params.add(new BasicNameValuePair("userID", "id001"));
        params.add(new BasicNameValuePair("userIP", "192.123.123.13"));

        params.add(new BasicNameValuePair("keywords","小米"));
        params.add(new BasicNameValuePair("topic","1"));
        params.add(new BasicNameValuePair("mobileAction","entSearch"));
        params.add(new BasicNameValuePair("pageNum","1"));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        //post.addHeader("Host", "yd.gsxt.gov.cn");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0");
       // post.addHeader("Origin","file://");
       // post.addHeader("Proxy-Connection","keep-alive");
        //post.addHeader("Accept-Encoding","gzip,deflate");
        //post.addHeader("Accepy-Language","zh-cn");




        int a=1;
        while (true){
            try {
                CloseableHttpResponse response = httpclient.execute(post);
                HttpEntity resEntity = response.getEntity();
                String tag = EntityUtils.toString(resEntity);
                System.out.println(tag);
                if (StringUtils.isNotEmpty(tag)&&!tag.contains("abuyun")) {
                    System.out.println(tag);
                    break;
                }
                System.out.println(a);
                a++;
                System.out.println("-----------------------------------------------------------------");
            }catch (Exception e){
                System.out.println("**************");
            }
        }
    }


    public static void gs() throws IOException {
        HttpHost proxy = new HttpHost("182.254.134.33",16816,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("182.254.134.33",16816),new UsernamePasswordCredentials("1343490516", "8m6g8rd0"));



        HttpClientBuilder builder = HttpClients.custom();

        //builder.setRoutePlanner(routePlanner);

       // builder.setDefaultCredentialsProvider(credsProvider);
        CloseableHttpClient httpclient = builder.build();

        String json="{\"keyword\":\"百度\",\"start\":\"0\",\"province\":\"\",\"domain\":\"\",\"yearTo\":\"2017-04-10\",\"cityCode\":\"\",\"yearFrom\":\"\",\"capiTo\":\"\",\"token\":\"iLqmzLRr4lzKFm8fkOpiYLxBGjZU8m9jXWqURvxQbxgSfxOAw4OqhTJSiifT8DVWqQwn7t+ieKjK6EpXjM2TsLNsWToD\\/X+4giUDshT1HB0=\",\"capiFrom\":\"\",\"sortBy\":\"\",\"method\":\"\"}";

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");
        HttpPost post = new HttpPost("http://114.55.50.141/APPVestService/enterprise/advanceSearch2");
        post.setEntity(entity);
        post.addHeader("session-id","b631cb40-ecfe-42dc-966a-a2fb93c1f383");
        post.addHeader("Host","114.55.50.141");
        post.addHeader("push-registration","171976fa8ab3e94601f");
        post.addHeader("nick-name","");
        post.addHeader("User-Agent","pluto/1.2.0 (iPhone; iOS 10.2.1; Scale/2.00)");
        post.addHeader("device-id","51E02BF5-E8E8-4D76-ABF2-B74147DD2146");
        post.addHeader("platform","iOS");
        post.addHeader("token","8gYvEfhLK5IWR0He9rb3uRod8xnPRsKZRPB0bLNA4Kfrhd93JkPEFdSzps0iIS1knm2UrZGTAew6TAOIouLdr32Si3+u5zF2j4RAJGutPjeAaSMOAxXeAYQaKKAQlEZrvX+dISa9BDG9H1qa+A758xhk0FdWqoYHslyJLJbg3IOkOHSBXHs1u5+p35V6/6YUORMOrPBasVp+FNHfyplL/Q==");
        post.addHeader("package-name","com.bertadata.qyxxcx");
        post.addHeader("user-id","58e7093212aaa6b435794d45");
        post.addHeader("app-name","");
        post.addHeader("app-version","1.2.0");
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity resEntity = response.getEntity();
        String tag = EntityUtils.toString(resEntity);
        System.out.println(tag);
    }



    public static void jietu(WebDriver driver,String screenPath) throws IOException {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE); // 关键代码，执行屏幕截图，默认会把截图保存到temp目录
        FileUtils.copyFile(scrFile, new File(screenPath));
    }
}
