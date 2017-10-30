package tianyancha.quanxinxi;

import java_socket.LoginClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/5/23.
 */
public class Zui {
    private static Logger logger = Logger.getLogger("loggertyc");

    public static void main(String args[]){
        Zui z=new Zui();
        gongsi gs=z.new gongsi();
        duilie du=z.new duilie();
        dudata d=z.new dudata(gs);
        get g=z.new get(du,gs);
        send s=z.new send(du);
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(d);
        pool.submit(g);
        pool.submit(g);
        pool.submit(s);
    }




    class dudata implements Runnable{
        private gongsi gs;
        public dudata(gongsi gs){
            this.gs=gs;
        }
        @Override
        public void run() {
            String driver1="com.mysql.jdbc.Driver";
            String url1="jdbc:mysql://112.126.86.232:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
            String username="dev";
            String password="innotree123!@#";
            try {
                Class.forName(driver1).newInstance();
            } catch (Exception e) {
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

            String sql="select t_id from tyc_jichu1 where dengji_jiguan=''";
            try {
                PreparedStatement ps=con.prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while (rs.next()){
                    String tid=rs.getString(rs.findColumn("t_id"));
                    gs.fang(tid);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    class get implements Runnable{
        private duilie dui;
        private gongsi gs;

        public get(duilie dui,gongsi gs){
            this.dui=dui;
            this.gs=gs;
        }
        @Override
        public void run() {
            Thread.currentThread().setName("get-1");
            DesiredCapabilities cap = new DesiredCapabilities();
            //cap.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            //cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=https","--proxy=1719f231c5.51mypc.cn:15113","--proxy-auth=innotree:innotree"});
            System.setProperty(Count.chrome, Count.chromepath);
            WebDriver driver = null;
            while (true) {
                try {
                    String tid = gs.qu();
                    driver = new ChromeDriver(cap);
                    driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                    logger.info("开始请求，序号为：" + tid);
                    while (true) {
                        try {
                            driver.get("http://www.tianyancha.com/company/" + tid);
                            Thread.sleep(1000);
                            try {
                                JavascriptExecutor executornext = (JavascriptExecutor) driver;
                                executornext.executeScript("$('#ng-view > div.ng-scope > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.row.b-c-white.company-content.base2017 > table > tbody > tr:nth-child(6) > td > div > span > a').click()");
                            }catch (Exception e){
                                System.out.println("buyongdian");
                            }
                            Thread.sleep(5000);
                            if (!driver.getPageSource().contains("为确认本次访问为正常用户行为") && !driver.getPageSource().contains("Unavailable") && driver.getPageSource().length() > 60000) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("time out reget");
                            driver.quit();
                            driver = new ChromeDriver(cap);
                            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                        }
                    }
                    logger.info("请求成功，开始向服务器写入。");
                    Document doc = Jsoup.parse(driver.getPageSource());
                    String[] key = new String[]{doc.outerHtml(), tid};
                    dui.fang(key);
                    driver.quit();
                }catch (Exception e){
                    System.out.println("get error");
                }
            }
        }

    }

    class duilie{
        BlockingQueue<String[]> bas=new LinkedBlockingQueue<String[]>();
        public void fang(String[] key) throws InterruptedException {
            bas.put(key);
        }
        public String[] qu() throws InterruptedException {
            return bas.take();
        }
    }

    class gongsi{
        BlockingQueue<String> go=new LinkedBlockingQueue<String>();
        public void fang(String key) throws InterruptedException {
            go.put(key);
        }
        public String qu() throws InterruptedException {
            return go.take();
        }
    }


    class send implements Runnable{
        private duilie dui;
        public send(duilie dui){
            this.dui=dui;
        }
        public void run(){
            while (true) {
                try {
                    String[] key = dui.qu();
                    String flag= LoginClient.client(key[0], key[1]);
                    if(flag!=null&&flag.equals("success")){
                        logger.info("传入服务器成功，序号为："+key[1]+"     文件大小为："+key[0].getBytes().length/1000+"k");
                        logger.info("------------------------------------------------------------------------------------------");
                    }else{
                        logger.info("传入服务器失败，序号为："+key[1]+"     文件大小为："+key[0].getBytes().length/1000+"k");
                        logger.info("------------------------------------------------------------------------------------------");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
