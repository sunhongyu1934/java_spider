package baidu;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tianyancha.XinxiXin.tongji;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;
import static tianyancha.XinxiXin.XinxiXin.suan;

/**
 * Created by Administrator on 2017/8/14.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "HL8LK84J5D81DB7D";
    final static String ProxyPass = "92BB5D9A91C09C59";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, DocumentException, InterruptedException {
        Document doc2 = Jsoup.connect("https://www.tianyancha.com/company/9519792")
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .timeout(5000)
                                    .get();

        System.out.println(doc2.outerHtml());

        Elements bei=getElements(doc2,"div.wechat div.mb10.in-block");

        if(bei!=null){
            for(Element e:bei){
                String logo=getHref(e,"div.in-block.vertical-top.wechatImg img","src",0);
                String ming=getString(e,"div.in-block.vertical-top.itemRight div.mb5",0);
                String hao=getString(e,"div.in-block.vertical-top.itemRight div.mb5:nth-child(2) span.in-block.vertical-top",1);
                String erweima=getHref(e,"div.in-block.vertical-top.itemRight div.mb5:nth-child(2) div.position-abs.erweimaBox img","src",0);
                String jieshao=getString(e,"span.overflow-width.in-block.vertical-top",0);

                System.out.println(logo);
                System.out.println(ming);
                System.out.println(hao);
                System.out.println(erweima);
                System.out.println(jieshao);
            }
        }
        /*ExecutorService pool= Executors.newCachedThreadPool();
        System.out.println("kaishi******************************************************************************");
        for(int x=1;x<=20;x++){

            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detailget("https://www.tianyancha.com/search?key=%E5%B0%8F%E7%B1%B3&checkFrom=searchBox",proxy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }

    public static void detailget() throws IOException, InterruptedException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();
        builder.setDefaultRequestConfig(requestConfig);
        Map<String,Object> map= jisuan("23402373");
        HttpGet get=new HttpGet("https://www.tianyancha.com/pagination/wechat.xhtml?ps=10&pn=" + 2 + "&id=" + "23402373" + "&_=" + map.get("time"));
        //CookieStore cookieStore= (CookieStore) map.get("cookie");
        get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        get.addHeader("Host","www.tianyancha.com");
        get.addHeader("X-Requested-With","XMLHttpRequest");
        get.addHeader("Accept","*/*");
        get.addHeader("Accept-Encoding","gzip, deflate, br");
        get.addHeader("Accept-Language","zh-CN,zh;q=0.8");

        Map<String,String> mco=new HashMap<String, String>();
        /*for(Cookie c:cookieStore.getCookies()){
            mco.put(c.getName(),c.getValue());
        }*/

        get.addHeader("Referer","https://www.tianyancha.com/company/23402373");
        Map<String,String> mm= (Map<String, String>) map.get("cookie");
        BasicCookieStore cookieStore=new BasicCookieStore();
        for(Map.Entry<String,String> entry:mm.entrySet()){
            if(!entry.getKey().equals("TYCID")&&!entry.getKey().equals("uccid")) {
                BasicClientCookie bb = new BasicClientCookie(entry.getKey(), entry.getValue());
                bb.setVersion(0);
                bb.setPath("/");
                bb.setDomain("www.tianyancha.com");
                cookieStore.addCookie(bb);
            }
        }
        System.out.println(cookieStore);
        while (true) {
            Document doc = null;
            Document doc2=null;
            while (true) {
                try {
                    CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
                    CloseableHttpResponse response=httpclient.execute(get);
                    HttpEntity entity=response.getEntity();
                    doc= Jsoup.parse(EntityUtils.toString(entity));
                    /*doc2 = Jsoup.connect("https://www.tianyancha.com/pagination/wechat.xhtml?ps=10&pn=" + 2 + "&id=" + "23402373" + "&_=" + map.get("time"))
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                            .timeout(5000)
                            .header("Host", "www.tianyancha.com")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .ignoreHttpErrors(true)
                            .cookies((Map<String, String>) map.get("cookie"))
                            .ignoreContentType(true)
                            .get();*/
                    break;
                    /*if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("Unauthorized") && !doc.outerHtml().contains("访问禁止")&&!doc.outerHtml().contains("Redirecting")) {
                        break;
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(500);
                    System.out.println("time out detail");
                }
            }
            System.out.println(doc);
            //System.out.println(doc2);
            break;
        }
    }

    public static  Map<String,Object>  jisuan(String tid) throws IOException {
        String pt=tid.substring(0,1);
        int  fl;
        if(String.valueOf(pt.codePointAt(0)).length()>1){
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)).substring(1,2));
        }else{
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)));
        }
        List<String> list=suan(fl);
        long ti=0;
        Gson gson = new Gson();
        tongji s = null;
        String html = null;
        org.apache.http.client.CookieStore cookieStore = new BasicCookieStore();
        Connection.Response doc2;
        while (true) {
            try {
                ti=System.currentTimeMillis();
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                        .setSocketTimeout(50000).build();

                HttpClientBuilder builder = HttpClients.custom();

                builder.setDefaultRequestConfig(requestConfig);
                HttpGet get=new HttpGet("http://www.tianyancha.com/tongji/" + URLEncoder.encode(tid, "utf-8") + ".json?_=" +ti );
                //HttpGet get=new HttpGet("https://www.tianyancha.com/");
                get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
                CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
                CloseableHttpResponse response=httpclient.execute(get);
                HttpEntity entity=response.getEntity();
                String doc= EntityUtils.toString(entity);

                doc2 = Jsoup.connect("http://www.tianyancha.com/tongji/" + URLEncoder.encode(tid, "utf-8") + ".json?_=" +ti )
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                html = doc2.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                s = gson.fromJson(html, tongji.class);
                break;
                /*if (doc != null && !doc.contains("http://www.qq.com/404/search_children.js")&& StringUtils.isNotEmpty(doc.replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.contains("abuyun")&&doc.length()>50&&!doc.contains("访问禁止")&&!doc.contains("访问拒绝")) {
                    html = doc2.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                    s = gson.fromJson(html, tongji.class);
                    break;
                }*/
            }catch (Exception e){
                System.out.println(html);
            }
        }
        System.out.println(doc2.cookies());
        System.out.println(cookieStore);
        Map<String,String> map=doc2.cookies();
        String[] chars = s.data.split(",");
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            str.append((char) Integer.parseInt(chars[i]));
        }
        String a = str.toString();
        String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");
        String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
        String[] codes = code.split(",");
        /*String shuzu="\"6\", \"b\", \"t\", \"f\", \"2\", \"z\", \"l\", \"5\", \"w\", \"h\", \"q\", \"i\", \"s\", \"e\", \"c\", \"p\", \"m\", \"u\", \"9\", \"8\", \"y\",\n" +
                "\"k\", \"j\", \"r\", \"x\", \"n\", \"-\", \"0\", \"3\", \"4\", \"d\", \"1\", \"a\", \"o\", \"7\", \"v\", \"g\"],\n" +
                "[\"1\", \"8\", \"o\", \"s\", \"z\", \"u\", \"n\", \"v\", \"m\", \"b\", \"9\", \"f\", \"d\", \"7\", \"h\", \"c\", \"p\", \"y\", \"2\", \"0\", \"3\",\n" +
                "\"j\", \"-\", \"i\", \"l\", \"k\", \"t\", \"q\", \"4\", \"6\", \"r\", \"a\", \"w\", \"5\", \"e\", \"x\", \"g\"],\n" +
                "[\"s\", \"6\", \"h\", \"0\", \"p\", \"g\", \"3\", \"n\", \"m\", \"y\", \"l\", \"d\", \"x\", \"e\", \"a\", \"k\", \"z\", \"u\", \"f\", \"4\", \"r\",\n" +
                "\"b\", \"-\", \"7\", \"o\", \"c\", \"i\", \"8\", \"v\", \"2\", \"1\", \"9\", \"q\", \"w\", \"t\", \"j\", \"5\"],\n" +
                "            [\"x\", \"7\", \"0\", \"d\", \"i\", \"g\", \"a\", \"c\", \"t\", \"h\", \"u\", \"p\", \"f\", \"6\", \"v\", \"e\", \"q\", \"4\", \"b\", \"5\", \"k\",\n" +
                "             \"w\", \"9\", \"s\", \"-\", \"j\", \"l\", \"y\", \"3\", \"o\", \"n\", \"z\", \"m\", \"2\", \"1\", \"r\", \"8\"],\n" +
                "            [\"z\", \"j\", \"3\", \"l\", \"1\", \"u\", \"s\", \"4\", \"5\", \"g\", \"c\", \"h\", \"7\", \"o\", \"t\", \"2\", \"k\", \"a\", \"-\", \"e\", \"x\",\n" +
                "             \"y\", \"b\", \"n\", \"8\", \"i\", \"6\", \"q\", \"p\", \"0\", \"d\", \"r\", \"v\", \"m\", \"w\", \"f\", \"9\"],\n" +
                "            [\"j\", \"h\", \"p\", \"x\", \"3\", \"d\", \"6\", \"5\", \"8\", \"k\", \"t\", \"l\", \"z\", \"b\", \"4\", \"n\", \"r\", \"v\", \"y\", \"m\", \"g\",\n" +
                "             \"a\", \"0\", \"1\", \"c\", \"9\", \"-\", \"2\", \"7\", \"q\", \"e\", \"w\", \"u\", \"s\", \"f\", \"o\", \"i\"],\n" +
                "            [\"8\", \"q\", \"-\", \"u\", \"d\", \"k\", \"7\", \"t\", \"z\", \"4\", \"x\", \"f\", \"v\", \"w\", \"p\", \"2\", \"e\", \"9\", \"o\", \"m\", \"5\",\n" +
                "             \"g\", \"1\", \"j\", \"i\", \"n\", \"6\", \"3\", \"r\", \"l\", \"b\", \"h\", \"y\", \"c\", \"a\", \"s\", \"0\"],\n" +
                "            [\"d\", \"4\", \"9\", \"m\", \"o\", \"i\", \"5\", \"k\", \"q\", \"n\", \"c\", \"s\", \"6\", \"b\", \"j\", \"y\", \"x\", \"l\", \"a\", \"v\", \"3\",\n" +
                "             \"t\", \"u\", \"h\", \"-\", \"r\", \"z\", \"2\", \"0\", \"7\", \"g\", \"p\", \"8\", \"f\", \"1\", \"w\", \"e\"],\n" +
                "            [\"7\", \"-\", \"g\", \"x\", \"6\", \"5\", \"n\", \"u\", \"q\", \"z\", \"w\", \"t\", \"m\", \"0\", \"h\", \"o\", \"y\", \"p\", \"i\", \"f\", \"k\",\n" +
                "             \"s\", \"9\", \"l\", \"r\", \"1\", \"2\", \"v\", \"4\", \"e\", \"8\", \"c\", \"b\", \"a\", \"d\", \"j\", \"3\"],\n" +
                "            [\"1\", \"t\", \"8\", \"z\", \"o\", \"f\", \"l\", \"5\", \"2\", \"y\", \"q\", \"9\", \"p\", \"g\", \"r\", \"x\", \"e\", \"s\", \"d\", \"4\", \"n\",\n" +
                "             \"b\", \"u\", \"a\", \"m\", \"c\", \"h\", \"j\", \"3\", \"v\", \"i\", \"0\", \"-\", \"w\", \"7\", \"k\", \"6\"]]";*/
        String[] shu2 = list.toString().replace("[","").replace("]","").replace(" ","").split(",");
        StringBuffer st = new StringBuffer();
        //String[] shu2 = shu[fl].replace("]", "").replace(",[", "").replace("[", "").split(",");
        for (int bb = 0; bb < codes.length; bb++) {
            st.append(shu2[Integer.parseInt(codes[bb])]);
        }
        String utm=st.toString();
        map.put("_utm",utm);
        map.put("token",token);
        map.put("bannerFlag","true");

        BasicClientCookie cok=new BasicClientCookie("_utm",utm);
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        BasicClientCookie cok2=new BasicClientCookie("token",token);
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        BasicClientCookie cok3=new BasicClientCookie("bannerFlag","true");
        cok.setVersion(0);
        cok.setPath("/");
        cok.setDomain("www.tianyancha.com");

        cookieStore.addCookie(cok);
        cookieStore.addCookie(cok2);
        cookieStore.addCookie(cok3);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("cookie",map);
        maps.put("time",ti-1);
        return maps;
    }
}
