package cn.lucifer.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒时计时器
 * <p>
 * 和{@link CyclicBarrierTest}的比, 这里多了一个命令发布者
 * </p>
 * 
 * @author Lucifer
 * 
 */
public class CountDownLatchTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final int size = 3;
		final CountDownLatch cdOrder = new CountDownLatch(1);
		final CountDownLatch cdAnswer = new CountDownLatch(size);
		for (int i = 0; i < size; i++) {
			Runnable runnable = new TestRunnable(cdOrder, cdAnswer);
			service.execute(runnable);
		}

		try {
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println(String.format("线程%s正发布命令, 等待命令被接受", Thread
					.currentThread().getName()));

			cdOrder.countDown();

			System.out.println(String.format("线程%s已发布命令, 正等待结果", Thread
					.currentThread().getName()));

			cdAnswer.await();

			System.out.println(String.format("线程%s已收到所有的报告", Thread
					.currentThread().getName()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		service.shutdown();
	}

	private static class TestRunnable implements Runnable {
		CountDownLatch cdOrder;
		CountDownLatch cdAnswer;

		public TestRunnable(CountDownLatch cdOrder, CountDownLatch cdAnswer) {
			super();
			this.cdOrder = cdOrder;
			this.cdAnswer = cdAnswer;
		}

		@Override
		public void run() {
			try {
				System.out.println(String.format("线程%s正准备接受命令", Thread
						.currentThread().getName()));
				cdOrder.await();

				System.out.println(String.format("线程%s正执行命令", Thread
						.currentThread().getName()));
				Thread.sleep((long) (Math.random() * 10000));

				System.out.println(String.format("线程%s回复命令结果", Thread
						.currentThread().getName()));
				cdAnswer.countDown();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
