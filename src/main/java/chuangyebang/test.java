package chuangyebang;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * Created by Administrator on 2017/4/19.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "H6I878E110016EVD";
    final static String ProxyPass = "E7D36F817D52CF35";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;

    public test(){
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        this.proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }
    public static void main(String args[]) throws IOException {
        for(int a=0;a<=300;a++) {
            Document doc = Jsoup.connect("http://172.17.107.56:8081/java_web/PatentCount")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                    .data("comp_id", "10000071910987229150\n")
                    .timeout(10000)
                    .post();
            System.out.println(doc.outerHtml());
        }
    }


}
