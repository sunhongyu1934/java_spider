package linshi_spider;

import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class zhuanli {
    private static Connection conn;
    // 代理隧道验证信息
    final static String ProxyUser = "HJ3F19379O94DO9D";
    final static String ProxyPass = "D1766F5002A70BC4";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static int aa=0;
    private static Ca c=new Ca();
    private static Cb cb=new Cb();
    private static Cc cc=new Cc();
    private static Cd cd=new Cd();
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
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static void main(String args[]) throws IOException, SQLException, InterruptedException {
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    get();
                } catch (IOException e) {
                    e.printStackTrace();
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
                        data2();
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

        for(int a=1;a<=10;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data3();
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

        for(int a=1;a<=15;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data4();
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

        for(int a=1;a<=15;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data5();
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

    public static void get() throws IOException, SQLException, InterruptedException {
        String sql="insert into ipc_patent(key_n,value_n,t_cneg) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        System.out.println("begin one");
        Document doc= JsoupUtils.get("http://epub.sipo.gov.cn/ipc.jsp",proxy);
        Elements ele=JsoupUtils.getElements(doc,"div.ipc_rbox.right ul.ipc_rul li");
        for(Element e:ele){
            String name=JsoupUtils.getString(e,"span a",0);
            String key=JsoupUtils.getString(e,"font a",0);

            ps.setString(1,key);
            ps.setString(2,name);
            ps.setString(3,"1");
            ps.executeUpdate();
            aa++;
            System.out.println(aa+"**********************************************");

            System.out.println("begin two");
            Document doc2=get2(key);
            Elements ele2=JsoupUtils.getElements(doc2,"div.ipc_rbox.right ul.ipc_rul li.ipc_li02");
            for(Element e2:ele2){
                String name2=JsoupUtils.getString(e2,"span a",0);
                String key2=JsoupUtils.getString(e2,"font a",0);

                cd.fang(key2);

                ps.setString(1,key2);
                ps.setString(2,name2);
                ps.setString(3,"2");
                ps.executeUpdate();
                aa++;
                System.out.println(aa+"**********************************************");
            }
        }
    }

    public static void data5() throws InterruptedException, IOException, SQLException {
        String sql="insert into ipc_patent(key_n,value_n,t_cneg) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String value = c.qu();
                System.out.println("begin six");
                Document doc6 = get2(value);
                Elements ele6 = JsoupUtils.getElements(doc6, "div.ipc_rbox.right ul.ipc_rul li.ipc_li06");
                for (Element e6 : ele6) {
                    String name6 = JsoupUtils.getString(e6, "span a", 0);
                    String key6 = JsoupUtils.getString(e6, "font a", 0);

                    ps.setString(1, key6);
                    ps.setString(2, name6);
                    ps.setString(3,"6");
                    ps.executeUpdate();
                    aa++;
                    System.out.println(aa + "**********************************************");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void data4() throws InterruptedException, IOException, SQLException {
        String sql="insert into ipc_patent(key_n,value_n,t_cneg) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String key4=cb.qu();
                System.out.println("begin five");
                Document doc5=get2(key4);
                Elements ele5=JsoupUtils.getElements(doc5,"div.ipc_rbox.right ul.ipc_rul li.ipc_li05");
                for(Element e5:ele5){
                    String name5=JsoupUtils.getString(e5,"span a",0);
                    String key5=JsoupUtils.getString(e5,"font a",0);

                    c.fang(key5);

                    ps.setString(1,key5);
                    ps.setString(2,name5);
                    ps.setString(3,"5");
                    ps.executeUpdate();
                    aa++;
                    System.out.println(aa+"**********************************************");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void data3() throws InterruptedException, IOException, SQLException {
        String sql="insert into ipc_patent(key_n,value_n,t_cneg) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String key3=cc.qu();
                System.out.println("begin four");
                Document doc4=get2(key3);
                Elements ele4=JsoupUtils.getElements(doc4,"div.ipc_rbox.right ul.ipc_rul li.ipc_li04");
                for(Element e4:ele4){
                    String name4=JsoupUtils.getString(e4,"span a",0);
                    String key4=JsoupUtils.getString(e4,"font a",0);

                    cb.fang(key4);

                    ps.setString(1,key4);
                    ps.setString(2,name4);
                    ps.setString(3,"4");
                    ps.executeUpdate();
                    aa++;
                    System.out.println(aa+"**********************************************");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void data2() throws InterruptedException, IOException, SQLException {
        String sql="insert into ipc_patent(key_n,value_n,t_cneg) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String key2=cd.qu();
                System.out.println("begin three");
                Document doc3=get2(key2);
                Elements ele3=JsoupUtils.getElements(doc3,"div.ipc_rbox.right ul.ipc_rul li.ipc_li03");
                for(Element e3:ele3){
                    String name3=JsoupUtils.getString(e3,"span a",0);
                    String key3=JsoupUtils.getString(e3,"font a",0);

                    cc.fang(key3);

                    ps.setString(1,key3);
                    ps.setString(2,name3);
                    ps.setString(3,"3");
                    ps.executeUpdate();
                    aa++;
                    System.out.println(aa+"**********************************************");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Document get2(String key) throws IOException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect("http://epub.sipo.gov.cn/ipce.action")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("sic", key)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 100 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }

    static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    static class Cb{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    static class Cc{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    static class Cd{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
