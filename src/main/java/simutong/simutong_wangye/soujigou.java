package simutong.simutong_wangye;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.*;
import static suanfa.Len.levenshtein;

/**
 * Created by Administrator on 2017/8/28.
 */
public class soujigou {
    // 代理隧道验证信息
    final static String ProxyUser = "HNW6M4X7VI3L8R4D";
    final static String ProxyPass = "855F207CD3A4AC56";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://100.115.97.86:3306/dc_cscyl?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String url2="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username2="spider";
        String password2="spider";
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

        soujigou s=new soujigou();
        final Keys k=s.new Keys();

        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        final Connection finalCon2 = con2;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(finalCon2, k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        for(int a=1;a<=5;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sousuo(proxy,k,finalCon2);
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

    public static void data(Connection con,Keys k) throws SQLException, InterruptedException {
        String sql="select DISTINCT sInstitution from financing_cs where sInstitutionid='0'";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String gid="0";
            String ming=rs.getString(rs.findColumn("sInstitution"));
            k.fang(new String[]{gid,ming});
        }
    }

    public static void data2(Connection con,Keys k) throws SQLException, InterruptedException {
        String sql="select distinct c_name from linshi_ming";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String gid="0";
            String ming=rs.getString(rs.findColumn("c_name"));
            k.fang(new String[]{gid,ming});
        }
    }

    public static void sousuo(Proxy proxy,Keys k,Connection con) throws IOException, InterruptedException, SQLException {
        String sql="insert into linshi2(c_key,c_source,c_xiangsi) values(?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        int p=0;
        while (true) {
            try {
                String value[] = k.qu();
                String gid = value[0];
                String name = value[1];
                Document doc = null;
                Map<Float,String> map=new HashMap<Float, String>();
                while (true) {
                    try {
                        doc = Jsoup.connect("http://www.pedata.cn/search/pedata_1_" + URLEncoder.encode(name, "UTF-8") + ".html")
                                .header("Cookie", "userName=%E5%BC%A0%E5%BC%80; userId=5CDAB65D-80E4-4108-B76E-271753FBAA2F; userStatus=%E8%BF%87%E6%9C%9F; userPsd=e4af27a8396968e1a3588a198bb13d18; firstEnterUrlInSession=http%3A//www.pedata.cn/; VisitorCapacity=1; JSESSIONID=F1C739088ABDAA81BB03EA419ADCFA11; operatorId=31183; pageReferrInSession=https%3A//www.baidu.com/link%3Furl%3DphWWKs-YwMTFIZM_SKCwUecr_VbZ4ZL5waNiMkSiTE3%26wd%3D%26eqid%3D8fd107690006e9490000000359a3789b; Hm_lvt_787334dc3d58f9a34c5292796f0b9185=1503574566,1503626453,1503732795,1503885474; Hm_lpvt_787334dc3d58f9a34c5292796f0b9185=1503888472")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .timeout(5000)
                                .proxy(proxy)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out");
                    }
                }
                Element ele;
                Element tele = getElement(doc, "div.sy_main.new_serach_main:contains(企业) div.search_one_main", 0);
                if (tele == null || StringUtils.isEmpty(tele.toString())) {
                    ele = getElement(doc, "div.sy_main.new_serach_main:contains(投资机构) div.search_one_main", 0);
                } else {
                    ele = getElement(doc, "div.sy_main.new_serach_main:contains(投资机构) div.search_one_main", 1);
                }
                int j = 0;
                String cc = null;
                if (ele != null) {
                    Elements elee = getElements(ele, "div.gw_search_result");
                    if (ele != null) {
                        for (Element e : elee) {
                            j++;
                            cc = getString(e, "div.index_one_news_title a", 0);
                            float dis=levenshtein(name,cc);
                            map.put(dis,cc);
                        }
                    }
                }

                if (j == 1) {
                    float di=levenshtein(name,cc);
                    ps.setString(1, name);
                    ps.setString(2, cc);
                    ps.setString(3, String.valueOf(di));
                    ps.executeUpdate();
                } else if (j > 1) {
                    float a;
                    List<Float> list=new ArrayList<Float>();
                    for(Map.Entry<Float,String> entry:map.entrySet()){
                        list.add(entry.getKey());
                    }

                    String sou=map.get(Collections.max(list));

                    ps.setString(1, name);
                    ps.setString(2, sou);
                    ps.setString(3, String.valueOf(Collections.max(list)));
                    ps.executeUpdate();
                } /*else {
                    ps.setString(1, name);
                    ps.setString(2, "0");
                    ps.executeUpdate();
                }*/

                p++;
                System.out.println(k.po.size() + "--------------------------------------------------------------------------");
                System.out.println("success_sousuo-simutong");
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }






    class Cang{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Keys{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
