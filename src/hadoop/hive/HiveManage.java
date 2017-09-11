package hadoop.hive;


import java.sql.*;

/**
 * Created by Administrator on 2017/9/11.
 */
public class HiveManage {
    private static final String URLHIVE = "jdbc:hive2://10.44.51.90:10000/test";
    private static Connection connection = null;
    public static void main(String args[]) throws SQLException {

//      String sql = "select ipaddress,count(ipaddress) as count from apachelog "
//              + "group by ipaddress order by count desc";
        String sql1="select * from tt";
        PreparedStatement pstm = getHiveConnection().prepareStatement(sql1);
        ResultSet rs= pstm.executeQuery(sql1);

        while (rs.next()) {
            System.out.println(rs.getString(1)+"    "+rs.getString(2));
        }
        pstm.close();
        rs.close();

    }
    public static Connection getHiveConnection() {
        if (null == connection) {
            synchronized (HiveManage.class) {
                if (null == connection) {
                    try {
                        Class.forName("org.apache.hive.jdbc.HiveDriver");
                        connection = DriverManager.getConnection(URLHIVE, "etl", "innotree_etl");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }
}
