package simutong;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/4/14.
 */
public class simutongbinggou {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
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


        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("simutong");
        final MongoCollection collection = db.getCollection("search_binggou");
        final MongoCollection collection2 = db.getCollection("detail_binggou");

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final java.net.Proxy proxy1 = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));


        qingqiu(collection,con,collection2,proxy1);

    }

    public static String[] detail(String url,MongoCollection collection,Proxy proxy) throws IOException {
        String coos = "JSESSIONID=CE2FF71D87DEFCD918F0CD3C1076D313; USER_LOGIN_ID=1477638608479541; USER_LOGIN_NAME_KEY=simutong1%40gaiyachuangxin.cn; IS_CS_KEY=true; USER_LOGIN_NAME=simutong1%40gaiyachuangxin.cn; USER_LOGIN_LANGUAGE=zh_CN; USER_CLIENT_ID=\"\"; firstEnterUrlInSession=http%3A//pe.pedata.cn/getIndexHome.action; VisitorCapacity=1; request_locale=zh_CN";
        String cooos[] = coos.split(";");
        Map map = new HashMap();
        for (int x = 0; x < cooos.length; x++) {
            map.put(cooos[x].split("=", 2)[0], cooos[x].split("=", 2)[1]);
        }

        Document doc=null;
        while (true) {
            doc=Jsoup.connect("http://pe.pedata.cn/" + url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .proxy(proxy)
                    .timeout(100000)
                    .header("Host", "pe.pedata.cn")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Origin", "http://pe.pedata.cn")
                    .cookies(map)
                    .get();
            if(!doc.outerHtml().contains("abuyun")){
                break;
            }
        }
        String mongo_id=storemongo(collection,doc.outerHtml());
        String name=doc.select("div.float_left span.detail_title24").text();
        String key[]=new String[]{mongo_id,name};
        return key;
    }

    public static void getliebiao(List<Double> list,MongoCollection collection,Connection con,MongoCollection collection2,Proxy proxy) throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InterruptedException {
        String coos = "JSESSIONID=E9F227CFBA906C5DC3DC8A026954244E; USER_LOGIN_ID=1477638608479541; USER_LOGIN_NAME_KEY=simutong1%40gaiyachuangxin.cn; IS_CS_KEY=true; USER_LOGIN_NAME=simutong1%40gaiyachuangxin.cn; USER_LOGIN_LANGUAGE=zh_CN; USER_CLIENT_ID=\"\"; firstEnterUrlInSession=http%3A//pe.pedata.cn/getIndexHome.action; VisitorCapacity=1; pageReferrInSession=http%3A//pe.pedata.cn/getListMa.action%3Fparam.showInfo%3Dtrue; request_locale=zh_CN";
        String cooos[] = coos.split(";");
        Map map = new HashMap();
        for (int x = 0; x < cooos.length; x++) {
            map.put(cooos[x].split("=", 2)[0], cooos[x].split("=", 2)[1]);
        }
        System.out.println("begin qingqiu");
        for(int p=1;p<=40;p++) {
            Document doc = null;
            while (true) {
              doc=  Jsoup.connect("http://pe.pedata.cn/getListMa.action")
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(100000)
                        .data("param.quick", "")
                        .data("param.orderBy", "desc")
                        .data("param.orderByField", "ma_eventlist_query_sort_date desc,ma_end_date")
                        .data("param.search_type", "base")
                        .data("param.showInfo", "true")
                        .data("param.search_type_check", "ownerCheck,conditionsUl,")
                        .data("param.isCrossBorder", "")
                        .data("param.ma_money_begin", String.valueOf(list.get(0)))
                        .data("param.ma_money_end", String.valueOf(list.get(1)))
                        .data("param.ma_money_end_search_show", String.valueOf(list.get(1)) + "百万")
                        .data("param.ma_money_begin_search_show", String.valueOf(list.get(0)) + "百万")
                        .data("param.ma_money_base_lable_id", "自定义")
                        .data("param.ma_stake_begin", "")
                        .data("param.ma_stake_end", "")
                        .data("param.maValue_start", "")
                        .data("param.maValue_end", "")
                        .data("param.maValue_base_lable_id", "全部")
                        .data("param.ma_end_date_begin", "")
                        .data("param.ma_end_date_end", "")
                        .data("param.epValue_start", "")
                        .data("param.epValue_end", "")
                        .data("param.epSetupDate_begin", "")
                        .data("param.epSetupDate_end", "")
                        .data("param.isTargetVcPe", "")
                        .data("param.isTargetIpo", "")
                        .data("param.isTargetStateOwned", "")
                        .data("param.epValueAcquirer_start", "")
                        .data("param.epValueAcquirer_end", "")
                        .data("param.epSetupDateAcquirer_begin", "")
                        .data("param.epSetupDateAcquirer_end", "")
                        .data("param.ep_isAcquirerVcPe", "")
                        .data("param.isAcquirerIpo", "")
                        .data("param.isAcquirerStateOwned", "")
                        .data("param.column", "0")
                        .data("param.column", "1")
                        .data("param.column", "2")
                        .data("param.column", "3")
                        .data("param.column", "4")
                        .data("param.column", "5")
                        .data("param.column", "6")
                        .data("param.column", "7")
                        .data("param.column", "8")
                        .data("param.column", "9")
                        .data("param.column", "10")
                        .data("param.currentPage", String.valueOf(p))

                        .header("Host", "pe.pedata.cn")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Origin", "http://pe.pedata.cn")
                        .cookies(map)
                        .post();
                if(!doc.outerHtml().contains("abuyun")){
                    System.out.println(doc.outerHtml());
                    break;
                }
            }

            Elements ele = doc.select("div.leftTableDivID.float_left.search_width20 table.table.table-hover tbody tr");
            String seaid=storemongo(collection2,doc.outerHtml());
            for (Element e : ele) {
                try {
                    System.out.println("begin detail");
                    Thread.sleep(2000);
                    String binggoufang = e.select("td").get(0).select("a").text();
                    String binggou = e.select("td").get(0).select("a").attr("href");
                    String key1[] = detail(binggou, collection, proxy);
                    String binname = key1[1];
                    Thread.sleep(2000);
                    String beibinggoufang = e.select("td").get(1).select("a").text();
                    String beibinggou = e.select("td").get(1).select("a").attr("href");
                    String key2[] = detail(beibinggou, collection, proxy);
                    String beiname = key2[1];
                    String binid = key1[0];
                    String beiid = key2[0];
                    String name[] = new String[]{binggoufang, binname, beibinggoufang, beiname, binid, beiid, seaid};
                    System.out.println("begin store data");
                    storedata(con, name);
                    System.out.println("**********************************************");
                }catch (Exception e1){
                    System.out.println("error detail");
                }
            }
            Thread.sleep(2000);
            System.out.println("--------------------------------------------------------------------------------------------");
        }
    }

    public static void storedata(Connection con,String name[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String insert="insert into simutong_binggouname(shortname_binggou,name_binggou,shortname_beibinggou,name_beibinggou,detail_mongoid_binggou,detail_mongoid_beibinggou,search_mongoid) values(?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(insert);
        ps.setString(1,name[0]);
        ps.setString(2,name[1]);
        ps.setString(3,name[2]);
        ps.setString(4,name[3]);
        ps.setString(5,name[4]);
        ps.setString(6,name[5]);
        ps.setString(7,name[6]);
        ps.executeUpdate();
        System.out.println("insert data success");

    }


    public static String storemongo(MongoCollection collection,String key){
        String uuid= UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("html", key).
                append("time", time);
        collection.insertOne(document);
        return uuid;
    }

    public static void qingqiu(MongoCollection collection,Connection con,MongoCollection collection2,Proxy proxy) throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InterruptedException {
        DecimalFormat df = new DecimalFormat( "0.0 ");
        double a= 3.4;
        List<Double> list=new ArrayList<Double>();
        while(true){
            list.add(a);
            a= Double.parseDouble(df.format(a+0.1));
            list.add(a);
            try {
                System.out.println(a);
                getliebiao(list, collection, con, collection2, proxy);
            }catch (Exception e){
                System.out.println("error");
            }
            list.clear();
            if(a>=400){
                list.add(400.1);
                list.add(100000.1);
                getliebiao(list,collection,con,collection2,proxy);
                break;
            }
        }
    }
}
