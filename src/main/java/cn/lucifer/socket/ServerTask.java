package cn.lucifer.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTask implements Runnable {

	private Socket socket;

	public ServerTask(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			handleSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String encode = "utf-8";

	SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * 跟客户端Socket进行通信
	 * 
	 * @throws Exception
	 */
	private void handleSocket() throws Exception {
		new Thread(new SocketInputTask()).start();
		new SocketOutputTask().handleSocketOutput();
	}

	class SocketInputTask implements Runnable {
		protected void handleSocketInput() throws Exception {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), encode));
			String line;
			while ((line = reader.readLine()) != null) {
				if ("eof".equals(line)) {// 遇到eof时就结束接收
					System.err.println(line);
					break;
				}
				System.err.println("Server say: " + line);
			}

			reader.close();
		}

		@Override
		public void run() {
			try {
				handleSocketInput();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class SocketOutputTask implements Runnable {
		protected void handleSocketOutput() throws Exception {
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			while (true) {
				writer.write("Hello Client by ");
				writer.write(dateFormat.format(new Date()));
				writer.write('\n');
				writer.flush();
				// FIXME
				if (false) {
					break;
				}
			}
			writer.write("eof\n");
			writer.flush();
			writer.close();
			socket.close();
		}

		@Override
		public void run() {
			try {
				handleSocketOutput();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
