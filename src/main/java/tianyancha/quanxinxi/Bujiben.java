package tianyancha.quanxinxi;

import com.google.gson.*;
import gongshi.liebiaonei;
import gongshi.xiangqing;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/23.
 */
public class Bujiben {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.166.12:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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


        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));



        ExecutorService pool= Executors.newFixedThreadPool(10);
        int p=0;
        for(int a=1;a<=10;a++){
            final java.sql.Connection finalCon = con;
            final int finalP = p;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        seldata(finalCon, proxy, finalP);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            p=p+9;
        }

    }


    public static void seldata(java.sql.Connection con,Proxy proxy,int x) throws SQLException, IOException {
        String sql="select a.quan_cheng as quan_cheng,a.id from tyc_jichu1 a where a.gongshang_hao=''  limit "+x+",9";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String quanchneg=rs.getString(rs.findColumn("quan_cheng"));
            String id=rs.getString(rs.findColumn("id"));
            serach(proxy,quanchneg,con,id);
            a++;
            System.out.println(a+"*************************************************************");
        }
    }



    public static void serach(Proxy proxy,String key, java.sql.Connection con,String id) throws IOException, SQLException {
        Connection.Response doc=null;
        int f=0;
        while(true) {
            try {
                doc = Jsoup.connect("http://yd.gsxt.gov.cn/QuerySummary")
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
                        .timeout(5000)
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
                if (StringUtils.isNotEmpty(doc.body()) && doc.body().length() > 250 && !doc.body().toString().contains("abuyun")) {
                    break;
                }
                f++;
                if (f > 10) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("request serach success");
        System.out.println(doc.body());
        if(doc!=null&&StringUtils.isNotEmpty(doc.body())) {
            List<String> parlist = parsrliebiao(doc.body().toString(),key);
            getdetail(parlist, proxy, con, id);
        }

    }

    public static void getdetail(List<String> listlie,Proxy proxy, java.sql.Connection con,String id) throws IOException, SQLException {
        Gson gson=new Gson();
        for(String s:listlie) {
            System.out.println("begin requst detail");
            Connection.Response doc2=null;
            while (true) {
                try {
                    doc2 = Jsoup.connect("http://yd.gsxt.gov.cn/QueryBusiLice")
                            .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0")
                            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                    // .data("pageSize", "10")
                            .data("userID", "id001")
                            .data("userIP", "192.123.123.13")
                                    // .data("keywords", "百度")
                                    // .data("topic", "1")
                            .data("nodenum", s.split(",", 4)[2])
                                    //.data("mobileAction", "entSearch")
                                    //.data("pageNum", "1")
                            .proxy(proxy)
                            .timeout(5000)
                            .method(Connection.Method.POST)
                            .data("pripid", s.split(",", 4)[0])
                            .data("enttype", s.split(",", 4)[2])
                            .data("mobileAction", "entDetail")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .execute();
                    if (StringUtils.isNotEmpty(doc2.body()) && !doc2.body().contains("abuyun")) {
                        System.out.println("request detail success");
                        String json2 = doc2.body().replace("{}", "\"\"").replace("\"\"\"\"", "\"\"");
                        xiangqing x = gson.fromJson(json2, xiangqing.class);
                        System.out.println(json2);
                        System.out.println("request detail success");
                        data(con, x, id);
                        break;
                    }
                }catch (Exception e){
                    System.out.println("time out reget");
                }
            }
            break;
        }
    }

    public static void data(java.sql.Connection con,xiangqing x,String id) throws SQLException {
        String sql="update tyc_jichu1 set gongshang_hao=?,tongyi_xinyong=?,qiye_leixing=?,yingye_nianxian=?,dengji_jiguan=? where id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,x.REGNO);
        ps.setString(2,x.UNISCID);
        ps.setString(3,x.ENTTYPE_CN);
        ps.setString(4,x.ESTDATE+"-"+x.OPTO);
        ps.setString(5,x.REGORG_CN);
        ps.setString(6,id);
        ps.executeUpdate();
        System.out.println(id+"         "+x.REGORG_CN);
    }



    public static List<String> parsrliebiao(String json,String key){
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
            if(field.ENTNAME.replace("<font color=red>", "").replace("</font>","").equals(key)) {
                list.add(field.PRIPID + "," + field.ENTTYPE + "," + field.S_EXT_NODENUM + "," + field.ENTNAME.replaceAll("[^\u4e00-\u9fa5]", "").trim());
            }
        }
        return list;
    }

}
