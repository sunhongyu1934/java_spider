package chuangxin;

import Utils.Dup;
import Utils.JsoupUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Map;

public class spider {
    private static int aa=0;
    private static String hexStr =  "0123456789ABCDEF";
    public static void main(String args[]) throws IOException, InterruptedException {
        //get("/aa/project/qyyl/id/226010");
        serach();
        //serach();
    }

    public static String BinaryToHexString(byte[] bytes){
        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex;
        }
        return result;
    }

    public static Map<String,String> login(String year) throws IOException, InterruptedException {
        Connection.Response docc=Jsoup.connect("https://cn.cxcyds.com/"+year+"/cxcydsmanage.php?s=/public/login")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Encoding","gzip, deflate, br")
                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .header("Host","baoming.cxcyds.com")
                .header("Referer","http://www.cxcyds.com/")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
        Map<String,String> mm=docc.cookies();
        System.out.println(mm);

        System.out.println("https://cn.cxcyds.com/"+year+"/cxcydsmanage.php?s=/public/verify&mt="+Math.random());
        Connection.Response doc1=Jsoup.connect("https://cn.cxcyds.com/"+year+"/cxcydsmanage.php?s=/public/verify&mt="+Math.random())
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding","gzip, deflate, br")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Host","baoming.cxcyds.com")
                .header("Origin","https://baoming.cxcyds.com")
                .header("Referer","https://baoming.cxcyds.com/cxcydsmanage.php?s=/public/login")
                .method(Connection.Method.GET)
                .cookies(mm)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute();
        byte[] data=doc1.bodyAsBytes();

        String da=BinaryToHexString(data);
        Document doc2 =null;
        while (true) {
            try {
                doc2 = Jsoup.connect("http://apib.sz789.net:88/RecvByte.ashx")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("username", "fleashese")
                        .data("password", "849915")
                        .data("softId", "61778")
                        .data("imgdata", da)
                        .post();
                break;
            }catch (Exception e){
                System.out.println("shibie cuowu");
            }
        }
        String json= Dup.qujson(doc2);
        JSONObject jsonObject=new JSONObject(json);
        System.out.println(jsonObject.toString());
        String yan=jsonObject.getString("result");

        Connection.Response doc=Jsoup.connect("https://cn.cxcyds.com/"+year+"/cxcydsmanage.php?s=/public/login")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding","gzip, deflate, br")
                .header("Accept-Language","zh-CN,zh;q=0.9")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Host","baoming.cxcyds.com")
                .header("Origin","https://baoming.cxcyds.com")
                .header("Referer","https://baoming.cxcyds.com/cxcydsmanage.php?s=/public/login")
                .data("KeyID","17038338a5b9ca05")
                .data("return_EncData","")
                .data("admin","")
                .data("username","c3SnqkrwBPJxkCRguQ5Owg==")
                .data("password","3puL9LRlhxHsXSwBZXw+Sw==")
                .data("verify",yan)
               .cookies(mm)
                .method(Connection.Method.POST)
                .execute();
        System.out.println(mm);
        return mm;

    }

    public static void serach() throws IOException, InterruptedException {
        String year="2015";
        Map<String,String> map=login("2017");
        String liuqi="https://cn.cxcyds.com/"+year+"/cxcydsmanage.php?s=%2Fproject%2Fqiye%2Fstatus%2F0&p=";
        String yiwu="https://cn.cxcyds.com/"+year+"/admin.php?m=project&a=qiye&status=0&p=";
        for(int a=1;a<=1000;a++) {
            Document doc = Jsoup.connect(yiwu+a)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .cookies(map)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .get();
            //System.out.println(doc.outerHtml());
            File file=new File("C:\\Users\\13434\\Desktop\\cx_serach\\cx_serach_"+year);
            if(!file.exists()){
                file.mkdirs();
            }
            OutputStream outputStream=new FileOutputStream("C:\\Users\\13434\\Desktop\\cx_serach\\cx_serach_"+year+"\\"+a+".txt",false);
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
            bufferedWriter.write(doc.outerHtml());
            bufferedWriter.flush();
            bufferedWriter.close();
            Elements elements= JsoupUtils.getElements(doc,"tbody tr");
            int s=0;
            for(Element element:elements){
                String url=JsoupUtils.getHref(element,"td a","href",0);
                if(!Dup.nullor(url)){
                    continue;
                }
                System.out.println(url);
                get(url,a,map,year);
                Thread.sleep(500);
                s++;
            }
            System.out.println(a);
            if(s<10){
                break;
            }
        }

    }
    public static void get(String url,int a,Map<String,String> map,String year) throws IOException {
        String url2="https://cn.cxcyds.com"+url.replace("infor","qyyl");
        //String url2="https://cn.cxcyds.com/2017/cxcydsmanage.php?s=/project/qyyl/id/176585";
        System.out.println(url2+"       "+a);
        Document doc= Jsoup.connect(url2)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .cookies(map)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .timeout(10000)
                .get();
        Elements ele=JsoupUtils.getElements(doc,"div.jbxx div.jbxx_table.gdxx_table table:contains(资料类型) tr");
        int px=0;
        for(Element e:ele){
            if(px!=0){
                String xz=JsoupUtils.getHref(e,"td a","href",0);
                //downImg(xz,url2.split("/")[8],year);
                System.out.println(xz);
            }
            px++;
        }
        File file=new File("C:\\Users\\13434\\Desktop\\cx\\cx_"+year);
        if(!file.exists()){
            file.mkdirs();
        }
        OutputStream outputStream=new FileOutputStream("C:\\Users\\13434\\Desktop\\cx\\cx_"+year+"\\"+url2.split("/")[4].replace("admin.php?m=project&a=qyyl&id=","")+".txt",false);
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
        bufferedWriter.write(doc.outerHtml());
        bufferedWriter.flush();
        bufferedWriter.close();
        aa++;
        System.out.println(aa+"************************************************************");

    }

    public static void parese() throws IOException {
        File file=new File("C:\\Users\\13434\\Desktop\\cx");
        File[] list=file.listFiles();
        for(File file1:list){
            String docs=null;
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file1),"utf-8"));
            String str;
            StringBuffer s=new StringBuffer();
            while ((str=bufferedReader.readLine())!=null){
                s.append(str+"\n");
            }
            docs=s.toString();
            Document doc=Jsoup.parse(docs,"utf-8");
            Elements ele=JsoupUtils.getElements(doc,"div.jbxx div.jbxx_table.gdxx_table tbody:contains(资料类型) tr");
            int px=0;
            for(Element e:ele){
                if(px!=0){
                    String xz=JsoupUtils.getHref(e,"td a","href",0);
                    //downImg(xz,file1.getName().replace(".txt",""));
                }
                px++;
            }
        }
    }

    public static void downImg(String xz,String id,String year) throws IOException {
        Connection.Response  doc2=Jsoup.connect("https://cn.cxcyds.com"+xz)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Cookie","UM_distinctid=16439defc44296-0189ba92d5b2624-293e1d4e-144000-16439defc45de2; PHPSESSID=0rfbbiogj6e2l1cih8gd4cfbo7; yunsuo_session_verify=0cc8609d57f751563c23a20fb87bc325")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .maxBodySize(1000000000)
                .execute();
        File file=new File("C:\\Users\\13434\\Desktop\\cx_download\\cx_download_"+year+"\\"+id);
        if(!file.exists()){
            file.mkdirs();
        }
        byte by[]=doc2.bodyAsBytes();
        OutputStream outputStream=new FileOutputStream("C:\\Users\\13434\\Desktop\\cx_download\\cx_download_"+year+"\\"+id+"\\"+xz.split("/")[5]);
        outputStream.write(by);
        outputStream.flush();
        outputStream.close();
    }

    public static void downImg2() throws IOException {
        /*Document doc=Jsoup.parse(new File("C:\\Users\\13434\\Desktop\\cx\\204596.txt"),"utf-8");
        System.out.println(doc.outerHtml());*/
        Connection.Response  doc2=Jsoup.connect("https://baoming.cxcyds.com/upload/image/20180525/20180525100616_83084.zip")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Cookie","UM_distinctid=1639518d8d16-0d03c001b1299d-31196d1d-144000-1639518d8d256d; yunsuo_session_verify=dc382781070430f246e973480e1f6234; PHPSESSID=8u7pmn360qn26boor9aqrpc2c7")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .maxBodySize(1000000000)
                .execute();
        byte by[]=doc2.bodyAsBytes();
        System.out.println(by.length);
        OutputStream outputStream=new FileOutputStream("C:\\Users\\13434\\Desktop\\20180525100616_83084.zip",true);
        outputStream.write(by);
        outputStream.flush();
        outputStream.close();
    }
}
