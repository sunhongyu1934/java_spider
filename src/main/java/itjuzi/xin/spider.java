package itjuzi.xin;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import itjuzi.zl.itjz_l;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.getElement;

public class spider {
    private static Ca c=new Ca();
    private static Map<String,String> map;
    private static PreparedStatement ps1;
    private static PreparedStatement ps2;
    private static int a=0;
    private static java.sql.Connection conn;
    static{
        try {
            String driver1 = "com.mysql.jdbc.Driver";
            String url1 = "jdbc:mysql://172.31.215.38:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
            String username = "spider";
            String password = "spider";
            Class.forName(driver1).newInstance();
            java.sql.Connection con = null;
            try {
                con = DriverManager.getConnection(url1, username, password);
            } catch (Exception e) {
                while (true) {
                    con = DriverManager.getConnection(url1, username, password);
                    if (con != null) {
                        break;
                    }
                }
            }
            conn=con;

            String sql1 = "insert into it_company_pc(c_id,`sName`,web_url,company_slogan,company_industry,sub_industry,company_address,company_logo,company_tags,product_logos,company_introduction,company_full_name,found_time,company_scale,company_status,mongo_id,`source_url`,data_date,e_mail,phone) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps1 = con.prepareStatement(sql1);

            String sql3 = "insert into it_finacing_pc(c_id,f_id,`juzi_name`,`financing_time`,financing_round,financing_money,vc,`dec_url`,data_date) values(?,?,?,?,?,?,?,?,?)";
            ps2 = con.prepareStatement(sql3);
        }catch (Exception e){

        }
    }
    public static void main(String args[]) throws IOException, InterruptedException, SQLException, ParseException {
        Thread thread=new Thread(new Runnable() {
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
        thread.start();
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        String day=args[0];
        map=login();
        controller(day);
    }

    public static void controller(String day) throws IOException, SQLException, ParseException {
        Date dates=new Date();
        long ti=dates.getTime()-(Long.parseLong(day)*24*60*60*1000);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        for(int a=4;a<=100;a++){
            try {
                String json = serach(String.valueOf(a));
                JSONObject jsonObject = new JSONObject(json);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray jsonArray = data.getJSONArray("rows");
                boolean bo=false;
                for (int b = 0; b < jsonArray.length(); b++) {
                    try {
                        String date = jsonArray.getJSONObject(b).get("date").toString();
                        String compid = jsonArray.getJSONObject(b).get("com_id").toString();
                        String table[] = new String[]{"it_company_pc", "it_finacing_pc"};
                        for (int g = 0; g < table.length; g++) {
                            flagdata(compid, table[g]);
                        }
                        detail(compid);
                        long tt = simpleDateFormat.parse(date).getTime();
                        if (tt < ti) {
                            bo=true;
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(bo){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public static void detail(String compid) throws IOException, SQLException {
        Gson gson=new Gson();
        Document doc=null;
        while (true){
            try {
                String proxyIpAndPort = c.qu();
                doc = Jsoup.connect("https://www.itjuzi.com/company/"+compid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "www.itjuzi.com")
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .cookies(map)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(3000)
                        .get();
                if(doc!=null&&doc.outerHtml().length()>46){
                    break;
                }
            }catch (Exception e){

            }
        }

        String name=JsoupUtils.getString(doc, "div.picinfo div.line-title span.title h1.seo-important-title", 0);
        String urlguan=JsoupUtils.getHref(doc, "div.link-line a", "href", 2);
        String kouhao=JsoupUtils.getString(doc,"div.info-line h2.seo-slogan",0);
        String hangye=JsoupUtils.getString(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a.one-level-tag",0);
        String zihangye=JsoupUtils.getString(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a.two-level-tag",0);
        String dizhi= JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-address-o) span",0);
        String logo=JsoupUtils.getHref(doc, "div.rowhead div.pic img", "src", 0);
        String tags=null;
        Elements tagele=JsoupUtils.getElements(doc,"div.rowfoot div.tagset.dbi.c-gray-aset.tag-list.feedback-btn-parent a");
        if(tagele!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:tagele){
                String tag=JsoupUtils.getString(e, "a", 0);
                String tagurl=e.attr("href");
                map.put(tagurl,tag);
                str.append(tag+";");
            }
            tags=str.toString();
        }
        String productlogos=null;
        Elements prolos=JsoupUtils.getElements(doc,"div.sec ul.list-prodcase.limited-itemnum li div.left");
        if(prolos!=null){
            StringBuffer str=new StringBuffer();
            for(Element e:prolos){
                if(StringUtils.isNotEmpty(JsoupUtils.getHref(e,"span.uniicon.albumbg","style",0).replace("background-image: url(","").replace(")",""))) {
                    str.append(JsoupUtils.getHref(e, "span.uniicon.albumbg", "style", 0).replace("background-image: url(", "").replace(")", "") + ";");
                }
            }
            productlogos=str.toString();
        }
        String yewu=JsoupUtils.getHref(doc,"meta[name='Description']","content",0);

        String fullname=JsoupUtils.getString(doc,"div.block div.des-more h2.seo-second-title",0).replace("公司全称：","");
        String chenglitime=JsoupUtils.getString(doc,"div.block div.des-more h3.seo-secand-tilte span",0);
        String guimo=JsoupUtils.getString(doc,"div.block div.des-more h3.seo-secand-tilte span",1);
        String zhuangtai=JsoupUtils.getString(doc,"div.block div.des-more span.tag.green",0);
        String email=JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-email-o) span",0);
        String phone=JsoupUtils.getString(doc,"ul.list-block.aboutus li:has(i.fa.icon.icon-phone-o) span",0);

        String mongo_id="0";

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String d=simpleDateFormat.format(date);

        ps1.setString(1,compid);
        ps1.setString(2,name);
        ps1.setString(3,urlguan);
        ps1.setString(4,kouhao);
        ps1.setString(5,hangye);
        ps1.setString(6,zihangye);
        ps1.setString(7,dizhi);
        ps1.setString(8,logo);
        ps1.setString(9,tags);
        ps1.setString(10,productlogos);
        ps1.setString(11,yewu);
        ps1.setString(12,fullname);
        ps1.setString(13,chenglitime);
        ps1.setString(14,guimo);
        ps1.setString(15,zhuangtai);
        ps1.setString(16,mongo_id);
        ps1.setString(17,"http://www.itjuzi.com/company/"+compid);
        ps1.setString(18,d);
        ps1.setString(19,email);
        ps1.setString(20,phone);
        ps1.executeUpdate();
        System.out.println(compid+"         "+fullname);

        int pp=0;
        Elements rzele=JsoupUtils.getElements(doc,"div#invest-portfolio table.list-round-v2 tbody tr.feedback-btn-parent");
        if(rzele!=null){
            for(Element e:rzele){
                String rzjjurl=JsoupUtils.getHref(e,"td:contains(详情) a","href",0);
                String rzlc=JsoupUtils.getString(e,"td.mobile-none span.round a",0);
                String fid=JsoupUtils.getHref(e,"td:contains(详情) a","href",0).split("/",5)[4];
                String rztime=JsoupUtils.getString(e,"td span.date",0);
                String rzje=JsoupUtils.getString(e,"td span.finades a",0);
                Elements vceles=JsoupUtils.getElements(getElement(e,"td",3),"a");
                String vc=null;
                StringBuffer str=new StringBuffer();
                if(vceles!=null){
                    for(Element ee:vceles){
                        str.append(ee.text()+";");
                    }
                }
                try{
                    vc=str.substring(0,str.length()-1).toString();
                }catch (Exception eee){

                }



                ps2.setString(1, compid);
                ps2.setString(2, fid);
                ps2.setString(3, name);
                ps2.setString(4,rztime);
                ps2.setString(5,rzlc);
                ps2.setString(6,rzje);
                ps2.setString(7,vc);
                ps2.setString(8,rzjjurl);
                ps2.setString(9,d);
                ps2.addBatch();
                pp++;
            }
            ps2.executeBatch();
        }

        if(pp>=3) {
            String json = detail2(compid);
            if (Dup.nullor(json)) {
                try {
                    itjz_l.fin f = gson.fromJson(json, itjz_l.fin.class);
                    for (itjz_l.fin.Dd dd : f.data) {
                        String rzjjurl = dd.url;
                        String rzlc = dd.round;
                        String fid = dd.investevents_id;
                        if (!Dup.nullor(fid)) {
                            fid = dd.acquisition_id;
                        }
                        String rztime = dd.date;
                        String rzje = dd.money;
                        String vc = null;
                        StringBuffer str = new StringBuffer();
                        for (itjz_l.fin.Dd.ito i : dd.investors) {
                            str.append(i.name + ";");
                        }
                        try {
                            vc = str.substring(0, str.length() - 1).toString();
                        } catch (Exception ee) {

                        }
                        ps2.setString(1, compid);
                        ps2.setString(2, fid);
                        ps2.setString(3, name);
                        ps2.setString(4, rztime);
                        ps2.setString(5, rzlc);
                        ps2.setString(6, rzje);
                        ps2.setString(7, vc);
                        ps2.setString(8, rzjjurl);
                        ps2.setString(9, d);
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                } catch (Exception es) {

                }
            }
        }
        a++;
        System.out.println(a+"***********************************************************");
    }

    public static void flagdata(String cid, String table) throws SQLException {
        String sql1="delete from "+table+" where c_id='"+cid+"'";
        PreparedStatement ps1=conn.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static String detail2(String compid){
        Document doc=null;
        while (true){
            try {
                String proxyIpAndPort = c.qu();
                doc = Jsoup.connect("https://www.itjuzi.com/company/ajax_load_com_invse/"+compid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "www.itjuzi.com")
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .cookies(map)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(3000)
                        .get();
                if(doc!=null&&doc.outerHtml().length()>46){
                    break;
                }
            }catch (Exception e){

            }
        }
        String json=Dup.qujson(doc);
        return json;
    }

    public static String serach(String page){
        Document doc=null;
        while (true){
            try{
                String proxyIpAndPort = c.qu();
                doc=Jsoup.connect("http://radar.itjuzi.com/investevent/info?location=in&orderby=def&page="+page)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept","application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Accept-Language","zh-CN,zh;q=0.9")
                        .header("Host","radar.itjuzi.com")
                        .header("Referer","http://radar.itjuzi.com//investevent")
                        .header("X-Requested-With","XMLHttpRequest")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .timeout(3000)
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .get();
                if(doc!=null&&doc.outerHtml().length()>100){
                    break;
                }
            }catch (Exception e){

            }
        }
        String json= Dup.qujson(doc);
        return json;
    }

    public static Map<String,String> login() throws IOException, InterruptedException {
        Connection.Response doc= null;
        while (true) {
            try {
                String proxyIpAndPort = c.qu();
                doc = Jsoup.connect("https://www.itjuzi.com/user/login?redirect=&flag=&radar_coupon=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Host", "www.itjuzi.com")
                        .header("Origin", "https://www.itjuzi.com")
                        .header("Referer", "https://www.itjuzi.com/user/login?redirect=&flag=")
                        .data("identity", "13717951934")
                        .data("password", "123456")
                        .data("submit", "")
                        .data("page", "")
                        .data("url", "")
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .method(Connection.Method.POST)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(3000)
                        .execute();
                if(doc!=null&&!doc.body().contains("abuyun")&&doc.cookies().size()>2){
                    break;
                }
            }catch (Exception e){

            }
        }
        System.out.println(doc.cookies());
        return doc.cookies();
    }




    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                /*Document doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=552166bfe40bf4f7af05ae2b6c6ccd2a&sep=3")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String[] ips = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").trim().split(" ");
                for(String s:ips){
                    if (s.contains("requests") || s.contains("请控制")) {
                        continue;
                    }
                    c.fang(s.trim());
                }*/
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(2000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static void conip() throws InterruptedException {
        while (true){
            if(c.po.size()>=5) {
                c.qu();
            }
            Thread.sleep(1000);
        }
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
