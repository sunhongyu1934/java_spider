package linshi_spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.util.JAXBSource;
import java.io.IOException;

public class address {
    public static void main(String args[]) throws IOException {
        Document doc= Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(5000)
                .get();

        Elements ele=doc.select("div.TRS_Editor div.TRS_PreAppend p");
        for(Element e:ele){
            System.out.println(e.text().split("     ",2)[1].replace("　","").replace("　　",""));
        }
    }
}
