package caipanwenshu;

import Utils.Dup;
import Utils.Producer;
import Utils.RedisClu;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Serach {
    private static RedisClu rd=new RedisClu();
    private static int p=0;
    static{
        if(rd.getslength("caipan_serach")==0) {
            String sql="select comp_full_name from dm_online.dm_company_base_info";
            try {
                PreparedStatement ps=Data.conn.prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while (rs.next()){
                    String comp=rs.getString(rs.findColumn("comp_full_name"));
                    rd.set("caipan_serach",comp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException, SQLException, InterruptedException {
        String user=args[0];
        String pass=args[1];
        String xian=args[2];
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int x=1;x<=Integer.parseInt(xian);x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        parse(user,pass);
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
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Data.heart();
            }
        });
        thread.start();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spider.getip();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spider.conip();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();

        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                System.out.println("结束了！");
                System.exit(0);
            }
            Thread.sleep(2000);
        }
    }

    public static void parse(String user,String pass) throws IOException, SQLException, DocumentException {
        //String sql = "insert into adjude_document_list(case_title,case_num,judge_date,judge_organ,judge_stage,judge_reason,serach_key,document_id) values(?,?,?,?,?,?,?,?)";
        //PreparedStatement ps = Data.conn.prepareStatement(sql);
        spider s=new spider();
        Producer producer=new Producer(false);
        while (true) {
            try {
                String key = rd.get("caipan_serach");
                if (!Dup.nullor(key)) {
                    break;
                }
                for(int pa=1;pa<=25;pa++) {
                    try {
                        Document doc = s.serach(String.valueOf(pa), key);
                        String json = Dup.qujson(doc).replace("\"[", "[").replace("]\"", "]").replace("\\\"", "\"");
                        if (Dup.nullor(json) && json.length() > 10) {
                            JSONArray jsonArray = new JSONArray(json);
                            for (int a = 0; a < jsonArray.length(); a++) {
                                try {
                                    if (a != 0) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                                        /*ps.setString(1, getValue(jsonObject, "案件名称"));
                                        ps.setString(2, getValue(jsonObject, "案号"));
                                        ps.setString(3, getValue(jsonObject, "裁判日期"));
                                        ps.setString(4, getValue(jsonObject, "法院名称"));
                                        ps.setString(5, getValue(jsonObject, "审判程序"));
                                        ps.setString(6, getValue(jsonObject, "裁判要旨段原文"));
                                        ps.setString(7, key);
                                        ps.setString(8, getValue(jsonObject, "文书ID"));
                                        ps.executeUpdate();*/

                                        JSONObject js=new JSONObject();
                                        js.put("familyname","wenshu");
                                        js.put("tablename","adjude_document_list");
                                        js.put("rowkey","document_id+document_id###case_title###familyname");
                                        js.put("document_id",getValue(jsonObject, "文书ID"));
                                        js.put("case_title",getValue(jsonObject, "案件名称"));
                                        js.put("case_num",getValue(jsonObject, "案号"));
                                        js.put("judge_date",getValue(jsonObject, "裁判日期"));
                                        js.put("judge_organ",getValue(jsonObject, "法院名称"));
                                        js.put("judge_stage",getValue(jsonObject, "审判程序"));
                                        js.put("judge_reason",getValue(jsonObject, "裁判要旨段原文"));
                                        js.put("serach_key",key);
                                        producer.send("ControlTotal",js.toString());
                                        p++;
                                        System.out.println(p + "********************************" + key);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (jsonArray.length() < 21) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getValue(JSONObject js,String key){
        try{
            return js.get(key).toString();
        }catch (Exception e){
            return "";
        }
    }
}
