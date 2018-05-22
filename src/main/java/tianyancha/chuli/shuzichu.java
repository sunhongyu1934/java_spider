package tianyancha.chuli;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;
import tianyancha.XinxiXin.XinxiXin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Utils.JsoupUtils.getString;

public class shuzichu {
    public static synchronized void xunlian() throws IOException, InterruptedException, DocumentException {
        SAXReader saxReader=new SAXReader();
        Document doc ;
        try {
            doc = saxReader.read(new FileInputStream("shu.xml"));
            Element root = doc.getRootElement();
            long cur = System.currentTimeMillis();
            if (cur - Long.parseLong(root.element("time").getText()) > 1800000) {
                org.jsoup.nodes.Document doc1 = XinxiXin.detailget("https://www.tianyancha.com/company/3100084741");
                org.jsoup.nodes.Document doc2 = XinxiXin.detailget("https://www.tianyancha.com/company/23402373");
                org.jsoup.nodes.Document doc3 = XinxiXin.detailget("https://www.tianyancha.com/company/29725665");
                String zs1 = getString(doc1, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
                String zs2 = getString(doc2, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
                String zs3 = getString(doc3, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
                String zz2 = getString(doc2, "div.new-border-bottom:contains(注册资本) div.pb10 div.baseinfo-module-content-value", 0);
                String hz2 = getString(doc2, "div.base0910 table tbody td", 16);
                String zz3 = getString(doc3, "div.new-border-bottom:contains(注册资本) div.pb10 div.baseinfo-module-content-value", 0);
                Map<String, String> map = new HashMap<>();
                map.put("0", zs1.substring(1, 2));
                map.put("1", zs1.substring(2, 3));
                map.put("2", zs1.substring(0, 1));
                map.put("3", zs2.substring(6, 7));
                map.put("4", zs3.substring(3, 4));
                map.put("5", zz2.substring(2, 3));
                map.put("6", hz2.substring(3, 4));
                map.put("7", zs1.substring(3, 4));
                map.put("8", zz2.substring(1, 2));
                map.put("9", zs1.substring(6, 7));
                map.put(".", zz3.substring(4, 5));
                getdom(map);
            }
        }catch (Exception e){
            org.jsoup.nodes.Document doc1 = XinxiXin.detailget("https://www.tianyancha.com/company/3100084741");
            org.jsoup.nodes.Document doc2 = XinxiXin.detailget("https://www.tianyancha.com/company/23402373");
            org.jsoup.nodes.Document doc3 = XinxiXin.detailget("https://www.tianyancha.com/company/29725665");
            String zs1 = getString(doc1, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
            String zs2 = getString(doc2, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
            String zs3 = getString(doc3, "div.new-border-bottom:contains(注册时间) div.pb10 div.baseinfo-module-content-value", 1);
            String zz2 = getString(doc2, "div.new-border-bottom:contains(注册资本) div.pb10 div.baseinfo-module-content-value", 0);
            String hz2 = getString(doc2, "div.base0910 table tbody td", 16);
            String zz3 = getString(doc3, "div.new-border-bottom:contains(注册资本) div.pb10 div.baseinfo-module-content-value", 0);
            Map<String, String> map = new HashMap<>();
            map.put("0", zs1.substring(1, 2));
            map.put("1", zs1.substring(2, 3));
            map.put("2", zs1.substring(0, 1));
            map.put("3", zs2.substring(6, 7));
            map.put("4", zs3.substring(3, 4));
            map.put("5", zz2.substring(2, 3));
            map.put("6", hz2.substring(3, 4));
            map.put("7", zs1.substring(3, 4));
            map.put("8", zz2.substring(1, 2));
            map.put("9", zs1.substring(6, 7));
            map.put(".", zz3.substring(4, 5));
            getdom(map);
        }
    }


    public static void getdom(Map<String,String> map) throws IOException {
        Document dom= DocumentHelper.createDocument();
        Element root=dom.addElement("root");
        for(Map.Entry<String,String> entry:map.entrySet()){
            Element shu=root.addElement("shu");
            shu.addAttribute("yuan",entry.getKey());
            shu.setText(entry.getValue());
        }
        Element time=root.addElement("time");
        time.setText(String.valueOf(System.currentTimeMillis()));

        XMLWriter writer = new XMLWriter(new FileOutputStream("shu.xml"));
        writer.write(dom);
        writer.close();
    }
}
