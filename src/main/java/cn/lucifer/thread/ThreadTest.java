package cn.lucifer.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThreadTest {

	private ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10);

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		pool.shutdown();
//		pool.shutdownNow();
		pool.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("tear down!!! , ActiveCount=" + pool.getActiveCount());
	}

	@Test
	public void testTimeout() {
//		pool.submit(new Hello());
		pool.submit(new Hello2());
	}

	@Test
	public void testScheduleAtFixedRate() {
		Hello hello = new Hello();
		pool.scheduleAtFixedRate(hello, 0, 3, TimeUnit.SECONDS);
	}

	private class Hello extends Thread {
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

	private class Hello2 extends Thread {
		int i = 0;

		@Override
		public void run() {
			for (int j = 0; j < 10000; j++) {
				System.out.println("hello " + ++i);
				try {
					sleep(1900);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("wake " + i);
			}

		}
	}
}
