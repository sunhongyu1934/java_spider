package simutong.simutong_jigou;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

import static simutong.simutong_jigou.Qingqiu.denglu;
import static simutong.simutong_jigou.simutong_jigou.touzi;

/**
 * Created by Administrator on 2017/6/19.
 */
public class Shijian {
    // 代理隧道验证信息
    final static String ProxyUser = "H4KKF9EHDF26260D";
    final static String ProxyPass = "2A64AB23C97FCA79";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InterruptedException, ParseException, IOException {
        Connection con=getcon();

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        data(con,proxy);

    }

    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
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

        return con;
    }


    public static void data(Connection con,Proxy proxy) throws SQLException, IOException, InterruptedException, ParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String sql="select DISTINCT s_id from si_jiben where s_id not in (select s_id from si_touzi) and id>20029";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        Random r=new Random();
        long begin=System.currentTimeMillis();
        long cur=(r.nextInt(50) * 60 * 1000) + 1800000;

        String[] zhanghu=new String[]{"simutong3@gaiyachuangxin.cn","111111","wang.hao@lingweispace.cn","111111"};
        String[] pc=new String[]{"50-7B-9D-FB-57-4B,2C-6E-85-9C-6A-F3,2C-6E-85-9C-6A-F7","20-68-9D-31-CD-3C,04-7D-7B-FB-55-BA"};
        Map<String,String> map=denglu(proxy,zhanghu[2],zhanghu[3],pc[1]);
        int flag=0;
        int p=0;
        while (rs.next()){

            int th = r.nextInt(5001) + 10000;
            String sid=rs.getString(rs.findColumn("s_id"));
            String pid="0";
            touzi(con,sid,pid,map,proxy);
            Thread.sleep(th);
            long t = System.currentTimeMillis();
            if (t > (begin + cur)) {
                cur=(r.nextInt(50) * 60 * 1000) + 1800000;
                Thread.sleep((r.nextInt(50) * 60 * 1000) + 1800000);
                if(con!=null) {
                    con.close();
                }
                con=getcon();
                map = denglu(proxy, zhanghu[2], zhanghu[3],pc[1]);
                    /*if(flag==0) {
                        map = denglu(proxy, zhanghu[2], zhanghu[3],pc[1]);
                        flag=1;
                    }else{
                        map = denglu(proxy, zhanghu[0], zhanghu[1],pc[0]);
                    }*/
                t=System.currentTimeMillis();
                begin = t;
            }
            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time1 = simpleDateFormat.format(date) + " 07:30:00";
            String time2 = simpleDateFormat.format(date) + " 21:30:00";
            long t2 = simpleDateFormat1.parse(time1).getTime();
            long t3 = simpleDateFormat1.parse(time2).getTime();
            if (t>=t3) {
                Thread.sleep(36000000);
                if(con!=null) {
                    con.close();
                }
                con=getcon();
                map = denglu(proxy, zhanghu[2], zhanghu[3],pc[1]);
                flag=0;
                t=System.currentTimeMillis();
                begin = t;
            }
            p++;
            System.out.println(p);
            System.out.println("success_simutong-shijian");
            System.out.println("------------------------------------------");

        }

    }

}
