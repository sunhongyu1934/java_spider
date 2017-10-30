package redis;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/27.
 */
public class rediss {
    private static List<String> list=new ArrayList<String>();
    private static List<String> listdelete=new ArrayList<String>();
    public static void main(String args[]) throws IOException, InterruptedException {
        final Jedis redis = new Jedis ("127.0.0.1",6379);//连接redis
        redis.select(1);
        Set set = redis.zrange("proxy", 0, -1);
        Iterator t1=set.iterator() ;
        while(t1.hasNext()){
            String obj1=t1.next().toString();
            list.add(obj1);
        }

        ExecutorService pool= Executors.newFixedThreadPool(100);
        for(int x=0;x<99;x++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        jiance( redis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                Thread.sleep(5000);
                String str[] = new String[listdelete.size()];
                for (int a = 0; a < listdelete.size(); a++) {
                    str[a] = listdelete.get(a);
                }
                System.out.println("begin remove");
                redis.zrem("proxy", str);
                System.out.println("remove success over");
                System.exit(0);
            }
            Thread.sleep(500);
        }
    }


    public static void jiance(Jedis redis) throws IOException, InterruptedException {
        while (true) {
            String ip = list.get(0).split(":", 2)[0];
            int port = Integer.parseInt(list.get(0).split(":", 2)[1]);
            list.remove(0);
            System.out.println("list: "+list.size());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
            String tag=null;
            long timestart=System.currentTimeMillis()/1000;
            try {
                System.out.println("begen get");
                Document doc= Jsoup.connect("https://www.baidu.com/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .proxy(proxy)
                        .timeout(5000)
                        .ignoreContentType(true)
                        .get();
                tag=doc.outerHtml();
            } catch (Exception e) {
                System.out.println(ip + ":" + port + "    0,remove");
                listdelete.add(ip + ":" + port);
            }
            long timestart2 = System.currentTimeMillis() / 1000;
            if (StringUtils.isNotEmpty(tag) && tag.contains("baidu") && (timestart2 - timestart) <= 1) {
                redis.zincrby("proxy", 200000000, ip + ":" + port);
                System.out.println(ip + ":" + port + "    100****************************************************");
            }
            if (StringUtils.isNotEmpty(tag) && tag.contains("baidu") && (timestart2 - timestart) <= 3 && (timestart2 - timestart) > 1) {
                redis.zincrby("proxy", 150000000, ip + ":" + port);
                System.out.println(ip + ":" + port + "    80*****************************************************");
            }
            if (StringUtils.isNotEmpty(tag) && tag.contains("baidu") && (timestart2 - timestart) <= 5 && (timestart2 - timestart) > 3) {
                redis.zincrby("proxy", 100000000, ip + ":" + port);
                System.out.println(ip + ":" + port + "    60*****************************************************");
            }
        }

    }

}
