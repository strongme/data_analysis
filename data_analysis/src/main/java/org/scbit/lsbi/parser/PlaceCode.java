package org.scbit.lsbi.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Ignore;
import org.junit.Test;
import org.scbit.lsbi.util.TextFile;

public class PlaceCode {
	
	@Ignore
	@Test
	public void doCollectionTest() {
		List<String> inner = new ArrayList<String>();
		Map<String,List<String>> outer = new HashMap<String, List<String>>();
		outer.put("first", inner);
		System.out.println(outer);
		inner.add("Inner First");
		System.out.println(outer);
	}
	
	/**
	 * 先行练习
	 */
	@Ignore
	@Test
	public void readFile() {
		String s = TextFile.read("location.txt");
		/*找出省份及其下属地区*/
		Pattern provinceRegex = Pattern.compile("(\\d){6}( {2})([\u4e00-\u9fa5]+)\\n");
		Matcher matcher = provinceRegex.matcher(s);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()) {
			String tmp = matcher.group();
			matcher.appendReplacement(buffer,"//\n"+tmp );
//			System.out.println(buffer);
		}
		matcher.appendTail(buffer);
		System.out.println(buffer);
	}

	/**
	 * 方案1
	 */
	@Ignore
	@Test
	public void readFile_test() {
		List<Integer> indexes = new ArrayList<Integer>();
		String s = TextFile.read("location.txt");
		/*找出省份及其下属地区*/
		Pattern provinceRegex = Pattern.compile("(\\d){6}( {2})([\u4e00-\u9fa5]+)\\n");
		Matcher matcher = provinceRegex.matcher(s);
		while(matcher.find()) {
			indexes.add(matcher.start());
		}
		Properties indexCouple = new Properties();
		for(int i=0;i<indexes.size();i++) {
			if(i==indexes.size()-1) {
				indexCouple.setProperty(""+indexes.get(i), ""+s.length());
				continue;
			}
			indexCouple.setProperty(indexes.get(i).toString(), (""+(indexes.get(i+1)-1)));
		}
		System.out.println(indexCouple);
		Map<String,String> everyPro = new HashMap<String,String>();
		matcher.reset();
		while(matcher.find()) {
			int start = matcher.start();
			int end = Integer.valueOf((String) indexCouple.get(""+start));
			everyPro.put(matcher.group(), s.substring(start, end));
		}
		System.out.println(everyPro);
	}
	
	/**
	 * 省：(\\d){6}( {2})([\u4e00-\u9fa5]+)\\n
	 * id pattern     [0-9]{2}0000
	 * 市：(\\d){6}( {4})([\u4e00-\u9fa5]+)\\n
	 * id pattern [0-9]{4}00
	 * 县： (\\d){6}( {6})([\u4e00-\u9fa5]+)\\n
	 * id pattern [0-9]{6}
	 */
