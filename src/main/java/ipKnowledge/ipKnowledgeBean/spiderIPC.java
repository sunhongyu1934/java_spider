package ipKnowledge.ipKnowledgeBean;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/7.
 */
public class spiderIPC {
    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        spider();
    }

    public static void spider() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        Document res= Jsoup.connect("http://legal.iprzd.com/portal/ipc/level/0")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .timeout(1000000)
                .ignoreContentType(true)
                .post();
        String ContantUrl="http://legal.iprzd.com/portal/ipc/findChildren/";
        String oneTag=res.body().toString().replace("<body>","").replace("</body>","").trim();
        Gson gson=new Gson();
        One one=gson.fromJson(oneTag,One.class);
        int line=0;
        int line2=0;
        int line3=0;
        int line4=0;
        for(int x=0;x<one.data.size();x++){
            Document resTwo= Jsoup.connect(ContantUrl+one.data.get(x).uuid)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .timeout(1000000)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .post();
            storeData_one(one.data.get(x).uuid, one.data.get(x).name, one.data.get(x).note, one.data.get(x).subnote, one.data.get(x).level, one.data.get(x).parentipc, one.data.get(x).isParent);
            line++;
            System.out.println("开始抓取一级标签，当前入库第："+line+"条,总计入库："+(line+line2+line3+line4)+"条");
            System.out.println("******************************************************************");
            String twoTag=resTwo.body().toString().replace("<body>","").replace("</body>","").trim();
            Two two=gson.fromJson(twoTag,Two.class);
            for(int y=0;y<two.data.size();y++){
                Document resThree= Jsoup.connect(ContantUrl+two.data.get(y).uuid)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .timeout(1000000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .post();
                storeData_two(two.data.get(y).uuid, two.data.get(y).name, two.data.get(y).note, two.data.get(y).subnote, two.data.get(y).level, two.data.get(y).parentipc, two.data.get(y).isParent);
                line2++;
                System.out.println("开始抓取二级标签，当前入库第："+line2+"条,总计入库："+(line+line2+line3+line4)+"条");
                System.out.println("******************************************************************");
                String threeTag=resThree.body().toString().replace("<body>","").replace("</body>","").trim();
                Three three = gson.fromJson(threeTag,Three.class);
                for(int z=0;z<three.data.size();z++){
                    Document resFour= Jsoup.connect(ContantUrl+three.data.get(z).uuid)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                            .timeout(1000000)
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .post();
                    storeData_three(three.data.get(z).uuid, three.data.get(z).name, three.data.get(z).note, three.data.get(z).subnote, three.data.get(z).level, three.data.get(z).parentipc, three.data.get(z).isParent);
                    line3++;
                    System.out.println("开始抓取三级标签，当前入库第："+line3+"条,总计入库："+(line+line2+line3+line4)+"条");
                    System.out.println("******************************************************************");
                    String fourTag=resFour.body().toString().replace("<body>","").replace("</body>","").trim();
                    Four four = gson.fromJson(fourTag, Four.class);
                    for(int a=0;a<four.data.size();a++){
                        Document resFive= Jsoup.connect(ContantUrl+four.data.get(a).uuid)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                                .timeout(1000000)
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .post();
                        String fiveTag=resFive.body().toString().replace("<body>","").replace("</body>","").trim();
                        Five five = gson.fromJson(fiveTag, Five.class);
                        if(five.data.isEmpty()){
                            /*System.out.println(four.data.get(a).uuid);
                            System.out.println(four.data.get(a).name);
                            System.out.println(four.data.get(a).note);
                            System.out.println(four.data.get(a).subnote);
                            System.out.println(four.data.get(a).level);
                            System.out.println(four.data.get(a).parentipc);
                            System.out.println(four.data.get(a).isParent);
                           //storeData(four.data.get(a).uuid, four.data.get(a).name, four.data.get(a).note, four.data.get(a).subnote, four.data.get(a).level, four.data.get(a).parentipc, four.data.get(a).isParent);
                            line++;
                            System.out.println("数据入库成功，当前为第："+line+"条");
                            System.out.println("******************************************");*/

                        }else{
                            storeData_four(four.data.get(a).uuid, four.data.get(a).name, four.data.get(a).note, four.data.get(a).subnote, four.data.get(a).level, four.data.get(a).parentipc, four.data.get(a).isParent);
                            line4++;
                            System.out.println("开始抓取四级标签，当前入库第："+line4+"条,总计入库："+(line+line2+line3+line4)+"条");
                            System.out.println("******************************************************************");
                        }
                        /*for(int b=0;b<five.data.size();b++){
                            System.out.println(five.data.get(b).uuid);
                            System.out.println(five.data.get(b).name);
                            System.out.println(five.data.get(b).note);
                            System.out.println(five.data.get(b).subnote);
                            System.out.println(five.data.get(b).level);
                            System.out.println(five.data.get(b).parentipc);
                            System.out.println(five.data.get(b).isParent);
                            //storeData(five.data.get(b).uuid, five.data.get(b).name, five.data.get(b).note, five.data.get(b).subnote, five.data.get(b).level, five.data.get(b).parentipc, five.data.get(b).isParent);
                            line++;
                            System.out.println("数据入库成功，当前为第："+line+"条");
                            System.out.println("------------------------------------------------");
                        }*/
                    }
                }
            }
        }
    }


    public static void storeData(String uuid,String name,String note,String subnote,String level,String parentipc,String isParent) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String insertIPCtype="insert into ipctype(uuid,name,note,subnote,level,parentipc,isParent) values(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(insertIPCtype);
        preparedStatement.setString(1,uuid);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,note);
        preparedStatement.setString(4,subnote);
        preparedStatement.setString(5,level);
        preparedStatement.setString(6,parentipc);
        preparedStatement.setString(7,isParent);
        preparedStatement.executeUpdate();



       /* Map<String,String> map=new HashMap<String, String>();

        String selectIpcType="select name,note from ipctype";
        PreparedStatement psipctype=con.prepareStatement(selectIpcType);
        final ResultSet rsipctype=psipctype.executeQuery();
        while(rsipctype.next()){
            map.put(rsipctype.getString(rsipctype.findColumn("name")),rsipctype.getString())
        }


        String updatePatentA="update patent_a_test set patentWords=?,flag_sun=? where ipc=?";
        final PreparedStatement psupdatePatentA=con.prepareStatement(updatePatentA);



        int x=0;
        while(true) {
            String selectPatent_a = "select ipc,patentWords from patent_a_test limit "+x+","+(x+10000);
            PreparedStatement ps = con.prepareStatement(selectPatent_a);
            final ResultSet rs = ps.executeQuery();
            if(rs==null||!rs.next()){
                break;
            }
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("开始遍历数据 patent_a_test");
                        while (rs.next()) {
                            String ipc1 = rs.getString(rs.findColumn("ipc"));
                            String patentWords = rs.getString(rs.findColumn("patentWords"));
                            if (StringUtils.isEmpty(patentWords)) {
                                if (ipc1.contains(";")) {
                                    String ipcs[] = ipc1.split(";");
                                    for (int i = 0; i < ipcs.length; i++) {
                                        String ipc = ipcs[i].split("\\(", 2)[0];
                                        while (rsipctype.next()) {
                                            String name1 = rsipctype.getString(rsipctype.findColumn("name"));
                                            String note1 = rsipctype.getString(rsipctype.findColumn("note"));
                                            if (ipc.equals(name1)) {
                                                System.out.println("开始修改,当前mainipc为：" + ipc1);
                                                if (i >= 1) {
                                                    note1 = note1 + ";";
                                                }
                                                psupdatePatentA.setString(1, note1);
                                                psupdatePatentA.setString(2, "1");
                                                psupdatePatentA.setString(3, ipc1);
                                                psupdatePatentA.executeUpdate();
                                            }
                                        }
                                    }
                                } else {
                                    String ipc = ipc1.split("\\(", 2)[0];
                                    while (rsipctype.next()) {
                                        String name1 = rsipctype.getString(rsipctype.findColumn("name"));
                                        String note1 = rsipctype.getString(rsipctype.findColumn("note"));
                                        if (ipc.equals(name1)) {
                                            System.out.println("开始修改,当前mainipc为：" + ipc1);
                                            psupdatePatentA.setString(1, note1);
                                            psupdatePatentA.setString(2, "1");
                                            psupdatePatentA.setString(3, ipc1);
                                            psupdatePatentA.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            x=x+10000;
        }*/
    }

    public static void storeData_one(String uuid,String name,String note,String subnote,String level,String parentipc,String isParent) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String insertIPCtype="insert into ipctype_one(uuid,name,note,subnote,level,parentipc,isParent) values(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(insertIPCtype);
        preparedStatement.setString(1,uuid);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,note);
        preparedStatement.setString(4,subnote);
        preparedStatement.setString(5,level);
        preparedStatement.setString(6,parentipc);
        preparedStatement.setString(7,isParent);
        preparedStatement.executeUpdate();
    }

    public static void storeData_two(String uuid,String name,String note,String subnote,String level,String parentipc,String isParent) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String insertIPCtype="insert into ipctype_two(uuid,name,note,subnote,level,parentipc,isParent) values(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(insertIPCtype);
        preparedStatement.setString(1,uuid);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,note);
        preparedStatement.setString(4,subnote);
        preparedStatement.setString(5,level);
        preparedStatement.setString(6,parentipc);
        preparedStatement.setString(7,isParent);
        preparedStatement.executeUpdate();
    }

    public static void storeData_three(String uuid,String name,String note,String subnote,String level,String parentipc,String isParent) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String insertIPCtype="insert into ipctype_three(uuid,name,note,subnote,level,parentipc,isParent) values(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(insertIPCtype);
        preparedStatement.setString(1,uuid);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,note);
        preparedStatement.setString(4,subnote);
        preparedStatement.setString(5,level);
        preparedStatement.setString(6,parentipc);
        preparedStatement.setString(7,isParent);
        preparedStatement.executeUpdate();
    }

    public static void storeData_four(String uuid,String name,String note,String subnote,String level,String parentipc,String isParent) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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


        String insertIPCtype="insert into ipctype_four(uuid,name,note,subnote,level,parentipc,isParent) values(?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=con.prepareStatement(insertIPCtype);
        preparedStatement.setString(1,uuid);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,note);
        preparedStatement.setString(4,subnote);
        preparedStatement.setString(5,level);
        preparedStatement.setString(6,parentipc);
        preparedStatement.setString(7,isParent);
        preparedStatement.executeUpdate();
    }
}
