package simutong.simutong_jigou;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/27.
 */
public class Qingqiu {
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
                System.out.println("time out reget");
            }
        }
        System.out.println("基本请求成功");
        return doc;
    }




    public static Document guanliget(String url,String data,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        System.out.println("开始请求管理团队");
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .data("param.lp_id", "")
                        .data("pagetools_pageNumber", "1")
                        .data("param.currentPage", data)
                        .proxy(proxy)
                        .post();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("管理团队请求成功");
        return doc;
    }

    public static Document touziget(String url,String data,String orgid,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .proxy(proxy)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .data("param.org_id", orgid)
                        .data("pagetools_pageNumber", "1")
                        .data("param.currentPage", data)
                        .post();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        return doc;
    }

    public static Document tuichuget(String url,String data,String orgid,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        System.out.println("开始请求退出");
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .proxy(proxy)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .data("param.org_id", orgid)
                        .data("pagetools_pageNumber", "1")
                        .data("param.currentPage", data)
                        .post();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("退出请求成功");
        return doc;
    }

    public static Document jijinget(String url,String data,String orgid,Map<String,String> map,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        System.out.println("开始请求基金");
        while (true) {
            try {
                Thread.sleep(r.nextInt(2001) + 3000);
                doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .cookies(map)
                        .proxy(proxy)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                        .data("param.org_id", orgid)
                        .data("pagetools_pageNumber", "2")
                        .data("param.currentPage", data)
                        .post();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }
        System.out.println("基金请求成功");
        return doc;
    }

    public static Document jijinxiangqing(String url,Proxy proxy) throws IOException, InterruptedException {
        Document doc=null;
        while (true) {
            Thread.sleep(r.nextInt(2001)+3000);
            doc= Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .proxy(proxy)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                    .header("Cookie", "JSESSIONID=19E44CD7D711C1B8E22B631F1BF73ECC; firstEnterUrlInSession=http%3A//pe.pedata.cn/removeMacMember.action; VisitorCapacity=1; USER_LOGIN_ID=1477638811645543; USER_LOGIN_NAME_KEY=simutong3%40gaiyachuangxin.cn; IS_CS_KEY=true; USER_LOGIN_NAME=simutong3%40gaiyachuangxin.cn; USER_LOGIN_LANGUAGE=zh_CN; USER_CLIENT_ID=\"\"; pageReferrInSession=http%3A//pe.pedata.cn/removeMacMember.action; operatorId=31183; request_locale=zh_CN")
                    .get();
            if(!doc.outerHtml().contains("abuyun")&& StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())){
                break;
            }
        }
        return doc;
    }


}
