package org.scbit.lsbi.protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class StringFeature {
	
	/*feature*/
	private static final String ALPHABETIC = "字母";				//字母
	private static final String UPPERCASE = "大写字母";				//大写字母
	private static final String LOWERCASE = "小写字母";				//小写字母
	private static final String NUMERIC = "数字";					//数字
	private static final String SPECIFIC_SYMBOL = "特殊符号";			//特殊符号
	
	//写入文件的流对象
	private OutputStreamWriter write = null;
	private BufferedWriter fileWriter=null;
	//private BufferedReader bufferedReader;
	String filepath = StringFeature.class.getClassLoader().getResource("").toString().substring(6)+"data/"+"270569_NCBI_ENSEMBL_RNA_data.txt";
	
	private Logger logger;
	
	@Before
	public void initDataReader() {
		logger = Logger.getLogger(StringFeature.class);
		logger.info("Logger初始化成功！");
		File file = new File("D:/桌面文件/result.txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		fileWriter=new BufferedWriter(write);
	}
	@After
	public void destroy() {
		try {
			if(fileWriter!=null)
				fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void test() {
		char[] specificSymbols = {'`','~','!','@','#','$','%','^','&','*','(',')','-','_','=','+','[',']','{','}',';',':','\'','\"',',','<','.','>','/','?','\\','|'};
		System.out.println("a:"+(int)'a');
		System.out.println("z:"+(int)'z');
		System.out.println("A:"+(int)'A');
		System.out.println("Z:"+(int)'Z');
		System.out.println("0:"+(int)'0');
		System.out.println("9:"+(int)'9');
		for(int i=0;i<specificSymbols.length;i++) {
			System.out.println(specificSymbols[i]+" : "+(int)specificSymbols[i]);
		}
	}
	
	
	@Test
	public void test2() {
//		}
//		System.out.println(isPureAlphabetic("asdfsdfsdfgsdADASS"));
//		System.out.println(isPureAlphabetic(bufferedReader));
//		System.out.println(findShortestString(bufferedReader));
//		System.out.println(isFixedTail_s(filepath));
//		System.out.println(isFixedHeader_s(filepath));
//		getDistributionOfLength(filepath);
		getDistributionOfFeature(filepath);
	}
	
	public void writeInfo(String string) {
		try {
			fileWriter.write(new String(string.getBytes(), "UTF-8")+"\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 总体扫描是可以分为两类的：
	 * 1. 针对单个字符串的扫描
	 * 2. 针对所有字符串的扫描
	 */
	/**
	 * 判断一个字符串是不是全部由字母组成
	 * @param string	<p>需要判断的字符串</p>
	 * @return		<p>true: 全部为字母  false: 不全为字母</p>
	 */
	public boolean isPureAlphabetic(String string) {
		for(int i=0;i<string.length();i++) {
			int tmp = string.charAt(i);
			if(tmp<65 || (tmp>90 && tmp<97) || tmp>122) 
				return false;
		}
		return true;
	}
	/**
	 * 扫描所有字符串是否全部为字母
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return   <p>true:全部为字母   false:不全是字母</p>
	 */
	public boolean isPureAlphabetic_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isPureAlphabetic(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯大写字母：65~90
	 * @param string <p>需要扫描的字符串</p>
	 * @return <p>true:全部大写 false:不全部为大写</p>
	 */
	public boolean isPureUpperAlphabetic(String string) {
		for(int i=0;i<string.length();i++) {
			int tmp = string.charAt(i);
			if(tmp<65 || tmp>90) 
				return false;
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯大写字母：65~90
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return <p>true:全部大写 false:不全部为大写</p>
	 */
	public boolean isPureUpperAlphabetic_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isPureUpperAlphabetic(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯 小写字母：97~122
	 * @param string <p>需要扫描的字符串</p>
	 * @return	<p>true:全部小写 false:不全部为小写</p>
	 */
	public boolean isPureLowerAlphabetic(String string) {
		for(int i=0;i<string.length();i++) {
			int tmp = string.charAt(i);
			if(tmp<97 || tmp>122) 
				return false;
		}
		return true;
	}
	/**
	 * 扫描每个字符串是否为
	 * 纯 小写字母：97~122
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return	<p>true:全部小写 false:不全部为小写</p>
	 */
	public boolean isPureLowerAlphabetic_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isPureLowerAlphabetic(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯数字：48~57
	 * @param string <p>需要扫描的字符串</p>
	 * @return	<p>true:全部数字 false:不全部为数字</p>
	 */
	public boolean isPureNumeric(String string) {
		for(int i=0;i<string.length();i++) {
			int tmp = string.charAt(i);
			if(tmp<48 || tmp>57) 
				return false;
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯数字：48~57
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return	<p>true:全部数字 false:不全部为数字</p>
	 */
	public boolean isPureNumeric_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isPureNumeric(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 可比较大小的数字串
	 * @param string  <p>需要扫描的字符串</p>
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return	<p>true:数字串可以比较大小 false:数字串不能比较大小</p>
	 */
	public boolean isNumericComparable(String string) {
		if(string.charAt(0) == '0')
			return false;
		else
			return true;
	}
	//当为纯数字时候
	public boolean isNumericComparable_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isNumericComparable(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static final char[] specificSymbols = {'`','~','!','@','#','$','%','^','&','*','(',')','-','_','=','+','[',']','{','}',';',':','\'','\"',',','<','.','>','/','?','\\','|'};
	/**
	 * 扫描每个字符串是否为
	 * 纯特殊字符
	 * @param string <p>需要扫描的字符串</p>
	 * @return	<p>true:全部为特殊符号 false:不全是特殊符号</p>
	 */
	public boolean isPureSpecificSymbol(String string) {
		for(int i=0;i<string.length();i++) {
			if(!isOneOfTheseSymbols(string.charAt(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * 扫描每个字符串是否为
	 * 纯特殊字符
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return	<p>true:全部为特殊符号 false:不全是特殊符号</p>
	 */
	public boolean isPureSpecificSymbol_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(!isPureSpecificSymbol(s))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 扫描这个字符是否为一个特殊字符中的一个
	 * @param c	<p>需要扫描的字符</p>
	 * @return <p>true: 是一个特殊字符 false：不是一个特殊字符</p>
	 */
	public boolean isOneOfTheseSymbols(char c) {
		for(int i=0;i<specificSymbols.length;i++) {
			if(c == specificSymbols[i]) 
				return true;
		}
		return false;
	}
	
	/**
	 * 扫描单个字符长度是否和先前一个长度相等
	 * @param string <p>需要扫描的字符串</p>
	 * @return	<p>true:字符串长度相同 false:字符串长度不相同</p>
	 */
	public boolean isFixedLength(String string,int length) {
		if(string.length() == length)
			return true;
		else
			return false;
	}
	/**
	 * 扫描所有字符串是否为
	 * 长度固定的
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @return	<p>true:字符串长度固定 false:字符串长度不固定</p>
	 */
	public boolean isFixedLength_s(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String s = null;
		boolean isFirst = true;
		int length = 0;
		try {
			while((s=bufferedReader.readLine())!=null) {
				if(isFirst) {
					length = s.length();
					isFirst = false;	
				} 
				boolean isEqual = isFixedLength(s, length);
				if(!isEqual)
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 获取数据字符串长度分布情况
	 * @param filepath	<p>需要扫描的文件的地址</p>
	 */
	@SuppressWarnings("unchecked")
	public void getDistributionOfLength(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Map<Integer,Integer> dis = new HashMap<Integer, Integer>();
		String tmp = null;
		try {
			while((tmp=bufferedReader.readLine())!=null) {
				int lengthOfString = tmp.length();
				if(dis.containsKey(lengthOfString)) {
					int count = dis.get(lengthOfString);
					dis.remove(lengthOfString);
					dis.put(lengthOfString, ++count);
				}else {
					dis.put(lengthOfString, 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Entry<Integer, Integer>> listent = new ArrayList<Map.Entry<Integer,Integer>>(dis.entrySet());
		Collections.sort(listent,new MapComparator());
		
		for(Entry<Integer, Integer> e : listent) {
			System.out.println("长度为：\t"+e.getKey()+"\t的数据有：\t"+e.getValue());
		}
	}
	/**
	 * 扫描所有字符串是否为
	 * 有固定字符串头的
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @param string <p>需要扫描的字符串</p>
	 * @return	<p>true:有固定的字符串头 false:没有固定的字符串头</p>
	 */
	public boolean isFixedHeader(String string,String header) {
		if(string.length()<header.length()) {
			return false;
		}
		if(string.substring(0, header.length()).equals(header))
			return true;
		else
			return false;
	}
	public boolean isFixedHeader_s(String filepath) {
		//存储所有header
		Map<String,Integer> headerMap = new HashMap<String, Integer>();
		String header = null;
		//先取出一条数据
		BufferedReader bufferedReaderOuter = null;
		String dataTemplate = null;
		try {
			bufferedReaderOuter = new BufferedReader(new FileReader(filepath));
			while((dataTemplate=bufferedReaderOuter.readLine())!=null) {
					if(!isGoon_header(headerMap, dataTemplate)) {
						continue;
					}
					//fileWriter.write("当前模版字符串："+dataTemplate+"\n");
					//fileWriter.flush();
//					System.out.println("当前模版字符串："+dataTemplate);
					for(int i=1;i<=dataTemplate.length();i++) {
						header = (dataTemplate.substring(0, i));
						int count = 0;
						try {
							String tmp = null;
							BufferedReader bufferedReader = null;
							try {
								bufferedReader = new BufferedReader(new FileReader(filepath));
							} catch (FileNotFoundException e2) {
								e2.printStackTrace();
							}
							while((tmp = bufferedReader.readLine())!=null) {
								if(isFixedHeader(tmp, header)) {
									count++;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(!isContain(headerMap,header)&& count !=1) {
							headerMap.put(header, count);
							isWrapperHeadr(headerMap, count, header);
						}
					}
//					displayInfoNow(headerMap);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		displayInfoNow(headerMap,true);
		return true;
	}
	
	//检查当前模版是否有必要进行
	public boolean isGoon_header(Map<String,Integer> headers,String template) {
		for(Entry<String,Integer> e : headers.entrySet()) {
			if(template.startsWith(e.getKey(), 0) && e.getValue()<=100) {
				return false;
			}
		}
		return true;
	}
	//查看是否存在已经扫描获得字符串头
	public boolean isContain(Map<String,Integer> h_t_maps,String h_t) {
		Set<String> keySet = h_t_maps.keySet();
		Iterator<String> iterator = keySet.iterator();
		String tmp = null;
		while(iterator.hasNext()) {
			tmp = iterator.next();
			if(h_t.equals(tmp))
				return true;
		}
		return false;
	}
	/**
	 * 查看是否有冗余header 如：
	 * 字符串头: SBAL402 以此字符串头开头的字符串条数: 574
	 * 字符串头: SBAL4028 以此字符串头开头的字符串条数: 574
	 * 字符串头: SBAL40288 以此字符串头开头的字符串条数: 574
	 * 诸如此类的数据，需要先将先前较短的header去掉插入较长的header
	 * 检查的同时就去掉较短的
	 */
	public void isWrapperHeadr(Map<String,Integer> headerMap,Integer counter,String header) {
		Set<String> keySet = headerMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		Collection<String> readyToRemove = new ArrayList<String>();
		String tmp = null;
		while(iterator.hasNext()) {
			tmp = iterator.next();
			if(header.length()>tmp.length() && header.startsWith(tmp, 0)) {
				if((Integer)headerMap.get(tmp).compareTo(counter) == 0) {
					readyToRemove.add(tmp);
//					System.out.println("移除较短小header: "+tmp);
				}
			}
		}
		keySet.removeAll(readyToRemove);
	}
	
	
	public int findShortestString(String filepath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int shortest = 0;
		String tmp = null;
		int forTmp = 0;
		try {
			while((tmp=bufferedReader.readLine())!=null) {
				forTmp = tmp.length();
				if(tmp.length()<shortest)
					shortest = tmp.length();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(shortest ==0 && isFixedLength_s(filepath)) {
			shortest = forTmp;
		}
		return shortest;
	}
	/*打印当前结果信息*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void displayInfoNow(Map<String,Integer> h_t_maps,boolean isH_t) {
		String sign = null;
		if(isH_t) 
			sign = "HEAD";
		else
			sign = "TAIL";
		List<Map.Entry<String, Integer>> arr = new ArrayList<Map.Entry<String, Integer>>(h_t_maps.entrySet());
		Collections.sort(arr,new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				Integer v1 = 0;
				Integer v2 = 0;
				v1 = o1.getValue();
				v2 = o2.getValue();
				return v2-v1;
			}
		});
		System.out.println("---------------当前数据情况:"+sign+"-----------------");
		for(Map.Entry<String, Integer> single:arr) {
			String key = single.getKey();
			String value = single.getValue().toString();
			System.out.println("With "+sign+" : "+key+"\t\t\t相应数据数量:\t\t"+value);
		}
		System.out.println("----------------------------------------");
	}
	
	/**
	 * 扫描所有字符串是否为
	 * 有固定字符串 尾的
	 * @param filepath <p> 需要扫描的字符串输入流文件地址</p>
	 * @param string <p>需要扫描的字符串</p>
	 * @param tail <p>当前要匹配的tail</p>
	 * @return	<p>true:有固定的字符串尾 false:没有固定的字符串尾</p>
	 */
	public boolean isFixedTail(String string,String tail) {
		if(string.length()<tail.length())
			return false;
		if(string.endsWith(tail))
			return true;
		else
			return false;
	}
	public boolean isFixedTail_s(String filepath) {
		//存储所有tail
		Map<String,Integer> tailMap = new HashMap<String, Integer>();
		String tail = null;
		//先取出一条数据
		BufferedReader bufferedReaderOuter = null;
		String dataTemplate = null;
		try {
			bufferedReaderOuter = new BufferedReader(new FileReader(filepath));
			while((dataTemplate=bufferedReaderOuter.readLine())!=null) {
					if(!isGoon_tail(tailMap, dataTemplate)) {
						continue;
					}
					//fileWriter.write("当前模版字符串："+dataTemplate+"\n");
					//fileWriter.flush();
//					System.out.println("当前模版字符串："+dataTemplate);
					for(int i=1;i<=dataTemplate.length();i++) {
						tail = (dataTemplate.substring(dataTemplate.length()-i, dataTemplate.length()));
						int count = 0;
						try {
							String tmp = null;
							BufferedReader bufferedReader = null;
							try {
								bufferedReader = new BufferedReader(new FileReader(filepath));
							} catch (FileNotFoundException e2) {
								e2.printStackTrace();
							}
							while((tmp = bufferedReader.readLine())!=null) {
								if(isFixedTail(tmp, tail)) {
									count++;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(!isContain(tailMap,tail)&& count !=1) {
							tailMap.put(tail, count);
							isWrapperTail(tailMap, count, tail);
						}
					}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		displayInfoNow(tailMap,false);
		return true;
	}
	
	/**
	 * 遍历当前tailsMap与tail比对看是否继续当前的datatemplate
	 * @param tailsMap	<p>存储当前tail结果</p>
	 * @param tail	<p>当前分析的tail</p>
	 * @return <p>true: 继续扫描当前template false:继续下一个datatemplate</p>
	 */
	public boolean isGoon_tail(Map<String,Integer> tailsMap,String tail) {
		for(Entry<String,Integer> e : tailsMap.entrySet()) {
			if(tail.endsWith(e.getKey()) && e.getValue()<=100) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 遍历查看是否已经包含的所进行tail
	 * @param tailMap	<p>存储当前tail结果</p>
	 * @param counter	<p>匹配当前tail的数据条数</p>
	 * @param tail	<p>当前分析的tail</p>
	 */
	public void isWrapperTail(Map<String,Integer> tailMap,Integer counter,String tail) {
		Set<String> keySet = tailMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		Collection<String> readyToRemove = new ArrayList<String>();
		String tmp = null;
		while(iterator.hasNext()) {
			tmp = iterator.next();
			if(tail.length()>tmp.length() && tail.endsWith(tail)) {
				if((Integer)tailMap.get(tmp).compareTo(counter) == 0) {
					readyToRemove.add(tmp);
//					System.out.println("移除较短小tail: "+tmp);
				}
			}
		}
		keySet.removeAll(readyToRemove);
	}
	
	public void getDistributionOfFeature(String filepath) {
		Map<ArrayList<String>,Integer> distribution = new HashMap<ArrayList<String>, Integer>();
		BufferedReader bufferedReaderOuter = null;
		try {
			bufferedReaderOuter = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String tmp = null;
		try {
			while((tmp=bufferedReaderOuter.readLine())!=null) {
				ArrayList<String> orderType = getOrderType(tmp);
				if(distribution.containsKey(orderType)) {
					Integer count = distribution.get(orderType);
					distribution.put(orderType, ++count);
				}else {
					distribution.put(orderType, 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(distribution);
	}
	
	public ArrayList<String> getOrderType(String string) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0;i<string.length();i++) {
			char tmp = string.charAt(i);
			String type = getCharType(tmp);
			if(result.size()==0) {
				result.add(type);
				continue;
			}
			if(result.get(result.size()-1).equals(type)) {
				continue;
			}else {
				result.add(type);
			}
		}
		return result;
	}
	
	public String getCharType(char c) {
		
		if(c>=65&&c<=90) {
			return UPPERCASE;
		}else if(c>=97&&c<=122) {
			return LOWERCASE;
		}else if(c>=48&&c<=57) {
			return NUMERIC;
		}
		
		for(int i=0;i<specificSymbols.length;i++) {
			if(c==specificSymbols[i])
				return SPECIFIC_SYMBOL;
		}
		return SPECIFIC_SYMBOL;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

class MapComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		Map.Entry obj1 = (Map.Entry)o1;  
		Map.Entry obj2 = (Map.Entry)o2;
		Integer v1 = (Integer) obj1.getValue();
		Integer v2 = (Integer) obj2.getValue();
		return v2.compareTo(v1);
	}
	
}
