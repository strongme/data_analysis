package org.scbit.lsbi.protein;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ProteinDataAnalysis {

	/**
	 * Map中Key值代表蛋白数据ID String中存放与ID对应的蛋白数据
	 */
	private Map<String, String> data = new HashMap<String, String>();
	/**
	 * Map中Key值代表蛋白数据ID List中存放与ID对应的NCBI_TaxID
	 */
	private Map<String, List<String>> dataForXML = new HashMap<String, List<String>>();
	/**
	 * 蛋白数据文件名称
	 */
	private String fileName;

	private StringBuilder dataForTXT = new StringBuilder();

	private File finalTxtFile = null;

	private Document finalXMLFile = DocumentHelper.createDocument();

	public ProteinDataAnalysis(String fileName) {
		long timeNow = System.currentTimeMillis();

		finalXMLFile.setXMLEncoding("UTF-8");
		finalXMLFile.addElement("data").addAttribute("createDate",
				"" + new Date(System.currentTimeMillis()));
		finalTxtFile = new File(Thread.currentThread().getContextClassLoader()
				.getResource("").toString().substring(6)
				+ "finalData.txt");
		try {
			finalTxtFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.fileName = Thread.currentThread().getContextClassLoader()
				.getResource("").toString().substring(6)
				+ fileName;

		resolve();
		long timeEnd = System.currentTimeMillis();
		double down = 1000;
		System.out.println("共耗时：" + (timeEnd - timeNow) / down + "s");
	}

	/**
	 * 将文件数据按照ID划分到Map中
	 */
	public void resolve() {
		StringBuilder tmp = new StringBuilder();
		BufferedReader bufReader = null;
		File dataFile = new File(fileName);
		int i = 0;
		try {
			bufReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(dataFile)));
			String tmpString = null;
			while (null != (tmpString = bufReader.readLine())) {
				if (!tmpString.startsWith("//")) {
					tmp.append(tmpString);
					continue;
				} else {
					i++;
					data.put(getProteninDataID(tmp.toString()), tmp.toString());
					if (i % 500  == 0) {
						System.out.println("data size :"+data.size());
						mappingDataForTxt_XML();
						writeIntoTXT();
						writeIntoXML();
						System.out.println("第" + i/500 + "次写数据到TXT和XML");
						data = new HashMap<String, String>();
						dataForTXT = new StringBuilder();
						dataForXML = new HashMap<String, List<String>>();
					}
					tmp.setLength(0);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("找不到数据文件,请将数据文件放入："
					+ Thread.currentThread().getContextClassLoader()
							.getResource("").toString().substring(6) + "目录下");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("共" + i + "条数据");
		intoXMLFile();

	}

	/**
	 * 获得data的ID值
	 * 
	 * @param data
	 * @return
	 */
	private String getProteninDataID(String data) {
		int flagID = data.indexOf(".");
		String ID = data.substring(5, flagID);
		return ID;
	}

	/**
	 * 将数据为dataForTxt和dataForXML分配
	 */
	private void mappingDataForTxt_XML() {
		Set<String> idSet = data.keySet();
		Iterator<String> it = idSet.iterator();
		while (it.hasNext()) {
			String idNow = it.next();
			String dataNow = data.get(idNow);
			splitNCBI_TaxID(idNow, dataNow);
		}
	}

	

	/**
	 * 过滤获得NCBITaxID
	 */
	public void splitNCBI_TaxID(String idNow, String dataNow) {
		List<String> tmpList = new ArrayList<String>();
		Pattern ncbiPattern = Pattern.compile("NCBI_TaxID=[0-9]*;");
		Matcher resultMatcher = ncbiPattern.matcher(dataNow);
		while (resultMatcher.find()) {
			String tmp = resultMatcher.group();
			int indexSem = tmp.indexOf(";");
			tmp = tmp.substring(11, indexSem);
			tmpList.add(tmp);
			tmp += "\t" + idNow.replace("ID   ", "") + ".\n";
			dataForTXT.append(tmp);
		}
		dataForXML.put(idNow.replace("ID   ", ""), tmpList);
	}
	
	/**
	 * 获取每条蛋白数据的NCBI_TaxID并且写入finalData
	 * 
	 * @param args
	 */
	public void writeIntoTXT() {
		RandomAccessFile dist = null;
		try {
			dist = new RandomAccessFile(finalTxtFile, "rw");
			long count = dist.length();
			System.out.println("当前文件大小:"+count);
			dist.seek(count);
			dist.writeBytes(dataForTXT.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (null != dist) {
				try {
					dist.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

	/**
	 * 写入XML文档
	 * 
	 * @param args
	 */
	public void writeIntoXML() {
		Element root = finalXMLFile.getRootElement();
		Set<String> keySet = dataForXML.keySet();
		TreeSet<String> inorderSet = new TreeSet<String>(
				new Comparator<String>() {
					public int compare(String s1, String s2) {
						s1 = s1.substring(0, s1.indexOf("_") - 1);
						s2 = s2.substring(0, s2.indexOf("_") - 1);
						if (s1.compareTo(s2) == 0)
							return 1;
						return s1.compareTo(s2);
					}
				});
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			inorderSet.add(it.next());
		}
		keySet = inorderSet;
		for (String key : keySet) {
			ArrayList<String> taxIds = (ArrayList<String>) dataForXML.get(key);
			Element unit = root.addElement("unit").addAttribute("id", key);
			for (int i = 0; i < taxIds.size(); i++) {
				unit.addElement("taxid").addText(taxIds.get(i));
			}
		}
	}

	private void intoXMLFile() {
		XMLWriter output = null;
		String xmlFilePath = Thread.currentThread().getContextClassLoader()
				.getResource("").toString().substring(6)
				+ "finalData.xml";
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
				output.write(finalXMLFile);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ProteinDataAnalysis analysis = new ProteinDataAnalysis("data.txt");
	}

}
