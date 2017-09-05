package lianxi;

import java.io.*;

/**
 * Created by Administrator on 2017/9/5.
 */
public class chuli {
    public static void main(String args[]) throws IOException {
        du();
    }

    public static void du() throws IOException {
        File in=new File("E:\\aaa");
        FileInputStream inp = null;
        InputStreamReader read = null;
        BufferedReader re = null;
        int a=0;
        for(File f:in.listFiles()){
            try {
                if (f.isFile()) {
                    inp = new FileInputStream(f);
                    read = new InputStreamReader(inp, "utf-8");
                    re = new BufferedReader(read);
                    StringBuffer str = new StringBuffer();
                    String info;
                    while (!((info = re.readLine()) == null)) {
                        str.append(info + "\r\n");
                    }
                    String du = str.toString();
                    if (du.contains("jdbc:mysql://etl1.innotree.org:3308/")) {
                        /*Pattern pat = Pattern.compile("public interface.+\\{");
                        Matcher mat = pat.matcher(du);
                        String aa = null;
                        while (mat.find()) {
                            aa = mat.group(0);
                        }*/
                        String bb = "E:\\bbb\\dbcpconfig.properties";
                        xie(du, bb);
                    }
                }
                a++;
                System.out.println(a + "****************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
        inp.close();
        read.close();
        re.close();
    }

    public static void xie(String aa,String th) throws IOException {
        OutputStream out=new FileOutputStream(th);
        OutputStreamWriter ou=new OutputStreamWriter(out,"utf-8");
        ou.write(aa);
        ou.flush();
        ou.close();
        out.close();
    }
}
