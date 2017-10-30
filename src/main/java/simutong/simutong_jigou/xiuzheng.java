package simutong.simutong_jigou;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.Map;
import java.util.Random;

import static Utils.JsoupUtils.getString;
import static simutong.simutong_jigou.Qingqiu.denglu;
import static simutong.simutong_jigou.Qingqiu.jichuget;

/**
 * Created by Administrator on 2017/7/14.
 */
public class xiuzheng {
    // 代理隧道验证信息
    final static String ProxyUser = "H47A980OB388590D";
    final static String ProxyPass = "4E67CD7BD260D551";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException, InterruptedException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        data(con,proxy);
    }



    public static void data(Connection con,Proxy proxy) throws SQLException, IOException, InterruptedException {
        String sql="select * from si_jiben where `describe` like '%基金 基金类型%' or `describe` like '%在职 姓名 %' or `describe` like '%年度 投资数量%' or `describe` like '%企业 行业%'";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String[] zhanghu=new String[]{"simutong3@gaiyachuangxin.cn","111111","wang.hao@lingweispace.cn","111111"};
        String[] pc=new String[]{"50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7","20-68-9D-31-CD-3C,04-7D-7B-FB-55-BA"};
        Map<String,String> map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);
        Random r=new Random();
        int a=0;
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 1800000;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String sid=rs.getString(rs.findColumn("s_id"));
            xiu(con,sid,map,proxy,id);
            Thread.sleep(r.nextInt(8000));
            long t = System.currentTimeMillis();
            if (t > (begin + cur)) {
                cur = (r.nextInt(50) * 60 * 1000) + 1800000;
                map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);
                t = System.currentTimeMillis();
                begin = t;
            }
            a++;
            System.out.println(a+"**************************************************************");
        }
    }

    public static void xiu(Connection con,String sid,Map<String,String> map,Proxy proxy,String iid) throws IOException, InterruptedException, SQLException {
        String sql="update si_jiben set `describe`=? where id="+iid;
        PreparedStatement ps=con.prepareStatement(sql);

        String url="http://pe.pedata.cn/getDetailOrg.action?param.org_id="+sid;
        Document doc=jichuget(url,map,proxy);
        String miaoshu=getString(doc,"div.detail_onebox:contains(描述) form.form-horizontal",0);
        ps.setString(1,miaoshu);
        ps.executeUpdate();

    }

}
