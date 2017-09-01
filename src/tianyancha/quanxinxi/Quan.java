/*
package tianyancha.quanxinxi;

import com.google.gson.*;
import gongshi.liebiaonei;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

*/
/**
 * Created by Administrator on 2017/4/12.
 *//*

public class Quan {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.44.60.141:3306/dw_online?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
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



        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        Quan q=new Quan();
        Lujing lu=q.new Lujing();
        Key k=q.new Key();
        Dulujing du=q.new Dulujing(lu);
        Jiexi ji=q.new Jiexi(lu,k,proxy);
        Duqu d=q.new Duqu(k,proxy,con);
        ExecutorService pool=Executors.newCachedThreadPool();
        pool.submit(du);
        pool.submit(ji);
        for(int x=1;x<=1;x++){
            pool.submit(d);
        }


    }







    class Dulujing implements Runnable{
        private Lujing lu;
        public Dulujing(Lujing lu){
            this.lu=lu;
        }
        @Override
        public void run() {

        }
    }


    class Jie implements  Runnable{
        private Cangku cang;
        private Json js;
        public Jie(Cangku cang,Json js){
            this.cang=cang;
            this.js=js;
        }
        @Override
        public void run() {


            while (true){
                try {
                    List<Object> list = cang.qu();
                    if(list==null){
                        break;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    class Jiexi implements Runnable{
        private Lujing lu;
        private Key k;
        private Proxy proxy;
        public Jiexi(Lujing lu,Key k,Proxy proxy){
            this.lu=lu;
            this.k=k;
            this.proxy=proxy;
        }
        @Override
        public void run() {

        }
    }





    class Cangku{
        BlockingQueue<List<Object>> cang=new LinkedBlockingQueue<List<Object>>();
        public void ru(List<Object> obj) throws InterruptedException {
            cang.put(obj);
        }
        public List<Object> qu() throws InterruptedException {
            return cang.poll(5,TimeUnit.SECONDS);
        }
    }



    class Key{
        BlockingQueue<String[]> key=new LinkedBlockingQueue<String[]>();
        public void fang(String[] str) throws InterruptedException {
            key.put(str);
        }
        public String[] na() throws InterruptedException {
            return key.poll(600, TimeUnit.SECONDS);
        }
    }

    class Duqu implements Runnable{
        private Key key;
        private Proxy proxy;
        private Connection con;
        public Duqu(Key key,Proxy proxy,Connection con){
            this.key=key;
            this.proxy=proxy;
            this.con=con;
        }
        @Override
        public void run() {
            while (true){
                try {
                    String[] str=key.na();
                    if(str==null){
                        Thread.sleep(1800000);
                        System.exit(0);
                    }
                    Json js=new Json();
                    Cangku ca=new Cangku();
                    ExecutorService pool= Executors.newCachedThreadPool();
                    js.jin("jichu", str[4]);
                    js.jin("pn",str[2]);
                    Jie ji=new Jie(ca,js);
                    Ruku ru=new Ruku(js,con);
                    Zhuyaorenyuan zhuyao=new Zhuyaorenyuan(proxy,ca,str);
                    Gudongxinxi guxin=new Gudongxinxi(proxy,ca,str);
                    Duiwaitouzi duiwai=new Duiwaitouzi(proxy,ca,str);
                    Biangeng bian=new Biangeng(proxy,ca,str);
                    Fenzhi fen=new Fenzhi(proxy,ca,str);
                    Rongzilishi rong=new Rongzilishi(proxy,ca,str);
                    Hexintuandui he=new Hexintuandui(proxy,ca,str);
                    Qiyeyewu qi=new Qiyeyewu(proxy,ca,str);
                    Touzishijian tou=new Touzishijian(proxy,ca,str);
                    Jingpinxinxi jing=new Jingpinxinxi(proxy,ca,str);
                    Falvsusong falv=new Falvsusong(proxy,ca,str);
                    Fayuangonggao fayuan=new Fayuangonggao(proxy,ca,str);
                    Beizhixingren bei=new Beizhixingren(proxy,ca,str);
                    Shixinren shi=new Shixinren(proxy,ca,str);
                    Jingyingyichang jingying=new Jingyingyichang(proxy,ca,str);
                    Xingzhengchufa xing=new Xingzhengchufa(proxy,ca,str);
                    Yanzhongweifa yan=new Yanzhongweifa(proxy,ca,str);
                    Guquanchuzhi gu=new Guquanchuzhi(proxy,ca,str);
                    Dongchandiya dong=new Dongchandiya(proxy,ca,str);
                    Qianshuigonggao qian=new Qianshuigonggao(proxy,ca,str);
                    Zhaotoubiao zhao=new Zhaotoubiao(proxy,ca,str);
                    Zhaiquanxinxi zhai=new Zhaiquanxinxi(proxy,ca,str);
                    Goudixinxi gou=new Goudixinxi(proxy,ca,str);
                    Zhaopin zhaopin=new Zhaopin(proxy,ca,str);
                    Shuiwupingji shui=new Shuiwupingji(proxy,ca,str);
                    Choucha chou=new Choucha(proxy,ca,str);
                    Chanpin chan=new Chanpin(proxy,ca,str);
                    Zizhizhengshu zizhi=new Zizhizhengshu(proxy,ca,str);
                    Shangbiaoxinxi shang=new Shangbiaoxinxi(proxy,ca,str);
                    Zhuanli zhuan=new Zhuanli(proxy,ca,str);
                    Zhuzuoquan zhu=new Zhuzuoquan(proxy,ca,str);
                    Wangzhanbeian wang=new Wangzhanbeian(proxy,ca,str);
                    pool.submit(duiwai);
                    pool.submit(bian);
                    pool.submit(fen);
                    pool.submit(rong);
                    pool.submit(he);
                    pool.submit(qi);
                    pool.submit(tou);
                    pool.submit(jing);
                    pool.submit(falv);
                    pool.submit(fayuan);
                    pool.submit(bei);
                    pool.submit(shi);
                    pool.submit(jingying);
                    pool.submit(xing);
                    pool.submit(yan);
                    pool.submit(gu);
                    pool.submit(dong);
                    pool.submit(qian);
                    pool.submit(zhao);
                    pool.submit(zhai);
                    pool.submit(gou);
                    pool.submit(zhaopin);
                    pool.submit(shui);
                    pool.submit(chou);
                    pool.submit(chan);
                    pool.submit(zizhi);
                    pool.submit(shang);
                    pool.submit(zhuan);
                    pool.submit(zhu);
                    pool.submit(wang);
                    pool.submit(zhuyao);
                    pool.submit(guxin);
                    pool.shutdown();
                    while (true){
                        Thread.sleep(500);
                        if(pool.isTerminated()){
                            Thread jt=new Thread(ji);
                            Thread th=new Thread(ru);
                            jt.start();
                            Jiankong jian=new Jiankong(jt,th);
                            Thread thread=new Thread(jian);
                            thread.start();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Jiankong implements Runnable{
        private Thread jt;
        private Thread th;
        public Jiankong(Thread jt,Thread th){
            this.jt=jt;
            this.th=th;
        }
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(5000);
                    if(!jt.isAlive()){
                        th.start();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Ruku implements Runnable{
        private Json js;
        private Connection con;
        public Ruku(Json js,Connection con){
            this.js=js;
            this.con=con;
        }
        @Override
        public void run() {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    class Duiwaitouzi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Duiwaitouzi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {

        }
    }

    class Zhuyaorenyuan implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhuyaorenyuan(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {

        }
    }


    class Gudongxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Gudongxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {

        }
    }


    class Biangeng implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Biangeng(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {

        }
    }

    class Fenzhi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Fenzhi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {

        }
    }

    class Rongzilishi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Rongzilishi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();

        }
    }

    class Hexintuandui implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Hexintuandui(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();

    }

    class Qiyeyewu implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Qiyeyewu(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();

        }
    }

    class Touzishijian implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Touzishijian(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Jingpinxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Jingpinxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Falvsusong implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Falvsusong(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Fayuangonggao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Fayuangonggao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Beizhixingren implements  Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Beizhixingren(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shixinren implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shixinren(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Jingyingyichang implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Jingyingyichang(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Xingzhengchufa implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Xingzhengchufa(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Yanzhongweifa implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Yanzhongweifa(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Guquanchuzhi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Guquanchuzhi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Dongchandiya implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Dongchandiya(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Qianshuigonggao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Qianshuigonggao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaotoubiao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaotoubiao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaiquanxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaiquanxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Goudixinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Goudixinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaopin implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaopin(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shuiwupingji implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shuiwupingji(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Choucha implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Choucha(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Chanpin implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Chanpin(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zizhizhengshu implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zizhizhengshu(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shangbiaoxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shangbiaoxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhuanli implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhuanli(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhuzuoquan implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhuzuoquan(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();

            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Wangzhanbeian implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Wangzhanbeian(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            try {

            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void get(Proxy proxy) throws InterruptedException, IOException {
        Gson gson=new Gson();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        File file=new File("C:\\Users\\Administrator\\Desktop\\xiaomi.txt");
        Document doc=Jsoup.parse(file,"xiaomi.txt","UTF-8");
        System.out.println();







        */
