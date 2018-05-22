package wecat.smallexe;

import Utils.Dup;
import Utils.Producer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class bailu {
    private static Producer producer;
    // 代理隧道验证信息
    final static String ProxyUser = "H689K6HN8Z46666D";
    final static String ProxyPass = "3E0DCB86C35C5504";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static Ca ca;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="base";
        String password="imkloKuLiqNMc6Cn";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;
    }

    public static void main(String args[]){

    }

    public static void serach() throws IOException, InterruptedException {
        while (true){
            String page=ca.qu();
            if(!Dup.nullor(page)){
                break;
            }
            Document doc=serachget(page);
            String json=Dup.qujson(doc);
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("Data");
            for(int a=0;a<jsonArray.length();a++){
                JSONObject jsonObject1=jsonArray.getJSONObject(a);
                String newid=Dup.getJsonValue(jsonObject1,"NewsId");
                String title=Dup.getJsonValue(jsonObject1,"Title");
                String tags=Dup.getJsonValue(jsonObject1,"Tags");
                String fabu=Dup.getJsonValue(jsonObject1,"Auther");
                String fari=Dup.getJsonValue(jsonObject1,"InsertDate");


            }

        }
    }

    public static void detail(String newid){
        Document doc=detailget(newid);
        String json=doc.text();
    }


    public static Document serachget(String page){
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect("https://api.bailuzhiku.com/v1.0.3/News/list.php")
                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/4G Language/zh_CN")
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "br, gzip, deflate")
                        .header("Accept-Language", "zh-cn")
                        .header("Connection", "keep-alive")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Host", "api.bailuzhiku.com")
                        .header("Referer", "https://servicewechat.com/wx660cf1b5fc65bc4e/9/page-frame.html")
                        .data("Keyword", "")
                        .data("Page", page)
                        .timeout(3000)
                        .proxy(proxy)
                        .post();
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }

    public static Document detailget(String newsid){
        Document doc= null;
        while (true) {
            try {
                doc=Jsoup.connect("https://api.bailuzhiku.com/v1.0.3/news/detail.php?newsid="+newsid)
                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/4G Language/zh_CN")
                        .header("Accept","*/*")
                        .header("Accept-Encoding","br, gzip, deflate")
                        .header("Accept-Language","zh-cn")
                        .header("Connection","keep-alive")
                        .header("Content-Type","json")
                        .header("Host","api.bailuzhiku.com")
                        .header("Referer","https://servicewechat.com/wx660cf1b5fc65bc4e/9/page-frame.html")
                        .proxy(proxy)
                        .timeout(3000)
                        .get();
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
