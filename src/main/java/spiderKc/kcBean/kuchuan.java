package spiderKc.kcBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Administrator on 2017/7/10.
 */
public class kuchuan {
    public static void main(String args[]) throws IOException {
        get();
    }
    public static void get() throws IOException {
        Document doc= Jsoup.connect("http://android.kuchuan.com/dailydownload?packagename=com.taobao.taobao&status=-1&date=1499680755106")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        System.out.println(doc.outerHtml());
    }
}
