package cn.lucifer.thread;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ForkJoinTest {

	int processors;

	@Before
	public void setUp() throws Exception {
		processors = Runtime.getRuntime().availableProcessors();
		System.out.println("processors count = " + processors);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		final ForkJoinPool mainPool = new ForkJoinPool();
		long[] array = new long[134];
		for (int i = 0, len = array.length; i < len; i++) {
			array[i] = (long) (Math.random() * 10000);
		}

		System.out.println(Arrays.toString(array));
		//mainPool.invoke(new SortTask(array, 0, array.length));
		mainPool.invoke(new IncrementTask(array, 0, array.length));
		System.out.println(Arrays.toString(array));
	}

	public int THRESHOLD = 50;

	class SortTask extends RecursiveAction {
		final long[] array;
		final int lo;
		final int hi;

		SortTask(long[] array, int lo, int hi) {
			this.array = array;
			this.lo = lo;
			this.hi = hi;
		}

		protected void compute() {
			if (hi - lo < THRESHOLD)
				sequentiallySort(array, lo, hi);
			else {
				int mid = (lo + hi) >>> 1;
				invokeAll(new SortTask(array, lo, mid), new SortTask(array,
						mid, hi));
				merge(array, lo, hi);
			}
		}

		private void merge(long[] array2, int fromIndex, int toIndex) {

		}

		private void sequentiallySort(long[] array2, int fromIndex, int toIndex) {
			Arrays.sort(array2, fromIndex, toIndex);
		}
	}

	class IncrementTask extends RecursiveAction {
		final long[] array;
		final int lo;
		final int hi;

		IncrementTask(long[] array, int lo, int hi) {
			this.array = array;
			this.lo = lo;
			this.hi = hi;
		}

		protected void compute() {
			if (hi - lo < THRESHOLD) {
				for (int i = lo; i < hi; ++i)
					array[i]++;
			} else {
				int mid = (lo + hi) >>> 1;
				invokeAll(new IncrementTask(array, lo, mid), new IncrementTask(
						array, mid, hi));
			}
		}
	}

	class Applyer extends RecursiveAction {
		final double[] array;
		final int lo, hi;
		double result;
		Applyer next; // keeps track of right-hand-side tasks

		Applyer(double[] array, int lo, int hi, Applyer next) {
			this.array = array;
			this.lo = lo;
			this.hi = hi;
			this.next = next;
		}

		double atLeaf(int l, int h) {
			double sum = 0;
			for (int i = l; i < h; ++i)
				// perform leftmost base step
				sum += array[i] * array[i];
			return sum;
		}

		protected void compute() {
			int l = lo;
			int h = hi;
			Applyer right = null;
			while (h - l > 1 && getSurplusQueuedTaskCount() <= 3) {
				int mid = (l + h) >>> 1;
				right = new Applyer(array, mid, h, right);
				right.fork();
				h = mid;
			}
			double sum = atLeaf(l, h);
			while (right != null) {
				if (right.tryUnfork()) // directly calculate if not stolen
					sum += right.atLeaf(right.lo, right.hi);
				else {
					right.join();
					sum += right.result;
				}
				right = right.next;
			}
			result = sum;
		}
	}
}
