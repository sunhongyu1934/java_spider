package tianyancha.XinxiXin;

import Utils.JsoupUtils;
import Utils.RedisClu;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/7/3.
 */
public class Tyc_serach {
    private static Connection conn;
    private static Ca c;
    private static int a=0;
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

        conn=con;

    }

    public static void main(String args[]) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Tyc_serach t=new Tyc_serach();
        Co co=t.new Co();
        Ca cc=new Ca();
        c=cc;
        String x=args[0];
        String biao=args[1];
        ExecutorService pool=Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(co,biao);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int p=1;p<=Integer.parseInt(x);p++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(co,biao);
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

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void data(Co co,String biao) throws SQLException, InterruptedException {
        String sql="select comp_id,comp_full_name from "+biao+" where zhuce_flag=1";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            try {
                String cid = rs.getString(rs.findColumn("comp_id"));
                String cname = rs.getString(rs.findColumn("comp_full_name"));

                co.fang(new String[]{cid, cname});
            }catch (Exception e){
                System.out.println("fang error");
            }
        }
    }

    public static void serach(Co co,String gbiao) throws InterruptedException, IOException, SQLException {
        String sql="update "+gbiao+" set tyc_url=?,tyc_key=?,tyc_value=?,tyc_sousuo=? where comp_id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            String[] value=co.qu();
            if(value==null||value.length<2){
                break;
            }
            Document doc=detailget("https://www.tianyancha.com/search?key="+URLEncoder.encode(value[1],"utf-8")+"&checkFrom=searchBox");
            Elements ele= JsoupUtils.getElements(doc,"div.search_result_single.search-2017.pb25.pt25.pl30.pr30 div.search_right_item");
            boolean bo=true;
            for(Element e:ele){
                String url = getHref(e, "a", "href", 0).replace(" ", "").trim();
                String cname = getString(e, "a", 0).replace(" ", "").trim();
                String key=JsoupUtils.getString(e,"div[class=add] span.sec-c3",0);
                String vv=JsoupUtils.getString(e,"div[class=add] span.overflow-width.over-hide.vertical-bottom.in-block",0);

                if((value[1].equals(cname))||key!=null&&key.equals("历史名称")&&value[1].equals(vv)) {
                    bo=false;
                    ps.setString(1, url);
                    ps.setString(2, key);
                    ps.setString(3, vv);
                    ps.setString(4, cname);
                    ps.setString(5, value[0]);
                    ps.executeUpdate();
                    a++;
                    System.out.println(a + "***********************************************************");
                }
            }
            if(bo){
                for(Element e:ele){
                    String url = getHref(e, "a", "href", 0).replace(" ", "").trim();
                    String cname = getString(e, "a", 0).replace(" ", "").trim();
                    String key=JsoupUtils.getString(e,"div[class=add] span.sec-c3",0);
                    String vv=JsoupUtils.getString(e,"div[class=add] span.overflow-width.over-hide.vertical-bottom.in-block",0);

                    ps.setString(1, url);
                    ps.setString(2, key);
                    ps.setString(3, vv);
                    ps.setString(4, cname);
                    ps.setString(5, value[0]);
                    ps.executeUpdate();
                    a++;
                    System.out.println(a + "***********************************************************");
                    break;
                }
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
        RedisClu rd=new RedisClu();
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

    class Co{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(60, TimeUnit.SECONDS);
        }
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
