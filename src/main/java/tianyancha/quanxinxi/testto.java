package tianyancha.quanxinxi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/5/15.
 */
public class testto {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "223.153.96.85";
    final static Integer ProxyPort = 19833;
    public static void main(String args[]) throws IOException, InterruptedException {

        testto t=new testto();
        final cangku c=t.new cangku();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    get(c);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


        /*while (true) {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--proxy-type=http", "--proxy=" + c.qu(),});
            System.setProperty(Count.phantomjs, Count.phantomjspath);
            WebDriver driver = new PhantomJSDriver(cap);
            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
            try {
                driver.get("http://www.itjuzi.com/company/62711");
                Thread.sleep(5000);
                Document doc = Jsoup.parse(driver.getPageSource());
                System.out.println(doc.body());
            }catch (Exception e){
                driver.quit();
            }*/


            while (true) {
                String ip=c.qu();
                System.out.println(ip);
                Document doc = Jsoup.connect("http://www.itjuzi.com/company/12225")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                        .timeout(100000)
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":",2)[1]))
                        .get();
                System.out.println(doc.body());
                Thread.sleep(5000);
            }

















        /*Document doc= Jsoup.connect("http://www.tianyancha.com/v2/search/440301103097413.json?")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();
        Gson gson=new Gson();
        Bean.Sousuo sou=gson.fromJson(doc.outerHtml().replace("<em>","").replace("</em>","").replace("\n","").replace("<html> <head></head> <body>","").replace("</body></html>","").trim(), Bean.Sousuo.class);
        String quancheng=sou.data.get(0).name;
        String logo=sou.data.get(0).logo;
        String phone=sou.data.get(0).phone;
        String web=sou.data.get(0).websites;
        String email=sou.data.get(0).emails;
        String address=sou.data.get(0).regLocation;
        String faren=sou.data.get(0).legalPersonName;
        String zhuceziben=sou.data.get(0).regCapital;
        String zhuceshijian=sou.data.get(0).estiblishTime;
        String zhuangtai=sou.data.get(0).regStatus;
        String jingyingfanwei=sou.data.get(0).businessScope;

        System.out.println(doc.outerHtml().replace("<em>","").replace("</em>","").replace("\n",""));*/



        /*System.setProperty(Count.chrome,Count.chromepath);
        WebDriver driver=new ChromeDriver();
        driver.get("http://www.tianyancha.com/company/23402373");
        Thread.sleep(1000);
        JavascriptExecutor executornext = (JavascriptExecutor) driver;
        executornext.executeScript("$('#ng-view > div.ng-scope > div > div > div > div.col-9.company-main.pl0.pr10.company_new_2017 > div > div.pl30.pr30.pt25 > div:nth-child(2) > div.row.b-c-white.company-content.base2017 > table > tbody > tr:nth-child(6) > td > div > span > a').click()");
        Thread.sleep(5000);



        Document doc= Jsoup.parse(driver.getPageSource());
        String quancheng=getString(doc,"span.f18.in-block.vertival-middle.ng-binding",0);
        String cengyongming=getString(doc,"div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4.ng-binding",0);
        String logo=getHref(doc,"div.b-c-white.new-border.over-hide.mr10.ie9Style img","ng-src",0);
        String phone=getString(doc,"div.f14.new-c3.mt10 span.ng-binding",0);
        String email=getString(doc,"div.in-block.vertical-top span.in-block.vertical-top.overflow-width.emailWidth.ng-binding",0);
        String web=getString(doc,"div.f14.new-c3 div.in-block.vertical-top.overflow-width.mr20 a.c9.ng-binding.ng-scope",0);
        String address=getString(doc,"div.in-block.vertical-top span.in-block.overflow-width.vertical-top.emailWidth.ng-binding",1);
        String faren=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td a.in-block.vertival-middle.overflow-width.f14.mr20.ng-binding.ng-scope.ng-isolate-scope",0);
        String zhuceziben=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",0);
        String zhuceshijian=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",1);
        String zhuangtai=getString(doc,"div.baseInfo_model2017 table.table.companyInfo-table.text-center.f14 td div.baseinfo-module-content-value.ng-binding",2);
        String gongshang=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",0);
        String zuzhijigou=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",1);
        String tongyixinxyong=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",2);
        String qiyeleixing=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",3);
        String hangye=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",4);
        String yingyenianxian=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",5);
        String hezhunriqi=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",6);
        String dengjijiguan=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",7);
        String zhucedizhi=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",8);
        String jingyingfanwei=getString(doc,"div.row.b-c-white.company-content.base2017 tbody tr td div.c8 span.ng-binding",9);
        System.out.println(quancheng);
        System.out.println(cengyongming);
        System.out.println(logo);
        System.out.println(phone);
        System.out.println(email);
        System.out.println(web);
        System.out.println(address);
        System.out.println(faren);
        System.out.println(zhuceziben);
        System.out.println(zhuceshijian);System.out.println(zhuangtai);
        System.out.println(gongshang);
        System.out.println(zuzhijigou);
        System.out.println(tongyixinxyong);
        System.out.println(qiyeleixing);
        System.out.println(hangye);
        System.out.println(yingyenianxian);
        System.out.println(hezhunriqi);
        System.out.println(dengjijiguan);
        System.out.println(zhucedizhi);
        System.out.println(jingyingfanwei);*/




    }

    private static String jsonString(String s){
        char[] temp = s.toCharArray();
        int n = temp.length;
        for(int i =0;i<n;i++){
            if(temp[i]==':'&&temp[i+1]=='"'){
                for(int j =i+2;j<n;j++){
                    if(temp[j]=='"'){
                        if(temp[j+1]!=',' &&  temp[j+1]!='}'){
                            temp[j]='”';
                        }else if(temp[j+1]==',' ||  temp[j+1]=='}'){
                            break ;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }

    public static void get(cangku c) throws IOException, InterruptedException {
        for(int x=1;x<=100;x++) {
            Document doc = Jsoup.connect("http://api.ip.data5u.com/dynamic/get.html?order=00b1c1dbec239455d92d87b98145951c&sep=3")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get();
            String ip = doc.body().toString().replace("<body>", "").replace("</body>", "").replace("\n", "").trim();
            c.fang(ip);
            Thread.sleep(1000);
        }
    }

    class cangku{
        BlockingQueue<String> pool=new LinkedBlockingQueue<String>();
        public void  fang(String fang) throws InterruptedException {
            pool.put(fang);
        }

        public  String qu() throws InterruptedException {
            return pool.take();
        }
    }



    public static String getString(Document doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getString(Element doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static String getHref(Document doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getHref(Element doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static Elements getElements(Document doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select,int a,String select2){
        Elements ele=null;
        try{
            ele=doc.select(select).get(a).select(select2);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

}
