package itjuzi;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/11.
 */
public class touzi {
    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.51.120.107:3306/finacing?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        MongoDatabase db = client.getDatabase("itjuzi");
        final MongoCollection collection = db.getCollection("touzi");

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        Document doc=Jsoup.connect("http://www.iqiyi.com/")
                .proxy(proxy)
                .get();

        ExecutorService pool= Executors.newFixedThreadPool(20);
        int x=1;
        for(int p=1;p<=16;p++) {
            final Connection finalCon = con;
            final int finalX = x;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        getgnrzlb(finalCon, collection, proxy, finalX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+147;
        }

        int y=1;
        for(int p=1;p<=4;p++) {
            final Connection finalCon = con;
            final int finalY = y;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        guowai(finalCon, collection, proxy, finalY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            y=y+165;
        }



    }


    public static void getgnrzlb(Connection con,MongoCollection collection,Proxy proxy,int p) throws IOException, SQLException {
        String touzishijianguonei="https://cobra.itjuzi.com/api/get/mobile_investevents?location=in&page=1"; //2348
        String touzishijianxiangqing="https://cobra.itjuzi.com/api/company/9329?user_id=(null)";

        String touzishijianguowai="https://cobra.itjuzi.com/api/get/mobile_investevents?location=out&page=1";//660
        String xiangqing="https://cobra.itjuzi.com/api/company/59692?user_id=(null)";


        for(int x=p;x<=p+147;x++) {
            try {
                Document doc = null;
                System.out.println("begin reuqest search and page:"+x);
                while (true) {
                    try {
                        doc = Jsoup.connect("https://cobra.itjuzi.com/api/get/mobile_investevents?location=in&page=" + x)
                                .userAgent("v4_itjuzi/3.9.2 (iPhone; iOS 10.2.1; Scale/2.00)")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .proxy(proxy)
                                .header("Accept-Encoding", "gzip, deflate")
                                .header("Accept-Language", "zh-Hans-CN;q=1")
                                .header("Authorization", "Bearer r0pFnfLZErvbc8ogZFhdbmyMVssk8a8KnlKyH5UW")
                                .timeout(5000)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out reget");
                    }
                }
                String str = doc.outerHtml();
                Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
                Matcher mat = pattern.matcher(str);


                while (mat.find()) {
                    StringBuffer string = new StringBuffer();
                    int data = Integer.parseInt(mat.group(0).replace("\\u", ""), 16);
                    string.append((char) data);
                    str = str.replace(mat.group(0), string.toString());
                }
                String json = str.replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").trim();
                Gson gson = new Gson();
                rzlbgn r = gson.fromJson(json, rzlbgn.class);
                for (rzgnlbdata da : r.data) {
                    System.out.println("request search success and begin request detail");
                    getgnrzxq(con, collection, da.com_id, proxy);
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }



    }


    public static void guowai(Connection con,MongoCollection collection,Proxy proxy,int p){
        for(int x=p;x<=p+165;x++) {
            try {
                Document doc = null;
                System.out.println("begin reuqest search and is guowai page:"+x);
                while (true) {
                    try {
                        doc = Jsoup.connect("https://cobra.itjuzi.com/api/get/mobile_investevents?location=out&page=" + x)
                                .userAgent("v4_itjuzi/3.9.2 (iPhone; iOS 10.2.1; Scale/2.00)")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .proxy(proxy)
                                .header("Accept-Encoding", "gzip, deflate")
                                .header("Accept-Language", "zh-Hans-CN;q=1")
                                .header("Authorization", "Bearer r0pFnfLZErvbc8ogZFhdbmyMVssk8a8KnlKyH5UW")
                                .timeout(5000)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out reget");
                    }
                }
                String str = doc.outerHtml();
                Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
                Matcher mat = pattern.matcher(str);


                while (mat.find()) {
                    StringBuffer string = new StringBuffer();
                    int data = Integer.parseInt(mat.group(0).replace("\\u", ""), 16);
                    string.append((char) data);
                    str = str.replace(mat.group(0), string.toString());
                }
                String json = str.replace("<html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").replace("</html>", "").trim();
                Gson gson = new Gson();
                rzlbgn r = gson.fromJson(json, rzlbgn.class);
                for (rzgnlbdata da : r.data) {
                    System.out.println("request search success and begin request detail and is guowai");
                    getgnrzxq(con, collection, da.com_id, proxy);
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static void getgnrzxq(Connection con,MongoCollection collection,String company_id,Proxy proxy) throws IOException, SQLException {
        Document doc = null;
        while (true) {
            System.out.println("begin request detail");
            try {
                doc = Jsoup.connect("https://cobra.itjuzi.com/api/company/" + company_id + "?user_id=(null)")
                        .userAgent("v4_itjuzi/3.9.2 (iPhone; iOS 10.2.1; Scale/2.00)")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-Hans-CN;q=1")
                        .header("Authorization", "Bearer r0pFnfLZErvbc8ogZFhdbmyMVssk8a8KnlKyH5UW")
                        .timeout(5000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        String str=doc.outerHtml();
        Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
        Matcher mat= pattern.matcher(str);

        while(mat.find()){
            StringBuffer string = new StringBuffer();
            int data = Integer.parseInt(mat.group(0).replace("\\u",""), 16);
            string.append((char) data);
            str=str.replace(mat.group(0),string.toString());
        }
        String json=str.replace("<html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").replace("</html>","").trim();
        Gson gson=new Gson();
        rzgnxqdata r=gson.fromJson(json,rzgnxqdata.class);
        System.out.println("begin insert mongo");
        String mongo_id=storemongo(collection,r);
        System.out.println("insert mongo success and begin insert data");
        storedata(con,r,mongo_id);
        System.out.println("insert mysql success");
        System.out.println("---------------------------------------------------------------------------");
    }


    public static void storedata(Connection con, rzgnxqdata r,String mongo_id) throws SQLException {
        String insert_com="insert into it_company(cid,`name`,url,juzi_score,address,logo,product_logos,introduction,company_full_name,`scale`,status,found_time,mongo_id,tags,slogan,industry,sub_industry) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psc=con.prepareStatement(insert_com);

        psc.setString(1,r.com_id);
        psc.setString(2,r.com_name+"/"+r.com_sec_name);
        psc.setString(3,r.com_url);
        psc.setString(4,r.com_radar_juziindex);
        psc.setString(5,r.com_prov+r.com_city);
        psc.setString(6,r.com_logo.replace("\\",""));
        StringBuffer str=new StringBuffer();
        for(product s:r.company_with_product){
            str.append(s.com_pro_logo+";");
        }
        String prologo=str.toString();
        psc.setString(7,prologo);
        psc.setString(8,r.com_des);
        psc.setString(9,r.com_registered_name);
        try {
            psc.setString(10, r.company_with_scale.com_scale_name);
        }catch (Exception e){
            psc.setString(10, "");
        }
        try {
            psc.setString(11, r.company_status.com_status_name);
        }catch (Exception e){
            psc.setString(11, "");
        }
        psc.setString(12,r.com_born_year+"-"+r.com_born_month);
        psc.setString(13,mongo_id);
        StringBuffer stag=new StringBuffer();
        for(tag t:r.company_tag){
            stag.append(t.tag_name+";");
        }
        String tags=stag.toString();
        psc.setString(14,tags);
        psc.setString(15,r.com_slogan);
        psc.setString(16,r.company_scope.get(0).cat_name);
        psc.setString(17,r.company_sub_scope.get(0).cat_name);
        psc.executeUpdate();
        ResultSet rs = psc.getGeneratedKeys(); //获取结果
        int cid=0;
        if (rs.next()) {
             cid = rs.getInt(1);//取得ID
        }

        String insert_tor="insert into it_competitor(cid,rcid,`name`,logo_url) values(?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(insert_tor);

        for(jingpin k:r.rand_com){
            pst.setInt(1, cid);
            pst.setString(2,k.com_id);
            pst.setString(3,k.com_name);
            pst.setString(4,k.com_logo_archive.replace("\\",""));
            pst.addBatch();
        }
        pst.executeBatch();


        String insert_fin="insert into it_finacing(cid,fid,`name`,`time`,round,money,vc,`dec`) values(?,?,?,?,?,?,?,?)";
        PreparedStatement psf=con.prepareStatement(insert_fin);

        for(rongzi ro:r.company_invest_events){
            psf.setInt(1,cid);
            psf.setString(2, ro.invse_id);
            psf.setString(3,r.com_name+"/"+r.com_sec_name);
            psf.setString(4,ro.invse_year+"-"+ro.invse_month+"-"+ro.invse_day);
            psf.setString(5,ro.invse_round.invse_round_name);
            String mo1=ro.invse_detail_money;
            String mo2=ro.invse_similar_money.invse_similar_money_name;

            if(mo1.equals("0")){
                psf.setString(6,mo2+ro.invse_currency.invse_currency_name);
            }else{
                psf.setString(6,mo1+ro.invse_currency.invse_currency_name);
            }

            String na1=ro.invse_rel_invst_name;
            if(StringUtils.isNotEmpty(na1)){
                psf.setString(7,na1);
            }else{
                StringBuffer st=new StringBuffer();
                for(tou tt:ro.invse_orags_list){
                    st.append(tt.invst_name+",");
                }
                String name=st.toString();
                psf.setString(7,name);
            }
            psf.setString(8,ro.invse_des);
            psf.addBatch();
        }
        psf.executeBatch();


        String insert_found="insert into it_founders(cid,`name`,position,introduction,weibo) values(?,?,?,?,?)";
        PreparedStatement psfo=con.prepareStatement(insert_found);

        for(tuandui t:r.company_with_per){
            psfo.setInt(1,cid);
            psfo.setString(2,t.per_name);
            psfo.setString(3,t.des);
            psfo.setString(4,t.per_des);
            psfo.setString(5,t.per_weibo);
            psfo.addBatch();
        }
        psfo.executeBatch();

        String insert_new="insert into it_news(cid,title,time,type,url) values(?,?,?,?,?)";
        PreparedStatement psn=con.prepareStatement(insert_new);

        for(news n:r.company_news){
            psn.setInt(1,cid);
            psn.setString(2,n.com_new_name);
            psn.setString(3,n.com_new_year+"-"+n.com_new_month+"-"+n.com_new_day);
            psn.setString(4,n.company_news_type.com_new_type_name);
            psn.setString(5,n.com_new_url);
            psn.addBatch();
        }
        psn.executeBatch();

        String insert_pro="insert into it_product(cid,`name`,type,introduction,url) values(?,?,?,?,?)";
        PreparedStatement pspro=con.prepareStatement(insert_pro);

        for(product p:r.company_with_product){
            pspro.setInt(1,cid);
            pspro.setString(2,p.com_pro_name);
            pspro.setString(3,p.product_type_name.com_pro_type_name);
            pspro.setString(4,p.com_pro_detail);
            pspro.setString(5,p.com_pro_url);
            pspro.addBatch();
        }
        pspro.executeBatch();

        String insert_map="insert into it_roadmap(cid,title,time) values(?,?,?)";
        PreparedStatement psm=con.prepareStatement(insert_map);

        for(stone st:r.company_mile_stones){
            psm.setInt(1,cid);
            psm.setString(2,st.com_mil_detail);
            psm.setString(3,st.com_mil_year+"-"+st.com_mil_month);
            psm.addBatch();
        }
        psm.executeBatch();
    }

    public static String storemongo(MongoCollection collection, rzgnxqdata dw){
        Gson gson=new Gson();
        String uuid= UUID.randomUUID().toString();
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(dw));
        org.bson.Document document = new org.bson.Document("_id", uuid).
                append("json", dbObject).
                append("time", time);
        collection.insertOne(document);
        return uuid;
    }

    public static Map<String,String> jiexi() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/data1/spider/java_spider/src/itjuzi/itjuzi.xml")));
        Element root=dom.getRootElement();
        Element table=root.element("table");
        String shouquan=table.element("shouquan").getText();
        String page=table.element("page").getText();
        Map<String,String> map=new HashMap<String,String>();
        map.put(shouquan,page);
        return map;
    }

}
