/*
package com.tmzs.crawl.text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.tmzs.crawl.bean.Page;
import com.tmzs.crawl.bean.Source;
import com.tmzs.crawl.bean.Stat;
import com.tmzs.crawl.jdbc.MySqlSession;
import com.tmzs.crawl.dao.StatMapper;
import com.tmzs.crawl.pool.PageStorage;
import com.tmzs.crawl.pool.SourceStorage;
import com.tmzs.crawl.util.CommonUtil;
import com.tmzs.crawl.util.PinyinUtils;
import com.tmzs.crawl.util.PropertyUtil;
import com.tmzs.crawl.util.UrlFilter;
import com.tmzs.crawl.util.Utils;

public class CrawlLinkThread extends Thread{
	static Logger logger = Logger.getLogger(CrawlLinkThread.class.getName());
	private PageStorage linkStorage;
	private SourceStorage sourceStorage;
	private StatMapper statMapper;

	public CrawlLinkThread(PageStorage ls, SourceStorage sourceStorage, StatMapper statMapper) {
		super();
		this.linkStorage = ls;
		this.sourceStorage = sourceStorage;
		this.statMapper = statMapper;
	}

	@Override
    public void run() {
		while(true){
			Source source;
			try {
				source = sourceStorage.pop();
				logger.debug("排队等待处理的网站源数为："+sourceStorage.size());
				if(source != null){
					execCrawlLink(source);
				}
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
			}
		}		
    }
	
	*/
/**
	 * 抽取出链接，并写入链接队列
	 * @param source
	 * @throws InterruptedException 
	 *//*

	public void execCrawlLink(Source source) throws InterruptedException{
		try{
			Date beginDate = new Date();
			int sourceLinkNum=0;
			int unCralerSourceLinkNum=0;
			String sourceName = source.getSourceNamec();
			System.out.println("开始爬取 : " + sourceName);
			logger.debug("开始爬取 : " + sourceName + " 区域: " + source.getArea());
			String sourceUrl = source.getUrl();
			source.setSourceNamee(PinyinUtils.getPinYin(sourceName).trim().replace(" ", ""));
			//抽取该信息源的链接
			Page page = CrawlLinkThread.extractLinks(sourceUrl, source.getArea(),source.getLinkUrlRegexp());
			
			if(page != null && page.getUrlList() != null){
				sourceLinkNum = page.getUrlList().size();
				if(sourceLinkNum>0){
					logger.debug(source.getSourceNamec()+"抓取到的链接Size:"+sourceLinkNum);
//					Page unCrawledPage = UrlFilter.findUnCrawledLinks(page);
					Page unCrawledPage = UrlFilter.findReisFilter(page);
					logger.debug(source.getSourceNamec()+"RedisFilter过滤之后的链接Size:"+unCrawledPage.getUrlList().size());
					if(unCrawledPage.getUrlList().size() != 0){
						page.setSource(source);
						page.setUnCrawledUrlList(unCrawledPage.getUrlList());
						page.setSourceUrl(sourceUrl);
						linkStorage.push(page);
					}
					logger.info("---------------待解析网站源队列中的数量------------------"+linkStorage.size());
					sourceLinkNum = page.getUrlList().size();
					if(page.getUnCrawledUrlList() != null){
						unCralerSourceLinkNum = page.getUnCrawledUrlList().size();
					}
					//记录本次爬取的链接的数量
					recordCrawlerLinkStat(sourceName,unCralerSourceLinkNum,sourceLinkNum);
				}else{
					logger.error(sourceUrl + " 区域 " + source.getArea() + " 不对");
					System.out.println(sourceUrl + " 区域 " + source.getArea() + " 不对");
				}
			}
			logger.debug("抓取结束 : " + source.getSourceNamec()+"------------线程--------耗时"+(new Date().getTime()-beginDate.getTime())+"毫秒。Url numbers:"+sourceLinkNum+",unCrawlered Url numbers:"+unCralerSourceLinkNum);
			System.out.println("抓取结束 : " + source.getSourceNamec()+"------------线程--------耗时"+(new Date().getTime()-beginDate.getTime())+"毫秒"+Thread.currentThread().getName());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("---",e);
		}
	}		

	*/
