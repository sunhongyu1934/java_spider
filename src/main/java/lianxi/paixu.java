package lianxi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/24.
 */
public class paixu {
    public static void main(String args[]){
        List<Integer> list=new ArrayList<Integer>();
        DecimalFormat decimalFormat=new DecimalFormat(".000");
        Random r=new Random();
        for(int x=1;x<=10000;x++){
            list.add(r.nextInt(100));
        }
        //System.out.println(list);
        long a=System.currentTimeMillis();
        shengxusan(list);
        long b=System.currentTimeMillis();
        float c=(float)(b-a)/1000;
        if(c<1){
            decimalFormat=new DecimalFormat("0.000");
        }

        System.out.println(decimalFormat.format(c)+"s");
       // System.out.println(list);
    }

    public static void shengxu(List<Integer> list){
        for(int x=0;x<list.size()-1;x++){
            for(int y=x+1;y<list.size();y++){
                int temp;
                if(list.get(x)>list.get(y)){
                    temp=list.get(x);
                    list.set(x,list.get(y));
                    list.set(y,temp);
                }
            }
        }
        System.out.println(list);
    }

    public static void shengxuer(List<Integer> list){
        for(int x=1;x<list.size();x++){
            for(int y=0;y<x;y++) {
                int temp;
                if (list.get(x) < list.get(y)) {
                    temp = list.get(x);
                    list.set(x, list.get(y));
                    list.set(y, temp);
                }
            }
        }
        System.out.println(list);
    }

    public static void shengxusan(List<Integer> list){
        for(int x=0;x<list.size()-1;x++){
            for(int y=0;y<list.size()-x-1;y++){
                int temp;
                if (list.get(y) > list.get(y+1)) {
                    temp = list.get(y);
                    list.set(y, list.get(y+1));
                    list.set(y+1, temp);
                }
            }
        }
        //System.out.println(list);
    }
}