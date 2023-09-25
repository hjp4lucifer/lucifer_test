package cn.lucifer.demo.file;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Map;

public class RarSimpleTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String startPassword = "0jDk";
	private LinkedList<Character> dictArray;
	private Map<Character, Integer> dictMap;
	private int dictSize;

	@Before
	public void setUp() throws Exception {
		dictArray = new LinkedList<>();
		for (char i = '0'; i <= '9'; i++) {
			dictArray.add(i);
		}
		for (char i = 'a'; i <= 'z'; i++) {
			dictArray.add(i);
		}
		for (char i = 'A'; i <= 'Z'; i++) {
			dictArray.add(i);
		}
		dictArray.add('@');
		dictArray.add('.');


		dictSize = dictArray.size();

		dictMap = Maps.newHashMapWithExpectedSize(dictSize);
		int j = 0;
		for (Character cc : dictArray) {
			dictMap.put(cc, j);
			j++;
		}
	}

	@Test
	public void test_junrar() throws Exception {
		FileInputStream input = new FileInputStream("M:\\tmp\\aaa.rar");
		final Archive archive = new Archive(input, startPassword);
		FileHeader fileHandler = archive.nextFileHeader();
		logger.info(String.format("fileHandler.fn=" + fileHandler.getFileName()));
	}

	@Test
	public void test_cmd() throws Exception {
		final String rarFilePath = "M:\\tmp\\aaa.rar";
		final File directory = new File("M:\\tmp\\test");

		String oldPwd = startPassword;
		int i = 0;
		while (true) {
			boolean hasLog = i++ % 100 == 0;
			String newPwd = generatePassword(oldPwd);
			String cmd = String.format("\"C:\\Program Files\\WinRAR\\Rar.exe\" e -or %s -p%s", rarFilePath, newPwd);
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd);
			Process exec = pb.directory(directory).redirectErrorStream(true).start();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream(), Charset.forName("GBK")))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (hasLog) {
						logger.info(line);
					}
				}

				int exitValue = exec.exitValue();
				// logger.info(String.format("执行命令结束：%s，exitValue=%s", cmd, exitValue));
				oldPwd = newPwd;
				if (exitValue != 0) {
					logger.info(String.format("执行命令出错，exitValue=%s，cmd=%s", exitValue, cmd));
					continue;
				}
				String successMsg = String.format("找到密码啦！！！！！cmd=%s； password=%s", cmd, newPwd);
				logger.info(successMsg);
				logger.warn(successMsg);
				logger.error(successMsg);
				return;
			} catch (Exception e) {
				logger.error(String.format("执行命令出错；cmd=%s", cmd), e);
			}
		}
	}

	@Test
	public void testPwd() {
		String oldPwd = startPassword;
		for (int i = 0; i < 300; i++) {
			String newPwd = generatePassword(oldPwd);
			logger.info(String.format("oldPwd=%s, newPwd=%s", oldPwd, newPwd));
			oldPwd = newPwd;
		}
	}

	private String generatePassword(String oldPwd) {
		char[] chars = oldPwd.toCharArray();
		for (int k = chars.length - 1; k >= 0; k--) {
			char cc = chars[k];
			if (cc == dictArray.getLast()) {
				// 进位
				chars[k] = dictArray.getFirst();
				if (k == 0) {
					// 最高位, 需要扩展数组了
					return dictArray.getFirst() + new String(chars);
				}
				// else 普通进位而已
			} else {
				Integer index = dictMap.get(chars[k]);
				chars[k] = dictArray.get(index + 1);
				return new String(chars);
			}
		}

		return null;
	}

}
