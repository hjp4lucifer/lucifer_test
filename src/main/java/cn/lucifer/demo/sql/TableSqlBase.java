package cn.lucifer.demo.sql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * used HeidiSQL's SQL format
 *
 * @author Lucifer
 *
 */
public class TableSqlBase {
	public static Logger log = Logger.getLogger("lucifer_test");

	protected boolean hasUserId;

	protected final HashMap<String, String> typeMap;

	public TableSqlBase() {
		typeMap = new HashMap<>();
		typeMap.put("BIGINT", "Long");
		typeMap.put("TIMESTAMP", "Date");
		typeMap.put("INT", "Integer");
		typeMap.put("MEDIUMINT", "Integer");
		typeMap.put("TINYINT", "Integer");
		typeMap.put("VARCHAR", "String");
		typeMap.put("CHAR", "String");
		typeMap.put("FLOAT", "Float");
		typeMap.put("DOUBLE", "Double");
		typeMap.put("SMALLINT", "Integer");
		typeMap.put("TEXT", "String");
		typeMap.put("DATE", "Date");
		typeMap.put("BLOB", "byte[]");
		typeMap.put("DECIMAL", "BigDecimal");
	}

	protected String tableName;
	protected String className;
	protected String tableComment;

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

		protected boolean isPrimaryKey;

		public FieldInfo(String name, String columnName, String columnType, String comment, boolean isAutoIncrement,
				String type, int defaultType) {
			super();
			this.name = name;
			this.columnName = columnName;
			this.columnType = columnType;
			this.comment = comment;
			this.isAutoIncrement = isAutoIncrement;
			this.type = type;
			this.defaultType = defaultType;
		}

	}

	protected LinkedList<FieldInfo> fieldList = new LinkedList<>();

	protected boolean checkFieldType(String columnType) {
		for (FieldInfo f : fieldList) {
			if (f.columnType.equals(columnType)) {
				return true;
			}
		}
		return false;
	}

	public void parseSql(File file) throws IOException {
		List<String> sqlLines = FileUtils.readLines(file);
		tableName = sqlLines.get(0);
		tableName = tableName.substring(tableName.indexOf("`") + 1, tableName.lastIndexOf("`"));
		log.debug("tableName=" + tableName);

		className = changeNameForClass(tableName);
		log.debug("className=" + className);

		for (int j = 1; j < sqlLines.size(); j++) {
			String line = sqlLines.get(j);
			if (line.contains("PRIMARY KEY")) {
				processPrimateKey(line);
				for (j++; j < sqlLines.size(); j++) {
					// find table comment
					line = sqlLines.get(j);
					if (line.startsWith("COMMENT=")) {
						tableComment = StringUtils
								.trimToNull(line.substring(line.indexOf("'") + 1, line.lastIndexOf("'")));
					}
				}
				break;
			}
			log.debug(line);

			int commentIndex = line.lastIndexOf("COMMENT");
			String commentStr = null;
			if (commentIndex > 0) {// 有注释
				commentStr = line.substring(commentIndex);
				commentStr = commentStr.substring(commentStr.indexOf("'") + 1, commentStr.lastIndexOf("'"));
				int endIndex = commentStr.indexOf(" COLLATE");
				if (endIndex > 0) {
					commentStr = commentStr.substring(0, endIndex - 1);
				}
				log.debug("commentStr=" + commentStr);
			}

			boolean isAutoIncrement = false;
			if (line.contains(" AUTO_INCREMENT")) {
				isAutoIncrement = true;
			}

			int defaultType = 0;
			if (line.contains(" DEFAULT NULL ")) {
				defaultType = 1;
			} else if (line.contains(" DEFAULT '")) {
				defaultType = 10;
			} else if (line.contains(" DEFAULT CURRENT_TIMESTAMP")) {
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
							throw new RuntimeException("No map type=" + columnType);
						}
						fieldIndex++;
					}
				}

				if (fieldIndex > 2) {
					break;
				}
			}

			FieldInfo info = new FieldInfo(fieldName, columnName, columnType, commentStr, isAutoIncrement,
					fieldTypeName, defaultType);
			fieldList.add(info);

			if ("user_id".equals(columnName)) {
				hasUserId = true;
			}
		}
	}

	protected void processPrimateKey(String line) {
		String primaryKeyName = line.substring(line.indexOf("`") + 1, line.lastIndexOf("`"));
		if (primaryKeyName.indexOf(",") > 0) {
			return;
		}
		primaryKeyName = changeNameForField(primaryKeyName);
		for (FieldInfo info : fieldList) {
			if (primaryKeyName.equals(info.name)) {
				primaryKey = info;
				info.isPrimaryKey = true;
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

	protected String firstCharToLowerCase(String name) {
		char[] array = new char[name.length()];
		name.getChars(0, name.length(), array, 0);
		array[0] = Character.toLowerCase(name.charAt(0));
		return new String(array);
	}

	protected String firstCharToUpperCase(String name) {
		char[] array = new char[name.length()];
		name.getChars(0, name.length(), array, 0);
		array[0] = Character.toUpperCase(name.charAt(0));
		return new String(array);
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

	/**
	 *
	 * @param tCount \t的数量
	 */
	protected void newline(StrBuilder builder, int tCount) {
		builder.append('\n');
		for (int i = 0; i < tCount; i++) {
			builder.append('\t');
		}
	}

	public String generateMessage(String messagePattern, Object... argArray) {
		FormattingTuple tuple = MessageFormatter.arrayFormat(messagePattern, argArray);
		return tuple.getMessage();
	}
}
