package tianyancha.Guoxin;

import baidu.RedisAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class getip {
    private static RedisAction rd;
    static{
        rd=new RedisAction("10.44.51.90",6379);
    }
    public static void main(String args[]) throws IOException, InterruptedException {
        String url=args[0];
        getip(url);
    }
    public static void getip(String url) throws IOException, InterruptedException {
        while (true) {
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String[] ips = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").trim().split(" ");
                for(String s:ips){
                    if (s.contains("requests") || s.contains("请控制")) {
                        continue;
                    }
                    System.out.println(s.trim());
                    if(rd.getslength("ip")<=5) {
                        rd.set("ip", s.trim());
                    }
                }
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println("ip error");
            }
        }
    }
}
