package test;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */
public class test0 {
    public static void main(String args[]) throws Exception {
        takeScreenshot("jieguo");

        //BMPLoader.compareImage("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg","C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt2.jpg");


    }

    public static void get(String s) throws Exception {


        int p=0;
        int f=0;
        String proxyIpAndPort="123.207.143.51:8080";
        p++;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        //caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=http","--proxy=proxy.abuyun.com:9020","--proxy-auth=H112205236B5G2PD:E9484DB291BFC579"});
        System.setProperty(Count.chrome,  Count.chromepath);
        WebDriver driver=new ChromeDriver();
        driver.get("http://www.gsxt.gov.cn/index.html");
        Thread.sleep(3000);
        jietu(driver,s);
        driver.findElement(By.id("keyword")).clear();
        driver.findElement(By.id("keyword")).sendKeys("小米");
        Actions action=new Actions(driver);
        Thread.sleep(1000);
        driver.findElement(By.id("btn_query")).click();
        action.sendKeys(Keys.ENTER).perform();
        Thread.sleep(3000);
        jietu(driver,s);
        while(true) {
  /*          WebElement element = driver.findElement(By.className("gt_cut_bg_slice"));

            FileUtils.copyFile(captureElement(element), new File("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg"));*/
        Document doc=Jsoup.parse(driver.getPageSource());
        List ar=new ArrayList();
        String ele=doc.select("div.gt_cut_bg.gt_show div.gt_cut_bg_slice").attr("style").replace("background-image: url(", "").replaceAll("\\);", "").replace(" background-position: -157px -58px;","").replace("\"","").trim().replace("webp","jpg");

        List<String[]> aa=new ArrayList<String[]>();
        Elements elee=doc.select("div.gt_box div.gt_cut_fullbg.gt_show div.gt_cut_fullbg_slice");
        for(Element e:elee) {
            aa.add(new String[]{e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[0].replace("px", ""), e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[1].replace("px", "")});
        }
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.open('" + "https://www.baidu.com/" + "')");
        Thread.sleep(1000);
        String handle=driver.getWindowHandle();
            for (String handles : driver.getWindowHandles()) {
                if (handles.equals(handle)) {
                    continue;
                }
                driver.switchTo().window(handles);
            }
        driver.get(ele);
            System.out.println(ele);
        for(int qw=0;qw<52;qw++) {
            FileUtils.copyFile(captureElement(driver.findElement(By.xpath("/html/body/img"))), new File("C:\\Users\\Administrator\\Desktop\\aaa\\ml"+qw+".png"));
            ar.add("C:\\Users\\Administrator\\Desktop\\aaa\\ml"+qw+".png");
        }
        System.out.println(imgduibi.combineImages(ar, aa, 26, 10, 58, "C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg", "jpg"));

        String handle2=driver.getWindowHandle();
        driver.close();
        for (String handles2 : driver.getWindowHandles()) {
            if (handles2.equals(handle2)) {
                continue;
            }
            driver.switchTo().window(handles2);
        }

        /*BufferedImage inputbig = createElementImage(driver,element);
        ImageIO.write(inputbig, "png", new File("C:\\Users\\Administrator\\Desktop\\aaa\\a8.png"));*/


            driver.findElement(By.className("gt_slider_knob")).click();
            Thread.sleep(3000);

        Document docc=Jsoup.parse(driver.getPageSource());
        List arr=new ArrayList();

        List<String[]> aaa=new ArrayList<String[]>();
        Elements eleeee=docc.select("div.gt_box div.gt_cut_fullbg.gt_show div.gt_cut_fullbg_slice");
        for(Element e:eleeee){
            aaa.add(new String[]{e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[0].replace("px", ""), e.attr("style").replaceAll("background-image.+position: ", "").replace(";", "").split(" ", 2)[1].replace("px", "")});
        }

        JavascriptExecutor executor2 = (JavascriptExecutor) driver;
        executor2.executeScript("window.open('" + "https://www.baidu.com/" + "')");
        String handleq=driver.getWindowHandle();
        for (String handles : driver.getWindowHandles()) {
            if (handles.equals(handleq)) {
                continue;
            }
            driver.switchTo().window(handles);
        }
        String eleee=ele.replaceAll("bg/.+.jpg",ele.split("/",8)[5]+".jpg");
            System.out.println(eleee);
        driver.get(eleee);
        // WebElement element=driver.findElement(By.className("gt_cut_fullbg"));
        for(int qw=0;qw<52;qw++) {
            FileUtils.copyFile(captureElement(driver.findElement(By.xpath("/html/body/img"))), new File("C:\\Users\\Administrator\\Desktop\\aaa\\ml"+qw+".png"));
            arr.add("C:\\Users\\Administrator\\Desktop\\aaa\\ml"+qw+".png");
        }
        System.out.println(imgduibi.combineImages(arr, aaa, 26, 10, 58, "C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt2.jpg", "jpg"));

        String handle2q=driver.getWindowHandle();
        driver.close();
        for (String handles2 : driver.getWindowHandles()) {
            if (handles2.equals(handle2q)) {
                continue;
            }
            driver.switchTo().window(handles2);
        }


        /*BufferedImage inputbig1 = createElementImage(driver,element1);
        ImageIO.write(inputbig1, "png", new File("C:\\Users\\Administrator\\Desktop\\aaa\\a7.png"));*/
            int b = imgduibi.findXDiffRectangeOfTwoImage("C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt.jpg", "C:\\Users\\Administrator\\Desktop\\aaa\\bbb\\tt2.jpg");
            int a=b-7;
            System.out.println(a);
            WebElement draggable = driver.findElement(By.className("gt_slider_knob"));
            action.clickAndHold(draggable);
            Thread.sleep(1000);
            if(a>=150) {
                action.moveToElement(draggable, (a * 11) / 12, 0).build().perform();
                for (int x = 1; x > 0; x++) {
                    int ll = (int) (Math.random() * 10 + 23);
                    int lls = 0;
                    lls = lls + ll;
                    action.moveToElement(draggable, ll, 0).build().perform();
                    Document doc2 = Jsoup.parse(driver.getPageSource());
                    Thread.sleep((long) (Math.random() * 300+200));
                    if (Integer.parseInt(doc2.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= a - 2) {
                        Thread.sleep(1000);
                        break;
                    }
                    if(x>25){
                        break;
                    }
                }
            }else if(a<150&&a>=100){
                action.moveToElement(draggable, (a * 7) / 12, 0).build().perform();
                for (int x = 1; x > 0; x++) {
                    int ll = (int) (Math.random() * 10 + 23);
                    action.moveToElement(draggable, ll, 0).build().perform();
                    Document doc2 = Jsoup.parse(driver.getPageSource());
                    Thread.sleep((long) (Math.random() * 300+200));
                    if (Integer.parseInt(doc2.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= a - 2) {
                        Thread.sleep(1000);
                        break;
                    }
                    if(x>25){
                        action.release().build().perform();
                        driver.findElement(By.className("gt_refresh_button")).click();
                        Thread.sleep(3000);
                        break;
                    }
                }
            }else if(a<=0){
                driver.findElement(By.className("gt_refresh_button")).click();
                Thread.sleep(3000);
                continue;
            }else{
                action.moveToElement(draggable, (a * 20) / 100, 0).build().perform();
                for (int x = 1; x > 0; x++) {
                    int ll = (int) (Math.random() * 10 + 23);
                    int lls = 0;
                    lls = lls + ll;
                    action.moveToElement(draggable, ll, 0).build().perform();
                    Document doc2 = Jsoup.parse(driver.getPageSource());
                    Thread.sleep((long) (Math.random() * 300+200));
                    if (Integer.parseInt(doc2.select("div.gt_slider_knob.gt_show").attr("style").replace("left: ", "").replace("px;", "")) >= a - 2) {
                        Thread.sleep(1000);
                        break;
                    }
                    if(x>25){
                        break;
                    }
                }
            }
            action.release().build().perform();
            Thread.sleep(500);
            jietu(driver, s);
            Thread.sleep(5000);
            try{
                driver.findElement(By.className("gt_refresh_button")).click();
            }catch (Exception e){
                break;
            }
            Thread.sleep(3000);
        }
        String html=driver.getPageSource();
        Document doc=Jsoup.parse(html);
        String childLink="http://www.gsxt.gov.cn"+doc.select("a.search_list_item.db").attr("href");
        driver.get(childLink);
        JavascriptExecutor executor2 = (JavascriptExecutor) driver;
        for(int o=0;o<10;o++) {
            executor2.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
        Thread.sleep(10000);
        String chtml=driver.getPageSource();
        Document docl=Jsoup.parse(chtml);
        int id=clean(chtml);
        clean_gp(chtml,id);
        int k=Integer.parseInt(docl.select("div.row div.col-sm-5 div.dataTables_info").text().split("\\s+",7)[5]);
        for(int kk=1;kk<k;k++){
            driver.findElement(By.className("paginate_button")).click();
            Thread.sleep(3000);
            String ll=driver.getPageSource();
            clean_gp(ll,id);
        }
        //action.dragAndDropBy(draggable, 69, 0).build().perform();
    }

    public static int clean(String html) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
                if(con!=null){
                    break;
                }
            }
        }

        String insert="insert into company_gx(`company_name`,`social`,`representative`,`authority`,`establishment`,`type`,`capital`,`chengli`,`start`,`end`,`date`,`state`,`address`,`scope`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(insert);



        Document doc=Jsoup.parse(html);
        System.out.println(doc.outerHtml());
        String name=doc.select("div.companyName h1.fullName").text();
        String social=doc.select("div.companyDetail.clearfix span.regNum_inner").get(0).select("span.nameBoxColor").text();
        String representative=doc.select("div.companyDetail.clearfix span.owner span.nameBoxColor").text();
        String authority=doc.select("div.companyDetail.clearfix span.regNum_inner").get(1).select("span.nameBoxColor").text();
        String establishment=doc.select("div.companyDetail.clearfix span.regNum_inner").get(2).select("span.nameBoxColor").text();
        String type=doc.select("div.details.clearfix div.overview dd.result").get(2).text();
        String capital=doc.select("div.details.clearfix div.overview dd.result").get(4).text();
        String chengli=doc.select("div.details.clearfix div.overview dd.result").get(5).text();
        String start=doc.select("div.details.clearfix div.overview dd.result").get(6).text();
        String end=doc.select("div.details.clearfix div.overview dd.result").get(7).text();
        String date=doc.select("div.details.clearfix div.overview dd.result").get(9).text();
        String state=doc.select("div.details.clearfix div.overview dd.result").get(10).text();
        String address=doc.select("div.details.clearfix div.overview dd.result").get(11).text();
        String scope=doc.select("div.details.clearfix div.overview dd").get(12).text();
        ps.setString(1,name);
        ps.setString(2,social);
        ps.setString(3,representative);
        ps.setString(4,authority);
        ps.setString(5,establishment);
        ps.setString(6,type);
        ps.setString(7,capital);
        ps.setString(8,chengli);
        ps.setString(9,start);
        ps.setString(10,end);
        ps.setString(11,date);
        ps.setString(12,state);
        ps.setString(13,address);
        ps.setString(14,scope);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys(); //获取结果
        int id=0;
        if (rs.next()) {
            id = rs.getInt(1);//取得ID
        } else {
            // throw an exception from here
        }
        return id;
    }


