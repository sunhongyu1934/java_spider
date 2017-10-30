package tianyancha.XinxiXin;

import baidu.RedisAction;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

public class Tyc_seaxin {
    private static java.sql.Connection conn;
    private static Ca c;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/clean_data?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        conn=con;

    }
    public static void main(String args[]){
        ExecutorService pool= Executors.newCachedThreadPool();
        Tyc_seaxin t=new Tyc_seaxin();
        final Key k=t.new Key();
        final Ca cc=new Ca();
        c=cc;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=25;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(k);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static void data(Key k) throws SQLException, InterruptedException {
        String sql="select distinct cname from aaa";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            try {
                String ming = rs.getString(rs.findColumn("cname")).replace("(","").replace(")","").replace("（","").replace("）","").replace("有限合伙","");
                k.put(ming);
            }catch (Exception e){

            }
        }
    }

    public static void get(Key k) throws SQLException {
        String sql="insert into jijin_tyc(c_name,s_name,k_ey,v_alue,t_id) values(?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        int jishu=0;
        while (true) {
            Document doc;
            try {
                String key = k.qu();
                doc = detailget("https://www.tianyancha.com/search?key=" + URLEncoder.encode(key, "utf-8") + "&checkFrom=searchBox");
                Elements eles = getElements(doc, "div.search_result_single.search-2017.pb25.pt25.pl30.pr30 div.search_right_item");
                int p=0;
                boolean ff=false;
                String aa=null;
                String uu=null;
                String cc=null;
                String tt=null;
                if (eles != null) {
                    for (Element e : eles) {
                        String tid = getHref(e, "a", "href", 0).replace(" ", "").trim().replace("https://www.tianyancha.com/company/", "");
                        String cname = getString(e, "a", 0).replace(" ", "").trim();
                        String kk=getString(e,"div.add span.sec-c3",0);
                        String vv=getString(e,"div.add span.overflow-width.over-hide.vertical-bottom.in-block",0);
                        p++;
                        if(p==1){
                            aa=cname;
                            uu=kk;
                            cc=vv;
                            tt=tid;
                        }
                        if(key.equals(cname)) {
                            ps.setString(1,key);
                            ps.setString(2,cname);
                            ps.setString(3,kk);
                            ps.setString(4,vv);
                            ps.setString(5,tid);
                            ps.executeUpdate();

                            ff=true;
                        }
                    }
                }
                if(StringUtils.isNotEmpty(aa)&&!ff){
                    ps.setString(1,key);
                    ps.setString(2,aa);
                    ps.setString(3,uu);
                    ps.setString(4,cc);
                    ps.setString(5,tt);
                    ps.executeUpdate();
                }
                jishu++;
                System.out.println(k.bo.size()+"***********************************************************");
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }


    public static Document detailget(String url) throws IOException, InterruptedException {
        System.out.println(url);
        Document doc=null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")) {
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static void getip() throws IOException, InterruptedException {
        RedisAction rd=new RedisAction("10.44.51.90",6379);
        while (true) {
            try {
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(1000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }


    public class Key{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.take();
        }
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
