package yiguan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/3.
 */
public class dau {
    public static void main(String args[]) throws IOException {
        get();
    }
    public static void get() throws IOException {
        //http://qianfan.analysys.cn/qianfan/search/searchData
        Document doc= Jsoup.connect("http://qianfan.analysys.cn/qianfan/app/indexBaseInfo")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                .data("appIds","2013176")
                .data("categoryIds","1041044")
                .header("Cookie","referer=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DMX_T2TqKh2f4A3T4RyegYZSA99BSVpDQWcANRA76vXHGe-yBGpe-ABI10x4_UOzZ%26wd%3D%26eqid%3D81ef96c000003f07000000035982d476; algtipA3home-=true; algtipA3-=true; Hm_lvt_d981851bd0d388f5b0fa75295b96745d=1501746307,1501746806; Hm_lpvt_d981851bd0d388f5b0fa75295b96745d=1501746806; JSESSIONID=476D21AA3A8DDEB8139ECAA6BF2FA14B; cacheCookie=%5B%7B%22appIds%22%3A2013176%2C%22categoryIds%22%3A1041044%2C%22itemId%22%3A2013176%2C%22itemName%22%3A%22%E9%A5%BF%E4%BA%86%E4%B9%88%22%7D%5D; Hm_lvt_abe5c65ffb860ebf053a859d05bee0ea=1501746784,1501746803,1501746837,1501746862; Hm_lpvt_abe5c65ffb860ebf053a859d05bee0ea=1501747001")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .post();
        System.out.println(doc.outerHtml());
    }
}
