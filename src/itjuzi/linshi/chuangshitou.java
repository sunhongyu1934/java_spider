package itjuzi.linshi;

import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/6/16.
 */
public class chuangshitou {
    // 代理隧道验证信息
    final static String ProxyUser = "H0X3GMU679V4173D";
    final static String ProxyPass = "BE36E279889A6886";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        chuangshitou ch=new chuangshitou();
        final cangku ca=ch.new cangku();

        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,ca);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=50;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(proxy,ca,finalCon);
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
    }

    public static void get(Proxy proxy,cangku ca,Connection con) throws IOException, InterruptedException, SQLException {
        String sql="update it_founders_pc set tou_xiang=? where c_id=? and `name`=?";
        PreparedStatement ps=con.prepareStatement(sql);
        int a=0;

        while (true) {
            String[] value=ca.qu();
            if(value==null){
                break;
            }
            String cid=value[0];
            Document doc = null;
            while (true) {
                try {
                    doc = Jsoup.connect("http://www.itjuzi.com/company/"+cid)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                            .ignoreHttpErrors(true)
                            .proxy(proxy)
                            .timeout(20000)
                            .get();
                    if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("time out reget");
                }
            }
            Elements ele = getElements(doc, "div.sec.institu-member ul.list-prodcase.limited-itemnum li");
            if (ele != null) {
                for (Element e : ele) {
                    String touxiang = getHref(e, "div.left a span img", "src", 0);
                    String ming=getString(e,"div.right h4 span.c",0);
                    ps.setString(1,touxiang);
                    ps.setString(2,cid);
                    ps.setString(3,ming);
                    ps.executeUpdate();
                    a++;
                    System.out.println(a+"*****************************************");
                }
            }
        }

    }


    public static void data(Connection con,cangku ca) throws SQLException, InterruptedException {
        String sql="select c_id,id from it_founders_pc where tou_xiang is null";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String cid=rs.getString(rs.findColumn("c_id"));
            String id=rs.getString(rs.findColumn("id"));
            ca.ru(new String[]{cid,id});
        }
    }

    class cangku{
        BlockingQueue<String[]> co=new LinkedBlockingQueue<String[]>();
        public void ru(String[] key) throws InterruptedException {
            co.put(key);
        }
        public String[] qu() throws InterruptedException {
            return co.poll(10, TimeUnit.SECONDS);
        }
    }

}
