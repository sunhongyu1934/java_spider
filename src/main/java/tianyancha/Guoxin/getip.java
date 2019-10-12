package tianyancha.Guoxin;

import Utils.RedisClu;
import baidu.RedisAction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class getip {
    private static RedisClu rd;
    static{
        rd=new RedisClu();
    }
    public static void main(String args[]) throws IOException, InterruptedException {
        String url=args[0];
        getip(url);
    }
    public static void getip(String url) throws IOException, InterruptedException {
        Pattern pat=Pattern.compile("[a-z]|[A-Z]");
        while (true) {
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .get();
                String[] ips = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").trim().split(" ");
                long timenow=System.currentTimeMillis();
                long timefive=timenow-5*60*1000;
                rd.removeZsetByScore("ip","0",String.valueOf(timefive));
                for(String s:ips){
                    if (s.contains("requests") || s.contains("请控制")||pat.matcher(s).find()) {
                        continue;
                    }
                    System.out.println(s.trim());
                    rd.zset("ip",s.trim(),String.valueOf(timenow));
                }
                Thread.sleep(5000);
            }catch (Exception e){
                System.out.println("ip error");
            }
        }
    }
}
