package qichacha;

import Utils.Dup;
import Utils.JsoupUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.logging.LogEntry;
import spiderKc.kcBean.Count;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class Yanzhengma {
    private static String hexStr =  "0123456789ABCDEF";
    public static void main(String args[]) throws Exception {
        test();
    }

    public static void test() throws Exception {
        Random random=new Random();
        System.setProperty(Count.chrome, "D:\\工作\\hy\\资源\\chromedriver.exe");
        ChromeOptions opiions=new ChromeOptions();
        opiions.addArguments("--start-maximized");
        WebDriver driver=new ChromeDriver(opiions);
        driver.get("http://www.qichacha.com/user_login");
        Thread.sleep(1000);
        Actions actions=new Actions(driver);
        for(int a=1;a<=8;a++) {
            actions.sendKeys(Keys.TAB);
        }
        actions.sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);

        actions.clickAndHold(driver.findElement(By.id("nc_2_n1z")));
        int t=0;
        for(int a=1;a<=348;a++){
            int h=random.nextInt(20)+1;
            actions.moveByOffset(h,0);
            actions.perform();
            t=t+h;
            if(t>=348){
                break;
            }
        }
        actions.release().perform();
        Thread.sleep(2000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 200)");
        Thread.sleep(1000);
        FileUtils.copyFile(captureElement(driver.findElement(By.className("clickCaptcha_img"))),new File("1.png"));
        Thread.sleep(2000);
        FileUtils.copyFile(captureElement(driver.findElement(By.id("nc_2__scale_text"))),new File("2.jpg"));
        Document doc= Jsoup.parse(driver.getPageSource());
        String url2= JsoupUtils.getHref(doc,"div.clickCaptcha_img img","src",0).replace("data:image/jpg;base64,","");
        download(url2,"1.jpg");
        String[] imgs={"2.jpg","1.jpg"};
        merge(imgs,"jpg", "3.jpg");
        String zuo=shibie("3.jpg");
        int x=Integer.parseInt(zuo.split("###")[0])+8;
        int y=Integer.parseInt(zuo.split("###")[1])+5;
        Thread.sleep(1000);
        actions.moveToElement(driver.findElement(By.className("clickCaptcha_img")),x,y);
        actions.click().perform();



    }

    public static String BinaryToHexString(byte[] bytes){

        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex;
        }
        return result;
    }

    public static String shibie(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }

        String da=BinaryToHexString(data);
        Document doc =null;
        while (true) {
            try {
                doc = Jsoup.connect("http://apib.sz789.net:88/RecvByte.ashx")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("username", "fleashese")
                        .data("password", "849915")
                        .data("softId", "61558")
                        .data("imgdata", da)
                        .post();
                break;
            }catch (Exception e){
                System.out.println("shibie cuowu");
            }
        }
        String json= Dup.qujson(doc);
        JSONObject jsonObject=new JSONObject(json);
        int x=Integer.parseInt(jsonObject.getString("result").replace(";","").split(",")[0])+15;
        int y=Integer.parseInt(jsonObject.getString("result").replace(";","").split(",")[1])-30;
        System.out.println(x+"  ,   "+y);
        return x+"###"+y;
    }

    public static File captureElement(WebElement element) throws Exception {
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        // 截图整个页面
        File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage img = ImageIO.read(screen);
        // 获得元素的高度和宽度
        //int width = element.getSize().getWidth();
        int width=200;
        int height = element.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        Rectangle rect = new Rectangle(width, height);
        // 得到元素的坐标
        org.openqa.selenium.Point p = element.getLocation();
        int x=p.getX()+235;
        int y=p.getY()-130;
        BufferedImage dest = img.getSubimage(x, y, rect.width,rect.height);
        //存为png格式
        ImageIO.write(dest, "jpg", screen);
        return screen;
    }

    public static File captureElement(WebDriver driver) throws Exception {
        // 截图整个页面
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return screen;
    }

    public static void download(String url,String name) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(url);
        OutputStream outputStream=new FileOutputStream(name);
        outputStream.write(b);
        outputStream.close();
    }


    public static boolean merge(String[] imgs, String type, String dst_pic) {
        //获取需要拼接的图片长度
        int len = imgs.length;
        //判断长度是否大于0
        if (len < 1) {
            return false;
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(imgs[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            // 从图片中读取RGB 像素
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height,  ImageArrays[i], 0, width);
        }

        int dst_height = 0;
        int dst_width = images[0].getWidth();
        //合成图片像素
        for (int i = 0; i < images.length; i++) {
            dst_width = dst_width > images[i].getWidth() ? dst_width     : images[i].getWidth();
            dst_height += images[i].getHeight();
        }
        //合成后的图片
        /*System.out.println("宽度:"+dst_width);
        System.out.println("高度:"+dst_height);*/
        if (dst_height < 1) {
            System.out.println("dst_height < 1");
            return false;
        }
        // 生成新图片
        try {
            // dst_width = images[0].getWidth();
            BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
                    BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            for (int i = 0; i < images.length; i++) {
                try {
                    ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),
                            ImageArrays[i], 0, dst_width);
                    height_i += images[i].getHeight();
                }catch (Exception e){

                }
            }

            File outFile = new File(dst_pic);
            ImageIO.write(ImageNew, type, outFile);// 写图片 ，输出到硬盘
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
