package cn.lucifer.demo.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import tutorial.Calculator;
import tutorial.CalculatorImpl;

public class ServerTest {

	public static void main(String[] args) {

		try {
			Calculator.Processor processor = new Calculator.Processor(
					new CalculatorImpl());
			TNonblockingServerTransport transport = new TNonblockingServerSocket(
					9090);
			TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(
					transport);
			tnbArgs.processor(processor);
        	tnbArgs.transportFactory(new TFramedTransport.Factory(256));
        	tnbArgs.protocolFactory(new TBinaryProtocol.Factory());
			
			
			tnbArgs.maxReadBufferBytes = 128;
			TNonblockingServer server = new TNonblockingServer(tnbArgs);

			System.out.println("server running,port:9900");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
	}

}
