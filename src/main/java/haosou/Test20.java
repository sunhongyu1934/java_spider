package haosou;

import org.jsoup.nodes.Document;

import java.io.IOException;

import static tianyancha.XinxiXin.XinxiXin.detailget;

public class Test20 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Document doc2 = detailget("http://www.tianyancha.com/company/3075039693" );
        System.out.println(doc2.outerHtml());
    }
}