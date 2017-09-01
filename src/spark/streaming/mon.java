package spark.streaming;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/28.
 */
public class mon {
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, UnsupportedEncodingException, MessagingException, ParseException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
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

        data2(con);

    }

    public static void data2(Connection con) throws SQLException, UnsupportedEncodingException, MessagingException {
        String sql="select sum(c_ount) as `sum`,sp_name,ty_pe from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 1 DAY) GROUP BY sp_name,ty_pe";
        String sqlsanxiaoshi="select sum(c_ount) as `sum`,sp_name,ty_pe from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 3 hour) GROUP BY sp_name,ty_pe";
        String sqlyi="select sum(c_ount) as `sum`,sp_name,ty_pe from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 1 hour) GROUP BY sp_name,ty_pe";
        String ban="select sum(c_ount) as `sum`,sp_name,ty_pe from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 30 minute) GROUP BY sp_name,ty_pe";


        PreparedStatement ps1=con.prepareStatement(sql);
        PreparedStatement ps2=con.prepareStatement(sqlsanxiaoshi);
        PreparedStatement ps3=con.prepareStatement(sqlyi);
        PreparedStatement ps4=con.prepareStatement(ban);

        ResultSet rs1=ps1.executeQuery();
        ResultSet rs2=ps2.executeQuery();
        ResultSet rs3=ps3.executeQuery();
        ResultSet rs4=ps4.executeQuery();


        String html="<html><body><table border=\"1\"><tr><th>爬虫服务</th><th>近一天抓取成功总数/近一天抓取失败总数/平均每个用时</th><th>近三小时抓取成功总数/近三小时抓取失败总数/平均每个用时</th><th>近一小时抓取成功总数/近一小时抓取失败总数/平均每个用时</th><th>近半小时抓取成功总数/近半小时抓取失败总数/平均每个用时</th></tr>";
        Map<String,String> map1=new HashMap<String, String>();
        Map<String,String> map2=new HashMap<String, String>();
        Map<String,String> map3=new HashMap<String, String>();
        Map<String,String> map4=new HashMap<String, String>();
        List<String> lists=new ArrayList<String>();

        while (rs1.next()){
            String table=rs1.getString(rs1.findColumn("sp_name"));
            String ty=rs1.getString(rs1.findColumn("ty_pe"));
            String sum=rs1.getString(rs1.findColumn("sum"));
            map1.put(table+"_"+ty,sum);
        }

        while (rs2.next()){
            String table=rs2.getString(rs2.findColumn("sp_name"));
            String ty=rs2.getString(rs1.findColumn("ty_pe"));
            String sum=rs2.getString(rs2.findColumn("sum"));
            map2.put(table+"_"+ty,sum);
        }

        while (rs3.next()){
            String table=rs3.getString(rs3.findColumn("sp_name"));
            String ty=rs3.getString(rs1.findColumn("ty_pe"));
            String sum=rs3.getString(rs3.findColumn("sum"));
            map3.put(table+"_"+ty,sum);
        }

        while (rs4.next()){
            String table=rs4.getString(rs4.findColumn("sp_name"));
            String ty=rs4.getString(rs1.findColumn("ty_pe"));
            String sum=rs4.getString(rs4.findColumn("sum"));
            map4.put(table+"_"+ty,sum);
        }


        boolean br=false;
        for(Map.Entry<String,String> entry: map1.entrySet()){
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            String filename=entry.getKey().split("_",2)[0];
            String s4=map4.get(filename+"_success");
            String s44=map4.get(filename+"_error");
            String s2=map2.get(filename+"_success");
            String s22=map2.get(filename+"_error");
            String s3=map3.get(filename+"_success");
            String s33=map3.get(filename+"_error");
            String s1=map1.get(filename+"_success");
            String s11=map1.get(filename+"_error");
            s2=flag(s2);
            s22=flag(s22);
            s3=flag(s3);
            s33=flag(s33);
            s1=flag(s1);
            s11=flag(s11);
            s4=flag(s4);
            s44=flag(s44);

            int p1=flag2(s1,s11);
            int p2=flag2(s2,s22);
            int p3=flag2(s3,s33);
            int p4=flag2(s4,s44);

            String a1= decimalFormat.format((float) 86400 / p1);
            String a2=decimalFormat.format((float) 10800 / p2);
            String a3=decimalFormat.format((float) 3600 / p3);
            String a4=decimalFormat.format((float) 1800 / p4);


            if(Float.parseFloat(a4)>Float.parseFloat(a1)*2){
                html=html+"<tr><td><font color=\"#FF0000\">"+filename+"</font></td><td><font color=\"#FF0000\">"+s1+"/"+s11+"/"+a1+"s</font></td><td>"+s2+"/"+s22+"/"+a2+"s</td><td>"+s3+"/"+s33+"/"+a3+"s</td><td><font color=\"#FF0000\">"+s4+"/"+s44+"/"+a4+"s</font></td></tr>";
                br=true;
                lists.add(filename);
            }else {
                html = html + "<tr><td>" + filename + "</td><td><font color=\"#0000C6\">" + s1 + "</font>/<font color=\"#FF0080\">"+s11+"</font>/" + a1 + "s</td><td><font color=\"#0000C6\">" + s2 + "</font>/<font color=\"#FF0080\">" +s22+"</font>/"+ a2 + "s</td><td><font color=\"#0000C6\">" + s3 + "</font>/<font color=\"#FF0080\">" +s33+"</font>/"+ a3 + "s</td><td><font color=\"#0000C6\">" + s4 + "</font>/<font color=\"#FF0080\">" +s44+"</font>/"+ a4 + "s</td></tr>";
            }
        }
        html=html+"</table>";

        Document doc= Jsoup.parse(html);
        Elements ele=doc.select("table tbody tr");
        int p=0;
        List<String> li=new ArrayList<String>();
        for(Element e:ele){
            if(p>=1){
                for(String s:li){
                    if(e.toString().equals(s)){
                        html=html.replace(s.replace("\n","").replace("> <","><"),"");
                        html=html.replace("</table>","")+s.replace("\n","").replace("> <","><")+"</table>";
                    }
                }
                li.add(e.toString());
            }
            p++;
        }


        if(map1.size()>0&&br) {
            send(html+"\n<font color=\"#FF0000\">警告！！！"+lists.toString()+"爬虫服务表，近半小时抓取速率低于近一天抓取速率一倍以上，请注意！！！</font>\n</body></html>");
        }else if(map1.size()>0&&!br){
            send(html);
        }else{
            send("<font color=\"#FF0000\">当前没有爬虫在运行，或爬虫日志没有进入相应目录</font>");
        }
    }

    public static void data(Connection con) throws SQLException, UnsupportedEncodingException, MessagingException, ParseException {
        String sql="select sum(c_ount) as `sum`,sp_name from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 1 DAY) and ty_pe='success' GROUP BY sp_name";
        String sqlsanxiaoshi="select sum(c_ount) as `sum`,sp_name from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 3 hour) and ty_pe='success' GROUP BY sp_name";
        String sqlyi="select sum(c_ount) as `sum`,sp_name from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 1 hour) and ty_pe='success' GROUP BY sp_name";
        String ban="select sum(c_ount) as `sum`,sp_name from spider_log_new where count_time > DATE_SUB(now(),INTERVAL 30 minute) and ty_pe='success' GROUP BY sp_name";


        PreparedStatement ps1=con.prepareStatement(sql);
        PreparedStatement ps2=con.prepareStatement(sqlsanxiaoshi);
        PreparedStatement ps3=con.prepareStatement(sqlyi);
        PreparedStatement ps4=con.prepareStatement(ban);

        ResultSet rs1=ps1.executeQuery();
        ResultSet rs2=ps2.executeQuery();
        ResultSet rs3=ps3.executeQuery();
        ResultSet rs4=ps4.executeQuery();

        String html="<html><body><table border=\"1\"><tr><th>爬虫服务</th><th>近一天抓取成功总数/近一天抓取失败总数/平均每个用时</th><th>近三小时抓取成功总数/近三小时抓取失败总数/平均每个用时</th><th>近一小时抓取成功总数/近一小时抓取失败总数/平均每个用时</th><th>近半小时抓取成功总数/近半小时抓取失败总数/平均每个用时</th></tr>";
        Map<String,String> map1=new HashMap<String, String>();
        Map<String,String> map2=new HashMap<String, String>();
        Map<String,String> map3=new HashMap<String, String>();
        Map<String,String> map4=new HashMap<String, String>();
        List<String> lists=new ArrayList<String>();

        while (rs1.next()){
            String table=rs1.getString(rs1.findColumn("sp_name"));
            String sum=rs1.getString(rs1.findColumn("sum"));
            map1.put(table,sum);
        }

        while (rs2.next()){
            String table=rs2.getString(rs2.findColumn("sp_name"));
            String sum=rs2.getString(rs2.findColumn("sum"));
            map2.put(table,sum);
        }

        while (rs3.next()){
            String table=rs3.getString(rs3.findColumn("sp_name"));
            String sum=rs3.getString(rs3.findColumn("sum"));
            map3.put(table,sum);
        }

        while (rs4.next()){
            String table=rs4.getString(rs4.findColumn("sp_name"));
            String sum=rs4.getString(rs4.findColumn("sum"));
            map4.put(table,sum);
        }

        boolean br=false;
        for(Map.Entry<String,String> entry:map1.entrySet()){
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            String filename=entry.getKey();
            String s2=map2.get(filename);
            String s3=map3.get(filename);
            String s4=map4.get(filename);
            s2=flag(s2);
            s3=flag(s3);
            s4=flag(s4);
            String a1= decimalFormat.format((float) 86400 / Integer.parseInt(entry.getValue()));
            String a2=decimalFormat.format((float) 10800 / Integer.parseInt(s2));
            String a3=decimalFormat.format((float) 3600 / Integer.parseInt(s3));
            String a4=decimalFormat.format((float) 1800 / Integer.parseInt(s4));


            if(Float.parseFloat(a4)>Float.parseFloat(a1)*2){
                html=html+"<tr><td><font color=\"#FF0000\">"+filename+"</font></td><td><font color=\"#FF0000\">"+entry.getValue()+"/"+a1+"s</font></td><td>"+s2+"/"+a2+"s</td><td>"+s3+"/"+a3+"s</td><td><font color=\"#FF0000\">"+s4+"/"+a4+"s</font></td></tr>";
                br=true;
                lists.add(filename);
            }else {
                html = html + "<tr><td>" + filename + "</td><td>" + entry.getValue() + "/" + a1 + "s</td><td>" + s2 + "/" + a2 + "s</td><td>" + s3 + "/" + a3 + "s</td><td>" + s4 + "/" + a4 + "s</td></tr>";
            }
        }
        html=html+"</table></body></html>";
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = simpleDateFormat.format(date) + " 10:30:00";
        String time2 = simpleDateFormat.format(date) + " 11:30:00";
        long t2 = simpleDateFormat1.parse(time1).getTime();
        long t3 = simpleDateFormat1.parse(time2).getTime();
        long t=System.currentTimeMillis();

        if(map1.size()>0&&br) {
            send(html+"\n<font color=\"#FF0000\">警告！！！"+lists.toString()+"爬虫服务表，近半小时抓取速率低于近一天抓取速率一倍以上，请注意！！！</font>\n</body></html>");
        }else if(map1.size()>0&&!br){
            send(html);
        }else{
            send("<font color=\"#FF0000\">当前没有爬虫在运行，或爬虫日志没有进入相应目录</font>");
        }

    }

    public static String flag(String key){
        if(StringUtils.isEmpty(key)){
            return "0";
        }else{
            return key;
        }
    }

    public static Integer flag2(String s1,String s2){
        if(Integer.parseInt(s1)+Integer.parseInt(s2)==0){
            return 1;
        }else{
            return Integer.parseInt(s1)+Integer.parseInt(s2);
        }
    }


    public static void send(String html) throws MessagingException, UnsupportedEncodingException {
        Properties prop=new Properties();
        prop.setProperty("mail.host","smtp.mxhichina.com");
        prop.setProperty("mail.trabsport.protocol","smtp");
        prop.setProperty("mail.smtp.auth","true");

        Session session=Session.getInstance(prop);
        session.setDebug(false);
        Transport transport=session.getTransport();
        transport.connect("smtp.mxhichina.com","hongyu.sun@innotree.cn","3961Shy3961");
        Message message = createMimeMessage(session,html);
        //5、发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static MimeMessage createMimeMessage(Session session,String html) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage=new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("hongyu.sun@innotree.cn","spider","utf-8"));
        mimeMessage.setRecipients(Message.RecipientType.TO, "hongyu.sun@innotree.cn");
        //mimeMessage.addRecipients(Message.RecipientType.TO, "tao.chen@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "enzhen.xiao@innotree.cn");
        //mimeMessage.addRecipients(Message.RecipientType.TO, "xiaolong.duan@innotree.cn");
        //mimeMessage.addRecipients(Message.RecipientType.TO, "yuxi.wang@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "lijian.sun@innotree.cn");
        mimeMessage.addRecipients(Message.RecipientType.TO, "ren.lan@innotree.cn");
        //mimeMessage.addRecipients(Message.RecipientType.TO, "wang.hao@innotree.cn");
        mimeMessage.setSubject("爬虫监控","utf-8");
        //邮件的文本内容
        mimeMessage.setContent(html, "text/html;charset=UTF-8");
        return mimeMessage;
    }
}
