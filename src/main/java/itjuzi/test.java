package itjuzi;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/4/18.
 */
public class test {
    public static void main(String args[]) throws IOException, ParseException {
        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        HttpClientBuilder builder = HttpClients.custom();

        //builder.setRoutePlanner(routePlanner);

        //builder.setDefaultCredentialsProvider(credsProvider);
        //builder.setDefaultRequestConfig(requestConfig);
        final CloseableHttpClient httpclient = builder.build();
        HttpGet get=new HttpGet("https://www.itjuzi.com/investfirm?page=1");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        String tag = null;
        while (true) {
            try {
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity resEntity = response.getEntity();
                tag = EntityUtils.toString(resEntity);
                if (StringUtils.isNotEmpty(tag) && !tag.contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("time out reget");
            }
        }
        Document doc= Jsoup.parse(tag);
        Elements ele=getElements(doc,"ul.list-main-investset li.clearfix i.cell.name a.institu-logo");
        for(Element e:ele){
            System.out.println(e.attr("href"));
        }

    }
    public static Elements getElements(Document doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }
}
