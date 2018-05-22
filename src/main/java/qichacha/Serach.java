package qichacha;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Serach {
    private static RedisClu rd=new RedisClu();
    private static Ca c=new Ca();
    public static void main(String args[]) throws IOException, InterruptedException {
        Document doc = spider.getSerach("http://www.qichacha.com/search?key="+ URLEncoder.encode("小米","utf-8"));
        System.out.println(doc.outerHtml());
    }

    public static void source(){

    }

    public static void parse() throws IOException, InterruptedException {
        while (true) {
            String cname=c.qu();
            if(!Dup.nullor(cname)){
                break;
            }
            Document doc = spider.getSerach("http://www.qichacha.com/search?key="+ URLEncoder.encode("小米","utf-8"));
            Elements elements = JsoupUtils.getElements(doc, "section.panel.b-a table.m_srchList tbody tr");
            boolean bo=true;
            String sn = null;
            int a=0;
            for (Element e : elements) {
                String sname=JsoupUtils.getString(e,"td:nth-child(2) a[onclick=zhugeTrack('企业搜索-列表-公司名称')]",0);
                String url=JsoupUtils.getHref(e,"td:nth-child(2) a[onclick=zhugeTrack('企业搜索-列表-公司名称')]","href",0);

                if(a==0){
                    sn=url;
                }

                if(cname.equals(sname)){
                    bo=false;
                    rd.set("qichacha",url);
                }
                a++;
            }
            if(bo&&Dup.nullor(sn)){
                rd.set("qichacha",sn);
            }
        }
    }

    static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
