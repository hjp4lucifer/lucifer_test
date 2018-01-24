package cn.lucifer.demo.serialization;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class SerializationUtilsTest {

	private final String test_file_path = "results/serial.test";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSerialize() throws IOException {
		TestA a = new TestA();
		a.a = "aaa";
		byte[] data = SerializationUtils.serialize(a);
		FileUtils.writeByteArrayToFile(new File(test_file_path), data);
	}

	@Test
	public void testDeserialize() throws IOException {
		byte[] data = FileUtils.readFileToByteArray(new File(test_file_path));
		Object a = SerializationUtils.deserialize(data);
		System.out.println(JSON.toJSONString(a, true));
	}
}