    public static void clean_gp(String ll,int gid) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
                if(con!=null){
                    break;
                }
            }
        }

        String insert="insert into company_gp(`gid`,`number`,`registration_number`,`chuzhiren`,`num`,`zhiquanren`,`zhengjianhaoma`,`date_re`,`zt`,`date_gs`,`xq`) values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(insert);



        Document docll=Jsoup.parse(ll);
        Elements ele=docll.select("div.row div.col-sm-12 table.display.dataTable.no-footer").get(3).select("tbody>tr[role=row]");
        System.out.println(docll.select("div.row div.col-sm-12 table.display.dataTable.no-footer").get(3));
        for(Element e:ele){
            String number=e.select("td").get(0).text();
            String registration_number=e.select("td").get(1).text();
            String chuzhiren=e.select("td").get(2).text();
            String num=e.select("td").get(4).text();
            String zhiquanren=e.select("td").get(5).text();
            System.out.println(zhiquanren);
            String zhengjianhaoma=e.select("td").get(6).text();
            String date_re=e.select("td").get(7).text();
            String zt=e.select("td").get(8).text();
            String date_gs=e.select("td").get(9).text();
            String xq=e.select("td").get(10).text();

            ps.setString(1,String.valueOf(gid));
            ps.setString(2,number);
            ps.setString(3,registration_number);
            ps.setString(4,chuzhiren);
            ps.setString(5,num);
            ps.setString(6,zhiquanren);
            ps.setString(7,zhengjianhaoma);
            ps.setString(8,date_re);
            ps.setString(9,zt);
            ps.setString(10,date_gs);
            ps.setString(11,xq);
            ps.executeUpdate();
        }
    }





    public static void jietu(WebDriver driver,String screenPath) throws IOException {
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE); // 关键代码，执行屏幕截图，默认会把截图保存到temp目录
        FileUtils.copyFile(scrFile, new File(screenPath));
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

    public static BufferedImage createElementImage(WebDriver driver,WebElement webElement)
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
        return croppedImage;
    }

    public static byte[] takeScreenshot(WebDriver driver) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
        //TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        //return takesScreenshot.getScreenshotAs(OutputType.BYTES);
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
        ImageIO.write(dest, "png", screen);
        return screen;
    }



    public static boolean combineImages(List<String> imgSrcList, List<String[]> topLeftPointList, int countOfLine, int cutWidth, int cutHeight, String savePath, String subfix) {
        if (imgSrcList == null || savePath == null || savePath.trim().length() == 0) return false;
        BufferedImage lastImage = new BufferedImage(cutWidth * countOfLine, cutHeight * ((int) (Math.floor(imgSrcList.size() / countOfLine))), BufferedImage.TYPE_INT_RGB);
        String prevSrc = "";
        BufferedImage prevImage = null;
        try {
            for (int i = 0; i < imgSrcList.size(); i++) {
                String src = imgSrcList.get(i);
                BufferedImage image;
                if (src.equals(prevSrc)) image = prevImage;
                else {
                    if (src.trim().toLowerCase().startsWith("http"))
                        image = ImageIO.read(new URL(src));
                    else
                        image = ImageIO.read(new File(src));
                    prevSrc = src;
                    prevImage = image;

                }
                if (image == null) continue;
                String[] topLeftPoint = topLeftPointList.get(i);
                int[] pixArray = image.getRGB(0 - Integer.parseInt(topLeftPoint[0].trim()), 0 - Integer.parseInt(topLeftPoint[1].trim()), cutWidth, cutHeight, null, 0, cutWidth);
                int startX = ((i) % countOfLine) * cutWidth;
                int startY = ((i) / countOfLine) * cutHeight;

                lastImage.setRGB(startX, startY, cutWidth, cutHeight, pixArray, 0, cutWidth);
            }
            File file = new File(savePath);
            return ImageIO.write(lastImage, subfix, file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }






    public static String getip(int p) throws IOException {
        Document doc=null;
        String ip[]=null;
        doc= Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(100000)
                .get();
        ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        if(p>=ip.length-5){
            doc=Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();
            ip=doc.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");
        }
        return ip[p];
    }

}
