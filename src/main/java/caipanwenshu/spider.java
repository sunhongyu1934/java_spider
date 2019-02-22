package caipanwenshu;

import Utils.Dup;
import Utils.RedisClu;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class spider {
    private static Ca c=new Ca();
    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(4000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static void conip() throws InterruptedException {
        while (true){
            if(c.po.size()>=10) {
                c.qu();
            }
            Thread.sleep(1000);
        }
    }
    public Document serach(String page,String type) throws IOException, ScriptException, NoSuchMethodException {
        Map<String,String> map=getcookie();
        Map<String,String> map1=getvlk(map.get("vjkl5"));
        Map<String,String> map2=getnum(map1.get("guid"));
        Document doc= null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect("http://wenshu.court.gov.cn/List/ListContent")
                        .header("Accept", "*/*")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Connection", "keep-alive")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Host", "wenshu.court.gov.cn")
                        .header("Origin", "http://wenshu.court.gov.cn")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .data("Param", "全文检索:" + type)
                        .data("Index", page)
                        .data("Page", "20")
                        .data("Order", "法院层级")
                        .data("Direction", "asc")
                        .data("vl5x", map1.get("vl5x"))
                        .data("guid", map1.get("guid"))
                        .data("number", map2.get("number"))
                        .header("Cookie", "vjkl5=" + map.get("vjkl5")+";")
                        .ignoreContentType(true)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .post();
                if(doc!=null&&!doc.outerHtml().contains("abuyun")&&doc.outerHtml().length()>46&&!doc.outerHtml().contains("remind key")&&!doc.outerHtml().contains("too many request")){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
        return doc;
    }

    public static Document get(String did) throws IOException {
        Document doc;
        while (true){
            try {
                String ip=c.qu();
                doc = Jsoup.connect("http://wenshu.court.gov.cn/CreateContentJS/CreateContentJS.aspx?DocID="+did)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Accept", "text/javascript, application/javascript, */*")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "wenshu.court.gov.cn")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .timeout(3000)
                        .get();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("too many request")) {
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                System.out.println("detail error");
            }
        }
        return doc;

    }

    public  static Map<String,String> getnum(String guid) throws IOException {
        Document doc=null;
        while (true) {
            try {
                String ip=c.qu();
                doc=Jsoup.connect("http://wenshu.court.gov.cn/ValiCode/GetCode")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Host", "wenshu.court.gov.cn")
                        .header("Origin", "http://wenshu.court.gov.cn")
                        .header("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
                        .ignoreHttpErrors(true)
                        .data("guid", guid)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreContentType(true)
                        .timeout(5000)
                        .get();
                if(doc!=null&&!doc.outerHtml().contains("abuyun")&&doc.outerHtml().length()>46&&!doc.outerHtml().contains("null")&&!doc.outerHtml().contains("too many request")){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                System.out.println("getnum error");
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("number",doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim());
        return map;
    }

    public static Map<String,String> getcookie() throws IOException {
        Connection.Response response=null;
        while (true) {
            try {
                String ip=c.qu();
                response = Jsoup.connect("http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .ignoreContentType(true)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "wenshu.court.gov.cn")
                        .header("Referer", "http://wenshu.court.gov.cn/Index")
                        .ignoreHttpErrors(true)
                        .method(Connection.Method.GET)
                        .timeout(5000)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .execute();
                if(response!=null&&!response.body().contains("abuyun")&&response.cookies()!=null&&response.cookies().size()>0&&Dup.nullor(response.cookies().get("vjkl5"))&&response.body().length()>46&&!response.body().contains("too many request")){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                System.out.println("getcookie error");
            }
        }
        return response.cookies();
    }

    public static Map<String,String> getvlk(String cook) throws ScriptException, NoSuchMethodException, FileNotFoundException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript");
        Map<String,String> map=new HashMap<>();

        engine.eval(new java.io.FileReader("js/caipanwenshu/getguid.js"));
        if (engine instanceof Invocable) {
            Object result =null;
            while (true) {
                try {
                    Invocable invocable = (Invocable) engine;
                    result = invocable.invokeFunction("getuid");
                    if(result!=null&& Dup.nullor(result.toString())){
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            map.put("guid",result.toString());
        }

        engine.eval(new java.io.FileReader("js/caipanwenshu/getkey.js"));
        if (engine instanceof Invocable) {
            Object result=null;
            while (true) {
                try {
                    Invocable invocable = (Invocable) engine;
                    invocable.invokeFunction("myGetKey", cook);
                    result = invocable.invokeFunction("getKey");
                    if(result!=null&& Dup.nullor(result.toString())){
                        break;
                    }
                }catch (Exception e){
                    System.out.println(cook);
                }
            }
            map.put("vl5x",result.toString());
        }

        return map;
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
