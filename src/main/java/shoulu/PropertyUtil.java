/*
package com.tmzs.crawl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {

	private static Properties prop=new Properties();
	private static Map<String,Double> doubleMap=new HashMap<String,Double>();
	private static Map<String,Integer> intMap=new HashMap<String,Integer>();
	
	static{
		InputStream is=PropertyUtil.class.getClassLoader().getResourceAsStream("constant.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getPropertyValue(String key){
		return prop.getProperty(key);
	}
	
	public static double getDoublePropertyValue(String key){
		if(doubleMap.get(key) == null){
			double value = Double.parseDouble(prop.getProperty(key));
			doubleMap.put(key, value);
		}
		return doubleMap.get(key);
	}
	
	public static int getIntPropertyValue(String key){
		if(intMap.get(key) == null){
			int value = Integer.parseInt(prop.getProperty(key));
			intMap.put(key, value);
		}
		return intMap.get(key);
	}	
	
	public static void main(String[] args) {
		System.out.println(getIntPropertyValue("link_queue"));
	}
}
*/
