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
import tianyancha.gudongbean.Resultgudong;
import tianyancha.gudongbean.gudongbean;

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
public class tyckeycompany {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

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
        MongoDatabase db = client.getDatabase("tianYanCha");
        final MongoCollection collection = db.getCollection("key_souname");

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));




        int x=0;
        for(int q=1;q<=3867;q++) {
            ExecutorService pool= Executors.newFixedThreadPool(15);
            final List<String> listncid = new ArrayList<String>();
            final List<String> listcname = new ArrayList<String>();
            String select="select ncid,cname from key_company where cname like '%公司%' and ncid>'124024' limit "+x+",15";
            PreparedStatement ps=con.prepareStatement(select);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String ncid=rs.getString(rs.findColumn("ncid"));
                String cname=rs.getString(rs.findColumn("cname"));
                listcname.add(cname);
                listncid.add(ncid);
            }
            final java.sql.Connection finalCon = con;
            for (int a = 0; a <= 14; a++) {
                final int finalA = a;
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            storedata(collection,finalCon,listncid.get(finalA),listcname.get(finalA),proxy);
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
            x = x + 15;
        }


    }

    public static String get(MongoCollection collection,Connection con,String name,String ncid,Proxy proxy) throws IOException, SQLException {
        Map<String,String> map=new HashMap<String, String>();
        Document doc= null;
        System.out.println("begin request search");
        while (true) {
            try {
                doc = Jsoup.connect("http://www.tianyancha.com/v2/search/" + name + ".json?")
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
            return "0";
        }
        for(Data data:t.data){
            map.put(data.id,data.name.replace("<em>","").replace("</em>","").replaceAll("\\n","").replaceAll("\\s+","").trim());
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            List<touzibean> list=touzi(entry.getKey(),proxy);
            if(list!=null&&list.size()>0) {
                System.out.println("begin insert touzi");
                storedata(con, list,ncid);
                System.out.println("insert touzi success");
            }else{
                System.out.println("touzi is null");
            }
            List<gudongbean> list2=gudong(entry.getKey(),proxy);
            if(list2!=null&&list2.size()>0) {
                System.out.println("begin insert gudong");
                storedata(con, list2,ncid);
                System.out.println("insert gudong success");
            }else{
                System.out.println("gudong is null");
            }
            System.out.println("**************************************");
            break;
        }
        System.out.println("begin insert mongo");
        String mongoid=storemongo(collection,t);
        System.out.println("insert mongo success and begin insert/update keycompany");
        storedata(con, map,ncid);
        return mongoid;
    }


    public static List<touzibean> touzi(String tyc_id,Proxy proxy) throws IOException {
        List<touzibean> list=new ArrayList<touzibean>();
        System.out.println("search request success and begin request touzi");
        for(int x=1;x<=5;x++) {
            Document doc = null;
            while (true) {
                doc = Jsoup.connect("http://www.tianyancha.com/expanse/inverst.json?id="+tyc_id+"&ps=20&pn="+x)
                         .proxy(proxy)
                        .ignoreContentType(true)
                        .timeout(100000)
                        .ignoreHttpErrors(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }
            String json=doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim();
            Gson gson=new Gson();
            touzibean t=gson.fromJson(json,touzibean.class);
            if((t.message!=null&&(t.message.equals("无数据")||t.message.equals("系统异常")))||(t.data.result==null||t.data.result.size()==0)){
                break;
            }
            System.out.println("touzi request success");
            list.add(t);
        }
        return list;
    }


    public static List<gudongbean> gudong(String tyc_id,Proxy proxy) throws IOException {
        List<gudongbean> list=new ArrayList<gudongbean>();
        System.out.println("begin request gudong");
        for(int x=1;x<=5;x++) {
            Document doc = null;
            while (true) {
                doc = Jsoup.connect("http://www.tianyancha.com/expanse/holder.json?id="+tyc_id+"&ps=20&pn="+x)
                        .proxy(proxy)
                        .ignoreContentType(true)
                       .timeout(100000)
                        .ignoreHttpErrors(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }
            String json=doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim();
            Gson gson=new Gson();
            gudongbean t=gson.fromJson(json,gudongbean.class);
            if((t.message!=null&&(t.message.equals("无数据")||t.message.equals("系统异常")))||(t.data.result==null||t.data.result.size()==0)){
                break;
            }
            System.out.println("gudong request success");
            list.add(t);
        }
        return list;
    }
    public static void storedata(MongoCollection collection,Connection con,String ncid,String cname,Proxy proxy) throws SQLException, IOException {
        String update="update key_company set mongo_id=? where ncid=?";
        PreparedStatement psu=con.prepareStatement(update);


        int a=1;

        String name= URLEncoder.encode(cname,"UTF-8");
        System.out.println("begin sou search ncid:"+ncid);
        String mongo_id=get(collection,con,name,ncid,proxy);


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


    public static void storedata(Connection con,List<?> list,String ncid) throws SQLException {
        String insert1="insert into tyc_key_touzi(business_scope,percent,regStatus,legalPersonName,`type`,pencertileScore,amount,tyc_id,category,regCapital,company_name,base,ncid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psi1=con.prepareStatement(insert1);

        String insert2="insert into tyc_key_gudong(amount,tyc_id,`type`,amomon,percent,`name`,ncid) values(?,?,?,?,?,?,?)";
        PreparedStatement psi2=con.prepareStatement(insert2);

        for(Object o:list){
            if(o instanceof touzibean){
                touzibean t=(touzibean) o;
                for(Resulttouzi r:t.data.result){
                    psi1.setString(1,r.business_scope);
                    psi1.setString(2,r.percent);
                    psi1.setString(3,r.regStatus);
                    psi1.setString(4,r.legalPersonName);
                    psi1.setString(5,r.type);
                    psi1.setString(6,r.pencertileScore);
                    psi1.setString(7,r.amount);
                    psi1.setString(8,r.id);
                    psi1.setString(9,r.category);
                    psi1.setString(10,r.regCapital);
                    psi1.setString(11,r.name);
                    psi1.setString(12,r.base);
                    psi1.setString(13,ncid);
                    psi1.addBatch();
                }
                psi1.executeBatch();
            }else{
                gudongbean g=(gudongbean) o;
                for(Resultgudong r:g.data.result){
                    psi2.setString(1,r.amount);
                    psi2.setString(2,r.id);
                    psi2.setString(3,r.type);
                    if(r.capital!=null&&r.capital.size()>0){
                        psi2.setString(4,r.capital.get(0).amomon);
                        psi2.setString(5,r.capital.get(0).percent);
                    }else{
                        psi2.setString(4,"");
                        psi2.setString(5,"");
                    }
                    psi2.setString(6,r.name);
                    psi2.setString(7,ncid);
                    psi2.addBatch();
                }
                psi2.executeBatch();
            }
        }
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
