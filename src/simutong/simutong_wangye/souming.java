package simutong.simutong_wangye;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/7/15.
 */
public class souming {
    // 代理隧道验证信息
    final static String ProxyUser = "H2CGL101C4D537QD";
    final static String ProxyPass = "1E60DF24843C82B0";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        souming s=new souming();
        final Cang c=s.new Cang();
        final Keys k=s.new Keys();

        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=5;x++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sousuo(c, proxy, k);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int y=1;y<=10;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c,finalCon,proxy);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void data(Connection con,Keys k) throws SQLException, InterruptedException {
        String sql="select  jian from spider_dim.si_comp_name";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String gid="0";
            String ming=rs.getString(rs.findColumn("jian"));
            k.fang(new String[]{gid,ming});
        }
    }

    public static void sousuo(Cang c,Proxy proxy,Keys k) throws IOException, InterruptedException {
        int p=0;
        while (true) {
            try {
                String value[] = k.qu();
                String gid = value[0];
                String name = value[1];
                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("http://www.pedata.cn/search/pedata_1_" + URLEncoder.encode(name, "UTF-8") + ".html")
                                .header("Cookie", "userName=%E5%BC%A0%E5%BC%80; userId=5CDAB65D-80E4-4108-B76E-271753FBAA2F; userStatus=%E8%BF%87%E6%9C%9F; userPsd=e4af27a8396968e1a3588a198bb13d18; JSESSIONID=702A01487FB608E4968B12A5E2C7A305; firstEnterUrlInSession=http%3A//www.pedata.cn/; VisitorCapacity=1; operatorId=31183; pageReferrInSession=https%3A//www.baidu.com/link%3Furl%3DV8-iHcGKs-QrNa5waom5pRgzbfwLT5jp65FXhC566Si%26wd%3D%26eqid%3D8b72cfb7000108ec00000003599f84cf; Hm_lvt_787334dc3d58f9a34c5292796f0b9185=1503570321,1503572515,1503574566,1503626453; Hm_lpvt_787334dc3d58f9a34c5292796f0b9185=1503626458")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .timeout(5000)
                                .proxy(proxy)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out");
                    }
                }
                Element ele = getElement(doc, "div.sy_main.new_serach_main:contains(企业) div.search_one_main", 0);
                if (ele != null) {
                    Elements elee = getElements(ele, "div.gw_search_result");
                    if (ele != null) {
                        for (Element e : elee) {
                            String url = getHref(e, "div.index_one_news_title a", "href", 0);
                            c.fang(new String[]{url, gid});
                        }
                    }
                }
                p++;
                System.out.println(p+"--------------------------------------------------------------------------");
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }

    public static void detail(Cang c,Connection con,Proxy proxy) throws SQLException {
        String sql="insert into si_company(g_id,quan_cheng,zhong_jian,ying_jian,hang_ye,chengli_time,g_type,zhuce_didian,zhuce_ziben,fa_ren,zuzhi_daima,gongshang_hao,de_sc) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        int p=0;
        while (true){
            try{
                String[] value=c.qu();
                String url=value[0];
                String gid=value[1];
                Document doc=null;
                while (true) {
                    try {
                        doc = Jsoup.connect(url)
                                .header("Cookie", "userName=%E5%BC%A0%E5%BC%80; userId=5CDAB65D-80E4-4108-B76E-271753FBAA2F; userStatus=%E8%BF%87%E6%9C%9F; userPsd=e4af27a8396968e1a3588a198bb13d18; JSESSIONID=702A01487FB608E4968B12A5E2C7A305; firstEnterUrlInSession=http%3A//www.pedata.cn/; VisitorCapacity=1; operatorId=31183; pageReferrInSession=https%3A//www.baidu.com/link%3Furl%3DV8-iHcGKs-QrNa5waom5pRgzbfwLT5jp65FXhC566Si%26wd%3D%26eqid%3D8b72cfb7000108ec00000003599f84cf; Hm_lvt_787334dc3d58f9a34c5292796f0b9185=1503570321,1503572515,1503574566,1503626453; Hm_lpvt_787334dc3d58f9a34c5292796f0b9185=1503626458")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .timeout(5000)
                                .proxy(proxy)
                                .get();
                        if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("abuyun")) {
                            break;
                        }
                    }catch (Exception e){
                        System.out.println("deteil time out");
                    }
                }
                String quancheng=getString(doc,"div.gw_qy_one_top h1",0);
                String jianchengzhong=getString(doc,"div.sy_main li:contains(中文简称)",0).replace("中文简称：","");
                String yingjian=getString(doc,"div.sy_main li:contains(英文简称)",0).replace("英文简称：","");
                String hangye=getString(doc,"div.sy_main li:contains(行业)",0).replace("行业：","");
                String chenglishijian=getString(doc,"div.sy_main li:contains(成立时间)",0).replace("成立时间：","");
                String gongsileixing=getString(doc,"div.sy_main li:contains(公司类型)",0).replace("公司类型：","");
                String zhucedidian=getString(doc,"div.sy_main li:contains(注册地点)",0).replace("注册地点：","");
                String zhuceziben=getString(doc,"div.sy_main li:contains(注册资本)",0).replace("注册资本：","");
                String faren=getString(doc,"div.sy_main li:contains(法人代表)",0).replace("法人代表：","");
                String zuzhijigou=getString(doc,"div.sy_main li:contains(组织机构代码)",0).replace("组织机构代码：","");
                String gongshang=getString(doc,"div.sy_main li:contains(工商注册号)",0).replace("工商注册号：","");
                String jianjie=getString(doc,"div.gw_qy_one p",0);

                ps.setString(1,gid);
                ps.setString(2,quancheng);
                ps.setString(3,jianchengzhong);
                ps.setString(4,yingjian);
                ps.setString(5,hangye);
                ps.setString(6,chenglishijian);
                ps.setString(7,gongsileixing);
                ps.setString(8,zhucedidian);
                ps.setString(9,zhuceziben);
                ps.setString(10,faren);
                ps.setString(11,zuzhijigou);
                ps.setString(12,gongshang);
                ps.setString(13,jianjie);
                ps.executeUpdate();
                p++;
                System.out.println(p+"***************************************************************************");
            }catch (Exception e){
                System.out.println("error");
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
