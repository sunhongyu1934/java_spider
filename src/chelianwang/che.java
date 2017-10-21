package chelianwang;

import Utils.Dup;
import Utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class che {
    // 代理隧道验证信息
    final static String ProxyUser = "H88A4Q10G0V31YCD";
    final static String ProxyPass = "C2E28C06C89836C4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int su=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }
    public static void main(String args[]) throws IOException, InterruptedException {
        ExecutorService pool= Executors.newCachedThreadPool();
        final che c=new che();
        final Ca cc=c.new Ca();
        final Ui u=c.new Ui();


        Document doc=Jsoup.connect("http://www.shujubang.com/Htmls/CompanyInfo/Company_0_1.html")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(10000)
                .get();

        Elements ele= JsoupUtils.getElements(doc,"div.mainbox div.mainbox_left div.menu a");
        for(Element e:ele){
            final String url="http://www.shujubang.com"+"/Shujubang/CategoryCompanyInfo.aspx?Id="+JsoupUtils.getHref(e,"a","href",0).replace("/Htmls/CompanyInfo/Company_","").replace("_1.html","")+"&Page=";
            final String lei=JsoupUtils.getString(e,"a",0);
            u.fang(new String[]{url,lei});
        }
        u.fang(new String[]{"http://www.shujubang.com/Shujubang/CategoryCompanyInfo.aspx?Id=0&Page=",""});


        for(int y=1;y<=10;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(cc,u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(cc);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void serach(Ca c,Ui u) throws IOException, InterruptedException {
        while (true) {
            String[] va=u.qu();
            for (int p = 1; p <= 162; p++) {
                try {
                    Document doc = get(va[0] + p);
                    Elements ele = JsoupUtils.getElements(doc, "div.mainbox_right div.listnews");
                    boolean bo = true;
                    for (Element e : ele) {
                        bo = false;
                        String url = JsoupUtils.getHref(e, "div.news_info div.news_title a", "href", 0);
                        Elements tags = JsoupUtils.getElements(e, "div.dvcategory a");
                        StringBuffer str = new StringBuffer();
                        for (Element ee : tags) {
                            str.append(ee.text() + ",");
                        }
                        String tag = null;
                        if (str != null && str.toString().length() > 2) {
                            tag = str.toString().substring(0, str.toString().length() - 1);
                        }
                        String logo = JsoupUtils.getHref(e, "div.listnews div.news_pic img", "src", 0);
                        if (Dup.nullor(url)) {
                            c.fang(new String[]{"http://www.shujubang.com" + url, tag, "http://www.shujubang.com" + logo, va[1]});
                        }
                    }
                    if (bo) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("serach error");
                }
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, IOException, SQLException {
        String sql="insert into chelianwang_zuosi_xin(comp_short_name,comp_full_name,comp_eng_name,comp_guo,create_time,comp_logo,shang_dai,shang_shi,zhu_zi,yuan_sum,zong_di,w_eb,yewu_ling,hang_di,de_sc,fa_zhan,comp_tags,comp_fenlei) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String[] url = c.qu();
                Document doc = get(url[0]);
                String jian = JsoupUtils.getString(doc, "div.ps", 0);
                String zh = JsoupUtils.getString(doc, "div.dvtable table tbody td", 0);
                String ying = JsoupUtils.getString(doc, "div.dvtable table tbody td", 1);
                String guo = JsoupUtils.getString(doc, "div.dvtable table tbody td", 2);
                String chengli = JsoupUtils.getString(doc, "div.dvtable table tbody td", 3);
                String shangdai = JsoupUtils.getString(doc, "div.dvtable table tbody td", 4);
                String shangshi = JsoupUtils.getString(doc, "div.dvtable table tbody td", 5);
                String zhuzi = JsoupUtils.getString(doc, "div.dvtable table tbody td", 6);
                String yuan = JsoupUtils.getString(doc, "div.dvtable table tbody td", 7);
                String zong = JsoupUtils.getString(doc, "div.dvtable table tbody td", 8);
                String web = JsoupUtils.getString(doc, "div.dvtable table tbody td", 9);
                String yewu = JsoupUtils.getString(doc, "div.dvtable table tbody td", 10);
                String hangdi = JsoupUtils.getString(doc, "div.dvtable table tbody td", 11);
                String jianjie = JsoupUtils.getString(doc, "div.dvtable table tbody td", 12);
                String fazhan = JsoupUtils.getString(doc, "div.dvtable table tbody td", 14);

                ps.setString(1, jian);
                ps.setString(2, zh);
                ps.setString(3, ying);
                ps.setString(4, guo);
                ps.setString(5, chengli);
                ps.setString(6, url[2]);
                ps.setString(7, shangdai);
                ps.setString(8, shangshi);
                ps.setString(9, zhuzi);
                ps.setString(10, yuan);
                ps.setString(11, zong);
                ps.setString(12, web);
                ps.setString(13, yewu);
                ps.setString(14, hangdi);
                ps.setString(15, jianjie);
                ps.setString(16, fazhan);
                ps.setString(17, url[1]);
                ps.setString(18,url[3]);
                ps.executeUpdate();
                su++;
                System.out.println(su + "**************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }

    public static Document get(String url) throws IOException {
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(10000)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && doc.outerHtml().length() > 44) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time our error");
            }
        }
       return doc;
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Ui{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
