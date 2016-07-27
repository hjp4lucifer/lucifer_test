package cn.lucifer.demo.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FindStringInJson {

	// String keyword = "GeoCursorBase";
	String keyword = "冰果";

	// final String folderPath = "D:/workspace/yinba_web/public/bg/jslib";
	// final String folderPath =
	// "D:/tools/mongodb-src-r2.4.11/mongodb-src-r2.4.11/src";
	final String folderPath = "F:/lcf/cartoon";

	final String fileSuffix = ".json";
	
	final LinkedList<String> linkedList = new LinkedList<>();

	@Test
	public void main() throws IOException {
		File folder = new File(folderPath);
		keyword = keyword.toLowerCase();
		find(folder);
		// System.in.read();
	}

	protected void find(File folder) throws IOException {
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return file.getName().endsWith(fileSuffix);
			}
		});

		for (File file : files) {
			if (file.isDirectory()) {
				find(file);
				continue;
			}
			String json = FileUtils.readFileToString(file, "utf-8").trim();
			if (!json.toLowerCase().contains(keyword)) {
				continue;
			}
			
			linkedList.clear();
			linkedList.add(file.getName());
			try {
				JSONObject jsonObj = JSON.parseObject(json);
				findJsonObj(jsonObj);
			} catch (Exception e) {
			}
			try {
				JSONArray jsonObj = JSON.parseArray(json);
				findJsonArray(jsonObj);
			} catch (Exception e) {
				continue;
			}
		}
	}

	protected void findJsonObj(JSONObject jsonObj) {
		for (Entry<String, Object> entry : jsonObj.entrySet()) {
			linkedList.add(entry.getKey());
			processValue(entry.getValue());
			linkedList.removeLast();
		}
	}

	protected void processValue(Object value) {
		if (value instanceof JSONObject) {
			JSONObject val = (JSONObject) value;
			findJsonObj(val);
		} else if (value instanceof JSONArray) {
			JSONArray val = (JSONArray) value;
			findJsonArray(val);
		} else if (value instanceof String) {
			String val = ((String) value).toLowerCase();
			if(val.contains(keyword)){
				linkedList.add((String) value);
				System.out.println(linkedList.toString());
				linkedList.removeLast();
			}
		}
	}

	protected void findJsonArray(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.size(); i++) {
			linkedList.add("array");
			processValue(jsonArray.get(i));
			linkedList.removeLast();
		}
	}
}
