package test;

import Dw.Dw;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import spiderKc.kcBean.Count;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/1.
 */
public class daweixin {
    public static void main(String args[]) throws InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));



        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);
        CloseableHttpClient httpclient = builder.build();
        data(httpclient);

    }


    public static String get(String str1) throws InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{"--proxy-type=http","--proxy=proxy.abuyun.com:9020","--proxy-auth=H7748N598W005E8D:A242740AED77F7E4"});
        caps.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        System.setProperty(Count.phantomjs,Count.phantomjspath);
        WebDriver driver=new PhantomJSDriver(caps);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        try {
            driver.get("http://www.innojoy.com/search/index.html");
        }catch (Exception e){
            driver.quit();
            while(true){
                int flag=0;
                System.setProperty(Count.phantomjs,Count.phantomjspath);
                driver=new PhantomJSDriver();
                driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
                try {
                    driver.get("http://www.innojoy.com/search/index.html");
                }catch (Exception e1){
                    driver.quit();
                    flag=1;
                }
                if(flag==0){
                    break;
                }
            }
        }
        driver.findElement(By.id("checkall")).click();
        driver.findElement(By.id("queryExpr-str")).clear();
        driver.findElement(By.id("queryExpr-str")).sendKeys(str1);
        Thread.sleep(500);
        driver.findElement(By.id("btnSearch")).click();
        while (true){
            Thread.sleep(1000);
            if(driver.getPageSource().length()>260000){
                break;
            }
        }
        String cname=null;
        for(int x=1;x<=9;x++) {
            Document doc = Jsoup.parse(driver.getPageSource());
            String str2 = doc.select("#idContainerSimple > table > tbody > tr:nth-child(9) > td > span").text();
            if(str2.contains(";")){
                str2=str2.split(";",2)[1].trim();
                if(str1.length()>str2.length()) {
                    if (SimilarDegree(str1.toUpperCase(),str2 ) >= 0.7) {
                        cname=doc.select("#idContainerSimple > table > tbody > tr:nth-child(9) > td > span > a:nth-child(1)").text();
                        break;
                    }
                }else{
                    if (SimilarDegree(str2,str1.toUpperCase() ) >= 0.7) {
                        cname=doc.select("#idContainerSimple > table > tbody > tr:nth-child(9) > td > span > a:nth-child(1)").text();
                        break;
                    }
                }
            }
            Actions action = new Actions(driver);
            action.click(driver.findElement(By.xpath("//*[@id='idItem" + x + "']/div/div[3]/span[1]"))).perform();
            driver.findElement(By.xpath("//*[@id='idItem" + x + "']/div/div[3]/span[1]")).click();
            Thread.sleep(500);
        }
        driver.quit();
        return cname;
    }

    public static String qq(String name,CloseableHttpClient httpclient) throws IOException {

        String json = "{\"requestModule\":\"PatentSearch\",\"userId\":\"\",\"patentSearchConfig\":{\"Query\":\"" + name + "\",\"TreeQuery\":\"\",\"Database\":\"nzpat,aupat,ttpat,dopat,copat,crpat,svpat,nipat,hnpat,gtpat,ecpat,papat,arpat,mxpat,capat,cupat,pepat,clpat,brpat,appat,oapat,zwpat,dzpat,tnpat,zmpat,mwpat,mapat,kepat,egpat,zapat,gcpat,uzpat,kzpat,tjpat,kgpat,idpat,mypat,ampat,trpat,phpat,sgpat,ilpat,mnpat,thpat,vnpat,jopat,inpat,inapp,bapat,ddpat,cspat,eapat,mepat,sipat,bypat,bgpat,cypat,eepat,gepat,hrpat,lvpat,mdpat,rspat,ropat,smpat,skpat,yupat,supat,ltpat,lupat,mcpat,mtpat,ptpat,atpat,uypat,uapat,espat,itpat,hupat,bepat,iepat,sepat,plpat,nlpat,fipat,nopat,czpat,grpat,dkpat,ispat,chpat,frpat,deuti,depat,deapp,kruti,krpat,krapp,jputi,jppat,jpapp,wopat,usdes,uspat,uspat1,usapp,gbpat,eppat,epapp,rupat,hkpat,twpat,fmsq,wgzl,syxx,fmzl\",\"Action\":\"Search\",\"DBOnly\":0,\"Page\":\"" + "1" + "\",\"PageSize\":\"10\",\"GUID\":\"4409d92326704d86b6ba3619128f4531\",\"Sortby\":\"-IDX,PNM\",\"AddOnes\":\"\",\"DelOnes\":\"\",\"RemoveOnes\":\"\",\"Verification\":null,\"SmartSearch\":\"\",\"TrsField\":\"\"}}";
        HttpPost post = new HttpPost("http://www.innojoy.com/client/interface.aspx");
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");
        post.setEntity(entity);
        post.addHeader("Host", "www.innojoy.com");
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        post.addHeader("Accept-Language", "en-us");
        post.addHeader("Origin", "http://www.innojoy.com");
        post.addHeader("Referer", "http://www.innojoy.com/search/tablesearch.html");


        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity resEntity = response.getEntity();
        String sj= EntityUtils.toString(resEntity);
        System.out.println(sj);
        Gson gson = new Gson();
        Dw dw = gson.fromJson(sj, Dw.class);
        String na=null;
        for (int x = 0; x < dw.Option.PatentList.size(); x++) {
            String pa = dw.Option.PatentList.get(x).PA;
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(pa);
            if (m.find()) {
                String en=pa.split(";")[1].replace("<font color=red>","").replace("</font>","");
                if(name.length()>en.length()) {
                    if (SimilarDegree(name.toUpperCase(),en ) >= 0.7) {
                        na=pa.split(";")[0];
                        break;
                    }
                }else{
                    if (SimilarDegree(en,name.toUpperCase() ) >= 0.7) {
                        na=pa.split(";")[0];
                        break;
                    }
                }
            }
        }
        return na;
    }



    public static void data(CloseableHttpClient httpclient) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
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

        String select="select id,assignee from com_en_cn where id>'456' and id<'546'";
        String update="update com_en_cn set cname=? where id=?";

        PreparedStatement ps1=con.prepareStatement(select);
        PreparedStatement ps2=con.prepareStatement(update);

        ResultSet rs=ps1.executeQuery();
        int a=1;
        while(rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            try {
                String assignee = rs.getString(rs.findColumn("assignee")).replace("(", "").replace(")", "").replace(".", "");
                if (assignee.contains(",")) {
                    assignee = assignee.split(",")[0];
                }
                String na = qq(assignee, httpclient);
                ps2.setString(1, na);
                ps2.setString(2, id);
                ps2.executeUpdate();
            }catch (Exception e){
                ps2.setString(1, "1");
                ps2.setString(2, id);
                ps2.executeUpdate();
            }
            System.out.println("当前第：" + a + "   条");
            a++;
            System.out.println("--------------------------------------------------");
        }



    }






    public static double SimilarDegree(String strA, String strB){

        String newStrA = removeSign(strA);

        String newStrB = removeSign(strB);

        int temp = Math.max(newStrA.length(), newStrB.length());

        int temp2 = longestCommonSubstring(newStrA, newStrB).length();

        return temp2 * 1.0 / temp;

    }



    /**

     * 相似度转百分比

     */

    public static String similarityResult(double resule){

        return  NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(resule);

    }

    private static String removeSign(String str) {

        StringBuffer sb = new StringBuffer();

        for (char item : str.toCharArray())

            if (charReg(item)){

                //System.out.println("--"+item);

                sb.append(item);

            }

        return sb.toString();

    }



    private static boolean charReg(char charValue) {

        return (charValue >= 0x4E00 && charValue <= 0X9FA5)

                || (charValue >= 'a' && charValue <= 'z')

                || (charValue >= 'A' && charValue <= 'Z')

                || (charValue >= '0' && charValue <= '9');

    }



    private static String longestCommonSubstring(String strA, String strB) {

        char[] chars_strA = strA.toCharArray();

        char[] chars_strB = strB.toCharArray();

        int m = chars_strA.length;

        int n = chars_strB.length;

        int[][] matrix = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (chars_strA[i - 1] == chars_strB[j - 1])

                    matrix[i][j] = matrix[i - 1][j - 1] + 1;

                else

                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);

            }

        }

        char[] result = new char[matrix[m][n]];

        int currentIndex = result.length - 1;

        while (matrix[m][n] != 0) {
            if (matrix[n] == matrix[n - 1])

                n--;

            else if (matrix[m][n] == matrix[m - 1][n])

                m--;

            else {

                result[currentIndex] = chars_strA[m - 1];

                currentIndex--;

                n--;

                m--;

            }
        }

        return new String(result);

    }
}
