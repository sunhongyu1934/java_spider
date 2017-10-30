package tianyancha;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tianyancha.quanxinxi.Bean;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/4/12.
 */
public class test {
    // 代理隧道验证信息
    final static String ProxyUser = "H741D356Z4WO7V7D";
    final static String ProxyPass = "7F76E3E09C38035C";

    // 代理服务器
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    public static void main(String[] args) throws InterruptedException, IOException {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));



    }

    public static String qingqiu(Proxy proxy,String url){
        Document doc= null;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .ignoreContentType(true)
                        .get();
                if (!doc.outerHtml().contains("abuyun") && StringUtils.isNotEmpty(doc.outerHtml().replace("<html>", "").replace("<body>", "").replace("</html>", "").replace("</body>", "").replace("<head></head>", "").replace(" ", "").trim())) {
                    break;
                }
            }catch (Exception e){
                System.out.println("time out reget");
            }
        }

        return doc.outerHtml().replace("<html>","").replace("</html>","").replace("<head>","").replace("</head>","").replace("<body>","").replace("</body>","").replace(" ","").trim();
    }

     class Dulujing implements Runnable{
        private Lujing lu;
        public Dulujing(Lujing lu){
            this.lu=lu;
        }
        @Override
        public void run() {
            File file=new File("/data1/spider/java_spider/tyc");
            File[] tempList=file.listFiles();
            for(int i=0;i<tempList.length;i++){
                if(tempList[i].isFile()){
                    File file2=new File(tempList[i].toString());
                    try {
                        BufferedReader buffer=new BufferedReader(new InputStreamReader(new FileInputStream(file2),"UTF-8"));
                        String link=null;
                        StringBuffer str=new StringBuffer();
                        while((link=buffer.readLine())!=null){
                            str.append(link+"\r\n");
                        }
                        String html=str.toString();
                        lu.chuan(html);
                        file2.renameTo(new File("/data1/spider/java_spider/tyc2/"+file2.getName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    class Jie implements  Runnable{
        private Cangku cang;
        private Json js;
        public Jie(Cangku cang,Json js){
            this.cang=cang;
            this.js=js;
        }
        @Override
        public void run() {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Gson gson=new Gson();
            while (true){
                try {
                    List<Object> list = cang.qu();
                    List<Map<String,String>> jsonlist=new ArrayList<Map<String, String>>();
                    Map<String,String> map;
                    if(list!=null&&list.size()>0) {
                        for (Object obj : list) {
                            if (obj instanceof Bean.Duiwaitouzi) {
                                Bean.Duiwaitouzi dui = (Bean.Duiwaitouzi) obj;
                                for (Bean.Duiwaitouzi.Da.detail dd : dui.data.result) {
                                    String beitouziqiyeming = dd.name.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String beitouzifading = dd.legalPersonName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuceziben2 = dd.regCapital.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishue = dd.amount.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzizhanbi = dd.percent.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    long zhuceshijian2 = Long.parseLong(dd.estiblishTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String duizhuceshijian = simpleDateFormat.format(zhuceshijian2);
                                    String zhuangtai = dd.regStatus.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingyingfanwei2 = dd.business_scope.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("beitouziqiyemingcheng", beitouziqiyeming);
                                    map.put("fadingdaibiaoren", beitouzifading);
                                    map.put("zhuceziben", zhuceziben2);
                                    map.put("touzishue", touzishue);
                                    map.put("touzizhanbi", touzizhanbi);
                                    map.put("zhuceshijian", duizhuceshijian);
                                    map.put("zhuangtai", zhuangtai);
                                    map.put("jingyingfanwei", jingyingfanwei2);
                                    list.add(map);
                                }
                                js.jin("duiwaitouzi", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Biangeng) {
                                Bean.Biangeng bian = (Bean.Biangeng) obj;
                                for (Bean.Biangeng.Da.detail bb : bian.data.result) {
                                    String biangengxiangmu = bb.changeItem.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    long biangengshijian = Long.parseLong(bb.changeTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String biangengtime = simpleDateFormat.format(biangengshijian);
                                    String biangengqian = bb.contentBefore.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String biangenghou = bb.contentAfter.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("biangengxiangmu", biangengxiangmu);
                                    map.put("biangengshijian", biangengtime);
                                    map.put("biangengqian", biangengqian);
                                    map.put("biangenghou", biangenghou);
                                    list.add(map);
                                }
                                js.jin("biangeng", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Fenzhi) {
                                Bean.Fenzhi fen = (Bean.Fenzhi) obj;
                                for (Bean.Fenzhi.Da.detail ff : fen.data.result) {
                                    String qiyemingcheng = ff.name.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fadingdaibiao = ff.legalPersonName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuangtai = ff.regStatus.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    long zhuceshijian2 = Long.parseLong(ff.estiblishTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String fenzhuceshijian = simpleDateFormat.format(zhuceshijian2);
                                    String jingying = ff.category.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("qiyemingcheng", qiyemingcheng);
                                    map.put("fadingdaibiaoren", fadingdaibiao);
                                    map.put("zhuangtai", zhuangtai);
                                    map.put("zhuceshijian", fenzhuceshijian);
                                    map.put("jingyingfanwei", jingying);
                                    list.add(map);
                                }
                                js.jin("fenzhijigou", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Finacning) {
                                Bean.Finacning fin = (Bean.Finacning) obj;
                                for (Bean.Finacning.Da.Pa.detail ff : fin.data.page.rows) {
                                    long rongzishijian = Long.parseLong(ff.date.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String rongzitime = simpleDateFormat.format(rongzishijian);
                                    String rongzilunci = ff.round.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String guzhi = ff.value.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String rongzijine = ff.money.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String bili = ff.share.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzifang = ff.rongziMap.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("rongzishijian", rongzitime);
                                    map.put("lunci", rongzilunci);
                                    map.put("guzhi", guzhi);
                                    map.put("rongzijine", rongzijine);
                                    map.put("bili", bili);
                                    map.put("touzifang", touzifang);
                                    list.add(map);
                                }
                                js.jin("rongzilishi", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Hexintuandui) {
                                Bean.Hexintuandui hexin = (Bean.Hexintuandui) obj;
                                for (Bean.Hexintuandui.Da.Pa.detail hh : hexin.data.page.rows) {
                                    String hexinlogo = hh.icon.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String hexinming = hh.name.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String hexinzhiwei = hh.title.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String hexinjianli = hh.desc.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("logo", hexinlogo);
                                    map.put("mingcheng", hexinming);
                                    map.put("zhiwei", hexinzhiwei);
                                    map.put("jianli", hexinjianli);
                                    list.add(map);
                                }
                                js.jin("hexintuandui", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Qiyeyewu) {
                                Bean.Qiyeyewu qi = (Bean.Qiyeyewu) obj;
                                for (Bean.Qiyeyewu.Da.Pa.detail qq : qi.data.page.rows) {
                                    String qilogo = qq.logo.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qiming = qq.product.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qifenlei = qq.hangye.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qimiaoshu = qq.yewu.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("logo", qilogo);
                                    map.put("mingcheng", qiming);
                                    map.put("fenlei", qifenlei);
                                    map.put("miaoshu", qimiaoshu);
                                    list.add(map);
                                }
                                js.jin("qiyeyewu", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Touzishijian) {
                                Bean.Touzishijian touzi = (Bean.Touzishijian) obj;
                                for (Bean.Touzishijian.Da.Pa.detail tt : touzi.data.page.rows) {
                                    long touzishijianshijian = Long.parseLong(tt.tzdate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String touzishijiantime = simpleDateFormat.format(touzishijianshijian);
                                    String touzishijianlunci = tt.lunci.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijianjine = tt.money.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijiantouzifang = tt.rongzi_map.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijianchanpin = tt.product.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijiandiqu = tt.location.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijianhangye = tt.hangye1.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String touzishijianyewu = tt.yewu.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("shijian", touzishijiantime);
                                    map.put("lunci", touzishijianlunci);
                                    map.put("jine", touzishijianjine);
                                    map.put("touzifang", touzishijiantouzifang);
                                    map.put("chanpin", touzishijianchanpin);
                                    map.put("diqu", touzishijiandiqu);
                                    map.put("hangye", touzishijianhangye);
                                    map.put("yewu", touzishijianyewu);
                                    list.add(map);
                                }
                                js.jin("touzishijian", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Jingpinxinxi) {
                                Bean.Jingpinxinxi jingpin = (Bean.Jingpinxinxi) obj;
                                for (Bean.Jingpinxinxi.Da.Pa.detail tt : jingpin.data.page.rows) {
                                    long jingpinchenglishijian = Long.parseLong(tt.setupDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String jingpinchenglitime = simpleDateFormat.format(jingpinchenglishijian);
                                    String jingpinchanpin = tt.jingpinProduct.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingpindiqu = tt.location.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingpinlunci = tt.round.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingpinhangye = tt.hangye.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingpinyewu = tt.yewu.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingpinguzhi = tt.value.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("chenglishijian", jingpinchenglitime);
                                    map.put("chanpin", jingpinchanpin);
                                    map.put("diqu", jingpindiqu);
                                    map.put("lunci", jingpinlunci);
                                    map.put("hangye", jingpinhangye);
                                    map.put("yewu", jingpinyewu);
                                    map.put("guzhi", jingpinguzhi);
                                    list.add(map);
                                }
                                js.jin("jingpinxinxi", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Falvsusong) {
                                Bean.Falvsusong falv = (Bean.Falvsusong) obj;
                                for (Bean.Falvsusong.Da.detail ff : falv.data.items) {
                                    long submittime = Long.parseLong(ff.submittime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String falvsusongriqi = simpleDateFormat.format(submittime);
                                    String falvsusongwenshu = ff.uuid.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String falvsusonganjianleixing = ff.casetype.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String falvsusonganjianhao = ff.caseno.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("riqi", falvsusongriqi);
                                    map.put("wenshu", falvsusongwenshu);
                                    map.put("anjianleixing", falvsusonganjianleixing);
                                    map.put("anjianhao", falvsusonganjianhao);
                                    list.add(map);
                                }
                                js.jin("falvsusong", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Fayuangonggao) {
                                Bean.Fayuangonggao fayuan = (Bean.Fayuangonggao) obj;
                                for (Bean.Fayuangonggao.detail ff : fayuan.courtAnnouncements) {
                                    String fayuangonggaoshijian = ff.publishdate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fayuanshangsufang = ff.party1.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fayuanbeisufang = ff.party2.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fayuangonggaoleixing = ff.bltntypename.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fayuanfayuan = ff.courtcode.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String fayuanxiangqing = ff.content.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("shijian", fayuangonggaoshijian);
                                    map.put("shangsufang", fayuanshangsufang);
                                    map.put("beisufang", fayuanbeisufang);
                                    map.put("gonggaoleixing", fayuangonggaoleixing);
                                    map.put("fayuan", fayuanfayuan);
                                    map.put("xiangqing", fayuanxiangqing);
                                    list.add(map);
                                }
                                js.jin("fayuangonggao", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Beizhixingren) {
                                Bean.Beizhixingren bei = (Bean.Beizhixingren) obj;
                                for (Bean.Beizhixingren.Da.detail bb : bei.data.items) {
                                    long beilihanriqi = Long.parseLong(bb.caseCreateTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String beilihantime = simpleDateFormat.format(beilihanriqi);
                                    String beizhixingbiaode = bb.execMoney.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String beianhao = bb.caseCode.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String beizhixingfayuan = bb.execCourtName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("lianriqi", beilihantime);
                                    map.put("zhixingbiaode", beizhixingbiaode);
                                    map.put("anhao", beianhao);
                                    map.put("zhixingfayuan", beizhixingfayuan);
                                    list.add(map);
                                }
                                js.jin("beizhixingren", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Shixinren) {
                                Bean.Shixinren shi = (Bean.Shixinren) obj;
                                for (Bean.Shixinren.Da.detail ss : shi.data.items) {
                                    long shilianriqi = Long.parseLong(ss.publishdate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String shiliantime = simpleDateFormat.format(shilianriqi);
                                    String shianhao = ss.gistid.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shizhixingfayuan = ss.courtname.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shilvxingzhuangtai = ss.performance.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shizhixingyijuwenhao = ss.casecode.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shixiangqing = ss.duty.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("lianriqi", shiliantime);
                                    map.put("anhao", shianhao);
                                    map.put("zhixingfayuan", shizhixingfayuan);
                                    map.put("lvxingzhuangtai", shilvxingzhuangtai);
                                    map.put("zhixingyijuwenhao", shizhixingyijuwenhao);
                                    map.put("xiangqing", shixiangqing);
                                    list.add(map);
                                }
                                js.jin("shixinren", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Jingyingyichang) {
                                Bean.Jingyingyichang jing = (Bean.Jingyingyichang) obj;
                                for (Bean.Jingyingyichang.Da.detail jj : jing.data.result) {
                                    String jinglieruriqi = jj.putDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jinglieruyuanyin = jj.putReason.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingjuedingjiguan = jj.putDepartment.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingyichuriqi = jj.removeDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingyichuyuanyin = jj.removeReason.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String jingyichujiguan = jj.removeDepartment.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("lieruriqi", jinglieruriqi);
                                    map.put("lieruyuanyin", jinglieruyuanyin);
                                    map.put("juedingjiguan", jingjuedingjiguan);
                                    map.put("yichuriqi", jingyichuriqi);
                                    map.put("yichuyuanyin", jingyichuyuanyin);
                                    map.put("yichujiguan", jingyichujiguan);
                                    list.add(map);
                                }
                                js.jin("jingyingyichang", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Xingzhengchufa) {
                                Bean.Xingzhengchufa xing = (Bean.Xingzhengchufa) obj;
                                for (Bean.Xingzhengchufa.Da.detail xx : xing.data.items) {
                                    String xingjuedingriqi = xx.decisionDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String xingjuedingwenshuhao = xx.punishNumber.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String xingleixing = xx.type.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String xingjuedingjiguan = xx.departmentName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String xingxiangqing = xx.type.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("juedingriqi", xingjuedingriqi);
                                    map.put("juedingwenshuhao", xingjuedingwenshuhao);
                                    map.put("leixing", xingleixing);
                                    map.put("juedingjiguan", xingjuedingjiguan);
                                    map.put("xiangqing", xingxiangqing);
                                    list.add(map);
                                }
                                js.jin("xingzhengchufa", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Yanzhongweifa) {
                                Bean.Yanzhongweifa yan = (Bean.Yanzhongweifa) obj;
                                for (Bean.Yanzhongweifa.Da.detail yy : yan.data.items) {
                                    long yanlieruriqi = Long.parseLong(yy.putDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String yanlieritime = simpleDateFormat.format(yanlieruriqi);
                                    String yanlieruyuanyin = yy.putReason.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String yanjuedingjiguan = yy.putDepartment.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("lieruriqi", yanlieritime);
                                    map.put("lieruyuanyin", yanlieruyuanyin);
                                    map.put("juedingjiguan", yanjuedingjiguan);
                                    list.add(map);
                                }
                                js.jin("yanzhongweifa", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Guquanchuzhi) {
                                Bean.Guquanchuzhi gu = (Bean.Guquanchuzhi) obj;
                                for (Bean.Guquanchuzhi.Da.detail gg : gu.data.items) {
                                    long gugonggaoshijian = Long.parseLong(gg.regDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String gugonggaotime = simpleDateFormat.format(gugonggaoshijian);
                                    String gudengjibianhao = gg.regNumber.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String guchuzhiren = gg.pledgor.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String guzhiquanren = gg.pledgee.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String guzhuangtai = gg.state.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String guxiangqing = gg.equityAmount.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map = new HashMap<String, String>();
                                    map.put("dengjibianhao", gudengjibianhao);
                                    map.put("chuzhiren", guchuzhiren);
                                    map.put("zhiquanren", guzhiquanren);
                                    map.put("gonggaoshijian", gugonggaotime);
                                    map.put("zhuangtai", guzhuangtai);
                                    map.put("xiangqing", guxiangqing);
                                    list.add(map);
                                }
                                js.jin("guquanchuzhi", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Dongchandiya) {
                                Bean.Dongchandiya dongchan = (Bean.Dongchandiya) obj;
                                Bean.Dongchandiya.Dongchan dong = gson.fromJson(dongchan.data, Bean.Dongchandiya.Dongchan.class);
                                if (dong.items != null && dong.items.size() > 0) {
                                    for (Bean.Dongchandiya.Dongchan.details dd : dong.items) {
                                        String dongdengjiriqi = dd.baseInfo.regDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        String dongdengjihao = dd.baseInfo.regNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        String dongbeidanbao = dd.baseInfo.type.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        String dongdengjijiguan = dd.baseInfo.regDepartment.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        String dongzhuangtai = dd.baseInfo.status.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        String dongxiangqing = dongchan.data.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                        map = new HashMap<String, String>();
                                        map.put("dengjiriqi", dongdengjiriqi);
                                        map.put("dengjihao", dongdengjihao);
                                        map.put("beidanbaozhaiquanleixing", dongbeidanbao);
                                        map.put("dengjijiguan", dongdengjijiguan);
                                        map.put("zhuangtai", dongzhuangtai);
                                        map.put("xiangqing", dongxiangqing);
                                        list.add(map);
                                    }
                                }
                                js.jin("dongchandiya", JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Qianshuigonggao) {
                                Bean.Qianshuigonggao qian = (Bean.Qianshuigonggao) obj;
                                for (Bean.Qianshuigonggao.Da.detail qq : qian.data.items) {
                                    String qianfaburiqi = qq.publishDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qiannashuiren = qq.personIdNumber.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qianqianshuizhong = qq.taxCategory.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String qianshuiyue = qq.ownTaxAmount.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("faburiqi",qianfaburiqi);
                                    map.put("nashuirenshibiehao",qiannashuiren);
                                    map.put("qianshuibizhong",qianqianshuizhong);
                                    map.put("qianshuiyue", qianshuiyue);
                                    list.add(map);
                                }
                                js.jin("qianshuigonggao",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zhaotoubiao) {
                                Bean.Zhaotoubiao zhao = (Bean.Zhaotoubiao) obj;
                                for (Bean.Zhaotoubiao.Da.detail zz : zhao.data.items) {
                                    long zhaofabushijian = Long.parseLong(zz.publishTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String zhaofabutime = simpleDateFormat.format(zhaofabushijian);
                                    String zhaobiaoti = zz.title.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaocaijiren = zz.purchaser.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaoxiangqing = zz.content.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaogonggaozhairen = zz.abs.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaozongjie = zz.intro.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("fabushijian",zhaofabutime);
                                    map.put("biaoti",zhaobiaoti);
                                    map.put("caigouren",zhaocaijiren);
                                    map.put("xiangqing",zhaoxiangqing);
                                    map.put("gonggaozhairen",zhaogonggaozhairen);
                                    map.put("zongjie",zhaozongjie);
                                    list.add(map);
                                }
                                js.jin("zhaotoubiao",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zhaiquanxinxi) {
                                Bean.Zhaiquanxinxi zhai = (Bean.Zhaiquanxinxi) obj;
                                for (Bean.Zhaiquanxinxi.Da.detail zz : zhai.data.bondList) {
                                    long zhaifaxingriqi = Long.parseLong(zz.publishTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String zhaifaxingtime = simpleDateFormat.format(zhaifaxingriqi);
                                    String zhaizhaiquanming = zz.bondName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaiquandaima = zz.bondNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaiquanleixing = zz.bondType.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaizuixinpingji = zz.debtRating.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaixiangqing = zz.tip.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("faxingriqi",zhaifaxingtime);
                                    map.put("zhaiquanmingcheng",zhaizhaiquanming);
                                    map.put("zhaiquandaima",zhaiquandaima);
                                    map.put("zhaiquanleixing",zhaiquanleixing);
                                    map.put("zuixinpingji",zhaizuixinpingji);
                                    map.put("xiangqing",zhaixiangqing);
                                    list.add(map);
                                }
                                js.jin("zhaiquanxinxi",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Goudixinxi) {
                                Bean.Goudixinxi gou = (Bean.Goudixinxi) obj;
                                for (Bean.Goudixinxi.Da.detail gg : gou.data.companyPurchaseLandList) {
                                    long gouqiandingriqi = Long.parseLong(gg.signedDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    long gouyuedingdong = Long.parseLong(gg.startTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String gouqiandingtime = simpleDateFormat.format(gouqiandingriqi);
                                    String gouyuedingdongtime = simpleDateFormat.format(gouyuedingdong);
                                    String goudianzijianguanhao = gg.elecSupervisorNo.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String gougongdimian = gg.totalArea.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String gouxingzhengqu = gg.location.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String gouxiangqing = gg.purpose.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("qiandingriqi",gouqiandingtime);
                                    map.put("dianzijianguanhao",goudianzijianguanhao);
                                    map.put("donggongri",gouyuedingdongtime);
                                    map.put("zongmianji",gougongdimian);
                                    map.put("xingzhengqu",gouxingzhengqu);
                                    map.put("xiangqing",gouxiangqing);
                                    list.add(map);
                                }
                                js.jin("goudixinxi",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zhaopin) {
                                Bean.Zhaopin zhao = (Bean.Zhaopin) obj;
                                for (Bean.Zhaopin.Da.detail zz : zhao.data.companyEmploymentList) {
                                    long zhaofabushijian = Long.parseLong(zz.updateTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String zhaofabutime = simpleDateFormat.format(zhaofabushijian);
                                    String zhaopinzhiwei = zz.title.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaoxinzi = zz.oriSalary.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String gongzuojingyan = zz.experience.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaopinrenshu = zz.employerNumber.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaochegnshi = zz.city.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhaoxiangqing = zz.description.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("fabushijian",zhaofabutime);
                                    map.put("zhaopinzhiwei",zhaopinzhiwei);
                                    map.put("xinzi",zhaoxinzi);
                                    map.put("gongzuojingyan",gongzuojingyan);
                                    map.put("zhaopinrenshu",zhaopinrenshu);
                                    map.put("suozaichengshi",zhaochegnshi);
                                    map.put("xiangqing",zhaoxiangqing);
                                    list.add(map);
                                }
                                js.jin("zhaopin",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Shuiwupingji) {
                                Bean.Shuiwupingji shui = (Bean.Shuiwupingji) obj;
                                for (Bean.Shuiwupingji.Da.detail ss : shui.data.items) {
                                    String shuinianfen = ss.year.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String nashuipingji = ss.grade.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shuileixing = ss.type.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String nashuirenshibiehao = ss.idNumber.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shuipingjia = ss.evalDepartment.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("nianfe",shuinianfen);
                                    map.put("nashuipingji",nashuipingji);
                                    map.put("leixing",shuileixing);
                                    map.put("shibiehao",nashuirenshibiehao);
                                    map.put("pingjiadanwei",shuipingjia);
                                    list.add(map);
                                }
                                js.jin("shuiwupingji",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Choucha) {
                                Bean.Choucha chou = (Bean.Choucha) obj;
                                for (Bean.Choucha.Da.detail cc : chou.data.items) {
                                    String chouriqi = cc.checkDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chouleixing = cc.checkType.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String choujieguo = cc.checkResult.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String choujiancha = cc.checkOrg.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("riqi",chouriqi);
                                    map.put("leixing",chouleixing);
                                    map.put("jieguo",choujieguo);
                                    map.put("jianchashishijiguan",choujiancha);
                                    list.add(map);
                                }
                                js.jin("chouchajiancha",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Chanpin) {
                                Bean.Chanpin chan = (Bean.Chanpin) obj;
                                for (Bean.Chanpin.Da.detail cc : chan.data.items) {
                                    String chantubiao = cc.icon.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chanmingcheng = cc.name.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chanjiancheng = cc.filterName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chanfenlei = cc.type.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chanlingyu = cc.classes.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String chanxiangqing = cc.brief.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("tubiao",chantubiao);
                                    map.put("chanpinmingcheng",chanmingcheng);
                                    map.put("chanpinjiancheng",chanjiancheng);
                                    map.put("chanpinfenlei",chanfenlei);
                                    map.put("lingyu",chanlingyu);
                                    map.put("xiangqing",chanxiangqing);
                                    list.add(map);
                                }
                                js.jin("chanpinxinxi",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zizhizhzengshu) {
                                Bean.Zizhizhzengshu zi = (Bean.Zizhizhzengshu) obj;
                                for (Bean.Zizhizhzengshu.Da.detail zz : zi.data.items) {
                                    long zifazhengriqi = Long.parseLong(zz.issueDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    long zizijiezhiriqi = Long.parseLong(zz.toDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String zifazhengtime = simpleDateFormat.format(zifazhengriqi);
                                    String zijiezhitime = simpleDateFormat.format(zizijiezhiriqi);
                                    String zishebeiming = zz.deviceName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zizhengshuleixing = zz.licenceType.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zishebeibianhao = zz.deviceType.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zixukebianhao = zz.licenceNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("shebeimingcheng",zishebeiming);
                                    map.put("zhengshuleixing",zizhengshuleixing);
                                    map.put("fazhengriqi",zifazhengtime);
                                    map.put("jiezhiriqi",zijiezhitime);
                                    map.put("shebeibianhao",zishebeibianhao);
                                    map.put("xukebianhao",zixukebianhao);
                                    list.add(map);
                                }
                                js.jin("zizhizhengshu",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Shangbiaoxinxi) {
                                Bean.Shangbiaoxinxi shang = (Bean.Shangbiaoxinxi) obj;
                                for (Bean.Shangbiaoxinxi.Da.detail ss : shang.data.items) {
                                    long shangshenqingriqi = Long.parseLong(ss.appDate.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String shangshenqingtime = simpleDateFormat.format(shangshenqingriqi);
                                    String shangbiao = ss.tmPic.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shangbiaoming = ss.tmName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shangzhucehao = ss.regNo.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shangleibie = ss.intCls.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String shangzhuangtai = ss.status.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("shenqingriqi",shangshenqingtime);
                                    map.put("shangbiao",shangbiao);
                                    map.put("shangbiaomingcheng",shangbiaoming);
                                    map.put("zhucehao",shangzhucehao);
                                    map.put("leibie",shangleibie);
                                    map.put("zhuangtai",shangzhuangtai);
                                    list.add(map);
                                }
                                js.jin("shangbiaoxinxi",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zhuanli) {
                                Bean.Zhuanli zhuan = (Bean.Zhuanli) obj;
                                for (Bean.Zhuanli.Da.detail zz : zhuan.data.items) {
                                    String zhuanshenqinggongburi = zz.applicationPublishTime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanliming = zz.patentName.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanshengqinghao = zz.patentNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanshenqinggongbuhao = zz.applicationPublishNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuandailijigou = zz.agency.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanfamingren = zz.inventor.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuandailiren = zz.agent.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanxiangqingtu = zz.imgUrl.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanfenleihao = zz.allCatNum.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanzhaiyao = zz.abstracts.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuandizhi = zz.address.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    String zhuanlileixing = zz.patentType.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim();
                                    map=new HashMap<String, String>();
                                    map.put("gongburi",zhuanshenqinggongburi);
                                    map.put("zhuanlimingcheng",zhuanliming);
                                    map.put("shenqinghao",zhuanshengqinghao);
                                    map.put("shenqinggongbuhao",zhuanshenqinggongbuhao);
                                    map.put("dailijigou",zhuandailijigou);
                                    map.put("famingren",zhuanfamingren);
                                    map.put("dailiren",zhuandailiren);
                                    map.put("xiangqingtu",zhuanxiangqingtu);
                                    map.put("fenleihao",zhuanfenleihao);
                                    map.put("zhaiyao",zhuanzhaiyao);
                                    map.put("dizhi",zhuandizhi);
                                    map.put("leixing",zhuanlileixing);
                                    list.add(map);
                                }
                                js.jin("zhuanli",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Zhuzuoquan) {
                                Bean.Zhuzuoquan zhu = (Bean.Zhuzuoquan) obj;
                                for (Bean.Zhuzuoquan.Da.detail zz : zhu.data.items) {
                                    long zhupizhunriqi = Long.parseLong(zz.regtime.replace("<em>", "").replace("</em>", "").replace("<br>", "").replace("\n", "").trim());
                                    String zhupizhuntime = simpleDateFormat.format(zhupizhunriqi);
                                    String zhuruanjianquan = zz.fullname;
                                    String zhuruanjianjian = zz.simplename;
                                    String zhudengjihao = zz.regnum;
                                    String zhufenleihao = zz.catnum;
                                    String zhubanbenhao = zz.version;
                                    String zhuxiangqing = zz.authorNationality;
                                    map=new HashMap<String, String>();
                                    map.put("pizhunriqi",zhupizhuntime);
                                    map.put("ruanjianquancheng",zhuruanjianquan);
                                    map.put("ruanjianjiancheng",zhuruanjianjian);
                                    map.put("dengjihao",zhudengjihao);
                                    map.put("fenlaihao",zhufenleihao);
                                    map.put("banbenhao",zhubanbenhao);
                                    map.put("xiangqing",zhuxiangqing);
                                    list.add(map);
                                }
                                js.jin("zhuzuoquan",JSONObject.fromObject(list).toString());
                            } else if (obj instanceof Bean.Wangzhanbeian) {
                                Bean.Wangzhanbeian wang = (Bean.Wangzhanbeian) obj;
                                for (Bean.Wangzhanbeian.detail ww : wang.data) {
                                    String wangshenheshijian = ww.examineDate;
                                    String wangzhanming = ww.webName;
                                    String wangzhanshouye = ww.webSite.toString();
                                    String wangyuming = ww.ym;
                                    String wangbeianhao = ww.liscense;
                                    String wangzhuangtai = "正常";
                                    String wangdanweixingzhi = ww.companyType;
                                    map=new HashMap<String, String>();
                                    map.put("shenheshijian",wangshenheshijian);
                                    map.put("wangzhanmingcheng",wangzhanming);
                                    map.put("wangzhanshouye",wangzhanshouye);
                                    map.put("yuming",wangyuming);
                                    map.put("beianhao",wangbeianhao);
                                    map.put("zhuangtai",wangzhuangtai);
                                    map.put("danweixingzhi",wangdanweixingzhi);
                                    list.add(map);
                                }
                                js.jin("wangzhanbeian",JSONObject.fromObject(list).toString());
                            }
                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }



    class Jiexi implements Runnable{
        private Lujing lu;
        private Key k;
        private Json j;
        public Jiexi(Lujing lu,Key k,Json j){
            this.lu=lu;
            this.k=k;
            this.j=j;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    String html=lu.quchu();
                    Document doc = Jsoup.parse(html.substring(0,html.lastIndexOf("\r\n")-1));
                    String pn=html.substring(html.lastIndexOf("\r\n")-1,html.lastIndexOf("\r\n"));
                    String tid = doc.select("div.shareBox.pt8.pb8.mt20.text-right div.text-left.in-block a").attr("ng-href").split(";")[0].split("&")[0].split("\\?")[1].split("/", 5)[4];
                    String quancheng = getString(doc, "span.f18.in-block.vertival-middle.ng-binding", 0);
                    String cengyongming = getString(doc, "div.historyName45Bottom.position-abs.new-border.pl8.pr8.pt4.pb4.ng-binding", 0);
                    String logo = getHref(doc, "div.companyTitleBox55 div.b-c-white.new-border.over-hide.mr10.ie9Style img", "ng-src", 0);
                    String phone = getString(doc, "div.in-block.vertical-top.overflow-width.mr20 span.ng-binding", 0);
                    String web = getString(doc, "div.in-block.vertical-top.overflow-width.mr20 a.c9.ng-binding.ng-scope", 0);
                    String email = getString(doc, "div.in-block.vertical-top span.in-block.vertical-top.overflow-width.emailWidth.ng-binding", 0);
                    String address = getString(doc, "div.in-block.vertical-top span.in-block.overflow-width.vertical-top.emailWidth.ng-binding", 0);
                    String faren = getString(doc, "div.baseInfo_model2017 tbody tr td a.in-block.vertival-middle.overflow-width.f14.mr20.ng-binding.ng-scope.ng-isolate-scope", 0);
                    String zhuceziben = getString(doc, "div.baseInfo_model2017 tbody tr td div.baseinfo-module-content-value.ng-binding", 0);
                    String zhuceshijian = getString(doc, "div.baseInfo_model2017 tbody tr td div.baseinfo-module-content-value.ng-binding", 1);
                    String statu = getString(doc, "div.baseInfo_model2017 tbody tr td div.baseinfo-module-content-value.ng-binding", 2);
                    String gongshang = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 0);
                    String zuzhijigou = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 1);
                    String tongyixinyong = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 2);
                    String qiyetype = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 3);
                    String hangye = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 4);
                    String yingyeqixian = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding.ng-scope", 0);
                    String hezhunriqi = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 6);
                    String dengjijiguan = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 7);
                    String zhucedizhi = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding", 8);
                    String jingyingfanwei = getString(doc, "div.row.b-c-white.company-content.base2017 tbody td.basic-td span.ng-binding.ng-scope", 1);


                    Map<String,String> jichumap=new HashMap<String, String>();
                    jichumap.put("tid",tid);
                    jichumap.put("quancheng",quancheng);
                    jichumap.put("cengyongming",cengyongming);
                    jichumap.put("logo",logo);
                    jichumap.put("phone",phone);
                    jichumap.put("web",web);
                    jichumap.put("email",email);
                    jichumap.put("address",address);
                    jichumap.put("faren",faren);
                    jichumap.put("zhuceziben",zhuceziben);
                    jichumap.put("zhuceshijian",zhuceshijian);
                    jichumap.put("zhuangtai",statu);
                    jichumap.put("gongshanghao",gongshang);
                    jichumap.put("zuzhijigoudaima",zuzhijigou);
                    jichumap.put("tongyixinyongdaima",tongyixinyong);
                    jichumap.put("qiyeleixing",qiyetype);
                    jichumap.put("hangye",hangye);
                    jichumap.put("yingyeqixian",yingyeqixian);
                    jichumap.put("hezhunriqi",hezhunriqi);
                    jichumap.put("dengjijiguan",dengjijiguan);
                    jichumap.put("zhucedizhi",zhucedizhi);
                    jichumap.put("jingyingfanwei",jingyingfanwei);
                    JSONObject jsonObject=JSONObject.fromObject(jichumap);
                    String jichujson=jsonObject.toString();


                    List<Map<String,String>> chengyuanlist=new ArrayList<Map<String, String>>();
                    Map<String,String> chengyuanmap;
                    Elements elechengyuan = getElements(doc, "div.in-block.float-left.new-border.mr10.mb3.ng-scope");
                    if (elechengyuan != null) {
                        for (Element e : elechengyuan) {
                            String chengyuanming = getString(e, "a.overflow-width.in-block.vertival-middle.pl15.mb4.ng-binding.ng-isolate-scope", 0);
                            String chengyuanzhiwei = null;
                            Elements elechengyuanzhiwei = getElements(e, "span.ng-binding.ng-scope");
                            if (elechengyuanzhiwei != null) {
                                StringBuffer str = new StringBuffer();
                                for (Element ee : elechengyuanzhiwei) {
                                    str.append(ee.text().replace("，", "") + ";");
                                }
                                chengyuanzhiwei = str.toString();
                            }
                            chengyuanmap=new HashMap<String, String>();
                            chengyuanmap.put("mingcheng",chengyuanming);
                            chengyuanmap.put("zhiwei",chengyuanzhiwei);
                            chengyuanlist.add(chengyuanmap);
                        }
                    }
                    String chengyuanjson=JSONObject.fromObject(chengyuanlist).toString();



                    List<Map<String,String>> gudonglist=new ArrayList<Map<String, String>>();
                    Map<String,String> gudongmap;
                    Elements elegudong = getElements(doc, "div[ng-if=dataItemCount.holderCount>0] table.table.companyInfo-table tbody tr.ng-scope");
                    if (elegudong != null) {
                        for (Element e : elegudong) {
                            String gudongming = getString(e, "td:nth-child(1) a.in-block.vertival-middle.overflow-width.ng-binding.ng-isolate-scope", 0);
                            String chuzibili = getString(e, "td:nth-child(2) span.c-money-y.ng-binding", 0);
                            String renjiaochuzijine = getString(e, "td:nth-child(3) div.ng-scope span.ng-binding", 0);
                            String renjiaochuzishijian = getString(e, "td:nth-child(3) div.ng-scope span.ng-binding.ng-scope", 0);
                            System.out.println(gudongming + "     " + chuzibili + "      " + renjiaochuzijine + "     " + renjiaochuzishijian);
                            gudongmap=new HashMap<String, String>();
                            gudongmap.put("mingcheng",gudongming);
                            gudongmap.put("chuzibili",chuzibili);
                            gudongmap.put("renjiaochuzijine",renjiaochuzijine);
                            gudongmap.put("renjiaochuzishijian",renjiaochuzishijian);
                            gudonglist.add(gudongmap);
                        }
                    }
                    String gudongjson=JSONObject.fromObject(gudonglist).toString();
                    String[] kk=new String[]{tid,quancheng,pn,jichujson,chengyuanjson,gudongjson};
                    k.fang(kk);


                }catch (Exception e){

                }
            }
        }
    }


    class Lujing{
        BlockingQueue<String> lujing=new LinkedBlockingQueue<String>();
        public void chuan(String key) throws InterruptedException {
            lujing.put(key);
        }
        public String quchu() throws InterruptedException {
            return lujing.take();
        }
    }


    class Cangku{
        BlockingQueue<List<Object>> cang=new LinkedBlockingQueue<List<Object>>();
        public void ru(List<Object> obj) throws InterruptedException {
            cang.put(obj);
        }
        public List<Object> qu() throws InterruptedException {
            return cang.take();
        }
    }

    class Json{
        Map<String,String> map=new HashMap<String, String>();
        public void jin(String key,String value){
            map.put(key,value);
        }
        public Map<String,String> quchu(){
            return map;
        }

    }

    class Key{
        BlockingQueue<String[]> key=new LinkedBlockingQueue<String[]>();
        public void fang(String[] str) throws InterruptedException {
            key.put(str);
        }
        public String[] na() throws InterruptedException {
            return key.poll(60000, TimeUnit.SECONDS);
        }
    }

    class Duqu implements Runnable{
        private Key key;
        private Proxy proxy;
        private Connection con;
        public Duqu(Key key,Proxy proxy,Connection con){
            this.key=key;
            this.proxy=proxy;
            this.con=con;
        }
        @Override
        public void run() {
            while (true){
                try {
                    String[] str=key.na();
                    if(str==null){
                        Thread.sleep(1800000);
                        System.exit(0);
                    }
                    Json js=new Json();
                    Cangku ca=new Cangku();
                    ExecutorService pool= Executors.newCachedThreadPool();
                    js.jin("jichu",str[3]);
                    js.jin("chengyuan",str[4]);
                    js.jin("gudong",str[5]);
                    js.jin("pn",str[2]);
                    Jie ji=new Jie(ca,js);
                    Duiwaitouzi duiwai=new Duiwaitouzi(proxy,ca,str);
                    Biangeng bian=new Biangeng(proxy,ca,str);
                    Fenzhi fen=new Fenzhi(proxy,ca,str);
                    Rongzilishi rong=new Rongzilishi(proxy,ca,str);
                    Hexintuandui he=new Hexintuandui(proxy,ca,str);
                    Qiyeyewu qi=new Qiyeyewu(proxy,ca,str);
                    Touzishijian tou=new Touzishijian(proxy,ca,str);
                    Jingpinxinxi jing=new Jingpinxinxi(proxy,ca,str);
                    Falvsusong falv=new Falvsusong(proxy,ca,str);
                    Fayuangonggao fayuan=new Fayuangonggao(proxy,ca,str);
                    Beizhixingren bei=new Beizhixingren(proxy,ca,str);
                    Shixinren shi=new Shixinren(proxy,ca,str);
                    Jingyingyichang jingying=new Jingyingyichang(proxy,ca,str);
                    Xingzhengchufa xing=new Xingzhengchufa(proxy,ca,str);
                    Yanzhongweifa yan=new Yanzhongweifa(proxy,ca,str);
                    Guquanchuzhi gu=new Guquanchuzhi(proxy,ca,str);
                    Dongchandiya dong=new Dongchandiya(proxy,ca,str);
                    Qianshuigonggao qian=new Qianshuigonggao(proxy,ca,str);
                    Zhaotoubiao zhao=new Zhaotoubiao(proxy,ca,str);
                    Zhaiquanxinxi zhai=new Zhaiquanxinxi(proxy,ca,str);
                    Goudixinxi gou=new Goudixinxi(proxy,ca,str);
                    Zhaopin zhaopin=new Zhaopin(proxy,ca,str);
                    Shuiwupingji shui=new Shuiwupingji(proxy,ca,str);
                    Choucha chou=new Choucha(proxy,ca,str);
                    Chanpin chan=new Chanpin(proxy,ca,str);
                    Zizhizhengshu zizhi=new Zizhizhengshu(proxy,ca,str);
                    Shangbiaoxinxi shang=new Shangbiaoxinxi(proxy,ca,str);
                    Zhuanli zhuan=new Zhuanli(proxy,ca,str);
                    Zhuzuoquan zhu=new Zhuzuoquan(proxy,ca,str);
                    Wangzhanbeian wang=new Wangzhanbeian(proxy,ca,str);
                    pool.submit(ji);
                    pool.submit(duiwai);
                    pool.submit(bian);
                    pool.submit(fen);
                    pool.submit(rong);
                    pool.submit(he);
                    pool.submit(qi);
                    pool.submit(tou);
                    pool.submit(jing);
                    pool.submit(falv);
                    pool.submit(fayuan);
                    pool.submit(bei);
                    pool.submit(shi);
                    pool.submit(jingying);
                    pool.submit(xing);
                    pool.submit(yan);
                    pool.submit(gu);
                    pool.submit(dong);
                    pool.submit(qian);
                    pool.submit(zhao);
                    pool.submit(zhai);
                    pool.submit(gou);
                    pool.submit(zhaopin);
                    pool.submit(shui);
                    pool.submit(chou);
                    pool.submit(chan);
                    pool.submit(zizhi);
                    pool.submit(shang);
                    pool.submit(zhuan);
                    pool.submit(zhu);
                    pool.submit(wang);
                    pool.shutdown();
                    while (true){
                        if(pool.isTerminated()){
                            Thread.sleep(5000);
                            Thread th=new Thread(new Ruku(js,con));
                            th.start();
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Ruku implements Runnable{
        private Json js;
        private Connection con;
        public Ruku(Json js,Connection con){
            this.js=js;
            this.con=con;
        }
        @Override
        public void run() {
            try {
                String sql="insert into tyc_information(xu_hao,essential_information,leading_member,shareholder_Information,outbound_investment,change_record,b_ranch,financing_history,core_team,enterprise_business,investment_event,competing_information,legal_proceedings,court_notice,person_subjected,dishonest_person,abnormal_operation,administrative_sanction,serious_violation,stock_ownership,chattel_mortgage,tax_notice,b_idding,bond_information,purchase_information,r_ecruit,tax_rating,spot_check,product_information,qualification_certificate,trademark_information,p_atent,c_opyright,website_filing) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps=con.prepareStatement(sql);
                Map<String,String> map=js.quchu();
                ps.setString(1,map.get("pn"));
                ps.setString(2,map.get("jichu"));
                ps.setString(3,map.get("chengyuan"));
                ps.setString(4,map.get("gudong"));
                ps.setString(5,map.get("duiwaitouzi"));
                ps.setString(6,map.get("biangeng"));
                ps.setString(7,map.get("fenzhijigou"));
                ps.setString(8,map.get("rongzilishi"));
                ps.setString(9,map.get("hexintuandui"));
                ps.setString(10,map.get("qiyeyewu"));
                ps.setString(11,map.get("touzishijian"));
                ps.setString(12,map.get("jingpinxinxi"));
                ps.setString(13,map.get("falvsusong"));
                ps.setString(14,map.get("fayuangonggao"));
                ps.setString(15,map.get("beizhixingren"));
                ps.setString(16,map.get("shixinre"));
                ps.setString(17,map.get("jingyingyichang"));
                ps.setString(18,map.get("xingzhengchufa"));
                ps.setString(19,map.get("yanzhongweifa"));
                ps.setString(20,map.get("guquanchuzhi"));
                ps.setString(21,map.get("dongchandiya"));
                ps.setString(22,map.get("qianshuigonggao"));
                ps.setString(23,map.get("zhaotoubiao"));
                ps.setString(24,map.get("zhaiquanxinxi"));
                ps.setString(25,map.get("goudixinxi"));
                ps.setString(26,map.get("zhaopin"));
                ps.setString(27,map.get("shuiwupingji"));
                ps.setString(28,map.get("chouchajiancha"));
                ps.setString(29,map.get("chanpinxinxi"));
                ps.setString(30,map.get("zizhizhengshu"));
                ps.setString(31,map.get("shangbiaoxinxi"));
                ps.setString(32,map.get("zhuanli"));
                ps.setString(33,map.get("zhuzuoquan"));
                ps.setString(34,map.get("wangzhanbeian"));
                System.out.println("kaishi");
                ps.executeUpdate();
                System.out.println("ok");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    class Duiwaitouzi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Duiwaitouzi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid = str[0];
            List<Object> list=new ArrayList<Object>();
            for (int i = 1; i <= 1000; i++) {
                try {
                    String duiwaitouzi = qingqiu(proxy, "http://www.tianyancha.com/expanse/inverst.json?id=" + tid + "&ps=10000&pn=1");
                    Bean.Duiwaitouzi duiwai = gson.fromJson(duiwaitouzi, Bean.Duiwaitouzi.class);
                    if (StringUtils.isEmpty(duiwai.message) && duiwai.data.result != null && duiwai.data.result.size() > 0) {
                        list.add(duiwai);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Biangeng implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Biangeng(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++) {
                try {
                    String biangeng = qingqiu(proxy, "http://www.tianyancha.com/expanse/changeinfo.json?id=" + tid + "&ps=10000&pn=" + i);
                    Bean.Biangeng bian = gson.fromJson(biangeng, Bean.Biangeng.class);
                    if (StringUtils.isEmpty(bian.message) && bian.data.result != null && bian.data.result.size() > 0) {
                        list.add(bian);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Fenzhi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Fenzhi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            String tid=str[0];
            List<Object> list = new ArrayList<Object>();
            for(int i=1;i<=1000;i++) {
                try {
                    String fenzhi = qingqiu(proxy, "http://www.tianyancha.com/expanse/branch.json?id=" + tid + "&ps=10000&pn=" + i);
                    Bean.Fenzhi fen = gson.fromJson(fenzhi, Bean.Fenzhi.class);
                    if (StringUtils.isEmpty(fen.message) && fen.data.result != null && fen.data.result.size() > 0) {
                        list.add(fen);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Rongzilishi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Rongzilishi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++) {
                try {
                    String rongzilishi = qingqiu(proxy, "http://www.tianyancha.com/expanse/findHistoryRongzi.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000  &pn=" + i);
                    Bean.Finacning fin = gson.fromJson(rongzilishi, Bean.Finacning.class);
                    if (fin.data.page.rows != null && fin.data.page.rows.size() > 0) {
                        list.add(fin);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Hexintuandui implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Hexintuandui(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++) {
                try {
                    String hexintuandui = qingqiu(proxy, "http://www.tianyancha.com/expanse/findTeamMember.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Hexintuandui hexin = gson.fromJson(hexintuandui, Bean.Hexintuandui.class);
                    if (hexin.data.page.rows != null && hexin.data.page.rows.size() > 0) {
                        list.add(hexin);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Qiyeyewu implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Qiyeyewu(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++) {
                try {
                    String qiyeyewu = qingqiu(proxy, "http://www.tianyancha.com/expanse/findProduct.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Qiyeyewu qi = gson.fromJson(qiyeyewu, Bean.Qiyeyewu.class);
                    if (qi.data.page.rows != null && qi.data.page.rows.size() > 0) {
                        list.add(qi);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Touzishijian implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Touzishijian(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String touzishijian = qingqiu(proxy, "http://www.tianyancha.com/expanse/findTzanli.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Touzishijian touzi = gson.fromJson(touzishijian, Bean.Touzishijian.class);
                    if (touzi.data.page.rows != null && touzi.data.page.rows.size() > 0) {
                        list.add(touzi);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Jingpinxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Jingpinxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String jingpinxinxi = qingqiu(proxy, "http://www.tianyancha.com/expanse/findJingpin.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Jingpinxinxi jingpin = gson.fromJson(jingpinxinxi, Bean.Jingpinxinxi.class);
                    if (jingpin.data.page.rows != null && jingpin.data.page.rows.size() > 0) {
                        list.add(jingpin);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Falvsusong implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Falvsusong(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String falvsusong = qingqiu(proxy, "http://www.tianyancha.com/v2/getlawsuit/" + URLEncoder.encode(quancheng, "UTF-8") + ".json?page=" + i + "&ps=10000");
                    Bean.Falvsusong falv = gson.fromJson(falvsusong, Bean.Falvsusong.class);
                    if (falv.data.items != null && falv.data.items.size() > 0) {
                        list.add(falv);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Fayuangonggao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Fayuangonggao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {
                String fayuangonggao = qingqiu(proxy, "http://www.tianyancha.com/v2/court/" + URLEncoder.encode(quancheng, "UTF-8") + ".json?ps=10000");
                Bean.Fayuangonggao fayuan = gson.fromJson(fayuangonggao, Bean.Fayuangonggao.class);
                if (fayuan.courtAnnouncements != null && fayuan.courtAnnouncements.size() > 0) {
                    list.add(fayuan);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Beizhixingren implements  Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Beizhixingren(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String beizhixingren = qingqiu(proxy, "http://www.tianyancha.com/expanse/zhixing.json?id=" + tid + "&pn=" + i + "&ps=100000");
                    Bean.Beizhixingren bei = gson.fromJson(beizhixingren, Bean.Beizhixingren.class);
                    if (StringUtils.isEmpty(bei.message) && bei.data.items != null && bei.data.items.size() > 0) {
                        list.add(bei);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shixinren implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shixinren(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            try {
                String shixinren = qingqiu(proxy, "http://www.tianyancha.com/v2/dishonest/" + URLEncoder.encode(quancheng, "UTF-8") + ".json");
                Bean.Shixinren shi = gson.fromJson(shixinren, Bean.Shixinren.class);
                if (shi.data.items != null && shi.data.items.size() > 0) {
                    list.add(shi);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Jingyingyichang implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Jingyingyichang(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            try {
                String jingyingyichang = qingqiu(proxy, "http://www.tianyancha.com/expanse/abnormal.json?id=" + tid + "&ps=10000&pn=1");
                Bean.Jingyingyichang jing = gson.fromJson(jingyingyichang, Bean.Jingyingyichang.class);
                if (jing.data.result != null && jing.data.result.size() > 0) {
                    list.add(jing);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Xingzhengchufa implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Xingzhengchufa(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String xingzhengchufa = qingqiu(proxy, "http://www.tianyancha.com/expanse/punishment.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Xingzhengchufa xing = gson.fromJson(xingzhengchufa, Bean.Xingzhengchufa.class);
                    if (xing.data.items != null && xing.data.items.size() > 0) {
                        list.add(xing);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Yanzhongweifa implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Yanzhongweifa(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            try {
                String yanzhongweifa = qingqiu(proxy, "http://www.tianyancha.com/expanse/illegal.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=1");
                Bean.Yanzhongweifa yan = gson.fromJson(yanzhongweifa, Bean.Yanzhongweifa.class);
                if (yan.data.items != null && yan.data.items.size() > 0) {
                    list.add(yan);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Guquanchuzhi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Guquanchuzhi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String guquanchuzhi = qingqiu(proxy, "http://www.tianyancha.com/expanse/companyEquity.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Guquanchuzhi gu = gson.fromJson(guquanchuzhi, Bean.Guquanchuzhi.class);
                    if (gu.data.items != null && gu.data.items.size() > 0) {
                        list.add(gu);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Dongchandiya implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Dongchandiya(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            try {
                String dongchandiya = qingqiu(proxy, "http://www.tianyancha.com/expanse/mortgageInfo.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=1&ps=10000");
                Bean.Dongchandiya dongchan = gson.fromJson(dongchandiya, Bean.Dongchandiya.class);
                if (StringUtils.isNotEmpty(dongchan.data)) {
                    list.add(dongchan);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Qianshuigonggao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Qianshuigonggao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String qianshuigonggao = qingqiu(proxy, "http://www.tianyancha.com/expanse/owntax.json?id=" + tid + "&ps=5&pn=" + i);
                    Bean.Qianshuigonggao qian = gson.fromJson(qianshuigonggao, Bean.Qianshuigonggao.class);
                    if (qian.data.items != null && qian.data.items.size() > 0) {
                        list.add(qian);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaotoubiao implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaotoubiao(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zhaotoubiao = qingqiu(proxy, "http://www.tianyancha.com/expanse/bid.json?id=" + tid + "&pn=" + i + "&ps=10000");
                    Bean.Zhaotoubiao zhao = gson.fromJson(zhaotoubiao, Bean.Zhaotoubiao.class);
                    if (zhao.data.items != null && zhao.data.items.size() > 0) {
                        list.add(zhao);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaiquanxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaiquanxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zhaiquanxinxi = qingqiu(proxy, "http://www.tianyancha.com/extend/getBondList.json?companyName=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=10000");
                    Bean.Zhaiquanxinxi zhai = gson.fromJson(zhaiquanxinxi, Bean.Zhaiquanxinxi.class);
                    if (zhai.data.bondList != null && zhai.data.bondList.size() > 0) {
                        list.add(zhai);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Goudixinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Goudixinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String goudixinxi = qingqiu(proxy, "http://www.tianyancha.com/expanse/purchaseland.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&ps=10000&pn=" + i);
                    Bean.Goudixinxi gou = gson.fromJson(goudixinxi, Bean.Goudixinxi.class);
                    if (gou.data.companyPurchaseLandList != null && gou.data.companyPurchaseLandList.size() > 0) {
                        list.add(gou);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhaopin implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhaopin(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zhaopin = qingqiu(proxy, "http://www.tianyancha.com/extend/getEmploymentList.json?companyName=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=10000");
                    Bean.Zhaopin zhao = gson.fromJson(zhaopin, Bean.Zhaopin.class);
                    if (zhao.data.companyEmploymentList != null && zhao.data.companyEmploymentList.size() > 0) {
                        list.add(zhao);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shuiwupingji implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shuiwupingji(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String shuiwupingji = qingqiu(proxy, "http://www.tianyancha.com/expanse/taxcredit.json?id=" + tid + "&ps=5&pn=" + i);
                    Bean.Shuiwupingji shui = gson.fromJson(shuiwupingji, Bean.Shuiwupingji.class);
                    if (shui.data.items != null && shui.data.items.size() > 0) {
                        list.add(shui);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Choucha implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Choucha(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String quancheng=str[1];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String choucha = qingqiu(proxy, "http://www.tianyancha.com/expanse/companyCheckInfo.json?name=" + URLEncoder.encode(quancheng, "UTF-8") + "&pn=" + i + "&ps=10000");
                    Bean.Choucha chou = gson.fromJson(choucha, Bean.Choucha.class);
                    if (chou.data.items != null && chou.data.items.size() > 0) {
                        list.add(chou);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Chanpin implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Chanpin(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String chanpin = qingqiu(proxy, "http://www.tianyancha.com/expanse/appbkinfo.json?id=" + tid + "&ps=5&pn=" + i);
                    Bean.Chanpin chan = gson.fromJson(chanpin, Bean.Chanpin.class);
                    if (chan.data.items != null && chan.data.items.size() > 0) {
                        list.add(chan);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zizhizhengshu implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zizhizhengshu(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zizhizhengshu = qingqiu(proxy, "http://www.tianyancha.com/expanse/qualification.json?id=" + tid + "&ps=5&pn=" + i);
                    Bean.Zizhizhzengshu zi = gson.fromJson(zizhizhengshu, Bean.Zizhizhzengshu.class);
                    if (zi.data.items != null && zi.data.items.size() > 0) {
                        list.add(zi);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Shangbiaoxinxi implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Shangbiaoxinxi(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String shangbiaoxinxi = qingqiu(proxy, "http://www.tianyancha.com/tm/getTmList.json?id=" + tid + "&pageNum=" + i + "&ps=10000");
                    Bean.Shangbiaoxinxi shang = gson.fromJson(shangbiaoxinxi, Bean.Shangbiaoxinxi.class);
                    if (shang.data.items != null && shang.data.items.size() > 0) {
                        list.add(shang);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhuanli implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhuanli(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zhuanli = qingqiu(proxy, "http://www.tianyancha.com/expanse/patent.json?id=" + tid + "&pn=" + i + "&ps=10000");
                    Bean.Zhuanli zhuan = gson.fromJson(zhuanli, Bean.Zhuanli.class);
                    if (zhuan.data.items != null && zhuan.data.items.size() > 0) {
                        list.add(zhuan);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Zhuzuoquan implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Zhuzuoquan(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            List<Object> list=new ArrayList<Object>();
            for(int i=1;i<=1000;i++){
                try {
                    String zhuzuoquan = qingqiu(proxy, "http://www.tianyancha.com/expanse/copyReg.json?id=" + tid + "&pn=" + i + "&ps=10000");
                    Bean.Zhuzuoquan zhu = gson.fromJson(zhuzuoquan, Bean.Zhuzuoquan.class);
                    if (zhu.data.items != null && zhu.data.items.size() > 0) {
                        list.add(zhu);
                    } else {
                        break;
                    }
                }catch (Exception e){

                }
            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Wangzhanbeian implements Runnable{
        private Proxy proxy;
        private Cangku cangku;
        private String[] str;
        public Wangzhanbeian(Proxy proxy,Cangku cangku,String[] str){
            this.proxy=proxy;
            this.cangku=cangku;
            this.str=str;
        }
        @Override
        public void run() {
            Gson gson=new Gson();
            String tid=str[0];
            List<Object> list=new ArrayList<Object>();
            try {
                String wangzhanbeian = qingqiu(proxy, "http://www.tianyancha.com/v2/IcpList/" + tid + ".json");
                Bean.Wangzhanbeian wang = gson.fromJson(wangzhanbeian, Bean.Wangzhanbeian.class);
                if (wang.data != null && wang.data.size() > 0) {
                    list.add(wang);
                }
            }catch (Exception e){

            }
            try {
                cangku.ru(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void get(Proxy proxy) throws InterruptedException, IOException {
        Gson gson=new Gson();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        File file=new File("C:\\Users\\Administrator\\Desktop\\xiaomi.txt");
        Document doc=Jsoup.parse(file,"xiaomi.txt","UTF-8");
        System.out.println();









        /*Elements eleduiwaitouzi=getElements(doc,"div[ng-if=dataItemCount.inverstCount>0] table.table.companyInfo-table tbody tr.ng-scope");
        if(eleduiwaitouzi!=null){
            for(Element e:eleduiwaitouzi){
                String beitouziqiyeming=getString(e,"td span.ng-binding",0);
                String beitouzifading=getString(e,"td span.ng-binding",1);
                String zhuceziben=getString(e,"td span.ng-binding",2);
                String touzishue=getString(e,"td span.ng-binding",3);
                String touzizhanbi=getString(e,"td span.ng-binding",4);
                String zhuceshijian=getString(e,"td span.ng-binding",5);
                String zhuangtai=getString(e,"td span.ng-binding",6);
                System.out.println(beitouziqiyeming+"   "+beitouzifading+"    "+zhuceziben+"    "+touzishue+"   "+touzizhanbi+"   "+zhuceshijian+"   "+zhuangtai);
            }
        }*/



        /*Elements elebiangeng=getElements(doc,"div[ng-if=items2.changeCount.show&&dataItemCount.changeCount>0] table.table.companyInfo-table tbody tr.ng-scope");
        if(elebiangeng!=null){
            for(Element e:elebiangeng){
                String biangengshijian=getString(e,"td:nth-child(1) div.ng-binding",0);
                String biangengxiangmu=getString(e,"td:nth-child(2) div.textJustFy.changeHoverText.ng-binding",0);
                String biangengqian=getString(e,"td:nth-child(3) div.ng-binding",0);
                String biangenghou=getString(e,"td:nth-child(4) div.textJustFy.changeHoverText.ng-binding",0);
                System.out.println(biangengshijian+"    "+biangengxiangmu+"    "+biangengqian+"    "+biangenghou);
            }
        }*/













    }




    public static String getString(Document doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getString(Element doc,String select,int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static String getHref(Document doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getHref(Element doc,String select,String href,int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static Elements getElements(Document doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc,String select,int a,String select2){
        Elements ele=null;
        try{
            ele=doc.select(select).get(a).select(select2);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }
}
