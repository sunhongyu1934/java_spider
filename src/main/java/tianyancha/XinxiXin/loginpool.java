package tianyancha.XinxiXin;

import Utils.Dup;
import Utils.MD5Util;
import Utils.RedisClu;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import tianyancha.yanzhengma.DownloadImgne;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class loginpool {
    private static RedisClu redisClu=new RedisClu();
    public static Ca c=new Ca();
    private static Set<String> list=new HashSet<>();
    private static Connection conn;

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

    public static Connection getcon() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="spider";
        String password="spider";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
                if(con!=null){
                    break;
                }
            }
        }
        return con;
    }

    public static void heart() throws SQLException, InterruptedException {
        while (true){
            try {
                if(conn==null||conn.isClosed()){
                    conn=getcon();
                }
                String sql = "select 1";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeQuery();
                Thread.sleep(20000);
            }catch (Exception e){

            }
        }
    }


    public static void main(String args[]) throws InterruptedException, DocumentException, IOException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checklogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        thread2.start();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    heart();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();

        Thread thread3=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    awakenauth();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread3.start();

    }

    public static void getip() throws IOException, InterruptedException {
        while (true) {
            try {
                if(c.po.size()>=2){
                    Thread.sleep(1000);
                    continue;
                }
                String ip=redisClu.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static void controller() throws IOException, InterruptedException, DocumentException {
        Random r=new Random();
        while (true){
            try {
                //tyclogin();
                Thread.sleep((r.nextInt(2) + 1) * 60 * 60 * 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

/*    public static void remove() throws InterruptedException {
        Random r=new Random();
        while (true){
            try {
                Thread.sleep((r.nextInt(2) + 1) * 60 * 60 * 1000);
                list.remove(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

    public static void checkUser() throws FileNotFoundException, DocumentException {
        try {
            String sql="select user_name,pass_word from tyc.tyc_auth where seal_flag=0";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet resultSet=ps.executeQuery();

            Set<String> set = redisClu.getAll("tyc_cookie");
            while (resultSet.next()) {
                String user=resultSet.getString(resultSet.findColumn("user_name"));
                String pass=resultSet.getString(resultSet.findColumn("pass_word"));
                String et=(user+"###"+pass);
                boolean bo = true;
                for (String s : set) {
                    if (et.equals(s.split("######")[0])) {
                        bo = false;
                    }
                }
                if (bo) {
                    list.add(et);
                }
            }

            ps.close();
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void checklogin() throws InterruptedException, SQLException {
        while (true){
            try {
                checkUser();
                System.out.println(list);
                ExecutorService pool= Executors.newFixedThreadPool(20);
                for (String randomValue : list) {
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String sql="update tyc.tyc_auth set seal_flag=1 where user_name=?";
                                PreparedStatement ps=conn.prepareStatement(sql);

                                String sql2="update tyc.tyc_auth set seal_flag=2 where user_name=?";
                                PreparedStatement ps2=conn.prepareStatement(sql2);

                                String username = Dup.reAllNull(randomValue.split("###")[0]);
                                String password = Dup.reAllNull(randomValue.split("###")[1]);
                                String pw = MD5Util.getMD5String(password);

                                //HttpClientBuilder builder = HttpClients.custom();

                                Map<String, String> map1 = new HashMap<>();
                                org.jsoup.nodes.Document docs = null;
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("autoLogin", "true");
                                jsonObject1.put("cdpassword", pw);
                                jsonObject1.put("loginway", "PL");
                                jsonObject1.put("mobile", username);
                                String data = jsonObject1.toString();
                                while (true) {
                                    try {
                                        String ip = c.qu();
                                        docs = Jsoup.connect("https://www.tianyancha.com/cd/login.json")
                                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                                .header("Accept", "*/*")
                                                .header("Accept-Encoding", "gzip, deflate, br")
                                                .header("Accept-Language", "zh-CN,zh;q=0.9")
                                                .header("Connection", "keep-alive")
                                                .header("Content-Type", "application/json; charset=UTF-8")
                                                .header("Host", "www.tianyancha.com")
                                                .header("Origin", "https://www.tianyancha.com")
                                                .header("Referer", "https://www.tianyancha.com/login")
                                                .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                                                .requestBody(data)
                                                .ignoreContentType(true)
                                                .ignoreHttpErrors(true)
                                                .timeout(3000)
                                                .post();
                                        String json = Dup.qujson(docs);
                                        System.out.println(json);

                                        if (Dup.nullor(json)) {
                                            if(json.contains("手机号或密码错误")){
                                                System.out.println(username+"       "+pw+"  "+password);
                                                ps2.setString(1,username);
                                                ps2.executeUpdate();
                                                break;
                                            }else {
                                                JSONObject jsonObject = new JSONObject(json);
                                                if (Dup.nullor(jsonObject.getJSONObject("data").getString("token"))) {
                                                    map1.put("auth_token", jsonObject.getJSONObject("data").getString("token"));
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception ee) {
                                        System.out.println("time out");
                                    }
                                }
                                System.out.println(map1);
                                if(map1!=null&&map1.size()>0) {
                                    JSONObject jsonObject = new JSONObject(map1);
                                    org.jsoup.nodes.Document doc = get(jsonObject.toString());
                                    if (doc != null && !doc.outerHtml().contains("我们只是确认一下你不是机器人") && !doc.outerHtml().contains("你已在其他地点登录") && doc.outerHtml().contains("小米科技有限责任公司")) {
                                        redisClu.set("tyc_cookie", randomValue + "######" + jsonObject.toString());
                                    } else if (doc != null && doc.outerHtml().contains("我们只是确认一下你不是机器人")) {
                                        DownloadImgne.shibie(randomValue + "######" + jsonObject.toString());
                                        authsleep(username);
                                        //redisClu.set("tyc_cookie", randomValue + "######" + jsonObject.toString());
                                    } else if (doc != null && doc.outerHtml().contains("系统检测到您非人类行为")) {
                                        ps.setString(1, username);
                                        ps.executeUpdate();
                                    }
                                }
                                ps.close();
                                ps2.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                pool.shutdown();
                while (true){
                    if (pool.isTerminated()) {
                        System.out.println("结束了！");
                        break;
                    }
                    Thread.sleep(10000);
                }
                list.clear();
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static void authsleep(String user) throws SQLException {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=simpleDateFormat.format(date);
        String sql="update tyc.tyc_auth set seal_flag=3,sleep_time='"+time+"' where user_name='"+user+"'";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    public static void awakenauth() throws SQLException, ParseException, InterruptedException {
        while (true){
            try {
                String sql="select user_name,sleep_time from tyc.tyc_auth where seal_flag=3";
                PreparedStatement ps=conn.prepareStatement(sql);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String sql2="update tyc.tyc_auth set seal_flag=0 where user_name=?";
                PreparedStatement ps2=conn.prepareStatement(sql2);


                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String user = rs.getString(rs.findColumn("user_name"));
                    String time = rs.getString(rs.findColumn("sleep_time"));
                    long be = simpleDateFormat.parse(time).getTime();
                    long now = new Date().getTime();
                    if ((now - be) > (60 * 60 * 1000)) {
                        ps2.setString(1, user);
                        ps2.executeUpdate();
                    }
                }

                ps.close();
                ps2.close();
                rs.close();
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static org.jsoup.nodes.Document get(String cookie) throws InterruptedException {
        org.jsoup.nodes.Document doc=null;
        while (true) {
            try {
                Map<String,Object> map1=new JSONObject(cookie).toMap();
                Map<String,String> map=new HashMap<>();
                for(Map.Entry<String,Object> entry:map1.entrySet()){
                    map.put(entry.getKey(),entry.getValue().toString());
                }

                String ip=c.qu();
                doc = Jsoup.connect("https://www.tianyancha.com/search?key=%E5%B0%8F%E7%B1%B3")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .cookies(map)
                        .get();
                if (doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")&&!doc.outerHtml().contains("503 Service Temporarily Unavailable")&&!doc.outerHtml().contains("too many request")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static class Ca{
        public BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