/**
	 * 抽取一个网页指定区域的链接
	 * @param url 原始url
	 * @param standardUrl 规则化后的url
	 * @param path 爬取的xpath路径
	 * @returnsourceStorage 已满
	 *//*

	@SuppressWarnings("finally")
	public static Page extractLinks(String url, String path,String regexp){
		url = CommonUtil.urlEncode(url);
		logger.debug("抓取主url : " + url);
		Map<String, String> urlToTitleMap = new HashMap<String, String>();
		ArrayList<String> urlList = new ArrayList<String>();
//		System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"开始");
		BrowserVersion.CHROME.setBrowserLanguage("zh-cn");
//		System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"指定语言");

        WebClient client = new WebClient(BrowserVersion.FIREFOX_45);
//		System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"初始化浏览器"); 
        client.getOptions().setJavaScriptEnabled(true);    //默认执行js，如果不执行js，则可能会登录失败，因为用户名密码框需要js来绘制。
        client.getOptions().setCssEnabled(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setTimeout(30000);
        client.waitForBackgroundJavaScript(30000);
        client.getCookieManager().setCookiesEnabled(false);
		System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"设置浏览器参数");
		client.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage htmlPage; 
		try {
			htmlPage = client.getPage(url);
			System.out.println("为了获取js执行的数据 线程开始沉睡等待");
            Thread.sleep(30000);
            System.out.println("线程结束沉睡");
			System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"获取页面信息");
			System.out.println(htmlPage.asXml());
//			logger.debug("htmlPage ==="+htmlPage);   //3000
			int stillRunningScript = client.waitForBackgroundJavaScript(30000);
			System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"仍然运行的ajax"+stillRunningScript);
			logger.debug("30 秒内仍然在运行的异步请求数量为："+stillRunningScript);
			if(htmlPage!=null){
				
				if(!StringUtils.isEmpty(path)){
					List htmlEleList =  htmlPage.getByXPath(path);
//					System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"获取区域内容");

					if(htmlEleList != null && htmlEleList.size()>0){
						List<HtmlElement>  nodeList  = new ArrayList();
						HtmlElement htmlElement = null;
						for(int i=0;i<htmlEleList.size();i++){
							htmlElement = (HtmlElement) htmlEleList.get(i);
							if(htmlElement != null){
								nodeList.addAll(htmlElement.getElementsByTagName("a"));
//								System.out.println(DateUtils.formatDate(new Date(),"hh:MM:ss:SSS")+"获取区域锚点信息");

							}else{
								logger.debug("配置的抓取区域未抓到内容");
							}
						}
						if(nodeList.size()>0){
							logger.info("区域所有链接列表长度 : " + nodeList.size());
							String link = "";
							for(HtmlElement ele : nodeList){
								String title = ele.asText().trim();
								link = ele.getAttribute("href");
								try {
									URL uurl = new URL(url); 
									URI base = new URI(uurl.getProtocol(), null, uurl.getHost(), uurl.getPort(), uurl.getPath(), uurl.getQuery(), null);
									link=base.resolve(link).toString();
									if(link.contains("html")||link.contains("shtml")||link.contains("htm")){
										URL ur = new URL(link);
										URI baseURI = new URI(ur.getProtocol(), ur.getHost(), ur.getPath(),null,null);
										link=baseURI.toString();
										logger.debug("去除链接参数成功.."+link);
									}	
								} catch (Exception e) {
									logger.debug("去除链接参数失败..",e);
								}
								logger.debug("页面抓取的link : " + link+"title : " + title);
								link = linkUrlAutoComplete(url,link);
								if(isLinkUrlValid(title,link,regexp) && !urlList.contains(link)){
									urlList.add(link);
									urlToTitleMap.put(link, title);
								}
							}
						}
						
						
					}else{
						logger.debug("配置的抓取区域未抓到内容");
					}
					
					
				*/
/****
					HtmlElement htmlElement = null;
					htmlElement = htmlPage.getFirstByXPath(path);
					if(htmlElement != null){
						List<HtmlElement>  nodeList = htmlElement.getElementsByTagName("a");
						logger.info("区域所有链接列表长度 : " + nodeList.size());
						String link = "";
						for(HtmlElement ele : nodeList){
							String title = ele.asText().trim();
							link = ele.getAttribute("href");
							logger.debug("页面抓取的link : " + link+"title : " + title);
							link = linkUrlAutoComplete(url,link);
							if(isLinkUrlValid(title,link,regexp) && !urlList.contains(link)){
								urlList.add(link);
								urlToTitleMap.put(link, title);
							}
						}
						
					}else{
						logger.debug("配置的抓取区域未抓到内容");
					}
				*****//*

				
				
				}
			}else{
				logger.debug("page is null");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
		}finally{
			client.close();
			Page page = new Page();
			page.setUrlList(urlList);
			page.setUrlTitleMap(urlToTitleMap);
			return page;	
		}
    }    
	
	*/
