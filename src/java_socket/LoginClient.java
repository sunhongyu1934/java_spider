package java_socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/5/15.
 */
public class LoginClient {
    public static void main(String args[]) throws InterruptedException {
        client("aaaaghsag阿萨德gg","aaa");
    }

    public static String client(String key,String key2) throws InterruptedException {
        String re=null;
        try {
            //1.建立客户端socket连接，指定服务器位置及端口
            Socket socket =new Socket("123.57.217.48",8800);
            //2.得到socket读写流
            OutputStream os=socket.getOutputStream();
            OutputStreamWriter out=new OutputStreamWriter(os,"UTF-8");
            PrintWriter pw=new PrintWriter(out);
            //3.利用流按照一定的操作，对socket进行读写操作
            String info=key;
            pw.write(info+"\r\n"+key2);
            pw.flush();
            socket.shutdownOutput();
            //输入流
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            //接收服务器的相应
            String reply=null;
            while(!((reply=br.readLine())==null)){
                re=reply;
            }
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re;
    }

}
