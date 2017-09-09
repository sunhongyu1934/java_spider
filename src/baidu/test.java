package baidu;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * Created by Administrator on 2017/8/14.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "HL8LK84J5D81DB7D";
    final static String ProxyPass = "92BB5D9A91C09C59";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, DocumentException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        /*BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer("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");
        System.out.println(new String(b,"gb2312"));*/
        Document doc = Jsoup.connect("https://www.tianyancha.com/company/23402373")
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .timeout(5000)
                                    .get();
        System.out.println(doc.select("div.wechat div.mb10.in-block div.in-block.vertical-top.itemRight div.mb5:nth-child(2) span.in-block.vertical-top").text().replace("企业简介：",""));

        //System.out.println(doc.outerHtml());


        /*ExecutorService pool= Executors.newCachedThreadPool();
        System.out.println("kaishi******************************************************************************");
        for(int x=1;x<=20;x++){

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detailget("https://www.tianyancha.com/search?key=%E5%B0%8F%E7%B1%B3&checkFrom=searchBox",proxy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/

    }

    public static void detailget(String url,Proxy proxy) throws IOException, InterruptedException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("HL8LK84J5D81DB7D", "92BB5D9A91C09C59"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        HttpGet get=new HttpGet(url);
        org.apache.http.client.CookieStore cookieStore = new BasicCookieStore();
        while (true) {
            Document doc = null;
            while (true) {
                try {
                    CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
                    CloseableHttpResponse response=httpclient.execute(get);
                    HttpEntity entity=response.getEntity();
                    doc= Jsoup.parse(EntityUtils.toString(entity));
                    /*doc = Jsoup.connect(url.replace("https","http"))
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                            .timeout(5000)
                            .header("Host", "www.tianyancha.com")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .proxy(proxy)
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .get();*/
                    if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("Unauthorized") && !doc.outerHtml().contains("访问禁止")&&!doc.outerHtml().contains("Redirecting")) {
                        break;
                    }
                } catch (Exception e) {
                    Thread.sleep(500);
                    System.out.println("time out detail");
                }
            }
            System.out.println(cookieStore.getCookies());
            System.out.println("aaaaaaaaaaaaaaa");
        }
    }
}
