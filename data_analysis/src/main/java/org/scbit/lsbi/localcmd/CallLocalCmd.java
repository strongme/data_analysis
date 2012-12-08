package org.scbit.lsbi.localcmd;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class CallLocalCmd {
	
	@Ignore
	@Test
	public void localCall_1() throws Exception {
//		Runtime.getRuntime().exec("F:/Program Files (x86)/PotPlayer/PotPlayerMini.exe");
		/**
		 * Java 调用本地命令打开制定目录文件
		 */
		Runtime.getRuntime().exec("cmd /c start D:/桌面文件/TMP/applicationContext.xml");
	}
	
	@Test
	public void getInfoOf_OS() {
		System.out.println(System.getenv("JAVA_HOME"));
		Map<String,String> envs = System.getenv();
//		System.out.println(envs);
//		System.out.println(System.getProperties());
//		System.out.println(System.nanoTime());;
	}
	

}
