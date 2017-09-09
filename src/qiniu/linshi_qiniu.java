package qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/9/9.
 */
public class linshi_qiniu {
    public static final String ACCESS_KEY = "Wbw_tA3cCzYqakUdU5Xqf-NBbnk80nWMbH4yGr0B"; // 你的access_key
    public static final String SECRET_KEY = "n9ahK41uBFqdKoM-n8EcnwCPJVBOFvKhlnml9qu9"; // 你的secret_key
    public static final String BUCKET_NAME = "innotreelogo"; // 你的secret_key
    private static final String BUCKET_HOST_NAME = "innotreelogo.qiniu.innotree.cn";
    public static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    public static final BucketManager bucketManager = new BucketManager(auth);
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

        conn=con;

    }
    public static void main(String args[]) throws QiniuException, SQLException {
        shang();
    }

    public static void shang() throws QiniuException, SQLException {
        UploadManager uploadManager = new UploadManager();
        String sql="insert into linshi_logo(c_id,c_logo) values(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        File file=new File("/home/etl_user/etl/logo_new");
        File[] ff=file.listFiles();
        int j=0;
        for(File f:ff) {
            if(f.isFile()) {
                String newUrl = null;
                Response res = uploadManager.put(f.getPath(), null, auth.uploadToken(BUCKET_NAME));
                String json = res.bodyString();
                Gson gson = new Gson();
                qijson q = gson.fromJson(json, qijson.class);
                newUrl = "https://" + BUCKET_HOST_NAME + "/" + q.key;

                ps.setString(1,f.getName().replace(".png",""));
                ps.setString(2,newUrl);
                ps.executeUpdate();
                j++;
                System.out.println(j+"*****************************************************");
            }
        }
    }


}
