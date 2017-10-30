package test;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/16.
 */
public class test4 {

    // 代理隧道验证信息
    final static String ProxyUser = "H112205236B5G2PD";
    final static String ProxyPass = "E9484DB291BFC579";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;

    public static String getUrlProxyContent(PreparedStatement ps) throws SQLException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        try
        {
            int i=1;
            for(int x=78;x<=100;x++) {
                Document doc = Jsoup.connect("http://bj.lianjia.com/ershoufang/pg"+x+"/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .proxy(proxy)
                        .timeout(100000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                System.out.println(doc.outerHtml());
                Elements elechild=doc.select("li.clear");
                for(Element ele:elechild){
                    String page = "pg"+x;
                    String title = ele.select("div.title a").text();
                    String didian = ele.select("div.address div.houseInfo a").text();
                    String elegeju = ele.select("div.address div.houseInfo").text().toString();
                    String geju = null;
                    try {
                        geju = elegeju.replace("|", "#####").split("#####", 6)[1];
                    } catch (Exception e1) {
                        geju = null;
                    }
                    String mianji = null;
                    try {
                        mianji = elegeju.replace("|", "#####").split("#####", 6)[2];
                    } catch (Exception e1) {
                        mianji = null;
                    }
                    String chaoxiang = null;
                    try {
                        chaoxiang = elegeju.replace("|", "#####").split("#####", 6)[3];
                    } catch (Exception e1) {
                        chaoxiang = null;
                    }
                    String zhuangxiu = null;
                    try {
                        zhuangxiu = elegeju.replace("|", "#####").split("#####", 6)[4];
                    } catch (Exception e1) {
                        zhuangxiu = null;
                    }
                    String dianti = null;
                    try {
                        dianti = elegeju.replace("|", "#####").split("#####", 6)[5];
                    } catch (Exception e1) {
                        dianti = null;
                    }


                    String elejianzao = ele.select("div.flood div.positionInfo").text();
                    String jianzao = null;
                    String louceng=null;
                    String loucengzongshu=null;
                    try {
                        jianzao = elejianzao.split("-", 2)[0].trim().split("\\)",2)[1];
                    } catch (Exception e1) {
                        jianzao = null;
                    }
                    try {
                        louceng = elejianzao.split("-", 2)[0].trim().split("\\(", 2)[0];
                    }catch (Exception e2){
                        louceng=null;
                    }
                    try {
                        loucengzongshu = elejianzao.split("-", 2)[0].trim().split("\\(", 2)[1].split("\\)", 2)[0];
                    }catch (Exception e3){
                        loucengzongshu=null;
                    }
                    String jianzaodidian = null;
                    try {
                        jianzaodidian = elejianzao.split("-", 2)[1].trim();
                    } catch (Exception e2) {
                        jianzaodidian = null;
                    }

                    Elements eletag = ele.select("div.tag span");
                    StringBuffer buffer = new StringBuffer();
                    for (Element etag : eletag) {
                        buffer.append(etag.text() + ",");
                    }
                    String tag = buffer.toString();



                    String zongjia = ele.select("div.priceInfo div.totalPrice span").text()+"万";

                    String danjia = ele.select("div.priceInfo div.unitPrice span").text();


                    String eleid = ele.select("div.info.clear div.title a").attr("href");
                    String id = null;
                    try {
                        id = eleid.split("/", 5)[4].replace(".html", "");
                    } catch (Exception e1) {
                        id = null;
                    }


                    ps.setString(1,page);
                    ps.setString(2,title);
                    ps.setString(3,didian);
                    ps.setString(4,geju);
                    ps.setString(5,mianji);
                    ps.setString(6,chaoxiang);
                    ps.setString(7,zhuangxiu);
                    ps.setString(8,dianti);
                    ps.setString(9,louceng);
                    ps.setString(10,loucengzongshu);
                    ps.setString(11,jianzao);
                    ps.setString(12,jianzaodidian);
                    ps.setString(13,tag);
                    ps.setString(14,zongjia);
                    ps.setString(15,danjia);
                    ps.setString(16,id);
                    ps.addBatch();
                    System.out.println("this is : "+i+" tiao"+"     "+x+" ye");
                    i++;
                    System.out.println("*************************************");
                }
                ps.executeBatch();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }





    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        String sql="insert into lianjia_list(page,`name`,address,pattern,area,trend,renovation,elevator,floor,floor_sum,build,build_address,tag,total,price,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        getUrlProxyContent(ps);









    }
}
