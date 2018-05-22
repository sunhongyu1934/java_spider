package qichacha;

import Utils.Dup;
import Utils.JsoupUtils;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

public class Modular {
    private Data data=new Data();
    public static void main(String args[]){
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique=9cce0780ab7644008b73bc2120479d31&companyname=%E5%B0%8F%E7%B1%B3%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8&tab=fengxian");
        System.out.println(doc.outerHtml());
    }

    public void gudong(Document doc,String cname) throws SQLException {
        Elements elements= JsoupUtils.getElements(doc,"section#Sockinfo:contains(股东信息) table.ntable.ntable-odd tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                String p_name=JsoupUtils.getString(e,"td",1).split(" ")[0];
                String chuzi_bili=JsoupUtils.getString(e,"td",2);
                String renjiao_chuzi=JsoupUtils.getString(e,"td",3);
                String renjiao_riqi=JsoupUtils.getString(e,"td",4);
                String gudong_type=JsoupUtils.getString(e,"td",5);

                data.ps1.setString(1,cname);
                data.ps1.setString(2,p_name);
                data.ps1.setString(3,chuzi_bili);
                data.ps1.setString(4,renjiao_chuzi);
                data.ps1.setString(5,renjiao_riqi);
                data.ps1.setString(6,gudong_type);
                data.ps1.executeUpdate();
            }

            a++;
        }
    }

    public void duiwaitouzi(Document doc,String url,String cname) throws UnsupportedEncodingException, SQLException {
        int sum=Integer.parseInt(JsoupUtils.getString(doc,"section#touzilist:contains(对外投资) span.tbadge",0));
        Elements elements=JsoupUtils.getElements(doc,"section#touzilist:contains(对外投资) table.ntable.ntable-odd tr");
        for(int p=1;p<=((sum/10)+1);p++){
            if(p>=2){
                doc = spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+ URLEncoder.encode(cname,"utf-8")+"&p="+p+"&tab=base&box=touzi");
                elements=JsoupUtils.getElements(doc,"table.ntable.ntable-odd tr");
            }
            int a=0;
            for(Element e:elements){
                if(a!=0){
                    String invest_name=JsoupUtils.getString(e,"td",0);
                    String representative=JsoupUtils.getString(e,"td",1);
                    String register_amount=JsoupUtils.getString(e,"td",2);
                    String investment_rate=JsoupUtils.getString(e,"td",3);
                    String register_date=JsoupUtils.getString(e,"td",4);
                    String com_status=JsoupUtils.getString(e,"td",5);

                    data.ps2.setString(1,cname);
                    data.ps2.setString(2,invest_name);
                    data.ps2.setString(3,representative);
                    data.ps2.setString(4,register_amount);
                    data.ps2.setString(5,register_date);
                    data.ps2.setString(6,investment_rate);
                    data.ps2.setString(7,com_status);
                    data.ps2.executeUpdate();
                }

                a++;
            }
        }
    }

    public void zhaopin(String url,String cname) throws UnsupportedEncodingException, SQLException, InterruptedException {
        Thread.sleep(2000);
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=run");
        Elements elements=JsoupUtils.getElements(doc,"section#joblist:contains(招聘) table.ntable.ntable-odd tr");
        int sum=Integer.parseInt(JsoupUtils.getString(doc,"section#joblist:contains(招聘) span.tbadge",0));
        for(int p=1;p<=((sum/10)+1);p++){
            Thread.sleep(2000);
            if(p>=2){
                doc = spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+ URLEncoder.encode(cname,"utf-8")+"&p="+p+"&tab=run&box=job");
                elements=JsoupUtils.getElements(doc,"table.ntable.ntable-odd tr");
            }
            System.out.println(p);
            int a=0;
            for(Element e:elements){
                if(a!=0){
                    String rec_time=JsoupUtils.getString(e,"td",1);
                    String rec_position=JsoupUtils.getString(e,"td",2);
                    String rec_salary=JsoupUtils.getString(e,"td",3);
                    String rec_city=JsoupUtils.getString(e,"td",4);
                    String rec_source=JsoupUtils.getHref(e,"td:nth-child(6) a","href",0);

                    data.ps3.setString(1,cname);
                    data.ps3.setString(2,rec_time);
                    data.ps3.setString(3,rec_position);
                    data.ps3.setString(4,rec_salary);
                    data.ps3.setString(5,rec_city);
                    data.ps3.setString(6,rec_source);
                    data.ps3.executeUpdate();
                }
                a++;
            }
        }
    }

    public void weixin(String url,String cname) throws UnsupportedEncodingException, SQLException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=run");
        Elements elements=JsoupUtils.getElements(doc,"section#wechatlist:contains(微信公众号) table.ntable.ntable-odd tr");
        int sum=Integer.parseInt(JsoupUtils.getString(doc,"section#wechatlist:contains(微信公众号) span.tbadge",0));
        for(int p=1;p<=((sum/10)+1);p++){
            if(p>=2){
                doc = spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+ URLEncoder.encode(cname,"utf-8")+"&p="+p+"&tab=run&box=wechat");
                elements=JsoupUtils.getElements(doc,"table.ntable.ntable-odd tr");
            }
            int a=0;
            for(Element e:elements){
                if(a!=0){
                    if(a%2==0) {
                        String w_logo = JsoupUtils.getHref(e, "td:nth-child(2) img", "src", 0);
                        String w_ming = JsoupUtils.getString(e, "td", 2);
                        String w_hao = JsoupUtils.getString(e, "td", 3);
                        String w_erwei = JsoupUtils.getHref(e, "td:nth-child(5) div img", "src", 0);
                        String w_desc = JsoupUtils.getString(e, "td", 5);

                        data.ps4.setString(1, cname);
                        data.ps4.setString(2, w_logo);
                        data.ps4.setString(3, w_ming);
                        data.ps4.setString(4, w_hao);
                        data.ps4.setString(5, w_erwei);
                        data.ps4.setString(6, w_desc);
                        data.ps4.executeUpdate();
                    }
                }

                a++;
            }
        }
    }

    public void zhaotoubiao(String url,String cname) throws UnsupportedEncodingException, SQLException, InterruptedException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=run");
        Elements elements=JsoupUtils.getElements(doc,"section#tenderlist:contains(招投标信息) table.ntable.ntable-odd tr");
        int sum=Integer.parseInt(JsoupUtils.getString(doc,"section#tenderlist:contains(招投标信息) span.tbadge",0));
        for(int p=1;p<=((sum/10)+1);p++){
            Thread.sleep(2000);
            if(p>=2){
                doc = spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+ URLEncoder.encode(cname,"utf-8")+"&p="+p+"&tab=run&box=tender");
                elements=JsoupUtils.getElements(doc,"table.ntable.ntable-odd tr");
            }
            int a=0;
            for(Element e:elements){
                if(a!=0){
                    String bidd_title=JsoupUtils.getString(e,"td",1);
                    String release_time=JsoupUtils.getString(e,"td",2);
                    String suoshu_diqu=JsoupUtils.getString(e,"td",3);
                    String xiangmu_fenlei=JsoupUtils.getString(e,"td",4);

                    data.ps5.setString(1,cname);
                    data.ps5.setString(2,release_time);
                    data.ps5.setString(3,bidd_title);
                    data.ps5.setString(4,suoshu_diqu);
                    data.ps5.setString(5,xiangmu_fenlei);
                    data.ps5.executeUpdate();
                }

                a++;
            }
        }
    }

    public void xingchugong(String url,String cname) throws UnsupportedEncodingException, SQLException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=fengxian");
        Elements elements=JsoupUtils.getElements(doc,"section#penaltylist:contains(行政处罚 [工商局]) table.ntable.ntable-odd tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                String jueding_wenshuhao=JsoupUtils.getString(e,"td",1);
                String weifa_leixing=JsoupUtils.getString(e,"td",2);
                String sanct_detail=JsoupUtils.getString(e,"td",3);
                String gongshi_time=JsoupUtils.getString(e,"td",4);
                String decision_org=JsoupUtils.getString(e,"td",5);
                String sanct_time=JsoupUtils.getString(e,"td",6);

                data.ps6.setString(1,cname);
                data.ps6.setString(2,sanct_time);
                data.ps6.setString(3,gongshi_time);
                data.ps6.setString(4,decision_org);
                data.ps6.setString(5,sanct_detail);
                data.ps6.setString(6,weifa_leixing);
                data.ps6.setString(7,jueding_wenshuhao);
                data.ps6.executeUpdate();
            }

            a++;
        }
    }

    public void xingchuzh(String url,String cname) throws UnsupportedEncodingException, SQLException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=fengxian");
        Elements elements=JsoupUtils.getElements(doc,"section.panel.clear.b-a:contains(行政处罚 [信用中国]) table.ntable.ntable-odd tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                String jueding_wenshuhao=JsoupUtils.getString(e,"td",1);
                String chufa_detail=JsoupUtils.getHref(e,"td:nth-child(2) a","onclick",0);
                String chufa_ming=JsoupUtils.getString(e,"td",2);
                String di_yu=JsoupUtils.getString(e,"td",3);
                String jueding_time=JsoupUtils.getString(e,"td",4);

                data.ps7.setString(1,cname);
                data.ps7.setString(2,jueding_wenshuhao);
                data.ps7.setString(3,chufa_detail);
                data.ps7.setString(4,chufa_ming);
                data.ps7.setString(5,di_yu);
                data.ps7.setString(6,jueding_time);
                data.ps7.executeUpdate();
            }

            a++;
        }
    }

    public void biangeng(Document doc,String cname) throws UnsupportedEncodingException, SQLException {
        Elements elements=JsoupUtils.getElements(doc,"section#Changelist:contains(变更记录) table.ntable tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                String change_time=JsoupUtils.getString(e,"td",1);
                String change_item=JsoupUtils.getString(e,"td",2);
                String before_change=JsoupUtils.getString(e,"td",3);
                String after_change=JsoupUtils.getString(e,"td",4);

                data.ps8.setString(1,cname);
                data.ps8.setString(2,change_time);
                data.ps8.setString(3,change_item);
                data.ps8.setString(4,before_change);
                data.ps8.setString(5,after_change);
                data.ps8.executeUpdate();
            }

            a++;
        }
    }

    public void fenzhi(Document doc,String url,String cname) throws UnsupportedEncodingException, SQLException {
        Elements elements=JsoupUtils.getElements(doc,"section#Subcom:contains(分支机构) table.ntable tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                String branch_name=JsoupUtils.getString(e,"td",1);
                String branch_url=JsoupUtils.getHref(e,"td:nth-child(2) a","href",0);

                String branch_name1=JsoupUtils.getString(e,"td",3);
                String branch_url1=JsoupUtils.getHref(e,"td:nth-child(4) a","href",0);

                data.ps9.setString(1,cname);
                data.ps9.setString(2,branch_name);
                data.ps9.setString(3,branch_url);
                data.ps9.executeUpdate();

                if(Dup.nullor(branch_name1)) {
                    data.ps9.setString(1, cname);
                    data.ps9.setString(2, branch_name1);
                    data.ps9.setString(3, branch_url1);
                    data.ps9.executeUpdate();
                }
            }

            a++;
        }
    }

    public void shuixin(String url,String cname) throws UnsupportedEncodingException, SQLException {
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=run");
        Elements elements=JsoupUtils.getElements(doc,"section#taxCreditList:contains(税务信用) table.ntable.ntable-odd tr");
        int a=0;
        for(Element e:elements){
            if(a!=0){
                if(a%2==0) {
                    String pingjia_nian = JsoupUtils.getString(e, "td", 1);
                    String nashui_hao = JsoupUtils.getString(e, "td", 2);
                    String nashui_ji = JsoupUtils.getString(e, "td", 3);
                    String ping_dan = JsoupUtils.getString(e, "td", 4);

                    data.ps10.setString(1, cname);
                    data.ps10.setString(2, pingjia_nian);
                    data.ps10.setString(3, nashui_hao);
                    data.ps10.setString(4, nashui_ji);
                    data.ps10.setString(5, ping_dan);
                    data.ps10.executeUpdate();
                }
            }

            a++;
        }
    }

    public void caipan(String url,String cname) throws UnsupportedEncodingException, SQLException, InterruptedException {
        Thread.sleep(2000);
        Document doc=spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+URLEncoder.encode(cname,"utf-8")+"&tab=susong");
        Elements elements=JsoupUtils.getElements(doc,"section#wenshulist:contains(裁判文书) table.ntable.ntable-odd tr");
        int sum=Integer.parseInt(JsoupUtils.getString(doc,"section#wenshulist:contains(裁判文书) span.tbadger",0));
        for(int p=1;p<=((sum/10)+1);p++){
            Thread.sleep(2000);
            if(p>=2){
                doc = spider.getDetail("http://www.qichacha.com/company_getinfos?unique="+url.replace("http://www.qichacha.com/firm_","").replace(".html","")+"&companyname="+ URLEncoder.encode(cname,"utf-8")+"&p="+p+"&tab=susong&box=wenshu&casetype=");
                elements=JsoupUtils.getElements(doc,"table.ntable.ntable-odd tr");
            }
            int a=0;
            for(Element e:elements){
                if(a!=0){
                    String anjian_ming=JsoupUtils.getString(e,"td",1);
                    String anjian_url=JsoupUtils.getHref(e,"td:nth-child(2) a","onclick",0);
                    String fabu_time=JsoupUtils.getString(e,"td",2);
                    String anjian_bianhao=JsoupUtils.getString(e,"td",3);
                    String anjian_shen=JsoupUtils.getString(e,"td",4);
                    String zhixing_fayuan=JsoupUtils.getString(e,"td",5);

                    data.ps11.setString(1,cname);
                    data.ps11.setString(2,anjian_ming);
                    data.ps11.setString(3,anjian_url);
                    data.ps11.setString(4,fabu_time);
                    data.ps11.setString(5,anjian_bianhao);
                    data.ps11.setString(6,anjian_shen);
                    data.ps11.setString(7,zhixing_fayuan);
                    data.ps11.executeUpdate();
                }

                a++;
            }
        }
    }


}
