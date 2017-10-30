package test;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/20.
 */
public class test7 {
    public static void main(String args[]) throws IOException {
        String gongsiku="https://cobra.itjuzi.com/api/company?sortby=inputtime&com_all=1&page=1";
        String gongsixiangqing="https://cobra.itjuzi.com/api/company/59686?user_id=(null)";

        String touzishijianguonei="https://cobra.itjuzi.com/api/get/mobile_investevents?location=in&page=1";
        String touzishijianxiangqing="https://cobra.itjuzi.com/api/company/9329?user_id=(null)";

        String touzishijianguowai="https://cobra.itjuzi.com/api/get/mobile_investevents?location=out&page=1";
        String xiangqing="https://cobra.itjuzi.com/api/company/59692?user_id=(null)";

        String binggouxinxi="https://cobra.itjuzi.com/api/get/mobile_merger?location=in&page=1";
        String xiangqingbinggou="https://cobra.itjuzi.com/api/company/1402?user_id=(null)";

        String touzijigou="https://cobra.itjuzi.com/api/investfirm?page=1";
        String touzijigouxiangqinggailan="https://cobra.itjuzi.com/api/investfirm/detail/2?user_id=(null)";   //detail/i,i为列表页id
        String touzijigoutouzilishi="https://cobra.itjuzi.com/api/investfirm/investeventlist/2?&page=1";
        String touzijigoufenxi="https://cobra.itjuzi.com/api/investfirm/chart/2";



        String renzhengchuangyezhe="https://cobra.itjuzi.com/api/mobile/entrepreneur/list?page=1&per_page=10";
        String renzhengtouziren="https://cobra.itjuzi.com/api/mobile/investor/list?page=1&per_page=10";


        String mingxiaoxiangmuliebiao="https://cobra.itjuzi.com/api/get/famous_edu";
        String mingxiaomiangmu="https://cobra.itjuzi.com/api/get/edu_com/2?page=1";  //edu_com/i,i为列表页取出id


        String mingqixiangmuliebiao="https://cobra.itjuzi.com/api/get/famous_job";
        String mingqixiangmu="https://cobra.itjuzi.com/api/get/job_com/1?page=1";


        String fuhuaqi="https://cobra.itjuzi.com/api/mobile/get_inc_list?page=1";
        String fuhuaqixiangqing="https://cobra.itjuzi.com/api/mobile/get_inc_show/155";



        String zhuanjiliebiao="https://cobra.itjuzi.com/api/album?page=1";
        String zhuanji="https://cobra.itjuzi.com/api/album/471?user_id=(null)";  //album/i,i为列表页取出id
        String jutiye="https://cobra.itjuzi.com/api/company/29414?user_id=(null)";


        Document doc = Jsoup.connect("https://cobra.itjuzi.com/api/investfirm?page=1")
                .userAgent("v4_itjuzi/3.9.2 (iPhone; iOS 10.2.1; Scale/2.00)")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-Hans-CN;q=1")
                .header("Authorization", "Bearer GTsRkuHoDN7ElNrve2U9tJ90FfhPenNo3dhGyu1z")
                .timeout(1000000)
                .get();
        String str=doc.outerHtml();
        Pattern pattern = Pattern.compile("\\\\u[0-9,a-f,A-F]{4}");
        Matcher mat= pattern.matcher(str);


        while(mat.find()){
            StringBuffer string = new StringBuffer();
                int data = Integer.parseInt(mat.group(0).replace("\\u",""), 16);
            string.append((char) data);
            str=str.replace(mat.group(0),string.toString());
        }
        System.out.println(str);


    }
}