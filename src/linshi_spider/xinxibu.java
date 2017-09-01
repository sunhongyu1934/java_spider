package linshi_spider;

import com.google.gson.Gson;
import linshi_spider.bean.xinxibubean;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/4.
 */
public class xinxibu {
    private static List<String> list=new ArrayList<String>();
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_data_panshi?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        select(con);

        ExecutorService pool= Executors.newFixedThreadPool(1);
        for(int x=0;x<=0;x++) {
            final java.sql.Connection finalCon = con;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(finalCon, proxy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();

    }


    public static void select(java.sql.Connection con) throws SQLException, UnsupportedEncodingException {
        String sql="select o.`sName` as na,o.`id` as id from company o  WHERE (    o.`sName` LIKE  '%公司'     OR o.`sName` LIKE  '%集团'     OR o.`sName` LIKE  '%工作室'     OR o.`sName` LIKE  '%中心'     OR o.`sName` LIKE  '%事务所'  )   AND (    surl IS NULL     OR surl = ''     OR `sLogoUrl` IS NULL     OR `sLogoUrl` = ''  ) and id not in(SELECT   t.id   FROM  `innotree_data_panshi`.`company` t join company_xinxibuchong b on t.id=b.c_id WHERE (    t.`sName` LIKE  '%公司'     OR t.`sName` LIKE  '%集团'     OR t.`sName` LIKE  '%工作室'     OR t.`sName` LIKE  '%中心'     OR t.`sName` LIKE  '%事务所'  )   AND (    surl IS NULL     OR surl = ''     OR `sLogoUrl` IS NULL     OR `sLogoUrl` = ''  ))";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String key=rs.getString(rs.findColumn("na"));
            String id=rs.getString(rs.findColumn("id"));
            list.add(key+"#####"+id);
        }
    }

