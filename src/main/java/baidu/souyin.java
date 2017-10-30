package baidu;

import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/9/1.
 */
public class souyin {
    private static Connection conn;
    private static int jishu=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        souyin s=new souyin();
        final Ca c=s.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c);
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
                        baisou(c);
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

    public static void data(Ca c) throws SQLException, InterruptedException {
        String sql="select id,k_key from linshi3";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String key=rs.getString(rs.findColumn("k_key"));

            c.jin(new String[]{id,key});
        }
    }

    public static void baisou(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="update linshi3 set k_source=? where id=?";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String[] val = c.qu();
                if (val == null || val.length < 2) {
                    break;
                }
                Document doc = Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=57095150_2_oem_dg&wd=" + URLEncoder.encode(val[1], "utf-8") + "&rsv_pq=8a0852f3000492fe&rsv_t=75713GlRQ6dy5h3Da8XWJb36JSCqveOI%2Fc9ymzY5OLAiE3Mzhc3fW3kqf55JjTeZ3nSNTLH5kLk&rqlang=cn&rsv_enter=1&rsv_sug3=2&rsv_sug1=1&rsv_sug7=100&rsv_sug2=0&inputT=1638&rsv_sug4=1638")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                        .get();
                String suo = "";
                Element ele= JsoupUtils.getElement(doc, "div.nums", 0);
                if(ele!=null){
                    suo=ele.ownText().replace("百度为您找到相关结果约", "").replace(",", "").replace("个", "");
                }

                ps.setString(1, suo);
                ps.setString(2, val[0]);
                ps.executeUpdate();
                jishu++;
                System.out.println("success_baidu");
                System.out.println(jishu + "*****************************************************");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void jin(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
