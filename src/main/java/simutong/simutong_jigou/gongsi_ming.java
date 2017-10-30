package simutong.simutong_jigou;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.Map;

import static Utils.JsoupUtils.getString;
import static simutong.simutong_jigou.Qingqiu.denglu;
import static simutong.simutong_jigou.Qingqiu.jichuget;

/**
 * Created by Administrator on 2017/6/28.
 */
public class gongsi_ming {
    // 代理隧道验证信息
    final static String ProxyUser = "H48Y5D1332U54J8D";
    final static String ProxyPass = "70CDB43BC3BAD91D";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
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


        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));


        String[] zhanghu=new String[]{"simutong3@gaiyachuangxin.cn","111111","wang.hao@lingweispace.cn","111111"};
        String[] pc=new String[]{"50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7","20-68-9D-31-CD-3C,04-7D-7B-FB-55-BA"};
        Map<String,String> map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);

        data(con,map,proxy);
    }

    public static void data(Connection con,Map<String,String> map,Proxy proxy) throws SQLException, IOException, InterruptedException {
        String sql="select beitou_sid from si_touzi_zuixin";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=1;
        while (rs.next()){
            String sid=rs.getString(rs.findColumn("beitou_sid"));
            get(map,proxy,sid,con);
            Thread.sleep(5000);
            System.out.println(a+"*********************************************");
            a++;
        }
    }


    public static String get(Map<String,String> map,Proxy proxy,String sid,Connection con) throws IOException, InterruptedException, SQLException {
        String url="http://pe.pedata.cn/getDetailEp.action?param.ep_id="+sid;
        Document doc=jichuget(url,map,proxy);

        String quan=getString(doc,"div.float_left span.detail_title24",0);
        String jian=getString(doc,"div.control-group:contains(中文简称) div.controls",0);
        String jian_y=getString(doc,"div.control-group:contains(英文简称) div.controls",0);

        String sql="insert into si_ming(quan_cheng,jian_cheng,jian_y,s_id) values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        ps.setString(1,quan);
        ps.setString(2,jian);
        ps.setString(3,jian_y);
        ps.setString(4,sid);
        ps.executeUpdate();
        return doc.outerHtml();
    }
}
