package tianyancha.XinxiXin;

import java.util.*;

/**
 * Created by Administrator on 2017/8/4.
 */
public class paozidian {
    public static void main(String args[]){

    }

    public static void jie(){
        Scanner scanner = new Scanner(System.in);
        Set<String> se = new HashSet<String>();
        while (true) {
            String aa = scanner.next();
            String[] chars = aa.split(",");
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                str.append((char) Integer.parseInt(chars[i]));
            }
            String a = str.toString();
            String token = a.split(";", 2)[0].replace("!function(n){document.cookie='token=", "").replace(";", "");
            String code = a.split("\\{", 2)[1].split("\\{", 2)[1].replace("return'", "").replace("'}}(window);", "").replace("\\{", "");
            String utm = scanner.next();
            for (int x = 0; x < utm.length(); x++) {
                se.add(code.split(",")[x] + "***" + utm.substring(x, x + 1));
            }
            System.out.println(se);
            if(se.size()>=34){
                break;
            }
        }
    }

    public static List<String> suan(int f){
        String o="f9D1x1Z2o1U2f5A1a1P1i7R1u2S1m1F1,o2A1x2F1u5~j1Y2z3!p2~r3G2m8S1c1,i3E5o1~d2!y2H1e2F1b6`g4v7,p1`t7D3x5#w2~l2Z1v4Y1k4M1n1,C2e3P1r7!s6U2n2~p5X1e3#,g4`b6W1x4R1r4#!u5!#D1f2,!z4U1f4`f2R2o3!l4I1v6F1h2F1x2!,b2~u9h2K1l3X2y9#B4t1,t5H1s7D1o2#p2#z1Q3v2`j6,r1#u5#f1Z2w7!r7#j3S1";
        String i="2633141825201321121345332721524273528936811101916293117022304236|1831735156281312241132340102520529171363214283321272634162219930|2332353860219720155312141629130102234183691124281413251227261733|2592811262018293062732141927100364232411333831161535317211222534|9715232833130331019112512913172124126035262343627321642220185148|3316362031032192529235212215274341412306269813312817111724201835|3293412148301016132183119242311021281920736172527353261533526224|3236623313013201625221912357142415851018341117262721294332103928|2619332514511302724163415617234183291312001227928218353622321031|3111952725113022716818421512203433241091723133635282932601432216";
        String ii=i.split("\\|")[f];
        String oo=o.split(",")[f];
        String[] ooo=oo.split("|");
        int s=0;
        String basechars="abcdefghijklmnopqrstuvwxyz1234567890-~!";
        List<String> list=new ArrayList<String>();
        for(int u=0;u<ooo.length;u++){
            if(!"`".equals(ooo[u])&&!"!".equals(ooo[u])&&!"~".equals(ooo[u])){

            }else{
                list.add(ii.substring(s, s + 1));
                s++;
            }
            if("#".equals(ooo[u])){
                list.add(ii.substring(s, s + 1));
                list.add(ii.substring(s+1, s + 3));
                list.add(ii.substring(s+3, s + 4));
                s+=4;
            }
            if(ooo[u].codePointAt(0)>96&&ooo[u].codePointAt(0)<123){
                int l= Integer.parseInt(ooo[u+1]);
                for(int c=0;c<l;c++){
                    list.add(ii.substring(s, s + 2));
                    s+=2;
                }
            }
            if(ooo[u].codePointAt(0)>64&&ooo[u].codePointAt(0)<91){
                int l= Integer.parseInt(oo.substring(u+1,u+2));
                for(int c=0;c<l;c++){
                    list.add(ii.substring(s, s + 1));
                    s++;
                }
            }
        }
        List<String> list2=new ArrayList<String>();
        for(int y=0;y<list.size();y++){
            list2.add(basechars.substring(Integer.parseInt(list.get(y)), Integer.parseInt(list.get(y))+1));
        }
        return list2;
    }

    public static void zidong(){

    }
}
