package Utils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dup {

    public static String qujson(Document doc){
        String json = doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("<font color=\"red\">", "").replace("</font>", "").replace("  ", "").replace("\n", "").trim();
        return json;
    }

    public static List<Map<String,Object>> quchong(List<Map<String,Object>> list,String qukey){
        List<Map<String,Object>> ll=new ArrayList<>();
        for(Map<String,Object> mm:list){
            String onid= String.valueOf(mm.get(qukey));
            for (int x=0;x<ll.size();x++) {
                String no = String.valueOf(ll.get(x).get(qukey));
                if (onid.equals(no)) {
                    ll.remove(x);
                }
            }

            ll.add(mm);
        }
        return ll;
    }

    public static boolean nullor(String key){
        if(key!=null&&key.replaceAll("\\s","").replace(" ","").length()>0){
            return true;
        }else {
            return false;
        }
    }

    public static String reAllNull(String key){
        if(Dup.nullor(key)){
            return key.replaceAll("\\s", "").replace(" ", "")
                    .replace(" ","").replace("　","")
                    .replace("　","").trim();
        }else{
            return null;
        }
    }

    public static String renull(String key){
        if(Dup.nullor(key)) {
            Pattern p = Pattern.compile("[a-zA-Z]");
            Matcher m = p.matcher(key);
            if(m.find()){
                return key.trim();
            }else{
                return key.replaceAll("\\s", "").replace(" ", "")
                        .replace(" ","").replace("　","")
                        .replace("　","").trim();
            }
        }else{
            return null;
        }
    }

    public static void mapvaluepaixu(Map<String,Integer> us){
        Set<Map.Entry<String, Integer>> ks = us.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                ks);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (o1.getValue() < o2.getValue())
                    return -1;
                else if (o1.getValue() > o2.getValue())
                    return 1;
                return 0;
            }
        });
        System.out.println(list);
    }

    public static String getRoundStr(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(52);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


    public static String getVa(JSONObject jsonObject, String key){
        try{
            if(jsonObject.get(key)!=null){
                return String.valueOf(jsonObject.get(key));
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static String toHi(String key){
        try {
            if (Dup.nullor(key)) {
                if(key.equals("null")){
                    return "";
                }else {
                    return key.replaceAll("\n", "").replaceAll("\r", "")
                            .replace("\001", "");
                }
            } else {
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static String parstTime(Long key){
        try{
            if(key!=null){
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return simpleDateFormat.format(new Date(key));
            }else {
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static String parstTime(String re1,String key){
        try{
            if(Dup.nullor(key)){
                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat(re1);
                SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return simpleDateFormat2.format(simpleDateFormat1.parse(key));
            }else {
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static String reN(String key){
        try{
            if(Dup.nullor(key)){
                return key.replaceAll("\r","&z&").replaceAll("\n","&z&")
                        .replace("\001","");
            }else{
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }



    public static Map<String,String> getAddGd(String add){
        Map<String,String> map=new HashMap<>();
        try {
            Document doc = null;
            int pa=0;
            while (true) {
                try {
                    doc = Jsoup.connect("https://restapi.amap.com/v3/geocode/geo?address="+ URLEncoder.encode(add,"utf-8")+"&output=json&key=9d8216eb89b2cc3b5ccd65b93dc07950")
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .timeout(3000)
                            .get();
                    if(doc.outerHtml().contains("当前并发量已经超过约定并发配额")){
                        Thread.sleep(500);
                    }else {
                        break;
                    }
                } catch (Exception e) {
                    pa++;
                    if(pa>=10){
                        break;
                    }
                    Thread.sleep(3000);
                    e.printStackTrace();
                }
            }
            String json = Dup.qujson(doc);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2=jsonObject.getJSONArray("geocodes").getJSONObject(0);

            String sp = jsonObject2.getString("province");
            String sc = jsonObject2.getString("city");
            String sd = jsonObject2.getString("district");
            String lng=jsonObject2.getString("location").split(",")[0];
            String lat=jsonObject2.getString("location").split(",")[1];

            map.put("province",sp);
            map.put("city",sc);
            map.put("district",sd);
            map.put("lng",lng);
            map.put("lat",lat);
            return map;
        }catch (Exception e){
            System.out.println("error");
            return null;
            //e.printStackTrace();
        }
    }

    public static Map<String,String> getAddBd(String add){
        Map<String,String> map=new HashMap<>();
        try {
            Document doc = null;
            int pa=0;
            while (true) {
                try {
                    doc = Jsoup.connect("http://api.map.baidu.com/geocoder/v2/?address="+URLEncoder.encode(add,"utf-8")+"&output=json&ak=S1fAPInHR3V02p51bYXBnpqMp21o1Eb9")
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .timeout(3000)
                            .get();
                    if(doc.outerHtml().contains("当前并发量已经超过约定并发配额")){
                        Thread.sleep(500);
                    }else {
                        break;
                    }
                } catch (Exception e) {
                    pa++;
                    if(pa>=10){
                        break;
                    }
                    Thread.sleep(3000);
                    e.printStackTrace();
                }
            }
            String json =Dup.qujson(doc);
            JSONObject jsonObject = new JSONObject(json);
            String lng = jsonObject.getJSONObject("result").getJSONObject("location").get("lng").toString();
            String lat = jsonObject.getJSONObject("result").getJSONObject("location").get("lat").toString();
            Document doc2 = null;
            int pb=0;
            while (true) {
                try {
                    doc2 = Jsoup.connect("http://api.map.baidu.com/geocoder/v2/?location="+lat+","+lng+"&output=json&pois=1&latest_admin=1&ak=S1fAPInHR3V02p51bYXBnpqMp21o1Eb9")
                            .ignoreHttpErrors(true)
                            .ignoreContentType(true)
                            .timeout(3000)
                            .get();
                    if(doc2.outerHtml().contains("当前并发量已经超过约定并发配额")){
                        Thread.sleep(500);
                    }else {
                        break;
                    }
                } catch (Exception e) {
                    pb++;
                    if(pb>=10){
                        break;
                    }
                    Thread.sleep(3000);
                    e.printStackTrace();
                }
            }
            String json2 = Dup.qujson(doc2);
            JSONObject jsonObject2 = new JSONObject(json2);
            String sp = jsonObject2.getJSONObject("result").getJSONObject("addressComponent").getString("province");
            String sc = jsonObject2.getJSONObject("result").getJSONObject("addressComponent").getString("city");
            String sd = jsonObject2.getJSONObject("result").getJSONObject("addressComponent").getString("district");



            map.put("province",sp);
            map.put("city",sc);
            map.put("district",sd);
            map.put("lng",lng);
            map.put("lat",lat);

            return map;
        }catch (Exception e){
            System.out.println("error");
            return null;
            //e.printStackTrace();
        }
    }
}
