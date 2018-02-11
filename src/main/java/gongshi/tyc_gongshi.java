package gongshi;

import com.google.gson.*;
import jdk.nashorn.internal.ir.Block;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class tyc_gongshi {
    // 代理隧道验证信息
    final static String ProxyUser = "H4KKF9EHDF26260D";
    final static String ProxyPass = "2A64AB23C97FCA79";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int j=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }

    public static void main(String args[]) throws IOException, InterruptedException, SQLException {
        tyc_gongshi t=new tyc_gongshi();
        Ca c=t.new Ca();
        Da d=t.new Da();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(d);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int a=1;a<=10;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(c,d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int b=1;b<=10;b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Da d) throws SQLException, InterruptedException {
        String sql="select distinct tyc_sousuo from gaoxin_mingdan where zhuce_flag=2 and tyc_sousuo not in (select quan_cheng from tyc.tyc_jichu_quan)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cname=rs.getString(rs.findColumn("tyc_sousuo"));
            d.fang(cname);
        }
    }

    public static void serach(Ca c,Da d) throws IOException, InterruptedException {
        while (true) {
            try {
                String key = d.qu();
                Document doc;
                while (true) {
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
                                // .data("pripid","E4AACF8882E14EFC2F0428CB18A1E7D1594EF1C54A2F1309CC84DF564601C0D817932D83D33C5ED3AD5FC031969B48ABADD2112B972C78637859DAFF901E79256EE5FF8930DA71D407BE1E05A3106893")
                                //.data("enttype","6150")
                                //.data("nodenum","310000")
                                //.data("mobileAction","entDetail")
                                //.data("userID","id001")
                                //  .data("userIP", "192.123.123.13")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .post();
                        if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    }catch (Exception ee){
                        System.out.println("time out reget");
                    }
                }
                String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("<font color=\"red\">", "").replace("</font>", "").replace("  ", "").replace("\n", "").trim();
                List<String[]> list = parsrliebiao(json);
                for (String[] s : list) {
                    String cname = s[3].replace("(","").replace(")","").replace("（","").replace("）","");
                    if (key.replace("(","").replace(")","").replace("（","").replace("）","").equals(cname)) {
                        c.fang(s);
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }


    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into tyc.tyc_jichu_quan(quan_cheng,tongyi_xinyong,fa_ren,dengji_jiguan,zhuce_shijian,jingying_zhuangtai,qiye_leixing,zhuce_ziben,yingye_nianxian,hezhun_riqi,a_ddress,jingying_fanwei) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Gson gson=new Gson();
        while (true) {
            try {
                String[] value = c.qu();
                Document doc;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://yd.gsxt.gov.cn/QueryBusiLice")
                                .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 Html5Plus/1.0")
                                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                // .data("pageSize", "10")
                                .data("userID", "id001")
                                .data("userIP", "192.123.123.13")
                                // .data("keywords", "百度")
                                // .data("topic", "1")
                                .data("nodenum", value[2])
                                //.data("mobileAction", "entSearch")
                                //.data("pageNum", "1")
                                .proxy(proxy)
                                .timeout(5000)
                                .data("pripid", value[0])
                                .data("enttype", value[1])
                                .data("mobileAction", "entDetail")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .post();
                        if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    }catch (Exception ee){
                        System.out.println("time out reget");
                    }
                }
                String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("<font color=\"red\">", "").replace("</font>", "").replace("{}","\"{}\"").replace("  ", "").replace("\n", "").trim();
                Gde g = gson.fromJson(json, Gde.class);
                String quan = g.ENTNAME;
                String tong = g.UNISCID;
                String fa = g.NAME;
                String dejg = g.REGORG_CN;
                String chengri = g.APPRDATE;
                String zt = g.REGSTATE_CN;
                String lei = g.ENTTYPE_CN;
                String zhuzi = g.REGCAP + g.REGCAPCUR_CN;
                String yiye = g.OPFROM + g.OPTO;
                String hezhun = g.ESTDATE;
                String add = g.DOM;
                String jf = g.OPSCOPE;

                ps.setString(1, quan);
                ps.setString(2, tong);
                ps.setString(3, fa);
                ps.setString(4, dejg);
                ps.setString(5, chengri);
                ps.setString(6, zt);
                ps.setString(7, lei);
                ps.setString(8, zhuzi);
                ps.setString(9, yiye);
                ps.setString(10, hezhun);
                ps.setString(11, add);
                ps.setString(12, jf);
                ps.executeUpdate();

                j++;
                System.out.println(j+"**********************************************************");
            }catch (Exception e){
                System.out.println("detail error");
                e.printStackTrace();
            }
        }
    }


    public static List<String[]> parsrliebiao(String json){
        List<String[]> list=new ArrayList<String[]>();
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
            list.add(new String[]{field.PRIPID,field.ENTTYPE,field.S_EXT_NODENUM,field.ENTNAME.replaceAll("[^\\u4e00-\\u9fa5]","").trim()});
        }
        return list;
    }


    public static class Gde{
        public String APPRDATE="";
        public String CAT18="";
        public String CAT2NAME="";
        public String DOM="";
        public String ENTNAME="";
        public String ENTTYPE_CN="";
        public String ESTDATE="";
        public String NAME="";
        public String OPFROM="";
        public String OPSCOPE="";
        public String OPTO="";
        public String REGCAP="";
        public String REGCAPCUR="";
        public String REGCAPCUR_CN="";
        public String REGNO="";
        public String REGORG_CN="";
        public String REGSTATE_CN="";
        public String UNISCID="";
    }


    class Da {
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    class Ca {
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
