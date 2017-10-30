package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/20.
 */
public class test6 {
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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

        String insert="insert into lianjia_list_two_pailie(url) values(?)";
        PreparedStatement ps=con.prepareStatement(insert);












        int q=1;
        String city2[]=new String[]{"http://sh.lianjia.com/ershoufang","http://su.lianjia.com/ershoufang",""};

        String city[]=new String[]{"http://bj.lianjia.com/ershoufang/","http://nj.lianjia.com/ershoufang/","http://cd.lianjia.com/ershoufang/","http://cq.lianjia.com/ershoufang/","http://cs.lianjia.com/ershoufang/","http://qd.lianjia.com/ershoufang/","http://dl.lianjia.com/ershoufang/","http://dg.lianjia.com/ershoufang/","http://sz.lianjia.com/ershoufang/","http://fs.lianjia.com/ershoufang/","http://tj.lianjia.com/ershoufang/","http://gz.lianjia.com/ershoufang/","http://wh.lianjia.com/ershoufang/","http://hz.lianjia.com/ershoufang/","http://hf.lianjia.com/ershoufang/","http://xm.lianjia.com/ershoufang/","http://jn.lianjia.com/ershoufang/","http://yt.lianjia.com/ershoufang/","http://bj.lianjia.com/ershoufang/yanjiao/","http://zs.lianjia.com/ershoufang/","http://zh.lianjia.com/ershoufang/"};

       /* for(int x=0;x<city.length;x++) {
            Document doc=Jsoup.connect(city[x])
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .timeout(1000000)
                    .get();
            Elements ele2 = doc.select("div.list-more dl").get(1).select("a");
            Elements ele = doc.select("div.list-more dl").get(0).select("a");
            for (Element e : ele) {
                for (Element e2 : ele2) {
                    for(int i=1;i<=100;i++) {
                        String url = city[x].replace("/ershoufang/","")+e2.attr("href").replace(e2.attr("href").split("/", 3)[2], "/pg" + i + e2.attr("href").split("/", 3)[2] + e.attr("href").split("/", 3)[2]).replace("/p", "p");
                        ps.setString(1,url);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    System.out.println(q);
                    q++;
                    System.out.println("---------------------------------------------");
                }
            }
        }*/



        for(int y=0;y<city2.length;y++) {
            Document doc = Jsoup.connect(city2[y])
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .timeout(1000000)
                    .get();
            Elements elements = doc.select("div.option-list.gio_district div.item-list a");
            Elements elements1 = doc.select("div.option-list.multiChk").get(0).select("div.item-list a");
            Elements elements2 = doc.select("div.option-list.multiChk").get(1).select("div.item-list a");
            for (Element element : elements) {
                for (Element element1 : elements2) {
                        for (Element element3 : elements1) {
                            for (int i = 1; i <= 100; i++) {
                                String url=city2[y].replace("/ershoufang","")+element.attr("href") + "/" + element1.attr("href").split("/", 3)[2] + "d" + i + element3.attr("href").split("/", 3)[2];
                                ps.setString(1,url);
                                ps.addBatch();
                            }
                            ps.executeBatch();
                            System.out.println(q);
                            q++;
                            System.out.println("---------------------------------------------");
                        }

                }
            }
        }






        /*Elements ele=doc.select("div[data-role=ershoufang] a");
        for(Element e:ele){
            for(int i=1;i<=100;i++) {
                String url = e.attr("href") + "pg"+i+"/";
                ps.setString(1, url);
                ps.addBatch();
            }
            ps.executeBatch();
        }

        Elements ele2=doc.select("div[data-role=ditiefang] a");
        for(Element e:ele2){
            for(int i=1;i<=100;i++) {
                String url = e.attr("href") + "pg"+i+"/";
                ps.setString(1, url);
                ps.addBatch();
            }
            ps.executeBatch();
        }

        Elements ele3=doc.select("div.list-more dl dd a");
        for(Element e:ele3){
            for(int i=1;i<=100;i++) {
                String url = e.attr("href").replace(e.attr("href").split("/", 3)[2], "pg"+i + e.attr("href").split("/", 3)[2]);
                ps.setString(1, url);
                ps.addBatch();
            }
            ps.executeBatch();
        }*/
    }
}
