package org.scbit.lsbi.parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

public class GetWOEID {
	@Ignore
	@Test
	public void getWoeid() {
		String shanghai = "%E4%B8%8A%E6%B5%B7";
		System.out.println(";http://.*.gif");
		try {
			String tmp = URLEncoder.encode("上海", "UTF-8");
			System.out.println(tmp);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Ignore
	@Test
	public void testConnWOEID() {
		
		try {
			Document docForWoeid = Jsoup.connect("http://sigizmund.info/woeidinfo/?woeid="+ URLEncoder.encode("太原", "UTF-8")).get();
			Element loc = docForWoeid.select("h3").first();
			System.out.println(loc.text());
			String patternStr = "WOEID: [0-9]*";
			String allText = docForWoeid.body().text();
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(allText);
			String woeid = null;
			if(matcher.find()) {
				woeid = matcher.group().substring(7);
				System.out.println("WOEID:"+woeid);
			}
			Document docForWeather = Jsoup.connect("http://weather.yahooapis.com/forecastrss?w="+woeid+"&u=c").get();
			System.out.println("City Name:"+docForWeather.getElementsByTag("yweather:location").attr("city"));
			System.out.println("Country Name:"+docForWeather.getElementsByTag("yweather:location").attr("country"));
			System.out.println("Date:"+docForWeather.getElementsByTag("pubdate").text());
			System.out.println("Tempreature:\n"+docForWeather.getElementsByTag("yweather:forecast").attr("low")+"℃ ~ "+docForWeather.getElementsByTag("yweather:forecast").attr("high")+"℃");
			System.out.println("Wind Speed:"+docForWeather.getElementsByTag("yweather:wind").attr("speed")+"km/h");
			System.out.println("Humidity:"+docForWeather.getElementsByTag("yweather:atmosphere").attr("humidity")+"%");
			System.out.println("Visibility:"+docForWeather.getElementsByTag("yweather:atmosphere").attr("visibility")+"km");
			System.out.println("Pressure:"+docForWeather.getElementsByTag("yweather:atmosphere").attr("pressure")+"km");
			System.out.println("Sunrise:"+docForWeather.getElementsByTag("yweather:astronomy").attr("sunrise"));
			System.out.println("Sunset:"+docForWeather.getElementsByTag("yweather:astronomy").attr("sunset"));
			System.out.println("Latitude:"+docForWeather.getElementsByTag("geo:lat").text());
			System.out.println("Longitude:"+docForWeather.getElementsByTag("geo:long").text());
			//获得天气表述图片url
			String urlForImg = null;
			Document docTmp = Jsoup.parse(docForWeather.getElementsByTag("description").text());
			urlForImg = docTmp.getElementsByTag("img").attr("src");
			System.out.println(docTmp.getElementsByTag("img").attr("src"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIsChinese() {
		String chinese = "上海";
		String english = "shanghai";
		System.out.println(chinese.getBytes().length==chinese.length());
		System.out.println(english.getBytes().length==english.length());
		System.out.println(new Date().getYear()+1900);
		System.out.println(new Date().getMonth()+1);
		System.out.println(new Date().getDate());
		
		
	}
	
	
}
