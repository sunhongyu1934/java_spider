package tianyancha.zhuce;

import Utils.Dup;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import spiderKc.kcBean.Count;

import java.io.*;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class tyc_zhuce {
    //private static Connection conn;

    static{
        /*String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/tyc?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con!=null){
                    break;
                }
            }
        }
        conn=con;*/

    }

    public static void main(String args[]) throws IOException, InterruptedException, SQLException {
        ExecutorService pool= Executors.newFixedThreadPool(10);
        for(int a=1;a<=10;a++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    String token = null;
                    try {
                        token = getYanzheng();
                        zhuce(token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static String getYanzheng() throws IOException {
        String json;
        while (true) {
            try {
                Document doc = Jsoup.connect("http://api.eobzz.com/httpApi.do?action=loginIn&uid=fleashesee&pwd=shyinnotree123")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(3000)
                        .get();
                json = Dup.qujson(doc);
                if(json.contains("fleashesee")){
                    break;
                }
            }catch (Exception e){

            }
        }
        return json.split("\\|")[1];
    }

    public static String getMob(String token) throws IOException {
        String json;
        while (true) {
            try {
                Document doc = Jsoup.connect("http://api.eobzz.com/httpApi.do?action=getMobilenum&pid=22859&uid=fleashesee&token=" + token + "&mobile=&size=1")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(20000)
                        .get();
                json = Dup.qujson(doc);
                if(json.contains(token)){
                    break;
                }
            }catch (Exception e){
                System.out.println("get phone error");
            }
        }
        return json.split("\\|")[0];
    }

    public static String getMa(String token,String mob,WebDriver driver) throws IOException, InterruptedException {
        String json;
        int a=0;
        while (true) {
            try {
                Thread.sleep(6000);
                Document doc = Jsoup.connect("http://api.eobzz.com/httpApi.do?action=getVcodeAndReleaseMobile&uid=fleashesee&token=" + token + "&mobile=" + mob + "&author_uid=fleashesee")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(20000)
                        .get();
                json = Dup.qujson(doc);
                System.out.println(json);
                if(json!=null&&json.contains("天眼查")){
                    break;
                }
                a++;
                if(a>=10){
                    return "is null";
                }
            }catch (Exception e){
                System.out.println("get ma error*************************************");
                Thread.sleep(3000);
            }
        }
        return json.split("\\|")[1];
    }

    public static void zhuce(String token) throws InterruptedException, SQLException {
        //String sql="insert into tyc_auth(user_name,pass_word) values(?,?)";
        //PreparedStatement ps=conn.prepareStatement(sql);

        System.setProperty(Count.chrome, "D:\\工作\\hy\\资源\\chromedriver.exe");
        ChromeOptions opiions=new ChromeOptions();
        opiions.addArguments("--start-maximized");
        Random random=new Random();
        WebDriver driver=new ChromeDriver(opiions);
        while (true) {
            try {
                String mob=getMob(token);
                driver.manage().deleteAllCookies();
                driver.get("https://www.tianyancha.com/login");
                Thread.sleep(500);
                driver.findElement(By.xpath("//div[@class='modulein modulein1 mobile_box pl30 pr30 f14 collapse in']/div[@class='text-center pt28']/div[@class='c9 f14 in-block point border-blue-bottom']")).click();
                driver.findElement(By.xpath("//input[@class='_input input_nor contactphone']")).sendKeys(mob);
                Thread.sleep(500);
                driver.findElement(By.xpath("//div[contains(text(),'获取验证码')]")).click();

                Thread.sleep(6000);
                String yanzheng = getMa(token, mob,driver).replace("【天眼查】你好， 你的验证码是：", "").replace("，有效时间3分钟", "");
                if(yanzheng.equals("is null")) {
                    continue;
                }
                int m = random.nextInt(12) + 8;
                String mi = getMi(m);
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div/div/div[2]/div/div[2]/div[1]/div[5]/input")).sendKeys(mi);
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div/div/div[2]/div/div[2]/div[1]/div[6]/input")).sendKeys(mi);
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div/div/div[2]/div/div[2]/div[1]/div[3]/input")).sendKeys(yanzheng);
                Thread.sleep(500);
                driver.findElement(By.xpath("//*[@id=\"web-content\"]/div/div/div/div[2]/div/div[2]/div[1]/div[7]")).click();
                while (true){
                    if(driver.getPageSource().length()>80000){
                        break;
                    }
                    Thread.sleep(500);
                }

                xieru(mob,mi);
                /*ps.setString(1, mob);
                ps.setString(2, mi);
                ps.executeUpdate();*/
            } catch (Exception e) {
                e.printStackTrace();
                driver.quit();
                driver=new ChromeDriver(opiions);
            }
        }
    }

    public static synchronized void xieru(String mob,String mi) throws IOException {
        XSSFWorkbook wb;
        InputStream inputStream = null;
        if(fileExist("C:\\Users\\13434\\Desktop\\user.xlsx")){
            inputStream=new FileInputStream("C:\\Users\\13434\\Desktop\\user.xlsx");
            wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet=wb.getSheet("user");
            for(int a=0;a<10000;a++){
                XSSFRow row=sheet.getRow(a);
                if(row==null) {
                    row=sheet.createRow(a);
                    XSSFCell cell = row.createCell(0);
                    XSSFCell cell1 = row.createCell(1);
                    if (!Dup.nullor(cell.getRawValue())) {
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(mob);
                        cell1.setCellType(CellType.STRING);
                        cell1.setCellValue(mi);
                        break;
                    }
                }
            }
        }else{
            wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("user");
            XSSFRow row = sheet1.createRow(0);
            XSSFCell cell = row.createCell(0);
            XSSFCell cell1 = row.createCell(1);
            cell.setCellValue(mob);
            cell1.setCellValue(mi);
        }

        OutputStream outputStream=new FileOutputStream("C:\\Users\\13434\\Desktop\\user.xlsx");
        wb.write(outputStream);
        if(inputStream!=null) {
            inputStream.close();
        }
        outputStream.close();
    }

    public static boolean fileExist(String fileDir){
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }

    public static String getMi(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        String str2="1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length-3; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        for(int i=0; i<3; ++i){
            //产生0-61的数字
            int number=random.nextInt(9);
            //将产生的数字通过length次承载到sb中
            sb.append(str2.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }
}
