package org.scbit.lsbi.loger;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMain {
	
	@BeforeClass
	public static void cfgLog4j() {
		PropertyConfigurator.configure(TestMain.class.getClassLoader().getResource("log4j.properties"));
	}
	
	
	
	@Test
	public void testLoger() {
		Loger loger = new Loger();
	}
	

}
