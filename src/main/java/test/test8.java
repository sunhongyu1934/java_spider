package test;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */
public class test8 {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        String data="%7B%0A%20%20%22uid%22%20%3A%20%22%22%2C%0A%20%20%22id%22%20%3A%20%22index%22%2C%0A%20%20%22list_type%22%20%3A%20%221%22%2C%0A%20%20%22day%22%20%3A%20%22%22%2C%0A%20%20%22inputtime%22%20%3A%20%221490086836%22%2C%0A%20%20%22pagesize%22%20%3A%20%2220%22%2C%0A%20%20%22page%22%20%3A%20%221%22%0A%7D";
        String d= URLDecoder.decode(data,"UTF-8");
        System.out.println(d);






        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://api.cyzone.cn/api.php&op=mobilev&a=lists");
        StringEntity entity = new StringEntity(d, ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("op", "mobilev"));
        params.add(new BasicNameValuePair("a", "lists"));
        params.add(new BasicNameValuePair("uid", ""));
        params.add(new BasicNameValuePair("id", "index"));
        params.add(new BasicNameValuePair("list_type", "1"));
        params.add(new BasicNameValuePair("day", ""));
        params.add(new BasicNameValuePair("inputtime", "1490086836"));
        params.add(new BasicNameValuePair("pagesize", "20"));
        params.add(new BasicNameValuePair("page", "1"));
        //post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        post.setEntity(entity);
        post.addHeader("Host", "api.cyzone.cn");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        post.addHeader("userAgent","创业邦 4.6.8 rv:4277 (iPhone; iOS 10.2.1; zh_CN)");
        String pas="op=mobilev,a=lists";
        StringEntity stringEntity = new StringEntity(pas, "UTF-8");
        stringEntity.setContentType("application/x-www-form-urlencoded");
       // post.setEntity(stringEntity);



        CloseableHttpResponse response=httpclient.execute(post);
        HttpEntity resEntity = response.getEntity();
        String tag = EntityUtils.toString(resEntity);
        System.out.println(tag);







        Document doc= Jsoup.connect("http://api.cyzone.cn/api.php")
                .userAgent("创业邦 4.6.8 rv:4277 (iPhone; iOS 10.2.1; zh_CN)")
                    .header("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
                    .header("Accept-Encoding", "gzip")
                .header("Host", "api.cyzone.cn")
                .data("uid","")
                .data("id", "index")
                .data("list_type", "1")
                .data("day", "")
                .data("inputtime","1490086836")
                .data("pagesize","20")
                .data("page","1")
                .data("op","mobilev")
                .data("a","lists")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(1000000)
                .post();
        System.out.println(doc.outerHtml());



    }
}
