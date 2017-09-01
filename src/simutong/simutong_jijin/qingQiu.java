package simutong.simutong_jijin;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/27.
 */
public class qingQiu {
    private static Random r=new Random();
    public static Map<String ,String> denglu(Proxy proxy,String user,String password,String ma) throws IOException, InterruptedException {
        Connection.Response doc= null;
        System.out.println("开始登陆");
        while (true) {
            try {
                doc= Jsoup.connect("http://pe.pedata.cn/ajaxLoginMember.action")
                        .data("param.loginName", user)
                        .data("param.pwd", password)
                        .data("param.iscs", "true")
                        .data("param.macaddress", ma)
                        .data("param.language", "zh_CN")
                        .data("request_locale", "zh_CN")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .method(Connection.Method.POST)
                        .execute();
                System.out.println(doc.body());
                if (!doc.body().contains("abuyun") && StringUtils.isNotEmpty(doc.body().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim()) && !doc.body().contains("Forbidden")) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        Map<String ,String> map=doc.cookies();
        System.out.println("登陆成功");
        return map;
    }


    public static Document jichuget(String url,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        System.out.println("开始请求基本");
        int p=0;
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .cookies(map)
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                p++;
                if(p>=5){
                    break;
                }
                System.out.println("time out reget");
            }
        }
        System.out.println("基本请求成功");
        return doc;
    }


}
