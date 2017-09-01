package tianyancha.XinxiXin;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.RedisAction;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/7/3.
 */
public class Tyc_serach {

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
       /* String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        }*/

        // 代理隧道验证信息
        final  String ProxyUser = args[0];
        final  String ProxyPass = args[1];

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        final TYCConsumer ty=new TYCConsumer("bs_tyc_search","spider","10.44.155.195:2181,10.44.143.200:2181,10.45.146.248:2181");
        final TYCProducer ty2=new TYCProducer("tyc_company","10.44.158.42:9092,10.44.137.192:9092,10.44.143.200:9092,10.44.155.195:9092");

        Tyc_serach t=new Tyc_serach();
        final Keys k=t.new Keys();

        int x= Integer.parseInt(args[2]);
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int p=1;p<=x;p++ ){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serachget(proxy,ty2,k);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    qukey(k,ty);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static synchronized void qukey(Keys k,TYCConsumer ty) throws InterruptedException {
        while (true){
            try {
                String key = ty.getmessage();
                k.put(key);
            }catch (Exception e){
                System.out.println("qu error");
            }
        }
    }


    public static void serachget(Proxy proxy,TYCProducer ty2,Keys k) throws IOException, InterruptedException, SQLException {
        int j=0;

        RedisAction redisAction = new RedisAction("a026.hb2.innotree.org", 6379);
        while (true) {
            try {
                String key=k.qu();
                for (int x = 1; x <= 5; x++) {
                    Document doc = null;
                    int flag = 0;
                    while (true) {
                        try {
                            doc = Jsoup.connect("http://www.tianyancha.com/search/p" + x + "?key=" + URLEncoder.encode(key, "utf-8"))
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                    .timeout(10000)
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .proxy(proxy)
                                    .get();
                            if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("time out");
                        }
                        flag++;
                        if (flag >= 50) {
                            break;
                        }
                    }
                    if (doc.outerHtml().contains("没有找到相关结果")) {
                        break;
                    }
                    Elements eles = getElements(doc, "div.search_result_single.search-2017.pb20.pt20.pl30.pr30");

                    if (eles != null) {
                        for (Element e : eles) {
                            String tid = getHref(e, "div.col-xs-10.search_repadding2.f18 a", "href", 0).replace("http://www.tianyancha.com/company/", "");
                            String ming = getString(e, "div.col-xs-10.search_repadding2.f18 a", 0);
                            if (!redisAction.getAllInfoCompanyId(tid) && !redisAction.getQYGXCompanyId(tid)) {
                                redisAction.setQYGXCompanyId(tid, ming);
                                redisAction.setAllInfoCompanyId(tid, ming);
                                ty2.send(tid + "-all");
                                ty2.send(tid + "-gsgx");
                                System.out.println(tid);
                            } else if (!redisAction.getAllInfoCompanyId(tid) && redisAction.getQYGXCompanyId(tid)) {
                                redisAction.setAllInfoCompanyId(tid, ming);
                                ty2.send(tid + "-all");
                                System.out.println(tid);
                            } else if (redisAction.getAllInfoCompanyId(tid) && !redisAction.getQYGXCompanyId(tid)) {
                                redisAction.setQYGXCompanyId(tid, ming);
                                ty2.send(tid + "-gsgx");
                                System.out.println(tid);
                            }
                        }
                        j++;
                        System.out.println("serach is "+j);
                        System.out.println("------------------------------------------------------------------------");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    /*public static void data(Connection con,Keys k) throws SQLException, InterruptedException {
        String sql="select `Name` from qichacha_search where Province!='BJ' and Province!='SH' and Province!='GD' limit 500000";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String key=rs.getString(rs.findColumn("Name"));
            k.put(key);
        }
    }*/


    class Keys{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }

        public String qu() throws InterruptedException {
            return bo.take();
        }
    }
}
