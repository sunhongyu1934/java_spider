package chuangyebang;

import Utils.Dup;
import Utils.Producer;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class cyb {
    public static Connection conn;
    public static int b=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://10.64.100.21:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100";
        String username="spider01";
        String password="innotree-spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con=null;
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
    public static void main(String args[]) throws IOException, InterruptedException {
        ExecutorService pool= Executors.newFixedThreadPool(20);
        for(int a=1;a<=8;a++){
            int finalA = a;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(String.valueOf(finalA));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();
        while (true){
            if(pool.isTerminated()){
                System.exit(0);
            }
            Thread.sleep(1000);
        }
    }

    public static void data(String page) throws IOException, SQLException, DocumentException {
        String sql="insert into cyb_finacing(comp_full_name,name,time,round,money,vc) values(?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        Producer producer=new Producer(false);
        Document doc= Jsoup.connect("https://www.cyzone.cn/event/list-0-"+page+"-0-0-0-0/0")
                .timeout(20000)
                .get();

        Elements elements=doc.select("tbody tr.table-plate3");
        for(Element element:elements){
            try {
                String comp_full_name = element.select("td:nth-child(2)>span:nth-child(3)").text().replace("'", "");
                String name = element.select("td:nth-child(2)>span:nth-child(1)>a").text().replace("'", "");
                String time = element.select("td:nth-child(7)").text().replace("'", "");
                String round = element.select("td:nth-child(4)").text().replace("'", "");
                String money = element.select("td:nth-child(3)").text().replace("'", "");
                /*Elements vcs = element.select("td:nth-child(5)>a");
                StringBuffer stringBuffer = new StringBuffer();
                for (Element element1 : vcs) {
                    stringBuffer.append(element1.text() + ";");
                }
                String vc=null;
                if(stringBuffer.length()>1) {
                    vc = stringBuffer.substring(0, stringBuffer.length() - 1).replace("'", "");
                }*/
                String deurl="https:"+element.select("td:nth-child(8)>a").attr("href");
                Document doc2= Jsoup.connect(deurl)
                        .timeout(20000)
                        .get();
                String vc=doc2.select("title").text().split("投资方")[1].length()>2
                        ? doc2.select("title").text().split("投资方")[1].replace("、",";")
                        .replace("。-创投库","").replace("'","\\\\'")
                        :null;
                if(Dup.nullor(vc)&&vc.substring(vc.length()-1).equals(";")){
                    vc=vc.substring(0,vc.length()-1);
                }


                JSONObject jsonObject4=new JSONObject();
                jsonObject4.put("comp_id","createid=comp_full_name");
                jsonObject4.put("comp_full_name",comp_full_name);
                jsonObject4.put("comp_short_name",name);
                jsonObject4.put("financing_time",time);
                jsonObject4.put("financing_round",round);
                jsonObject4.put("financing_amount",money);
                jsonObject4.put("invest_institution",vc);
                jsonObject4.put("rowkey","comp_short_name+comp_short_name###invest_institution###financing_time###financing_round###familyname");
                jsonObject4.put("tablename","ods_company_financing");
                jsonObject4.put("familyname","cyb");
                producer.send("ControlTotal",jsonObject4.toString());

                /*ps.setString(1, comp_full_name);
                ps.setString(2, name);
                ps.setString(3, time);
                ps.setString(4, round);
                ps.setString(5, money);
                ps.setString(6, vc);
                System.out.println(comp_full_name);
                System.out.println(name);
                System.out.println(time);
                System.out.println(round);
                System.out.println(money);
                System.out.println(vc);
                ps.addBatch();*/
                b++;
                System.out.println(b + "***********************************************" + page);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //ps.executeBatch();
    }
}
