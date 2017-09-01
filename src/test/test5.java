package test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/16.
 */
public class test5 {
    public static void main(String args[]) throws IOException, SQLException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpHost proxy = new HttpHost("219.245.12.131",3128,"http");

       /* DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));
*/


        HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setProxy(proxy);

        /*builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);*/

        CloseableHttpClient httpclient=builder.build();
        HttpGet get=new HttpGet("https://www.chandashi.com/apps/downloadall/appId/ME.ELE/type/day/date/20170309-20170316.html");
        get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.3 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response=httpclient.execute(get);
        HttpEntity resEntity=response.getEntity();
        if(resEntity!=null){
            System.out.println(EntityUtils.toString(resEntity));
        }

    }


    private static SSLConnectionSocketFactory createSSLConnSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

}
