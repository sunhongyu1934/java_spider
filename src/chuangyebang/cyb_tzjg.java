package chuangyebang;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/8/11.
 */
public class cyb_tzjg {
    // 代理隧道验证信息
    final static String ProxyUser = "H71K6773EZ870E0D";
    final static String ProxyPass = "341C5939CFF9F8B5";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;

    public cyb_tzjg(){
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        this.proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        final Connection finalCon = con;
        cyb_tzjg c=new cyb_tzjg();
        final URL u=c.new URL();
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int x=1;x<=18;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detailget(u, finalCon);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        int p=1;
        for(int y=1;y<=2;y++) {
            final int finalP = p;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serachget(u, finalP);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            p=p+61;
        }

    }
    public static void serachget(URL u,int p) throws IOException, InterruptedException {
        Document doc;
        for(int x=p;x<p+61;x++) {
            doc=get("http://www.cyzone.cn/company/list-0-"+x+"-0/");
            Elements ele = getElements(doc, "div.list-table2 table tbody tr.table-plate2");
            if (ele != null) {
                for (Element e : ele) {
                    String url = getHref(e, "td.table-company2-tit a", "href", 0);
                    if(StringUtils.isNotEmpty(url)) {
                        u.fang(url);
                    }
                }
            }
        }
    }

    public static void detailget(URL u,Connection con) throws InterruptedException, IOException, SQLException {
        String sql1="insert into cyb_touzijigou(c_id,c_name,c_logo,cheng_time,touzi_ph,w_eb,de_sc) values(?,?,?,?,?,?,?)";
        String sql2="insert into cyb_tzjg_founder(c_id,p_name,p_url,p_logo,p_zhiwu) values(?,?,?,?,?)";
        String sql3="insert into cyb_tzjg_finacing(c_id,t_ming,t_id,t_money,t_hangye,t_round,t_time) values(?,?,?,?,?,?,?)";
        String sql4="insert into cyb_tzjg_news(c_id,c_logo,c_title,c_lianjie) values(?,?,?,?)";

        PreparedStatement ps1=con.prepareStatement(sql1);
        PreparedStatement ps2=con.prepareStatement(sql2);
        PreparedStatement ps3=con.prepareStatement(sql3);
        PreparedStatement ps4=con.prepareStatement(sql4);

        Document doc;
        while (true){
            String url=u.qu();
            if(StringUtils.isEmpty(url)){
                break;
            }
            doc=get(url);
            String cid=url.split("/",6)[3]+"-"+url.split("/",6)[4]+"-"+url.split("/",6)[5].replace(".html","");
            String ming=getString(doc,"div.top-info.clearfix.top-info-organize ul li.organize h1",0);
            String logo=getHref(doc,"div.top-info.clearfix.top-info-organize div.tl-img-bar img","src",0);
            String chengli=getString(doc,"div.top-info.clearfix.top-info-organize ul li",1).replace("成立时间：","");
            String pianhao=getString(doc,"div.top-info.clearfix.top-info-organize ul li",2).replace("投资偏好：","");
            String web=getString(doc,"div.top-info.clearfix.top-info-organize ul li",3).replace("机构官网：","");
            String desc=getString(doc,"div.people-info div.people-info-intro div.people-info-box p",0);

            ps1.setString(1,cid);
            ps1.setString(2,ming);
            ps1.setString(3,logo);
            ps1.setString(4,chengli);
            ps1.setString(5,pianhao);
            ps1.setString(6,web);
            ps1.setString(7,desc);
            ps1.executeUpdate();

            Elements ele=getElements(doc,"div.team.clearfix.look ul.clearfix li");
            if(ele!=null){
                for(Element e:ele){
                    String name=getString(e,"div.team-info p.name a",0);
                    String rurl=getHref(e,"div.team-info p.name a","href",0);
                    String rlogo=getHref(e,"div.team-img a img","src",0);
                    String zhiwu=getString(e,"div.team-info p.job",0);

                    ps2.setString(1,cid);
                    ps2.setString(2,name);
                    ps2.setString(3,rurl);
                    ps2.setString(4,rlogo);
                    ps2.setString(5,zhiwu);
                    ps2.executeUpdate();
                }
            }

            String fin=getHref(doc,"div.check-more2 a","href",0);
            if(StringUtils.isNotEmpty(fin)&&!fin.equals("http://www.cyzone.cn/")){
                finac(fin,ps3,cid);
            }else{
                Elements fele=getElements(doc,"div.live table.limit-8>tbody>tr.table-plate3");
                if(fele!=null){
                    int p=0;
                    for(Element e:fele){
                        if(p>0){
                            String tming=getString(e,"td.table-organize a",0);
                            String turl=getHref(e,"td.table-organize a","href",0);
                            String tid;
                            if(StringUtils.isNotEmpty(turl)&&!turl.equals("http://www.cyzone.cn/")) {
                                tid= turl.split("/", 6)[3] + "-" + turl.split("/", 6)[4] + "-" + turl.split("/", 6)[5].replace(".html", "");
                            }else{
                                tid="";
                            }
                            String jine=getString(e,"div.money",0);
                            String hangye=getString(e,">td",2);
                            String jieduan=getString(e,">td",3);
                            String shijian=getString(e,">td",4);

                            ps3.setString(1,cid);
                            ps3.setString(2,tming);
                            ps3.setString(3,tid);
                            ps3.setString(4,jine);
                            ps3.setString(5,hangye);
                            ps3.setString(6,jieduan);
                            ps3.setString(7,shijian);
                            ps3.executeUpdate();
                        }
                        p++;
                    }
                }
            }

            String baourl=getHref(doc,"div.about.clearfix div.about-title a","href",0).replace("&amp;","&");
            if(StringUtils.isNotEmpty(baourl)&&!baourl.equals("http://www.cyzone.cn/")){
                news(baourl,ps4,cid);
            }
            System.out.println(u.u.size()+"*********************************************************************");
        }
    }