/*Elements eleduiwaitouzi=getElements(doc,"div[ng-if=dataItemCount.inverstCount>0] table.table.companyInfo-table tbody tr.ng-scope");
        if(eleduiwaitouzi!=null){
            for(Element e:eleduiwaitouzi){
                String beitouziqiyeming=getString(e,"td span.ng-binding",0);
                String beitouzifading=getString(e,"td span.ng-binding",1);
                String zhuceziben=getString(e,"td span.ng-binding",2);
                String touzishue=getString(e,"td span.ng-binding",3);
                String touzizhanbi=getString(e,"td span.ng-binding",4);
                String zhuceshijian=getString(e,"td span.ng-binding",5);
                String zhuangtai=getString(e,"td span.ng-binding",6);
                System.out.println(beitouziqiyeming+"   "+beitouzifading+"    "+zhuceziben+"    "+touzishue+"   "+touzizhanbi+"   "+zhuceshijian+"   "+zhuangtai);
            }
        }*//*




        */
/*Elements elebiangeng=getElements(doc,"div[ng-if=items2.changeCount.show&&dataItemCount.changeCount>0] table.table.companyInfo-table tbody tr.ng-scope");
        if(elebiangeng!=null){
            for(Element e:elebiangeng){
                String biangengshijian=getString(e,"td:nth-child(1) div.ng-binding",0);
                String biangengxiangmu=getString(e,"td:nth-child(2) div.textJustFy.changeHoverText.ng-binding",0);
                String biangengqian=getString(e,"td:nth-child(3) div.ng-binding",0);
                String biangenghou=getString(e,"td:nth-child(4) div.textJustFy.changeHoverText.ng-binding",0);
                System.out.println(biangengshijian+"    "+biangengxiangmu+"    "+biangengqian+"    "+biangenghou);
            }
        }*//*














    }




    public static String getString(Document doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getString(Element doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static String getHref(Document doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getHref(Element doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static Elements getElements(Document doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select,int a,String select2){
        Elements ele=null;
        try{
            ele=doc.select(select).get(a).select(select2);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }
}
*/
