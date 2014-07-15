package cn.lucifer.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTest2 {

	private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			10);

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Hello hello;
		long delay;
		for (int i = 0; i < 11; i++) {
			hello = new Hello(i);
			// executor.submit(hello);
			if (i == 3) {
				delay = 1000;
			} else {
				delay = 1;
			}

			executor.schedule(hello, delay, TimeUnit.MILLISECONDS);
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	private static class Hello extends Thread {
		int index;

		public Hello(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			System.out.println("hello " + index);
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("bye  " + index);
		}
	}

}
