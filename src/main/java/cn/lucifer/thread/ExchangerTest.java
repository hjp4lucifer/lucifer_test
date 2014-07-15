package cn.lucifer.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用于实现两个人之间的数据交换
 * 
 * @author Lucifer
 * 
 */
public class ExchangerTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Exchanger<String> exchanger = new Exchanger<String>();

		final String data1 = "apple", data2 = "orange";
		service.execute(new TestRunnable(exchanger, data1));
		service.execute(new TestRunnable(exchanger, data2));

		service.shutdown();
	}

	private static class TestRunnable implements Runnable {
		final Exchanger<String> exchanger;
		final String data;

		public TestRunnable(final Exchanger<String> exchanger, final String data) {
			this.exchanger = exchanger;
			this.data = data;
		}

		@Override
		public void run() {
			try {

				System.out.println(String.format("线程%s正准备把数据【%s】换出去", Thread
						.currentThread().getName(), data));
				Thread.sleep((long) (Math.random() * 10000));

				String data2 = exchanger.exchange(data);

				System.out.println(String.format("线程%s换回了数据【%s】", Thread
						.currentThread().getName(), data2));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
