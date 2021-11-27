package cn.lucifer.demo.weibo;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import cn.lucifer.util.HttpClientHelper;

import java.io.IOException;

public class SimpleTest {

	protected final String access_token = "2.00pebQzBjguruB8f5e5c144cI9a8jD";
	protected final String uid = "1756456945";

	@Test
	public void testUsersShow() throws IOException {
		String url = "https://api.weibo.com/2/users/show.json";
		NameValuePair[] parametersBody = new NameValuePair[] {
				new NameValuePair("access_token", access_token),
				new NameValuePair("uid", uid) };

		byte[] data = HttpClientHelper.httpGet(url, parametersBody);
		System.out.println(new String(data));
	}

}
