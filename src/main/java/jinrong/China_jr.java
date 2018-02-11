package jinrong;

import Utils.Dup;
import Utils.JsoupUtils;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class China_jr {
    // 代理隧道验证信息
    final static String ProxyUser = "HP5G1I415085Y7AD";
    final static String ProxyPass = "9CDAD2529F99DC54";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int jishu=0;
    private static Map<String,String> map;

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


    public static void main(String args[]) throws IOException {
        China_jr ch=new China_jr();
        Ca c=ch.new Ca();
        Da d=ch.new Da();
        ExecutorService pool= Executors.newCachedThreadPool();

        login();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(d);
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
                        serach(d,c);
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

    public static void data(Da d) throws InterruptedException {
        int j=0;
        for(int a=1;a<=5;a++){
            d.fang(String.valueOf(j));
            j=j+30;
        }
    }

    public static void serach(Da d,Ca c) throws IOException, InterruptedException {
        Gson gson=new Gson();

        while (true) {
            try {
                String of = d.qu();
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://www.fintechdb.cn/request/loadmore")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                                .header("Accept","application/json, text/javascript, */*; q=0.01")
                                .header("Accept-Encoding","gzip, deflate")
                                .header("Accept-Language","zh-CN,zh;q=0.9")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
                                .timeout(5000)
                                .data("csrf_weiyangx_token", "e67ee55c27d7ad4be2b984ef51905ca9")
                                .data("city", "0")
                                .data("financing", "0")
                                .data("field", "0")
                                .data("timestart", "0")
                                .data("timeend", "0")
                                .data("offset", of)
                                .data("sort", "1")
                                .data("order", "desc")
                                .header("Host","www.fintechdb.cn")
                                .header("Origin","http://www.fintechdb.cn")
                                .header("Referer","http://www.fintechdb.cn/")
                                .header("X-Requested-With","XMLHttpRequest")
                                //.header("Cookie","UM_distinctid=1602ee15c09101-0a2735ec8b6863-5b452a1d-144000-1602ee15c0a5cb; csrf_weiyangx_cookie=e67ee55c27d7ad4be2b984ef51905ca9; CNZZDATA1270867928=1433567949-1512613447-%7C1512613447; _weiyangx_product_=hkoaornbetv5do54s1fgke3i46rr5fn0")
                                .proxy(proxy)
                                .post();
                        if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out");
                    }
                }
                String json = Dup.qujson(doc);
                serach s = gson.fromJson(json, serach.class);
                for (serach.co cc : s.company_list) {
                    c.fang(new String[]{"http://www.fintechdb.cn/product/"+cc.url, cc.name, cc.companyImg, cc.setupTime, cc.financingStep, cc.city});
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into china_jinrong(p_name,p_cretime,p_suozaidi,p_fin,p_logo,c_desc,comp_full_name,c_cretime,c_shezu,c_web,c_web_statu,c_zhuhao,c_faren,c_zhuzi,c_zhudi,c_tags) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        String sql2="insert into china_renyuan(c_id,comp_full_name,p_name,p_job) values(?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        while (true){
            try {
                String[] value = c.qu();
                Document doc = get(value[0]);
                String desc = JsoupUtils.getString(doc, "div.wa-company-detail-info p", 1);
                String gming = JsoupUtils.getString(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed td", 1);
                String chenti = JsoupUtils.getString(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed td", 3);
                String shezu = JsoupUtils.getString(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed td", 5);
                String web = JsoupUtils.getString(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed td a", 0);
                String weby = JsoupUtils.getString(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed td span", 0);
                Element bia = JsoupUtils.getElement(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-condensed", 1);
                ;
                String zhuceh = JsoupUtils.getString(bia, "td", 1);
                String faren = JsoupUtils.getString(bia, "td", 3);
                String zhuzi = JsoupUtils.getString(bia, "td", 5);
                String zhudi = JsoupUtils.getString(bia, "td", 7);
                Elements eletag = JsoupUtils.getElements(JsoupUtils.getElement(doc, "div.uk-width-1-1.wa-shadow.wa-company-meta.uk-grid.uk-grid-collapse",1),"span");
                System.out.println(gming);
                String tag = null;
                StringBuffer str = new StringBuffer();
                for (Element e : eletag) {
                    str.append(e.text() + ";");
                }
                Elements eletag2=JsoupUtils.getElements(doc,"div.wa-company-detail-info p.wa-product-tags uk-clearfix span");
                for(Element e:eletag2){
                    str.append(e.text() + ";");
                }

                tag = str.toString();

                ps.setString(1, value[1]);
                ps.setString(2, value[3]);
                ps.setString(3, value[5]);
                ps.setString(4, value[4]);
                ps.setString(5, value[2]);
                ps.setString(6, desc);
                ps.setString(7, gming);
                ps.setString(8, chenti);
                ps.setString(9, shezu);
                ps.setString(10, web);
                ps.setString(11, weby);
                ps.setString(12, zhuceh);
                ps.setString(13, faren);
                ps.setString(14, zhuzi);
                ps.setString(15, zhudi);
                ps.setString(16, tag);
                ps.executeUpdate();

                /*String cid = null;
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    cid = String.valueOf(rs.getInt(1));
                }


                Element bia2 = JsoupUtils.getElement(doc, "div.wa-company-detail-content.uk-grid.uk-grid-small table.uk-table.uk-table-hover.uk-table-striped.uk-table-condensed", 2);
                ;
                Elements gele = JsoupUtils.getElements(bia2, "tbody tr");
                int l=0;
                for (Element e : gele) {
                    if(l!=0) {
                        String xming = JsoupUtils.getString(e, "td", 0);
                        String zhiwu = JsoupUtils.getString(e, "td", 1);

                        ps2.setString(1, cid);
                        ps2.setString(2, gming);
                        ps2.setString(3, xming);
                        ps2.setString(4, zhiwu);
                        ps2.executeUpdate();
                    }

                    l++;
                }*/

                jishu++;
                System.out.println(jishu + "*********************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static void login() throws IOException {
        org.jsoup.Connection.Response doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://www.fintechdb.cn/request/ajaxLogin")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("csrf_weiyangx_token", "f81f4256d8db3b8024a3766c2a429368")
                        .data("phone", "13717951934")
                        .data("password", "123456")
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding", "Accept-Encoding")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Host", "www.fintechdb.cn")
                        .header("Origin", "http://www.fintechdb.cn")
                        .header("Referer", "http://www.fintechdb.cn/")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Proxy-Connection","keep-alive")
                        .method(org.jsoup.Connection.Method.POST)
                        .timeout(5000)
                        .proxy(proxy)
                        .execute();
                System.out.println(doc.cookies());
                if (doc != null && doc.body().length() > 50 && !doc.body().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }
        }

        map=doc.cookies();
        System.out.println(map);
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                       // .header("Cookie","UM_distinctid=1602ee15c09101-0a2735ec8b6863-5b452a1d-144000-1602ee15c0a5cb; CNZZDATA1270867928=1433567949-1512613447-%7C1512618950; csrf_weiyangx_cookie=40d8876e4bff838f42f1709895ed0f40; _weiyangx_product_=ciav9k0vt26phnd3u3jon0chisbms5sl")
                        .proxy(proxy)
                        .cookies(map)
                        .timeout(5000)
                        .get();
                if(doc.outerHtml().contains("立即登录")){
                    login();
                    continue;
                }
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Da{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }


    public static class serach{
        public List<co> company_list;
        public static class co{
            public String url;
            public String name;
            public String companyImg;
            public String setupTime;
            public String financingStep;
            public String city;
        }
    }
}
