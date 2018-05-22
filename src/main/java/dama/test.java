package dama;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String args[]) throws IOException, ScriptException, NoSuchMethodException {
        Map<String,String> map=getcookie();
        System.out.println(map);
        Map<String,String> map1=getvlk(map.get("vjkl5"));
        System.out.println(map1);
        Map<String,String> map2=getnum(map1.get("guid"));
        System.out.println(map2);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        String time=simpleDateFormat.format(date);
        Document doc= Jsoup.connect("http://wenshu.court.gov.cn/List/ListContent")
                .header("Accept","*/*")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Connection","keep-alive")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .header("Host","wenshu.court.gov.cn")
                .header("Origin","http://wenshu.court.gov.cn")
                .header("Referer","http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
                .header("X-Requested-With","XMLHttpRequest")
                .data("Param","案件类型:执行案件")
                .data("Index","1")
                .data("Page","5")
                .data("Order","法院层级")
                .data("Direction","asc")
                .data("vl5x",map1.get("vl5x"))
                .data("guid",map1.get("guid"))
                .data("number",map2.get("number"))
                .header("Cookie","vjkl5="+map.get("vjkl5"))
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .post();
        System.out.println(doc.outerHtml());
    }

    public  static Map<String,String> getnum(String guid) throws IOException {
        Document doc=Jsoup.connect("http://wenshu.court.gov.cn/ValiCode/GetCode")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("Accept","*/*")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .header("Host","wenshu.court.gov.cn")
                .header("Origin","http://wenshu.court.gov.cn")
                .header("Referer","http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
                .ignoreHttpErrors(true)
                .data("guid",guid)
                .ignoreContentType(true)
                .get();
        Map<String,String> map=new HashMap<>();
        map.put("number",doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim());
        return map;
    }

    public static Map<String,String> getcookie() throws IOException {
        Connection.Response response=Jsoup.connect("http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+5+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E6%89%A7%E8%A1%8C%E6%A1%88%E4%BB%B6")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .ignoreContentType(true)
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Host","wenshu.court.gov.cn")
                .header("Referer","http://wenshu.court.gov.cn/Index")
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .execute();
        return response.cookies();
    }

    public static Map<String,String> getvlk(String cook) throws ScriptException, NoSuchMethodException, FileNotFoundException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript");
        Map<String,String> map=new HashMap<>();
        engine.eval(new java.io.FileReader("/D:/工作/代码/java_spider/src/main/resources/caipanwenshu/getkey.js"));
        if (engine instanceof Invocable) {
            Invocable invocable = (Invocable) engine;
            invocable.invokeFunction("myGetKey",cook);
            Object result = invocable.invokeFunction("getKey");
            map.put("vl5x",result.toString());
        }

        engine.eval(new java.io.FileReader("/D:/工作/代码/java_spider/src/main/resources/caipanwenshu/getguid.js"));
        if (engine instanceof Invocable) {
            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction("getuid");
            map.put("guid",result.toString());
        }
        return map;
    }
}
