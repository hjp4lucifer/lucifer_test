package cn.lucifer.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

	private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			10);

	public static void main(String[] args) {
		Hello hello = new Hello();
		executor.scheduleAtFixedRate(hello, 0, 3, TimeUnit.SECONDS);
	}

	private static class Hello extends Thread {
		int i = 0;

		@Override
		public void run() {
			System.out.println("hello " + ++i);
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("wake " + i);
		}
	}

}
