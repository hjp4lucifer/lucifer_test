package cn.lucifer.demo.sql;

import org.apache.commons.lang.text.StrBuilder;

import java.io.File;
import java.io.IOException;

/**
 * used HeidiSQL's SQL format
 *
 * @author Lucifer
 *
 */
public class TableSqlToModelV2 extends TableSqlToModel {

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


			builder.append("\n\t")
					.append(generateMessage("@EntityField(propertyName = \"{}\", columnName = \"{}\")", info.name,
							info.columnName));

			builder.append("\n\tprivate ").append(info.type).append(" ").append(info.name).append(";");
		}

		builder.append("\n}");
		return builder.toString();
	}


}
