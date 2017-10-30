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
        Document doc=Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=57095150_2_oem_dg&wd=ip&rsv_pq=f1aefae100040db2&rsv_t=daed6GQC7sE%2BVugjVOxR%2FdpUQuf0RsA3TR7jwgfg1cPyeHAUfP5JG4JT0kR2DK62FW6rIlUs2KI&rqlang=cn&rsv_enter=1&rsv_sug3=8&rsv_sug1=9&rsv_sug7=100&rsv_sug2=0&inputT=4150&rsv_sug4=4151")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .timeout(5000)
                .get();

    }


}
