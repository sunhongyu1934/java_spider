package gongzhonghao;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.Producer;
import Utils.RedisClu;
import haosou.haosouBean;
import org.apache.poi.ss.formula.functions.T;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class spider {
    private static Connection conn;
    private static RedisClu rd=new RedisClu();
    private static Ca c=new Ca();
    private static Uu u=new Uu();
    private static int sa=0;
    private static int sb=0;

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
        conn=con;
        try {
            String sql = "select id,comp_full_name from innotree_data_financing.wecat_serach where mark_time='' or mark_time is null";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String time=simpleDateFormat.format(date);
            String sql2="update innotree_data_financing.wecat_serach set mark_time='"+time+"' where id=?";
            PreparedStatement ps2=conn.prepareStatement(sql2);
            while (rs.next()){
                String se=rs.getString(rs.findColumn("comp_full_name"));
                String id=rs.getString(rs.findColumn("id"));
                rd.set("wecat_zl",id+"###"+se);
                ps2.setInt(1, Integer.parseInt(id));
                ps2.executeUpdate();
            }
        }catch (Exception e){

        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
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
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int a=1;a<=10;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serach();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for(int b=1;b<=10;b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                System.out.println("结束了！");
                System.exit(0);
            }
            Thread.sleep(2000);
        }
    }



    public static void serach() throws IOException, DocumentException, InterruptedException {
        Producer producer=new Producer(false);
        while (true) {
            try {
                String key = rd.get("wecat_zl");
                if (!Dup.nullor(key)) {
                    break;
                }
                for (int q = 1; q <= 10; q++) {
                    int pp=0;
                    try {
                        Document doc = serachGet(key.split("###")[1], String.valueOf(q));
                        Pattern pat = Pattern.compile("<script>var account_anti_url =.+?</script>");
                        Matcher mat = pat.matcher(doc.outerHtml());
                        String urlc = null;
                        while (mat.find()) {
                            urlc = mat.group().replace("<script>var account_anti_url = \"", "").replace("\";</script>", "");
                        }
                        String json = Dup.qujson(getShu(urlc));
                        //System.out.println(json);
                        JSONObject jsonObject1 =new JSONObject();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            jsonObject1 = jsonObject.getJSONObject("msg");
                        }catch (Exception e){

                        }
                        Elements ele = JsoupUtils.getElements(doc, "div.news-box ul.news-list2 li");
                        for (Element e : ele) {
                            try {
                                String gname = JsoupUtils.getString(e, "div.gzh-box2 div.txt-box p", 0);
                                String url = JsoupUtils.getHref(e, "div.gzh-box2 div.txt-box p a", "href", 0);
                                String wei = JsoupUtils.getString(e, "div.gzh-box2 div.txt-box p label", 0);
                                String logo = JsoupUtils.getHref(e, "div.gzh-box2 div.img-box a img", "src", 0);
                                String pub_intro = JsoupUtils.getString(e, "dd", 0);
                                String comp = JsoupUtils.getString(e, "dd", 1);
                                String wenzhangshu=null; ;
                                try {
                                    wenzhangshu = getValue(jsonObject1, logo.split("/")[6]) != null
                                            ? getValue(jsonObject1, logo.split("/")[6]).split(",")[0]
                                            : null;
                                }catch (Exception ee1){

                                }

                                String zongwen = null;
                                try {
                                    zongwen = getValue(jsonObject1, logo.split("/")[6]) != null
                                            ? getValue(jsonObject1, logo.split("/")[6]).split(",")[1]
                                            : null;
                                }catch (Exception ee1){

                                }

                                u.fang(new String[]{url,wei});
                                JSONObject js = new JSONObject();
                                js.put("comp_full_name", comp);
                                js.put("comp_id", "createid=comp_full_name");
                                js.put("pub_name", gname);
                                js.put("pub_id", "createid=pub_name");
                                js.put("pub_intro", pub_intro);
                                js.put("pub_logo", logo);
                                js.put("detail_url", url);
                                js.put("wechart_num", wei);
                                js.put("title_num", wenzhangshu);
                                js.put("title_num_total", zongwen);
                                js.put("rowkey", "comp_full_name+comp_full_name###pub_name###familyname");
                                js.put("tablename", "public_account_info");
                                js.put("familyname", "sogou");
                                producer.send("ControlTotal", js.toString());
                                sa++;
                                System.out.println("serach      "+sa+"******************************************************"+rd.getslength("wecat_zl"));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            pp++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(pp<8){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void detail() throws UnsupportedEncodingException, FileNotFoundException, DocumentException, InterruptedException {
        Producer producer=new Producer(false);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            try {
                String key[] = u.qu();
                if (key==null && !rd.getExists("wecat_zl")&&u.po.size()==0) {
                    break;
                }
                Document doc = detailGet(key[0]);
                String gname = JsoupUtils.getString(doc, "div.profile_info strong.profile_nickname", 0);
                String wei = key[1];
                String jie = JsoupUtils.getString(doc, "ul.profile_desc li div.profile_desc_value", 0);
                String logo = JsoupUtils.getHref(doc, "div.profile_info_group span.radius_avatar.profile_avatar img", "src", 0);
                String zhuti = JsoupUtils.getString(doc, "ul.profile_desc div.profile_desc_value", 1);

                Pattern pattern = Pattern.compile("var biz =.*?\".+?\"");
                Matcher matcher = pattern.matcher(doc.outerHtml());
                String biz = null;
                while (matcher.find()) {
                    biz = matcher.group();
                }
                biz = biz.replace("var biz = \"", "").replace("\"", "");
                Pattern pat = Pattern.compile("var msgList =.+?};");
                Matcher mat = pat.matcher(doc.outerHtml());
                String json = null;
                while (mat.find()) {
                    json = mat.group();
                }
                json = json.replace("var msgList = ", "").replace(";", "");
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int a = 0; a < jsonArray.length(); a++) {
                    try {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(a).getJSONObject("app_msg_ext_info");
                        String title = getValue(jsonObject1, "title");
                        String titieurl = getValue(jsonObject1, "content_url");
                        String img = getValue(jsonObject1, "cover");
                        String jian = getValue(jsonObject1, "digest");
                        String shijian = jsonArray.getJSONObject(a).getJSONObject("comm_msg_info").get("datetime").toString();
                        shijian = simpleDateFormat.format(new Date(Long.parseLong(shijian) * 1000));

                        JSONObject js=new JSONObject();
                        js.put("rowkey", "wechart_num+wechart_num###title###familyname");
                        js.put("tablename", "public_title_info");
                        js.put("familyname", "sogou");
                        js.put("pub_name",gname);
                        js.put("pub_id","createid=pub_name");
                        js.put("title_id","createid=title");
                        js.put("title",title);
                        js.put("title_url",titieurl);
                        js.put("pub_logo",logo);
                        js.put("pub_intro",jie);
                        js.put("wechart_num",wei);
                        js.put("pub_develop",zhuti);
                        js.put("title_logo",img);
                        js.put("title_intro",jian);
                        js.put("publish_date",shijian);
                        js.put("b_id",biz);
                        producer.send("ControlTotal", js.toString());
                        sb++;
                        System.out.println("title   "+sb+"****************************************************"+u.po.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Document detailGet(String url){
        Document doc= null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Host", "mp.weixin.qq.com")
                         .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(3000)
                        .get();
                if(doc!=null&&!doc.outerHtml().contains("too many request")&&doc.outerHtml().length()>46&&!doc.outerHtml().contains("请输入验证码")){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){

            }
        }
        return doc;
    }

    public static String getValue(JSONObject jsonObject,String key){
        try{
            return jsonObject.get(key).toString();
        }catch (Exception e){
            return null;
        }
    }

    public static Document getShu(String str){
        Document doc=null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect("http://weixin.sogou.com"+str)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "weixin.sogou.com")
                         .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(3000)
                        .get();
                if(doc!=null&&!doc.outerHtml().contains("too many request")&&doc.outerHtml().length()>46){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){

            }
        }
        return doc;
    }

    public static Document serachGet(String key,String page) throws IOException {
        Document doc= null;
        while (true) {
            try {
                String ip=c.qu();
                doc = Jsoup.connect("http://weixin.sogou.com/weixin?query="+URLEncoder.encode(key,"utf-8")+"&_sug_type_=&s_from=input&_sug_=y&type=1&page="+page+"&ie=utf8")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "weixin.sogou.com")
                        .header("Referer", "http://weixin.sogou.com/")
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(3000)
                        .get();
                if(doc!=null&&!doc.outerHtml().contains("too many request")&&doc.outerHtml().length()>9000){
                    if (!c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){

            }
        }
        return doc;
    }

    public static void getip() throws IOException, InterruptedException {
        RedisClu rd=new RedisClu();
        while (true) {
            try {
                String ip=rd.get("ip");
                c.fang(ip);
                System.out.println(c.po.size()+"    ip***********************************************");
                Thread.sleep(4000);
            }catch (Exception e){
                Thread.sleep(1000);
                System.out.println("ip wait");
            }
        }
    }

    public static void conip() throws InterruptedException {
        while (true){
            if(c.po.size()>=10) {
                c.qu();
            }
            Thread.sleep(1000);
        }
    }
    public static class Ca{
        public BlockingQueue<String> po=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    public static class Uu{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>(100);
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(20, TimeUnit.SECONDS);
        }
    }
}
