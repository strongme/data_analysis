package org.scbit.lsbi.loger;

import org.apache.log4j.Logger;

public class Loger {
	
	private static Logger log = Logger.getLogger(Loger.class);
	
	public Loger() {
		log.info("创建Loger类事例");
		for(int i=0;i<30;i++) {
			log.info("Message from constructor of the "+(i+1)+"message");
		}
		
		
		
	}
}
