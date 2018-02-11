package gongshi;

import com.google.gson.*;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/12.
 */
public class jichu {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("10.44.51.90",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("gongshi");
        final MongoCollection collection = db.getCollection("search");
        final MongoCollection collection2 = db.getCollection("detail");





        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final java.net.Proxy proxy1 = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));



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



        int x=0;
        for(int q=1;q<=200000;q++) {
            ExecutorService pool= Executors.newFixedThreadPool(5);
            final List<String> listname = new ArrayList<String>();
            final List<String> listid = new ArrayList<String>();
            String select="select `id`,`name` from company_name_unique where id>74 limit "+x+",5";
            PreparedStatement ps=con.prepareStatement(select);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                String id=rs.getString(rs.findColumn("id"));
                String name=rs.getString(rs.findColumn("name")).replaceAll("[^\\u4e00-\\u9fa5]","").trim().replace("吊销", "");
                listname.add(name);
                listid.add(id);
            }
            final java.sql.Connection finalCon = con;
            for (int a = 0; a <= 4; a++) {
                final int finalA = a;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data(finalCon, listname.get(finalA), listid.get(finalA),collection, collection2, proxy1);
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
            x = x + 5;
        }
    }

    public static Map<String,String> get(MongoCollection collection,String key,Proxy proxy) throws IOException {
        String json=null;
        Connection.Response doc= null;
        Map<String,String> map=new HashMap<String, String>();
        System.out.println("begin requst search");
        while(true) {
            doc= Jsoup.connect("http://yd.gsxt.gov.cn/QuerySummary")
                    //.header("Hose", "yd.gsxt.gov.cn")
                    //.header("Origin", "file://")
                    .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .data("pageSize", "10")
                    .data("userID", "id001")
                    .data("userIP", "192.123.123.13")
                    .data("keywords", key)
                    .data("topic", "1")
                            //.data("nodenum","110000")
                    .data("mobileAction", "entSearch")
                    .data("pageNum", "1")
                    .proxy(proxy)
                    .timeout(500000)
                    .method(Connection.Method.POST)
                            // .data("pripid","E4AACF8882E14EFC2F0428CB18A1E7D1594EF1C54A2F1309CC84DF564601C0D817932D83D33C5ED3AD5FC031969B48ABADD2112B972C78637859DAFF901E79256EE5FF8930DA71D407BE1E05A3106893")
                            //.data("enttype","6150")
                            //.data("nodenum","310000")
                            //.data("mobileAction","entDetail")
                            //.data("userID","id001")
                            //  .data("userIP", "192.123.123.13")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .execute();
            if(StringUtils.isNotEmpty(doc.body())&&!doc.body().contains("abuyun")){
                System.out.println("requst search success");
                json=doc.body();
                System.out.println("begin search to mongo");
                String mongo_id1=storemong1(collection,json);
                System.out.println("search mongo success");
                map.put(mongo_id1,json);
                break;
            }
        }
        return map;

    }

    public static void getdetail(List<String> listlie,Proxy proxy,java.sql.Connection con,MongoCollection collection) throws IOException, SQLException {
        String json2=null;
        Connection.Response doc2= null;
        String mongo_id2=null;
        int a=1;
        for(String s:listlie) {
            System.out.println("begin requst detail");
            while (true) {
                doc2 = Jsoup.connect("http://yd.gsxt.gov.cn/QueryBusiLice")
                        .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                // .data("pageSize", "10")
                        .data("userID", "id001")
                        .data("userIP", "192.123.123.13")
                                // .data("keywords", "百度")
                                // .data("topic", "1")
                        .data("nodenum",s.split(",",4)[2])
                                //.data("mobileAction", "entSearch")
                                //.data("pageNum", "1")
                        .proxy(proxy)
                        .timeout(500000)
                        .method(Connection.Method.POST)
                        .data("pripid",s.split(",",4)[0])
                        .data("enttype",s.split(",",4)[2])
                        .data("mobileAction", "entDetail")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .execute();
                if (StringUtils.isNotEmpty(doc2.body())&&!doc2.body().contains("abuyun")) {
                    System.out.println("request detail success");
                    json2 = doc2.body().replace("{}","\"\"").replace("\"\"\"\"","\"\"");
                    if(json2.length()>5) {
                        Gson gson = new Gson();
                        xiangqing x = null;
                        xiangqing2 x2 = null;
                        try {
                            x = gson.fromJson(json2, xiangqing.class);
                            System.out.println("begin detail to mongo");
                            mongo_id2 = storemong2(collection, x);
                            System.out.println("detail to mongo success and begin to data");
                            detaildata(con, x, mongo_id2);
                            System.out.println("detail insert data success");
                        } catch (Exception e) {
                            x2 = gson.fromJson(json2, xiangqing2.class);
                            System.out.println("begin detail to mongo");
                            mongo_id2 = storemong2(collection, x2);
                            System.out.println("detail to mongo success and begin to data");
                            detaildata(con, x2, mongo_id2);
                            System.out.println("detail insert data success");
                        }
                    }else{
                        nudetaildata(con,s.split(",",4)[3]);
                        System.out.println("this detail is null");
                    }
                    break;
                }
            }
            System.out.println("this detail is run "+a+"    line");
            a++;
            System.out.println("*******************************************");
        }
    }


    public static List<String> parsrliebiao(String json){
        List<String> list=new ArrayList<String>();
        Gson gson=new Gson();
        JsonParser parser = new JsonParser();

//通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
        JsonElement el = parser.parse(json);

//把JsonElement对象转换成JsonObject
        JsonObject jsonObj = null;
        if(el.isJsonObject()){
            jsonObj = el.getAsJsonObject();
        }

//把JsonElement对象转换成JsonArray
        JsonArray jsonArray = null;
        if(el.isJsonArray()){
            jsonArray = el.getAsJsonArray();
        }

        //遍历JsonArray对象
        liebiaonei field = null;
        Iterator it = jsonArray.iterator();
        while(it.hasNext()){
            JsonElement e = (JsonElement)it.next();
//JsonElement转换为JavaBean对象
            field = gson.fromJson(e, liebiaonei.class);
            list.add(field.PRIPID+","+field.ENTTYPE+","+field.S_EXT_NODENUM+","+field.ENTNAME.replaceAll("[^\u4e00-\u9fa5]","").trim());
        }
        return list;
    }


    public static String storemong2(MongoCollection collection,Object o){
        Gson gson=new Gson();
        String uuid= UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);

        if(o instanceof xiangqing){
            xiangqing x=(xiangqing) o;
            DBObject dbObject = (DBObject) JSON.parse(gson.toJson(x));
            org.bson.Document document = new org.bson.Document("_id", uuid).
                    append("json", dbObject).
                    append("time", time);
            collection.insertOne(document);
        }else{
            xiangqing2 x2=(xiangqing2) o;
            DBObject dbObject = (DBObject) JSON.parse(gson.toJson(x2));
            org.bson.Document document = new org.bson.Document("_id", uuid).
                    append("json", dbObject).
                    append("time", time);
            collection.insertOne(document);
        }


        return uuid;

    }

    public static String storemong1(MongoCollection collection,Object o){
        String uuid= UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String json=(String) o;
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("json", json).
                append("time", time);
        collection.insertOne(document);

        return uuid;

    }

    public static void data(java.sql.Connection con,String name,String id,MongoCollection collection,MongoCollection collection2,Proxy proxy) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {

        String update="update company_name_unique set mongo_id2=? where id=?";
        PreparedStatement psu=con.prepareStatement(update);


        int a=1;
        String mongo_id1=null;
        List<String> listlie=null;
        try {
            System.out.println("begin search id:"+id);
            Map<String, String> map1 = get(collection, name, proxy);
            System.out.println("get search success id:"+id);
            for (Map.Entry<String, String> entry : map1.entrySet()) {
                mongo_id1 = entry.getKey();
                listlie = parsrliebiao(entry.getValue());
            }
            System.out.println("begin detail id:"+id);
            getdetail(listlie, proxy, con, collection2);
            System.out.println("detaul success id:"+id);

            psu.setString(1, mongo_id1);
            psu.setString(2, id);
            psu.executeUpdate();

            System.out.println("this data is run "+a+"  line");
            a++;
            System.out.println("-------------------------------------------------------------------------------------");
        }catch (Exception e){
            e.printStackTrace();
            psu.setString(1, "1");
            psu.setString(2, id);
            psu.executeUpdate();
        }


    }

    public static void nudetaildata(java.sql.Connection con,String name) throws SQLException {
        String insert2="insert into gongshi_search(company_name,mongo_id) values(?,?)";
        PreparedStatement psi=con.prepareStatement(insert2);

        psi.setString(1,name);
        psi.setString(2,"0");
        psi.executeUpdate();
    }

    public static void detaildata(java.sql.Connection con,Object o,String mongo_id) throws SQLException {
        String insert="insert into company_detail(APPRDATE,DOM,ENTNAME,ENTTYPE_CN,ESTDATE,`NAME`,OPFROM,OPSCOPE,OPTO,REGCAP,REGCAPCUR,REGCAPCUR_CN,REGNO,REGORG_CN,REGSTATE_CN,UNISCID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(insert);

        String insert2="insert into gongshi_search(company_name,mongo_id) values(?,?)";
        PreparedStatement psi=con.prepareStatement(insert2);
        if(o instanceof xiangqing){
            /*xiangqing x=(xiangqing) o;
            ps.setString(1,x.APPRDATE.replace("{}",""));
            ps.setString(2,x.DOM.replace("{}",""));
            ps.setString(3,x.ENTNAME.replace("{}",""));
            ps.setString(4,x.ENTTYPE_CN.replace("{}",""));
            ps.setString(5,x.ESTDATE.replace("{}",""));
            ps.setString(6,x.NAME.replace("{}",""));
            ps.setString(7,x.OPFROM.replace("{}",""));
            ps.setString(8,x.OPSCOPE.replace("{}",""));
            ps.setString(9,x.OPTO.replace("{}",""));
            ps.setString(10,x.REGCAP.replace("{}",""));
            ps.setString(11,x.REGCAPCUR.replace("{}",""));
            ps.setString(12,x.REGCAPCUR_CN.replace("{}",""));
            ps.setString(13,x.REGNO.replace("{}",""));
            ps.setString(14,x.REGORG_CN.replace("{}",""));
            ps.setString(15,x.REGSTATE_CN.replace("{}",""));
            ps.setString(16,x.UNISCID.replace("{}",""));
            ps.executeUpdate();

            psi.setString(1,x.ENTNAME);
            psi.setString(2,mongo_id);
            psi.executeUpdate();*/
        }else{
            xiangqing2 x=(xiangqing2) o;
            ps.setString(1, x.APPRDATE.replace("{}",""));
            ps.setString(2,x.DOM.replace("{}",""));
            ps.setString(3,x.ENTNAME.replace("{}",""));
            ps.setString(4,x.ENTTYPE_CN.replace("{}",""));
            ps.setString(5,x.ESTDATE.replace("{}",""));
            ps.setString(6,x.NAME.replace("{}",""));
            ps.setString(7,x.OPFROM.replace("{}",""));
            ps.setString(8,x.OPSCOPE.replace("{}",""));
            ps.setString(9,x.OPTO.replace("{}",""));
            ps.setString(10,x.REGCAP.replace("{}",""));
            ps.setString(11,"");
            ps.setString(12,"");
            ps.setString(13,x.REGNO.replace("{}",""));
            ps.setString(14,x.REGORG_CN.replace("{}",""));
            ps.setString(15,x.REGSTATE_CN.replace("{}",""));
            ps.setString(16,x.UNISCID.replace("{}",""));
            ps.executeUpdate();

            psi.setString(1,x.ENTNAME);
            psi.setString(2,mongo_id);
            psi.executeUpdate();
        }





    }




}
