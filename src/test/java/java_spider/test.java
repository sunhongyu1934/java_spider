package java_spider;


import Utils.Dup;
import Utils.JsoupUtils;
import Utils.MD5Util;
import Utils.RedisClu;
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
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import shuiwu.spider;
import tianyancha.yanzhengma.DownloadImgne;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
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
    public static void main(String args[]) throws IOException, InterruptedException, SQLException {
        String sql="select comp_full_name from temp_database.comp_mingdan_1000";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        RedisClu rd=new RedisClu();
        int a=0;
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("comp_full_name"));
            rd.set("comp_zl_test",cname);
            a++;
            System.out.println(a+"***************************************");
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
