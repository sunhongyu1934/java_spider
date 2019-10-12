package App;

import Utils.Producer;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONObject;
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
import java.sql.SQLException;
import java.util.concurrent.*;

import static Utils.JsoupUtils.*;

/**
 * Created by Administrator on 2017/8/15.
 */
public class wandoujia {
    // 代理隧道验证信息
    final static String ProxyUser = "H30W5D0WBHL6301D";
    final static String ProxyPass = "782C396260F8755D";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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
        wandoujia w=new wandoujia();
        final Fk f=w.new Fk();
        final Url u=w.new Url();

        serach(f);

        for(int x=1;x<=5;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach2(u,f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int x=1;x<=5;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(u);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void serach(Fk f) throws IOException, InterruptedException {
        Document docs=get("http://www.wandoujia.com/category/app");
        Document doc=Jsoup.parse(docs.outerHtml().replace("<!--","").replace("-->",""));
        Elements ele=getElements(doc,"ul.app-popup.tag-popup.clearfix a,ul.game-popup.tag-popup.clearfix a");
        if(ele!=null) {
            for (Element e : ele) {
                String lie = getHref(e, "a", "href", 0);
                if(StringUtils.isNotEmpty(lie)){
                    System.out.println(lie);
                    f.fang(lie);
                }
            }
        }
    }

    public static void serach2(Url u,Fk f) throws IOException, InterruptedException {
        while (true) {
            String lie=f.qu();
            if(StringUtils.isEmpty(lie)){
                break;
            }
            Document doc = get(lie + "/1");
            for (int x = 1; x <= 300; x++) {
                try {
                    if ("javascript:;".equals(getHref(doc, "div.pagination div.page-wp.roboto a.page-item.next-page.next-disabled", "href", 0)) || StringUtils.isEmpty(getHref(doc, "div.pagination div.page-wp.roboto a.page-item.next-page", "href", 0))) {
                        break;
                    }
                    if (x >= 2) {
                        doc = get(lie + "/" + x);
                    }
                    Elements ele = getElements(doc, "ul#j-tag-list div.app-desc");
                    if (ele != null) {
                        for (Element e : ele) {
                            String url = getHref(e, "h2 a.name", "href", 0);
                            if (StringUtils.isNotEmpty(url)) {
                                u.fang(url);
                            }
                        }
                    }
                }catch (Exception e){
                    System.out.println("serach error");
                }
            }
        }
    }

    public static void detail(Url u) throws IOException, InterruptedException, SQLException, DocumentException {
        Producer producer=new Producer(false);
        //String sql="insert into wandoujia_xin_18510(a_name,a_logo,a_bao,a_xia,a_hap,a_ping,a_size,a_fen,a_tag,a_update,a_ban,a_kaifa,a_jietu,a_desc,a_all) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //PreparedStatement ps=conn.prepareStatement(sql);
        int p=0;
        while (true) {
            try {
                String url = u.qu();
                if (StringUtils.isEmpty(url)) {
                    break;
                }
                Document doc = get(url);
                String logo = getHref(doc, "div.detail-wrap div.detail-top.clearfix div.app-icon img", "src", 0);
                String baoming = url.split("/", 4)[3];
                String ming = getString(doc, "div.app-info p.app-name span.title", 0);
                String xiazai = getString(doc, "div.num-list span.item i", 0);
                String haoping = getString(doc, "div.num-list span.item.love i", 0);
                String pinglun = getString(doc, "div.num-list a.item.last.comment-open i", 0);
                String size = getString(doc, "div.col-right div.infos dl.infos-list dd", 0);
                String fenlei = getString(doc, "div.col-right div.infos dl.infos-list dd.tag-box", 0).replace(" ", ";");
                String tag = getString(doc, "div.col-right div.infos dl.infos-list dd div.side-tags.clearfix", 0).replace(" ", ";");
                String gengxin = getString(doc, "div.col-right div.infos dl.infos-list dd time#baidu_time", 0);
                String banben = getString(doc, "div.col-right div.infos dl.infos-list dd", 4);
                String kaifa = getString(doc, "div.col-right div.infos dl.infos-list dd span.dev-sites", 0);
                Elements jieele = getElements(doc, "div.screenshot div.overview img");
                String jietu = "";
                StringBuffer str = new StringBuffer();
                if (jieele != null&&jieele.toString().length()>0) {
                    for (Element e : jieele) {
                        str.append(getHref(e, "img", "src", 0) + ";");
                    }
                    jietu = str.substring(0, str.length() - 1);
                }
                String desc = getString(doc, "div.desc-info div.con", 0);
                String haizai = "";
                StringBuffer str2 = new StringBuffer();
                Elements hele = getElements(doc, "ol.clearfix.pc-download.log-param-f li.new-app");
                if (hele != null&&hele.toString().length()>0) {
                    for (Element e : hele) {
                        str2.append(getString(e, ">a", 1) + ";");
                    }
                    haizai = str2.substring(0, str2.length() - 1);
                }

                /*ps.setString(1, ming);
                ps.setString(2, logo);
                ps.setString(3, baoming);
                ps.setString(4, xiazai);
                ps.setString(5, haoping);
                ps.setString(6, pinglun);
                ps.setString(7, size);
                ps.setString(8, fenlei);
                ps.setString(9, tag);
                ps.setString(10, gengxin);
                ps.setString(11, banben);
                ps.setString(12, kaifa);
                ps.setString(13, jietu);
                ps.setString(14, desc);
                ps.setString(15, haizai);
                ps.executeUpdate();*/

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("familyname","wandoujia");
                jsonObject.put("tablename","app_base_info");
                jsonObject.put("rowkey","app_name+app_name###develop_name###app_package###familyname");
                jsonObject.put("app_id","createid=app_name###develop_name");
                jsonObject.put("app_name",ming);
                jsonObject.put("app_sort",fenlei);
                jsonObject.put("develop_name",kaifa);
                jsonObject.put("app_intro",desc);
                jsonObject.put("app_update",gengxin);
                jsonObject.put("app_total_download",xiazai);
                jsonObject.put("app_logo",logo);
                jsonObject.put("app_package",baoming);
                jsonObject.put("feedback_rate",haoping);
                jsonObject.put("app_total_comment",pinglun);
                jsonObject.put("app_size",size);
                jsonObject.put("app_tag",tag);
                jsonObject.put("app_version",banben);
                jsonObject.put("app_screenshot",jietu);
                jsonObject.put("app_other_like",haizai);
                producer.send("ControlTotal",jsonObject.toString());
                p++;
                System.out.println(u.bo.size()+"****************************************************************");
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
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (!doc.outerHtml().contains("获取验证码") && !doc.outerHtml().contains("abuyun")&&doc.outerHtml().length()>50) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
    }


    class Url {
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.poll(60, TimeUnit.SECONDS);
        }
    }

    class Fk{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.poll(10,TimeUnit.SECONDS);
        }
    }
}
