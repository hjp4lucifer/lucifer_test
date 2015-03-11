package tutorial;

import org.apache.thrift.TException;

import tutorial.Calculator.Iface;

public class CalculatorImpl implements Iface {

	@Override
	public SharedStruct ping(SharedStruct sss) throws TException {
		System.out.println(sss);
		sss.setValue("hi hi");
		return sss;
	}

}
