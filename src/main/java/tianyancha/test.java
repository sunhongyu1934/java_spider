package tianyancha;

import Utils.Dup;
import Utils.RedisClu;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import spiderKc.kcBean.Count;

import java.io.File;
import java.util.*;

public class test{
    private static RedisClu redisClu=new RedisClu();
    public static void main(String args[]) throws InterruptedException {
        Random random=new Random();
        System.setProperty(Count.chrome,  "/data1/java_spider/chromedriver_linux");
        //System.setProperty(Count.chrome,  Count.chromepath);
        ChromeOptions options=new ChromeOptions();
        String ip;
        while (true) {
            ip=redisClu.get("ip");
            if(Dup.nullor(ip)){
                break;
            }else{
                Thread.sleep(500);
            }
        }
        options.addExtensions(new File("/data1/java_spider/test.crx"));
        //options.addExtensions(new File("C:\\Users\\13434\\Desktop\\innotree\\test.crx"));
        options.addArguments("--proxy-server=http://" + ip);
        List<String> list=new ArrayList<>();
        list.add("enable-automation");
        options.setExperimentalOption("excludeSwitches",list);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);
        //driver.manage().window().maximize();
        driver.get("https://www.qichacha.com/user_login");
        Thread.sleep(20000);
        System.out.println(driver.getPageSource());
        driver.quit();
    }

    public static Map<String, List<Integer>> getTrack(int distance){
        Random random=new Random();
        double v=25;
        double t=0.2;
        List<Integer> forward_tracks=new ArrayList<>();
        List<Integer> back_tracks=new ArrayList<>();
        double current=0;
        //double mid=(distance*6)/10;
        double mid=distance-10;
        //distance+=20;
        double a;
        boolean bo=true;
        while (current<distance){
            if(current>mid&&bo){
                v=12;
                bo=false;
            }
            a=-(random.nextInt(3)+3);
            /*if(current<mid||v<3){
                a=random.nextInt(3)+2;
            }else{
                a=-(random.nextInt(3)+3);
            }*/
            double v0=v;
            double s = v0*t+0.5*a*(t*t);
            if(s<3){
                s=3;
            }
            current+=s;
            forward_tracks.add((int) Math.round(s));
            v=v0+a*t;
            if(s==3&&bo){
                v=random.nextInt(10)+15;
            }
        }

        back_tracks.add(-3);
        back_tracks.add(-3);
        back_tracks.add(-3);
        back_tracks.add(-2);
        back_tracks.add(-2);
        back_tracks.add(-2);
        back_tracks.add(-2);
        back_tracks.add(-1);
        back_tracks.add(-2);

        /*for(int i=0;i<4;i++){
            back_tracks.add(-(random.nextInt(2)+2));
        }

        for(int i=0;i<4;i++){
            back_tracks.add(-(random.nextInt(3)+1));
        }*/
        Map<String,List<Integer>> map=new HashMap<>();
        map.put("forward_tracks",forward_tracks);
        map.put("back_tracks",back_tracks);

        return map;
    }

}