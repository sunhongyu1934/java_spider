package shuiwu;

import Utils.*;
import org.dom4j.DocumentException;
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

public class spider_list {
    private static Connection conn;
    // 代理隧道验证信息
    final static String ProxyUser = "HJGR1T7575J6744D";
    final static String ProxyPass = "109FD50EC1CC22A7";

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

        for(int q=1;q<=10;q++){
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
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
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
        String add[]=new String[]{"北京###110000","天津###120000","河北###130000","山西###140000","内蒙古###150000","辽宁###210000",
                    "大连###210200","吉林###220000","黑龙江###230000","上海###310000","江苏###320000","浙江###330000","宁波###330200",
                    "安徽###340000","福建###350000","厦门###350200","江西###360000","山东###370000","青岛###370200","河南###410000",
                    "湖北###420000","湖南###430000","广东###440000","深圳###440300","广西###450000","海南###460000","重庆###500000",
                    "四川###510000","贵州###520000","云南###530000","西藏###540000","陕西###610000","甘肃###620000","青海###630000",
                    "宁夏###640000","新疆###650000"};
        for(String s:add) {
            p.fang(s);
        }
    }

    public static void spider(int xu) throws InterruptedException, SQLException, FileNotFoundException, DocumentException {
        Producer producer=new Producer(false);
        while (true){
            String key=p.qu();
            if(!Dup.nullor(key)){
                break;
            }
            for(int l=1;l<=10000;l++) {
                try {
                    String data = cospider(key, xu, l,producer);
                    if (data.equals("over")) {
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String cospider(String key,int xu,int l,Producer producer) throws InterruptedException, SQLException {
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
                String data=getData(rand, cookie2, key,l,producer);
                if(Dup.nullor(data)){
                    return data;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static String getData(String rand,Map<String,String> cookie2,String key,int l,Producer producer) throws SQLException, IOException {
        //String sql="insert into revenue_tax_grade(comp_full_name,taxpayer_number,grade_year,comp_label,comp_area) values(?,?,?,?,?)";
        //PreparedStatement ps=conn.prepareStatement(sql);

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
                        .data("articleField02","")
                        .data("articleField03","2017")
                        .data("articleField06","")
                        .data("taxCode",key.split("###")[1])
                        .data("cPage", String.valueOf(l))
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
                if (t != 0&&t!=18) {
                    String hao = JsoupUtils.getString(e, "td", 0);
                    String ming = JsoupUtils.getString(e, "td", 1);
                    String nian = JsoupUtils.getString(e, "td", 2);

                    if((Dup.nullor(hao)&&!hao.equals("首页"))||Dup.nullor(ming)||Dup.nullor(nian)) {
                        /*ps.setString(1, ming);
                        ps.setString(2, hao);
                        ps.setString(3, nian);
                        ps.setString(4, "A级纳税人");
                        ps.setString(5, key.split("###")[0]);
                        ps.executeUpdate();*/

                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("familyname","chinatax");
                        jsonObject.put("tablename","revenue_tax_grade");
                        jsonObject.put("rowkey","comp_full_name+comp_full_name###comp_area###familyname");
                        jsonObject.put("comp_id","createid=comp_full_name");
                        jsonObject.put("comp_full_name",ming);
                        jsonObject.put("taxpayer_number",hao);
                        jsonObject.put("grade_year",nian);
                        jsonObject.put("comp_label","A级纳税人");
                        jsonObject.put("comp_area",key.split("###")[0]);
                        producer.send("ControlTotal",jsonObject.toString());
                    }
                    sum++;
                    System.out.println(sum + "******************************************************");
                }
            }catch (Exception e1){
                e1.printStackTrace();
            }
            t++;
        }
        //ps.close();
        if(t==0){
            return null;
        }
        if(t>0&&t<19){
            return "over";
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
