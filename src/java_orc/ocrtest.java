package java_orc;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/10.
 */
public class ocrtest {

    public static void main(String[] args) {
        String path = "C:\\Users\\Administrator\\Desktop\\test\\ppp.tif";
        System.out.println("ORC Test Begin......");
        try {
            String valCode = new OCR().recognizeText(new File(path), "tif");
            System.out.println(valCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ORC Test End......");
    }

}
