package org.scbit.lsbi.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Walter
 *	练习使用Jsoup解析网页
 */

public class ParserBase {
	
	@Ignore
	@Test
	public void jsoup_1() throws Exception {
		
		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		Elements newsHeadlines = doc.select("#mp-itn b a");
		System.out.println(newsHeadlines);
		
	}
	
	@Ignore
	@Test
	public void jsoup_2() throws Exception {
		
		Document doc = Jsoup.connect("http://www.baidu.com").get();
		System.out.println(doc);
	}
	
	@Ignore
	@Test
	public void jsoup_3() throws Exception {
		
		String html = "<p>An <a href='http://www.baidu.com' class='hyperlink' id='hyperlink_id'><b>Baidu</b></a></p>";
		Document doc = Jsoup.parse(html);
		Element link = doc.select("a").first();
		
		System.out.println(doc.body().text());
		System.out.println(link.id());
		System.out.println(link.tagName());
		System.out.println(link.className());
		System.out.println(link.attr("href"));
		System.out.println(link.text());
		System.out.println(link.outerHtml());
		System.out.println(link.html());
		
		
	}
	
	
	

}
