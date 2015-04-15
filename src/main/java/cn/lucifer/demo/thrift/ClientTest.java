package cn.lucifer.demo.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import tutorial.Calculator;
import tutorial.SharedStruct;

public class ClientTest {
	@Test
	public void testClient() throws Exception {
		try {
			TTransport transport = new TSocket("127.0.0.1", 9090);
//			TTransport transport = new TSocket("local.game.duowan.com", 9090);
			TProtocol protocol = new TBinaryProtocol(new TFramedTransport(
					transport));
//			TProtocol protocol = new TJSONProtocol(new TFramedTransport(
//					transport));
			Calculator.Client client = new Calculator.Client(protocol);
			transport.open();

			SharedStruct sss = new SharedStruct(111, "my", Long.MAX_VALUE);
			SharedStruct adInfoStr = client.ping(sss);
			transport.close();

			System.out.println("adInfoStr=" + adInfoStr);
			System.out.println(sss.key + " = " + sss.getValue());
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
