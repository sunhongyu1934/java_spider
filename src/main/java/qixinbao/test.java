package qixinbao;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/21.
 */
public class test {
    public static void main(String args[]) throws IOException {
        Document doc = null;
        System.out.println("begin request");
        while (true) {
            try {
                doc = Jsoup.connect("http://www.qixin.com/company/78227bbc-c8cd-4208-b927-590e2502a2d8")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(2000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml()) && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("完成上面的验证")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out reget");
            }
        }
        String value=doc.select("div.company-name span.company-name-now").text();
        String logo=doc.select("div.col-xs-3.company-left.bg-white div.company-card div.company-logo.preload").attr("pre-src");
        if(logo.equals("http://cache.qixin.com/images/default.png")){
            logo="";
        }
        String web=doc.select("div.company-info-item-text a[rel=nofollow]").attr("href");

        System.out.println(web);
        System.out.println(logo);
        System.out.println(value);
    }
}