    public static void serach(java.sql.Connection con,Proxy proxy) throws IOException, SQLException {
        while (true){
            try {
                if (list.size() == 0) {
                    break;
                }
                String str = list.get(0);
                list.remove(0);
                data(str, con, proxy);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static void serachtwo(java.sql.Connection con,Proxy proxy) throws IOException, SQLException {
        while (true){
            try {
                String str = list.get(0);
                list.remove(0);
                qixin(proxy,con,str);
                if (list.size() == 0) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }


    public static void data(String str, java.sql.Connection con,Proxy proxy) throws IOException, SQLException {
        String names=str.split("#####",2)[0].replace("\n","").replace(" ","").replace("/","");
        String name=URLEncoder.encode(names, "UTF-8");
        String id=str.split("#####",2)[1];

        String sql="insert into company_xinxibuchong(c_id,company_key,company_value,company_logo,company_web,company_order,pipei_xinxi) values(?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        String url="http://www.tianyancha.com/v2/search/"+name+".json";
        Document doc= null;
        System.out.println("begin request");
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<body>", "").replace("</html>", "").replace("</body>", "").replace("<head></head>", "").replace(" ", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        String json=doc.outerHtml().replace("<html>","").replace("<body>","").replace("</html>","").replace("</body>","").replace("<head></head>","").replace(" ","").trim();
        Gson gson=new Gson();
        System.out.println(json);
        System.out.println(names);
        xinxibubean xin=gson.fromJson(json,xinxibubean.class);
        if(StringUtils.isEmpty(xin.message)&&!xin.message.contains("无数据")) {
            int a = 0;
            for (xinxibubean.dat d : xin.data) {
                String namez = d.name.replace("<em>", "").replace("</em>", "").replace("\n", "");
                String logo = d.logo;
                String web = d.websites;
                if (StringUtils.isNotEmpty(web) && web.contains(";")) {
                    web = web.split(";")[0];
                }
                String pipei=null;
                try {
                    pipei=d.matchField.content.replace("<em>", "").replace("</em>", "").replace("\n", "");
                }catch (Exception e){
                    pipei="";
                }

                ps.setString(1, id);
                ps.setString(2, names);
                ps.setString(3, namez);
                ps.setString(4, logo);
                ps.setString(5, web);
                ps.setString(6, String.valueOf(a + 1));
                ps.setString(7,pipei);
                ps.addBatch();
                if (a == 5) {
                    break;
                }
                a++;
            }
            System.out.println("request success and begin insert mysql");
            ps.executeBatch();
            System.out.println("insert mysql success");
            System.out.println("---------------------------------------------------------------------");
        }else{
            ps.setString(1, id);
            ps.setString(2, names);
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setString(5, "");
            ps.setString(6, "0");
            ps.setString(7,"");
            ps.executeUpdate();
            System.out.println("request success and begin insert mysql");
            System.out.println("insert mysql success");
            System.out.println("---------------------------------------------------------------------");
        }
    }

    public static void qixin(Proxy proxy,Connection con,String str) throws UnsupportedEncodingException, SQLException {
        String sql="insert into company_xinxibuchong(c_id,company_key,company_value,company_logo,company_web,company_order,pipei_xinxi) values(?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        String names=str.split("#####",2)[0];
        String id=str.split("#####",2)[1];

        String sou = URLEncoder.encode(names, "UTF-8");
        Document doc = null;
        System.out.println("begin request");
        while (true) {
            try {
                doc = Jsoup.connect("http://www.qixin.com/search?key=" + sou + "&type=enterprise&method=")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(2000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>","").replace("<body>","").replace("</html>","").replace("</body>","").replace("<head></head>","").replace(" ","").trim()) && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("完成上面的验证")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out reget");
            }
        }

        Elements ele=doc.select("div.search-list-bg div.search-ent-row.clearfix");
        int a=0;
        for(Element e:ele){
            String name=e.select("a.search-result-company-name").text().replace("<em>","").replace("</em>","").replace("{{el.name | html}}", "");
            String qid=e.select("div.is-follow.no-follow").attr("ms-attr-eid").replace("'", "");
            if(StringUtils.isNotEmpty(name)){
                detail(qid,proxy,con,str,a);
                if(a==2){
                    break;
                }
                a++;
            }
        }
        if(StringUtils.isEmpty(ele.toString())){
            ps.setString(1, id);
            ps.setString(2, names);
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setString(5, "");
            ps.setString(6, "0");
            ps.setString(7,"");
            ps.executeUpdate();
            System.out.println("sou null");
            System.out.println("*******************************************************");
        }

    }

    public static void detail(String qid,Proxy proxy,Connection con,String str,int a) throws SQLException, UnsupportedEncodingException {
        String sql="insert into company_xinxibuchong(c_id,company_key,company_value,company_logo,company_web,company_order,pipei_xinxi) values(?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);


        Document doc = null;
        System.out.println("begin request detail");
        while (true) {
            try {
                doc = Jsoup.connect("http://www.qixin.com/company/"+qid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(2000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>","").replace("<body>","").replace("</html>","").replace("</body>","").replace("<head></head>","").replace(" ","").trim()) && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("完成上面的验证")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out reget");
            }
        }
        String value=doc.select("div.company-name span.company-name-now").text();
        String logo=doc.select("div.col-xs-3.company-left.bg-white div.company-card div.company-logo.preload").attr("pre-src");
        if(logo.equals("http://cache.qixin.com/images/default.png")){
            logo="";
        }
        String web=doc.select("div.company-info-item-text a[rel=nofollow]").attr("href");
        String names=str.split("#####",2)[0];
        String id=str.split("#####",2)[1];

        ps.setString(1, id);
        ps.setString(2, names);
        ps.setString(3, value);
        ps.setString(4, logo);
        ps.setString(5, web);
        ps.setString(6, String.valueOf(a + 1));
        ps.setString(7,"");
        ps.executeUpdate();
        System.out.println("insert mysql success");
        System.out.println("---------------------------------------------------------------------");



    }

}
