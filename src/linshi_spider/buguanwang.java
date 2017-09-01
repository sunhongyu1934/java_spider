package linshi_spider;

import com.google.gson.Gson;
import itjuzi.linshi.bean;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/6/16.
 */
public class buguanwang {
    // 代理隧道验证信息
    final static String ProxyUser = "H0X3GMU679V4173D";
    final static String ProxyPass = "BE36E279889A6886";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String url2="jdbc:mysql://100.115.97.86:3306/innotree_data_panshi?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username2="tech_spider";
        String password2="sPiDer$#@!23";
        Class.forName(driver1).newInstance();
        java.sql.Connection con2=null;
        try {
            con2 = DriverManager.getConnection(url2, username2, password2);
        }catch (Exception e){
            while(true){
                con2 = DriverManager.getConnection(url2, username2, password2);
                if(con2!=null){
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

        buguanwang b=new buguanwang();
        final cangku ca=b.new cangku();
        ExecutorService pool= Executors.newCachedThreadPool();

        final Connection finalCon = con2;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    storedata(finalCon,ca);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=20;x++){
            final Connection finalCon1 = con;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(finalCon1,ca,proxy);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    public static List<String[]> data(Connection con) throws SQLException {
        String sql="select sign,token from qichacha_token";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String[]> list=new ArrayList<String[]>();

        while (rs.next()){
            String sign=rs.getString(rs.findColumn("sign"));
            String token=rs.getString(rs.findColumn("token"));
            list.add(new String[]{sign,token});
        }
        return list;
    }

    public static void get(Connection con,cangku ca,Proxy proxy) throws SQLException, InterruptedException {
        List<String[]> list=data(con);
        Random r=new Random();
        Gson gson=new Gson();
        String sql="insert into linshi_web(sou_name,yuan_name,yuan_id,web) values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        int a=0;

        while (true){
            try {
                int index = r.nextInt(10);
                String sign = list.get(index)[0];
                String token = list.get(index)[1];
                String[] value = ca.qu();
                if (ca == null) {
                    break;
                }
                String id = value[0];
                String ming = value[1];

                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://opensdk.qichacha.com/open/v1/base/advancedSearch?appId=85265c07198d947b03b9bfbab46d409c&pageIndex=1&sign=" + sign + "&token=" + token + "&searchKey=" + URLEncoder.encode(ming, "UTF-8") + "&searchIndex=default&isSortAsc=false&industryCode&subIndustryCode")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
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

                String json = doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim();
                bean b = gson.fromJson(json, bean.class);
                for (bean.Re.RR rr : b.result.Result) {
                    String name = rr.Name;
                    String web = rr.WebSite;

                    ps.setString(1, name);
                    ps.setString(2, ming);
                    ps.setString(3, id);
                    ps.setString(4, web);
                    ps.executeUpdate();
                    a++;
                    System.out.println(a+"***************************************************");
                }
            }catch (Exception e){
                System.out.println("error");
            }

        }


    }

    public static void storedata(Connection con,cangku ca) throws SQLException, InterruptedException {
        String sql="select id,sName from company where (sUrl is null or sUrl='')";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String ming=rs.getString(rs.findColumn("sName"));
            ca.ru(new String[]{id,ming});
        }

    }

    class cangku {
        BlockingQueue<String[]> ca = new LinkedBlockingQueue<String[]>();

        public void ru(String[] key) throws InterruptedException {
            ca.put(key);
        }

        public String[] qu() throws InterruptedException {
            return ca.poll(10, TimeUnit.SECONDS);
        }
    }

}
