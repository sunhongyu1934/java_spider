package redis;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/2.
 */
public class test {
    public static void main(String args[]) throws IOException {
        Map map = new HashMap();
        map.put( "name", "json" );
        map.put("bool", Boolean.TRUE);
        map.put("int", new Integer(1));
        map.put( "arr", new String[]{"a","b"} );
        map.put( "func", "function(i){ return this.arr[i]; }" );

        JSONObject jsonObject = JSONObject.fromObject( map );
        System.out.println( jsonObject.toString() );


        HttpServer server = HttpServer.create(new InetSocketAddress(
                "127.0.0.1", 8765), 0);
        server.createContext("/",new MyResponseHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        get();
    }


    public static class MyResponseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            //针对请求的处理部分
            //返回请求响应时，遵循HTTP协议
            String responseString = "<font color='#ff0000'>Hello! This a HttpServer!</font>";
            //设置响应头
            httpExchange.sendResponseHeaders(HttpStatus.SC_OK, responseString.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(responseString.getBytes());
            os.close();
        }
    }

    public static void get() throws IOException {
        Document doc= Jsoup.connect("http://127.0.0.1:8765").get();
        System.out.println(doc.outerHtml());
    }


}
