package cn.lucifer.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 * 可阻塞的队列
 * 
 * @author Lucifer
 * 
 */
public class BlockingQueueTest {
	public static void main(String[] args) {
		final int size = 3;
		ExecutorService service = Executors.newCachedThreadPool();
		// 必须一个放，一个取，取后才能继续放
		// SynchronousQueue<String> queue = new SynchronousQueue<String>();

		// 和Lock类似, 但可以多锁、多释放
		// Semaphore semaphore = new Semaphore(size);
		// semaphore.acquire();
		// semaphore.release();
		
		// 解决ArrayList并发问题
		// CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();

		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(size);
		for (int i = 0; i < size - 1; i++) {
			service.execute(new TestRunnable(queue, true, 1000));
		}

		service.execute(new TestRunnable(queue, false, 500));
		service.shutdown();
	}

	private static class TestRunnable implements Runnable {
		final BlockingQueue<String> queue;
		final boolean isPut;
		final int baseSleepTime;

		public TestRunnable(final BlockingQueue<String> queue,
				final boolean isPut, final int baseSleepTime) {
			this.queue = queue;
			this.isPut = isPut;
			this.baseSleepTime = baseSleepTime;
		}

		@Override
		public void run() {
			while (true) {
				if (isPut) {
					putOnece();
				} else {
					takeOnece();
				}
			}
		}

		private void putOnece() {
			try {
				Thread.sleep((long) (Math.random() * baseSleepTime));
				queue.put("a");
				System.out.println(String.format("线程%s目前有【%d】个数据", Thread
						.currentThread().getName(), queue.size()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void takeOnece() {
			try {
				Thread.sleep((long) (Math.random() * baseSleepTime));
				String takeData = queue.take();
				System.out.println(String.format("线程%s目前有【%d】个数据, 获取到的数据是：%s",
						Thread.currentThread().getName(), queue.size(),
						takeData));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
