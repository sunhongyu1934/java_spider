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
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/13.
 */
public class tianyanchasou {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    final static Integer flag=500;
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
        MongoDatabase db = client.getDatabase("tianYanCha");
        final MongoCollection collection = db.getCollection("sou");

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));




        int x=0;
        for(int q=1;q<=460000;q++) {
            ExecutorService pool= Executors.newFixedThreadPool(20);
            final List<String> listncid = new ArrayList<String>();
            final List<String> listcname = new ArrayList<String>();
            String select="select id,`name` from company_name_unique2 limit "+x+",20";
            PreparedStatement ps=con.prepareStatement(select);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String ncid=rs.getString(rs.findColumn("id"));
                String cname=rs.getString(rs.findColumn("name"));
                listcname.add(cname);
                listncid.add(ncid);
            }
            final java.sql.Connection finalCon = con;
            for (int a = 0; a <= 19; a++) {
                final int finalA = a;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            storedata(collection, finalCon, listncid.get(finalA), listcname.get(finalA), proxy);
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

    public static String get(MongoCollection collection,Connection con,String names,String ncid,Proxy proxy) throws IOException, SQLException {
        Map<String,String> map=new HashMap<String, String>();
        Document doc= null;
        System.out.println("begin request search");
        int flag=0;
        while (true) {
            flag++;
            try {
                String name=names.replace("+", "");
                doc = Jsoup.connect("http://www.tianyancha.com/v2/search/"+name+".json?")
                        .timeout(10000)
                        .proxy(proxy)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
                if(flag==50){
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
            return "0";
        }
        for(Data data:t.data){
            map.put(data.id,data.name.replace("<em>","").replace("</em>","").replaceAll("\\n","").replaceAll("\\s+","").trim());
        }
        System.out.println("begin insert mongo");
        String mongoid=storemongo(collection,t);
        System.out.println("insert mongo success and begin insert/update keycompany");
        storedata(con, map,ncid);
        return mongoid;
    }



    public static void storedata(MongoCollection collection,Connection con,String ncid,String cname,Proxy proxy) throws SQLException, IOException {
        String update="update company_name_unique2 set mongo_id3=? where id=?";
        PreparedStatement psu=con.prepareStatement(update);


        int a=1;

        String cnames=cname.replace("<em>","").replace("</em>","").trim();
        String name= URLEncoder.encode(cnames,"UTF-8");
        System.out.println("begin sou search ncid:"+ncid);
        String mongo_id ="1";
        try {
             mongo_id = get(collection, con, name, ncid, proxy);
        }catch (Exception e){
            System.out.println("error");
        }

        psu.setString(1,mongo_id);
        psu.setString(2,ncid);
        psu.executeUpdate();
        System.out.println("insert/update company success ncid:" + ncid);
        System.out.println("this is     "+a+"   line");
        a++;
        System.out.println("-------------------------------------------------------------------------------------");



    }



    public static void storedata(Connection con,Map<String,String> map,String ncid) throws SQLException {
        String sql="insert into key_company_souname(company_name,tyc_id,ncid) values(?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        for(Map.Entry<String,String> entry:map.entrySet()){
            ps.setString(1,entry.getValue());
            ps.setString(2,entry.getKey());
            ps.setString(3,ncid);
            ps.addBatch();
        }
        ps.executeBatch();
    }




    public static String storemongo(MongoCollection collection,tyc_sou_javabean dw){
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
        return uuid;
    }


}