//	@Ignore
	@Test
	public void split_Test() throws IOException {
		BufferedReader reader = TextFile.readAsReader("location.txt");
		String tmp = null;
		int pro = 0;
		int city = 0;
		int county = 0;
		int total = 0;
		while((tmp=reader.readLine())!=null) {
			total++;
			if(isProvince(tmp.trim()) && isProID(tmp.trim().substring(0, 6))) {
				pro++;
//				city = 0;
			}
			if(isCity(tmp.trim()) && isCiID(tmp.trim().substring(0, 6))) {
				city++;
//				county=0;
			}
			if(isCounty(tmp.trim()) && isCoID(tmp.trim().substring(0, 6))) {
				county++;
			}
		}
		System.out.println("total"+total);
		System.out.println("pro "+pro);
		System.out.println("city "+city);
		System.out.println("county "+county);
		System.out.println("pro+city+county="+(pro+city+county));
		System.out.println("total ="+total);
	}
	
	
	/**
	 * 最终方案
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void isWorking() throws IOException {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		document.addElement("中国");
		Element root = document.getRootElement();
		BufferedReader reader = TextFile.readAsReader("location.txt");
		String tmp = null;
		String oldPro = null;
		String oldCity = null;
		boolean isFirstPro = true;
		boolean isFirstCity = true;
		Map<String,List<String>> province = null;
		List<String> counties = null;
		while((tmp=reader.readLine())!=null) {
			if(isProvince(tmp.trim())) {
				System.out.println("省："+tmp.trim());
				if(isFirstPro) {
					isFirstPro = false;
					oldPro = tmp.trim();
					province = new HashMap<String, List<String>>();
				}else {
					province.put(oldCity, counties);
					addProvince(root, oldPro, province);
					province = null;
					province = new HashMap<String, List<String>>();
					oldPro = tmp.trim();
				}
			}
			if(isCity(tmp.trim())) {
				System.out.println(oldPro+"  的市 "+tmp.trim());
				if(isFirstCity) {
					isFirstCity =false;
					counties = new ArrayList<String>();
					oldCity = tmp.trim();
				}else {
					province.put(oldCity,counties);
					counties = null;
					counties = new ArrayList<String>();
					oldCity = tmp.trim();
				}
			}
			if(isCounty(tmp.trim())) {
				System.out.println(oldPro+"  的市 "+oldCity+"  的县 "+tmp.trim());
				counties.add(tmp.trim());
			}
		}
		//收尾
		province.put(oldCity, counties);
		addProvince(root, oldPro, province);
		intoXml(document);
		
	}
	
	/**
	 * 将document对象写入到xml文件中
	 * @param document
	 */
	public void intoXml(Document document) {
		XMLWriter output = null;
		String xmlFilePath = Thread.currentThread().getContextClassLoader()
				.getResource("").toString().substring(6)
				+ "finalData.xml";
		System.out.println(xmlFilePath);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		try {
			output = new XMLWriter(new FileOutputStream(new File(xmlFilePath)),
					format);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				output.write(document);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 向root节点添加province节点
	 * 向province节点依次添加其下的
	 * 市区和县区
	 * @param root
	 * @param provinceName
	 * @param province
	 */
	public void addProvince(Element root,String provinceName,Map<String,List<String>> province) {
		Element provinceNode = root.addElement("省").addAttribute("名字", provinceName);
		Set<String> class2 = province.keySet();
		Iterator<String> class2Name = class2.iterator();
		while(class2Name.hasNext()) {
			String cityName = class2Name.next();
			Element cityNode = provinceNode.addElement("市区").addAttribute("名字", cityName);
			List<String> counties = province.get(cityName);
			for(int i=0;i<counties.size();i++) {
					cityNode.addElement("区域").setText(counties.get(i));
			}
		}
	}
	
	
	/**
	 * 检测字符串s是否是省
	 * @param s
	 * @return
	 */
	public boolean isProvince(String s) {
		Pattern pattern = Pattern.compile("(\\d){6}( {2})([\u4e00-\u9fa5]+)");
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	/*
	 * 检测ID
	 */
	public boolean isProID(String id) {
		Pattern pattern = Pattern.compile("[0-9]{2}0000");
		Matcher matcher = pattern.matcher(id);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	
	public boolean isCiID(String id) {
		Pattern pattern = Pattern.compile("[0-9]{4}00");
		Matcher matcher = pattern.matcher(id);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	public boolean isCoID(String id) {
		Pattern pattern = Pattern.compile("[0-9]{6}");
		Matcher matcher = pattern.matcher(id);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	/**
	 * 检测字符串s是否是市区
	 * @param s
	 * @return
	 */
	public boolean isCity(String s) {
		Pattern pattern = Pattern.compile("(\\d){6}( {4})([\u4e00-\u9fa5]+)");
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	/**
	 * 检测字符串s是否是县
	 * @param s
	 * @return
	 */
	public boolean isCounty(String s) {
		Pattern pattern = Pattern.compile("(\\d){6}( {6})([\u4e00-\u9fa5]+)");
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches())
			return true;
		else
			return false;
	}
	
	
	
}
