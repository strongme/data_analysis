package org.scbit.lsbi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFile {
	
	public static String read(String fileName) {
		InputStream in =TextFile.class.getResourceAsStream("/org/scbit/lsbi/parser/"+fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder builder = new StringBuilder();
		String tmp = null;
		try {
			while((tmp=reader.readLine())!=null) {
				System.out.println(tmp);
				builder.append(tmp+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	public static BufferedReader readAsReader(String fileName) {
		InputStream in =TextFile.class.getResourceAsStream("/org/scbit/lsbi/parser/"+fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		return reader;
	}

}
