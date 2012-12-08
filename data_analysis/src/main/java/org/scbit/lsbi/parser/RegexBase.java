package org.scbit.lsbi.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.scbit.lsbi.util.TextFile;

public class RegexBase {
	
	@Before
	public void doStart() {
		System.out.println("-------------START----------------");
	}
	@After
	public void doEnd() {
		System.out.println("-------------END----------------");
		System.out.println();
	}
	
	
	@Test
	public void testRegex_1() {
		String pattenrS1 = "Regex";
		String patternS2 = "<.+?>";
		String matchingS1 = "正则表Regex式 Hello World,正则Regex达式 Hello World";
		String matchingS2 = "<a href='http://www.baiduc.om'><b>HyperLink</b></a>";
		Pattern pattern = Pattern.compile(patternS2);
		Matcher matcher = pattern.matcher(matchingS2);
		System.out.println("Group Count:"+matcher.groupCount());
		System.out.println(matcher.replaceAll(""));
//		System.out.println(matcher.replaceFirst("Java"));
//		System.out.println(matcher.replaceAll("Java"));
	}
	
	@Test
	public void testRegex_2() {
		Pattern pattern = Pattern.compile("^Java.+");
		Matcher matcher = pattern.matcher("Java");
		System.out.println("Group Count:"+matcher.groupCount());
		System.out.println(matcher.matches());
		
		Pattern pattern2 = Pattern.compile("[, |]+"); 
		String[] strs = pattern2.split("Java Hello World Java,Hello, ,World|Sun"); 
		for (int i=0;i<strs.length;i++) { 
		    System.out.println(strs[i]); 
		} 
	}
	
	@Test 
	public void testRegex_3() {
		Pattern pattern = Pattern.compile("\\(?\\d{3}\\)?\\s?\\d{3}(-|\\s)?\\d{4}");
		Matcher m = pattern.matcher("(235) 363-6589sd11254(225)666-5692 236 336-2368 232559-5625 (569)5559595 566959 9999");
		System.out.println("Group Count:"+m.groupCount());
		while(m.find()) {
			System.out.println(m.group());
			System.out.println(m.start(1)+"~"+m.end(1));
		}
	}
	
	@Test
	public void testRegex_4() {
		Pattern pattern = Pattern.compile("\\b(\\w+)\\s+\\1\\b",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("k k Java JaVA is really funny,so i think think that you    you should find me fun in this Language");
		System.out.println("Group Count:"+matcher.groupCount());
		while(matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.start());
			System.out.println(matcher.end());
			System.out.println(matcher.start(1)+"~"+matcher.end(1));
		}
	}
	
	
	@Test
	public void testRegex_5() {
		String[] input = {
				"Java has regular expressions in 1.4",
				"regular expression now expressing in Java",
				"Java represses oracular expressions"};
		Pattern p1 = Pattern.compile("re\\w*");
		Pattern p2 = Pattern.compile("Java.*");
		for(int i=0;i<input.length;i++) {
			System.out.println("Input "+i+" : "+input[i]);	
			Matcher m1 = p1.matcher(input[i]);
			Matcher m2 = p2.matcher(input[i]);
			while(m1.find())
				System.out.println("m1.find() : "+m1.group()+" start="+m1.start()+" end="+m1.end());
			while(m2.find())
				System.out.println("m2.find() : "+m2.group()+" start="+m2.start()+" end="+m2.end());
			if(m1.lookingAt())
				System.out.println("m1.lookingAt() found start="+m1.start()+" end="+m1.end());
			if(m2.lookingAt())
				System.out.println("m2.lookingAt() found start="+m2.start()+" end="+m2.end());
			if(m1.matches())
				System.out.println("m1.matches() start="+m1.start()+" end="+m1.end());
			if(m2.matches())
				System.out.println("m2.matches() start="+m2.start()+" end="+m2.end());
			System.out.println();
		}
	}
	
	@Test
	public void testRegex_6() {
		Pattern pattern = Pattern.compile("(^java|java$)",Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("java has regex\nJava has regex\n" +
				"JAVA has pretty good regular expressions jAva\n" +
				"Regular expressions java are in Java\n"
				);
		while(matcher.find()) 
			System.out.println(matcher.group()+" start="+matcher.start()+" end="+matcher.end());
	}
	
	/*! Here's a block of text to use as input to
    the regular expression matcher. Note that we'll
    first extract the block of text by looking for
    the special delimiters, then process the
    extracted block. !*/
	@Test
	public void testRegex_7() {
		String s= TextFile.read("RegexBase.java");
		
//		Matcher matcher = Pattern.compile("/\\*!(.*)!\\*/", Pattern.DOTALL).matcher(s);
		Matcher matcher = Pattern.compile("(?s)/\\*!(.*)!\\*/").matcher(s);
		if(matcher.find())
			s = matcher.group(1);
		s = s.replaceAll(" {2,}", " ");
		s = s.replaceAll("(?m)^ +", "");
		System.out.println(s);
		s = s.replaceFirst("[aeiou]", "(VOWEL1)");
		StringBuffer buffer = new StringBuffer();
		Pattern pattern = Pattern.compile("[aeiou]");
		Matcher m = pattern.matcher(s);
		while(m.find())  {
			String tmp = m.group();
			System.out.println(tmp+" start="+m.start()+" end="+m.end());
			m.appendReplacement(buffer, tmp.toUpperCase());
			System.out.println(buffer);
		}
		m.appendTail(buffer);
		System.out.println(buffer);
		
	}
	
	
	

}
