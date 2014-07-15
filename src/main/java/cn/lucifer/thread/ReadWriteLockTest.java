package cn.lucifer.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁测试, 区别于{@link java.util.concurrent.locks.ReentrantLock}和
 * {@link java.util.concurrent.locks.ReentrantLock#newCondition()}中的
 * {@link java.util.concurrent.locks.Condition}
 * 
 * @author Lucifer
 * 
 */
public class ReadWriteLockTest {
	public static void main(String[] args) {
		final Queue3 q3 = new Queue3();

		for (int i = 0; i < 3; i++) {
			new Thread() {
				public void run() {
					while (true) {
						q3.get();
					}
				};
			}.start();

			new Thread() {
				public void run() {
					while (true) {
						q3.put((int) (Math.random() * 10000));
					}
				};
			}.start();
		}
	}

	private static class Queue3 {
		private Object data = null;// 共享数据, 只能有一个线程访问
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		public void get() {
			lock.readLock().lock();
			printThreadNameAndSay(" ready read !");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printThreadNameAndSay(" read finish ! data : " + data);
			lock.readLock().unlock();
		}

		public void put(int d) {
			lock.writeLock().lock();
			printThreadNameAndSay(" ready write !");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.data = d;
			printThreadNameAndSay(" write finish ! data : " + data);
			lock.writeLock().unlock();
		}

		private void printThreadNameAndSay(String sayContent) {
			System.out.println(Thread.currentThread().getName() + " : "
					+ sayContent);
		}
	}

	/**
	 * 读写锁官方示例
	 * 
	 * @see ReentrantReadWriteLock
	 * 
	 */
	class CachedData {
		Object data;
		volatile boolean cacheValid;
		ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

		void processCachedData() {
			rwl.readLock().lock();
			if (!cacheValid) {
				// Must release read lock before acquiring write lock
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				// Recheck state because another thread might have acquired
				// write lock and changed state before we did.
				if (!cacheValid) {
					// data = ...
					cacheValid = true;
				}
				// Downgrade by acquiring read lock before releasing write lock
				// 简单说就是恢复processCachedData()第一行中的读锁，以保持功能不发生变化
				rwl.readLock().lock();
				rwl.writeLock().unlock(); // Unlock write, still hold read
			}

			// use(data);
			rwl.readLock().unlock();
		}
	}

}
