package linshi_spider;

import Utils.Dup;
import Utils.JsoupUtils;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class beian {
    // 代理隧道验证信息
    final static String ProxyUser = "HJGR1T7575J6744D";
    final static String ProxyPass = "109FD50EC1CC22A7";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int jishu=0;
    private static int pp=0;

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

    public static void heart() throws SQLException, InterruptedException {
        while (true) {
            String sql = "select 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            Thread.sleep(60000);
        }
    }


    public static void main(String args[]) throws IOException {
        beian ch=new beian();
        Da d=ch.new Da();
        ExecutorService pool= Executors.newCachedThreadPool();

        String aa="20";
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(d);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=Integer.parseInt(aa);a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach2(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    heart();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void data(Da d) throws InterruptedException, SQLException {
        String sql="select distinct top_domain from dim_site where top_domain not in (select comp_domain from beian_xin)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            d.fang(rs.getString(rs.findColumn("top_domain")));
        }
    }

    public static void data2(Da d) throws InterruptedException, SQLException {
        String sql="select comp_id,comp_full_name from comp_bulu_logoweb where comp_web_url='' or comp_web_url is null";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            d.fang(rs.getString(rs.findColumn("comp_id"))+"###"+rs.getString(rs.findColumn("comp_full_name")));
        }
    }

    public static void serach(Da d) throws IOException, InterruptedException, SQLException {
        String sql="insert into beian_xin(comp_full_name,comp_web,comp_domain) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true) {
            try {
                String key=d.qu();
                Document doc=get("http://icp.chinaz.com/"+ URLEncoder.encode(key,"utf-8"));

                String fname=JsoupUtils.getString(doc,"div.Tool-IcpMainCent.wrapper02 div.pr.zI0 ul.bor-t1s01.IcpMain01 li.bg-gray.clearfix:contains(主办单位) p",0).replace("使用高级查询纠正信息","");
                String web=JsoupUtils.getString(doc,"div.Tool-IcpMainCent.wrapper02 div.pr.zI0 ul.bor-t1s01.IcpMain01 li.bg-gray.clearfix:contains(网站首页) p",0);

                ps.setString(1,fname);
                ps.setString(2,web);
                ps.setString(3,key);
                ps.executeUpdate();

                jishu++;
                System.out.println(jishu+"********************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    public static void serach2(Da d) throws IOException, InterruptedException, SQLException {
        String sql="insert into beian_linshi(comp_id,comp_full_name,comp_web) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true) {
            try {
                String key=d.qu();
                Document doc=get("http://www.beianbeian.com/s?keytype=2&q="+ URLEncoder.encode(key.split("###")[1],"utf-8"));

                Elements ele=JsoupUtils.getElements(doc,"table.seo tr");
                String web = null;
                if(ele!=null){
                    for(Element e:ele){
                        if(Dup.nullor(JsoupUtils.getString(e,"td div#home_url a",0))) {
                            web = JsoupUtils.getString(e, "td div#home_url a", 0);
                        }
                    }
                }

                ps.setString(1,key.split("###")[0]);
                ps.setString(2,key.split("###")[1]);
                ps.setString(3,web);
                ps.executeUpdate();

                jishu++;
                System.out.println(jishu+"********************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    public static Document get(String url) throws IOException {
        System.out.println(url);
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                //System.out.println(doc);
                if(doc!=null&&doc.outerHtml().length()>50&&!doc.outerHtml().contains("abuyun")){
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
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
