package dawei;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/10.
 */
public class daweibaidu {
    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException, InstantiationException, InterruptedException, IllegalAccessException {

        get("%E5%B0%8F%E7%B1%B3");



    }

    public static String get(String sous) throws IOException {
        String sou= URLEncoder.encode(sous,"UTF-8");
        Document doc= Jsoup.connect("http://fanyi.baidu.com/v2transapi")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .timeout(100000)
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .data("from","en")
                .data("to","zh")
                .data("query",sou)
                .data("transtype","translang")
                .data("simple_means_flag","3")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .post();
        String str=doc.outerHtml();
        System.out.println(str);
        Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
        Matcher mat= pattern.matcher(str);


        while(mat.find()){
            StringBuffer string = new StringBuffer();
            int data = Integer.parseInt(mat.group(0).replace("\\u",""), 16);
            string.append((char) data);
            str=str.replace(mat.group(0),string.toString());
        }
        Gson gson=new Gson();
        String json=str.replace("<html>","").replace("</html>","").replace("<head></head>","").replace("<body>","").replace("</body>","").trim();
        daweibean d=gson.fromJson(json,daweibean.class);
        String da=d.trans_result.data.get(0).dst.replace("+", "");
        return da;
    }

    public static void data() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, InterruptedException {
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

        String select="select id,assignee from com_en_cn where cname='1' or cname is NULL";
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
                String na = get(assignee);
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
}
