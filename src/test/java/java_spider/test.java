package java_spider;


import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;
import static tianyancha.XinxiXin.XinxiXin.detailget;
import static tianyancha.XinxiXin.XinxiXin.jisuan;


public class test {
    private static java.sql.Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con!=null){
                    break;
                }
            }
        }
        conn=con;

    }

    public static org.jsoup.nodes.Document get(String cookie) throws InterruptedException {
        org.jsoup.nodes.Document doc=null;
        while (true) {
            try {
                /*Map<String,Object> map1=new JSONObject(cookie).toMap();
                Map<String,String> map=new HashMap<>();
                for(Map.Entry<String,Object> entry:map1.entrySet()){
                    map.put(entry.getKey(),entry.getValue().toString());
                }*/

                doc = Jsoup.connect("https://www.tianyancha.com/search?key=%E5%B0%8F%E7%B1%B3")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        //.proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Cookie",cookie)
                        .get();
                if (doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")&&!doc.outerHtml().contains("503 Service Temporarily Unavailable")&&!doc.outerHtml().contains("too many request")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                e.printStackTrace();
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static void main(String args[]) throws IOException, InterruptedException, SQLException, ScriptException, NoSuchMethodException {
        //Document doc2=Jsoup.parse(new File("C:\\Users\\13434\\Desktop\\c.txt"),"utf-8");
        RedisClu rd=new RedisClu();
        String ip=rd.getZsetByKey("ip","0","0").iterator().next();
        String cookie=rd.getrand("tyc_cookie");
        System.out.println(cookie);
        Map<String,Object> map1=new JSONObject(cookie.split("######")[1]).toMap();
        Map<String,String> map=new HashMap<>();
        for(Map.Entry<String,Object> entry:map1.entrySet()){
            map.put(entry.getKey(),entry.getValue().toString());
        }
        Document doc = Jsoup.connect("https://www.tianyancha.com/search?key="+ URLEncoder.encode("富仕通通信（深圳）有限公司","utf-8") +"&checkFrom=searchBox")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .timeout(5000)
                .header("Host", "www.tianyancha.com")
                .header("X-Requested-With", "XMLHttpRequest")
                .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .cookies(map)
                .get();
        System.out.println(doc.outerHtml());





    }
    public static JSONObject getValueArray(JSONArray jsonObject, int a){
        try{
            return jsonObject.getJSONObject(a);
        }catch (Exception e){
            return null;
        }
    }

    public static JSONObject getValueObject(JSONObject jsonObject, String key){
        try{
            return jsonObject.getJSONObject(key);
        }catch (Exception e){
            return null;
        }
    }


    public static String getValue(JSONObject jsonObject,String key){
        try{
            return jsonObject.get(key).toString();
        }catch (Exception e){
            return "";
        }
    }







    public static void tyclogin() throws IOException {
        CookieStore cookieStore=new BasicCookieStore();
        HttpClientBuilder builder= HttpClients.custom();
        CloseableHttpClient httpClient=builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.tianyancha.com/cd/login.json");
        post.setHeader("Accept","*/*");
        post.setHeader("Accept-Encoding","gzip, deflate, br");
        post.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        post.setHeader("Connection","keep-alive");
        post.setHeader("Content-Type","application/json; charset=UTF-8");
        post.setHeader("Host","www.tianyancha.com");
        post.setHeader("Origin","https://www.tianyancha.com");
        post.setHeader("Referer","https://www.tianyancha.com/login");
        Map<String, String> map=new HashMap<>();
        map.put("autoLogin","true");
        map.put("cdpassword","8014717b480a1a0cd44bca717631d976");
        map.put("loginway","PL");
        map.put("mobile","13717951934");
        StringEntity entity=new StringEntity(new JSONObject(map).toString(), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        CloseableHttpResponse response=httpClient.execute(post);
        String json=EntityUtils.toString(response.getEntity());
        JSONObject jsonObject=new JSONObject(json);
        Map<String,String> map1=new HashMap<>();
        map1.put("auth_token",jsonObject.getJSONObject("data").getString("token"));
        System.out.println(map1);
    }

    public static void tycget() throws IOException {
        Document doc = Jsoup.connect("https://www.tianyancha.com/pagination/changeinfo.xhtml?ps=10&pn=2&id=23402373&_=1523358957455")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .timeout(5000)
                .header("Host", "www.tianyancha.com")
                .header("X-Requested-With", "XMLHttpRequest")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .header("Cookie","auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzcxNzk1MTkzNCIsImlhdCI6MTUyMzM1OTAwNSwiZXhwIjoxNTM4OTExMDA1fQ.87LB3UQ6JZrWLtpj3hRYnh5I5MlF9hMUsmgTMtsAIwVYONiIOhrCQWHdIU5-mYo0LusmbIjeO4LvSjscvD0IUg")
                .get();
        System.out.println(doc.outerHtml());
    }

    public static void bailu() throws IOException {
        Document doc= Jsoup.connect("https://api.bailuzhiku.com/v1.0.3/News/list.php")
                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/4G Language/zh_CN")
                .header("Accept","*/*")
                .header("Accept-Encoding","br, gzip, deflate")
                .header("Accept-Language","zh-cn")
                .header("Connection","keep-alive")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Host","api.bailuzhiku.com")
                .header("Referer","https://servicewechat.com/wx660cf1b5fc65bc4e/9/page-frame.html")
                .data("Keyword","")
                .data("Page","466")
                .post();
        System.out.println(doc.outerHtml().toString().replace("\\/",""));

        Document document=Jsoup.connect("https://api.bailuzhiku.com/v1.0.3/news/detail.php?newsid=18030514593597600150731")
                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/4G Language/zh_CN")
                .header("Accept","*/*")
                .header("Accept-Encoding","br, gzip, deflate")
                .header("Accept-Language","zh-cn")
                .header("Connection","keep-alive")
                .header("Content-Type","json")
                .header("Host","api.bailuzhiku.com")
                .header("Referer","https://servicewechat.com/wx660cf1b5fc65bc4e/9/page-frame.html")
                .get();
        String json= document.text();
        System.out.println(document.outerHtml().replace("\\/",""));
        JSONObject jsonObject=new JSONObject(json);
        System.out.println(jsonObject.toString());
    }
}
