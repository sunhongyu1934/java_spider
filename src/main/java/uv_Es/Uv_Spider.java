package uv_Es;

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

public class Uv_Spider {
    // 代理隧道验证信息
    final static String ProxyUser = "H88A4Q10G0V31YCD";
    final static String ProxyPass = "C2E28C06C89836C4";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int j=0;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
    public static void main(String args[]) throws IOException {
        Uv_Spider u=new Uv_Spider();
        Ca c=u.new Ca();
        Pa p=u.new Pa();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    con(p);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=10;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach(p,c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int b=1;b<=20;b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void con(Pa p) throws InterruptedException {
        for(int a=0;a<=17329;a++){
            p.fang(String.valueOf(a));
        }
    }

    public static void serach(Pa p,Ca c) throws IOException, InterruptedException {
        while (true) {
            try {
                String ye = p.qu();
                Document doc = get("http://cy.ncss.org.cn/search/projectlist?name=&industryCode=&typeCode=&wasBindUniTechnology=-9&investStageCode=&provinceCode=&pageIndex=" + ye + "&pageSize=15");
                Elements ele = JsoupUtils.getElements(doc, "div.search-list-item");
                for (Element e : ele) {
                    try {
                        String logo = "http://cy.ncss.org.cn" + JsoupUtils.getHref(e, "div.project-list-face img", "src", 0);
                        String url = "http://cy.ncss.org.cn" + JsoupUtils.getHref(e, "div.project-list-info a", "href", 0);
                        c.fang(new String[]{url, logo});
                    }catch (Exception ee){
                        System.out.println("se fang error");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("serach error");
            }
        }
    }

    public static void detail(Ca c) throws InterruptedException, SQLException {
        String sql="insert into daxuesheng_pro(pro_name,pro_logo,pro_tag,pro_hy,pro_add,pro_desc,pro_jd,team_name,team_desc) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        while (true){
            try {
                String[] value = c.qu();
                Document doc=get(value[0]);
                String proname=JsoupUtils.getString(doc,"div.project-title-block div.project-title h1",0);
                String tag=null;
                StringBuffer tags=new StringBuffer();
                Elements tagele=JsoupUtils.getElements(doc,"div.project-label span");
                for(Element e:tagele){
                    tags.append(e.text()+",");
                }
                tag=chu(tags);
                String hy=null;
                StringBuffer hys=new StringBuffer();
                Elements hyele=JsoupUtils.getElements(doc,"p.project-label span");
                for(Element e:hyele){
                    hys.append(e.text()+",");
                }
                hy=chu(hys);
                String add=JsoupUtils.getString(doc,"p.project-label.mt10",0);
                String xmjj=JsoupUtils.getString(doc,"div.project-block-content.pb10 div:contains(项目简介) p",0);
                String xmjz=JsoupUtils.getString(doc,"div.project-block-content.pb10 div:contains(项目进展) p",0);
                String tdmc=JsoupUtils.getString(doc,"div.project-block-content.project-block-bottom div:contains(团队名称) p",0);
                String tdjs=JsoupUtils.getString(doc,"div.project-block-content.project-block-bottom div:contains(团队介绍) p",0);

                ps.setString(1,proname);
                ps.setString(2,value[1]);
                ps.setString(3,tag);
                ps.setString(4,hy);
                ps.setString(5,add);
                ps.setString(6,xmjj);
                ps.setString(7,xmjz);
                ps.setString(8,tdmc);
                ps.setString(9,tdjs);
                ps.executeUpdate();
                j++;
                System.out.println(j+"**************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
    }


    public static Document get(String url) throws IOException {
        Document doc=null;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .header("Referer", "http://cy.ncss.org.cn/search/projects")
                        .header("Host", "cy.ncss.org.cn")
                        .header("Accept-Language", "zh-CN,zh;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept", "*/*")
                        .proxy(proxy)
                        .timeout(5000)
                        .get();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out detail");
            }
        }
        return doc;
    }

    public static String chu(StringBuffer str){
        if(str!=null&&str.length()>2){
            try {
                return str.substring(0, str.length() - 1);
            }catch (Exception e){
                return null;
            }
        }else{
            return null;
        }
    }

    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String key[]) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

    class Pa{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
