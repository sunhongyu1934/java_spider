package linshi_spider;

import Utils.Dup;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

public class jiance {
    private static java.sql.Connection conn;
    private static int a=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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

    public static void main(String args[]) throws SQLException {
        jiance j=new jiance();
        Cc c=j.new Cc();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(c);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        gg2(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Cc c) throws SQLException, InterruptedException {
        String sql="select comp_id,comp_full_name,comp_web,comp_domain from beian_xin where comp_id in (select comp_id from bbb)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("comp_id"));
            String fweb=rs.getString(rs.findColumn("comp_domain"));
            String web=rs.getString(rs.findColumn("comp_web"));
            String cname=rs.getString(rs.findColumn("comp_full_name"));

            if(web.length()<50&&!web.contains("https://store.taobao.com/shop/noshop.htm")) {
                c.fang(new String[]{id,fweb,web,cname});
            }
        }
    }

    public static void data2(Cc c) throws SQLException, InterruptedException {
        String sql="select comp_id as id,comp_web_url from comp_havfinance where comp_web_url!='' and comp_web_url is not null";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String web=rs.getString(rs.findColumn("comp_web_url"));
            if(web.length()<50&&!web.contains("https://store.taobao.com/shop/noshop.htm")) {
                c.fang(new String[]{id, web});
            }
        }
    }

    public static void gg2(Cc c) throws SQLException, InterruptedException {
        String sql="update comp_havfinance set comp_web_url=? where comp_id=?";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String[] value = c.qu();
                if (value == null || value.length < 2) {
                    break;
                }

                String id = value[0];
                String web = value[1];
                String url =null;
                if(web.contains("http://")||web.contains("https://")) {
                    url=jian(web, "");
                }else{
                    url=jian("http://"+web, "");
                }

                if (Dup.nullor(url)) {
                    if(url.length()<50&&!url.contains("https://store.taobao.com/shop/noshop.htm")) {
                        ps.setString(1, url);
                        ps.setString(2, id);
                        ps.executeUpdate();
                    }
                } else {
                    ps.setString(1, null);
                    ps.setString(2, id);
                    ps.executeUpdate();
                }
                a++;
                System.out.println(a + "**********************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static void gg(Cc c) throws InterruptedException, SQLException {
        String sql2="update bbb set comp_web_url=? where comp_id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        while (true){
            String[] value=c.qu();
            if(value==null||value.length<2){
                break;
            }

            String id=value[0];
            String fweb=value[1];
            String web=value[2];
            String cname=value[3];

            String url=null;
            if(fweb.contains("www")) {
                url=jian("http://"+fweb, cname);
            }else{
                url=jian("http://www."+fweb, cname);
            }
            if(!Dup.nullor(url)) {
                if(web.contains("www")) {
                    url=jian("http://"+web, cname);
                }else{
                    url=jian("http://www."+web, cname);
                }
                if (Dup.nullor(url)) {
                    ps2.setString(1, url);
                    ps2.setString(2, id);
                    ps2.executeUpdate();
                }
            }else{
                ps2.setString(1, url);
                ps2.setString(2, id);
                ps2.executeUpdate();
            }
            a++;
            System.out.println(a+"**********************************************");
        }
    }

    public static String jian(String key,String va){
        if(Dup.nullor(key)) {
            Connection.Response doc =null;
            System.out.println(key);
            int m=0;
            while (true) {
                try {
                    doc = Jsoup.connect(key)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .timeout(3000)
                            .method(Connection.Method.GET)
                            .execute();
                    if (doc != null && doc.body().length() > 44) {
                        break;
                    }
                }catch (Exception e){
                }
                m++;
                if(m>=5){
                    break;
                }
            }

            if(doc!=null){
                if((doc.statusCode()==200||doc.statusCode()==302)) {
                    return doc.url().toString();
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    class Cc{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
