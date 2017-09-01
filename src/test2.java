import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */
public class test2 {
    private static Logger logger1 = Logger.getLogger("logger1");
    private static Logger logger2 = Logger.getLogger("logger2");

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        String json="{\"requestModule\":\"PatentSearch\",\"userId\":\"\",\"patentSearchConfig\":{\"Query\":\"PA=HUAWEI\",\"TreeQuery\":\"\",\"Database\":\"nzpat,aupat,ttpat,dopat,copat,crpat,svpat,nipat,hnpat,gtpat,ecpat,papat,arpat,mxpat,capat,cupat,pepat,clpat,brpat,appat,oapat,zwpat,dzpat,tnpat,zmpat,mwpat,mapat,kepat,egpat,zapat,gcpat,uzpat,kzpat,tjpat,kgpat,idpat,mypat,ampat,trpat,phpat,sgpat,ilpat,mnpat,thpat,vnpat,jopat,inpat,inapp,bapat,ddpat,cspat,eapat,mepat,sipat,bypat,bgpat,cypat,eepat,gepat,hrpat,lvpat,mdpat,rspat,ropat,smpat,skpat,yupat,supat,ltpat,lupat,mcpat,mtpat,ptpat,atpat,uypat,uapat,espat,itpat,hupat,bepat,iepat,sepat,plpat,nlpat,fipat,nopat,czpat,grpat,dkpat,ispat,chpat,frpat,deuti,depat,deapp,kruti,krpat,krapp,jputi,jppat,jpapp,wopat,usdes,uspat,uspat1,usapp,gbpat,eppat,epapp,rupat\",\"Action\":\"Search\",\"DBOnly\":0,\"Page\":\"300\",\"PageSize\":\"10\",\"GUID\":\"4409d92326704d86b6ba3619128f4531\",\"Sortby\":\"-IDX,PNM\",\"AddOnes\":\"\",\"DelOnes\":\"\",\"RemoveOnes\":\"\",\"Verification\":null,\"SmartSearch\":\"\",\"TrsField\":\"\"}}";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://www.innojoy.com/client/interface.aspx");
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");
        post.setEntity(entity);
        post.addHeader("Host", "www.innojoy.com");
        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.addHeader("userAgent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        post.addHeader("X-Requested-With", "XMLHttpRequest");
        post.addHeader("Accept-Language", "en-us");
        post.addHeader("Origin", "http://www.innojoy.com");
        post.addHeader("Referer", "http://www.innojoy.com/search/tablesearch.html");
        List list=new ArrayList();
       //list.add(new BasicNameValuePair("username", "vip"));
       // post.setEntity(new UrlEncodedFormEntity(list));
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }


        /*String cookies="access_token=42606e47635a74249c379148100fd927; channel=AppStore; u=37820408; version=4.15.0;";
        Map map=new HashMap();
        map.put("access_token","42606e47635a74249c379148100fd927;");
        map.put("channel","AppStore;");
        map.put("u","37820408;");
        map.put("version","4.15.0;");

        Document doc=Jsoup.connect("https://maimai.cn/web/search_center?type=feed&query=%E5%9B%A0%E6%9E%9C%E6%A0%91&highlight=true")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .followRedirects(false)
                .method(Connection.Method.GET)
                .cookies(map)
                .ignoreContentType(true)
                .get();
        System.out.println(doc.outerHtml());*/




    }

}
