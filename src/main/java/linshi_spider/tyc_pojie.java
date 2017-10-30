package linshi_spider;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/23.
 */
public class tyc_pojie {
    public static void main(String args[]) throws IOException {
        getutm();
    }

    public static void getutm() throws IOException {
        Document doc= Jsoup.connect("http://static.tianyancha.com/wap/resources/scripts/app-7476398ced.js")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .get();
        Pattern pat=Pattern.compile("window.\\$SoGou\\$=.+?]");
        Matcher mat=pat.matcher(doc.outerHtml());
        String[] st = null;
        while (mat.find()){
            st=mat.group(0).replace("window.$SoGou$=[","").replace("]","").replace("\"","").split(",");
        }

        String[] chars="33,102,117,110,99,116,105,111,110,40,110,41,123,100,111,99,117,109,101,110,116,46,99,111,111,107,105,101,61,39,116,111,107,101,110,61,48,52,51,100,97,54,102,56,49,99,54,49,52,98,54,48,98,101,49,48,51,50,54,48,99,52,50,102,99,50,100,53,59,112,97,116,104,61,47,59,39,59,110,46,119,116,102,61,102,117,110,99,116,105,111,110,40,41,123,114,101,116,117,114,110,39,50,44,50,44,55,44,49,53,44,51,51,44,50,56,44,51,52,44,50,50,44,51,51,44,51,52,44,49,53,44,54,44,49,55,44,51,44,51,52,44,49,53,44,54,44,49,57,44,50,50,44,55,44,50,44,49,44,50,56,44,55,44,55,44,49,55,44,49,50,44,50,56,44,50,56,44,51,52,44,49,55,44,49,50,39,125,125,40,119,105,110,100,111,119,41,59".split(",");
        StringBuffer str=new StringBuffer();
        for(int i=0;i<chars.length;i++){
            str.append((char)Integer.parseInt(chars[i]));
        }
        String a=str.toString();

        String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");

        String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");

        String[] codes = code.split(",");

        StringBuffer stp=new StringBuffer();
        for (int bb = 0; bb < codes.length; bb++) {
            stp.append(st[Integer.parseInt(codes[bb])]);
        }
        System.out.println(code);
        System.out.println(stp.toString());
        System.out.println(token);




    }
}
