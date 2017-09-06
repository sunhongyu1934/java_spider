package tianyancha.XinxiXin;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static Utils.JsoupUtils.getElements;
import static Utils.JsoupUtils.getHref;
import static Utils.JsoupUtils.getString;

/**
 * Created by Administrator on 2017/7/3.
 */
public class XinxiXin {
    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static Proxy proxy;
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
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
        XinxiXin.proxy =proxy;

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

        XinxiXin x=new XinxiXin();
        final Url u=x.new Url();
        final Key k=x.new Key();
        final TYCConsumer tyc=new TYCConsumer("tyc_zl","web","10.44.51.90:12181,10.44.152.49:12181,10.51.82.74:12181");
        ExecutorService pool= Executors.newCachedThreadPool();
        final Connection finalCon = con;
        /*final String po=args[4];
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    duqu(finalCon,k,po);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    duqu(tyc,k);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

        String se=args[2];
        for(int a=1;a<=Integer.parseInt(se);a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serachget(u,k);
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

        String de=args[3];
        final String zhua=args[4];
        for(int b=1;b<=Integer.parseInt(de);b++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(u,finalCon,zhua.split(","),k);
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

        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                System.out.println("结束了！");
                System.exit(0);
            }
            Thread.sleep(2000);
        }

    }

    public static void duqu(TYCConsumer tyc,Key k) throws UnsupportedEncodingException, InterruptedException {
        Thread.sleep(3000);
        while (true){
            String cname=tyc.getmessage();
            k.put(cname);
        }
    }

    public static void duqu(Connection con,Key k,String po) throws SQLException, InterruptedException {
        String sql="select c_name from spider.linshi_xiangmu where c_name!='' and c_name is not null limit "+po+",500 ";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String key=rs.getString(rs.findColumn("c_name"));
            k.put(key);
        }
    }

    public static void serachget(Url u,Key k) throws IOException, InterruptedException, SQLException {
        Document doc;
        while (true) {
            try {
                String key = k.qu();
                if(StringUtils.isEmpty(key)){
                    break;
                }
                doc = detailget("http://www.tianyancha.com/search?key=" + URLEncoder.encode(key, "utf-8") + "&checkFrom=searchBox");
                Elements eles = getElements(doc, "div.search_result_single.search-2017.pb20.pt20.pl30.pr30");
                int p=0;
                boolean ff=false;
                String aa=null;
                String uu=null;
                if (eles != null) {
                    for (Element e : eles) {
                        String url = getHref(e, "div.col-xs-10.search_repadding2.f18 a", "href", 0).replace(" ", "").trim();
                        String cname = getString(e, "div.col-xs-10.search_repadding2.f18 a", 0).replace(" ", "").trim();
                        p++;
                        if(p==1){
                            aa=cname;
                            uu=url;
                        }
                        if(key.equals(cname)) {
                            u.put(new String[]{url, cname});
                            ff=true;
                        }
                    }
                }
                if(StringUtils.isNotEmpty(aa)&&!ff){
                    u.put(new String[]{uu, aa});
                }
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }

    public static void data(Url u,Connection con,String[] zhua,Key k) throws InterruptedException, IOException, SQLException {
        Tyc_quan t=new Tyc_quan(con,zhua);
        Thread.sleep(60000);
        while (true){
            try {
                String[] value = u.qu();
                if((value==null||value.length<2)&&k.bo.size()==0){
                    break;
                }else if((value==null||value.length<2)&&k.bo.size()>0){
                    continue;
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
                System.out.println("error");
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

    public static List<String> suan(int f){
        String o="f9D1x1Z2o1U2f5A1a1P1i7R1u2S1m1F1,o2A1x2F1u5~j1Y2z3!p2~r3G2m8S1c1,i3E5o1~d2!y2H1e2F1b6`g4v7,p1`t7D3x5#w2~l2Z1v4Y1k4M1n1,C2e3P1r7!s6U2n2~p5X1e3#,g4`b6W1x4R1r4#!u5!#D1f2,!z4U1f4`f2R2o3!l4I1v6F1h2F1x2!,b2~u9h2K1l3X2y9#B4t1,t5H1s7D1o2#p2#z1Q3v2`j6,r1#u5#f1Z2w7!r7#j3S1";
        String i="2633141825201321121345332721524273528936811101916293117022304236|1831735156281312241132340102520529171363214283321272634162219930|2332353860219720155312141629130102234183691124281413251227261733|2592811262018293062732141927100364232411333831161535317211222534|9715232833130331019112512913172124126035262343627321642220185148|3316362031032192529235212215274341412306269813312817111724201835|3293412148301016132183119242311021281920736172527353261533526224|3236623313013201625221912357142415851018341117262721294332103928|2619332514511302724163415617234183291312001227928218353622321031|3111952725113022716818421512203433241091723133635282932601432216";
        String ii=i.split("\\|")[f];
        String oo=o.split(",")[f];
        String[] ooo=oo.split("|");
        int s=0;
        String basechars="abcdefghijklmnopqrstuvwxyz1234567890-~!";
        List<String> list=new ArrayList<String>();
        for(int u=0;u<ooo.length;u++){
            if(!"`".equals(ooo[u])&&!"!".equals(ooo[u])&&!"~".equals(ooo[u])){

            }else{
                list.add(ii.substring(s, s + 1));
                s++;
            }
            if("#".equals(ooo[u])){
                list.add(ii.substring(s, s + 1));
                list.add(ii.substring(s+1, s + 3));
                list.add(ii.substring(s+3, s + 4));
                s+=4;
            }
            if(ooo[u].codePointAt(0)>96&&ooo[u].codePointAt(0)<123){
                int l= Integer.parseInt(ooo[u+1]);
                for(int c=0;c<l;c++){
                    list.add(ii.substring(s, s + 2));
                    s+=2;
                }
            }
            if(ooo[u].codePointAt(0)>64&&ooo[u].codePointAt(0)<91){
                int l= Integer.parseInt(oo.substring(u+1,u+2));
                for(int c=0;c<l;c++){
                    list.add(ii.substring(s, s + 1));
                    s++;
                }
            }
        }
        List<String> list2=new ArrayList<String>();
        for(int y=0;y<list.size();y++){
            list2.add(basechars.substring(Integer.parseInt(list.get(y)), Integer.parseInt(list.get(y))+1));
        }
        return list2;
    }


    public static String login() throws IOException {
        CloseableHttpClient httpClient= HttpClients.custom().build();
        HttpPost post=new HttpPost("http://www.tianyancha.com/cd/login.json");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        /*List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("autoLogin", "true"));  //请求参数
        list.add(new BasicNameValuePair("cdpassword", "349617b80072ce2b45926f82f0b2d492")); //请求参数
        list.add(new BasicNameValuePair("loginway", "PL")); //请求参数
        list.add(new BasicNameValuePair("mobile", "13717951934")); //请求参数
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");
        post.setEntity(entity);*/
        Map<String,String> map=new HashMap<String, String>();
        map.put("autoLogin","true");
        map.put("cdpassword","349617b80072ce2b45926f82f0b2d492");
        map.put("loginway","PL");
        map.put("mobile","13717951934");
        StringEntity entity = new StringEntity(JSONObject.fromObject(map).toString(), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        CloseableHttpResponse response=httpClient.execute(post);
        Header[] head=response.getHeaders("Set-Cookie");
        StringBuffer str=new StringBuffer();
        for(Header h:head){
            str.append(h.getValue()+";");
        }
        String cookie=str.substring(0,str.length()-1);
        System.out.println(cookie);
        return cookie;
    }

    public class Key{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>(10);
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }
        public String qu() throws InterruptedException {
            return bo.poll(60, TimeUnit.SECONDS);
        }
    }

    public class Url{
        BlockingQueue<String[]> bo=new LinkedBlockingQueue<String[]>();
        public void put(String[] key) throws InterruptedException {
            bo.put(key);
        }
        public String[] qu() throws InterruptedException {
            return bo.poll(120, TimeUnit.SECONDS);
        }
    }


}
