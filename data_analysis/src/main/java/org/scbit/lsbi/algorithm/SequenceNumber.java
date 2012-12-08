package org.scbit.lsbi.algorithm;

public class SequenceNumber {

	private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 8;
		};
	};

	public int getNextNum() {
		seqNum.set((Integer) seqNum.get() *10);
		return (Integer) seqNum.get();
	}

	private static class TestClient extends Thread {
		private SequenceNumber sn;

		public TestClient(SequenceNumber sn) {
			this.sn = sn;
		}

		public void run() {
			/*每个线程打出三个序列值*/
			for (int i = 0; i < 3; i++) {
				System.out.println("thread[" + Thread.currentThread().getName()
						+ "] sn[" + sn.getNextNum() + "]");
			}
		}
	}
	
	public static void main(String[] args) {
		
		SequenceNumber sn = new SequenceNumber();
		TestClient t1 = new TestClient(sn);
		TestClient t2 = new TestClient(sn);
		TestClient t3 = new TestClient(sn);
		t1.start();
		t2.start();
		t3.start();
		
		
	}

}
