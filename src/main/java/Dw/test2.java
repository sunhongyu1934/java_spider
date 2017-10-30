package Dw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */
public class test2 {
    public static List<String> flag() throws IOException {
        List<String> list=new ArrayList<String>();
        String ip[]=null;
        Document docr= Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1").get();
        ip=docr.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");

        for(int x=0;x<11;x++) {
            URL url = null;
            try {
                url = new URL("http://www.baidu.com");
            } catch (MalformedURLException e) {
            }
            InetSocketAddress addr = null;
            addr = new InetSocketAddress(ip[x].split(":", 2)[0], Integer.parseInt(ip[x].split(":", 2)[1]));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http proxy
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection(proxy);
                conn.setConnectTimeout(1000);
                in = conn.getInputStream();
            } catch (Exception e) {
            }
            String s = convertStreamToString(in);
            Document doc=Jsoup.parse(s);
            if (doc.select("span.bg.s_btn_wr input").attr("value").equals("百度一下")) {//有效IP
                list.add(ip[x].split(":", 2)[0] + ":" + Integer.parseInt(ip[x].split(":", 2)[1]));
            }
        }
        return list;
    }


    public static String convertStreamToString(InputStream is) {
        if (is == null)
            return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
}
