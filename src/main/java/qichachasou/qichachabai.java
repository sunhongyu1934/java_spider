package qichachasou;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */
public class qichachabai {

    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("company");
        MongoCollection collection = db.getCollection("qichacha_search");
        for(int x=8000;x<=16500;x++) {
            try {
                sou(collection, x);
            }catch (Exception e){
               e.printStackTrace();
            }
            System.out.println(x);
            System.out.println("-------------------------------------------------------");
        }
    }

    public static void sou(MongoCollection collection,int page) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        FindIterable<Document>  iterable = collection.find().limit(1).skip((page - 1) * 5);
        MongoCursor<Document> cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document o= cursor.tryNext();
            String  obj=o.toJson();
            System.out.println(obj);
            data(obj);
        }
    }
    public static void data(String obj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

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

        Gson gson=new Gson();
        String sql="insert into qichacha_search(`KeyNo`,`Name`,`No`,`BelongOrg`,`OperName`,`StartDate`,`EndDate`,`Status`,`Province`,`UpdatedDate`,`ShortStatus`,`CreditCode`,`RegistCapi`,`EconKind`,`Address`,`IndustryCode`,`SubIndustryCode`,`Industry`,`SubIndustry`,`Scope`,`ContactNumber`,`Email`,`WebSite`,`ImageUrl`,`OrgNo`,`EnglishName`,`Product`,`Type`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);


        qichachamongo q=gson.fromJson(obj,qichachamongo.class);
        if(q.search_json.result.Result!=null&&q.search_json.result.Result.size()>0){
            List<neirong> list=q.search_json.result.Result;
            for(int a=0;a<list.size();a++){
                String KeyNo = list.get(a).KeyNo;
                String Name = list.get(a).Name;
                String No = list.get(a).No;
                String BelongOrg = list.get(a).BelongOrg;
                String OperName = list.get(a).OperName;
                String StartDate = list.get(a).StartDate;
                String EndDate = list.get(a).EndDate;
                String Status = list.get(a).Status;
                String Province = list.get(a).Province;
                String UpdatedDate = list.get(a).UpdatedDate;
                String ShortStatus = list.get(a).ShortStatus;
                String CreditCode = list.get(a).CreditCode;
                String RegistCapi = list.get(a).RegistCapi;
                String EconKind = list.get(a).EconKind;
                String Address = list.get(a).Address;
                String Scope = list.get(a).Scope;
                String ContactNumber = list.get(a).ContactNumber;
                String Email = list.get(a).Email;
                String WebSite = list.get(a).WebSite;
                String ImageUrl = list.get(a).ImageUrl;
                String OrgNo = list.get(a).OrgNo;
                String EnglishName = list.get(a).EnglishName;
                String Product = list.get(a).Product;
                String Type = list.get(a).Type;
                String IndustryCode = null;
                String SubIndustry =null;
                String Industry=null;
                String SubIndustryCode=null;
                try {
                    IndustryCode = list.get(a).Industry.IndustryCode;
                }catch (Exception e){
                    IndustryCode=null;
                }
                try {
                    SubIndustryCode = list.get(a).Industry.SubIndustryCode;
                }catch (Exception e){
                    SubIndustryCode=null;
                }
                try {
                    Industry = list.get(a).Industry.Industry;
                }catch (Exception e){
                    Industry=null;
                }
                try {
                    SubIndustry = list.get(a).Industry.SubIndustry;
                }catch (Exception e){
                    SubIndustry=null;
                }

                ps.setString(1,KeyNo);
                ps.setString(2,Name);
                ps.setString(3,No);
                ps.setString(4,BelongOrg);
                ps.setString(5,OperName);
                ps.setString(6,StartDate);
                ps.setString(7,EndDate);
                ps.setString(8,Status);
                ps.setString(9,Province);
                ps.setString(10,UpdatedDate);
                ps.setString(11,ShortStatus);
                ps.setString(12,CreditCode);
                ps.setString(13,RegistCapi);
                ps.setString(14,EconKind);
                ps.setString(15,Address);
                ps.setString(16,IndustryCode);
                ps.setString(17,SubIndustryCode);
                ps.setString(18,Industry);
                ps.setString(19,SubIndustry);
                ps.setString(20,Scope);
                ps.setString(21,ContactNumber);
                ps.setString(22,Email);
                ps.setString(23,WebSite);
                ps.setString(24,ImageUrl);
                ps.setString(25,OrgNo);
                ps.setString(26,EnglishName);
                ps.setString(27,Product);
                ps.setString(28,Type);
                ps.executeUpdate();
            }
        }


    }

}
