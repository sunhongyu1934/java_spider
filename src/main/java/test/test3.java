package test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/15.
 */
public class test3 {
    public static void main(String args[]) throws Exception {
        /*DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        System.setProperty(Count.phantomjs,Count.phantomjspath);
        WebDriver driver=new PhantomJSDriver(cap);
        driver.get("http://www.tianyancha.com/v2/near/s.json?id=2320664849&pn=1");
        Thread.sleep(10000);
        Document doc= Jsoup.parse(driver.getPageSource());
        System.out.println(doc.outerHtml());
        driver.quit();*/
        //caps.setCapability("phantomjs.page.customHeaders.t", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        Document doc= Jsoup.connect("http://www.tianyancha.com/v2/search/%E5%9B%A0%E6%9E%9C%E6%A0%91.json?pn=1")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        System.out.println(doc.outerHtml());


    }

    public static void get(String path) throws InterruptedException, IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy=proxy.abuyun.com:9020","--proxy-auth=H7748N598W005E8D:A242740AED77F7E4"});
        System.setProperty(Count.phantomjs,Count.phantomjspath);
        WebDriver driver=new PhantomJSDriver(caps);
        driver.get("http://www.innojoy.com/searchresult/default.html");
        Thread.sleep(30000);
        jietu(driver,path);
        driver.findElement(By.id("checkall")).click();
        driver.findElement(By.id("queryExpr-str")).clear();
        driver.findElement(By.id("queryExpr-str")).sendKeys("huawei");
        Thread.sleep(500);
        System.out.println(driver.getPageSource());
        driver.findElement(By.id("btnSearch")).click();
        Thread.sleep(10000);
        jietu(driver,path);
        System.out.println(driver.getPageSource());
        while (true){
            Thread.sleep(1000);
            if(driver.getPageSource().length()>260000){
                break;
            }
        }
        System.out.println(driver.getPageSource());
    }



    public static void takeScreenshot(String imgName) throws Exception {
        String screenName=imgName+ DateUtils.MILLIS_PER_DAY+".jpg";
        String fileString= "C:\\Users\\Administrator\\Desktop\\aaa\\bbb";
        if (!(new File(fileString).isDirectory())) {  // 判断是否存在该目录
            new File(fileString).mkdir();  // 如果不存在则新建一个目录
        }
        File dir = new File(fileString);
        if (!dir.exists())
            dir.mkdirs();
        String screenPath = dir.getAbsolutePath() + "\\" + screenName;
        get(screenPath);
    }

    public static void jietu(WebDriver driver,String screenPath) throws IOException {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE); // 关键代码，执行屏幕截图，默认会把截图保存到temp目录
        FileUtils.copyFile(scrFile, new File(screenPath));
    }
}
