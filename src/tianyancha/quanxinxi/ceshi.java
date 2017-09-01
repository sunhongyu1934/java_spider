package tianyancha.quanxinxi;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/14.
 */
public class ceshi {
    // 代理隧道验证信息
    final static String ProxyUser = "H4XGPM790E93518D";
    final static String ProxyPass = "2835A47D56143D62";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException {
        Document doc=Jsoup.connect("http://www.tianyancha.com/company/23402373")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .timeout(10000)
                .get();
        System.out.println(doc.outerHtml());
    }

    public static class suan{
        public Da data;
        public static class Da{
            public String v;
        }
    }


    public static void get() throws IOException {
            System.out.println(System.currentTimeMillis());
            Connection.Response doc = Jsoup.connect("http://www.tianyancha.com/tongji/150041670.json?random=" + System.currentTimeMillis())
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(5000)
                    .method(Connection.Method.GET)
                    .execute();
            Map<String,String> map=doc.cookies();
            String html = doc.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
            Gson gson = new Gson();
            suan s = gson.fromJson(html, suan.class);
            String[] chars = s.data.v.split(",");
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                str.append((char) Integer.parseInt(chars[i]));
            }
            String a = str.toString();
            //System.out.println(a);
        System.out.println(a);
            String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");
        System.out.println(token);
            String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
            System.out.println(code);
            String[] codes = code.split(",");
            StringBuffer strr=new StringBuffer();
            /*for(int i=0;i<codes.length;i++){
                String ap=codes[i];
                if(Integer.parseInt(ap) == 1){
                    strr.append("7");
                }else if(Integer.parseInt(ap) == 2){
                    strr.append("0");
                }else if(Integer.parseInt(ap) == 3){
                    strr.append("d");
                }else if(Integer.parseInt(ap) == 13){
                    strr.append("6");
                }else if(Integer.parseInt(ap) == 36){
                    strr.append("8");
                }else if(Integer.parseInt(ap) == 15){
                    strr.append("e");
                }else if(Integer.parseInt(ap) == 12){
                    strr.append("f");
                }else if(Integer.parseInt(ap) == 18){
                    strr.append("b");
                }else if(Integer.parseInt(ap) == 17){
                    strr.append("4");
                }else if(Integer.parseInt(ap) == 6){
                    strr.append("a");
                }else if(Integer.parseInt(ap) == 22){
                    strr.append("9");
                }else if(Integer.parseInt(ap) == 28){
                    strr.append("3");
                }else if(Integer.parseInt(ap) == 19){
                    strr.append("5");
                }else if(Integer.parseInt(ap) == 34){
                    strr.append("1");
                }else if(Integer.parseInt(ap) == 6){
                    strr.append("a");
                }else if(Integer.parseInt(ap) == 33){
                    strr.append("2");
                }else if(Integer.parseInt(ap) == 13){
                    strr.append("6");
                }else if(Integer.parseInt(ap) == 7){
                    strr.append("c");
                }else{
                    strr.append("*");
                }
            }
        String ut=strr.toString();
        System.out.println(ut);
        sou(token,ut);*/