/**
	 * 判断一个字符串是否包含中文
	 * @param chineseStr
	 * @return
	 *//*

	public static final boolean isChineseCharacter(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
				// Java判断一个字符串是否有中文是利用Unicode编码来判断，
				// 因为中文的编码区间为：0x4e00--0x9fbb
				return true;
			}
		}
		return false;
	}	
    
	*/
/**
	 * 记录每个源的网站爬取数量
	 * @param sourceName
	 * @param contentNum
	 *//*

	void recordCrawlerLinkStat(String sourceName,int crawlLinkNum,int totalNum){
		Stat stat = new Stat();
		String curDate = Utils.getCurDate();
		stat.setHdate(curDate);
		stat.setSourceName(sourceName);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("hdate", curDate);
		map.put("sourceName", sourceName);
		List<Stat> statList = statMapper.findByStat(map);
		if(statList.size() >= 1){
			stat = statList.get(0);
			int totalLinkNum = stat.getTotalNum() + totalNum; 
			int crawlLinkNumAll = stat.getCrawlLinkNum()+crawlLinkNum;
			stat.setCrawlLinkNum(crawlLinkNumAll);
			stat.setTotalNum(totalLinkNum);
			statMapper.update(stat);
		}else{
			stat.setCrawlLinkNum(crawlLinkNum);
			stat.setTotalNum(totalNum);
			statMapper.save(stat);
		}		
	}
	
	*/
/**
	 * 将相对链接自动补充为完整链接
	 * @param sitUrl
	 * @param link
	 * @return
	 *//*

	public static String linkUrlAutoComplete(String sitUrl,String link){
		String resultUrl = link;
		if(!StringUtil.startsWithIgnoreCase(link, "http://") && !StringUtil.startsWithIgnoreCase(link, "https://") && !StringUtil.startsWithIgnoreCase(link, "javascript:")){
			URL uurl;
			try {
				uurl = new URL(sitUrl);
				URI baseURI = new URI(uurl.getProtocol(), null, uurl.getHost(), uurl.getPort(), uurl.getPath(), uurl.getQuery(), null);
				resultUrl = baseURI.resolve(link).toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				logger.debug("auto complete Link exception sitUrl has problem "+sitUrl,e);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				logger.debug("auto complete Link exception linkurl has problem"+link,e);
			} 
			logger.debug("auto complete Link : " + resultUrl);
		}
		return resultUrl;
	}
	*/
/**
	 * 如果有正则表达式先根据正则表达式判断链接的合法性
	 * @param linkTitle
	 * @param linkUrl
	 * @param regexp
	 * @return
	 *//*

	public static boolean isLinkUrlValid(String linkTitle ,String linkUrl, String regexp) {
		boolean isValid = false;
		if (StringUtil.isNotBlank(linkUrl) && !StringUtils.startsWith(linkUrl, "javascript:")) {
			if (StringUtil.isNotBlank(regexp)) {
				Pattern p = Pattern.compile(regexp);
				Matcher m = p.matcher(linkUrl);
				if (m.find()) {
					isValid = true;
				}else{
					logger.debug("["+linkUrl+"] is not match regexp ["+regexp+"]");
				}
			} else {
				isValid = isTitleValid(linkTitle);
			}
		}
		return isValid;
	}
	
	*/
/**
	 * 根据配置的标题长度判断标题是否合法
	 * @param title
	 * @return
	 *//*

	public static boolean isTitleValid(String title){
		boolean isValid = false;
		if(StringUtils.isNotBlank(title)){
			int titleLen = CommonUtil.englishLength(title);
			if(isChineseCharacter(title)){
				titleLen = title.length();
			}
			if(titleLen > PropertyUtil.getDoublePropertyValue("title_min_words")){
				isValid = true; 
			}
			
		}
		return isValid;
	}
}*/
