package cn.lucifer.thread;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	public static void main(String[] args) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println(System.currentTimeMillis());
			}
		}, 10, 300);
		// quartz可以处理定时问题
	}
}
