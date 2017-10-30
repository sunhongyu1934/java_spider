package tianyancha.human;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/3.
 */
public class tyc_ren {
    public static void main(String args[]) throws IOException {
        get();
    }

    public static void get() throws IOException {
        Document doc= Jsoup.connect("https://www.tianyancha.com/humansearch/%E9%9B%B7%E5%86%9B?searchfrom=human")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        System.out.println(doc.outerHtml());
    }
}
