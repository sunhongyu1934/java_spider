package spark.streaming;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/26.
 */
public class JdbcUtils_DBCP {

    private static DataSource ds = null;

    static{
         try{
                 //加载dbcpconfig.properties配置文件
                 InputStream in = JdbcUtils_DBCP.class.getClassLoader().getResourceAsStream("spark/streaming/dbcpconfig.properties");
                 Properties prop = new Properties();
                 prop.load(in);
                 //创建数据源
                 ds = BasicDataSourceFactory.createDataSource(prop);
             }catch (Exception e) {
                 e.printStackTrace();
             }
        }





    public static Connection getConnection() throws SQLException {
                 //从数据源中获取数据库连接
                 return ds.getConnection();
    }

    public static void release(Connection conn,PreparedStatement st,ResultSet rs){
         if(rs!=null){
                 try{
                         //关闭存储查询结果的ResultSet对象
                         rs.close();
                     }catch (Exception e) {
                         e.printStackTrace();
                     }
                 rs = null;
             }
         if(st!=null){
                 try{
                         //关闭负责执行SQL命令的Statement对象
                         st.close();
                     }catch (Exception e) {
                         e.printStackTrace();
                     }
             }

         if(conn!=null){
                 try{
                         //将Connection连接对象还给数据库连接池
                         conn.close();
                     }catch (Exception e) {
                         e.printStackTrace();
                     }
             }
    }


    public static void release(Connection conn,PreparedStatement st){
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(conn!=null){
            try{
                //将Connection连接对象还给数据库连接池
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
