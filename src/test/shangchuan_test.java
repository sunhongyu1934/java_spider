package test;

import JavaBean.qiniubean;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import qiniu.qijson;
import spiderKc.kcBean.Count;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/30.
 */
public class shangchuan_test {
    public static final String ACCESS_KEY = "Wbw_tA3cCzYqakUdU5Xqf-NBbnk80nWMbH4yGr0B"; // 你的access_key
    public static final String SECRET_KEY = "n9ahK41uBFqdKoM-n8EcnwCPJVBOFvKhlnml9qu9"; // 你的secret_key
    public static final String BUCKET_NAME = "innotreelogo"; // 你的secret_key
    private static final String BUCKET_HOST_NAME = "innotreelogo.qiniu.innotree.cn";
    private static Logger logger = Logger.getLogger("loggerqiniu");
    public static void main(String args[]) throws IllegalAccessException, InstantiationException, FormatEexception, SQLException, DocumentException, FileNotFoundException, ClassNotFoundException {
        data();
    }



    public static class FormatEexception extends Exception
    {
        public FormatEexception(String msg)
        {
            super(msg);
        }
    }

    public static void data() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, FileNotFoundException, DocumentException, FormatEexception {
        String username=null;
        String password=null;
        String database=null;
        String table=null;
        String fieldlogo=null;
        String fieldupdate=null;
        String ip=null;

        List<qiniubean> ai=parseXml();

        for(qiniubean q:ai) {
            if (q.getChose().equals("kaifa")) {
                username = "dev";
                password = "innotree123!@#";
                ip = "10.51.120.107";
            } else if (q.getChose().equals("xianshang")) {
                username = "tech_spider";
                password = "sPiDer$#@!23";
                ip = "10.51.52.82";
            } else {
                throw new FormatEexception("you should chose kaifa or xianshang");
            }

            database = q.getDatabase();
            table = q.getName();
            fieldupdate = q.getFieldupdate();

            String driver1 = "com.mysql.jdbc.Driver";
            String url1 = "jdbc:mysql://" + ip + ":3306/" + database + "?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
            Class.forName(driver1).newInstance();
            java.sql.Connection con = null;
            try {
                con = DriverManager.getConnection(url1, username, password);
            } catch (Exception e) {
                while (true) {
                    con = DriverManager.getConnection(url1, username, password);
                    if (con != null) {
                        break;
                    }
                }
            }


            for(String ss:q.getFieldlogos()) {
                fieldlogo=ss;
                String select = "select " + fieldlogo + "," + fieldupdate + " from " + table + " where " + fieldlogo + " not like '%http://7xnnx4.com2.z0%' and " + fieldlogo + " not like '%innotreelogo.qiniu.innotree.cn%' and " + fieldlogo + " !=''";
                String update = "update " + table + " set " + fieldlogo + "=? where " + fieldupdate + " =?";
                PreparedStatement psu = con.prepareStatement(update);
                PreparedStatement ps = con.prepareStatement(select);
                ResultSet rs = ps.executeQuery();
                int a = 1;
                logger.info("get data link success begin select");
                String uui = UUID.randomUUID().toString();
                while (rs.next()) {
                    String yuanfield = rs.getString(rs.findColumn(fieldlogo));
                    String up = rs.getString(rs.findColumn(fieldupdate));
                    logger.info("begin put this field:" + up);
                    try {
                        String xinfield = shangchuan(yuanfield, uui);
                        if (StringUtils.isNotEmpty(xinfield)) {
                            psu.setString(1, xinfield);
                            psu.setString(2, up);
                            try {
                                psu.executeUpdate();
                            } catch (Exception e1) {
                                logger.info("store database error");
                            }
                            logger.info("update success current field:" + up + "  this is " + a + " line");
                            a++;
                            logger.info("---------------------------------------------------------------------------");
                        } else {
                            logger.info("jietu error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("put error this field:" + up);
                    }
                }
            }
        }
        delFolder("pic/");
    }


    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }



    public static String shangchuan(String url,String uui) throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth);
        String newUrl =null;
        // 要求url可公网正常访问BucketManager.fetch(url, bucketName, key);
        // @param url 网络上一个资源文件的URL
        // @param bucketName 空间名称
        // @param key 空间内文件的key[唯一的]

        try {
            DefaultPutRet putret = bucketManager.fetch(url, BUCKET_NAME);
            newUrl = "https://" + BUCKET_HOST_NAME + "/" + putret.key;
        }catch (Exception e){
            if(jietu(url,uui)) {
                UploadManager uploadManager = new UploadManager();
                Response res = uploadManager.put(uui + ".jpg", null, getUpToken());
                String json = res.bodyString();
                Gson gson = new Gson();
                qijson q = gson.fromJson(json, qijson.class);
                newUrl = "https://" + BUCKET_HOST_NAME + "/" + q.key;
            }else{
                newUrl=null;
            }
        }

        return newUrl;
    }

    public static String getUpToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET_NAME);
    }


    public static boolean jietu(String url,String uui) throws Exception {
        System.setProperty(Count.phantomjs,Count.linuxph);
        WebDriver driver=new PhantomJSDriver();
        driver.get(url);
        try {
            Thread.sleep(500);
            FileUtils.copyFile(captureElement(driver.findElement(By.xpath("/html/body/img"))), new File("/data1/spider/java_spider/pic/"+uui+".jpg"));
            driver.quit();
        }catch (Exception e){
            driver.quit();
            return false;
        }
        return true;
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

    public static List<qiniubean> parseXml() throws FileNotFoundException, DocumentException {
        SAXReader saxReader=new SAXReader();
        List<qiniubean> qis=new ArrayList<qiniubean>();

        org.dom4j.Document dom =  saxReader.read(new FileInputStream(new File("/data1/spider/java_spider/src/qiniu/qiniu.xml")));
        Element root=dom.getRootElement();
        List<Element> tables=root.elements("table");


        for(Element e:tables) {
            qiniubean qi=new qiniubean();
            List<String> f=new ArrayList<String>();

            Element fieldlogos=e.element("fieldlogos");
            for(Element ele:(List<Element>) fieldlogos.elements("fieldlogo")){
                f.add(ele.getText());
            }
            qi.setFieldlogos(f);

            if (e.element("chose") != null) {
                qi.setChose(e.element("chose").getText());
            }

            if (e.element("name") != null) {
                qi.setName(e.element("name").getText());
            }

            if (e.element("database") != null) {
                qi.setDatabase(e.element("database").getText());
            }

            if (e.element("fieldupdate") != null) {
                qi.setFieldupdate(e.element("fieldupdate").getText());
            }
            qis.add(qi);
        }
        return qis;
    }


}
