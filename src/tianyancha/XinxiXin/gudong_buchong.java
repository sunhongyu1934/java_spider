package tianyancha.XinxiXin;

import baidu.RedisAction;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getString;
import static tianyancha.XinxiXin.XinxiXin.suan;

/**
 * Created by Administrator on 2017/8/21.
 */
public class gudong_buchong {
    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        System.out.println("spider begin ******************************************************************************");
        // 代理隧道验证信息
        final  String ProxyUser = args[0];
        final  String ProxyPass = args[1];

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        gudong_buchong.proxy =proxy;

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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
        gudong_buchong x=new gudong_buchong();
        final Url u=x.new Url();
        final RedisAction rs=new RedisAction("a024.hb2.innotree.org", 6379);
        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        final String[] zhua=new String[]{"基本信息","主要成员","股东信息","对外投资"};
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    duqu(rs,u);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        String xian=args[2];
        for(int a=1;a<=Integer.parseInt(xian);a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(u,finalCon,zhua);
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


    public static void duqu(RedisAction r,Url u) throws SQLException, InterruptedException {
        int p=0;
        while (true){
            try {
                if(p>=100){
                    break;
                }
                String va = r.get("tyc_buchong");
                if(StringUtils.isNotEmpty(va)) {
                    try {
                        u.put(new String[]{"https://www.tianyancha.com/company/" + va.split("\\*\\*\\*\\*\\*")[0].trim(), va.split("\\*\\*\\*\\*\\*")[1].trim()});
                    }catch (Exception e1){
                        System.out.println("danci error");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                p++;
                System.out.println("fang error");
            }
        }
    }

    public static void data(Url u,Connection con,String[] zhua) throws InterruptedException, IOException, SQLException {
        Tyc_quan_bu t=new Tyc_quan_bu(con,zhua);

        while (true){
            try {
                String[] value = u.qu();
                if(value==null||value.length<2){
                    Thread.sleep(300000);
                    System.exit(0);
                }
                String url = value[0];
                String cname = URLEncoder.encode(value[1], "UTF-8");
                String tid = url.replace("https://www.tianyancha.com/company/", "");
                Document doc = detailget(url);
                String quancheng=getString(doc,"div.company_header_width.ie9Style span.f18.in-block.vertival-middle",0);
                if(StringUtils.isEmpty(quancheng)){
                    continue;
                }
                cname=URLEncoder.encode(quancheng, "UTF-8");;
                for(int a=0;a<zhua.length;a++){
                    if(zhua[a].equals("基本信息")){
                        t.jichu(doc, tid, cname);
                    }else if(zhua[a].equals("主要成员")){
                        t.zhuyao(doc, tid, cname);
                    }else if(zhua[a].equals("股东信息")){
                        t.gudong(doc, tid, cname);
                    }else if(zhua[a].equals("对外投资")){
                        t.duiwaitouzi(doc, tid, cname);
                    }else if(zhua[a].equals("变更记录")){
                        t.biangeng(doc, tid, cname);
                    }else if(zhua[a].equals("分支机构")){
                        t.fenzhi(doc, tid, cname);
                    }else if(zhua[a].equals("融资历史")){
                        t.rongzi(doc, tid, cname);
                    }else if(zhua[a].equals("核心团队")){
                        t.hexin(doc, tid, cname);
                    }else if(zhua[a].equals("投资事件")){
                        t.touzishi(doc, tid, cname);
                    }else if(zhua[a].equals("竞品信息")){
                        t.jingpin(doc, tid, cname);
                    }else if(zhua[a].equals("法律诉讼")){
                        t.susong(doc, tid, cname);
                    }else if(zhua[a].equals("法院公告")){
                        t.fagong(doc, tid, cname);
                    }else if(zhua[a].equals("被执行人")){
                        t.beizhixing(doc, tid, cname);
                    }else if(zhua[a].equals("失信人")){
                        t.shixin(doc, tid, cname);
                    }else if(zhua[a].equals("经营异常")){
                        t.jingyi(doc, tid, cname);
                    }else if(zhua[a].equals("行政处罚")){
                        t.xingchu(doc, tid, cname);
                    }else if(zhua[a].equals("严重违法")){
                        t.yanwei(doc, tid, cname);
                    }else if(zhua[a].equals("股权出质")){
                        t.guquan(doc, tid, cname);
                    }else if(zhua[a].equals("动产抵押")){
                        t.dongchan(doc, tid, cname);
                    }else if(zhua[a].equals("欠税公告")){
                        t.qianshui(doc, tid, cname);
                    }else if(zhua[a].equals("招投标")){
                        t.zhaotou(doc, tid, cname);
                    }else if(zhua[a].equals("债券信息")){
                        t.zhaiquan(doc, tid, cname);
                    }else if(zhua[a].equals("购地信息")){
                        t.goudi(doc, tid, cname);
                    }else if(zhua[a].equals("招聘")){
                        t.zhaopin(doc, tid, cname);
                    }else if(zhua[a].equals("税务评级")){
                        t.shuiwu(doc, tid, cname);
                    }else if(zhua[a].equals("抽查检查")){
                        t.choujian(doc, tid, cname);
                    }else if(zhua[a].equals("产品信息")){
                        t.chanpin(doc, tid, cname);
                    }else if(zhua[a].equals("资质证书")){
                        t.zizhi(doc, tid, cname);
                    }else if(zhua[a].equals("商标信息")){
                        t.shang(doc, tid, cname);
                    }else if(zhua[a].equals("专利")){
                        t.zhuanli(doc, tid, cname);
                    }else if(zhua[a].equals("著作权")){
                        t.zhuzuo(doc, tid, cname);
                    }else if(zhua[a].equals("网站备案")){
                        t.beian(doc, tid, cname);
                    }else if(zhua[a].equals("企业业务")){
                        t.yewu(doc, tid, cname);
                    }else if(zhua[a].equals("微信公众号")){
                        t.gongzhonghao(doc, tid, cname);
                    }
                }

                System.out.println(u.bo.size() + "********************************************************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error_tyc-quan");
            }
        }
    }

    public static Document detailget(String url,Map<String,String> map) throws IOException {
        System.out.println(url);
        Document doc=null;
        int p=0;
        while (true) {
            try {
                p++;
                if(p>=20){
                    break;
                }
                doc = Jsoup.connect(url.replace("https","http"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .proxy(proxy)
                        .cookies(map)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out detail");
            }
        }
        if(StringUtils.isEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())){
            return null;
        }
        return doc;
    }

    public static Document detailget(String url) throws IOException, InterruptedException {
        System.out.println(url);
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect(url.replace("https","http"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .proxy(proxy)
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        return doc;
    }



    public static Map<String,Object> jisuan(String tid) throws IOException {
        String pt=tid.substring(0,1);
        int  fl;
        if(String.valueOf(pt.codePointAt(0)).length()>1){
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)).substring(1,2));
        }else{
            fl=Integer.parseInt(String.valueOf(pt.codePointAt(0)));
        }
        List<String> list=suan(fl);
        org.jsoup.Connection.Response doc = null;
        long ti=0;
        Gson gson = new Gson();
        tongji s;
        String html = null;
        while (true) {
            try {
                ti=System.currentTimeMillis();
                doc = Jsoup.connect("http://www.tianyancha.com/tongji/" + URLEncoder.encode(tid,"utf-8") + ".json?_=" +ti )
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .proxy(proxy)
                        .method(org.jsoup.Connection.Method.GET)
                        .execute();
                if (doc != null && !doc.body().contains("http://www.qq.com/404/search_children.js")&& StringUtils.isNotEmpty(doc.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.body().contains("abuyun")&&doc.body().length()>50&&!doc.body().contains("访问禁止")&&!doc.body().contains("访问拒绝")) {
                    html = doc.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
                    s = gson.fromJson(html, tongji.class);
                    break;
                }
            }catch (Exception e){
                System.out.println(html);
            }
        }
        Map<String,String> map=doc.cookies();
        String[] chars = s.data.split(",");
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            str.append((char) Integer.parseInt(chars[i]));
        }
        String a = str.toString();
        String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");
        String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
        String[] codes = code.split(",");
        /*String shuzu="\"6\", \"b\", \"t\", \"f\", \"2\", \"z\", \"l\", \"5\", \"w\", \"h\", \"q\", \"i\", \"s\", \"e\", \"c\", \"p\", \"m\", \"u\", \"9\", \"8\", \"y\",\n" +
                "\"k\", \"j\", \"r\", \"x\", \"n\", \"-\", \"0\", \"3\", \"4\", \"d\", \"1\", \"a\", \"o\", \"7\", \"v\", \"g\"],\n" +
                "[\"1\", \"8\", \"o\", \"s\", \"z\", \"u\", \"n\", \"v\", \"m\", \"b\", \"9\", \"f\", \"d\", \"7\", \"h\", \"c\", \"p\", \"y\", \"2\", \"0\", \"3\",\n" +
                "\"j\", \"-\", \"i\", \"l\", \"k\", \"t\", \"q\", \"4\", \"6\", \"r\", \"a\", \"w\", \"5\", \"e\", \"x\", \"g\"],\n" +
                "[\"s\", \"6\", \"h\", \"0\", \"p\", \"g\", \"3\", \"n\", \"m\", \"y\", \"l\", \"d\", \"x\", \"e\", \"a\", \"k\", \"z\", \"u\", \"f\", \"4\", \"r\",\n" +
                "\"b\", \"-\", \"7\", \"o\", \"c\", \"i\", \"8\", \"v\", \"2\", \"1\", \"9\", \"q\", \"w\", \"t\", \"j\", \"5\"],\n" +
                "            [\"x\", \"7\", \"0\", \"d\", \"i\", \"g\", \"a\", \"c\", \"t\", \"h\", \"u\", \"p\", \"f\", \"6\", \"v\", \"e\", \"q\", \"4\", \"b\", \"5\", \"k\",\n" +
                "             \"w\", \"9\", \"s\", \"-\", \"j\", \"l\", \"y\", \"3\", \"o\", \"n\", \"z\", \"m\", \"2\", \"1\", \"r\", \"8\"],\n" +
                "            [\"z\", \"j\", \"3\", \"l\", \"1\", \"u\", \"s\", \"4\", \"5\", \"g\", \"c\", \"h\", \"7\", \"o\", \"t\", \"2\", \"k\", \"a\", \"-\", \"e\", \"x\",\n" +
                "             \"y\", \"b\", \"n\", \"8\", \"i\", \"6\", \"q\", \"p\", \"0\", \"d\", \"r\", \"v\", \"m\", \"w\", \"f\", \"9\"],\n" +
                "            [\"j\", \"h\", \"p\", \"x\", \"3\", \"d\", \"6\", \"5\", \"8\", \"k\", \"t\", \"l\", \"z\", \"b\", \"4\", \"n\", \"r\", \"v\", \"y\", \"m\", \"g\",\n" +
                "             \"a\", \"0\", \"1\", \"c\", \"9\", \"-\", \"2\", \"7\", \"q\", \"e\", \"w\", \"u\", \"s\", \"f\", \"o\", \"i\"],\n" +
                "            [\"8\", \"q\", \"-\", \"u\", \"d\", \"k\", \"7\", \"t\", \"z\", \"4\", \"x\", \"f\", \"v\", \"w\", \"p\", \"2\", \"e\", \"9\", \"o\", \"m\", \"5\",\n" +
                "             \"g\", \"1\", \"j\", \"i\", \"n\", \"6\", \"3\", \"r\", \"l\", \"b\", \"h\", \"y\", \"c\", \"a\", \"s\", \"0\"],\n" +
                "            [\"d\", \"4\", \"9\", \"m\", \"o\", \"i\", \"5\", \"k\", \"q\", \"n\", \"c\", \"s\", \"6\", \"b\", \"j\", \"y\", \"x\", \"l\", \"a\", \"v\", \"3\",\n" +
                "             \"t\", \"u\", \"h\", \"-\", \"r\", \"z\", \"2\", \"0\", \"7\", \"g\", \"p\", \"8\", \"f\", \"1\", \"w\", \"e\"],\n" +
                "            [\"7\", \"-\", \"g\", \"x\", \"6\", \"5\", \"n\", \"u\", \"q\", \"z\", \"w\", \"t\", \"m\", \"0\", \"h\", \"o\", \"y\", \"p\", \"i\", \"f\", \"k\",\n" +
                "             \"s\", \"9\", \"l\", \"r\", \"1\", \"2\", \"v\", \"4\", \"e\", \"8\", \"c\", \"b\", \"a\", \"d\", \"j\", \"3\"],\n" +
                "            [\"1\", \"t\", \"8\", \"z\", \"o\", \"f\", \"l\", \"5\", \"2\", \"y\", \"q\", \"9\", \"p\", \"g\", \"r\", \"x\", \"e\", \"s\", \"d\", \"4\", \"n\",\n" +
                "             \"b\", \"u\", \"a\", \"m\", \"c\", \"h\", \"j\", \"3\", \"v\", \"i\", \"0\", \"-\", \"w\", \"7\", \"k\", \"6\"]]";*/
        String[] shu2 = list.toString().replace("[","").replace("]","").replace(" ","").split(",");
        StringBuffer st = new StringBuffer();
        //String[] shu2 = shu[fl].replace("]", "").replace(",[", "").replace("[", "").split(",");
        for (int bb = 0; bb < codes.length; bb++) {
            st.append(shu2[Integer.parseInt(codes[bb])]);
        }
        String utm=st.toString();
        map.put("token",token);
        map.put("_utm",utm);
        map.put("bannerFlag","true");
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("cookie",map);
        maps.put("time",ti-1);
        return maps;
    }

    class Url{
        BlockingQueue<String[]> bo=new LinkedBlockingQueue<String[]>(100);
        public void put(String[] key) throws InterruptedException {
            bo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return bo.poll(120, TimeUnit.SECONDS);
        }
    }
}
