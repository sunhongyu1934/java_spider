package test.jiyan;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;
import spiderKc.kcBean.Count;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class sanjijietu {
    static List<WebElement> ll=new ArrayList<>();
    public static void main(String args[]) throws IOException, InterruptedException, AWTException {
        String[] str=new String[]{"","","","","","","","","",""};
        jietu("快看世界（北京）科技有限公司");
    }

    public static void jietu(String comp) throws InterruptedException, IOException, AWTException {
        System.setProperty(Count.chrome, Count.chromepath);
        WebDriver driver=new ChromeDriver();
        driver.manage().window().fullscreen();
        driver.get("http://172.29.29.30:8088/searchGraph/treesearch?wd="+ URLEncoder.encode(comp,"utf-8") +"&type=company");
        Thread.sleep(2000);
        int q=0;
        for (int pp=0;pp<3;pp++) {
            q = dian(driver,q);
            if(q==0){
                break;
            }
        }

        Robot robot=new Robot();
        robot.mouseWheel(3);
        BufferedImage originalImage =
                ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
        ImageIO.write(originalImage, "png", new File("C:\\Users\\13434\\Desktop\\aa\\"+comp+".png"));
        driver.quit();
    }

    public static byte[] takeScreenshot(WebDriver driver) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        //TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        //return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }


    public static int dian(WebDriver driver,int q) throws InterruptedException {
        List<WebElement> list=driver.findElements(By.className("ghostCircle"));
        int sum;
        if(q==0){
            sum=list.size()-1;
        }else{
            sum=list.size();
        }
        if(q==list.size()){
            return 0;
        }
        for(int a=q;a<sum;a++){
            try {
                List<WebElement> list3 = driver.findElements(By.className("ghostCircle"));
                list.get(a).click();
                Thread.sleep(1000);
                List<WebElement> list2 = driver.findElements(By.className("ghostCircle"));
                System.out.println(list3.size() + "***************" + list2.size());
                if (list2.size() < list3.size()) {
                    list.get(a).click();
                    Thread.sleep(1000);
                }
            }catch (Exception e){

            }
        }
        System.out.println(list.size());
        return list.size();
    }
}

