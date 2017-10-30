package linshi_spider;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/6/16.
 */
public class tyc_sou {
    // 代理隧道验证信息
    final static String ProxyUser = "H4XGPM790E93518D";
    final static String ProxyPass = "2835A47D56143D62";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException, InterruptedException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
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


        tyc_sou t=new tyc_sou();
        final cangku ca=t.new cangku();
        ExecutorService pool= Executors.newCachedThreadPool();

        final Connection finalCon = con;
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(finalCon,ca);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=40;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(ca,finalCon,proxy);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }






    }

    public static void data(Connection con,cangku c) throws SQLException, IOException, InterruptedException {
        int p=0;
        for(int x=1;x<=10;x++) {
            String sql = "select id,`Name` from qichacha_search where Province='SH' limit "+p+",200000";
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("kaishi duqu");
            ResultSet rs = ps.executeQuery();
            System.out.println("duqu success");
            while (rs.next()) {
                String quancheng = rs.getString(rs.findColumn("Name"));
                String xuhao = rs.getString(rs.findColumn("id"));

                String st[] = new String[]{ quancheng, xuhao};
                c.fang(st);
            }
            p=p+200000;
        }

    }

    public static Document geturl(String url,Proxy proxy) throws IOException {
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }



    public static void get(cangku ca,Connection con,Proxy proxy) throws InterruptedException, SQLException, IOException {
        String sql="insert into tyc_jichu_sh_baidu(quan_cheng,logo,p_hone,e_mail,a_ddress,w_eb,fa_ren,zhuce_ziben,zhuce_shijian,jingying_zhuangtai,gongshang_hao,zuzhijigou_daima,tongyi_xinyong,qiye_leixing,hang_ye,yingye_nianxian,dengji_jiguan,zhuce_dizhi,jingying_fanwei,t_id,xu_hao,yuan_cheng) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);


        int a=0;
        while (true) {
            try {
                String[] value = ca.qu();
                String ming = value[0];
                String id = value[1];

                Document doc = null;
                while (true) {
                    try {
                        doc = Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&rsv_idx=1&tn=62095104_oem_dg&wd=" + URLEncoder.encode("天眼查 " + ming, "UTF-8") + "&oq=%25E5%2595%25A6%25E5%2595%25A6%25E5%2595%25A6&rsv_pq=d6fc72770002ceba&rsv_t=a6c2BlebcoVZi%2BRTkphpRYdVDybiB1m9b65dKx6YfGmlG8f0vBncuO9XwJHsqPtBAjiWLif2&rqlang=cn&rsv_enter=0&prefixsug=%25E5%2595%25A6%25E5%2595%25A6%25E5%2595%25A6&rsp=3")
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                .ignoreHttpErrors(true)
                                .ignoreContentType(true)
                                //.proxy(proxy)
                                .timeout(50000)
                                .get();
                        if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("time out reget");
                    }
                }
                Elements ele = getElements(doc, "div#content_left div.result.c-container");
                if (ele != null) {
                    for (Element e : ele) {
                        try {
                            String name = getString(e, "div.c-abstract", 0);
                            String flag = getString(e, "div.result.c-container div.f13 a", 0);
                            if (flag.contains("www.tianyancha.com")) {
                                String url = getHref(e, "a.m", "href", 0);
                                Document doct = geturl(url, proxy);
                                String tid = getHref(doct, "div#bd_snap_note a", "href", 0).split("/", 5)[4];
                                String logo = getHref(doct, "div.company_info img", "src", 0);
                                String na = getString(doct, "div.company_info_text p.ng-binding", 0);
                                String phone = getString(doct, "span.ng-binding:contains(电话)", 0).replace("电话:", "");
                                String email = getString(doct, "span.ng-binding:contains(邮箱)", 0).replace("邮箱:", "");
                                String web = getString(doct, "span:contains(网址)", 0).replace("网址:", "");
                                String address = getString(doct, "span.ng-binding:contains(地址)", 0).replace("地址:", "");
                                String faren = getString(doct, "td.td-legalPersonName-value.c9 a.ng-binding.ng-scope", 0);
                                String zhuceziben = getString(doct, "td.td-regCapital-value p.ng-binding", 0);
                                String statu = getString(doct, "td.td-regStatus-value p.ng-binding", 0);
                                String zhuceshijian = getString(doct, "td.td-regTime-value p.ng-binding", 0);
                                String hangye = getString(doct, "div.c8:contains(行业) span.ng-binding", 0);
                                String gongshang = getString(doct, "div.c8:contains(工商注册号) span.ng-binding", 0);
                                String qiyeleixing = getString(doct, "div.c8:contains(企业类型) span.ng-binding", 0);
                                String zuzhijigou = getString(doct, "div.c8:contains(组织机构代码) span.ng-binding", 0);
                                String yingyeqixian = getString(doct, "div.c8:contains(营业期限) span.ng-binding", 0);
                                String dengjijiguan = getString(doct, "div.c8:contains(登记机关) span.ng-binding", 0);
                                String hezhunriqi = getString(doct, "div.c8:contains(核准日期) span.ng-binding", 0);
                                String tongyixinyong = getString(doct, "div.c8:contains(统一信用代码) span.ng-binding", 0);
                                String zhucedizhi = getString(doct, "div.c8:contains(注册地址) span.ng-binding", 0);
                                String jingyingfanwei = getString(doct, "div.c8:contains(经营范围) span.ng-binding", 0);

                                ps.setString(1, na);
                                ps.setString(2, logo);
                                ps.setString(3, phone);
                                ps.setString(4, email);
                                ps.setString(5, address);
                                ps.setString(6, web);
                                ps.setString(7, faren);
                                ps.setString(8, zhuceziben);
                                ps.setString(9, zhuceshijian);
                                ps.setString(10, statu);
                                ps.setString(11, gongshang);
                                ps.setString(12, zuzhijigou);
                                ps.setString(13, tongyixinyong);
                                ps.setString(14, qiyeleixing);
                                ps.setString(15, hangye);
                                ps.setString(16, yingyeqixian);
                                ps.setString(17, dengjijiguan);
                                ps.setString(18, zhucedizhi);
                                ps.setString(19, jingyingfanwei);
                                ps.setString(20, tid);
                                ps.setString(21, id);
                                ps.setString(22, ming);
                                ps.executeUpdate();
                                a++;
                                System.out.println(a + "*******************************************************");
                            }
                        }catch (Exception e1){
                            System.out.println("ben tiao erroe");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }



    }



    class cangku{
        BlockingQueue<String[]> blo=new LinkedBlockingQueue<String[]>(10000);
        public void fang(String key[]) throws InterruptedException {
            blo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return blo.poll(600, TimeUnit.SECONDS);
        }
    }

}
