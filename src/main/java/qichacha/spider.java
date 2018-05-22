package qichacha;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class spider {
    // 代理隧道验证信息
    final static String ProxyUser = "HE5I6A6073H102ID";
    final static String ProxyPass = "48512F15BA217F88";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;

    static{
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static void main(String args[]) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique=9cce0780ab7644008b73bc2120479d31&companyname=%E5%B0%8F%E7%B1%B3%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8&tab=susong");
        System.out.println(doc.outerHtml());
    }


    public static Document getSerach(String url) throws IOException {
        Map<String,String> map=getCookie();
        Document doc=null;
        System.out.println(map);
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "www.qichacha.com")
                        .header("Referer", "http://www.qichacha.com/")
                        .cookies(map)
                        .proxy(proxy)
                        .timeout(3000)
                        .get();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("window.location.href='http://www.qichacha.com/index_verify?type=companysearch&back=/search?key=%E5%B0%8F%E7%B1%B3';")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }

    public static Map<String,String> getCookie() throws IOException {
        Connection.Response response=null;
        while (true){
            try {
                response = Jsoup.connect("http://www.qichacha.com/firm_9cce0780ab7644008b73bc2120479d31.html")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Cache-Control", "max-age=0")
                        .header("Host", "www.qichacha.com")
                        .header("Referer", "http://www.qichacha.com/search?key=%E5%B0%8F%E7%B1%B3")
                        .timeout(3000)
                        .proxy(proxy)
                        .method(Connection.Method.GET)
                        .execute();
                if (response != null && response.body().length() > 50 && !response.body().contains("abuyun")&&!response.body().contains("window.location.href='http://www.qichacha.com/index_verify?type=companysearch&back=/search?key=%E5%B0%8F%E7%B1%B3';")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }

        return response.cookies();
    }

    public static Document getDetail(String url){
        Document doc=null;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "www.qichacha.com")
                        .header("Referer", "http://www.qichacha.com/")
                        .header("Cookie","PHPSESSID=dgdkq4b34v38pipaqo4r46krl6; zg_did=%7B%22did%22%3A%20%22162f12b7ead24f-04b636da0b6fa7-39614101-144000-162f12b7eae7d3%22%7D; UM_distinctid=162f12b7ed04e6-0896c65f9a2a75-39614101-144000-162f12b7ed140e; _uab_collina=152446458250048149440368; acw_tc=AQAAAIxKy0O2PQ0AyhD53MMYVPHIv5gL; _umdata=C234BF9D3AFA6FE7799BBE64CAF32B14DC5A5A208A738597C4549994A2A338417E533EF7B15B49ADCD43AD3E795C914C8B3B6C6D1BC8180C431178F45C8DBA54; hasShow=1; Hm_lvt_3456bee468c83cc63fb5147f119f1075=1524651719,1524651812,1524709762,1524712093; CNZZDATA1254842228=711411862-1524459719-null%7C1524715688; zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f=%7B%22sid%22%3A%201524720528084%2C%22updated%22%3A%201524720529093%2C%22info%22%3A%201524464582322%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22www.qichacha.com%22%2C%22cuid%22%3A%20%22f1a6d19ae8a7d8e734075696aef7442d%22%7D; Hm_lpvt_3456bee468c83cc63fb5147f119f1075=1524720529")
                       // .proxy(proxy)
                        .timeout(3000)
                        .get();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("window.location.href='http://www.qichacha.com/index_verify?type=companysearch&back=/search?key=%E5%B0%8F%E7%B1%B3';")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }
}
