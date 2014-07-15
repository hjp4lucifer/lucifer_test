package cn.lucifer.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 循环障碍
 * 
 * @author Lucifer
 * 
 */
public class CyclicBarrierTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final int size = 3;
		final CyclicBarrier cb = new CyclicBarrier(size);
		for (int i = 0; i < size; i++) {
			Runnable runnable = new TestRunnable(size, cb);
			service.execute(runnable);
		}
		service.shutdown();
	}

	private static class TestRunnable implements Runnable {

		private int size;
		private CyclicBarrier cb;

		public TestRunnable(int size, CyclicBarrier cb) {
			super();
			this.size = size;
			this.cb = cb;
		}

		@Override
		public void run() {
			try {
				int index = 0;
				waitPoint(size, cb, index++);

				waitPoint(size, cb, index++);

				waitPoint(size, cb, index++);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void waitPoint(final int size, final CyclicBarrier cb, int index)
				throws InterruptedException, BrokenBarrierException {
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println(String.format("线程%s即将到达地点%d，当前已有%d个并发, %s",
					Thread.currentThread().getName(), index, cb
							.getNumberWaiting() + 1,
					cb.getNumberWaiting() < size - 1 ? "正在等候" : "go again !"));
			cb.await();
		}
	}
}
