package linshi_spider;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "H0QCBTTB7675S1XD";
    final static String ProxyPass = "26A1FF9238C9050D";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static class Spiderlog{
        public String file_md5;
        public String file_size;
        public String file_name;
        public String spider_time;
        public String parse_time;
        public String data_date;
        public String store_data_flag;
        public String source;
    }
    public static void main(String args[]) throws Exception {
        /*ExecutorService pool= Executors.newCachedThreadPool();
        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ge();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
        /*long a=System.currentTimeMillis();
        Document doc = Jsoup.connect("https://trends.so.com/index/indexquerygraph?t=30&area=%E5%85%A8%E5%9B%BD&q=%E5%B0%8F%E7%B1%B3")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Cookie","__guid=210905680.4581115104801143000.1499862185293.6477; __huid=114YJei0kPiXVkGvYicflrZtWQUXjsOHWDsSjQpaitPEY%3D; _S=68e2c3c06dbca8fbaed28d83fdc81d0b; Q=u%3Ddcnymz12891%26n%3D%26le%3DZwD3ZGV2AQLmAlH0ZUSkYzAioD%3D%3D%26m%3D%26qid%3D2922887526%26im%3D1_t00df551a583a87f4e9%26src%3Dpcw_360index_Sina%26t%3D1; T=s%3D763f80ed844d7b4b11d0c5eb27526265%26t%3D1501230681%26lm%3D%26lf%3D%26sk%3D661707a22e66ed31ff46ff223935ae21%26mt%3D1501230681%26rc%3D%26v%3D2.0%26a%3D0; __bn=%2C%2BU%2CpU.%2Cd%28oX%28%294dT1XDz%28g4%7DgYdG%2B%40.J%3FMfY%5E0%7CMspleT%23mY7gh4%21Y6GPoC%3FbjGa0OoBYS6%7Bmxon; monitor_count=2; test_cookie_enable=null; __gid=210905680.689155211.1499862185292.1501230756140.50; __sid=210905680.3531326734806575600.1501230676411.962")
                .timeout(10000)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        long b=System.currentTimeMillis();
        int c= (int) (b-a);
        System.out.println(c);
        System.out.println(doc.body());*/
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(100000).setConnectionRequestTimeout(100000)
                .setSocketTimeout(100000).build();


        HttpClientBuilder builder = HttpClients.custom();

        CloseableHttpClient httpclient = builder.build();
        HttpGet get=new HttpGet("http://upload.bkzy.org/files/2017-07-27/1b2c7d4ca600ac15b689a04c538836c1.pdf");
        get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        CloseableHttpResponse response = httpclient.execute(get);
        InputStream in=response.getEntity().getContent();
        OutputStream out=new FileOutputStream("C:\\Users\\Administrator\\Desktop\\hahaha.pdf");
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }
    
    public static void ceshi(Proxy proxy) throws IOException, InterruptedException {
        int a=0;
        while (true) {
            node(proxy);
            node2(proxy);
            //Thread.sleep(500);
            a++;
            System.out.println(a+"**************************************************");
        }
    }

    public static Document node(Proxy proxy) throws IOException {
        Document doc = null;
        while (true) {
            try {
                doc = Jsoup.connect("https://trends.so.com/index/indexquerygraph?t=30&area=%E5%85%A8%E5%9B%BD&q=%E5%B0%8F%E7%B1%B3")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .header("Cookie","__guid=210905680.4581115104801143000.1499862185293.6477; __huid=114YJei0kPiXVkGvYicflrZtWQUXjsOHWDsSjQpaitPEY%3D; Q=u%3Ddcnymz12891%26n%3D%26le%3DZwD3ZGV2AQLmAlH0ZUSkYzAioD%3D%3D%26m%3D%26qid%3D2922887526%26im%3D1_t00df551a583a87f4e9%26src%3Dpcw_360index_Sina%26t%3D1; T=s%3D5d7bbd6d6c00dbb1480397a5e0e3eb31%26t%3D1501310826%26lm%3D%26lf%3D%26sk%3D975f77fc42ea9b65223de825da0cfe45%26mt%3D1501310826%26rc%3D%26v%3D2.0%26a%3D0; _S=ed784b70a024bd6758623d305ac53f76; __bn=%2C%2B%2CUp%2B.%2Cd%28poXo%294dT%281%28DUz%29g%7DoYGT%40Jz%3FM%2Cf%29%5E0J%7Cs%40lef%23pmG7%29hf%21p6PCfbTjaf; monitor_count=7; __gid=210905680.689155211.1499862185292.1501315169737.56; __sid=210905680.2297668699684476200.1501315165337.7754; test_cookie_enable=null")
                        .ignoreContentType(true)
                        .timeout(10000)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && org.apache.commons.lang.StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
    }

    public static Document node2(Proxy proxy) throws IOException {
        Document doc = null;
        while (true) {
            try {
                doc = Jsoup.connect("http://trends.so.com/index/overviewJson?area=%E5%85%A8%E5%9B%BD&q=%E5%B0%8F%E7%B1%B3")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .header("Cookie","__guid=210905680.4581115104801143000.1499862185293.6477; __huid=114YJei0kPiXVkGvYicflrZtWQUXjsOHWDsSjQpaitPEY%3D; Q=u%3Ddcnymz12891%26n%3D%26le%3DZwD3ZGV2AQLmAlH0ZUSkYzAioD%3D%3D%26m%3D%26qid%3D2922887526%26im%3D1_t00df551a583a87f4e9%26src%3Dpcw_360index_Sina%26t%3D1; T=s%3D5d7bbd6d6c00dbb1480397a5e0e3eb31%26t%3D1501310826%26lm%3D%26lf%3D%26sk%3D975f77fc42ea9b65223de825da0cfe45%26mt%3D1501310826%26rc%3D%26v%3D2.0%26a%3D0; _S=ed784b70a024bd6758623d305ac53f76; __bn=%2C%2B%2CUp%2B.%2Cd%28poXo%294dT%281%28DUz%29g%7DoYGT%40Jz%3FM%2Cf%29%5E0J%7Cs%40lef%23pmG7%29hf%21p6PCfbTjaf; monitor_count=7; __gid=210905680.689155211.1499862185292.1501315169737.56; __sid=210905680.2297668699684476200.1501315165337.7754; test_cookie_enable=null")
                        .timeout(10000)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && org.apache.commons.lang.StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace(" <head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out");
            }
        }
        return doc;
    }

    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }


    public static void ge() throws IOException {
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> TablePro=new HashMap<String, Object>();
        Map<String,String> detail=new HashMap<String, String>();
        Map<String,String> detail2=new HashMap<String, String>();
        Map<String,String> detail3=new HashMap<String, String>();
        Map<String,String> detail4=new HashMap<String, String>();
        Map<String,String> detail5=new HashMap<String, String>();
        Map<String,String> detail6=new HashMap<String, String>();

        List<Map<String,String>> list=new ArrayList<Map<String, String>>();

        List<String> ll=new ArrayList<String>();
        ll.add("touzi_jigou");
        ll.add("jigou_sid");
        ll.add("beitou_gongsi");
        ll.add("beitou_sid");
        ll.add("hang_ye");
        ll.add("diqu");


        TablePro.put("table","si_touzi_zuixin");

        detail.put("content","这是个测试");


        detail2.put("content","这是个测试");


        detail3.put("content","这是个测试");


        detail4.put("content","这是个测试");


        detail5.put("content","");


        detail6.put("content","这是个测试");
        list.add(detail);
        list.add(detail2);
        list.add(detail3);
        list.add(detail4);
        list.add(detail5);
        list.add(detail6);

        map.put("tablePro",TablePro);
        map.put("detail",list);
        JSONObject jsonObject=JSONObject.fromObject(map);
        System.out.println(jsonObject.toString());

        while (true) {
            Document doc = Jsoup.connect("http://59.110.52.96:8080/java_web/servlet/insertServlet")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .data("json",jsonObject.toString())
                    .post();
            System.out.println(doc.body().toString().replace("<body>","").replace("</body>","").replace("<em>","").replace("</em>","").replace("\n","").trim());
        }
    }











    public static void get(CloseableHttpClient httpClient) throws IOException {
        HttpGet get=new HttpGet("http://www.tianyancha.com/v2/company/23402373.json");
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity resEntity = response.getEntity();
        String tag = EntityUtils.toString(resEntity);
        System.out.println(tag);
    }





    public static String serach(String key) throws IOException {
        Document doc= null;
        int a=0;
        while (true) {
            try {
                doc=Jsoup.connect("http://www.tianyancha.com/v2/search/" + key + ".json?")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                                //.proxy(proxy)
                        //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .get();
                System.out.println(doc.body());
                if (StringUtils.isNotEmpty(doc.body().toString().replace("<body>", "").replace("</body>", "").replace("<em>", "").replace("</em>", "").replace("\n", "").trim()) && !doc.body().toString().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
            a++;
            if(a>=500){
                return null;
            }
        }
        return doc.body().toString().replace("<body>","").replace("</body>","").replace("<em>","").replace("</em>","").replace("\n","").trim();
    }





    public static void gett() throws IOException {
        Connection.Response res=Jsoup.connect("http://www.tianyancha.com/act/create.json")
                .data("tid","gr_user_id=dda2019e-2703-48c8-abc4-8d17082d7f50; aliyungf_tc=AQAAAFKOGSQo6AEAKiluJHYHguiRdMTu; TYCID=3aee6935d3d74ba5be42b615c81e8679; tnet=36.110.41.42; _pk_ref.1.e431=%5B%22%22%2C%22%22%2C1494224239%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DtLu0ffDvPUk8yo64LLn3I5s9Bv2iC7a9T0E8PAufZZiJZdaORP8l1GRAnCxwZMf-%26wd%3D%26eqid%3De9a1461e0000307e0000000359100cfe%22%5D; _pk_ref.6835.e431=%5B%22%22%2C%22%22%2C1494224242%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DtLu0ffDvPUk8yo64LLn3I5s9Bv2iC7a9T0E8PAufZZiJZdaORP8l1GRAnCxwZMf-%26wd%3D%26eqid%3De9a1461e0000307e0000000359100cfe%22%5D; RTYCID=7a48f08c5c35443f817913c242b27165; _pk_id.6835.e431=dca25ee1f593216f.1492066392.16.1494228981.1494224242.; _pk_ses.6835.e431=*; _pk_id.1.e431=76e54f0017e500f9.1492066392.16.1494228981.1494224239.; _pk_ses.1.e431=*; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1494227343,1494228646,1494228724,1494228728; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1494228981; token=0ba2bc77814549a59d8b265b7742c832; _utm=c4c1cdb7473744d8b859b813561f110d; paaptp=3231871eb43a8c0f5a07a7e45510d48c7fa551f6090029cba315be6fcd3c5")
                .data("type","searchFocus")
                .data("ua","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .data("url","http://www.tianyancha.com/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.8")
                .header("Connection","keep-alive")
                .header("Content-Type","application/json; charset=UTF-8")
                .header("Host","www.tianyancha.com")
                .header("IgnoreError","ignore")
                .header("Origin","http://www.tianyancha.com")
                .header("Referer","http://www.tianyancha.com/")
                .header("Tyc-From","normal")

                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .execute();

        System.out.println(res.body());
        System.out.println(res.cookies());
    }




}
