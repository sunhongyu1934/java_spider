package itjuzi.zl;

import Utils.Dup;
import Utils.JsoupUtils;
import com.google.common.collect.Lists;
import itjuzi.itjuzi;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;

public class list_zl {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }

    public static void main(String args[]) throws IOException, InterruptedException, SQLException, ParseException, DocumentException {
        search();

    }


    public static void search() throws IOException, DocumentException, SQLException, InterruptedException, ParseException {
        String sql3="insert into it_finacing_pc(c_id,f_id,`juzi_name`,`financing_time`,financing_round,financing_money,vc,`dec_url`,data_date) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps3=conn.prepareStatement(sql3);

        Date dates=new Date();
        SimpleDateFormat simpleDateFormats=new SimpleDateFormat("yyyy-MM-dd");
        String d=simpleDateFormats.format(dates);

        System.out.println("begin login");
        System.out.println("login success");
        int page= Integer.parseInt(jiexi()[0]);
        int day=Integer.parseInt(jiexi()[5]);
        System.out.println("begin search");
        for(int x=1;x<=page;x++) {
            HttpGet get = new HttpGet("https://www.itjuzi.com/investevents?page="+x);
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
            String tag = null;
            while (true) {
                try {
                    //String ip=get();
                    CredentialsProvider credsProvider = new BasicCredentialsProvider();

                    credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
                    HttpHost proxy2 = new HttpHost("proxy.abuyun.com", 9020);
                    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy2);
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                            .setSocketTimeout(50000).build();

                    HttpClientBuilder builder = HttpClients.custom();

                    builder.setRoutePlanner(routePlanner);

                    builder.setDefaultCredentialsProvider(credsProvider);
                    builder.setDefaultRequestConfig(requestConfig);
                    CloseableHttpClient httpclient = builder.build();

                    CloseableHttpResponse response = httpclient.execute(get);
                    HttpEntity resEntity = response.getEntity();
                    tag = EntityUtils.toString(resEntity);
                    if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("time out reget");
                }
            }
            Document doc= Jsoup.parse(tag);
            Elements dateele=getElements(doc,"ul.list-main-eventset li i.cell.date span");
            int pp=1;
            if(dateele!=null){
                Date datee=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.M.d");
                String date1=simpleDateFormat.format(datee);
                long dd1=simpleDateFormat.parse(date1).getTime();
                long dd2=dd1-(day-1)*24*60*60*1000;
                for(Element e:dateele){
                    String date=e.text();
                    long date3=simpleDateFormat.parse(date).getTime();
                    if(date3<dd2){
                        break;
                    }
                    pp++;
                }
            }


            Elements ele=getElements(doc,"ul.list-main-eventset li");
            int oo=1;
            boolean br=false;
            if(ele!=null){
                int a=0;
                for(Element e:ele){
                    if(a>0) {
                        if(oo==pp){
                            br=true;
                            break;
                        }
                        String detailurl = getHref(e, "i.cell.maincell p.title a", "href", 0);
                        String cid = detailurl.split("/", 5)[4];
                        String fid=JsoupUtils.getHref(e,"i.cell.detail a","href",0).split("/",5)[4];
                        String jname=JsoupUtils.getString(e,"i.cell.maincell p.title span",0);
                        String rotime=JsoupUtils.getString(e,"i.cell.date span",0);
                        String lunci=JsoupUtils.getString(e,"i.cell.round span",0);
                        String jin=JsoupUtils.getString(e,"i.cell.money",0);
                        String vc=null;
                        Elements veles=JsoupUtils.getElements(e,"div.investorset a,div.investorset span");
                        if(veles!=null&& Dup.nullor(veles.toString())) {
                            StringBuffer strs = new StringBuffer();
                            for (Element v : veles) {
                                strs.append(v.text()+";");
                            }

                            vc=strs.substring(0,strs.length()-1).toString();
                            System.out.println(vc);
                        }
                        String deurl=JsoupUtils.getHref(e,"i.cell.detail a","href",0);

                        flagdata( fid, "it_finacing_pc",cid,deurl);

                        ps3.setString(1, cid);
                        ps3.setString(2, fid);
                        ps3.setString(3, jname);
                        ps3.setString(4,rotime);
                        ps3.setString(5,lunci);
                        ps3.setString(6,jin);
                        ps3.setString(7,vc);
                        ps3.setString(8,deurl);
                        ps3.setString(9,d);
                        ps3.executeUpdate();
                        System.out.println("stroe data success");
                        System.out.println("----------------------------------------------------------------------");
                        oo++;
                    }
                    a++;
                }
            }
            if(br){
                break;
            }

        }

    }

    public static void flagdata(String fid,String table,String cid,String dec) throws SQLException {
        String sql1="delete from "+table+" where f_id='"+fid+"' and c_id='"+cid+"' and dec_url='"+dec+"'";
        PreparedStatement ps1=conn.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static Map<String,String> login(String[] str) throws IOException {
//创建认证，并设置认证范围
        Map<String,String> map=new HashMap<String, String>();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H6STQJ2G9011329D", "E946B835EC9D2ED7"));
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
        HttpPost post=new HttpPost("https://www.itjuzi.com/user/login?redirect=&flag=&radar_coupon=");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("identity",str[6]));
        params.add(new BasicNameValuePair("password",str[7]));
        params.add(new BasicNameValuePair("remember","1"));
        params.add(new BasicNameValuePair("submit",""));
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

    public static String[] jiexi() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/data1/spider/java_spider/implement/itjz/itjzzl.xml")));
        org.dom4j.Element root=dom.getRootElement();
        org.dom4j.Element table=root.element("table");
        String page=table.element("page").getText();
        String ip=table.element("ip").getText();
        String database=table.element("database").getText();
        String username=table.element("username").getText();
        String password=table.element("password").getText();
        String zhanghu=table.element("zhanghu").getText();
        String mima=table.element("mima").getText();
        String day=table.element("day").getText();
        String str[]=new String[]{page,ip,database,username,password,day,zhanghu,mima};
        /*XMLWriter writer = new XMLWriter(new FileWriter("output.xml"));
        writer.write(doc);
        writer.close();*/
        return str;
    }
}
