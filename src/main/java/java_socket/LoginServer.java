package java_socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/15.
 */
public class LoginServer {
    public static void main(String[] args) {
        try {
            //1.建立一个服务器Socket(ServerSocket)绑定指定端口
            ServerSocket serverSocket=new ServerSocket(8800);
            ExecutorService pool= Executors.newCachedThreadPool();
            LoginServer ll=new LoginServer();
            while (true){
                //2.使用accept()方法阻止等待监听，获得新连接
                Socket socket=serverSocket.accept();
                //3.获得输入流
                InputStream is=socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                //4.读取用户输入信息
                String info = null;
                String pn = null;
                StringBuffer str = new StringBuffer();
                while (!((info = br.readLine()) == null)) {
                    pn = info;
                    str.append(pn + "\r\n");
                }
                xie x=ll.new xie(str,pn);
                pool.submit(x);
                //获得输出流
                OutputStream os=socket.getOutputStream();
                OutputStreamWriter out=new OutputStreamWriter(os,"UTF-8");
                PrintWriter pw=new PrintWriter(out);
                //给客户一个响应
                String reply="success";
                pw.write(reply);
                pw.flush();
                //5.关闭资源
                pw.close();
                os.close();
                is.close();
                br.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class xie implements Runnable{
        private StringBuffer str;
        private String pn;
        public xie(StringBuffer str,String pn){
            this.str=str;
            this.pn=pn;
        }

        @Override
        public void run() {
            try {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String time = simpleDateFormat.format(date);
                FileOutputStream ff = new FileOutputStream("/data1/spider/java_spider/tyc/" + pn + "_" + time);
                OutputStreamWriter out=new OutputStreamWriter(ff,"UTF-8");
                System.out.println("begein xieru and pn:"+pn);
                out.write(str.toString());
                out.flush();
                ff.close();
                out.close();


                System.out.println("xieru success and pn:"+pn);
                System.out.println("---------------------------------------------------------");
            }catch (Exception e){

            }
        }
    }

}
