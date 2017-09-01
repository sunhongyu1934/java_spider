
package test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/5.
 */
public class simutongtest {
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        get();

    }




    public static void get() throws IOException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
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

        String select="select * from company_shuzu where page=0";
        PreparedStatement ps=con.prepareStatement(select);
        ResultSet rs=ps.executeQuery();

        String insert="insert into company_name(`name`) values(?)";
        PreparedStatement psi=con.prepareStatement(insert);

        String update="update company_shuzu set page=? where id=?";
        PreparedStatement psu=con.prepareStatement(update);

        String coos = "JSESSIONID=591F150841DCBD268D2FEE599CB21D9B; firstEnterUrlInSession=http%3A//pe.pedata.cn/addUserInfoMember.action; VisitorCapacity=1; USER_LOGIN_ID=BD64CFB3-183C-4C21-90F7-8279CB90E714; USER_LOGIN_NAME_KEY=17704555458; IS_CS_KEY=true; USER_LOGIN_NAME=17704555458; USER_LOGIN_LANGUAGE=zh_CN; USER_CLIENT_ID=\"\"; pageReferrInSession=http%3A//pe.pedata.cn/addUserInfoMember.action; request_locale=zh_CN";
        String cooos[] = coos.split(";");
        Map map = new HashMap();
        for (int x = 0; x < cooos.length; x++) {
            map.put(cooos[x].split("=", 2)[0], cooos[x].split("=", 2)[1]);
        }

        System.out.println(map);

        int g=1;
        int b=1;
        while(rs.next()) {
            String id= String.valueOf(rs.getInt(rs.findColumn("id")));
            String industry= String.valueOf(rs.getInt(rs.findColumn("industry"))).replace("0","");
            String headquartersPlace= String.valueOf(rs.getInt(rs.findColumn("headquartersPlace"))).replace("0", "");
            String epRegistre= String.valueOf(rs.getInt(rs.findColumn("epRegistre"))).replace("0", "");
            String epOrganization= String.valueOf(rs.getInt(rs.findColumn("epOrganization"))).replace("0","");
            for(int i=1;i<=40;i++) {
                try {
                    Document doc = Jsoup.connect("http://pe.pedata.cn/getListEp.action")
                            .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.11 Safari/537.36")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .timeout(100000)
                            .data("param.quick", "")
                            .data("param.orderBy", "")
                            .data("param.orderByField", "")
                            .data("param.search_type", "base")
                            .data("param.isVcpe", "")
                            .data("param.isIpo", "")
                            .data("param.date_begin", "")
                            .data("param.date_end", "")
                            .data("param.report_type", "0")
                            .data("param.report_period", "2011-12-31")
                            .data("param.property_begin", "")
                            .data("param.property_end", "")
                            .data("param.income_begin", "")
                            .data("param.income_end", "")
                            .data("param.profit_begin", "")
                            .data("param.profit_end", "")
                            .data("param.assets_begin", "")
                            .data("param.assets_end", "")

                            .data("param.industry", industry)
                            .data("param.headquartersPlace", headquartersPlace)
                            .data("param.epRegistre", epRegistre)
                            .data("param.epOrganization", epOrganization)

                            .data("param.column", "0")
                            .data("param.column", "1")
                            .data("param.column", "4")
                            .data("param.column", "5")
                            .data("param.column", "6")
                            .data("param.column", "7")
                            .data("param.column", "8")
                            .data("param.column", "9")
                            .data("param.column", "10")
                            .data("param.column", "12")
                            .data("param.column", "13")
                            .data("param.column", "14")
                            .data("param.column", "15")
                            .data("param.column", "17")
                            .data("param.column", "18")
                            .data("param.currentPage", String.valueOf(i))
                            .header("Host", "pe.pedata.cn")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .header("Origin", "http://pe.pedata.cn/getListEp.action")
                            .cookies(map)
                            .post();

                    System.out.println(doc.outerHtml());
                    Elements elements = doc.select("div.leftTableDivID.float_left.search_width20 table.table.table-hover tbody tr");
                    String name=null;
                    for (Element ele : elements) {
                       name = ele.select("td a").text();
                        psi.setString(1, name);
                        psi.addBatch();
                        System.out.println(g);
                        g++;
                        System.out.println("*************************************");
                    }
                    if(StringUtils.isEmpty(name)){
                        psu.setInt(1, 1);
                        psu.setInt(2, Integer.parseInt(id));
                        psu.executeUpdate();
                        Thread.sleep(2000);
                        break;
                    }
                    psi.executeBatch();

                    psu.setInt(1, i);
                    psu.setInt(2, Integer.parseInt(id));
                    psu.executeUpdate();
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println(b+"  tiao");
            b++;
            System.out.println("----------------------------------");
        }
    }
}
