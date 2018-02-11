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
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
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
            break;
        }
    }

    public static void shang(Ca c,String path) throws QiniuException, SQLException, InterruptedException {
        String sql0="update dw_dim_online.company_base_info_copy0 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps0=conn.prepareStatement(sql0);

        String sql1="update dw_dim_online.company_base_info_copy1 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps1=conn.prepareStatement(sql1);

        String sql2="update dw_dim_online.company_base_info_copy2 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        String sql3="update dw_dim_online.company_base_info_copy3 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps3=conn.prepareStatement(sql3);

        String sql4="update dw_dim_online.company_base_info_copy4 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps4=conn.prepareStatement(sql4);

        String sql5="update dw_dim_online.company_base_info_copy5 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps5=conn.prepareStatement(sql5);

        String sql6="update dw_dim_online.company_base_info_copy6 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps6=conn.prepareStatement(sql6);

        String sql7="update dw_dim_online.company_base_info_copy7 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps7=conn.prepareStatement(sql7);

        String sql8="update dw_dim_online.company_base_info_copy8 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps8=conn.prepareStatement(sql8);

        String sql9="update dw_dim_online.company_base_info_copy9 set comp_logo_url=? where comp_id=? and (logo_source not like '%人工%' or logo_source='' or logo_source is null)";
        PreparedStatement ps9=conn.prepareStatement(sql9);

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

                String comp_id=f.getName().replace(".png", "");

                if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==0) {
                    ps0.setString(1, newUrl);
                    ps0.setString(2, comp_id);
                    ps0.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==1){
                    ps1.setString(1, newUrl);
                    ps1.setString(2, comp_id);
                    ps1.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==2){
                    ps2.setString(1, newUrl);
                    ps2.setString(2, comp_id);
                    ps2.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==3){
                    ps3.setString(1, newUrl);
                    ps3.setString(2, comp_id);
                    ps3.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==4){
                    ps4.setString(1, newUrl);
                    ps4.setString(2, comp_id);
                    ps4.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==5){
                    ps5.setString(1, newUrl);
                    ps5.setString(2, comp_id);
                    ps5.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==6){
                    ps6.setString(1, newUrl);
                    ps6.setString(2, comp_id);
                    ps6.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==7){
                    ps7.setString(1, newUrl);
                    ps7.setString(2, comp_id);
                    ps7.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==8){
                    ps8.setString(1, newUrl);
                    ps8.setString(2, comp_id);
                    ps8.executeUpdate();
                }else if(Integer.parseInt(comp_id.substring(comp_id.length()-1))==9){
                    ps9.setString(1, newUrl);
                    ps9.setString(2, comp_id);
                    ps9.executeUpdate();
                }
                renameto(f,path+f.getName());
                a++;
                System.out.println(a + "*****************************************************");
            }catch (Exception e){
                e.printStackTrace();
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

    public static void linshang(String path) throws QiniuException {
        UploadManager uploadManager = new UploadManager();
        String newUrl = null;
        System.out.println("begin");
        Response res = uploadManager.put(path, null, auth.uploadToken(BUCKET_NAME));
        String json = res.bodyString();
        System.out.println(json);
        Gson gson = new Gson();
        qijson q = gson.fromJson(json, qijson.class);
        newUrl = "https://" + BUCKET_HOST_NAME + "/" + q.key;
        System.out.println(newUrl);
    }

    class Ca{
        BlockingQueue<File> po=new LinkedBlockingDeque<File>();
        public void fang(File key) throws InterruptedException {
            po.put(key);
        }
        public File qu() throws InterruptedException {
            return po.poll(10,TimeUnit.SECONDS);
        }
    }


}