    public static void finac(String url,PreparedStatement ps3,String cid) throws IOException, SQLException, InterruptedException {
        Document doc=get(url);
        String cas=url.split("-",5)[1];
        String weiye="";
        if(StringUtils.isNotEmpty(getHref(doc,"div#pages a#lastpage","href",0))&&!getHref(doc,"div#pages a#lastpage","href",0).equals("http://www.cyzone.cn/")){
            weiye=getHref(doc,"div#pages a#lastpage","href",0).split("-",5)[4].replace("/","");
        }
        if(StringUtils.isNotEmpty(weiye)) {
            for (int x = 1; x <= Integer.parseInt(weiye); x++) {
                if (x >= 2) {
                    doc = get("http://www.cyzone.cn/company/case-" + cas + "-0-0-" + x + "/");
                }
                Elements ele = getElements(doc, "div.list-table3 table.event01>tbody>tr.table-plate3");
                if (ele != null) {
                    for (Element e : ele) {
                        String ming = getString(e, "td.tp2 span.tp2_tit a", 0);
                        String murl = getHref(e, "td.tp2 span.tp2_tit a", "href", 0);
                        String mid;
                        if(StringUtils.isNotEmpty(murl)&&!murl.equals("http://www.cyzone.cn/")) {
                             mid= murl.split("/", 6)[3] + "-" + murl.split("/", 6)[4] + "-" + murl.split("/", 6)[5].replace(".html", "");
                        }else{
                            mid="";
                        }
                        String jine = getString(e, "div.money", 0);
                        String jieduan = getString(e, ">td", 2);
                        String hangye = getString(e, ">td", 4);
                        String shijian = getString(e, ">td", 5);

                        ps3.setString(1,cid);
                        ps3.setString(2,ming);
                        ps3.setString(3,mid);
                        ps3.setString(4,jine);
                        ps3.setString(5,hangye);
                        ps3.setString(6,jieduan);
                        ps3.setString(7,shijian);
                        ps3.executeUpdate();
                    }
                }
            }
        }else{
            Elements ele = getElements(doc, "div.list-table3 table.event01>tbody>tr.table-plate3");
            if (ele != null) {
                for (Element e : ele) {
                    String ming = getString(e, "td.tp2 span.tp2_tit a", 0);
                    String murl = getHref(e, "td.tp2 span.tp2_tit a", "href", 0);
                    String mid;
                    if(StringUtils.isNotEmpty(murl)&&!murl.equals("http://www.cyzone.cn/")) {
                        mid= murl.split("/", 6)[3] + "-" + murl.split("/", 6)[4] + "-" + murl.split("/", 6)[5].replace(".html", "");
                    }else{
                        mid="";
                    }
                    String jine = getString(e, "div.money", 0);
                    String jieduan = getString(e, ">td", 2);
                    String hangye = getString(e, ">td", 4);
                    String shijian = getString(e, ">td", 5);

                    ps3.setString(1,cid);
                    ps3.setString(2,ming);
                    ps3.setString(3,mid);
                    ps3.setString(4,jine);
                    ps3.setString(5,hangye);
                    ps3.setString(6,jieduan);
                    ps3.setString(7,shijian);
                    ps3.executeUpdate();
                }
            }
        }
    }

    public static void news(String url,PreparedStatement ps4,String cid) throws IOException, SQLException, InterruptedException {
        url=url.split("wd=",2)[0]+"wd="+URLEncoder.encode(url.split("wd=",2)[1],"UTF-8");
        Document doc=get(url);
        String weiye="";
        if(StringUtils.isNotEmpty(getHref(doc,"div.page-box a#lastpage","href",0))&&!getHref(doc,"div.page-box a#lastpage","href",0).equals("http://www.cyzone.cn/")){
            weiye=getHref(doc,"div.page-box a#lastpage","href",0).replace("&amp;","&").split("&",7)[6].replace("page=","");
        }
        if(StringUtils.isNotEmpty(weiye)) {
            for (int x = 1; x <= Integer.parseInt(weiye); x++) {
                if (x >= 2) {
                    doc = get(url + "&page=" + x);
                }
                Elements ele=getElements(doc,"div.wrapper.search-list div.article-item.clearfix");
                if(ele!=null){
                    for(Element e:ele){
                        String logo=getHref(e,"div.item-pic.pull-left a img","src",0);
                        String title=getString(e,"div.item-intro a",0);
                        String lianjie=getHref(e,"div.item-intro a","href",0);

                        ps4.setString(1,cid);
                        ps4.setString(2,logo);
                        ps4.setString(3,title);
                        ps4.setString(4,lianjie);
                        ps4.executeUpdate();
                    }
                }
            }
        }else{
            Elements ele=getElements(doc,"div.wrapper.search-list div.article-item.clearfix");
            if(ele!=null){
                for(Element e:ele){
                    String logo=getHref(e,"div.item-pic.pull-left a img","src",0);
                    String title=getString(e,"div.item-intro a",0);
                    String lianjie=getHref(e,"div.item-intro a","href",0);

                    ps4.setString(1,cid);
                    ps4.setString(2,logo);
                    ps4.setString(3,title);
                    ps4.setString(4,lianjie);
                    ps4.executeUpdate();
                }
            }
        }
    }



    public static Document get(String url) throws IOException, InterruptedException {
        Document doc;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("获取验证码") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("time out");
            }
        }
        return doc;
    }

    class URL{
        BlockingQueue<String> u=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            u.put(key);
        }
        public String qu() throws InterruptedException {
            return u.poll(60, TimeUnit.SECONDS);
        }
    }
}
