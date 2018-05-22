package simutong.xin_smt;

import Utils.Dup;
import Utils.JsoupUtils;
import Utils.RedisClu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class smt_xin {
    // 代理隧道验证信息
    final static String ProxyUser = "H6STQJ2G9011329D";
    final static String ProxyPass = "E946B835EC9D2ED7";

    // 代理服务器
    final static String ProxyHost = "http-dyn.abuyun.com";
    final static Integer ProxyPort = 9020;
    private static Proxy proxy;
    private static smt_utils sutils;
    private static Map<String,String> map;
    private static Connection conn;
    private static Ca c=new Ca();
    private static RedisClu rd=new RedisClu();

    static{
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
        sutils=new smt_utils(proxy);

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con!=null){
                    break;
                }
            }
        }

        conn=con;
    }
    public static void main(String args[]) throws IOException, InterruptedException, ParseException {
        //System.out.println(sutils.tzlist("http://pe.pedata.cn/getListInvest.action", String.valueOf(3), map, "2017-01-01", "2017-01-16"));
        //System.exit(0);

        String k=args[0];
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        /*pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    serach();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    detail();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    controller(Integer.parseInt(k));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    xin();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void controller(String key) throws IOException, InterruptedException {
        List<String[]> list=new ArrayList<>();
        String[] str1=new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","xiaohang.qu@lingweispace.cn","96e79218965eb72c92a549dd5a330112","dc:a9:04:91:9c:e6,0e:a9:04:91:9c:e6,b6:3b:f5:52:98:6a,c2:00:9c:f1:f4:01,c2:00:9c:f1:f4:00,c2:00:9c:f1:f4:00,0:0:0:0:0:0,0:0:0:0:0:0","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"cldr\":\"31.0.1\",\"tz\":\"2017b\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"darwin\"}"};
        String[] str2=new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","wang.hao@lingweispace.cn","96e79218965eb72c92a549dd5a330112","dc:a9:04:91:9c:e6,0e:a9:04:91:9c:e6,b6:3b:f5:52:98:6a,c2:00:9c:f1:f4:01,c2:00:9c:f1:f4:00,c2:00:9c:f1:f4:00,0:0:0:0:0:0,0:0:0:0:0:0","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"cldr\":\"31.0.1\",\"tz\":\"2017b\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"darwin\"}"};
        String[] str3=new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","chengyu.liang@lingweispace.cn","96e79218965eb72c92a549dd5a330112","84-EF-18-FE-2E-AA,84-EF-18-FE-2E-AE","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"win32\"}"};

        list.add(str1);
        list.add(str2);
        list.add(str3);
        Random r=new Random();

        while (true){
            try {
                for(String s:key.split(",")) {
                    map = sutils.login(list.get(Integer.parseInt(s)));
                    Thread.sleep((r.nextInt(2)+1)*60*60*1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void xin() throws SQLException, InterruptedException {
        while (true) {
            String sql = "select 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            Thread.sleep(60000);
        }
    }

    public static void controller(int k) throws IOException, InterruptedException {
        List<String[]> list=new ArrayList<>();
        String[] str1=new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","xiaohang.qu@lingweispace.cn","96e79218965eb72c92a549dd5a330112","dc:a9:04:91:9c:e6,0e:a9:04:91:9c:e6,b6:3b:f5:52:98:6a,c2:00:9c:f1:f4:01,c2:00:9c:f1:f4:00,c2:00:9c:f1:f4:00,0:0:0:0:0:0,0:0:0:0:0:0","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"cldr\":\"31.0.1\",\"tz\":\"2017b\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"darwin\"}"};
        String[] str2=new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","wang.hao@lingweispace.cn","96e79218965eb72c92a549dd5a330112","dc:a9:04:91:9c:e6,0e:a9:04:91:9c:e6,b6:3b:f5:52:98:6a,c2:00:9c:f1:f4:01,c2:00:9c:f1:f4:00,c2:00:9c:f1:f4:00,0:0:0:0:0:0,0:0:0:0:0:0","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"cldr\":\"31.0.1\",\"tz\":\"2017b\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"darwin\"}"};
        String[] str3=new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36","chengyu.liang@lingweispace.cn","96e79218965eb72c92a549dd5a330112","84-EF-18-FE-2E-AA,84-EF-18-FE-2E-AE","{\"http_parser\":\"2.7.0\",\"node\":\"9.1.0\",\"v8\":\"6.2.414.42\",\"uv\":\"1.15.0\",\"zlib\":\"1.2.11\",\"ares\":\"1.13.0\",\"modules\":\"59\",\"nghttp2\":\"1.25.0\",\"openssl\":\"1.0.2m\",\"icu\":\"59.1\",\"unicode\":\"9.0\",\"nw\":\"0.26.6\",\"node-webkit\":\"0.26.6\",\"nw-commit-id\":\"b12ee45-2da8f18-05043ec-58095c1\",\"chromium\":\"62.0.3202.94\",\"platform\":\"win32\"}"};

        list.add(str1);
        list.add(str2);
        list.add(str3);

        while (true){
            try {
                map = sutils.login(list.get(k));
                Thread.sleep(3600000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static boolean flagdata(String sid) throws SQLException {
        String sql="select id from si_institution_base_info where si_id='"+sid+"'";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String sql2="select id from si_fund_base_info where fund_id='"+sid+"'";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        ResultSet rs2=ps2.executeQuery();
        boolean bo=true;
        while (rs.next()){
            bo=false;
        }
        while (rs2.next()){
            bo=false;
        }
       return bo;
    }

    public static void serach() throws ParseException, IOException, InterruptedException {
        long start=System.currentTimeMillis();
        while (true) {
            if(map==null||map.size()==0){
                Thread.sleep(1000);
                continue;
            }
            int d = 0;
            String qi = "1900-01-01";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String liurls[] = new String[]{"http://pe.pedata.cn/getListOrg.action", "http://pe.pedata.cn/getListFund.action"};
            for (int q = 1; q <= 2832; q++) {
                String end = simpleDateFormat.format(simpleDateFormat.parse(qi).getTime() + 15 * 24 * 60 * 60 * 1000);
                if (simpleDateFormat.parse(end).getTime() > simpleDateFormat.parse("2018-01-01").getTime()) {
                    end = "2018-02-11";
                    q = 10000;
                }

                System.out.println(qi+"     "+end);
                for (int b = 0; b < liurls.length; b++) {
                    for (int a = 1; a <= 40; a++) {
                        try {
                            Document doc = null;
                            if (b == 0) {
                                doc = sutils.jglist(liurls[0], String.valueOf(a), map, qi, end);
                            } else if (b == 1) {
                                doc = sutils.jjlist(liurls[1], String.valueOf(a), map, qi, end);
                            }
                            boolean bo = true;
                            Elements ele = JsoupUtils.getElements(doc, "div.leftTableDivID.float_left.search_width20 table.table.table-hover tbody tr");
                            for (Element e : ele) {
                                bo = false;
                                String href = JsoupUtils.getHref(e, "td a", "href", 0);
                                System.out.println(href);

                                if (!href.contains("person") && Dup.nullor(href)) {
                                    if (flagdata(href.replace("getDetailED.action?param.qkid=", ""))) {
                                        rd.set("smt", href);
                                        d++;
                                        System.out.println(d + "*********************************************************************");
                                    }
                                }
                            }
                            if (bo) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                qi = end;
                long zh=System.currentTimeMillis();
                if(((zh-start)/1000/60/60)>=5){
                    Thread.sleep(3600000);
                    start=System.currentTimeMillis();
                }
            }
        }
    }

    public static void detail() throws IOException, InterruptedException {
        while (true) {
            if(map==null||map.size()==0){
                Thread.sleep(1000);
                continue;
            }
            int pp = 0;
            while (true) {
                try {
                    String href = rd.get("smt");
                    if(!Dup.nullor(href)){
                        Thread.sleep(1000);
                        continue;
                    }
                    Document doc = sutils.detail("http://pe.pedata.cn/" + href, map);
                    Elements tele = JsoupUtils.getElements(doc, "div.qy_detail_info p.qy_tag span");
                    StringBuffer str = new StringBuffer();
                    String tag = null;
                    for (Element e : tele) {
                        str.append(e.text() + ";");
                    }
                    if (str != null && str.length() > 2) {
                        tag = str.substring(0, str.length() - 1);
                    }
                    if (tag != null && tag.contains("基金")) {
                        jjdetail(href, doc);
                    }
                    if (tag != null && tag.contains("机构")) {
                        jgdetail(href, doc);
                    }
                    pp++;
                    System.out.println(pp + "-------------------------------------------------------------------------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void data() throws InterruptedException, SQLException {
        String sql = "insert into si_institution_base_info(si_id,inst_name,inst_short_name,inst_english_name,inst_logo,inst_type,inst_tag,refe_inst_name,main_captial_amount,is_on_amac,fund_num,intend_area,intend_industry,intend_standard,regulate_captial,organ_type,one_invest_amount,is_brostr_invest,intend_stage,create_date,regist_area,inst_headquarter,lp_captial_amount,social_credit_code,inst_industry,company_type,captial_type,lp_type,fund_raise,inst_background,inst_desc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="insert into si_institution_branch(si_id,inst_name,regist_num,responsible_person,online_project,regist_address,regist_authority) values(?,?,?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        String sql3="insert into si_institution_contacts(si_id,headquarter_type,contact_address,postal_code,contact_person,contact_phone,contact_fax,email) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps3=conn.prepareStatement(sql3);

        String sql4="insert into si_institution_invest_fund(si_id,fund_name,inst_name,inst_id,fund_type,raise_round,target_scale,invest_date,promise_amount) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps4=conn.prepareStatement(sql4);

        String sql5="insert into si_institution_invest_info(invest_si_id,invest_name,inst_name,subject_name,invest_tag,invest_industry,invest_area,invest_date,invest_round,invest_amount,stock_right,detail) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps5=conn.prepareStatement(sql5);

        String sql6="insert into si_institution_lower(si_id,inst_name,headquarter_area,regulate_captial,fund_num,invest_case) values(?,?,?,?,?,?)";
        PreparedStatement ps6=conn.prepareStatement(sql6);

        String sql7="insert into si_institution_main(si_id,member_name,member_position,member_telephone,member_email,member_entry_date,member_experience) values(?,?,?,?,?,?,?)";
        PreparedStatement ps7=conn.prepareStatement(sql7);

        String sql8="insert into si_institution_manage_fund(si_id,fund_name,fund_type,raise_status,raise_amount,raise_finish_date,invest_case,return_multiple) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps8=conn.prepareStatement(sql8);

        String sql9="insert into si_institution_exit_info(exit_si_id,exit_name,inst_si_id,inst_name,be_exited_id,be_exited_name,exit_type,exit_date,first_invest_date,exit_amount,return_multiple,inter_return_rate,detail) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps9=conn.prepareStatement(sql9);

        String sql10="insert into si_institution_news(news_title,relase_date,data_source,s_id) values(?,?,?,?)";
        PreparedStatement ps10=conn.prepareStatement(sql10);

        String sql11="insert into si_institution_rank_list(list_title,list_rank,inst_name,s_id) values(?,?,?,?)";
        PreparedStatement ps11=conn.prepareStatement(sql11);

        String sql12="insert into si_fund_base_info(fund_id,fund_name,fund_short_name,fund_english_name,fund_type,raise_status,organ_type,raise_begin_dates,inst_type,refe_inst_name,main_captial_amount,is_on_amac,fund_num,social_credit_code,fund_industry,company_type,captial_type,regulate_inst_id,regulate_inst_name,target_scale,raise_amount,regulate_captial,one_invest_amount,is_brostr_invest,intend_stage,create_date,regist_area,fund_headquarter,fund_background,fund_desc,fund_tag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps12=conn.prepareStatement(sql12);

        String sql13="insert into si_fund_manage_fund(si_id,fund_name,fund_type,raise_status,raise_amount,raise_finish_date,invest_case,return_multiple) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps13=conn.prepareStatement(sql13);

        String sql14="insert into si_fund_raise(fund_id,lp_name,lp_type,raise_amount,raise_finish_date,raise_round,promise_amount,invest_amount,invest_date) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps14=conn.prepareStatement(sql14);
        while (true) {
            try {
                Object o = c.qu();
                if (o instanceof smt_bean.jigou.base) {
                    ps.setString(1, ((smt_bean.jigou.base) o).getSid());
                    ps.setString(2, ((smt_bean.jigou.base) o).getQuan());
                    ps.setString(3, ((smt_bean.jigou.base) o).getJian());
                    ps.setString(4, ((smt_bean.jigou.base) o).getYquan());
                    ps.setString(5, ((smt_bean.jigou.base) o).getLogo());
                    ps.setString(6, ((smt_bean.jigou.base) o).getJilei());
                    ps.setString(7, ((smt_bean.jigou.base) o).getTag());
                    ps.setString(8, ((smt_bean.jigou.base) o).getSji());
                    ps.setString(9, ((smt_bean.jigou.base) o).getKeda());
                    ps.setString(10, ((smt_bean.jigou.base) o).getFlamac());
                    ps.setString(11, ((smt_bean.jigou.base) o).getGjis());
                    ps.setString(12, ((smt_bean.jigou.base) o).getNitd());
                    ps.setString(13, ((smt_bean.jigou.base) o).getNithy());
                    ps.setString(14, ((smt_bean.jigou.base) o).getTzbz());
                    ps.setString(15, ((smt_bean.jigou.base) o).getGlzbl());
                    ps.setString(16, ((smt_bean.jigou.base) o).getZzxs());
                    ps.setString(17, ((smt_bean.jigou.base) o).getDxtzje());
                    ps.setString(18, ((smt_bean.jigou.base) o).getFlqszt());
                    ps.setString(19, ((smt_bean.jigou.base) o).getNtjd());
                    ps.setString(20, ((smt_bean.jigou.base) o).getClsj());
                    ps.setString(21, ((smt_bean.jigou.base) o).getZcdq());
                    ps.setString(22, ((smt_bean.jigou.base) o).getGszb());
                    ps.setString(23, ((smt_bean.jigou.base) o).getLpgzbl());
                    ps.setString(24, ((smt_bean.jigou.base) o).getTysh());
                    ps.setString(25, ((smt_bean.jigou.base) o).getHy());
                    ps.setString(26, ((smt_bean.jigou.base) o).getGslx());
                    ps.setString(27, ((smt_bean.jigou.base) o).getZblx());
                    ps.setString(28, ((smt_bean.jigou.base) o).getLplx());
                    ps.setString(29, ((smt_bean.jigou.base) o).getCyjjmj());
                    ps.setString(30, ((smt_bean.jigou.base) o).getGybj());
                    ps.setString(31, ((smt_bean.jigou.base) o).getMs());
                    ps.executeUpdate();
                } else if (o instanceof smt_bean.jigou.fzjg) {
                    ps2.setString(1, ((smt_bean.jigou.fzjg) o).getSid());
                    ps2.setString(2, ((smt_bean.jigou.fzjg) o).getGsmc());
                    ps2.setString(3, ((smt_bean.jigou.fzjg) o).getZch());
                    ps2.setString(4, ((smt_bean.jigou.fzjg) o).getFzr());
                    ps2.setString(5, ((smt_bean.jigou.fzjg) o).getYbjy());
                    ps2.setString(6, ((smt_bean.jigou.fzjg) o).getZcdz());
                    ps2.setString(7, ((smt_bean.jigou.fzjg) o).getDjjg());
                    ps2.executeUpdate();
                } else if (o instanceof smt_bean.jigou.lxfs) {
                    ps3.setString(1, ((smt_bean.jigou.lxfs) o).getSid());
                    ps3.setString(2, ((smt_bean.jigou.lxfs) o).getZblx());
                    ps3.setString(3, ((smt_bean.jigou.lxfs) o).getAdd());
                    ps3.setString(4, ((smt_bean.jigou.lxfs) o).getYb());
                    ps3.setString(5, ((smt_bean.jigou.lxfs) o).getLxr());
                    ps3.setString(6, ((smt_bean.jigou.lxfs) o).getPhone());
                    ps3.setString(7, ((smt_bean.jigou.lxfs) o).getCz());
                    ps3.setString(8, ((smt_bean.jigou.lxfs) o).getEmail());
                    ps3.executeUpdate();
                } else if (o instanceof smt_bean.jigou.xmtc) {
                    ps9.setString(1, ((smt_bean.jigou.xmtc) o).getSid());
                    ps9.setString(2, ((smt_bean.jigou.xmtc) o).getTcf());
                    ps9.setString(3, ((smt_bean.jigou.xmtc) o).getGljgid());
                    ps9.setString(4, ((smt_bean.jigou.xmtc) o).getGljg());
                    ps9.setString(5, ((smt_bean.jigou.xmtc) o).getBtcfid());
                    ps9.setString(6, ((smt_bean.jigou.xmtc) o).getBtcf());
                    ps9.setString(7, ((smt_bean.jigou.xmtc) o).getTcfs());
                    ps9.setString(8, ((smt_bean.jigou.xmtc) o).getTcsj());
                    ps9.setString(9, ((smt_bean.jigou.xmtc) o).getSctzsj());
                    ps9.setString(10, ((smt_bean.jigou.xmtc) o).getTcje());
                    ps9.setString(11, ((smt_bean.jigou.xmtc) o).getHbbs());
                    ps9.setString(12, ((smt_bean.jigou.xmtc) o).getNbsy());
                    ps9.setString(13, ((smt_bean.jigou.xmtc) o).getXq());
                    ps9.executeUpdate();
                } else if (o instanceof smt_bean.jigou.tzjj) {
                    ps4.setString(1, ((smt_bean.jigou.tzjj) o).getSid());
                    ps4.setString(2, ((smt_bean.jigou.tzjj) o).getJjmc());
                    ps4.setString(3, ((smt_bean.jigou.tzjj) o).getGjjg());
                    ps4.setString(4, ((smt_bean.jigou.tzjj) o).getGljgid());
                    ps4.setString(5, ((smt_bean.jigou.tzjj) o).getJjlx());
                    ps4.setString(6, ((smt_bean.jigou.tzjj) o).getMjlc());
                    ps4.setString(7, ((smt_bean.jigou.tzjj) o).getMbgm());
                    ps4.setString(8, ((smt_bean.jigou.tzjj) o).getCzsj());
                    ps4.setString(9, ((smt_bean.jigou.tzjj) o).getCnje());
                    ps4.executeUpdate();
                } else if (o instanceof smt_bean.jigou.tzxm) {
                    ps5.setString(1, ((smt_bean.jigou.tzxm) o).getSid());
                    ps5.setString(2, ((smt_bean.jigou.tzxm) o).getTzf());
                    ps5.setString(3, ((smt_bean.jigou.tzxm) o).getGljg());
                    ps5.setString(4, ((smt_bean.jigou.tzxm) o).getBdf());
                    ps5.setString(5, ((smt_bean.jigou.tzxm) o).getTag());
                    ps5.setString(6, ((smt_bean.jigou.tzxm) o).getHy());
                    ps5.setString(7, ((smt_bean.jigou.tzxm) o).getDq());
                    ps5.setString(8, ((smt_bean.jigou.tzxm) o).getTzsj());
                    ps5.setString(9, ((smt_bean.jigou.tzxm) o).getLc());
                    ps5.setString(10, ((smt_bean.jigou.tzxm) o).getTzje());
                    ps5.setString(11, ((smt_bean.jigou.tzxm) o).getGq());
                    ps5.setString(12, ((smt_bean.jigou.tzxm) o).getXq());
                    ps5.executeUpdate();
                } else if (o instanceof smt_bean.jigou.xjjg) {
                    ps6.setString(1, ((smt_bean.jigou.xjjg) o).getSid());
                    ps6.setString(2, ((smt_bean.jigou.xjjg) o).getJgmc());
                    ps6.setString(3, ((smt_bean.jigou.xjjg) o).getZbdq());
                    ps6.setString(4, ((smt_bean.jigou.xjjg) o).getJggzbl());
                    ps6.setString(5, ((smt_bean.jigou.xjjg) o).getGjjs());
                    ps6.setString(6, ((smt_bean.jigou.xjjg) o).getTzal());
                    ps6.executeUpdate();
                } else if (o instanceof smt_bean.jigou.zycy) {
                    ps7.setString(1, ((smt_bean.jigou.zycy) o).getSid());
                    ps7.setString(2, ((smt_bean.jigou.zycy) o).getName());
                    ps7.setString(3, ((smt_bean.jigou.zycy) o).getZw());
                    ps7.setString(4, ((smt_bean.jigou.zycy) o).getPhone());
                    ps7.setString(5, ((smt_bean.jigou.zycy) o).getEmail());
                    ps7.setString(6, ((smt_bean.jigou.zycy) o).getRzsj());
                    ps7.setString(7, ((smt_bean.jigou.zycy) o).getNl());
                    ps7.executeUpdate();
                } else if (o instanceof smt_bean.jigou.gljj) {
                    ps8.setString(1, ((smt_bean.jigou.gljj) o).getSid());
                    ps8.setString(2, ((smt_bean.jigou.gljj) o).getJjmc());
                    ps8.setString(3, ((smt_bean.jigou.gljj) o).getJjlx());
                    ps8.setString(4, ((smt_bean.jigou.gljj) o).getMjzt());
                    ps8.setString(5, ((smt_bean.jigou.gljj) o).getMjje());
                    ps8.setString(6, ((smt_bean.jigou.gljj) o).getMjwcsj());
                    ps8.setString(7, ((smt_bean.jigou.gljj) o).getTzal());
                    ps8.setString(8, ((smt_bean.jigou.gljj) o).getNbsy());
                    ps8.executeUpdate();
                } else if (o instanceof smt_bean.jigou.xw) {
                    ps10.setString(1, ((smt_bean.jigou.xw) o).getBt());
                    ps10.setString(2, ((smt_bean.jigou.xw) o).getFbsj());
                    ps10.setString(3, ((smt_bean.jigou.xw) o).getLy());
                    ps10.setString(4, ((smt_bean.jigou.xw) o).getSid());
                    ps10.executeUpdate();
                } else if (o instanceof smt_bean.jigou.bd) {
                    ps11.setString(1, ((smt_bean.jigou.bd) o).getBiao());
                    ps11.setString(2, ((smt_bean.jigou.bd) o).getMing());
                    ps11.setString(3, ((smt_bean.jigou.bd) o).getGm());
                    ps11.setString(4, ((smt_bean.jigou.bd) o).getSid());
                    ps11.executeUpdate();
                } else if (o instanceof smt_bean.jijin.base) {
                    ps12.setString(1, ((smt_bean.jijin.base) o).getSid());
                    ps12.setString(2, ((smt_bean.jijin.base) o).getQuan());
                    ps12.setString(3, ((smt_bean.jijin.base) o).getJian());
                    ps12.setString(4, ((smt_bean.jijin.base) o).getYquan());
                    ps12.setString(5, ((smt_bean.jijin.base) o).getJjlx());
                    ps12.setString(6, ((smt_bean.jijin.base) o).getMjzt());
                    ps12.setString(7, ((smt_bean.jijin.base) o).getZzxs());
                    ps12.setString(8, ((smt_bean.jijin.base) o).getKsmj());
                    ps12.setString(9, ((smt_bean.jijin.base) o).getJglx());
                    ps12.setString(10, ((smt_bean.jijin.base) o).getSjjg());
                    ps12.setString(11, ((smt_bean.jijin.base) o).getJgktdl());
                    ps12.setString(12, ((smt_bean.jijin.base) o).getFlamac());
                    ps12.setString(13, ((smt_bean.jijin.base) o).getGljjsl());
                    ps12.setString(14, ((smt_bean.jijin.base) o).getTysh());
                    ps12.setString(15, ((smt_bean.jijin.base) o).getHy());
                    ps12.setString(16, ((smt_bean.jijin.base) o).getGslx());
                    ps12.setString(17, ((smt_bean.jijin.base) o).getZblx());
                    ps12.setString(18, ((smt_bean.jijin.base) o).getGljgid());
                    ps12.setString(19, ((smt_bean.jijin.base) o).getGljg());
                    ps12.setString(20, ((smt_bean.jijin.base) o).getMbgm());
                    ps12.setString(21, ((smt_bean.jijin.base) o).getMjje());
                    ps12.setString(22, ((smt_bean.jijin.base) o).getJgzb());
                    ps12.setString(23, ((smt_bean.jijin.base) o).getDxtj());
                    ps12.setString(24, ((smt_bean.jijin.base) o).getSfqs());
                    ps12.setString(25, ((smt_bean.jijin.base) o).getNtjd());
                    ps12.setString(26, ((smt_bean.jijin.base) o).getClsj());
                    ps12.setString(27, ((smt_bean.jijin.base) o).getZcdq());
                    ps12.setString(28, ((smt_bean.jijin.base) o).getGszb());
                    ps12.setString(29, ((smt_bean.jijin.base) o).getGybj());
                    ps12.setString(30, ((smt_bean.jijin.base) o).getMs());
                    ps12.setString(31, ((smt_bean.jijin.base) o).getTag());
                    ps12.executeUpdate();
                } else if (o instanceof smt_bean.jijin.gljj) {
                    ps13.setString(1, ((smt_bean.jijin.gljj) o).getSid());
                    ps13.setString(2, ((smt_bean.jijin.gljj) o).getJjmc());
                    ps13.setString(3, ((smt_bean.jijin.gljj) o).getJjlx());
                    ps13.setString(4, ((smt_bean.jijin.gljj) o).getMjzt());
                    ps13.setString(5, ((smt_bean.jijin.gljj) o).getMjje());
                    ps13.setString(6, ((smt_bean.jijin.gljj) o).getMjwc());
                    ps13.setString(7, ((smt_bean.jijin.gljj) o).getTzal());
                    ps13.setString(8, ((smt_bean.jijin.gljj) o).getNbsy());
                    ps13.executeUpdate();
                } else if (o instanceof smt_bean.jijin.jjmj) {
                    ps14.setString(1, ((smt_bean.jijin.jjmj) o).getSid());
                    ps14.setString(2, ((smt_bean.jijin.jjmj) o).getLp());
                    ps14.setString(3, ((smt_bean.jijin.jjmj) o).getLpsx());
                    ps14.setString(4, ((smt_bean.jijin.jjmj) o).getMjje());
                    ps14.setString(5, ((smt_bean.jijin.jjmj) o).getMjwc());
                    ps14.setString(6, ((smt_bean.jijin.jjmj) o).getLc());
                    ps14.setString(7, ((smt_bean.jijin.jjmj) o).getCncz());
                    ps14.setString(8, ((smt_bean.jijin.jjmj) o).getSjcz());
                    ps14.setString(9, ((smt_bean.jijin.jjmj) o).getCzsj());
                    ps14.executeUpdate();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void jgdetail(String href,Document doc) throws IOException, InterruptedException {
        smt_bean.jigou.base b=new smt_bean.jigou.base();
        b.setJilei(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构类型)+td",0));
        b.setGlzbl(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构管理资本量)+td",0));
        b.setSji(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(上级机构)+td",0));
        b.setZzxs(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(组织形式)+td",0));
        b.setKeda(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构可投大陆资本量)+td",0));
        b.setDxtzje(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(单项投资金额)+td",0));
        b.setFlamac(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(是否AMAC备案)+td",0));
        b.setFlqszt(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(是否劵商直投)+td",0));
        b.setGjis(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(管理基金数量)+td",0));
        b.setNitd(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(拟投阶段)+td",0));
        b.setNtjd(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(拟投地区)+td",0));
        b.setNithy(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(拟投行业)+td",0));
        b.setTzbz(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(投资标准)+td",0));
        b.setTysh(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(统一社会信用代码)+td",0));
        b.setClsj(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(成立时间)+td",0));
        b.setHy(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(行业)+td",0));
        b.setJian(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司简称)+td",0));
        b.setGslx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司类型)+td",0));
        b.setZcdq(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(注册地区)+td",0));
        b.setZblx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(资本类型)+td",0));
        b.setGszb(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司总部)+td",0));
        b.setLplx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(LP类型)+td",0));
        b.setLpgzbl(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(LP管理资本量)+td",0));
        b.setCyjjmj(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(参与基金募集)+td",0));
        b.setGybj(JsoupUtils.getString(doc,"div.table_border_content:contains(国有背景详情) p.table_border_content_indent+p",0));
        b.setQuan(JsoupUtils.getString(doc,"div.qy_detail_info p.qy_name",0));
        b.setYquan(JsoupUtils.getString(doc,"div.qy_detail_info p.qy_name_en",0));
        Elements tele=JsoupUtils.getElements(doc,"div.qy_detail_info p.qy_tag span");
        StringBuffer str=new StringBuffer();
        for(Element e:tele){
            str.append(e.text()+";");
        }
        if(str!=null&&str.length()>2) {
            b.setTag(str.substring(0,str.length()-1));
        }
        b.setLogo(JsoupUtils.getHref(doc,"div#entity_header_label div.qy_detail_header_content div.detail_logo img","src",0));
        b.setMs(JsoupUtils.getString(doc,"div.table_border_content:contains(描述)",0).replace("描述",""));
        String sid=href.replace("getDetailED.action?param.qkid=","");
        b.setSid(sid);
        c.fang(b);
        System.out.println("jiben ok");

        for(int a=1;a<=100;a++) {
            Document lxdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetContactED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(lxdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.lxfs l=new smt_bean.jigou.lxfs();
                l.setZblx(JsoupUtils.getString(e,"td",0));
                l.setLxr(JsoupUtils.getString(e,"td",1));
                l.setEmail(JsoupUtils.getString(e,"td",2));
                l.setPhone(JsoupUtils.getString(e,"td",3));
                l.setCz(JsoupUtils.getString(e,"td",4));
                l.setYb(JsoupUtils.getString(e,"td",5));
                l.setAdd(JsoupUtils.getString(e,"td",6));
                l.setSid(sid);
                c.fang(l);
            }
        }
        System.out.println("lianxi ok");


        for(int a=1;a<=100;a++){
            Document zydoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetMemberED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(zydoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.zycy z=new smt_bean.jigou.zycy();
                z.setName(JsoupUtils.getString(e,"td",0));
                z.setZw(JsoupUtils.getString(e,"td",1));
                z.setEmail(JsoupUtils.getString(e,"td",2));
                z.setPhone(JsoupUtils.getString(e,"td",3));
                z.setRzsj(JsoupUtils.getString(e,"td",4));
                z.setNl(JsoupUtils.getString(e,"td",5));
                z.setSid(sid);
                c.fang(z);
            }
        }
        System.out.println("zhuyao ok");


        for(int a=1;a<=100;a++){
            Document fzdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetBranchOrgED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(fzdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.fzjg f=new smt_bean.jigou.fzjg();
                f.setGsmc(JsoupUtils.getString(e,"td",0));
                f.setZch(JsoupUtils.getString(e,"td",1));
                f.setFzr(JsoupUtils.getString(e,"td",2));
                f.setYbjy(JsoupUtils.getString(e,"td",3));
                f.setZcdz(JsoupUtils.getString(e,"td",4));
                f.setDjjg(JsoupUtils.getString(e,"td",5));
                f.setSid(sid);
                c.fang(f);
            }
        }
        System.out.println("fenzhi ok");


        for(int a=1;a<=100;a++){
            Document xjdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetChildOrgED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(xjdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.xjjg x=new smt_bean.jigou.xjjg();
                x.setJgmc(JsoupUtils.getString(e,"td",0));
                x.setZbdq(JsoupUtils.getString(e,"td",1));
                x.setJggzbl(JsoupUtils.getString(e,"td",2));
                x.setGjjs(JsoupUtils.getString(e,"td",3));
                x.setTzal(JsoupUtils.getString(e,"td",4));
                x.setSid(sid);
                c.fang(x);
            }
        }
        System.out.println("xiaji ok");


        for(int a=1;a<=100;a++){
            Document gldoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetManageFundED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(gldoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.gljj g=new smt_bean.jigou.gljj();
                g.setJjmc(JsoupUtils.getString(e,"td",0));
                g.setJjlx(JsoupUtils.getString(e,"td",1));
                g.setMjzt(JsoupUtils.getString(e,"td",2));
                g.setMjje(JsoupUtils.getString(e,"td",3));
                g.setMjwcsj(JsoupUtils.getString(e,"td",4));
                g.setTzal(JsoupUtils.getString(e,"td",5));
                g.setNbsy(JsoupUtils.getString(e,"td",6));
                g.setSid(sid);
                c.fang(g);
            }
        }
        System.out.println("guanlijijin ok");


        for(int a=1;a<=100;a++){
            Document tzdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetInvestFundED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(tzdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.tzjj t=new smt_bean.jigou.tzjj();
                t.setJjmc(JsoupUtils.getString(e,"td",0));
                t.setGjjg(JsoupUtils.getString(e,"td",1));
                try {
                    t.setGljgid(e.select("td").get(1).select("a").attr("href").replace("getDetailED.action?param.qkid=", ""));
                }catch (Exception e1){
                    t.setGljgid("0");
                }
                t.setJjlx(JsoupUtils.getString(e,"td",2));
                t.setMjlc(JsoupUtils.getString(e,"td",3));
                t.setMbgm(JsoupUtils.getString(e,"td",4));
                t.setCzsj(JsoupUtils.getString(e,"td",5));
                t.setCnje(JsoupUtils.getString(e,"td",6));
                t.setSid(sid);
                c.fang(t);
            }
        }
        System.out.println("touzijijin ok");


        for(int a=1;a<=100;a++){
            Document txdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetInvestProjectED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(txdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.tzxm tx=new smt_bean.jigou.tzxm();
                tx.setTzf(JsoupUtils.getString(e,"td",0));
                tx.setGljg(JsoupUtils.getString(e,"td",1));
                tx.setBdf(JsoupUtils.getString(e,"td",2));
                tx.setTag(JsoupUtils.getString(e,"td",3));
                tx.setHy(JsoupUtils.getString(e,"td",4));
                tx.setDq(JsoupUtils.getString(e,"td",5));
                tx.setTzsj(JsoupUtils.getString(e,"td",6));
                tx.setLc(JsoupUtils.getString(e,"td",7));
                tx.setTzje(JsoupUtils.getString(e,"td",8));
                tx.setGq(JsoupUtils.getString(e,"td",9));
                try {
                    tx.setXq(e.select("td").get(10).select("a").attr("href"));
                }catch (Exception e1){
                    tx.setXq("");
                }
                tx.setSid(sid);
                c.fang(tx);
            }
        }
        System.out.println("touzixiangmu ok");


        for(int a=1;a<=100;a++){
            Document xtdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetProjectEixtED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(xtdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.xmtc xt=new smt_bean.jigou.xmtc();
                xt.setTcf(JsoupUtils.getString(e,"td",0));
                xt.setGljg(JsoupUtils.getString(e,"td",1));
                try {
                    xt.setGljgid(e.select("td").get(1).select("a").attr("href").replace("getDetailED.action?param.qkid=", ""));
                }catch (Exception e1){
                    xt.setGljgid("0");
                }
                xt.setBtcf(JsoupUtils.getString(e,"td",2));
                try {
                    xt.setBtcfid(e.select("td").get(2).select("a").attr("href").replace("getDetailED.action?param.qkid=", ""));
                }catch (Exception eee){
                    xt.setBtcfid("0");
                }
                xt.setTcfs(JsoupUtils.getString(e,"td",3));
                xt.setTcsj(JsoupUtils.getString(e,"td",4));
                xt.setSctzsj(JsoupUtils.getString(e,"td",5));
                xt.setTcje(JsoupUtils.getString(e,"td",6));
                xt.setHbbs(JsoupUtils.getString(e,"td",7));
                xt.setNbsy(JsoupUtils.getString(e,"td",8));
                try {
                    xt.setXq(e.select("td").get(9).select("a").attr("href"));
                }catch (Exception ee){
                    xt.setXq("");
                }
                xt.setSid(sid);
                c.fang(xt);
            }
        }
        System.out.println("tuichu ok");


        for(int a=1;a<=100;a++){
            Document xwdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetNewsED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(xwdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jigou.xw xw=new smt_bean.jigou.xw();
                xw.setFbsj(JsoupUtils.getString(e,"td",0));
                xw.setBt(JsoupUtils.getString(e,"td",1));
                xw.setLy(JsoupUtils.getString(e,"td",2));
                xw.setSid(sid);
                c.fang(xw);
            }
        }
        System.out.println("xinwen ok");


        for(int a=1;a<=100;a++){
            Document bddoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetRankED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(bddoc,"div.bd_detail_content div.bd_box");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                smt_bean.jigou.bd bd=new smt_bean.jigou.bd();
                bd.setBiao(JsoupUtils.getString(e,"div.bd_content p",0));
                bd.setMing(JsoupUtils.getString(e,"div.bd_content p.bd_name span.bd_pm",0));
                bd.setGm(JsoupUtils.getString(e,"div.bd_content p.bd_name span",1));
                bd.setSid(sid);
                c.fang(bd);
            }
        }
        System.out.println("bangdan ok");
    }

    public static void jjdetail(String href,Document doc) throws IOException, InterruptedException {
        smt_bean.jijin.base b=new smt_bean.jijin.base();
        b.setJjlx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(基金类型)+td",0));
        b.setGljg(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(管理机构)+td",0));
        try {
            b.setGljgid(doc.select("div#base_info_label div.detail_wrapper_table table tbody td:contains(管理机构)+td a").get(0).attr("href").replace("getDetailED.action?param.entityType=2&amp;param.qkid=", ""));
        }catch (Exception e){
            b.setGljgid("0");
        }
        b.setMjzt(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(募集状态)+td",0));
        b.setMbgm(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(目标规模)+td",0));
        b.setZzxs(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(组织形式)+td",0));
        b.setFlamac(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(是否AMAC备案)+td",0));
        b.setKsmj(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(开始募集时间)+td",0));
        b.setMjje(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(募集金额)+td",0));
        b.setTysh(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(统一社会信用代码)+td",0));
        b.setClsj(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(成立时间)+td",0));
        b.setHy(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(行业)+td",0));
        b.setJian(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司简称)+td",0));
        b.setGslx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司类型)+td",0));
        b.setZcdq(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(注册地区)+td",0));
        b.setZblx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(资本类型)+td",0));
        b.setGszb(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(公司总部)+td",0));
        b.setGybj(JsoupUtils.getString(doc,"div.table_border_content:contains(国有背景详情) p.table_border_content_indent+p",0));
        b.setJglx(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构类型)+td",0));
        b.setJgzb(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构管理资本量)+td",0));
        b.setSjjg(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(上级机构)+td",0));
        b.setDxtj(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(单项投资金额)+td",0));
        b.setSfqs(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(是否劵商直投)+td",0));
        b.setGljjsl(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(管理基金数量)+td",0));
        b.setNtjd(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(拟投阶段)+td",0));
        b.setQuan(JsoupUtils.getString(doc,"div.qy_detail_info p.qy_name",0));
        b.setYquan(JsoupUtils.getString(doc,"div.qy_detail_info p.qy_name_en",0));
        b.setJgktdl(JsoupUtils.getString(doc,"div#base_info_label div.detail_wrapper_table table tbody td:contains(机构可投大陆资本量)+td",0));
        Elements tele=JsoupUtils.getElements(doc,"div.qy_detail_info p.qy_tag span");
        StringBuffer str=new StringBuffer();
        for(Element e:tele){
            str.append(e.text()+";");
        }
        if(str!=null&&str.length()>2) {
            b.setTag(str.substring(0,str.length()-1));
        }
        b.setMs(JsoupUtils.getString(doc,"div.table_border_content:contains(描述)",0).replace("描述",""));
        String sid=href.replace("getDetailED.action?param.qkid=","");
        b.setSid(sid);
        c.fang(b);
        System.out.println("jijin ok");



        for(int a=1;a<=100;a++){
            Document jmdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetFundRaiseED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(jmdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele==null){
                break;
            }
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jijin.jjmj jm=new smt_bean.jijin.jjmj();
                jm.setLc(JsoupUtils.getString(e,"td",0));
                jm.setMjwc(JsoupUtils.getString(e,"td",1));
                jm.setMjje(JsoupUtils.getString(e,"td",2));
                jm.setLp(JsoupUtils.getString(e,"td",3));
                jm.setLpsx(JsoupUtils.getString(e,"td",4));
                jm.setCncz(JsoupUtils.getString(e,"td",5));
                jm.setSjcz(JsoupUtils.getString(e,"td",6));
                jm.setCzsj(JsoupUtils.getString(e,"td",7));
                jm.setSid(sid);
                c.fang(jm);
            }
        }
        System.out.println("jijinmuji ok");


        for(int a=1;a<=100;a++){
            Document gjdoc = sutils.lxfs("http://pe.pedata.cn/ajaxGetManageFundED.action", map, sid, String.valueOf(a));
            Elements ele=JsoupUtils.getElements(gjdoc,"div.detail_wrapper_table table tbody tr.table_bd.info_table");
            if(ele.size()<=1){
                break;
            }
            int p=0;
            for(Element e:ele) {
                p++;
                if(p==1){
                    continue;
                }
                smt_bean.jijin.gljj gj=new smt_bean.jijin.gljj();
                gj.setJjmc(JsoupUtils.getString(e,"td",0));
                gj.setJjlx(JsoupUtils.getString(e,"td",1));
                gj.setMjzt(JsoupUtils.getString(e,"td",2));
                gj.setMjje(JsoupUtils.getString(e,"td",3));
                gj.setMjwc(JsoupUtils.getString(e,"td",4));
                gj.setTzal(JsoupUtils.getString(e,"td",5));
                gj.setNbsy(JsoupUtils.getString(e,"td",6));
                gj.setSid(sid);
                c.fang(gj);
            }
        }
        System.out.println("guanlijijin ok");
    }

    public static class Ca{
        BlockingQueue<Object> po=new LinkedBlockingQueue<>();
        public void fang(Object obj) throws InterruptedException {
            po.put(obj);
        }
        public Object qu() throws InterruptedException {
            return po.take();
        }
    }
}
