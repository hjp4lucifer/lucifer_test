package cn.lucifer.socket;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SocketClient4NettyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCient() throws IOException {
		final String host = "183.61.6.102";
		final int port = 8192;

		Socket client = new Socket(host, port);
		// 设置超时间为60秒
		client.setSoTimeout(60 * 1000);

		Writer writer = new OutputStreamWriter(client.getOutputStream());
		new Thread(new ClientWriteTask(writer)).start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				client.getInputStream(), ServerTask.encode));
		char chars[] = new char[64];
		int len;
		try {
			while ((len = reader.read(chars)) != -1) {
				System.out.println(new String(chars, 0, len));
			}
		} catch (SocketTimeoutException e) {
			System.err.println("数据读取超时。");
		}
		IOUtils.closeQuietly(reader);
		client.close();
	}

	protected class ClientWriteTask implements Runnable {
		Writer writer;

		public ClientWriteTask(Writer writer) {
			this.writer = writer;
		}

		@Override
		public void run() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			try {
				while (true) {
					writer.write("Hi " + dateFormat.format(new Date()));
					writer.flush();

					// FIXME
					if (true) {
						break;
					}
				}
				writer.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IOUtils.closeQuietly(writer);
		}

	}

}
