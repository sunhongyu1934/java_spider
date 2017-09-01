package linshi_spider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import linshi_spider.bean.Chaibean;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/6/12.
 */
public class Chaibiao {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Chaibiao ch=new Chaibiao();
        final Cang ca=ch.new Cang();

        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Jiexi(finalCon,ca);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=10;a++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Jiexi(ca,finalCon);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }



    }

    public static void Jiexi(Connection con,Cang ca) throws SQLException, InterruptedException {
        String sql="select t_id,leading_member,shareholder_Information,outbound_investment from tyc_information";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String zhuyaochengyuan=rs.getString(rs.findColumn("leading_member"));
            String gudongxinxi=rs.getString(rs.findColumn("shareholder_Information"));;
            String duiwaitouzi=rs.getString(rs.findColumn("outbound_investment"));
            String tid=rs.getString(rs.findColumn("t_id"));
            ca.fang(new String[]{zhuyaochengyuan,gudongxinxi,duiwaitouzi,tid});
        }
    }

    public static void Jiexi(Cang ca,Connection con) throws InterruptedException, SQLException {
        while (true){
            String[] keys=ca.qu();
            if(keys!=null){
                JsonParser jsonParser=new JsonParser();
                JsonArray jsonArray1=null;
                JsonArray jsonArray2=null;
                JsonArray jsonArray3=null;


                if(StringUtils.isNotEmpty(keys[0])) {
                    JsonElement jsonElement1 = jsonParser.parse(keys[0]);
                    if(jsonElement1.isJsonArray()){
                        jsonArray1= jsonElement1.getAsJsonArray();
                    }
                }
                if(StringUtils.isNotEmpty(keys[1])) {
                    JsonElement jsonElement2 = jsonParser.parse(keys[1]);
                    if(jsonElement2.isJsonArray()){
                        jsonArray2= jsonElement2.getAsJsonArray();
                    }
                }
                if(StringUtils.isNotEmpty(keys[2])) {
                    JsonElement jsonElement3 = jsonParser.parse(keys[2]);
                    if(jsonElement3.isJsonArray()){
                        jsonArray3= jsonElement3.getAsJsonArray();
                    }
                }
                Chai(jsonArray1,jsonArray2,jsonArray3,keys[3],con);

            }else{
                break;
            }
        }
    }

    public static void Chai(JsonArray jsonArray1,JsonArray jsonArray2,JsonArray jsonArray3,String tid,Connection con) throws InterruptedException, SQLException {
        Gson gson=new Gson();
        String sql1="insert into tyc_company_employee(com_id,per_name,per_position) values(?,?,?)";
        PreparedStatement ps1=con.prepareStatement(sql1);

        String sql2="insert into tyc_company_shareholder(com_id,shareholder_name,contribution_amount,contribution_rate) values(?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(sql2);

        String sql3="insert into tyc_company_out_investment(com_id,invest_name,representative,register_amount,register_date,scope_business,investment_amount,investment_rate,status) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps3=con.prepareStatement(sql3);

        if(jsonArray1!=null) {
            Iterator it1 = jsonArray1.iterator();
            while (it1.hasNext()){
                JsonElement jsonElement= (JsonElement) it1.next();
                Chaibean.Zhuyaochengyuan zu=gson.fromJson(jsonElement, Chaibean.Zhuyaochengyuan.class);
                ps1.setString(1, tid);
                ps1.setString(2, zu.ming);
                ps1.setString(3, zu.zhiwu);
                ps1.executeUpdate();
            }
        }
        if(jsonArray2!=null) {
            Iterator it2 = jsonArray2.iterator();
            while (it2.hasNext()){
                JsonElement jsonElement= (JsonElement) it2.next();
                Chaibean.Gudong gu=gson.fromJson(jsonElement, Chaibean.Gudong.class);
                ps2.setString(1, tid);
                ps2.setString(2, gu.ming);
                ps2.setString(3, gu.renjiaochuzi);
                ps2.setString(4, gu.chuzibili);
                ps2.executeUpdate();
            }
        }
        if(jsonArray3!=null) {
            Iterator it3 = jsonArray3.iterator();
            while (it3.hasNext()){
                JsonElement jsonElement= (JsonElement) it3.next();
                Chaibean.Duiwaitouzi dui=gson.fromJson(jsonElement, Chaibean.Duiwaitouzi.class);
                ps3.setString(1, tid);
                ps3.setString(2, dui.beitouziqiyemingcheng);
                ps3.setString(3, dui.fadingdaibiaoren);
                ps3.setString(4, dui.zhuceziben);
                ps3.setString(5, dui.zhuceshijian);
                ps3.setString(6, dui.jingyingfanwei);
                ps3.setString(7, dui.touzishue);
                ps3.setString(8, dui.touzizhanbi);
                ps3.setString(9, dui.zhuangtai);
                ps3.executeUpdate();
            }
        }
    }




    class Cang{
        BlockingQueue<String[]> cool=new LinkedBlockingQueue<String[]>();

        public void fang(String[] key) throws InterruptedException {
            cool.put(key);
        }

        public String[] qu() throws InterruptedException {
            return cool.poll(60, TimeUnit.SECONDS);
        }
    }



}
