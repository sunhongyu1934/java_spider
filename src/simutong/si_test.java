package simutong;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/3.
 */
public class si_test {
    // 代理隧道验证信息
    final static String ProxyUser = "H4TL2M827AIJ963D";
    final static String ProxyPass = "81C9D64628A60CF9";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ParseException {
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

        get(con,proxy);
    }

    public static  Map<String ,String> denglu(Proxy proxy) throws IOException {
        Connection.Response doc= null;
        while (true) {
            try {
                doc=Jsoup.connect("http://pe.pedata.cn/ajaxLoginMember.action")
                        .data("param.loginName", "1343490516@qq.com")
                        .data("param.pwd", "123456")
                        .data("param.iscs", "true")
                        .data("param.macaddress", "50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7")
                        .data("param.language", "zh_CN")
                        .data("request_locale", "zh_CN")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .method(Connection.Method.POST)
                        .execute();
                if (!doc.body().contains("abuyun") && StringUtils.isNotEmpty(doc.body().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.body().contains("Forbidden")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        Map<String ,String> map=doc.cookies();
        return map;
    }

    public static void get(java.sql.Connection con,Proxy proxy) throws IOException, SQLException, InterruptedException, ParseException {
        String sql="insert into si_test(`value`,thread_sleep) values(?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        Random r=new Random();
        Map<String ,String> map=denglu(proxy);
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(2)*60*60*1000)+10800000;

        int flag=1;
        for(int x=1;x<=50000;x++) {
            boolean b=true;
            int th=r.nextInt(80001)+100000;
            Document doc2 = null;
            while (true) {
                try {
                    doc2 = Jsoup.connect("http://pe.pedata.cn/getDetailOrg.action?param.org_id=" + x)
                            .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                            .cookies(map)
                            .timeout(5000)
                            .proxy(proxy)
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .get();
                    if (!doc2.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc2.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc2.outerHtml().contains("Forbidden")) {
                        break;
                    }
                }catch (Exception e){
                    System.out.println("time out reget");
                }
            }
            if(doc2.outerHtml().contains("登录验证失败")){
                b=false;
                ps.setString(1,"账户被封");
                ps.setString(2, String.valueOf(th/1000));
                ps.executeUpdate();
                System.out.println("store mysql :"+x+"  tiao    zhanghu bei feng  and begin sleep   :"+(th/1000)+"   s  and this is  "+flag+"   ci");
                System.out.println("------------------------------------------");
                flag++;
                if(flag==50){
                    break;
                }
            }else{
                ps.setString(1,doc2.outerHtml());
                ps.setString(2, String.valueOf(th/1000));
                ps.executeUpdate();
            }
            if(b) {
                System.out.println("store mysql :" + x + "  tiao and begin sleep   :" + (th / 1000) + "  s");
                System.out.println("------------------------------------------");
            }
            Thread.sleep(th);
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time1=simpleDateFormat.format(date)+" 00:00:00";
            String time2=simpleDateFormat.format(date)+" 00:30:00";
            long t1=simpleDateFormat1.parse(time1).getTime();
            long t2=simpleDateFormat1.parse(time2).getTime();
            long t=System.currentTimeMillis();
            if(t>t1&&t<t2){
                map=denglu(proxy);
            }
            if(t>(begin+cur)){
                map=denglu(proxy);
                begin=t;
                cur=(r.nextInt(2)*60*60*1000)+10800000;;
            }
        }
    }
}
