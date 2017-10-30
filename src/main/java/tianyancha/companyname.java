package tianyancha;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/10.
 */
public class companyname {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;


    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InterruptedException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

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
        ServerAddress serverAddress = new ServerAddress("10.44.51.90",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("tianYanCha");
        final MongoCollection collection = db.getCollection("company_name");


        int x=0;
        for(int q=1;q<=348797;q++) {
            ExecutorService pool= Executors.newFixedThreadPool(20);
            final List<String> listurl = new ArrayList<String>();
            final List<Integer> listid = new ArrayList<Integer>();
            String select = "select url,id from tyc_url where id>9820 limit " + x + ",20";
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String urls = rs.getString(rs.findColumn("url"));
                int id = rs.getInt(rs.findColumn("id"));
                listurl.add(urls);
                listid.add(id);
            }
            final java.sql.Connection finalCon = con;
            for (int a = 0; a <= 19; a++) {
                final int finalA = a;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data(proxy, listurl.get(finalA), listid.get(finalA), finalCon,collection);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            pool.shutdown();
            while (true) {
                if (pool.isTerminated()) {
                    break;
                }
                Thread.sleep(500);
            }
            x = x + 20;
        }
    }

    public static void data(Proxy proxy, String urls, int id, Connection con,MongoCollection collection) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {


        int p=0;
        String insert="insert into tyc_name(`tyc_id`,`tyc_name`) values(?,?)";
        PreparedStatement psi=con.prepareStatement(insert);


        String update="update tyc_url set flag=? where id=?";
        PreparedStatement psu=con.prepareStatement(update);

        System.out.println("begin get name id:"+id);
        Map<String, String> map = get(urls, proxy,collection);
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                psi.setString(1, entry.getKey());
                psi.setString(2, entry.getValue());
                psi.addBatch();
            }
            psi.executeBatch();
            System.out.println("insert success id:"+id);

            psu.setInt(1,1);
            psu.setInt(2, id);
            psu.executeUpdate();
        }else{
            psu.setInt(1,0);
            psu.setInt(2, id);
            psu.executeUpdate();
        }

        p++;
        System.out.println(p);
        System.out.println("-----------------------------------------------------");


    }


    public static Map<String,String> get(String urls,Proxy proxy,MongoCollection collection) throws IOException {
        Map<String,String> map=new HashMap<String, String>();
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(urls)
                        .timeout(100000)
                        .proxy(proxy)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("reget");
            }
        }

        String json=doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim();
        Gson gson=new Gson();
        tyc_sou_javabean t=gson.fromJson(json,tyc_sou_javabean.class);
        if(StringUtils.isNotEmpty(t.message)){
            return map;
        }
        storemongo(t,collection);
        for(Data data:t.data){
            map.put(data.id,data.name);
        }

        for(int x=2;x<=50;x++){
            String url2=urls.split(".json\\?",2)[0]+".json?&pn="+x+urls.split(".json\\?",2)[1];
            Document doc2= null;
            while (true) {
                try {
                    doc2 = Jsoup.connect(url2)
                            .timeout(100000)
                            .proxy(proxy)
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .get();
                    if (!doc2.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc2.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                        break;
                    }
                }catch (Exception e){
                    System.out.println("reget");
                }
            }

            String json2=doc2.outerHtml().replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim();
            Gson gson2=new Gson();
            tyc_sou_javabean t2=gson2.fromJson(json2,tyc_sou_javabean.class);
            if(StringUtils.isNotEmpty(t2.message)){
                break;
            }
            storemongo(t2,collection);
            for(Data data:t2.data){
                map.put(data.id,data.name);
            }
        }
        return map;
    }

    public static void storemongo(tyc_sou_javabean dw,MongoCollection collection){


        Gson gson=new Gson();
        String uuid= UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(dw));
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("json", dbObject).
                append("time", time);
        collection.insertOne(document);

    }

}
