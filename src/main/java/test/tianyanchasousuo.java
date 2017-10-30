package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.*;

/**
 * Created by Administrator on 2017/4/5.
 */
public class tianyanchasousuo
{
    // 代理隧道验证信息
    final static String ProxyUser = "H7748N598W005E8D";
    final static String ProxyPass = "A242740AED77F7E4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    public static String getUrlProxyContent()
    {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        try
        {
            // 此处自己处理异常、其他参数等
            String sou="百度";
            String so= URLEncoder.encode(sou,"UTF-8");

            Document doc= Jsoup.connect("http://www.tianyancha.com/v2/search/%E5%B0%8F%E7%B1%B3.json?pn=")

                    //.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                   // .timeout(30000).proxy(proxy)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get();
            System.out.println(doc.outerHtml());

            if(doc != null) {
                System.out.println(doc.body().html());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws Exception
    {
        // 要访问的目标页面
        String targetUrl = "http://test.abuyun.com/proxy.php";

        getUrlProxyContent();
    }
}
