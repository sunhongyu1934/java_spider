package qixinbao;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/17.
 */
public class qixinbaosousuo {
    // 代理隧道验证信息
    final static String ProxyUser = "H0X3GMU679V4173D";
    final static String ProxyPass = "BE36E279889A6886";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.51.120.107:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        ServerAddress serverAddress = new ServerAddress("10.44.51.90",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("qixinbao");
        final MongoCollection collection = db.getCollection("sousuo");


        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));





        final List<String> listid = new ArrayList<String>();
        final List<String> listname = new ArrayList<String>();
        int k=0;
        for(int p=1;p<=10;p++) {
            String select = "select id,name from company_name_unique2 limit "+k+", 1000000";
            PreparedStatement pss = con.prepareStatement(select);
            ResultSet rs = pss.executeQuery();
            while (rs.next()) {
                String id = rs.getString(rs.findColumn("id"));
                String name = rs.getString(rs.findColumn("name"));
                listname.add(name);
                listid.add(id);
                if (listid.size() == 20000) {
                    ExecutorService pool = Executors.newFixedThreadPool(20);
                    final java.sql.Connection finalCon = con;
                    int x = 0;
                    for (int a = 0; a <= 19; a++) {
                        final int finalX = x;
                        pool.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    data(finalCon, proxy, collection, listname.subList(finalX, finalX + 1000), listid.subList(finalX, finalX + 1000));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        x = x + 1000;
                    }
                    pool.shutdown();
                    while (true) {
                        if (pool.isTerminated()) {
                            break;
                        }
                        Thread.sleep(500);
                    }
                    listname.clear();
                    listid.clear();
                }
            }
            k=k+1000000;
        }


    }

    public static Map<String,String> sou(String sous,Proxy proxy,MongoCollection collection) throws IOException {
        String sou= URLEncoder.encode(sous,"UTF-8");
        Document doc= null;
        System.out.println("begin request");
        while(true) {
            try {
                doc = Jsoup.connect("http://www.qixin.com/search?key=" + sou + "&type=enterprise&method=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(2000)
                        .get();
                if(StringUtils.isNotEmpty(doc.outerHtml())&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("完成上面的验证")){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("request success and begin store to mongo");
        Map<String,String> map=new HashMap<String, String>();
        String mongo_id=mongo(doc.outerHtml(),collection);
        map.put("mongo_id",mongo_id);
        Elements ele=doc.select("div.search-list-bg div.search-ent-row.clearfix");
        for(Element e:ele){
            String name=e.select("a.search-result-company-name").text().replace("<em>","").replace("</em>","").replace("{{el.name | html}}", "");
            String qid=e.select("div.is-follow.no-follow").attr("ms-attr-eid").replace("'", "");
            if(StringUtils.isNotEmpty(name)){
                map.put(qid,name);
            }
        }
        Elements ele2=doc.select("div.rencent-claim-lists div.rencent-claim-item a");
        for(Element e:ele2){
            String name=e.select("div.recent-claim-item-name").text();
            String qid=e.attr("href").replace("/company/", "");
            map.put(qid,name);
        }
        return map;
    }

    public static void data(Connection con,Proxy proxy,MongoCollection collection,List<String> namel,List<String> idl) throws SQLException, IOException {
        String update="update company_name_unique2 set mongo_id2=? where id=?";
        PreparedStatement ps=con.prepareStatement(update);

        String insert="insert into qixin_id_test(`name`,mongo_id,qixin_id) values(?,?,?)";
        PreparedStatement psi=con.prepareStatement(insert);


        String insert2="insert into qixin_id_test_er(`name`,mongo_id,qixin_id,yuanid) values(?,?,?,?)";
        PreparedStatement psi2=con.prepareStatement(insert2);
        for(int x=0;x<idl.size();x++) {
            Map<String, String> map = sou(namel.get(x), proxy, collection);
            String mongo_id = map.get("mongo_id");
            System.out.println("store mongo succes and begin insert data");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String company_name = entry.getValue();
                String company_id = entry.getKey();
                if (!company_id.equals("mongo_id")) {
                    System.out.println(company_name);
                    System.out.println(company_id);
                    psi.setString(1, company_name);
                    psi.setString(2, mongo_id);
                    psi.setString(3, company_id);
                    try {
                        psi.executeUpdate();
                    } catch (Exception e) {
                        psi2.setString(1, company_name);
                        psi2.setString(2, mongo_id);
                        psi2.setString(3, company_id);
                        psi2.setString(4,idl.get(x));
                        psi2.executeUpdate();
                        System.out.println("chongfu");
                    }
                }
            }

            ps.setString(1, mongo_id);
            ps.setString(2, idl.get(x));
            ps.executeUpdate();
            System.out.println("insert data success");
            System.out.println("---------------------------------------------------------------------");
        }
    }

    public static String mongo(String html,MongoCollection collection){
        String uuid= UUID.randomUUID().toString();
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("html", html).
                append("time", time);
        collection.insertOne(document);
        return uuid;
    }

}
