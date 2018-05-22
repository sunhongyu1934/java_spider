package tianyancha.yanzhengma;

import Utils.Dup;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.misc.BASE64Decoder;
import tianyancha.XinxiXin.XinxiXin;
import tianyancha.XinxiXin.loginpool;
import tianyancha.XinxiXin.send_mail;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

public class DownloadImgne {
    private static String hexStr =  "0123456789ABCDEF";

    public static void shibie(String cookie) throws IOException, InterruptedException {
        Map<String,String> mapcookie=new HashMap();
        new JSONObject(cookie.split("######")[1]).toMap().entrySet().stream().forEach(n -> mapcookie.put(n.getKey(),n.getValue().toString()));


        String img1="imgs/img"+cookie.split("######")[0]+"1.jpg";
        String img2="imgs/img"+cookie.split("######")[0]+"2.jpg";
        String img="imgs/img"+cookie.split("######")[0]+".jpg";
        String id=DownloadImgne.getimg(mapcookie,img1,img2);
        String[] imgs={img2,img1};
        DownloadImgne.merge(imgs,"jpg", img);

        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(img));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }

        String da=BinaryToHexString(data);
        Document doc =null;
        while (true) {
            try {
                doc = Jsoup.connect("http://apib.sz789.net:88/RecvByte.ashx")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("username", "fleashese")
                        .data("password", "849915")
                        .data("softId", "61363")
                        .data("imgdata", da)
                        .post();
                break;
            }catch (Exception e){
                System.out.println("shibie cuowu");
            }
        }
        String json= Dup.qujson(doc);
        JSONObject jsonObject=new JSONObject(json);
        String[] results=jsonObject.getString("result").split(";");
        List<Map<String,String>> list=new ArrayList<>();
        for(String result:results){
            Map<String,String> map=new HashMap<>();
            map.put("%22x%22"+":"+String.valueOf(Integer.parseInt(result.split(",")[0])-5),"%22y%22"+":"+String.valueOf(Integer.parseInt(result.split(",")[1])-33));
            list.add(map);
        }
        String zuobiao=list.toString().replace("=",",").replace(" ","");
        System.out.println(zuobiao);
        System.out.println(id);
        yanzheng(zuobiao,id,mapcookie);

    }

    public static void yanzheng(String zuobiao,String id,Map<String,String> cookie) throws IOException, InterruptedException {
        long time=new Date().getTime();
        String url="https://antirobot.tianyancha.com/captcha/checkCaptcha.json?captchaId="+id.split("###")[0]+"&clickLocs="+zuobiao+"&t="+time+"&_="+String.valueOf(Long.parseLong(id.split("###")[1])-1);
        System.out.println(url);
        Document doc =null;
        while (true) {
            try {
                String ip = loginpool.c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .cookies(cookie)
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "antirobot.tianyancha.com")
                        .header("Referer", "https://antirobot.tianyancha.com/captcha/verify?return_url=https%3A%2F%2Fwww.tianyancha.com%2Fsearch%3Fkey%3D%25E5%25B0%258F%25E7%25B1%25B3&rnd=")
                        .header("X-CSRFToken", "null")
                        .get();
                if (!doc.outerHtml().contains("请输入验证码") && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("访问拒绝") && !doc.outerHtml().contains("abuyun") && !doc.outerHtml().contains("Unauthorized") && !doc.outerHtml().contains("访问禁止") && !doc.outerHtml().contains("503 Service Temporarily Unavailable") && !doc.outerHtml().contains("too many request")) {
                    if (!loginpool.c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            loginpool.c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        System.out.println(doc.outerHtml());
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

    public static String getimg(Map<String,String> map,String img1,String img2) throws InterruptedException, IOException {
        long time=new Date().getTime();
        long time2=time-52;
        String url="https://antirobot.tianyancha.com/captcha/getCaptcha.json?t="+time+"&_="+time2;
        Document doc=null;
        while (true) {
            try {
                String ip=loginpool.c.qu();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .proxy(ip.split(":", 2)[0], Integer.parseInt(ip.split(":", 2)[1]))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .cookies(map)
                        .get();
                if (!doc.outerHtml().contains("请输入验证码")&& doc.outerHtml().length()>50&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")&&!doc.outerHtml().contains("503 Service Temporarily Unavailable")&&!doc.outerHtml().contains("too many request")) {
                    if (!loginpool.c.po.contains(ip)) {
                        for (int x = 1; x <= 10; x++) {
                            loginpool.c.fang(ip);
                        }
                    }
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        String json= Dup.qujson(doc);
        JSONObject jsonObject=new JSONObject(json);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        String bgurl=jsonObject1.getString("bgImage");
        String taurl=jsonObject1.getString("targetImage");
        String id=jsonObject1.getString("id");
        download(bgurl,img1);
        download(taurl,img2);
        return id+"###"+time2;
    }

    public static void download(String url,String name) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(url);
        OutputStream outputStream=new FileOutputStream(name);
        outputStream.write(b);
        outputStream.close();
    }


    public static boolean merge(String[] imgs, String type, String dst_pic) {
        //获取需要拼接的图片长度
        int len = imgs.length;
        //判断长度是否大于0
        if (len < 1) {
            return false;
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(imgs[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            // 从图片中读取RGB 像素
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height,  ImageArrays[i], 0, width);
        }

        int dst_height = 0;
        int dst_width = images[0].getWidth();
        //合成图片像素
        for (int i = 0; i < images.length; i++) {
            dst_width = dst_width > images[i].getWidth() ? dst_width     : images[i].getWidth();
            dst_height += images[i].getHeight();
        }
        //合成后的图片
        /*System.out.println("宽度:"+dst_width);
        System.out.println("高度:"+dst_height);*/
        if (dst_height < 1) {
            System.out.println("dst_height < 1");
            return false;
        }
        // 生成新图片
        try {
            // dst_width = images[0].getWidth();
            BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
                    BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            for (int i = 0; i < images.length; i++) {
                ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),
                        ImageArrays[i], 0, dst_width);
                height_i += images[i].getHeight();
            }

            File outFile = new File(dst_pic);
            ImageIO.write(ImageNew, type, outFile);// 写图片 ，输出到硬盘
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
