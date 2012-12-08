package org.scbit.lsbi.algorithm;

public class Common {
	
	
	public synchronized void  synMethod1() {
		System.out.println("synMethod1 is called");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("synMethod1 is done");
	}
	
	public synchronized void  synMethod2() {
		System.out.println("synMethod2 is called");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("synMethod2 is done");
	}

}
