package cn.lucifer.thread;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;

public class MapThreadTest {
	ExecutorService service;

	@Before
	public static void main(String[] args) {
		ExecutorService service;
		service = Executors.newCachedThreadPool();
		final HashMap<Integer, Integer> map = new HashMap<>(30);
		for (int i = 1; i < 4; i++) {
			map.put(i, i);
		}
		System.out.println(map);
		final CountDownLatch cdOrder = new CountDownLatch(1);
		final CountDownLatch cdAnswer = new CountDownLatch(2);

		service.execute(new EachThread(map, cdOrder, cdAnswer));
		service.execute(new PutThread(map, cdOrder, cdAnswer));

		cdOrder.countDown();
		try {
			cdAnswer.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(map);
		service.shutdown();
	}

	static class EachThread extends Thread {
		HashMap<Integer, Integer> map;
		CountDownLatch cdOrder;
		CountDownLatch cdAnswer;

		public EachThread(HashMap<Integer, Integer> map,
				CountDownLatch cdOrder, CountDownLatch cdAnswer) {
			this.map = map;
			this.cdOrder = cdOrder;
			this.cdAnswer = cdAnswer;
		}

		@Override
		public void run() {
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			cdAnswer.countDown();
		}
	}

	static class PutThread extends Thread {
		HashMap<Integer, Integer> map;
		CountDownLatch cdOrder;
		CountDownLatch cdAnswer;

		public PutThread(HashMap<Integer, Integer> map, CountDownLatch cdOrder,
				CountDownLatch cdAnswer) {
			this.map = map;
			this.cdOrder = cdOrder;
			this.cdAnswer = cdAnswer;
		}

		@Override
		public void run() {
			for (int i = 1; i < 4; i++) {
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int key = i % 3;

				System.out.println("key=" + key);
				// map.remove(key);
				map.put(key, map.get(key) == null ? 0 : map.get(key) + 1);
			}
			cdAnswer.countDown();
		}
	}
}
