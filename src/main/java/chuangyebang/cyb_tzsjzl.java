package chuangyebang;

import Utils.Dup;
import Utils.RedisClu;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static Utils.JsoupUtils.*;
import static chuangyebang.cyb_tzsj.storedata;

/**
 * Created by Administrator on 2017/8/11.
 */
public class cyb_tzsjzl {
    private static Ca c=new Ca();
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ParseException {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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
        get(con);

    }

    public static void get(Connection con) throws IOException, InterruptedException, ParseException, SQLException {
        int p=1;
        while (true) {
            try {
                Document doc = get("https://www.cyzone.cn/event/list-0-"+p+"-0-0-0-0/0");
   /*             OutputStream outputStream=new FileOutputStream("aaa.html");
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
                bufferedWriter.write(doc.outerHtml());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();*/
                Elements ele = getElements(doc, "div.list-table3 table>tbody>tr.table-plate3");
                int pp = 1;
                if (ele != null) {
                    Date datee = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date1 = simpleDateFormat.format(datee);
                    long dd1 = simpleDateFormat.parse(date1).getTime();
                    long dd2 = dd1 - (4 - 1) * 24 * 60 * 60 * 1000;
                    for (Element e : ele) {
                        String time = getString(e, ">td:nth-last-child(2)", 0);
                        long date3 = simpleDateFormat.parse(time).getTime();
                        if (date3 <= dd2) {
                            break;
                        }
                        pp++;
                    }
                }
                Elements dele = getElements(doc, "div.list-table3 table>tbody>tr.table-plate3");
                boolean br = false;
                int oo = 1;
                if (dele != null) {
                    for (Element e : dele) {
                        try {
                            if (pp > 1 && oo == pp - 1 && pp<20 || pp == 1 && oo == pp) {
                                br = true;
                                break;
                            }
                            String detailurl = "http:"+getHref(e, "td.tp2 span.tp2_tit a", "href", 0);
                            String cid = detailurl.split("/", 5)[4].replace(".html","");
                            String[] table = new String[]{"cyb_company", "cyb_dt", "cyb_finacing", "cyb_founders", "cyb_news", "cyb_tag"};
                            for (int g = 0; g < table.length; g++) {
                                flagdata(con, cid, table[g]);
                            }
                            getdetail(detailurl, con);
                        }catch (Exception e1){

                        }
                        oo++;
                    }
                }
                if (br) {
                    break;
                }
                p++;
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }
        System.exit(0);
    }

    public static void flagdata(Connection con,String cid,String table) throws SQLException {
        String sql1="delete from "+table+" where cid='"+cid+"'";
        PreparedStatement ps1=con.prepareStatement(sql1);
        ps1.executeUpdate();
    }

    public static void getdetail(String url,Connection con) throws SQLException, InterruptedException, IOException {
        Document doc=null;
        while (true) {
            try {
                String proxyIpAndPort = c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .timeout(5000)
                        .get();
                if (Dup.nullor(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.outerHtml().contains("abuyun")) {
                    if (!c.po.contains(proxyIpAndPort)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(proxyIpAndPort);
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                Thread.sleep(1000);
                System.out.println(url);
                System.out.println("time out detail");
            }
        }
        System.out.println("request detail success and begin store data");
        storedata(con, doc, url);
        System.out.println("success_cyb-tzsjzl");
        System.out.println("insert mysql success");
        System.out.println("-----------------------------------------------------------------");
    }

    public static Document get(String url) throws IOException, InterruptedException {
        Document doc;
        while (true) {
            try {
                String proxyIpAndPort = c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxyIpAndPort.split(":")[0], Integer.parseInt(proxyIpAndPort.split(":")[1]))
                        .timeout(5000)
                        .get();
                if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())  && !doc.outerHtml().contains("abuyun")) {
                    if (!c.po.contains(proxyIpAndPort)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(proxyIpAndPort);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("time out");
            }
        }
        return doc;
    }

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                /*Document doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=552166bfe40bf4f7af05ae2b6c6ccd2a&sep=3")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String[] ips = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").trim().split(" ");
                for(String s:ips){
                    if (s.contains("requests") || s.contains("请控制")) {
                        continue;
                    }
                    c.fang(s.trim());
                }*/
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(2000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static void conip() throws InterruptedException {
        while (true){
            if(c.po.size()>=5) {
                c.qu();
            }
            Thread.sleep(1000);
        }
    }

    public static class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
