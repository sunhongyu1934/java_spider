package Dw;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/3/13.
 */
public class spiderdw {
    public static void main(String args[]) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        ExecutorService pool= Executors.newFixedThreadPool(5);
        int x=0;
        for(int a=1;a<=5;a++){
            final int finalX = x;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        spider(finalX);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            x=x+30000;
        }
    }

    public static void spider(int o) throws IOException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://101.200.161.221:3306/patent?useUnicode=true&useCursorFetch=true&defaultFetchSize=100?useUnicode=true&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="tech_spider";
        String password="sPiDer$#@!23";
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

        MongoClient client = null;
        ServerAddress serverAddress = new ServerAddress("123.57.217.48",27017);
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        seeds.add(serverAddress);
        MongoCredential credentials = MongoCredential.createScramSha1Credential("root", "admin", "innotree_mongodb".toCharArray());
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(credentials);
        client = new MongoClient(seeds, credentialsList);
        MongoDatabase db = client.getDatabase("Dw");
        MongoCollection collection = db.getCollection("dw");






        String sql="insert into top_patent2(cid,page,`NO`,RNO,FTOTAL,FID,APNID,FP,DN,KC,AN,AD,PNM,PD,LD,TI,ABST,CL,PA,`AS`,AR,INN,PR,CO,DAN,AGC,AGT,SIC,PIC,CPC,CTGC,IAN,IPN,PAT,DEN,PP,MP,DPN,CLPN,DEPN,DRPN,LV,LS,LLS,LLSD,YXX,DB,DBName,SICN,GRNT,ZYFTLJ,REFP,CD,PAN,INNN,IFIID,SNC,FTERM,SEC,GZSX,FMLN,BYZS,CHQ,IDX,PATMS,INNTMS,PL,ISNEWDATA,CLMN,REFBYN,REFPN) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);

        String select="select id,cid,ename,page from top_ppage WHERE mongo_id='' and id<'150000' limit "+o+",30000";
        PreparedStatement pss=con.prepareStatement(select);

        String update="update top_ppage set mongo_id=?,guid=? where id=?";
        PreparedStatement psu=con.prepareStatement(update);


        ResultSet rs=pss.executeQuery();
        int i=1;
        int y=1;
        int flag = 0;
        System.out.println("开始遍历数据库");
        String ip[]=null;
        Document docr= Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1").get();
        ip=docr.body().toString().replace("<body>","").replace("</body>","").trim().split(" ");

        while(rs.next()){
            try {
                    String ename = rs.getString(rs.findColumn("ename"));
                    String cid = rs.getString(rs.findColumn("cid"));
                    String page = rs.getString(rs.findColumn("page"));
                    String id = rs.getString(rs.findColumn("id"));

                    String json = "{\"requestModule\":\"PatentSearch\",\"userId\":\"\",\"patentSearchConfig\":{\"Query\":\"PA=" + ename + "\",\"TreeQuery\":\"\",\"Database\":\"nzpat,aupat,ttpat,dopat,copat,crpat,svpat,nipat,hnpat,gtpat,ecpat,papat,arpat,mxpat,capat,cupat,pepat,clpat,brpat,appat,oapat,zwpat,dzpat,tnpat,zmpat,mwpat,mapat,kepat,egpat,zapat,gcpat,uzpat,kzpat,tjpat,kgpat,idpat,mypat,ampat,trpat,phpat,sgpat,ilpat,mnpat,thpat,vnpat,jopat,inpat,inapp,bapat,ddpat,cspat,eapat,mepat,sipat,bypat,bgpat,cypat,eepat,gepat,hrpat,lvpat,mdpat,rspat,ropat,smpat,skpat,yupat,supat,ltpat,lupat,mcpat,mtpat,ptpat,atpat,uypat,uapat,espat,itpat,hupat,bepat,iepat,sepat,plpat,nlpat,fipat,nopat,czpat,grpat,dkpat,ispat,chpat,frpat,deuti,depat,deapp,kruti,krpat,krapp,jputi,jppat,jpapp,wopat,usdes,uspat,uspat1,usapp,gbpat,eppat,epapp,rupat\",\"Action\":\"Search\",\"DBOnly\":0,\"Page\":\"" + page + "\",\"PageSize\":\"10\",\"GUID\":\"4409d92326704d86b6ba3619128f4531\",\"Sortby\":\"-IDX,PNM\",\"AddOnes\":\"\",\"DelOnes\":\"\",\"RemoveOnes\":\"\",\"Verification\":null,\"SmartSearch\":\"\",\"TrsField\":\"\"}}";
                    if (flag >= (ip.length - 5)) {
                        Document doct = Jsoup.connect("http://dev.kuaidaili.com/api/getproxy/?orderid=957600066232834&num=300&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp1=1&quality=1&sep=1").get();
                        ip = doct.body().toString().replace("<body>", "").replace("</body>", "").trim().split(" ");
                        flag=0;
                    }


                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    // 依次是代理地址，代理端口号，协议类型
                    HttpHost proxy = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                    // httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                    HttpPost post = new HttpPost("http://www.innojoy.com/client/interface.aspx");
                    //post.setConfig(config);
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
            /*List list=new ArrayList();
            list.add(new BasicNameValuePair("username", "vip"));
             post.setEntity(new UrlEncodedFormEntity(list));*/
                    System.out.println("开始请求");
                    CloseableHttpResponse response = null;
                    try {
                        response = httpclient.execute(post);
                    } catch (Exception e) {
                        while (true) {
                            System.out.println("获取连接失败，重新获取");
                            HttpHost proxy1 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                            RequestConfig config1 = RequestConfig.custom().setProxy(proxy1).build();
                            post.setConfig(config1);
                            flag++;
                            try {
                                response = httpclient.execute(post);
                            } catch (Exception e1) {
                                System.out.println("re");
                            }
                            break;
                        }
                    }
                    System.out.println("获取数据");
                    HttpEntity resEntity = null;
                    resEntity = response.getEntity();
                    if (resEntity != null) {
                        String tag = null;
                        try {
                            tag = EntityUtils.toString(resEntity);
                        } catch (Exception e5) {
                            while (true) {
                                System.out.println("获取数据失败，重新获取");
                                HttpHost proxy1 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                RequestConfig config1 = RequestConfig.custom().setProxy(proxy1).build();
                                post.setConfig(config1);
                                flag++;
                                try {
                                    response = httpclient.execute(post);
                                } catch (Exception e1) {
                                    while (true) {
                                        System.out.println("获取连接失败，重新获取");
                                        HttpHost proxy2 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                        RequestConfig config2 = RequestConfig.custom().setProxy(proxy2).build();
                                        post.setConfig(config2);
                                        flag++;
                                        try {
                                            response = httpclient.execute(post);
                                        } catch (Exception e3) {
                                            System.out.println("re");
                                        }
                                        break;
                                    }
                                }
                                resEntity = response.getEntity();
                                try {
                                    tag = EntityUtils.toString(resEntity);
                                } catch (Exception e0) {
                                    System.out.println("tag");
                                }
                                if (tag.contains("REFPN")) {
                                    break;
                                }
                            }
                        }
                        if (!tag.contains("REFPN")) {
                            while (true) {
                                System.out.println("获取数据失败，重新获取");
                                HttpHost proxy1 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                RequestConfig config1 = RequestConfig.custom().setProxy(proxy1).build();
                                post.setConfig(config1);
                                flag++;
                                try {
                                    response = httpclient.execute(post);
                                } catch (Exception e1) {
                                    while (true) {
                                        System.out.println("获取连接失败，重新获取");
                                        HttpHost proxy2 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                        RequestConfig config2 = RequestConfig.custom().setProxy(proxy2).build();
                                        post.setConfig(config2);
                                        flag++;
                                        try {
                                            response = httpclient.execute(post);
                                        } catch (Exception e3) {
                                            System.out.println("re");
                                        }
                                        break;
                                    }
                                }
                                resEntity = response.getEntity();
                                try {
                                    tag = EntityUtils.toString(resEntity);
                                } catch (Exception e5) {
                                    while (true) {
                                        System.out.println("获取数据失败，重新获取");
                                        HttpHost proxy0 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                        RequestConfig config0 = RequestConfig.custom().setProxy(proxy0).build();
                                        post.setConfig(config0);
                                        flag++;
                                        try {
                                            response = httpclient.execute(post);
                                        } catch (Exception e1) {
                                            while (true) {
                                                System.out.println("获取连接失败，重新获取");
                                                HttpHost proxy2 = new HttpHost(ip[flag].split(":", 2)[0], Integer.parseInt(ip[flag].split(":", 2)[1]), "HTTP");
                                                RequestConfig config2 = RequestConfig.custom().setProxy(proxy2).build();
                                                post.setConfig(config2);
                                                flag++;
                                                try {
                                                    response = httpclient.execute(post);
                                                } catch (Exception e3) {
                                                    System.out.println("re");
                                                }
                                                break;
                                            }
                                        }
                                        resEntity = response.getEntity();
                                        try {
                                            tag = EntityUtils.toString(resEntity);
                                        } catch (Exception e0) {
                                            System.out.println("tag");
                                        }
                                        if (tag.contains("REFPN")) {
                                            break;
                                        }
                                    }
                                }
                                if (tag.contains("REFPN")) {
                                    break;
                                }
                            }
                        }
                        System.out.println(tag);
                        Gson gson = new Gson();
                        Dw dw = gson.fromJson(tag, Dw.class);
                        String guuid = null;
                        try {
                            guuid = dw.Option.GUID;
                        } catch (Exception e) {
                            guuid = null;
                        }
                        String uuid = UUID.randomUUID().toString();
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = simpleDateFormat.format(date);
                        System.out.println("开始解析并入库");
                        for (int x = 0; x < dw.Option.PatentList.size(); x++) {
                            ps.setString(1, cid);
                            ps.setString(2, page);
                            ps.setString(3, dw.Option.PatentList.get(x).NO);
                            ps.setString(4, dw.Option.PatentList.get(x).RNO);
                            ps.setString(5, dw.Option.PatentList.get(x).FTOTAL);
                            ps.setString(6, dw.Option.PatentList.get(x).FID);
                            ps.setString(7, dw.Option.PatentList.get(x).APNID);
                            ps.setString(8, dw.Option.PatentList.get(x).FP);
                            ps.setString(9, dw.Option.PatentList.get(x).DN);
                            ps.setString(10, dw.Option.PatentList.get(x).KC);
                            ps.setString(11, dw.Option.PatentList.get(x).AN);
                            ps.setString(12, dw.Option.PatentList.get(x).AD);
                            ps.setString(13, dw.Option.PatentList.get(x).PNM);
                            ps.setString(14, dw.Option.PatentList.get(x).PD);
                            ps.setString(15, dw.Option.PatentList.get(x).LD);
                            ps.setString(16, dw.Option.PatentList.get(x).TI);
                            ps.setString(17, dw.Option.PatentList.get(x).ABST);
                            ps.setString(18, dw.Option.PatentList.get(x).CL);
                            ps.setString(19, dw.Option.PatentList.get(x).PA);
                            ps.setString(20, dw.Option.PatentList.get(x).AS);
                            ps.setString(21, dw.Option.PatentList.get(x).AR);
                            ps.setString(22, dw.Option.PatentList.get(x).INN);
                            ps.setString(23, dw.Option.PatentList.get(x).PR);
                            ps.setString(24, dw.Option.PatentList.get(x).CO);
                            ps.setString(25, dw.Option.PatentList.get(x).DAN);
                            ps.setString(26, dw.Option.PatentList.get(x).AGC);
                            ps.setString(27, dw.Option.PatentList.get(x).AGT);
                            ps.setString(28, dw.Option.PatentList.get(x).SIC);
                            ps.setString(29, dw.Option.PatentList.get(x).PIC);
                            ps.setString(30, dw.Option.PatentList.get(x).CPC);
                            ps.setString(31, dw.Option.PatentList.get(x).CTGC);
                            ps.setString(32, dw.Option.PatentList.get(x).IAN);
                            ps.setString(33, dw.Option.PatentList.get(x).IPN);
                            ps.setString(34, dw.Option.PatentList.get(x).PAT);
                            ps.setString(35, dw.Option.PatentList.get(x).DEN);
                            ps.setString(36, dw.Option.PatentList.get(x).PP);
                            ps.setString(37, dw.Option.PatentList.get(x).MP);
                            ps.setString(38, dw.Option.PatentList.get(x).DPN);
                            ps.setString(39, dw.Option.PatentList.get(x).CLPN);
                            ps.setString(40, dw.Option.PatentList.get(x).DEPN);
                            ps.setString(41, dw.Option.PatentList.get(x).DRPN);
                            ps.setString(42, dw.Option.PatentList.get(x).LV);
                            ps.setString(43, dw.Option.PatentList.get(x).LS);
                            ps.setString(44, dw.Option.PatentList.get(x).LLS);
                            ps.setString(45, dw.Option.PatentList.get(x).LLSD);
                            ps.setString(46, dw.Option.PatentList.get(x).YXX);
                            ps.setString(47, dw.Option.PatentList.get(x).DB);
                            ps.setString(48, dw.Option.PatentList.get(x).DBName);
                            ps.setString(49, dw.Option.PatentList.get(x).SICN);
                            ps.setString(50, dw.Option.PatentList.get(x).CRNT);
                            ps.setString(51, dw.Option.PatentList.get(x).ZYFTLJ);
                            ps.setString(52, dw.Option.PatentList.get(x).REFP);
                            ps.setString(53, dw.Option.PatentList.get(x).CD);
                            ps.setString(54, dw.Option.PatentList.get(x).PAN);
                            ps.setString(55, dw.Option.PatentList.get(x).INNN);
                            ps.setString(56, dw.Option.PatentList.get(x).IFIID);
                            ps.setString(57, dw.Option.PatentList.get(x).SNC);
                            ps.setString(58, dw.Option.PatentList.get(x).FTERM);
                            ps.setString(59, dw.Option.PatentList.get(x).SEC);
                            ps.setString(60, dw.Option.PatentList.get(x).GZSX);
                            ps.setString(61, dw.Option.PatentList.get(x).FMLN);
                            ps.setString(62, dw.Option.PatentList.get(x).BYZS);
                            ps.setString(63, dw.Option.PatentList.get(x).CHQ);
                            ps.setString(64, dw.Option.PatentList.get(x).IDX);
                            ps.setString(65, dw.Option.PatentList.get(x).PATMS);
                            ps.setString(66, dw.Option.PatentList.get(x).INNTMS);
                            ps.setString(67, dw.Option.PatentList.get(x).PL);
                            ps.setString(68, dw.Option.PatentList.get(x).ISNEWDATA);
                            ps.setString(69, dw.Option.PatentList.get(x).CLMN);
                            ps.setString(70, dw.Option.PatentList.get(x).REFBYN);
                            ps.setString(71, dw.Option.PatentList.get(x).REFPN);
                            try {
                                ps.executeUpdate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("当前第：" + i + "条入库成功");
                            System.out.println("当前第" + Thread.currentThread().getName());
                            i++;
                            System.out.println("------------------------------------------------------------");
                        }
                        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(dw));
                        org.bson.Document document = new org.bson.Document("_id", uuid).
                                append("cid", cid).
                                append("json", dbObject).
                                append("time", time);
                        collection.insertOne(document);
                        psu.setString(1, uuid);
                        psu.setString(2, guuid);
                        psu.setString(3, id);
                        psu.executeUpdate();
                    }
                flag++;
            }catch (Exception e00){
                System.out.println("当条异常");
            }
        }

    }
}
