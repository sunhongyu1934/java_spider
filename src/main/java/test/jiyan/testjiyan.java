package test.jiyan;

import Utils.Dup;
import Utils.RedisClu;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;
import spiderKc.kcBean.Count;
import test.imgduibi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class testjiyan {
    private static RedisClu redisClu=new RedisClu();
    public static void main(String args[]) throws Exception {
        Random random=new Random();
        System.setProperty(Count.chrome, Count.chromepath);
        String ip ;
        while (true) {
            ip=redisClu.get("ip");
            if(Dup.nullor(ip)){
                break;
            }else{
                Thread.sleep(500);
            }
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--proxy-server=http://" + ip);
        WebDriver driver=new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        while (true) {
            try {
                while (true) {
                    ip=redisClu.get("ip");
                    if(Dup.nullor(ip)){
                        break;
                    }else{
                        Thread.sleep(500);
                    }
                }
                options = new ChromeOptions();
                //options.addArguments("--proxy-server=http://" + ip);
                driver = new ChromeDriver(options);
                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                //driver.manage().window().maximize();
                System.out.println("准备进入登录页面");
                driver.get("https://www.tianyancha.com/login");
                Thread.sleep(5000);
                System.out.println("进入登录页面成功，开始获取验证码");
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div[2]/div/div[2]/div/div[3]/div[1]/div[2]")).click();
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/input")).sendKeys("13717951934");
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/input")).sendKeys("3961shy3961");
                Thread.sleep(1000);
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]")).click();
                System.out.println("第一次点击");
                Thread.sleep(5000);
                //driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]")).click();
                //System.out.println("第二次点击");
                //Thread.sleep(5000);
                tt(driver);
                tt2(driver);

                int b=imgduibi.findXDiffRectangeOfTwoImage("C:\\Users\\13434\\Desktop/jiyan/sy/tt.jpg", "C:\\Users\\13434\\Desktop/jiyan/sy/tt2.jpg");
                int a=b-7;
                //int f=(a*7)/8;
                System.out.println(a);
                Actions actions=new Actions(driver);
                WebElement drag=driver.findElement(By.xpath("/html/body/div[10]/div[2]/div[2]/div[2]/div[2]"));
                actions.clickAndHold(drag).build().perform();
                Map<String,List<Integer>> map;
                if(a>100){
                    map=getTrack(a);
                }else{
                    map=getTrackd(a);
                }
                System.out.println(map);
                List<Integer> forward_tracks=map.get("forward_tracks");
                List<Integer> back_tracks=map.get("back_tracks");
                for(Integer i:forward_tracks){
                    actions.moveByOffset( i, 0).build().perform();
                    Thread.sleep(random.nextInt(11));
                }
                /*Thread.sleep(500);
                for(Integer i:back_tracks){
                    actions.moveByOffset( i, 0).build().perform();
                }
                Thread.sleep(300);
                actions.moveByOffset( -3, 0).build().perform();
                Thread.sleep(400);
                actions.moveByOffset( 3, 0).build().perform();*/
                Thread.sleep(500);
                actions.release().build().perform();
                Thread.sleep(5000);
                Document doc=Jsoup.parse(driver.getPageSource());
                if(!doc.outerHtml().contains("请输入手机号")&&!doc.outerHtml().contains("手机号或密码错误")) {
                    System.out.println("登录成功");
                    Set<Cookie> set=driver.manage().getCookies();
                    driver.close();
                    driver.quit();
                    Map<String,String> mapcookie=new HashMap<>();
                    for(Cookie cookie:set){
                        mapcookie.put(cookie.getName(),cookie.getValue());
                    }
                    System.out.println(mapcookie.toString());
                }else if(doc.outerHtml().contains("手机号或密码错误")){
                    System.out.println("密码有误，结束登录");
                    driver.close();
                    driver.quit();
                    System.out.println("密码错误");
                }else{
                    System.out.println("登录失败，从新登录");
                    driver.close();
                    driver.quit();
                }
            } catch (Exception e) {
                driver.close();
                driver.quit();
            }
        }
    }

    public static Map<String,List<Integer>> getTrackd(int distance){
        Random random=new Random();
        double v=0;
        double t=0.2;
        List<Integer> forward_tracks=new ArrayList<>();
        List<Integer> back_tracks=new ArrayList<>();
        double current=0;
        //double mid=(distance*6)/10;
        double mid=distance-20;
        //distance+=20;
        double a;
        boolean bo=true;
        while (current<distance){
            if(current>mid&&bo){
                v=0;
                bo=false;
            }
            a=random.nextInt(3)+2;
            /*if(current<mid||v<3){
                a=random.nextInt(3)+2;
            }else{
                a=-(random.nextInt(3)+3);
            }*/
            double v0=v;
            double s = v0*t+0.5*a*(t*t);
            if(s>3){
                s=3;
            }
            current+=s;
            forward_tracks.add((int) Math.round(s));
            v=v0+a*t;
            if(s==3){
                v=random.nextInt(6)+5;
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

    public static Map<String,List<Integer>> getTrack(int distance){
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

    public static Integer sumList(List<Integer> list){
        int sum=0;
        for(Integer i:list){
            sum+=i;
        }
        return sum;
    }

    public static void tt(WebDriver driver) throws Exception {
        List ar=new ArrayList();
        Document doc= Jsoup.parse(driver.getPageSource());
        String url1=doc.select("body > div.gt_holder.gt_popup.gt_show > div.gt_popup_wrap > div.gt_popup_box > div.gt_widget > div.gt_box_holder > div.gt_box > a.gt_fullbg.gt_show > div.gt_cut_fullbg.gt_show > div:nth-child(1)")
                .toString().replace("<div class=\"gt_cut_fullbg_slice\" style=\"background-image: url(&quot;","")
                .replaceAll("&quot;\\); background-position: .+\"></div>","");
        List<String[]> aa=new ArrayList<String[]>();
        Elements elee=doc.select("div.gt_box div.gt_cut_fullbg.gt_show div.gt_cut_fullbg_slice");
        for(Element e:elee) {
            aa.add(new String[]{e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[0].replace("px", ""), e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[1].replace("px", "")});
        }
        System.out.println("获取第一张图片url完毕，开始进行下载："+url1);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.open('" + url1 + "')");
        Thread.sleep(1000);
        String handle=driver.getWindowHandle();
        for (String handles : driver.getWindowHandles()) {
            if (handles.equals(handle)) {
                continue;
            }
            driver.switchTo().window(handles);
        }
        File file=captureElement(driver.findElement(By.xpath("/html/body/img")));
        for(int qw=0;qw<52;qw++) {
            FileUtils.copyFile(file, new File("C:\\Users\\13434\\Desktop/jiyan/ml/"+qw+".png"));
            ar.add("C:\\Users\\13434\\Desktop/jiyan/ml/"+qw+".png");
        }
        System.out.println("下载完毕，开始拼接");
        System.out.println(imgduibi.combineImages(ar, aa, 26, 10, 58, "C:\\Users\\13434\\Desktop/jiyan/sy/tt.jpg", "jpg"));
        driver.close();
        driver.switchTo().window(handle);
    }

    public static void tt2(WebDriver driver) throws Exception {
        List ar=new ArrayList();
        Document doc= Jsoup.parse(driver.getPageSource());
        String url1=doc.select("body > div.gt_holder.gt_popup.gt_show > div.gt_popup_wrap > div.gt_popup_box > div.gt_widget > div.gt_box_holder > div.gt_box > a.gt_bg.gt_show > div.gt_cut_bg.gt_show > div:nth-child(1)")
                .toString().replace("<div class=\"gt_cut_bg_slice\" style=\"background-image: url(&quot;","")
                .replaceAll("&quot;\\); background-position: .+\"></div>","");
        List<String[]> aa=new ArrayList<String[]>();
        Elements elee=doc.select("div.gt_box div.gt_cut_bg.gt_show div.gt_cut_bg_slice");
        for(Element e:elee) {
            aa.add(new String[]{e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[0].replace("px", ""), e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[1].replace("px", "")});
        }
        System.out.println("获取第二张图片url完毕，开始进行下载："+url1);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.open('" + url1 + "')");
        Thread.sleep(1000);
        String handle=driver.getWindowHandle();
        for (String handles : driver.getWindowHandles()) {
            if (handles.equals(handle)) {
                continue;
            }
            driver.switchTo().window(handles);
        }
        Thread.sleep(1000);
        File file=captureElement(driver.findElement(By.xpath("/html/body/img")));
        for(int qw=0;qw<52;qw++) {
            FileUtils.copyFile(file, new File("C:\\Users\\13434\\Desktop/jiyan/ml2/"+qw+".png"));
            ar.add("C:\\Users\\13434\\Desktop/jiyan/ml2/"+qw+".png");
        }
        System.out.println("下载完毕，开始拼接");
        System.out.println(imgduibi.combineImages(ar, aa, 26, 10, 58, "C:\\Users\\13434\\Desktop/jiyan/sy/tt2.jpg", "jpg"));
        driver.close();
        driver.switchTo().window(handle);
    }

    public static File captureElement(WebElement element) throws Exception {
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        // 截图整个页面
        File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screen, new File("C:\\Users\\13434\\Desktop/jiyan/ce/qqq.png"));
        BufferedImage img = ImageIO.read(screen);
        // 获得元素的高度和宽度
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        java.awt.Rectangle rect = new java.awt.Rectangle(width, height);
        // 得到元素的坐标
        org.openqa.selenium.Point p = element.getLocation();
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width,rect.height);
        //存为png格式
        ImageIO.write(dest, "png", screen);
        return screen;
    }

    public static File createElementImage(WebDriver driver,WebElement webElement,String path)
            throws IOException {
        // 获得webElement的位置和大小。
        org.openqa.selenium.Point location = webElement.getLocation();
        org.openqa.selenium.Dimension size = webElement.getSize();
        // 创建全屏截图。
        BufferedImage originalImage =
                ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
        // 截取webElement所在位置的子图。
        BufferedImage croppedImage = originalImage.getSubimage(
                location.getX(),
                location.getY(),
                size.getWidth(),
                size.getHeight());
        File file=new File(path);
        ImageIO.write(croppedImage, "png", file);
        return file;
    }

    public static byte[] takeScreenshot(WebDriver driver) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        //TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        //return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }
}
