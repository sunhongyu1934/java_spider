package tianyancha.XinxiXin;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tianyancha.quanxinxi.ceshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/7/4.
 */
public class Tyc_detail {
    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String args[]) throws IOException {
// 代理隧道验证信息
        final  String ProxyUser = args[0];
        final  String ProxyPass = args[1];

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        final TYCConsumer ty3=new TYCConsumer("tyc_company","spider","10.44.155.195:2181,10.44.143.200:2181,10.45.146.248:2181");
        final TYCProducer ty2=new TYCProducer("tyc_company","10.44.158.42:9092,10.44.137.192:9092,10.44.143.200:9092,10.44.155.195:9092");

        final String path=args[3];

        int x= Integer.parseInt(args[2]);
        ExecutorService pool= Executors.newCachedThreadPool();

        Tyc_detail t=new Tyc_detail();
        final Keys k=t.new Keys();
        final Gsgx g=t.new Gsgx();


        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    quid(ty3,k);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pool.submit(new Runnable() {
            @Override
            public void run() {
                fanghui(ty2,g);
            }
        });

        for(int p=1;p<=20;p++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detailget(proxy,path,k,g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void quid(TYCConsumer ty3,Keys k) throws InterruptedException {
        while (true){
            try {
                String tid = ty3.getmessage();
                k.put(tid);
            }catch (Exception e){
                System.out.println("qu error");
            }
        }
    }

    public static void fanghui(TYCProducer ty2,Gsgx g){
        while (true){
            try{
                String kk=g.qu();
                ty2.send(kk);
            }catch (Exception e){
                System.out.println("fang error");
            }
        }
    }


    public static void detailget(Proxy proxy,String path,Keys k,Gsgx g) throws IOException {
        int j=0;
        int t=0;

        while (true) {
            try {
                String tid=k.qu();
                if (tid.split("-", 2)[1].equals("all")) {
                    Document doc = null;
                    while (true) {
                        try {
                            doc = Jsoup.connect("http://www.tianyancha.com/company/" + tid.split("-", 2)[0])
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                                    .timeout(10000)
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .proxy(proxy)
                                    .get();
                            if (!doc.outerHtml().contains("获取验证码")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("time out");
                        }
                    }
                    xieru(doc,tid,path);
                    j++;
                    System.out.println("all info is "+j);
                } else {
                    g.put(tid);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("detail orror");
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
    }

    public static Map<String,String> jisuan(Proxy proxy,String tid,String path) throws IOException {
        Connection.Response doc = null;
        while (true) {
            try {
                doc = Jsoup.connect("https://www.tianyancha.com/tongji/"+tid.split("-", 2)[0]+".json?_=" + System.currentTimeMillis())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .proxy(proxy)
                        .method(Connection.Method.GET)
                        .execute();
                if (doc != null && !doc.body().contains("http://www.qq.com/404/search_children.js")&& StringUtils.isNotEmpty(doc.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("time out retongji");
            }
        }
        Map<String,String> map=doc.cookies();
        String html = doc.body().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim();
        Gson gson = new Gson();
        ceshi.suan s = gson.fromJson(html, ceshi.suan.class);
        String[] chars = s.data.v.split(",");
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            str.append((char) Integer.parseInt(chars[i]));
        }
        String a = str.toString();
        String token = a.split(";", 2)[0].replace("!function(n){document.cookie='rtoken=", "").replace(";", "");
        String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
        String[] codes = code.split(",");
        String shuzu="[[\"6\", \"b\", \"t\", \"f\", \"2\", \"z\", \"l\", \"5\", \"w\", \"h\", \"q\", \"i\", \"s\", \"e\", \"c\", \"p\", \"m\", \"u\", \"9\", \"8\", \"y\",\n" +
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
                "             \"b\", \"u\", \"a\", \"m\", \"c\", \"h\", \"j\", \"3\", \"v\", \"i\", \"0\", \"-\", \"w\", \"7\", \"k\", \"6\"]]";
        String[] shu = shuzu.replace("\"", "").replace(" ", "").replace("\n","").split("]");
        String pt=tid.substring(0,1);
        int fl= Integer.parseInt(String.valueOf(pt.codePointAt(0)).substring(1,2));
        StringBuffer st = new StringBuffer();
        String[] shu2 = shu[fl].replace("]", "").replace(",[", "").replace("[", "").split(",");
        for (int bb = 0; bb < codes.length; bb++) {
            st.append(shu2[Integer.parseInt(codes[bb])]);
        }
        String utm=st.toString();
        map.put("token",token);
        map.put("_utm",utm);
        return map;
    }

    public static void tu(Proxy proxy,String tid,String path) throws IOException {
        Map<String,String> maps=jisuan(proxy,tid,path);

        Document docc=null;
        while (true) {
            try {
                docc = Jsoup.connect("http://dis.tianyancha.com/dis/getInfoById/"+tid.split("-", 2)[0]+".json?")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .header("Tyc-From", "normal")
                        .header("Accept", "application/json, text/plain, ")
                        .header("CheckError", "check")
                        .header("Referer", "http://dis.tianyancha.com/dis/old")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(maps)
                        .timeout(5000)
                        .proxy(proxy)
                        .get();
                if(docc.outerHtml().contains("relationships")&& StringUtils.isNotEmpty(docc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())){
                    break;
                }
                maps=jisuan(proxy,tid,path);
            }catch (Exception e){
                maps=jisuan(proxy,tid,path);
                System.out.println("time out tu");
                break;
            }
        }
        xieru(docc,tid,path);
    }

    public static void xieru(Document doc,String tid,String path) throws IOException {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH");
        String time=simpleDateFormat.format(date);

        File file=new File(path+"tyc_"+time);
        if(!file.exists()){
            file.mkdirs();
        }
        FileOutputStream outputStream=new FileOutputStream(path+"tyc_"+time+"/"+tid);
        OutputStreamWriter out=new OutputStreamWriter(outputStream,"UTF-8");
        out.write(doc.outerHtml());
        out.flush();
        outputStream.close();
        out.close();
    }


    class Keys{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>(100);
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }

        public String qu() throws InterruptedException {
            return bo.take();
        }
    }

    class Gsgx{
        BlockingQueue<String> bo=new LinkedBlockingQueue<String>(100);
        public void put(String key) throws InterruptedException {
            bo.put(key);
        }

        public String qu() throws InterruptedException {
            return bo.take();
        }
    }
}
