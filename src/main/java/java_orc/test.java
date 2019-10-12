package java_orc;


import Utils.Dup;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tianyancha.yanzhengma.DownloadImgne.BinaryToHexString;

public class test {
    static int a=0;
    public static void main(String args[]) throws IOException {
        File files=new File("C:\\Users\\13434\\Desktop\\train");
        ExecutorService pool=Executors.newFixedThreadPool(20);
        for(File file:files.listFiles()){
            int xuhao=Integer.parseInt(file.getName().replace(".jpg",""));
            if(xuhao>=1&&xuhao<=400){
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String yan = shibie(file.getPath()).toLowerCase();
                            InputStream inputStream = new FileInputStream(file.getPath());
                            OutputStream outputStream = new FileOutputStream("C:\\Users\\13434\\Desktop\\train3\\" + yan + ".jpg");
                            byte[] bytes = new byte[1024];
                            int len = -1;
                            while ((len = inputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                            }
                            inputStream.close();
                            outputStream.close();
                            a++;
                            System.out.println(a + "*************");
                        }catch (Exception e){
                            System.out.println("error=============================================================================="+xuhao);
                        }
                    }
                });
            }
        }
    }

    public static String shibie(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
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
                        .data("password", "innotree_dama")
                        .data("softId", "61778")
                        .data("imgdata", da)
                        .post();
                break;
            }catch (Exception e){
                System.out.println("shibie cuowu");
            }
        }
        String json= Dup.qujson(doc);
        JSONObject jsonObject=new JSONObject(json);
        return jsonObject.getString("result");
    }
}
