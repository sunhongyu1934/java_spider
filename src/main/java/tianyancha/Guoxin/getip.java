package tianyancha.Guoxin;

import Utils.RedisClu;
import baidu.RedisAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class getip {
    private static RedisClu rd;
    static{
        rd=new RedisClu();
    }
    public static void main(String args[]) throws IOException, InterruptedException {
        String url=args[0];
        String ge=args[1];
        getip(url,ge);
    }
    public static void getip(String url,String ge) throws IOException, InterruptedException {
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
                    if(rd.getslength("ip")<=Integer.parseInt(ge)) {
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