            String shuzu = "[[6,b,t,f,l\",\"5\",\"w\",\"h\",\"q\",\"i\",\"s\",\"e\",\"c\",\"p\",\"m\",\"u\",\"9\",\"8\",\"y\",\"2\",\"z\",\"k\",\"j\",\"r\",\"x\",\"n\",\"-\",\"0\",\"3\",\"4\",\"d\",\"1\",\"a\",\"o\",\"7\",\"v\",\"g\"],[\"1\",\"8\",\"o\",\"s\",\"z\",\"m\",\"b\",\"9\",\"f\",\"d\",\"7\",\"h\",\"c\",\"u\",\"n\",\"v\",\"p\",\"y\",\"2\",\"0\",\"3\",\"j\",\"-\",\"i\",\"l\",\"k\",\"t\",\"q\",\"4\",\"6\",\"r\",\"a\",\"w\",\"5\",\"e\",\"x\",\"g\"],[\"g\",\"a\",\"c\",\"t\",\"h\",\"u\",\"p\",\"f\",\"6\",\"x\",\"7\",\"0\",\"d\",\"i\",\"v\",\"e\",\"q\",\"4\",\"b\",\"5\",\"k\",\"w\",\"9\",\"s\",\"-\",\"j\",\"l\",\"y\",\"3\",\"o\",\"n\",\"z\",\"m\",\"2\",\"1\",\"r\",\"8\"],[\"s\",\"6\",\"h\",\"0\",\"y\",\"l\",\"d\",\"x\",\"e\",\"a\",\"k\",\"z\",\"u\",\"f\",\"4\",\"r\",\"b\",\"-\",\"p\",\"g\",\"3\",\"n\",\"m\",\"7\",\"o\",\"c\",\"i\",\"8\",\"v\",\"2\",\"1\",\"9\",\"q\",\"w\",\"t\",\"j\",\"5\"],[\"d\",\"4\",\"9\",\"m\",\"o\",\"i\",\"5\",\"k\",\"q\",\"n\",\"c\",\"s\",\"6\",\"b\",\"j\",\"y\",\"x\",\"l\",\"a\",\"v\",\"3\",\"t\",\"u\",\"h\",\"-\",\"r\",\"z\",\"2\",\"0\",\"7\",\"g\",\"p\",\"8\",\"f\",\"1\",\"w\",\"e\"],[\"z\",\"5\",\"g\",\"c\",\"h\",\"7\",\"o\",\"t\",\"2\",\"k\",\"a\",\"-\",\"e\",\"x\",\"y\",\"j\",\"3\",\"l\",\"1\",\"u\",\"s\",\"4\",\"b\",\"n\",\"8\",\"i\",\"6\",\"q\",\"p\",\"0\",\"d\",\"r\",\"v\",\"m\",\"w\",\"f\",\"9\"],[\"p\",\"x\",\"3\",\"d\",\"6\",\"5\",\"8\",\"k\",\"t\",\"l\",\"z\",\"b\",\"4\",\"n\",\"r\",\"v\",\"y\",\"m\",\"g\",\"a\",\"0\",\"1\",\"c\",\"9\",\"-\",\"2\",\"7\",\"q\",\"j\",\"h\",\"e\",\"w\",\"u\",\"s\",\"f\",\"o\",\"i\"],[\"q\",\"-\",\"u\",\"d\",\"k\",\"7\",\"t\",\"z\",\"4\",\"8\",\"x\",\"f\",\"v\",\"w\",\"p\",\"2\",\"e\",\"9\",\"o\",\"m\",\"5\",\"g\",\"1\",\"j\",\"i\",\"n\",\"6\",\"3\",\"r\",\"l\",\"b\",\"h\",\"y\",\"c\",\"a\",\"s\",\"0\"],[\"7\",\"-\",\"g\",\"x\",\"6\",\"5\",\"n\",\"u\",\"q\",\"z\",\"w\",\"t\",\"m\",\"0\",\"h\",\"o\",\"y\",\"p\",\"i\",\"f\",\"k\",\"s\",\"9\",\"l\",\"r\",\"1\",\"2\",\"v\",\"4\",\"e\",\"8\",\"c\",\"b\",\"a\",\"d\",\"j\",\"3\"],[\"1\",\"t\",\"8\",\"z\",\"o\",\"f\",\"l\",\"5\",\"2\",\"y\",\"q\",\"9\",\"p\",\"g\",\"r\",\"x\",\"e\",\"s\",\"d\",\"4\",\"n\",\"b\",\"u\",\"a\",\"m\",\"c\",\"h\",\"j\",\"3\",\"v\",\"i\",\"0\",\"-\",\"w\",\"7\",\"k\",\"6\"]]";
            String[] shu = shuzu.replace("\"", "").split("]");
            for (int aa = 0; aa < shu.length; aa++) {
                StringBuffer st = new StringBuffer();
                String[] shu2 = shu[aa].replace("]", "").replace(",[", "").replace("[", "").split(",");
                System.out.println(shu[aa].replace("]", "").replace(",[", "").replace("[", ""));
                for (int bb = 0; bb < codes.length; bb++) {
                    st.append(shu2[Integer.parseInt(codes[bb])]);
                }
                System.out.println(token + "      " + st.toString());
                if(aa==9) {
                    sou(token, st.toString(),map);
                }
            }





    }

    public static void sou(String token,String utm,Map<String,String> maps) throws IOException {
        maps.put("token",token);
        maps.put("_utm",utm);
        System.out.println(maps);

        Connection.Response docc=Jsoup.connect("http://www.tianyancha.com/company/150041670.json")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .header("Tyc-From","normal")
                .header("Accept","application/json, text/plain, ")
                .header("CheckError","check")
                .header("Referer","http://www.tianyancha.com/company/150041670")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .cookies(maps)
                .timeout(100000)
                .method(Connection.Method.GET)
                .execute();
        System.out.println(docc.cookies());
        System.out.println(docc.body());

        CookieStore cookieStore = new BasicCookieStore();

        CloseableHttpClient httpClient= HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        BasicClientCookie cookie2 = new BasicClientCookie("token", token);
        cookie2.setVersion(0);
        cookie2.setPath("/");
        BasicClientCookie cookie3 = new BasicClientCookie("_utm", utm);
        cookie3.setVersion(0);
        cookie3.setPath("/");




        cookieStore.addCookie(cookie2);
        cookieStore.addCookie(cookie3);

        HttpGet get=new HttpGet("http://www.tianyancha.com/v2/company/150041670.json");
        get.addHeader("UserAgent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36");
        //get.addHeader("Cookie","tnet=36.110.41.42;token="+token+";_utm="+utm);
        get.addHeader("Tyc-From","normal");
        get.addHeader("Accept","application/json, text/plain, */*");
        get.addHeader("CheckError","check");
        get.addHeader("Referer","http://www.tianyancha.com/company/150041670");
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity resEntity = response.getEntity();
        String tag = EntityUtils.toString(resEntity);
        System.out.println(tag);

        List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }

    }

    static class Ipchi{
        BlockingQueue<String> blo=new LinkedBlockingQueue<String>(10000);
        public void fang(String key) throws InterruptedException {
            blo.put(key);
        }
        public String qu() throws InterruptedException {
            return blo.poll(60, TimeUnit.SECONDS);
        }
    }

}
