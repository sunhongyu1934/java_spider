package test;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Administrator on 2017/3/29.
 */
public class ceshi {
    public static void main(String args[]) throws Exception {

        /*DesiredCapabilities cap = new DesiredCapabilities();
        Proxy proxy=new Proxy();
        proxy.setHttpProxy("proxy.abuyun.com:9020").setFtpProxy("proxy.abuyun.com:9020").setSslProxy("proxy.abuyun.com:9020");
        cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        cap.setCapability(CapabilityType.PROXY, proxy);
        cap.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        System.setProperty(Count.chrome, Count.chromepath);
        //cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--proxy-type=http", "--proxy=proxy.abuyun.com:9020", "--proxy-auth=H112205236B5G2PD:E9484DB291BFC579"});*/

        DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        //caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=http","--proxy=proxy.abuyun.com:9020","--proxy-auth=H112205236B5G2PD:E9484DB291BFC579"});
        System.setProperty(Count.chrome,  Count.chromepath);
        WebDriver driver=new ChromeDriver(caps);
        driver.get("http://www.gsxt.gov.cn/index.html");
        Thread.sleep(3000);
        driver.findElement(By.id("keyword")).clear();
        driver.findElement(By.id("keyword")).sendKeys("小米");
        Actions action=new Actions(driver);
        Thread.sleep(1000);
        driver.findElement(By.id("btn_query")).click();
        action.sendKeys(Keys.ENTER).perform();
        Thread.sleep(2000);

        WebElement element = driver.findElement(By.className("gt_box"));
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        FileUtils.copyFile(captureElement(element), new File("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg"));
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb");
        driver.findElement(By.className("gt_slider_knob")).click();
        Thread.sleep(3000);


        WebElement element2 = driver.findElement(By.className("gt_box"));

        FileUtils.copyFile(captureElement(element2), new File("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt2.jpg"));

        int a=imgduibi.findXDiffRectangeOfTwoImage("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg","C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt2.jpg");

        System.out.println(a);
        WebElement draggable = driver.findElement(By.className("gt_slider_knob"));
        action.clickAndHold(draggable).perform();
        Thread.sleep(1000);

        /*while(true) {
            action.moveToElement(draggable, l, 0).build().perform();
            Document doc3 = Jsoup.parse(driver.getPageSource());
            if (Integer.parseInt(doc3.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= (30 * a) / 100) {
                while(true){
                    action.moveToElement(draggable, l, 0).build().perform();
                    Thread.sleep((long) (Math.random()*500));
                    Document doc2 = Jsoup.parse(driver.getPageSource());
                    if(Integer.parseInt(doc2.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= (80 * a) / 100){
                        while (true){
                            action.moveToElement(draggable, l, 0).build().perform();
                            Thread.sleep((long) (Math.random()*200));
                            Document doc0 = Jsoup.parse(driver.getPageSource());
                            if (Integer.parseInt(doc0.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= a - 10 && Integer.parseInt(doc0.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) <= a - 3) {
                                System.out.println("aaaaaa");
                                Thread.sleep(1000);
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }*/
        int b=a-7;
        int sum=0;


        if(b<100){
            while(true) {
                int v2= (int) (Math.random()*2+23);
                action.moveToElement(draggable, v2, 0).perform();
                sum=sum+(v2-22);
                if(sum>=(50*b)/100){
                    int v= (int) (Math.random()*5+23);
                    action.moveToElement(draggable, v, 0).perform();
                    Thread.sleep((long) (Math.random()*10+10));
                    sum=sum+(v-22);
                }
                if(sum>=(85*b)/100){
                    while(true){
                        int v1= (int) (Math.random()*2+23);
                        action.moveToElement(draggable, v1, 0).perform();
                        Thread.sleep((long) (Math.random()*200+300));
                        sum=sum+(v1-22);
                        if(sum>=b-1&&sum<=b+1){
                            break;
                        }
                    }
                    break;
                }
            }
        }else if(b>=100&&b<150){
            while(true) {
                int v2= (int) (Math.random()*2+23);
                action.moveToElement(draggable, v2, 0).perform();
                sum=sum+(v2-22);
                if(sum>=(50*b)/100){
                    int v= (int) (Math.random()*5+23);
                    action.moveToElement(draggable, v, 0).perform();
                    Thread.sleep((long) (Math.random()*10+10));
                    sum=sum+(v-22);
                }
                if(sum>=(92*b)/100){
                    while(true){
                        int v1= (int) (Math.random()*2+23);
                        action.moveToElement(draggable, v1, 0).perform();
                        Thread.sleep((long) (Math.random()*200+300));
                        sum=sum+(v1-22);
                        if(sum>=b-1&&sum<=b+1){
                            break;
                        }
                    }
                    break;
                }
            }
        }else{
            while(true) {
                int v2= (int) (Math.random()*2+23);
                action.moveToElement(draggable, v2, 0).perform();
                sum=sum+(v2-22);
                if(sum>=(50*b)/100){
                    int v= (int) (Math.random()*5+23);
                    action.moveToElement(draggable, v, 0).perform();
                    Thread.sleep((long) (Math.random()*10+10));
                    sum=sum+(v-22);
                }
                if(sum>=(97*b)/100){
                    while(true){
                        int v1= (int) (Math.random()*2+23);
                        action.moveToElement(draggable, v1, 0).perform();
                        Thread.sleep((long) (Math.random()*200+300));
                        sum=sum+(v1-22);
                        if(sum>=b-1&&sum<=b+1){
                            break;
                        }
                    }
                    break;
                }
            }
        }


        Thread.sleep(500);
        action.release().build().perform();

    }



    public static File captureElement(WebElement element) throws Exception {
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        // 截图整个页面
        File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage img = ImageIO.read(screen);
        // 获得元素的高度和宽度
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        Rectangle rect = new Rectangle(width, height);
        // 得到元素的坐标
        org.openqa.selenium.Point p = element.getLocation();
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width,rect.height);
        //存为png格式
        ImageIO.write(dest, "jpg", screen);
        return screen;
    }
}
