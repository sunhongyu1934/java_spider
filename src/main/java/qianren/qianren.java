package qianren;

import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class qianren {
    public static void main(String args[]) throws IOException {
        serach();
    }

    public static void serach() throws IOException {
        Document doc=get("http://www.1000plan.org/wiki/index.php?category-view-23-2");
        Elements ele= JsoupUtils.getElements(doc,"div.w-680.l dl.col-dl");
        for(Element e:ele){
            String url=JsoupUtils.getHref(e,"dt.h2 a","href",0);
            System.out.println(url);
        }
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            doc=Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get();
            if(doc!=null&&doc.outerHtml().length()>44&&!doc.outerHtml().contains("abuyun")){
                break;
            }
        }
        return doc;
    }
}
