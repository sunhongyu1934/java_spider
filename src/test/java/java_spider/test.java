package java_spider;


import Utils.Dup;
import Utils.JsoupUtils;
import Utils.MD5Util;
import Utils.RedisClu;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import shuiwu.spider;
import spiderKc.kcBean.Count;
import tianyancha.yanzhengma.DownloadImgne;

import javax.imageio.stream.FileImageInputStream;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;
import static tianyancha.zhuce.tyc_zhuce.fileExist;
import static tianyancha.zhuce.tyc_zhuce.zhuce;


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



    public static void main(String args[]) throws IOException, InterruptedException, SQLException, ScriptException, NoSuchMethodException {
        Document doc2=Jsoup.parse(new File("C:\\Users\\13434\\Desktop\\a.txt"),"utf-8");
        String ceng = getString(doc2, "div.history-name-box.tag.tag-history-name.mr10 span.history-content", 0);
        String phone = getString(doc2, "div.in-block:containsOwn(电话) span", 1);
        String email = getString(doc2, "div.in-block:containsOwn(邮箱) span", 1);
        String web = getString(doc2, "div.in-block:containsOwn(网址) a", 0);
        String address = getString(doc2,"div.in-block:matches(地址.+)",0)!=null
                ?getString(doc2,"div.in-block:matches(地址.+)",0).replace("附近公司","").replace("地址：","")
                :null;
        String address2 =doc2.select("div.in-block:matches(地址.+) span.pl5").toString()!=null
                ?doc2.select("div.in-block:matches(地址.+) span.pl5").toString().replace("<span class=\"pl5\"><script type=\"text/html\">\"","")
                .replace("\"</script><span class=\"link-click\" onclick=\"openAddressPopup(this)\">详情</span></span>","")
                :null;
        String logo = getHref(doc2, "div.logo.-w100 img", "data-src", 0);
        String statu = getString(doc2, "td:containsOwn(公司状态) div.num-opening", 0);
        String gongshang = getString(doc2, "td:containsOwn(工商注册号)+td", 0);
        String zuzhijigou = getString(doc2, "td:containsOwn(组织机构代码)+td", 0);
        String tongyixinyong = getString(doc2, "td:containsOwn(统一社会信用代码)+td", 0);
        String qiyeleixing = getString(doc2, "td:containsOwn(公司类型)+td", 0);
        String nashuiren = getString(doc2, "td:containsOwn(纳税人识别号)+td", 0);
        String hangye = getString(doc2, "td:containsOwn(行业)+td", 0);
        String yingyeqixian = getString(doc2, "td:containsOwn(营业期限)+td", 0);
        String hezhunriq = getString(doc2, "td:containsOwn(核准日期)+td", 0);
        String dengjijiguan = getString(doc2, "td:containsOwn(登记机关)+td", 0);
        String zhucedizhi = getString(doc2, "td:containsOwn(注册地址)+td", 0).replace("附近公司","");
        String yingming=getString(doc2, "td:containsOwn(英文名称)+td", 0);
        String jingyingfanwei = getString(doc2, "td:containsOwn(经营范围)+td", 0).replace("...详情","");
        String faren = getString(doc2, "div.humancompany div.name a", 0);
        String desc = doc2.select("script#company_base_info_detail").toString().replace("<script type=\"text/html\" id=\"company_base_info_detail\">","").replace("</script>","").replace(" ","").replace("\n","");
        String shizi=getString(doc2, "td:containsOwn(实缴资本)+td", 0);
        String canbao=getString(doc2, "td:containsOwn(参保人数)+td", 0);
        String nazi=getString(doc2, "td:containsOwn(纳税人资质)+td", 0);
        System.out.println(tongyixinyong);


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
