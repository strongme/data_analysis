package org.scbit.lsbi.upload;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class MultiFileUpload {
	
	private String getrandomBounary() {
		String result = "------WebKitFormBoundary";
		String repo = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYX";
		for(int i=0;i<16;i++) {
			result += repo.charAt((int) Math.round(Math.random()*51));
		}
		return result;
	}
	
	@Test
	public void testChatByte() {
		File file = new File("D:/桌面文件/tmp_pom/activation-1.0.2.pom");
		String randomBoundary = getrandomBounary();
		StringBuilder sb = new StringBuilder();
		sb.append(randomBoundary+"\n\n");
		sb.append("Content-Disposition: form-data; name=\"r\""+"\r\n\n\n\n");
		sb.append("thirdparty"+"\n\n");
		sb.append(randomBoundary+"\n\n");
		sb.append("Content-Disposition: form-data; name=\"hasPom\""+"\r\n\n\n\n");
		sb.append("true"+"\n\n");
		sb.append(randomBoundary+"\n\n");
		sb.append("Content-Disposition: form-data; name=\"c\""+"\r\n\n\n\n");
		sb.append("null"+"\n\n");
		sb.append(randomBoundary+"\n\n");
		sb.append("Content-Disposition: form-data; name=\"e\""+"\r\n\n\n\n");
		sb.append("null"+"\n\n");
		sb.append(randomBoundary+"\n\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"D:/桌面文件/tmp_pom/xalan-2.5.1.pom\""+"\n\n");
		sb.append("Content-Type: application/octet-stream"+"\r\r\n\n\n\n\n");
		sb.append(randomBoundary+"--"+"\n");
		OutputStream out = null;
		DataInputStream in = null;
		try {
			URL url = new URL("http://localhost:8081/nexus/service/local/artifact/maven/content?authorization=Basic%20YWRtaW46YWRtaW4xMjM=");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			conn.setRequestProperty("Cache-Control", "max-age=0");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+randomBoundary);
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Host", "localhost:8081");
			conn.setRequestProperty("Origin", "http://localhost:8081");
			conn.setRequestProperty("Referer", "http://localhost:8081/nexus/index.html");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.47 Safari/536.11");
			
			out = new DataOutputStream(conn.getOutputStream());
			byte[] data = sb.toString().getBytes();
			out.write(data);
			System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());
			InputStream ins = conn.getInputStream();
			int a = 0;
			while((a=ins.read())!=-1) {
				System.out.println(a);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
