package cn.lucifer.demo.sql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * used HeidiSQL's SQL format
 * 
 * @author Lucifer
 *
 */
public class TableSqlBase {
	public static Logger log = Logger.getLogger("lucifer_test");

	protected final HashMap<String, String> typeMap;

	public TableSqlBase() {
		typeMap = new HashMap<>();
		typeMap.put("BIGINT", "long");
		typeMap.put("TIMESTAMP", "Date");
		typeMap.put("INT", "int");
		typeMap.put("TINYINT", "int");
		typeMap.put("VARCHAR", "String");
		typeMap.put("FLOAT", "float");
		typeMap.put("DOUBLE", "double");
		typeMap.put("SMALLINT", "int");
		typeMap.put("TEXT", "String");
	}

	protected String tabelName;
	protected String className;
	/**
	 * null表示非简单主键
	 */
	protected FieldInfo primaryKey;

	protected class FieldInfo {
		protected String name;
		protected String columnName;
		protected String columnType;
		protected String comment;
		protected boolean isAutoIncrement;
		protected String type;
		/**
		 * 0没默认值, 1默认NULL, 2CURRENT_TIMESTAMP, 10除外
		 */
		protected int defaultType;

		public FieldInfo(String name, String columnName, String columnType,
				String comment, boolean isAutoIncrement, String type,
				int defaultType) {
			super();
			this.name = name;
			this.columnName = columnName;
			this.comment = comment;
			this.isAutoIncrement = isAutoIncrement;
			this.type = type;
			this.defaultType = defaultType;
		}

	}

	protected LinkedList<FieldInfo> fieldList = new LinkedList<>();

	public void parseSql(File file) throws IOException {
		List<String> sqlLines = FileUtils.readLines(file);
		tabelName = sqlLines.get(0);
		tabelName = tabelName.substring(tabelName.indexOf("`") + 1,
				tabelName.lastIndexOf("`"));
		log.debug("tabelName=" + tabelName);

		className = changeNameForClass(tabelName);
		log.debug("className=" + className);

		for (int j = 1; j < sqlLines.size(); j++) {
			String line = sqlLines.get(j);
			if (line.contains("PRIMARY KEY")) {
				processPrimateKey(line);
				break;
			}
			log.debug(line);

			int commentIndex = line.lastIndexOf("COMMENT");
			String commentStr = null;
			if (commentIndex > 0) {// 有注释
				commentStr = line.substring(commentIndex);
				commentStr = commentStr.substring(commentStr.indexOf("'") + 1,
						commentStr.lastIndexOf("'"));
				int endIndex = commentStr.indexOf(" COLLATE");
				if (endIndex > 0) {
					commentStr = commentStr.substring(0, endIndex - 1);
				}
				log.debug("commentStr=" + commentStr);
			}

			boolean isAutoIncrement = false;
			if (line.contains(" AUTO_INCREMENT ")) {
				isAutoIncrement = true;
			}

			int defaultType = 0;
			if (line.contains(" DEFAULT NULL ")) {
				defaultType = 1;
			} else if (line.contains(" DEFAULT '")) {
				defaultType = 10;
			} else if (line.contains(" DEFAULT CURRENT_TIMESTAMP ")) {
				defaultType = 2;
			}

			int fieldIndex = 0;
			String fieldName = null;
			String columnName = null;
			String columnType = null;
			String fieldTypeName = null;
			for (int i = 0, len = line.length(); i < len; i++) {
				if (0 == fieldIndex) {// 字段名
					if (line.charAt(i) == '`') {
						i++;
						int startIndex = i;
						for (; i < len; i++) {
							if (line.charAt(i) == '`') {
								break;
							}
						}
						columnName = line.substring(startIndex, i);
						fieldName = changeNameForField(columnName);
						log.debug("fieldName=" + fieldName);
						fieldIndex++;
					}
				} else if (1 == fieldIndex) {// 类型
					if (line.charAt(i) != ' ') {
						int startIndex = i;
						for (i++; i < len; i++) {
							if (line.charAt(i) == ' ' || line.charAt(i) == '(') {
								break;
							}
						}
						columnType = line.substring(startIndex, i);
						boolean notFind = true;
						for (Entry<String, String> entry : typeMap.entrySet()) {
							if (entry.getKey().equals(columnType)) {
								fieldTypeName = entry.getValue();
								log.debug("fieldTypeName=" + fieldTypeName);
								notFind = false;
								break;
							}
						}
						if (notFind) {
							throw new RuntimeException("No map type="
									+ fieldTypeName);
						}
						fieldIndex++;
					}
				}

				if (fieldIndex > 2) {
					break;
				}
			}

			FieldInfo info = new FieldInfo(fieldName, columnName, columnType,
					commentStr, isAutoIncrement, fieldTypeName, defaultType);
			fieldList.add(info);
		}
	}

	protected void processPrimateKey(String line) {
		String primaryKeyName = line.substring(line.indexOf("`") + 1,
				line.lastIndexOf("`"));
		if (primaryKeyName.indexOf(",") > 0) {
			return;
		}
		primaryKeyName = changeNameForField(primaryKeyName);
		for (FieldInfo info : fieldList) {
			if (primaryKeyName.equals(info.name)) {
				primaryKey = info;
				return;
			}
		}
	}

	protected String changeNameForClass(String name) {
		char[] array = new char[name.length()];
		array[0] = Character.toUpperCase(name.charAt(0));
		int i = 1;
		for (int j = 1, len = array.length; j < len; i++, j++) {
			if (name.charAt(j) == '_' && (j + 1) < len) {
				j++;
				array[i] = Character.toUpperCase(name.charAt(j));
			} else {
				array[i] = name.charAt(j);
			}
		}
		return new String(array, 0, i);
	}

	protected String changeNameForField(String name) {
		char[] array = new char[name.length()];
		array[0] = Character.toLowerCase(name.charAt(0));
		int i = 1;
		for (int j = 1, len = array.length; j < len; i++, j++) {
			if (name.charAt(j) == '_' && (j + 1) < len) {
				j++;
				array[i] = Character.toUpperCase(name.charAt(j));
			} else {
				array[i] = name.charAt(j);
			}
		}
		return new String(array, 0, i);
	}
}
