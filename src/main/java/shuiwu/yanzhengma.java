package shuiwu;

import Utils.Dup;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class yanzhengma {
    private static String hexStr =  "0123456789ABCDEF";

    public static String shibie(String img){
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
        String result = null;
        int t=0;
        while (true) {
            try {
                doc = Jsoup.connect("http://apib.sz789.net:88/RecvByte.ashx")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .data("username", "fleashese")
                        .data("password", "849915")
                        .data("softId", "61778")
                        .data("imgdata", da)
                        .post();
                String json= Dup.qujson(doc);
                JSONObject jsonObject=new JSONObject(json);
                result=jsonObject.getString("result");
                if(Dup.nullor(result)) {
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("shibie cuowu");
            }
            t++;
            if(t>=10){
                break;
            }
        }


        return result;
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
}
