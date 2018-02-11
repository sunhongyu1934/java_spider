package baidu;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/8/14.
 */
public class news_xin {
    public static void main(String args[]){
        news_xin n=new news_xin();
        final Cang c=n.new Cang();

        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=25;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        get(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    public static void data(Cang c) throws SQLException, InterruptedException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Connection con=getcon();
        String sql="select project_nm from baidu_news_project";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String pname=rs.getString(rs.findColumn("project_nm"));
            c.fang(pname);
        }
        con.close();
    }

    public static void data2(Cang c) throws SQLException, InterruptedException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Connection con=getcon();
        String sql="select comp_name from gaoxin_qiyemingdan";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String pname=rs.getString(rs.findColumn("comp_name"));
            c.fang(pname);
        }
        con.close();
    }

    public static void get(Cang c) throws IOException, InterruptedException, SQLException {
        String sql="insert into baidu_news_newpro(p_name,co_ount,de_count,da_te) values(?,?,?,?)";
        PreparedStatement ps=null;
        Connection con=null;
        int p=0;
        while (true) {
            try {
                String value = c.qu();
                if(StringUtils.isEmpty(value)){
                    Thread.sleep(60000);
                    System.exit(0);
                }
                if(con==null||con.isClosed()){
                    con=getcon();
                }
                ps=con.prepareStatement(sql);
                qingqiu(ps,value);
                p++;
                System.out.println(p + "******************************************************");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void qingqiu(PreparedStatement ps,String pname) throws SQLException, ParseException, InterruptedException {
        List<Integer> list1=new ArrayList<Integer>();
        List<Integer> list2=new ArrayList<Integer>();
        List<Integer> list3=new ArrayList<Integer>();
        List<Integer> list4=new ArrayList<Integer>();
        List<Integer> list5=new ArrayList<Integer>();
        List<Integer> list6=new ArrayList<Integer>();
        List<Integer> list7=new ArrayList<Integer>();
        String sou="0";
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        String da=simpleDateFormat.format(date);
        long d1=simpleDateFormat.parse(da).getTime();
        int p=0;
        while (true) {
            int bl=0;
            Document doc = null;
            while (true) {
                try {
                    doc = Jsoup.connect("http://news.baidu.com/ns?word=title%3A%28" + URLEncoder.encode(pname, "UTF-8") + "%29&pn=" + p + "&cl=2&ct=1&tn=newstitle&rn=20&ie=utf-8&bt=0&et=0")
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .timeout(5000)
                            .get();
                    if (StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("页面不存在")) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("time out");
                }finally {
                    bl++;
                    if(bl>=50){
                        System.out.println("begin sleep");
                        Thread.sleep(600000);
                    }
                }
            }
            sou = getString(doc, "div#header_top_bar span.nums", 0).replace("找到相关新闻约", "").replace("找到相关新闻","").replace("篇", "").replace(",", "");
            if(sou.equals("0")){
                break;
            }
            Elements ele=getElements(doc,"div#wrapper_wrapper div.result.title");
            boolean br=false;
            int pp=0;
            for(Element e:ele){
                String va[]=Jsoup.parse(e.select("div.c-title-author").toString().replace("&nbsp;"," ")).text().split(" ");
                if(!va[1].contains("小时前")||!va[1].contains("分钟前")){
                    if(va.length==4){
                        long d3=simpleDateFormat.parse(va[1]).getTime();
                        if(d3==(d1-(2-1)*24*60*60*1000)){
                            list1.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(3-1)*24*60*60*1000)){
                            list2.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(4-1)*24*60*60*1000)){
                            list3.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(5-1)*24*60*60*1000)){
                            list4.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(6-1)*24*60*60*1000)){
                            list5.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(7-1)*24*60*60*1000)){
                            list6.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3==(d1-(8-1)*24*60*60*1000)){
                            list7.add(Integer.valueOf(va[3].replace("条相同新闻>>","")));
                        }else if(d3<(d1-(9-1)*24*60*60*1000)){
                            br=true;
                            break;
                        }
                    }
                }
                pp++;
            }
            if(pp<20){
                break;
            }
            p=p+20;
            if(br){
                break;
            }
        }
        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list1)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(2-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list2)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(3-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list3)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(4-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list4)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(5-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list5)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(6-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list6)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(7-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");

        ps.setString(1,pname);
        ps.setString(2,sou);
        ps.setString(3, String.valueOf(sum(list7)));
        ps.setString(4,simpleDateFormat.format(new Date(d1-(8-1)*24*60*60*1000)));
        ps.executeUpdate();
        System.out.println("success_baidu-news");
    }

    public static Connection getcon() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
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
        return con;
    }

    public static Integer sum(List<Integer> list){
        int result=0;
        for(Integer su:list){
            result+=su;
        }
        return result;
    }


    class Cang{
        BlockingQueue<String> po=new LinkedBlockingQueue<String>(100);
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(6000, TimeUnit.SECONDS);
        }
    }
}
