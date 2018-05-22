package shuiwu;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.MD5Util;
import Utils.RedisClu;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.*;

public class spider {
    private static Connection conn;
    // 代理隧道验证信息
    final static String ProxyUser = "HA3X4W7A5VHY0CCD";
    final static String ProxyPass = "11A94D1DE1668809";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Ca c=new Ca();
    private static Cp p=new Cp();
    private static int sum=0;

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
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

    }

    public static void main(String args[]) throws InterruptedException {
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    controller();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int q=1;q<=20;q++){
            int finalQ = q;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        spider(finalQ);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        pool.shutdown();
        while (true){
            if (pool.isTerminated()){
                System.out.println("结束了！");
                System.exit(0);
            }
            Thread.sleep(2000);
        }
    }

    public static void controller() throws SQLException, InterruptedException {
        String sql="select comp_full_name from dm_project.dm_comp_name";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            p.fang(Dup.renull(rs.getString(rs.findColumn("comp_full_name"))));
        }
    }

    public static void spider(int xu) throws InterruptedException, SQLException {
        while (true){
            String key=p.qu();
            if(!Dup.nullor(key)){
                break;
            }
            cospider(key,xu);
        }
    }

    public static void cospider(String key,int xu) throws InterruptedException, SQLException {
        while (true) {
            try {
                String ip = c.qu();
                Map<String, String> cookie = getCookie(ip);
                if (cookie == null) {
                    continue;
                }
                Map<String, String> cookie2 = getCookie(cookie, ip);
                if (cookie2 == null) {
                    continue;
                }
                cookie2.putAll(cookie);
                String rand = cookie2.get("rand");
                cookie2.remove("rand");
                Map<String, String> cookie3 = getImg(cookie2, ip,xu);
                if(cookie3==null){
                    continue;
                }
                cookie2.putAll(cookie3);
                System.out.println("kaishishibie-------------------------------------------------");
                String yan = yanzhengma.shibie("img/shuiwuyan" + xu + ".jpg").toLowerCase();
                if(!Dup.nullor(yan)){
                    continue;
                }
                System.out.println("yanheng&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                String yflag=yanzheng(cookie2, yan);
                if(!Dup.nullor(yflag)){
                    continue;
                }
                System.out.println("getdata%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                String data=getData(rand, cookie2, key);
                if(Dup.nullor(data)){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static String getData(String rand,Map<String,String> cookie2,String key) throws SQLException, IOException {
        String sql="insert into revenue_tax_grade(comp_full_name,taxpayer_number,grade_year,comp_label,search_key) values(?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        Document doc;
        while (true){
            try{
                doc=Jsoup.connect("http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .header("Host","hd.chinatax.gov.cn")
                        .header("Origin","http://hd.chinatax.gov.cn")
                        .header("Referer","http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .data("articleField01","")
                        .data("articleField02",key)
                        .data("articleField03","2017")
                        .data("articleField06","")
                        .data("taxCode","")
                        .data("cPage","")
                        .data("randCode",rand)
                        .data("flag","1")
                        .proxy(proxy)
                        .cookies(cookie2)
                        .timeout(5000)
                        .post();
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("/wswaf_forbidden_file/wswaf-403.png")){
                    break;
                }
            }catch (Exception e){

            }
        }
        Elements ele=JsoupUtils.getElements(doc,"table.sv_center td.sv_hei tbody tr");
        int t=0;
        for(Element e:ele){
            try {
                if (t != 0) {
                    String hao = JsoupUtils.getString(e, "td", 0);
                    String ming = JsoupUtils.getString(e, "td", 1);
                    String nian = JsoupUtils.getString(e, "td", 2);

                    if((Dup.nullor(hao)&&!hao.equals("首页"))||Dup.nullor(ming)||Dup.nullor(nian)) {
                        ps.setString(1, ming);
                        ps.setString(2, hao);
                        ps.setString(3, nian);
                        ps.setString(4, "A级纳税人");
                        ps.setString(5, key);
                        ps.executeUpdate();
                    }
                    sum++;
                    System.out.println(sum + "******************************************************");
                }
            }catch (Exception e1){
                e1.printStackTrace();
            }
            t++;
        }
        ps.close();
        if(t==0){
            return null;
        }
        return "success";
    }

    public static Map<String,String> getCookie(String ip){
        org.jsoup.Connection.Response doc;
        int o=0;
        while (true){
            try{
                doc= Jsoup.connect("http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Referer","http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Host","hd.chinatax.gov.cn")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]))
                        .timeout(3000)
                        .method(org.jsoup.Connection.Method.GET)
                .execute();
                if(doc!=null&&!doc.body().contains("abuyun")){
                    /*if(!c.po.contains(ip)){
                        for(int j=1;j<=10;j++){
                            c.fang(ip);
                        }
                    }*/
                    break;
                }
            }catch (Exception e){

            }
            o++;
            if(o>=10){
                return null;
            }
        }
        return doc.cookies();
    }

    public static Map<String,String> getCookie(Map<String,String> map,String ip){
        org.jsoup.Connection.Response doc = null;
        int l=0;
        while (true){
            try{
                doc= Jsoup.connect("http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Referer","http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Host","hd.chinatax.gov.cn")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .cookies(map)
                        .proxy(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]))
                        .timeout(3000)
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                if(doc!=null&&!doc.body().contains("abuyun")){
                    /*if(!c.po.contains(ip)){
                        for(int j=1;j<=10;j++){
                            c.fang(ip);
                        }
                    }*/
                    break;
                }
            }catch (Exception e){

            }
            l++;
            if(l>=10){
                return null;
            }
        }
        String rand= JsoupUtils.getHref(Jsoup.parse(doc.body()),"form#searchForm input[name='randCode']","value",0);
        Map<String,String> mm=doc.cookies();
        mm.put("rand",rand);
        return mm;
    }

    public static String yanzheng(Map<String,String> map,String yan){
        org.jsoup.Connection.Response doc;
        while (true){
            try{
                doc= Jsoup.connect("http://hd.chinatax.gov.cn/fagui/servlet/VerifyServlet?verifyCode="+yan)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Referer","http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .header("Accept","*/*")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Host","hd.chinatax.gov.cn")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(3000)
                        .cookies(map)
                        .method(org.jsoup.Connection.Method.GET)
                .execute();
                if(doc!=null&&!doc.body().contains("abuyun")){
                    break;
                }
            }catch (Exception e){

            }
        }
        String flag=Dup.qujson(Jsoup.parse(doc.body()));
        if(flag.contains("N")){
            return null;
        }else{
            return "success";
        }
    }

    public static Map<String,String> getImg(Map<String,String> map,String ip,int xu){
        org.jsoup.Connection.Response response=null;
        int l=0;
        while (true) {
            try {
                response = Jsoup.connect("http://hd.chinatax.gov.cn/fagui/kaptcha.jpg")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                        .header("Referer", "http://hd.chinatax.gov.cn/fagui/action/InitCredit.do")
                        .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Host","hd.chinatax.gov.cn")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(3000)
                        .cookies(map)
                        .proxy(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]))
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                if (response != null && response.body().length() > 50 && !response.body().contains("abuyun")) {
                    /*if(!c.po.contains(ip)){
                        for(int j=1;j<=10;j++){
                            c.fang(ip);
                        }
                    }*/
                    break;
                }
            } catch (Exception e) {

            }
            l++;
            if(l>=10){
                return null;
            }
        }

        byte[] b=response.bodyAsBytes();
        savaImage(b,"img/shuiwuyan"+xu+".jpg");
        return response.cookies();
    }

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(1000);
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

    public static void savaImage(byte[] img,String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        File dir = new File(filePath);
        try {
            //判断文件目录是否存在
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdir();
            }
            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(img);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Cp{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
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
