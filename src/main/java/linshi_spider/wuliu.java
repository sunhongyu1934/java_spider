package linshi_spider;


import Utils.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class wuliu {
    // 代理隧道验证信息
    final static String ProxyUser = "HP5G1I415085Y7AD";
    final static String ProxyPass = "9CDAD2529F99DC54";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static Connection conn;
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.44:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        conn=con;

    }

    public static void main(String args[]) throws IOException {
        wuliu w=new wuliu();
        Da d=w.new Da();
        Ca c=w.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        String[] str=new String[]{"物流服务###wuliufuwu###3191","包装机械###baozhuangjixie###2011","其他物流设备###qitawuliushebei###2018","其它搬运车辆###qitabanyuncheliang###2017","货架###huojia2016","工业门###gongyemen2015","物流整理设备###wuliuzhenglishebei2021","叉车###chache2012","起重设备###qizhongshebei2019","自动化立体仓库###zidonghualiticangku2022","二手物流设备###ershouwuliushebei2013","输送机###shusongji2020","分拣设备###fenjianshebei2014"};
        for(String s:str){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(s.split("###",3)[0],s.split("###",3)[1],d,s.split("###",3)[2]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /*for(int y=1;y<=10;y++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data("物流服务",d,c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/


        for(int x=1;x<=10;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail(d);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    JsoupUtils.getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    JsoupUtils.conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        /*pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    da(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    public static void da(Ca c) throws InterruptedException {
        for(int a=1;a<=128;a++){
            c.fang(String.valueOf(a));
        }
    }

    public static void data(String type,String zi,Da d,String ma) throws IOException, InterruptedException {
        for (int a=1;a<=1000;a++){
            try {
                System.out.println(a+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                Document doc = JsoupUtils.get("http://www.6-china.com/company/"+ma+"p" + a + "/"+zi+".html", proxy);
                Elements ele = JsoupUtils.getElements(doc, "div.conD.companyList ul");
                boolean bo=true;
                if (ele != null) {
                    for (Element e : ele) {
                        bo=false;
                        String url = JsoupUtils.getHref(e, "a", "href", 0);
                        d.fang(new String[]{url, type});
                    }
                }
                if(bo){
                    break;
                }
            }catch (Exception e){
                System.out.println("serach error");
            }
        }
    }

    public static void detail(Da d) throws InterruptedException, IOException, SQLException {
        String sql="insert into china_wuliu(comp_full_name,comp_type,comp_desc) values(?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        while (true){
            try {
                String[] value = d.qu();
                Document doc = JsoupUtils.detailget(value[0]);
                String quan = doc.title();;
                String desc = JsoupUtils.getString(doc, "p", 0);

                if(desc.contains("中国物流网 - 物流行业门户网站!")){
                    desc = JsoupUtils.getString(doc, "p", 1);
                }
                if(desc.length()<50){
                    desc = JsoupUtils.getString(doc, "p", 2);
                }

                ps.setString(1, quan);
                ps.setString(2, value[1]);
                ps.setString(3, desc);
                ps.executeUpdate();
                aa++;
                System.out.println(aa + "***************************************************"+"     "+d.po.size());
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }



    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }

    class Da{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }

}
