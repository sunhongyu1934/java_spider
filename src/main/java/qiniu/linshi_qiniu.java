package qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/9/9.
 */
public class linshi_qiniu {
    public static final String ACCESS_KEY = "Wbw_tA3cCzYqakUdU5Xqf-NBbnk80nWMbH4yGr0B"; // 你的access_key
    public static final String SECRET_KEY = "n9ahK41uBFqdKoM-n8EcnwCPJVBOFvKhlnml9qu9"; // 你的secret_key
    public static final String BUCKET_NAME = "innotreelogo"; // 你的secret_key
    private static final String BUCKET_HOST_NAME = "innotreelogo.qiniu.innotree.cn";
    public static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static Connection conn;

    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://47.95.31.183:3306/innotree_data_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="test";
        String password="123456";
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
    public static void main(String args[]) throws QiniuException, SQLException, InterruptedException {
        linshi_qiniu l=new linshi_qiniu();
        final Ca c=l.new Ca();
        /*final String path1=args[0];
        final String path2=args[1];
        String xa=args[2];
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(c,path1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int x=1;x<=Integer.parseInt(xa);x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        shang(c,path2);
                    } catch (QiniuException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
        c.fang(new File("C:\\Users\\13434\\Desktop\\1.png"));
        shang(c,"");
    }

    public static void data(Ca c,String path) throws InterruptedException, SQLException {
/*        String sql="select c_id from linshi_logo2";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<String> list=new ArrayList<String>();
        while (rs.next()){
            list.add(rs.getString(rs.findColumn("c_id")));
        }*/
        while (true) {
            File file = new File(path);
            File[] ff = file.listFiles();
            for (File f : ff) {
                if (f.isFile()&&f.exists()) {
                    c.fang(f);
                }
            }
            while (true) {
                Thread.sleep(10000);
                if(c.po.size()==0){
                    break;
                }
            }
        }
    }

    public static void shang(Ca c,String path) throws QiniuException, SQLException, InterruptedException {
        String sql="insert into logo_qiniu(only_id,qiniu_url) values(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        UploadManager uploadManager = new UploadManager();
        int a=0;
        while (true) {
            try {
                File f= c.qu();
                if (f == null) {
                    break;
                }
                String newUrl = null;
                System.out.println("begin");
                Response res = uploadManager.put(f.getPath(), null, auth.uploadToken(BUCKET_NAME));
                String json = res.bodyString();
                System.out.println(json);
                Gson gson = new Gson();
                qijson q = gson.fromJson(json, qijson.class);
                newUrl = "https://" + BUCKET_HOST_NAME + "/" + q.key;
                System.out.println(newUrl);

                ps.setString(1, f.getName().replace(".png", ""));
                ps.setString(2, newUrl);
                //ps.executeUpdate();
                //renameto(f,path+f.getName());
                a++;
                System.out.println(a + "*****************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }

    }

    public static void renameto(File file,String newpath) throws IOException {
        FileInputStream ins = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(newpath);
        byte[] b = new byte[1024];
        int n=0;
        while((n=ins.read(b))!=-1){
            out.write(b, 0, n);
        }
        file.delete();
        ins.close();
        out.close();
    }

    class Ca{
        BlockingQueue<File> po=new LinkedBlockingDeque<File>(50);
        public void fang(File key) throws InterruptedException {
            po.put(key);
        }
        public File qu() throws InterruptedException {
            return po.take();
        }
    }


}
