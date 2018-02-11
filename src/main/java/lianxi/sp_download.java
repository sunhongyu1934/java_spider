package lianxi;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.IOException;

public class sp_download {
    public static void main(String args[]){

    }
    public static void exec() throws IOException {
        Connection.Response doc= Jsoup.connect("http://cy.ncss.org.cn/picture?path=project/90037/20160708/df000a469b3a4b6bae69bd852703d36e.mp4")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .method(Connection.Method.GET)
                .execute();
        byte[] inputStream=doc.bodyAsBytes();

    }
}
