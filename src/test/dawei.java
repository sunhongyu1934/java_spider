package test;

import Dw.Dw;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/28.
 */
public class dawei {
    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IOException, IllegalAccessException {



        HttpHost proxy = new HttpHost("proxy.abuyun.com",9020,"http");

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //创建认证，并设置认证范围

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(new AuthScope("proxy.abuyun.com",9020),new UsernamePasswordCredentials("H112205236B5G2PD", "E9484DB291BFC579"));



        HttpClientBuilder builder = HttpClients.custom();

        builder.setRoutePlanner(routePlanner);

        builder.setDefaultCredentialsProvider(credsProvider);

        CloseableHttpClient httpclient = builder.build();

        get(httpclient);


    }

    public static String qq(String name,CloseableHttpClient httpclient) throws IOException {

        String json = "{\"requestModule\":\"PatentSearch\",\"userId\":\"\",\"patentSearchConfig\":{\"Query\":\"" + name + "\",\"TreeQuery\":\"\",\"Database\":\"nzpat,aupat,ttpat,dopat,copat,crpat,svpat,nipat,hnpat,gtpat,ecpat,papat,arpat,mxpat,capat,cupat,pepat,clpat,brpat,appat,oapat,zwpat,dzpat,tnpat,zmpat,mwpat,mapat,kepat,egpat,zapat,gcpat,uzpat,kzpat,tjpat,kgpat,idpat,mypat,ampat,trpat,phpat,sgpat,ilpat,mnpat,thpat,vnpat,jopat,inpat,inapp,bapat,ddpat,cspat,eapat,mepat,sipat,bypat,bgpat,cypat,eepat,gepat,hrpat,lvpat,mdpat,rspat,ropat,smpat,skpat,yupat,supat,ltpat,lupat,mcpat,mtpat,ptpat,atpat,uypat,uapat,espat,itpat,hupat,bepat,iepat,sepat,plpat,nlpat,fipat,nopat,czpat,grpat,dkpat,ispat,chpat,frpat,deuti,depat,deapp,kruti,krpat,krapp,jputi,jppat,jpapp,wopat,usdes,uspat,uspat1,usapp,gbpat,eppat,epapp,rupat,hkpat,twpat,fmsq,wgzl,syxx,fmzl\",\"Action\":\"Search\",\"DBOnly\":0,\"Page\":\"" + "1" + "\",\"PageSize\":\"10\",\"GUID\":\"4409d92326704d86b6ba3619128f4531\",\"Sortby\":\"-IDX,PNM\",\"AddOnes\":\"\",\"DelOnes\":\"\",\"RemoveOnes\":\"\",\"Verification\":null,\"SmartSearch\":\"\",\"TrsField\":\"\"}}";
        HttpPost post = new HttpPost("http://www.innojoy.com/client/interface.aspx");
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");
        post.setEntity(entity);
        post.addHeader("Host", "www.innojoy.com");
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        post.addHeader("Accept-Language", "en-us");
        post.addHeader("Origin", "http://www.innojoy.com");
        post.addHeader("Referer", "http://www.innojoy.com/search/tablesearch.html");


        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity resEntity = response.getEntity();
        String sj= EntityUtils.toString(resEntity);
        return sj;
    }

    public static void get(CloseableHttpClient httpclient) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://112.126.86.232:3306/innotree_spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="dev";
        String password="innotree123!@#";
        Class.forName(driver1).newInstance();
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                con = DriverManager.getConnection(url1, username, password);
                if(con!=null){
                    break;
                }
            }
        }

        String update="update com_en_cn set cname=? where id=?";
        PreparedStatement pp=con.prepareStatement(update);


        String select="select id,assignee from com_en_cn where `cname`='1'";
        PreparedStatement ps=con.prepareStatement(select);
        ResultSet rs=ps.executeQuery();
        int a=1;
        int i=1;
        boolean b=false;
        while(rs.next()){
            String assignee=rs.getString(rs.findColumn("assignee")).replace("(","").replace(")","");
            String id=rs.getString(rs.findColumn("id"));
            try {
                System.out.println(assignee);
                String json = qq(assignee,httpclient);
                Gson gson = new Gson();
                Dw dw = gson.fromJson(json, Dw.class);
                System.out.println(json);
                for (int x = 0; x < dw.Option.PatentList.size(); x++) {
                    String name = dw.Option.PatentList.get(x).AS;
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(name);
                    if (m.find()) {
                        b = true;
                        pp.setString(1, name);
                        pp.setString(2, id);
                        pp.executeUpdate();
                        System.out.println("成功：" + a + "条" + "    " + "共：" + i + "条");
                        a++;
                        System.out.println("---------------------------------");
                        break;
                    }
                }
                if (!b) {
                    pp.setString(1, "null");
                    pp.setString(2, id);
                    pp.executeUpdate();
                }
                System.out.println("当前第：" + i + "条");
                i++;
                System.out.println("************************************");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
                pp.setString(1, "1");
                pp.setString(2, id);
                pp.executeUpdate();
            }
        }



    }
}
