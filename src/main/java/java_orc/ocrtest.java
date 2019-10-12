package java_orc;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/10.
 */
public class ocrtest {

    public static void main(String[] args) {
        String path = "C:\\Users\\13434\\Desktop\\train\\23.jpg";
        System.out.println("ORC Test Begin......");
        try {
            String valCode = new OCR().recognizeText(new File(path), "jpg");
            System.out.println("aaa");
            System.out.println(valCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ORC Test End......");
    }

}
