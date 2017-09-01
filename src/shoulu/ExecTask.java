package com.tmzs.crawl.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.tmzs.crawl.bean.Cycle;
import com.tmzs.crawl.bean.Source;
import com.tmzs.crawl.bean.Stat;
import com.tmzs.crawl.dao.SnapMapper;
//import com.tmzs.crawl.jdbc.CycleMappers;
//import com.tmzs.crawl.jdbc.SourceMappers;
//import com.tmzs.crawl.jdbc.StatMapper;

import com.tmzs.crawl.dao.CycleMapper;
import com.tmzs.crawl.dao.SourceMapper;
import com.tmzs.crawl.dao.StatMapper;

import com.tmzs.crawl.pool.PageStorage;
import com.tmzs.crawl.pool.SourceStorage;
import com.tmzs.crawl.util.PropertyUtil;
import com.tmzs.crawl.util.Utils;

@Service
public class ExecTask {
	static Logger logger = Logger.getLogger(ExecTask.class.getName());
	@Autowired
	SnapMapper snapMapper;	
	@Autowired
	TaskExecutor taskExecutor;
	@Autowired
	CycleMapper cycleMapper;	
	@Autowired
	SourceMapper sourceMapper;	
	@Autowired
	StatMapper statMapper;	
	
	public static Cycle cycle;
	final PageStorage ls = new PageStorage();
	final SourceStorage sourceStorage = new SourceStorage();
	final List<Thread> linkThreadsList = new ArrayList<Thread>();
	final List<Thread> textThreadsList = new ArrayList<Thread>();
	static boolean isInit = false;
	
	private void init(){
		//启动监控线程
		if(!isInit){
			int numberOfLinkCrawlers = (int) PropertyUtil.getDoublePropertyValue("link_threads");
			for (int i = 1; i <= numberOfLinkCrawlers; i++) {
				CrawlLinkThread crawlLinkThread = new CrawlLinkThread(ls,sourceStorage, statMapper);
				logger.debug("启动抓取链接线程--------------------");
				crawlLinkThread.setName("link_"+i);
				crawlLinkThread.start();
				linkThreadsList.add(crawlLinkThread);
			}	
			
			  int numberOfTextCrawlers = (int) PropertyUtil.getDoublePropertyValue("text_threads");
		      for (int i = 1; i <= numberOfTextCrawlers; i++) {
		    	  CrawlTextThread crawlTextThread = new CrawlTextThread(ls, snapMapper,statMapper);
		    	  logger.debug("启动解析线程--------------------");
		    	  crawlTextThread.setName("text_"+i);
		    	  crawlTextThread.start();
		    	  textThreadsList.add(crawlTextThread);
		      }	
		      
		      Thread monitorLinkThread = new Thread(new Runnable() {
		          public void run() {
		            try {
		            	int j=1;
		                while (true) {
		                  Thread.sleep(60000);
		                  for (int i = 0; i < linkThreadsList.size(); i++) {
		                    Thread thread = linkThreadsList.get(i);
		                    if (!thread.isAlive()) {
		                    	if(sourceStorage.size() > 0){
			                        logger.info("Thread { "+thread.getName()+" } was dead, I'll recreate it");
			                        System.out.println("Thread { "+thread.getName()+" } was dead, I'll recreate it");
			                        thread = new CrawlLinkThread(ls,sourceStorage, statMapper);
			                        thread.setName("link_" + i+"_"+j);
			                        linkThreadsList.remove(i);
			                        linkThreadsList.add(i, thread);
			                        thread.start();
		                    	}
		                    }else{
		                    	logger.debug("thread {"+thread.getName()+"} is alive");
		                    	System.out.println(" thread { "+thread.getName()+" } is alive");
		                    }
		                  }
		                  j++;
		                }
		            } catch (Exception e) {
		              logger.error("monitorLinkThread occur Unexpected Error", e);
		            }
		          }
		        });		      
		      
		      Thread monitorTextThread = new Thread(new Runnable() {
		          public void run() {
		            try {
		                while (true) {
		                  Thread.sleep(60000);
		                  for (int i = 0; i < textThreadsList.size(); i++) {
		                    Thread thread = textThreadsList.get(i);
		                    if (!thread.isAlive()) {
		                    	if(ls.size() > 0){
			                        logger.info("解析 Thread { "+thread.getName()+" } was dead, I'll recreate it");
			                        System.out.println("解析  Thread { "+thread.getName()+" } was dead, I'll recreate it");
			                        thread = new CrawlTextThread(ls, snapMapper,statMapper);
			                        thread.setName("text_" + i);
			                        textThreadsList.remove(i);
			                        textThreadsList.add(i, thread);
			                        thread.start();
		                    	}
		                    }else{
		                    	System.out.println("解析  thread { "+thread.getName()+" } is alive");
		                    }
		                  }
		                }
		            } catch (Exception e) {
		              logger.error("monitorTextThread occur Unexpected Error", e);
		            }
		          }
		        });
		        monitorLinkThread.setName("link monitor");
		      	monitorLinkThread.start();
		      	monitorTextThread.setName("text monitor");
		      	monitorTextThread.start(); 
		        isInit = true;
		}
	}
	
	/**
	 * 扫描时间表，判断当前时间是应该执行抓取任务
	 */
	public void scanSchedule(){
		String curTime = Utils.getCurTime("HH:mm");
		System.out.println("curTime : " + curTime);
		List<Cycle> cycleList = cycleMapper.findByMediaType("网页");
//		List<Cycle> weixinList = cycleMapper.findByMediaType("微信");
//		cycleList.addAll(weixinList);		
		if(cycleList != null){
			for(Cycle c : cycleList){
				if(curTime.contains(c.getTime())){
					System.out.println(curTime + " : 爬取任务开始");
					logger.info(curTime + " : 爬取任务开始");
					scanSource();
				}
			}
		}
	}
	
	/**
	 * 扫信息源，依次抓取各个信息源
	 */	
	public void scanSource(){
		int start = PropertyUtil.getIntPropertyValue("start");
		int pageSize = PropertyUtil.getIntPropertyValue("pageSize");
		int sourceQueueLength = PropertyUtil.getIntPropertyValue("source_queue");
		logger.debug("this crawler handle source begin "+start);
		logger.debug("this crawler handle source numbers is"+pageSize);
		Map<String, Object> map=new HashMap<String, Object>();
		if(sourceQueueLength-sourceStorage.size()>pageSize){
			map.put("start", start);
			map.put("pagesize", pageSize);
//			map.put("mediaType", "网页");
			List<Source> sourceList = sourceMapper.findSourceByPage(map);
			try {
				for(Source source : sourceList){
					if(sourceStorage.size() < sourceQueueLength){
						logger.debug(source.getSourceNamec()+"--------------"+source.getUrl());
						sourceStorage.push(source);
					}else{
						logger.debug("sourceStorage 已满，" + sourceStorage.size() + "个源");
						break;
					}
				}
			} catch (InterruptedException e) {
				logger.error("获取待爬去源异常.",e);
			}
			init();
		}else{
			logger.debug("队列大小不足，可以增加link_threads或者减小pageSize，，处理队列预设大小为:"+sourceQueueLength+",当前队列中排队数据为:"+sourceStorage.size()+"等待加入的源的数量为："+pageSize);
		}
		
	}
	
	/**
	 * 扫信息源，依次抓取各个信息源
	 */	
	public void sendMail(){
		List<Stat> statList = statMapper.findAll();
		for(Stat stat : statList){
			
		}
	}	
}
