package simutong.xin_smt;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Random;

public class smt_utils {
    private static Proxy proxy;
    public smt_utils(Proxy proxy){
        this.proxy=proxy;
    }

    public int getran(){
        Random random=new Random();
        return (random.nextInt(2)+1)*1000;
    }
    public Map<String,String> login(String[] str) throws IOException {
        Connection.Response doc;
        while (true){
            try {
                doc = Jsoup.connect("http://pe.pedata.cn/ajaxLoginMember.action")
                        .userAgent(str[0])
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Host", "pe.pedata.cn")
                        .header("Origin", "chrome-extension://pjkgicchjgaacdbclmpmbhpkcipedjid")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .data("param.loginName", str[1])
                        .data("param.pwd", str[2])
                        .data("param.iscs", "true")
                        .data("param.macaddress", str[3])
                        .data("param.language", "zh_CN")
                        .data("request_locale", "zh_CN")
                        .data("param.versions", str[4])
                        .data("param.enmode", "1")
                        .timeout(5000)
                        .method(Connection.Method.POST)
                        .proxy(proxy)
                        .execute();
                if (doc != null && doc.body().length() > 50 && !doc.body().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }
        }
        return doc.cookies();
    }
    public Document jglist(String url,String page,Map<String,String> map,String begin,String end) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc = null;
        int q=0;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .data("param.quick", "")
                        .data("param.orderBy", "desc")
                        .data("param.orderByField", "history_invest_count")
                        .data("param.search_type", "base")
                        .data("param.showInfo", "true")
                        .data("param.search_type_check", "ownerCheck,conditionsUl,")
                        .data("param.org_manage_capital_begin", "")
                        .data("param.org_manage_capital_end", "")
                        .data("param.orgBackground", "")
                        .data("param.org_record", "")
                        .data("param.org_setup_date_begin", begin)
                        .data("param.org_setup_date_end", end)
                        .data("param.org_setup_date_base_lable_id","自定义")
                        .data("param.fund_goal_money_us_begin", "")
                        .data("param.fund_goal_money_us_end", "")
                        .data("param.fund_record", "")
                        .data("param.fund_setup_date_begin", "")
                        .data("param.fund_setup_date_end", "")
                        .data("param.epValue__start", "")
                        .data("param.epValue__end", "")
                        .data("param.epSetupDate_begin", "")
                        .data("param.epSetupDate_end", "")
                        .data("param.invest_money_begin", "")
                        .data("param.invest_money_end", "")
                        .data("param.invest_stake_start", "")
                        .data("param.invest_stake_end", "")
                        .data("param.invest_enterprise_start", "")
                        .data("param.invest_enterprise_end", "")
                        .data("param.invest_date_begin", "")
                        .data("param.invest_date_end", "")
                        .data("param.column", "0")
                        .data("param.column", "1")
                        .data("param.column", "2")
                        .data("param.column", "3")
                        .data("param.column", "4")
                        .data("param.column", "5")
                        .data("param.column", "6")
                        .data("param.column", "12")
                        .data("param.currentPage", page)
                        .timeout(5000)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }finally {
                q++;
                if(q>=10){
                    break;
                }
            }
        }
        return doc;
    }

    public Document jjlist(String url,String page,Map<String,String> map,String begin,String end) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc = null;
        int q=0;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .data("param.quick", "")
                        .data("param.orderBy", "desc")
                        .data("param.orderByField", "fund_setup_date")
                        .data("param.search_type", "base")
                        .data("param.showInfo", "true")
                        .data("param.search_type_check", "ownerCheck,conditionsUl,")
                        .data("param.fund_goal_money_us_begin", "")
                        .data("param.fund_goal_money_us_end", "")
                        .data("param.fund_record", "")
                        .data("param.fund_setup_date_begin", begin)
                        .data("param.fund_setup_date_end", end)
                        .data("param.fund_setup_date_base_lable_id","自定义")
                        .data("param.fund_raise_date_start_begin", "")
                        .data("param.fund_raise_date_start_end", "")
                        .data("param.raise_complete_date_begin", "")
                        .data("param.raise_complete_date_end", "")
                        .data("param.column", "0")
                        .data("param.column", "1")
                        .data("param.column", "2")
                        .data("param.column", "5")
                        .data("param.column", "6")
                        .data("param.column", "7")
                        .data("param.column", "8")
                        .data("param.column", "9")
                        .data("param.column", "10")
                        .data("param.column", "11")
                        .data("param.column", "12")
                        .data("param.currentPage", page)
                        .timeout(5000)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }finally {
                q++;
                if(q>=10){
                    break;
                }
            }
        }
        return doc;
    }

    public Document tzlist(String url,String page,Map<String,String> map,String begin,String end) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc = null;
        int q=0;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .data("param.quick", "")
                        .data("param.orderBy", "desc")
                        .data("param.orderByField", "invest_date")
                        .data("param.search_type", "base")
                        .data("param.showInfo", "true")
                        .data("param.search_type_check", "ownerCheck,conditionsUl,")
                        .data("param.investRound", "2491")
                        .data("param.investRound", "4122")
                        .data("param.investRound", "7641")
                        .data("param.investRound", "1061")
                        .data("param.investRound", "1064")
                        .data("param.invest_money_begin", "")
                        .data("param.invest_money_end", "")
                        .data("param.invest_stake_start", "")
                        .data("param.invest_stake_end", "")
                        .data("param.invest_enterprise_start", "")
                        .data("param.invest_enterprise_end", "")
                        .data("param.invest_date_begin", begin)
                        .data("param.invest_date_end", end)
                        .data("param.invest_date_base_lable_id", "自定义")
                        .data("param.epValue__start", "")
                        .data("param.epValue__end", "")
                        .data("param.epSetupDate_begin", "yyyy-mm-dd")
                        .data("param.epSetupDate_end", "yyyy-mm-dd")
                        .data("param.isTargetIpo", "")
                        .data("param.orgBackground", "")
                        .data("param.orgRecord", "")
                        .data("param.isSecuritiesBlocker", "")
                        .data("param.fund_record", "")
                        .data("param.fund_setup_date_begin", "yyyy-mm-dd")
                        .data("param.fund_setup_date_end", "yyyy-mm-dd")
                        .data("param.column", "0")
                        .data("param.column", "1")
                        .data("param.column", "5")
                        .data("param.column", "6")
                        .data("param.column", "7")
                        .data("param.column", "8")
                        .data("param.column", "9")
                        .data("param.column", "10")
                        .data("param.column", "11")
                        .data("param.column", "12")
                        .data("param.column", "13")
                        .data("param.column", "14")
                        .data("param.currentPage", page)
                        .timeout(5000)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }finally {
                q++;
                if(q>=10){
                    break;
                }
            }
        }
        return doc;
    }

    public Document tclist(String url,String page,Map<String,String> map,String begin,String end) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc=null;
        int q=0;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .data("param.quick", "")
                        .data("param.orderBy", "desc")
                        .data("param.orderByField", "exit_date")
                        .data("param.search_type", "base")
                        .data("param.showInfo", "true")
                        .data("param.search_type_check", "ownerCheck,conditionsUl,")
                        .data("param.exit_date_start", begin)
                        .data("param.exit_date_end", end)
                        .data("param.eixt_date_base_lable_id", "自定义")
                        .data("param.exit_return_money_start", "")
                        .data("param.exit_return_money_end", "")
                        .data("param.exit_return_multiple_start", "")
                        .data("param.exit_return_multiple_end", "")
                        .data("param.exit_return_rate_start", "")
                        .data("param.exit_return_rate_end", "")
                        .data("param.epValue__start", "")
                        .data("param.epValue__end", "")
                        .data("param.epSetupDate_begin", "yyyy-mm-dd")
                        .data("param.epSetupDate_end", "yyyy-mm-dd")
                        .data("param.orgBackground", "")
                        .data("param.orgRecord", "")
                        .data("param.fund_record", "")
                        .data("param.fund_setup_date_begin", "yyyy-mm-dd")
                        .data("param.fund_setup_date_end", "yyyy-mm-dd")
                        .data("param.column", "0")
                        .data("param.column", "1")
                        .data("param.column", "4")
                        .data("param.column", "5")
                        .data("param.column", "6")
                        .data("param.column", "7")
                        .data("param.column", "8")
                        .data("param.column", "9")
                        .data("param.column", "10")
                        .data("param.currentPage", page)
                        .timeout(5000)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }finally {
                q++;
                if(q>=10){
                    break;
                }
            }
        }
        return doc;
    }

    public Document detail(String url,Map<String,String> map) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc=null;
        int q=0;
        while (true){
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .timeout(5000)
                        .proxy(proxy)
                        .get();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            }catch (Exception e){

            }finally {
                q++;
                if(q>=10){
                    break;
                }
            }
        }
        return doc;
    }

    public Document lxfs(String url,Map<String,String> map,String sid,String page) throws IOException, InterruptedException {
        Thread.sleep(getran());
        Document doc = null;
        int q=0;
        while (true) {
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Origin", "http://pe.pedata.cn")
                        .header("Host", "pe.pedata.cn")
                        .cookies(map)
                        .data("param.qkid", sid)
                        .data("param.pageSize", "5")
                        .data("param.currentPage", page)
                        .timeout(5000)
                        .proxy(proxy)
                        .post();
                if (doc != null && doc.outerHtml().length() > 50 && !doc.outerHtml().contains("abuyun")) {
                    break;
                }
            } catch (Exception e) {

            } finally {
                q++;
                if (q >= 10) {
                    break;
                }
            }
        }
        return doc;
    }

}
