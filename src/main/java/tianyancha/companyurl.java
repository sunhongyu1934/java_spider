package tianyancha;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import spiderKc.kcBean.Count;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */
public class companyurl {
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        get();
    }


    public static void get() throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/company?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String sql="insert into tyc_url(url) values(?)";
        PreparedStatement ps=con.prepareStatement(sql);





        int f=0;

        System.setProperty(Count.chrome,Count.chromepath);
        WebDriver driver=new ChromeDriver();
        driver.get("http://www.tianyancha.com/");
        Thread.sleep(10000);
        Document doc=Jsoup.parse(driver.getPageSource());
        driver.quit();
        String ziben[]=new String[]{"","&moneyStart=0&moneyEnd=100","&moneyStart=100&moneyEnd=200","&moneyStart=200&moneyEnd=500","&moneyStart=500&moneyEnd=1000","&moneyStart=1000&moneyEnd=200000"};
        String sj[]=new String[]{"","&estiblishTimeStart=1460101352136&estiblishTimeEnd=1491637352136","&estiblishTimeStart=1333871053533&estiblishTimeEnd=1460101453533","&estiblishTimeStart=1176018289088&estiblishTimeEnd=1333871089088","&estiblishTimeStart=1018251919895&estiblishTimeEnd=1176018319895","&estiblishTimeStart=-4819796050023&estiblishTimeEnd=1018251949977"};
        String zt[]=new String[]{"","&regStatus=%E5%9C%A8%E4%B8%9A","&regStatus=%E5%AD%98%E7%BB%AD","&regStatus=%E5%90%8A%E9%94%80","&regStatus=%E6%B3%A8%E9%94%80","&regStatus=%E8%BF%81%E5%87%BA"};
        List<String> oc=new ArrayList();
        Map<String,String> map=new HashMap();

        Elements links=doc.select("div.industry_box.ng-scope a.industry_name.a3.ng-binding");
        for(Element ele:links){
            String oc1=ele.attr("ng-href");
            oc.add(oc1);
        }
        Elements links2=doc.select("div.industry_box.ng-scope ul li a.c3");
        for(Element ele2:links2){
            String oc2=ele2.attr("href");
            oc.add(oc2);
        }
        Elements l=doc.select("div.bace.ng-scope");
        for(Element ele:l){
            Elements ee=ele.select("a.f13.c3");
            String k=ele.select("a.f15.c18").attr("ng-href");
            String d=ele.attr("ng-href");
            for(Element e:ee){
                map.put(e.attr("ng-href")+","+e.text().replace("• ",""),k);
            }
            map.put(k+","+ele.select("a.f15.c18").text().replace("• ", ""),k);
        }
        for(int x=0;x<oc.size();x++){
            for(Map.Entry<String, String> entry : map.entrySet()){
                for(int a=0;a<ziben.length;a++){
                    for(int b=0;b<sj.length;b++){
                        for(int c=0;c<zt.length;c++){
                            try {
                                String o = oc.get(x).replace("http://www.tianyancha.com/search/oc", "");
                                String d = entry.getKey().toString().split(",", 2)[0].replace("http://", "").replace(".tianyancha.com/search", "");
                                String city = URLEncoder.encode(entry.getKey().toString().split(",", 2)[1], "UTF-8");
                                String k = entry.getValue().toString().replace("http://", "").replace(".tianyancha.com/search", "");
                                String url = null;
                                if (!d.equals(k)) {
                                    url = "http://" + d + ".tianyancha.com/v2/search/%22%24%22.json?&base=" + k + "&city=" + city + "&type=tail&category=" + o + ziben[a] + sj[b] + zt[c];
                                } else {
                                    url = "http://" + d + ".tianyancha.com/v2/search/%22%24%22.json?&base=" + k + "&type=tail&category=" + o + ziben[a] + sj[b] + zt[c];
                                }
                                ps.setString(1, url);
                                ps.addBatch();
                                f++;
                                if (f % 96 == 0) {
                                    ps.executeBatch();
                                    System.out.println(f);
                                    System.out.println("-------------------------------------------------------------");
                                }
                            }catch (Exception e){
                                System.out.println("error");
                            }
                        }
                    }
                }
            }
        }
    }


}
