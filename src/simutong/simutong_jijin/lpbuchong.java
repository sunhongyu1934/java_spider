package simutong.simutong_jijin;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import simutong.simutong_jigou.Qingqiu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

import static Utils.JsoupUtils.*;
import static simutong.simutong_jijin.qingQiu.denglu;

/**
 * Created by Administrator on 2017/7/24.
 */
public class lpbuchong {
    // 代理隧道验证信息
    final static String ProxyUser = "H0QCBTTB7675S1XD";
    final static String ProxyPass = "26A1FF9238C9050D";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IllegalAccessException, InterruptedException, ParseException, IOException, InstantiationException, SQLException, ClassNotFoundException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        Connection con=getcon();
        data(con,proxy);

    }

    public static void data(Connection con,Proxy proxy) throws SQLException, IOException, InterruptedException, ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Random r=new Random();
        String sql="select s_id,count(id) from si_jiji_lp GROUP BY s_id HAVING count(id)=10";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 1800000;
        String[] zhanghu=new String[]{"simutong3@gaiyachuangxin.cn","111111","xiaohang.qu@lingweispace.cn","111111"};
        String[] pc=new String[]{"50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7","50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7"};
        Map<String,String> map= denglu(proxy, zhanghu[0], zhanghu[1], pc[0]);
        int flag=0;
        int p=0;
        while (rs.next()){
            try {
                int th = r.nextInt(5001) + 10000;
                String s_id = rs.getString(rs.findColumn("s_id"));
                buchong(proxy,s_id,con,map);
                Thread.sleep(th);
                long t = System.currentTimeMillis();
                if (t > (begin + cur)) {
                    cur = (r.nextInt(50) * 60 * 1000) + 1800000;
                    /*Thread.sleep(cur);
                    if(con!=null) {
                        con.close();
                    }
                    con=getcon();
                    map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);*/
                    if (flag == 0) {
                        map = Qingqiu.denglu(proxy, zhanghu[2], zhanghu[3], pc[1]);
                        flag = 1;
                    } else {
                        map = Qingqiu.denglu(proxy, zhanghu[0], zhanghu[1], pc[0]);
                        flag = 0;
                    }
                    t = System.currentTimeMillis();
                    begin = t;
                }
                java.util.Date date = new java.util.Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time1 = simpleDateFormat.format(date) + " 07:30:00";
                String time2 = simpleDateFormat.format(date) + " 21:30:00";
                long t3 = simpleDateFormat1.parse(time2).getTime();
                if (t >= t3) {
                    Thread.sleep(36000000);
                    if (con != null) {
                        con.close();
                    }
                    con = getcon();
                    map = Qingqiu.denglu(proxy, zhanghu[2], zhanghu[3], pc[1]);
                    flag = 1;
                    t = System.currentTimeMillis();
                    begin = t;
                }
                p++;
                System.out.println(p);
                System.out.println("------------------------------------------");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static void buchong(Proxy proxy,String s_id,Connection con,Map<String,String> map) throws SQLException, IOException, InterruptedException {
        for(int x=2;x<=100;x++) {
            Document doc = qingqiu("http://pe.pedata.cn/getLp3rdFund.action?param.fund_id=" + s_id, map, proxy, String.valueOf(x));
            //write("/data2/simutong/lp", doc.outerHtml(), s_id);
            String sql = "insert into si_jiji_lp(s_id,lp_ming,guoyou_flag,shu_xing,lei_xing,chengnuo_jine,lun_ci,chuzi_time,detail_url) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            Elements ele = getElements(doc, "div.detail_onebox:contains(LP) table.table.table-hover tbody tr");
            int p=0;
            if (ele != null) {
                for (Element e : ele) {
                    String ming = getString(e, "td", 0);
                    String detailurl = getHref(e, "td a", "href", 0);
                    String guoyou = getString(e, "td", 1);
                    String lpshuxing = getString(e, "td", 2);
                    String lpleixing = getString(e, "td", 3);
                    String jine = getString(e, "td", 4);
                    String lunci = getString(e, "td", 5);
                    String chuzitime = getString(e, "td", 6);

                    ps.setString(1, s_id);
                    ps.setString(2, ming);
                    ps.setString(3, guoyou);
                    ps.setString(4, lpshuxing);
                    ps.setString(5, lpleixing);
                    ps.setString(6, jine);
                    ps.setString(7, lunci);
                    ps.setString(8, chuzitime);
                    ps.setString(9, detailurl);
                    ps.executeUpdate();
                    p++;
                }
            }
            if(p!=10){
                break;
            }
        }
        System.out.println("lp ok");
    }

    public static Document qingqiu(String url,Map<String,String> map,Proxy proxy,String data){
        Random r=new Random();
        Document doc=null;
        System.out.println("开始请求lp翻页");
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .data("pagetools_pageNumber", "1")
                        .data("param.currentPage", data)
                        .proxy(proxy)
                        .post();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("lp翻页请求成功");
        return doc;
    }

    public static void write(String path,String key,String sid) throws IOException {
        try {
            FileOutputStream ff = new FileOutputStream(path + "/" + sid);
            OutputStreamWriter out = new OutputStreamWriter(ff, "UTF-8");
            out.write(key);
            out.flush();
            ff.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        return con;
    }
}
