package simutong;

import com.google.common.collect.Lists;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "HOD68Z55TZC8319D";
    final static String ProxyPass = "A63620DAF4A33078";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException {

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        HttpHost proxy = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("HOD68Z55TZC8319D", "A63620DAF4A33078"));
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        CookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClients.custom().setDefaultCookieStore(cookieStore);

         builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CloseableHttpClient httpclient = builder.build();
        HttpPost get=new HttpPost("https://app.pedata.cn/PEDATA_APP_BACK/user/login?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone5S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3");
        get.addHeader("userAgent","AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)");
        get.addHeader("Content-Type","application/x-www-form-urlencoded");
        get.addHeader("Host","app.pedata.cn");
        get.addHeader("Accept-Language","zh-Hans-CN;q=1");

        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username","13717951934"));
        params.add(new BasicNameValuePair("password","3961shy"));
        get.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));


        CloseableHttpResponse response = httpclient.execute(get);
        List<Cookie> list = cookieStore.getCookies();// get all cookies


        HttpPost ge=new HttpPost("https://app.pedata.cn/PEDATA_APP_BACK/event/investList?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone6S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3");
        ge.addHeader("userAgent","AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)");
        ge.addHeader("Content-Type","application/x-www-form-urlencoded");
        ge.addHeader("Host","app.pedata.cn");
        ge.addHeader("Accept-Language","zh-Hans-CN;q=1");
        List<NameValuePair> params1 = Lists.newArrayList();
        params1.add(new BasicNameValuePair("limit","0"));
        params1.add(new BasicNameValuePair("start","199"));
        ge.setEntity(new UrlEncodedFormEntity(params1, Consts.UTF_8));

        Map<String,String> map=new HashMap<String, String>();
        for (Cookie cookie : list) {
            map.put(cookie.getName(),cookie.getValue());
        }
        ge.addHeader("Cookie",map.toString());
        CloseableHttpResponse response2 = httpclient.execute(ge);
        HttpEntity entity=response2.getEntity();
        String tag= EntityUtils.toString(entity);
        System.out.println(tag);


    }

    public static Map<String,String> denglu() throws IOException {
        Connection.Response doc=Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/user/login?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone5S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3")
                .userAgent("AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Host","app.pedata.cn")
                .header("Accept-Language","zh-Hans-CN;q=1")
                .data("username","13717951934")
                .data("password","3961shy")
                .method(Connection.Method.POST)
                .execute();
        Map<String,String> map=doc.cookies();
        return map;
    }

    public static void serach() throws IOException {
        Map<String,String> map=denglu();
        Document doc= Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/event/investList?platform=ios&app_name=smt_app&platversion=3.9.5&device_info=iPhone10.2.1&device_version=iPhone5S&ios_uid=5A605C13-1ECD-42B8-A67D-73B403258760&ios_idfa=28D39193-3C59-47AC-B294-CC3E9DEA64E3")
                .userAgent("AppDelegate/406 (iPhone; iOS 10.2.1; Scale/2.00)")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Host","app.pedata.cn")
                .header("Accept-Language","zh-Hans-CN;q=1")
                .cookies(map)
                .data("limit", "10")
                .data("start","0")
                .post();
        String json=doc.outerHtml().replace("<html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").replace("</html>","").replace("\n","");
        System.out.println(doc.outerHtml());
    }

    public static void detail() throws IOException {
        Document doc=Jsoup.connect("https://app.pedata.cn/PEDATA_APP_BACK/event/eventDetail?eventId=231828519&typeName=invest&callback=Ext.data.JsonP.callback2&_dc=1496643103855")
                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27")
                .header("Host", "app.pedata.cn")
                .header("Accept-Language","zh-cn")
                .header("Cookie","JSESSIONID=4A2539ABD13B030679038ABC44E302E0; quickLogonKey=13717951934$84403366F5345359C05932B42A8448B1; APP3_0Client=smtApp")
                .get();
        System.out.println(doc.outerHtml());
    }

}
