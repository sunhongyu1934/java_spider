package test;

/**
 * Created by Administrator on 2017/4/6.
 */
public class G_Code {
    public static char[] a = new char[8];

    public String jy(char[] a) {
        int[] ww = { 3, 7, 9, 10, 5, 8, 4, 2 };

        int[] cc = new int[8];
        int DD = 0;
        int C9 = 0;

        for (int i = 0; i < 8; i++) {
            cc[i] = a[i];
            if (47 < cc[i] && cc[i] < 58)
                cc[i] = cc[i] - 48;
            else
                cc[i] = cc[i] - 65;
        }
        for (int i = 0; i < 8; i++) {
            DD += cc[i] * ww[i];
        }
        C9 = 11 - DD % 11;
        if (C9 == 10) {
            for (int i = 0; i < 8; i++)
                System.out.print(G_Code.a[i]);
            System.out.println("-X");
            return new String(a) + "-X";
        } else if (C9 == 11) {
            for (int i = 0; i < 8; i++)
                System.out.print(G_Code.a[i]);
            System.out.println("-" + (char) (48));
            return new String(a) + "-" + (char) (48);
        } else {
            for (int i = 0; i < 8; i++)
                System.out.print(G_Code.a[i]);
            System.out.println("-" + (char) (C9 + 48));
            return new String(a) + "-" + (char) (C9 + 48);
        }

    }
    /*
     * public static void main(String[] args){ try{
     *
     * System.out.println("请输入长8位的本体代码！"); for(int i=0;i<8;i++){
     *
     *
     * G_Code.a[i] =(char)System.in.read(); } }catch (IOException e) { } G_Code
     * G=new G_Code(); G.jy(a);
     *
     * }
     */
}
