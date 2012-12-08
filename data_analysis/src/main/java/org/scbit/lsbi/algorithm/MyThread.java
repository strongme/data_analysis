package org.scbit.lsbi.algorithm;

public class MyThread extends Thread {
	
	private Common common;
	private int id;
	
	public MyThread(int id,String name,Common common) {
		super(name);
		this.id = id;
		this.common = common;
	}
	
	@Override
	public void run() {
		if(id==0) {
			common.synMethod1();
		}else {
			common.synMethod2();
		}
	}
	
	public static void main(String[] args) {
		Common common = new Common();
		MyThread t1 = new MyThread(0, "MyOne", common);
		MyThread t2 = new MyThread(1, "MyTwo", common);
		t1.start();
		t2.start();
	}
	

}
