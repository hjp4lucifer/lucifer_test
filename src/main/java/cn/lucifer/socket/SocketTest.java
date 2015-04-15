package cn.lucifer.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class SocketTest {

	public static void main(String[] args) throws IOException {
		new SocketTest().testServer();
	}

	@Test
	public void testServer() throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			// server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
			Socket socket = server.accept();
			// 每接收到一个Socket就建立一个新的线程来处理它
			new Thread(new ServerTask(socket)).start();
		}

	}

	int port = 8899;

	@Test
	public void testClient() throws IOException {
		// 为了简单起见，所有的异常都直接往外抛
		String host = "127.0.0.1"; // 要连接的服务端IP地址
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 设置超时间为10秒
		client.setSoTimeout(10 * 1000);
		// 建立连接后就可以往服务端写数据了
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		writer.write("Hello Server by " + dateFormat.format(new Date()));
		writer.write('\n');
		writer.write("eof\n");
		writer.flush();
		// 写完以后进行读操作
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				client.getInputStream(), ServerTask.encode));
		StringBuilder sb = new StringBuilder();
		String line;
		int index;
		try {
			while ((line = reader.readLine()) != null) {
				if ((index = line.indexOf("eof")) != -1) {
					sb.append(line.substring(0, index));
					break;
				}
				sb.append(line).append('\n');
			}
		} catch (SocketTimeoutException e) {
			System.out.println("数据读取超时。");
		}
		System.out.println("Client say: " + sb);
		writer.close();
		reader.close();
		client.close();
	}

	@Test
	public void testClient4Thread() throws IOException {
		// 为了简单起见，所有的异常都直接往外抛
		String host = "127.0.0.1"; // 要连接的服务端IP地址
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 设置超时间为10秒
		client.setSoTimeout(10 * 1000);

		// 建立连接后就可以往服务端写数据了
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		writer.write("hi !\n");
		writer.flush();
		new Thread(new ClientWriteTask(writer)).start();

		// 写完以后进行读操作
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				client.getInputStream(), ServerTask.encode));
		String line;

		try {
			while ((line = reader.readLine()) != null) {
				if ("eof".equals(line)) {// 遇到eof时就结束接收
					System.out.println("Client say: " + line);
					break;
				}
				System.out.println("Client say: " + line);
			}
		} catch (SocketTimeoutException e) {
			System.out.println("数据读取超时。");
		}
		reader.close();
		client.close();

		writer.close();
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
					writer.write("Hello Server by "
							+ dateFormat.format(new Date()));
					writer.write('\n');
					writer.flush();

					// FIXME
					if (false) {
						break;
					}
				}
				writer.write("eof\n");
				writer.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
