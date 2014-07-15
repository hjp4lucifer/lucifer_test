package cn.lucifer.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 利用阻塞队列来
 * 
 * @author Lucifer
 * 
 */
public class BlockingQueueCommunication {
	public static void main(String[] args) {
		final Business business = new Business();
		new Thread() {
			public void run() {
				for (int i = 0; i < 50; i++) {
					business.sub(i);
				}
			};
		}.start();

		for (int i = 0; i < 50; i++) {
			business.main(i);
		}
	}

	private static class Business {

		BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> queue2 = new ArrayBlockingQueue<Integer>(1);

		{
			try {
				queue1.put(1);// 控制谁先执行
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void main(int i) {
			try {
				queue2.take();
				System.out.println("main : " + i);
				queue1.put(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void sub(int i) {
			try {
				queue1.take();
				System.out.println("sub : " + i);
				queue2.put(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
