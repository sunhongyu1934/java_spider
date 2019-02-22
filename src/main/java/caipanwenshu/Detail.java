package caipanwenshu;

import Utils.Dup;
import Utils.Producer;
import Utils.RedisClu;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detail {
    private static int p=0;
    private static RedisClu rd=new RedisClu();
    static{
        if(rd.getslength("caipan_detail")==0) {
            String sql="select document_id from spider.adjude_document_list__wenshu";
            try {
                PreparedStatement ps=Data.conn.prepareStatement(sql);
                ResultSet rs=ps.executeQuery();
                while (rs.next()){
                    String comp=rs.getString(rs.findColumn("document_id"));
                    rd.set("caipan_detail",comp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String args[]) throws IOException, SQLException, DocumentException, InterruptedException {
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
        spider s=new spider();
        Producer producer=new Producer(false);
        while (true) {
            try {
                String key = rd.get("caipan_detail");
                if (!Dup.nullor(key)) {
                    break;
                }
                try {
                    Document doc = s.get(key);
                    String str=Dup.qujson(doc);
                    Pattern pat=Pattern.compile("JSON.stringify\\(.+?\\);");
                    Matcher mat=pat.matcher(str);
                    String json1 = null;
                    while (mat.find()){
                        json1=mat.group(0).replace("JSON.stringify(","").replace(");","");
                    }

                    Pattern pat2=Pattern.compile("var\\sdirData\\s=.+?;");
                    Matcher mat2=pat2.matcher(str);
                    String json2=null;
                    while (mat2.find()){
                        json2=mat2.group(0).replace("var dirData = ","").replace(";","").replace("“","\\\"").replace("”","\\\"");
                    }

                    Pattern pat3=Pattern.compile("var\\sjsonHtmlData\\s=.+?var\\sjsonData");
                    Matcher mat3=pat3.matcher(str);
                    String json3=null;
                    while (mat3.find()){
                        json3=mat3.group(0).replace("\"; var jsonData","").replace("var jsonHtmlData = \"","").replace("\"","\\\"").replace("\\\\\"","\"").replace("“","\\\"").replace("”","\\\"");
                    }

                    //JSONObject jsonObject1=new JSONObject(json1);
                    JSONObject jsonObject2=new JSONObject(json2);
                    JSONObject jsonObject3=new JSONObject(json3);

                    String shenfa=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),0)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),0),"value")
                                    :null
                            :null;
                    String anlei=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),1)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),1),"value")
                                    :null
                            :null;
                    String anyou=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),2)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),2),"value")
                                    :null
                            :null;

                    String shencheng=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),3)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),3),"value")
                                    :null
                            :null;
                    String cairi=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),4)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),4),"value")
                                    :null
                            :null;
                    String dangren=getArray(jsonObject2,"RelateInfo")!=null?
                            getObject(getArray(jsonObject2,"RelateInfo"),5)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"RelateInfo"),5),"value")
                                    :null
                            :null;
                    String fayi=getArray(jsonObject2,"LegalBase")!=null?
                            getObject(getArray(jsonObject2,"LegalBase"),0)!=null
                                    ?getValue(getObject(getArray(jsonObject2,"LegalBase"),0),"法规名称")
                                    :null
                            :null;
                    String fanei=getArray(jsonObject2,"LegalBase")!=null?
                            getObject(getArray(jsonObject2,"LegalBase"),0)!=null
                                    ?getArray(getObject(getArray(jsonObject2,"LegalBase"),0),"Items")!=null
                                        ?getArray(getObject(getArray(jsonObject2,"LegalBase"),0),"Items").toString()
                                        :null
                                    :null
                            :null;
                    Pattern pattern=Pattern.compile("浏览.+?次");
                    Matcher matcher=pattern.matcher(str);
                    String cishu=null;
                    while (matcher.find()){
                        cishu=matcher.group(0).replace("浏览：","").replace("次","");
                    }

                    JSONObject js1=new JSONObject();
                    JSONObject js2=new JSONObject();
                    js1.put("familyname","wenshu");
                    js1.put("tablename","adjude_document_abstract");
                    js1.put("rowkey","document_id+document_id###case_title###familyname");
                    js1.put("case_title",getValue(jsonObject3,"Title"));
                    js1.put("judge_organ",shenfa);
                    js1.put("case_type",anlei);
                    js1.put("judge_reason",anyou);
                    js1.put("judge_stage",shencheng);
                    js1.put("judge_date",cairi);
                    js1.put("case_party",dangren);
                    js1.put("law_basis",fayi+"###"+fanei);
                    js1.put("document_id",key);

                    js2.put("familyname","wenshu");
                    js2.put("tablename","adjude_document_content");
                    js2.put("rowkey","document_id+document_id###case_title###familyname");
                    js2.put("document_id",key);
                    js2.put("case_title",getValue(jsonObject3,"Title"));
                    js2.put("publish_date",getValue(jsonObject3,"PubDate"));
                    js2.put("scan_num",cishu);
                    js2.put("judgment_content",getValue(jsonObject3,"Html"));
                    js2.put("case_type",anlei);
                    System.out.println(js1.toString());
                    System.out.println(js2.toString());
                    p++;
                    System.out.println(p + "********************************" + key);
                }catch (Exception e){
                    e.printStackTrace();
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

    public static JSONArray getArray(JSONObject js,String key){
        try{
            return js.getJSONArray(key);
        }catch (Exception e){
            return null;
        }
    }

    public static JSONObject getObject(JSONArray js,int key){
        try{
            return js.getJSONObject(key);
        }catch (Exception e){
            return null;
        }
    }
}
