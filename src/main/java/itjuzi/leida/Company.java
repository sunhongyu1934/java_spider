package itjuzi.leida;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/7/4.
 */
public class Company {
    // 代理隧道验证信息
    final static String ProxyUser = "H4TL2M827AIJ963D";
    final static String ProxyPass = "81C9D64628A60CF9";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        Company c=new Company();
        final Keys k=c.new Keys();
        ExecutorService pool= Executors.newCachedThreadPool();
        final java.sql.Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Run(finalCon,proxy,k);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public static void data(java.sql.Connection con,Keys k) throws SQLException, IOException, InterruptedException {
        String sql="select distinct c_id from dw_online.it_company_pc where sName!='' and c_id not in (select s_id from it_leida_company)";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String sid=rs.getString(rs.findColumn("c_id"));
            k.put(sid);
        }
    }

    public static void Run(java.sql.Connection con,Proxy proxy,Keys k) throws IOException {
        Random r=new Random();
        Map<String,String> map=login();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 10800000;
        int j=0;
        while (true){
            try{
                String sid=k.qu();
                String zong=getfinacing(con,sid,map,proxy);
                System.out.println("rongzi ok");
                get(con, zong, sid, map, proxy);
                System.out.println("gongsi ok");
                getjingpin(con, sid, map, proxy);
                System.out.println("jingpin ok");
                gettuandui(con, sid, map, proxy);
                System.out.println("tuandui ok");
                j++;
                System.out.println(j+"***********************************************************");
                long t = System.currentTimeMillis();
                if(t>(begin+cur)){
                    System.out.println("return login");
                    cur=(r.nextInt(50) * 60 * 1000) + 10800000;
                    map=login();
                    t=System.currentTimeMillis();
                    begin = t;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static Map<String,String> login() throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H4TL2M827AIJ963D", "81C9D64628A60CF9"));
        HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();

        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        builder.setDefaultRequestConfig(requestConfig);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = builder.setDefaultCookieStore(cookieStore).build();
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity","13717951934"));
        params.add(new BasicNameValuePair("password","3961shy"));
        params.add(new BasicNameValuePair("remember","1"));
        params.add(new BasicNameValuePair("page",""));
        params.add(new BasicNameValuePair("url",""));
        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        List<Cookie> cookies =null;
        while (true) {
            CloseableHttpResponse response = httpclient.execute(post);
            HttpEntity resEntity = response.getEntity();
            String tag = EntityUtils.toString(resEntity);
            cookies=cookieStore.getCookies();
            if (cookies != null && cookies.size()>2) {
                break;
            }
        }
        for (int i = 0; i < cookies.size(); i++) {
            map.put(cookies.get(i).getName(),cookies.get(i).getValue());
        }

        /*Connection.Response res= null;
        while (true) {
            try {
                res = Jsoup.connect("https://www.itjuzi.com/user/login?redirect=&flag=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .header("Content-Type", "text/html;charset=utf-8")
                        .data("identity", "13717951934")
                        .data("password", "3961shy")
                        .data("remember", "1")
                        .data("page", "")
                        .data("url", "")
                        .timeout(100000)
                        .method(Connection.Method.POST)
                        .execute();
                if (res != null && StringUtils.isNotEmpty(res.cookies().toString().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out");
            }
        }
        Map<String,String> map=res.cookies();*/
        System.out.println(map);
        return map;
    }

    public static void get(java.sql.Connection con,String zong,String sid, Map<String,String> map,Proxy proxy) throws IOException, SQLException {
        String sql="insert into it_leida_company(s_id,c_name,c_shortname,logo,web,f_fin,industry,sub_ins,tags,s_desc,address,c_time,gui_mo,gu_zhi,fin_sum) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://radar.itjuzi.com/company/"+sid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .cookies(map)
                        .proxy(proxy)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .get();
                if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out gongsi");
            }
        }

        String jiancheng="";
        try {
            doc.select("div.company-information div.company-title h2").first().ownText();
        }catch (Exception e){
            jiancheng="";
        }
        String web=getHref(doc,"div.company-information div.company-title h2 a","href",0);
        String frong=getString(doc, "div.company-information div.company-title h2 span", 0);
        String logo=getHref(doc, "div.company-information img#ed_imgauto", "src", 0);
        String indus=getString(doc,"div.company-information div.company-title p.company-industry a",0);
        String sub_indus=getString(doc,"div.company-information div.company-title p.company-industry a",1);
        Elements tele=getElements(doc,"div.company-information ul.company-tit-tags li");
        StringBuffer str=new StringBuffer();
        if(tele!=null){
            int l=0;
            for(Element e:tele){
                if(l!=0){
                    str.append(getString(e,"a",0)+";");
                }
                l++;
            }
        }
        String tags="";
        try {
            tags=str.toString();
        }catch (Exception e){
            tags="";
        }
        String quancheng="";
        try {
            quancheng=getString(doc, "div.content-main div.content-content p.cont-Introduction", 0).replace("工商全称：", "");
        }catch (Exception e){
            quancheng="";
        }
        String jianjie=getString(doc, "div.content-main div.content-content p.cont-Introduction",1);
        String address=getString(doc, "div.content-main div.content-content ul.cont-lable li span", 0);
        String time=getString(doc,"div.content-main div.content-content ul.cont-lable li span",1);
        String guimi=getString(doc,"div.content-main div.content-content ul.cont-lable li span",2);
        String guzhi=getString(doc,"div.content-main div.content-content ul.cont-lable li",3);

        ps.setString(1, sid);
        ps.setString(2, quancheng);
        ps.setString(3, jiancheng);
        ps.setString(4,logo);
        ps.setString(5,web);
        ps.setString(6,frong);
        ps.setString(7,indus);
        ps.setString(8,sub_indus);
        ps.setString(9,tags);
        ps.setString(10,jianjie);
        ps.setString(11,address);
        ps.setString(12,time);
        ps.setString(13,guimi);
        ps.setString(14,guzhi);
        ps.setString(15,zong);
        ps.executeUpdate();

        /*Connection.Response res=Jsoup.connect("http://radar.itjuzi.com/company/getinvchartdata/4782")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .cookies(map)*/
                //.header("Accept","application/json, text/javascript, */*; q=0.01")
               /* .header("X-Requested-With","XMLHttpRequest")
                .header("Origin","http://radar.itjuzi.com")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(100000)
                .method(Connection.Method.POST)
                .execute();
        String str=res.body();
        Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
        Matcher mat= pattern.matcher(str);


        while(mat.find()){
            StringBuffer string = new StringBuffer();
            int data = Integer.parseInt(mat.group(0).replace("\\u",""), 16);
            string.append((char) data);
            str=str.replace(mat.group(0),string.toString());
        }
        System.out.println(str);*/

    }


    public static String getfinacing(java.sql.Connection con,String sid, Map<String,String> map,Proxy proxy) throws IOException, SQLException {
        String sql="insert into it_leida_finacing(s_id,fin_time,fin_round,fin_pc,fin_money,fin_pc_sid) values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        System.out.println("aaa");

        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://radar.itjuzi.com//company/invest/"+sid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .cookies(map)
                        .proxy(proxy)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .get();
                if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out rongzi");
            }
        }

        String rongzizonge=getString(doc,"div.content-main div.content-content div.cont-financing ul.financing-lable span",1);
        Elements ele=getElements(doc,"div.content-main div.content-content div.cont-financing ul.financing-list li");
        if(ele!=null){
            for(Element e:ele){
                try {
                    String lunci = getString(e, "span.info-round", 0);
                    String time = getString(e, "span.list-time", 0);
                    String money = getString(e, "span.info-invest", 0);
                    Elements tele = getElements(e, "p.financing-note a");
                    StringBuffer str = new StringBuffer();
                    StringBuffer str2 = new StringBuffer();
                    if (tele != null && StringUtils.isNotEmpty(tele.toString())) {
                        for (Element ee : tele) {
                            str.append(ee.text() + ";");
                            str2.append(ee.attr("href").replace("/investment/", "") + ";");
                        }
                    } else {
                        str.append(e.select("p.financing-note").text());
                    }
                    String jigou = str.toString();
                    String jigouid = str2.toString();

                    ps.setString(1, sid);
                    ps.setString(2, time);
                    ps.setString(3, lunci);
                    ps.setString(4, jigou);
                    ps.setString(5, money);
                    ps.setString(6, jigouid);
                    ps.executeUpdate();
                }catch (Exception e1){

                }
            }
        }
        return rongzizonge;
    }



    public static void getjingpin(java.sql.Connection con,String sid, Map<String,String> map,Proxy proxy) throws IOException, SQLException {
        String sql="insert into it_leida_jingpin(s_id,c_name,c_sid,c_logo,c_round,juzi_zhishu,address,gu_zhi,c_money,gui_mo,chengli_yueshu,c_pc,one_yue,last_yue) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://radar.itjuzi.com/company/compete/"+sid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .cookies(map)
                        .timeout(20000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .get();
                if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out jingpin");
            }
        }


        Elements ele=getElements(doc,"div.content-main table.content-competition tbody tr");
        if(ele!=null){
            for(Element e:ele){
                try {
                    String logo = getHref(e, "td img", "src", 0);
                    String ming = getString(e, "td a", 0);
                    String gid = getHref(e, "td a", "href", 0).replace("/company/", "");
                    String lunci = getString(e, "td", 3);
                    String juzizhishu = getString(e, "td", 4);
                    String diqu = getString(e, "td", 5);
                    String guzhi = getString(e, "td", 6);
                    String rongzizonge = getString(e, "td", 7);
                    String guimo = getString(e, "td", 8);
                    String yueshu = getString(e, "td", 9);
                    String toujigou = getString(e, "td a", 1);
                    String diyilunyueshu = getString(e, "td", 11);
                    String zuihouyilun = getString(e, "td", 12);

                    ps.setString(1, sid);
                    ps.setString(2, ming);
                    ps.setString(3, gid);
                    ps.setString(4, logo);
                    ps.setString(5, lunci);
                    ps.setString(6, juzizhishu);
                    ps.setString(7, diqu);
                    ps.setString(8, guzhi);
                    ps.setString(9, rongzizonge);
                    ps.setString(10, guimo);
                    ps.setString(11, yueshu);
                    ps.setString(12, toujigou);
                    ps.setString(13, diyilunyueshu);
                    ps.setString(14, zuihouyilun);
                    ps.executeUpdate();
                }catch (Exception e1){

                }
            }
        }
    }

    public static void gettuandui(java.sql.Connection con,String sid, Map<String,String> map,Proxy proxy) throws IOException, SQLException {
        String sql="insert into it_leida_founder(s_id,p_name,p_logo,p_zhuwu,p_desc,p_jiaoyu) values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://radar.itjuzi.com/company/team/"+sid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .cookies(map)
                        .timeout(10000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .get();
                if (doc != null && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out chengyuan");
            }
        }


        Elements ele=getElements(doc,"div.content-main div.content-content div.content-list-box ul.team-list li");
        if(ele!=null){
            for(Element e:ele){
                try {
                    String logo = getHref(e, "img", "src", 0);
                    String ming = e.select("p.team-name").first().ownText();
                    String zhiwu = getString(e, "p.team-name span", 0);
                    String desc = getString(e, "p.team-note", 0);
                    StringBuffer str = new StringBuffer();
                    Elements elee = getElements(e, "p.team-tags span");
                    if (elee != null) {
                        for (Element ee : elee) {
                            str.append(ee.text() + ";");
                        }
                    }
                    String jingli = str.toString();

                    ps.setString(1, sid);
                    ps.setString(2, ming);
                    ps.setString(3, logo);
                    ps.setString(4, zhiwu);
                    ps.setString(5, desc);
                    ps.setString(6, jingli);
                    ps.executeUpdate();
                }catch (Exception e1){

                }
            }
        }
    }


    class Keys{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }

        public String qu() throws InterruptedException {
            return bo.take();
        }
    }
}
