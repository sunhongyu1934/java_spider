package etl.itjz_etl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */
public class test {
    private static List<String> list=new ArrayList<String>();
    public static void main(String args[]) throws IOException {
        LinkedList<String> lList = new LinkedList<String>();
        lList.add("1");
        lList.add("2");
        lList.add("3");
        lList.add("4");
        lList.add("5");
       String a=lList.get(0);
        lList.remove(0);
        System.out.println(a);
        System.out.println(lList.get(0));

        //List<String> list=new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        String b=list.get(0);
        list.clear();
        System.out.println(b);
//        System.out.println(list.get(0));
    }
}
