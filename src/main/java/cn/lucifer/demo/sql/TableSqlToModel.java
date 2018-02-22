package cn.lucifer.demo.sql;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.text.StrBuilder;

/**
 * used HeidiSQL's SQL format
 * 
 * @author Lucifer
 *
 */
public class TableSqlToModel extends TableSqlBase {

	public String generate(File file) throws IOException {
		parseSql(file);

		StrBuilder builder = new StrBuilder();

		builder.append("import java.util.Date;\n");
		builder.append("import java.math.BigDecimal;\n");

		builder.append("\n\n");
		
		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("public class ").append(className).append(" {");

		for (FieldInfo info : fieldList) {
			if (null != info.comment) {
				if (info.isAutoIncrement) {
					generateComment(builder, info.comment + ", 自增");
				} else {
					generateComment(builder, info.comment);
				}
			} else if (info.isAutoIncrement) {
				generateComment(builder, "自增");
			}
			builder.append("\n\tprotected ").append(info.type).append(" ")
					.append(info.name).append(";");
		}

		builder.append("\n}");
		return builder.toString();
	}

	protected void generateComment(StrBuilder builder, String comment) {
		builder.append("\n\t/**\n\t * ").append(comment).append("\n\t */");
	}

}
