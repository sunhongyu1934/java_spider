package tianyancha.quanxinxi;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by DELL' on 2017/5/22.
 */
public class jiben {
    // 代理隧道验证信息
    final static String ProxyUser = "H4XGPM790E93518D";
    final static String ProxyPass = "2835A47D56143D62";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        String key=URLEncoder.encode("小米","UTF-8");
        serach(key,proxy);


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



        String url2="jdbc:mysql://10.51.120.107:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username2="sunhongyu";
        String password2="yJdviIeSuicKn8xX";
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






        jiben j=new jiben();
        final cangku c=j.new cangku();


        ExecutorService pool=Executors.newCachedThreadPool();
        final Connection finalCon2 = con2;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon2,c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final Connection finalCon = con;
        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c, finalCon,proxy);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public static void data(Connection con,cangku c) throws SQLException, IOException, InterruptedException {
        int p=0;
        for(int x=1;x<=10;x++) {
            String sql = "select id,`Name`,`No` from qichacha_search where Province='SH' limit "+p+",200000";
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("kaishi duqu");
            ResultSet rs = ps.executeQuery();
            System.out.println("duqu success");
            while (rs.next()) {
                String gongshang = rs.getString(rs.findColumn("No"));
                String quancheng = rs.getString(rs.findColumn("Name"));
                String xuhao = rs.getString(rs.findColumn("id"));

                String st[] = new String[]{gongshang, quancheng, xuhao};
                c.fang(st);
            }
            p=p+200000;
        }

    }

    public static void detail(cangku c,Connection con,Proxy proxy) throws InterruptedException {
        int f=0;
        while (true){
            String value[]=c.qu();
            if(value==null){
                break;
            }

            String gongshang=value[0];
            String quancheng=value[1];
            String xuhao=value[2];
            String js="";
            try {

                Gson gson = new Gson();
                String keys=URLEncoder.encode(quancheng,"UTF-8");
                String json = serach(keys, proxy);
                if (StringUtils.isEmpty(json)) {
                    continue;
                }


                Bean.Sousuo sou = gson.fromJson(json, Bean.Sousuo.class);

                if(sou.data==null){
                    String key= URLEncoder.encode(gongshang,"UTF-8");
                    json=serach(key,proxy);
                    sou=gson.fromJson(json, Bean.Sousuo.class);
                }
                js=json;

                String quan=sou.data.get(0).name.replace(" ","");
                String tid=sou.data.get(0).id;
                String logo = sou.data.get(0).logo;
                String phone = sou.data.get(0).phone;
                String web = sou.data.get(0).websites;
                String email = sou.data.get(0).emails;
                String address = sou.data.get(0).regLocation;
                String zhucedizhi = address;
                String faren = sou.data.get(0).legalPersonName;
                String zhuceziben = sou.data.get(0).regCapital;
                String zhuceshijian = sou.data.get(0).estiblishTime;
                String zhuangtai = sou.data.get(0).regStatus;
                String jingyingfanwei = sou.data.get(0).businessScope;
                String dengjijiguan = "";
                String tongyishehuidaima = "";
                String zuzhijigoudaima = "";
                String qiyeleixing = "";
                String hangye = "";
                String yingyenianxian = "";
                String cengyongming = "";
                String gongshanghao = "";

               /* boolean br = false;
                String sql2 = "select * from qichacha_base where Name='" + quan + "'";
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    br = true;
                    dengjijiguan = rs2.getString(rs2.findColumn("BelongOrg"));
                    tongyishehuidaima = rs2.getString(rs2.findColumn("CreditCode"));
                    zuzhijigoudaima = rs2.getString(rs2.findColumn("OrgNo"));
                    qiyeleixing = rs2.getString(rs2.findColumn("EconKind"));
                    hangye = rs2.getString(rs2.findColumn("Industry"));
                    yingyenianxian = rs2.getString(rs2.findColumn("StartDate")) + "-" + rs2.getString(rs2.findColumn("EndDate"));
                    cengyongming = rs2.getString(rs2.findColumn("OriginalName"));
                    gongshanghao = rs2.getString(rs2.findColumn("No"));
                }*/

                System.out.println(quancheng+"   ***   "+quan);
                String insert = "insert into tyc_jichu_sh(quan_cheng,ceng_yongming,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,jingying_zhuangtai,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,t_id,xu_hao,yuan_cheng) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement psi = con.prepareStatement(insert);
                psi.setString(1, quan);
                psi.setString(2, cengyongming);
                psi.setString(3, logo);
                psi.setString(4, phone);
                psi.setString(5, email);
                psi.setString(6, address);
                psi.setString(7, web);
                psi.setString(8, faren);
                psi.setString(9, zhuceziben);
                psi.setString(10, zhuceshijian);
                psi.setString(11, zhuangtai);
                psi.setString(12, gongshanghao);
                psi.setString(13, zuzhijigoudaima);
                psi.setString(14, tongyishehuidaima);
                psi.setString(15, qiyeleixing);
                psi.setString(16, hangye);
                psi.setString(17, yingyenianxian);
                psi.setString(18, dengjijiguan);
                psi.setString(19, zhucedizhi);
                psi.setString(20, jingyingfanwei);
                psi.setString(21,tid);
                psi.setString(22,xuhao);
                psi.setString(23,quancheng);
                psi.executeUpdate();
                f++;
                System.out.println(f+"***********************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error  "+quancheng+"    "+gongshang);
                System.out.println(js);
            }


        }
    }


    class cangku{
        BlockingQueue<String[]> blo=new LinkedBlockingQueue<String[]>(10000);
        public void fang(String key[]) throws InterruptedException {
            blo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return blo.poll(60, TimeUnit.SECONDS);
        }
    }


    public static String serach(String key,Proxy proxy) throws IOException {
        Document doc= null;
        int a=0;
        while (true) {
            try {
                doc=Jsoup.connect("http://www.tianyancha.com/v2/search/" + key + ".json?")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        //.proxy(proxy)
                        .timeout(5000)
                        .get();
                System.out.println(doc.body());
                if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
            a++;
            if(a>=500){
                return null;
            }
        }
        return doc.body().toString().replace("<body>","").replace("</body>","").replace("<em>","").replace("</em>","").replace("\n","").trim();
    }
}
