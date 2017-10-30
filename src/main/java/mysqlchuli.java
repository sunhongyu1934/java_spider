import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/10.
 */
public class mysqlchuli {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/coolchuan_tmp?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        String select="select * from app_download_thirty";
        PreparedStatement ps=con.prepareStatement(select);
        String insert="insert into app_download_day(pid,`name`,`day`,download) values(?,?,?,?)";
        PreparedStatement ps2=con.prepareStatement(insert);

        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            String tians=rs.getString(rs.findColumn("app_download"));
            String pid=rs.getString(rs.findColumn("pid"));
            String name=rs.getString(rs.findColumn("name"));
            String tianq[]=tians.split(",");
            for(int x=1;x<tianq.length+1;x++){
               ps2.setString(1,pid);
                ps2.setString(2,name);
                ps2.setString(3, String.valueOf(x));
                ps2.setString(4,tianq[x-1]);
                ps2.executeUpdate();
                System.out.println(x);
            }
        }



    }
}
