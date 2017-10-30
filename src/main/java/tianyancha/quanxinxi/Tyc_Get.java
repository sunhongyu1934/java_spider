package tianyancha.quanxinxi;

import com.google.gson.Gson;
import java_socket.LoginClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;
import tianyancha.Data;
import tianyancha.tyc_sou_javabean;

import java.net.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/5/15.
 */
public class Tyc_Get {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Logger logger = Logger.getLogger("loggertyc");
    private static Logger logger2 = Logger.getLogger("loggertycsea");
    public static void main(String args[]) throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));


        Tyc_Get t=new Tyc_Get();
        final duilie d=t.new duilie();
        gongs gongshang=t.new gongs();
        final gongsi gongid=t.new gongsi();
        dudata duda=t.new dudata(gongshang);
        serach sr=t.new serach(gongshang,proxy,gongid);
        send s=t.new send(d);
        get g=t.new get(d,gongid);
        ExecutorService pool= Executors.newCachedThreadPool();
        Monitor monitor=t.new Monitor(g,pool,d,gongid);
        pool.submit(duda);
        for(int x=1;x<=2;x++){
            pool.submit(sr);
        }

        pool.submit(g);
        pool.submit(g);
        pool.submit(s);
        pool.submit(monitor);


    }

    class Monitor implements Runnable{
        private get g;
        private ExecutorService pool;
        private duilie dui;
        private gongsi gs;
        public Monitor(get g,ExecutorService pool,duilie dui,gongsi gs){
            this.g=g;
            this.pool=pool;
            this.dui=dui;
            this.gs=gs;
        }

        public Thread find() throws InterruptedException {
            Thread.sleep(60000);
            Thread thread = null;
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            while(group != null) {
                Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
                int count = group.enumerate(threads, true);
                for(int i = 0; i < count; i++) {
                    if("get-1".equals(threads[i].getName()) ) {
                        thread=threads[i];
                    }
                }
                group = group.getParent();
            }
            return thread;
        }

        @Override
        public void run() {
            Thread thread = null;
            try {
                thread = find();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    Thread.sleep(60000);
                    if(!thread.isAlive()){
                        g=new get(dui,gs);
                        pool.submit(g,"get-1");
                        thread = find();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class gongs{
        BlockingQueue<String> gongshang=new LinkedBlockingQueue<String>();

        public void jin(String key) throws InterruptedException {
            gongshang.put(key);
        }
        public String chu() throws InterruptedException {
            return gongshang.take();
        }
    }

    class dudata implements Runnable{
        private gongs g;
        public dudata(gongs g){
            this.g=g;
        }
        @Override
        public void run() {
            String driver1="com.mysql.jdbc.Driver";
            String url1="jdbc:mysql://101.200.166.12:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
            String username="spider";
            String password="spider";
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

            String sql="select `工商登记号（企业法人营业执照注册号）`,`序号` from tyc_web where `工商登记号（企业法人营业执照注册号）` not in (select gongshang_hao from tyc_information) and 工商登记号（企业法人营业执照注册号） not like '%#N/A%'";
            try {
                PreparedStatement ps=con.prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while (rs.next()){
                    String gongshang=rs.getString(rs.findColumn("工商登记号（企业法人营业执照注册号）"));
                    String pn=rs.getString(rs.findColumn("序号"));
                    g.jin(gongshang+"###"+pn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    class serach implements Runnable{
        private gongs g;
        private Proxy proxy;
        private gongsi gs;
        public serach(gongs g,Proxy proxy,gongsi gs){
            this.g=g;
            this.proxy=proxy;
            this.gs=gs;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    Document doc = null;
                    String oo=g.chu();
                    String names = oo.split("###", 2)[0];
                    String pn=oo.split("###", 2)[1];
                    String name = URLEncoder.encode(names, "UTF-8");
                    logger2.info("开始搜索列表，序号为："+pn);
                    while (true) {
                        try {
                            doc = Jsoup.connect("http://www.tianyancha.com/v2/search/" + name + ".json?")
                                    .timeout(100000)
                                    .proxy(proxy)
                                    .ignoreContentType(true)
                                    .ignoreHttpErrors(true)
                                    .get();
                            if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim())) {
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("reget");
                        }
                    }
                    logger2.info("列表搜索成功，序号为："+pn);
                    logger2.info("--------------------------------------------------------------------------------------");
                    String json = doc.outerHtml().replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("</body>", "").trim();
                    Gson gson = new Gson();
                    tyc_sou_javabean t = gson.fromJson(json, tyc_sou_javabean.class);
                    if(StringUtils.isNotEmpty(t.message)){
                        continue;
                    }
                    for (Data data : t.data) {
                        try {
                            gs.fang(data.id+"###"+pn);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
              cap.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
              //cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=https","--proxy=1719f231c5.51mypc.cn:15113","--proxy-auth=innotree:innotree"});
              System.setProperty(Count.phantomjs, Count.phantomjspath);
              WebDriver driver = null;
              while (true) {
                  try {
                      String oo = gs.qu();
                      String tid = oo.split("###", 2)[0];
                      String pn = oo.split("###", 2)[1];
                      driver = new PhantomJSDriver(cap);
                      driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                      logger.info("开始请求，序号为：" + pn);
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
                              driver = new PhantomJSDriver(cap);
                              driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                          }
                      }
                      logger.info("请求成功，开始向服务器写入。");
                      Document doc = Jsoup.parse(driver.getPageSource());
                      String[] key = new String[]{doc.outerHtml(), pn};
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
                    String flag=LoginClient.client(key[0],key[1]);
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


