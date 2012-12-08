package org.scbit.lsbi.algorithm;

import org.junit.Ignore;
import org.junit.Test;

import com.sun.jmx.snmp.Timestamp;

/**
 * 练习一些算法知识
 * @author 1
 *
 */

public class DoAlg {
	
	/**
	 * 计算num中含1 的比特数
	 * @param num
	 */
	@Ignore
	@Test
	public void countNumOne() {
		int num = 15;
		/*左移一位表示原来的数乘2*/
		System.out.println(num<<1);
		/*^异或：相同则为0，不同 就为1*/
		System.out.println(num^1);
		System.out.println(num);
		String result = "";
		int count = 0;
		int tmp = num;
		while(tmp!=0) {
			if(tmp%2 !=0) {
				count++;
			}
			tmp = tmp >> 1;
		}
		System.out.println("There are "+count+" '1' ");
	}
	
	@Ignore
	@Test
	public void timeNano() {
		
		long t1 = System.nanoTime();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long t2 = System.nanoTime();
		System.out.println(t2-t1);
	}
	@Ignore
	@Test
	public void checkInterface() {
		
		/*借口不是Object*/
		System.out.println(Me.class.getSuperclass());
		System.out.println(DoAlg.class.getSuperclass());
		Me you = new You();
		System.out.println(You.class.getSuperclass());
		System.out.println(you.getClass().getSuperclass());
		You you2 = new You();
		System.out.println(you2.getClass().getSuperclass());
	}
	
	class You implements Me {}
	
	interface Me {
		
	}
	
	@Test
	public void isEven() {
		System.out.println("------&------");
		long now1 = System.nanoTime();
		System.out.println("4222222是偶数?"+((4222222&1)==0));
		long now2 = System.nanoTime();
		System.out.println("耗时间："+(now2-now1));
		System.out.println("------%2==0---");
		long now3 = System.nanoTime();
		System.out.println("4222222是偶数?"+(4222222%2==0));
		long now4 = System.nanoTime();
		System.out.println();
		System.out.println("耗时间："+(now4-now3));
		
	}
	
	

}
