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
public class TableSqlToDao extends TableSqlBase {

	public String generate(File file) throws IOException {
		parseSql(file);

		StrBuilder builder = new StrBuilder();
		builder.append("import org.springframework.stereotype.Repository;\n");
		builder.append("import org.springframework.jdbc.core.RowMapper;\n");
		builder.append("import java.sql.ResultSet;\n");
		builder.append("import java.sql.SQLException;\n");
		builder.append("import java.util.Date;\n");
		builder.append("import java.math.BigDecimal;\n");

		// BLOB
		if (checkFieldType("BLOB")) {
			builder.append("\n");
			builder.append("import org.apache.commons.io.IOUtils;\n");
			builder.append("import java.io.IOException;\n");
			builder.append("import java.io.ByteArrayOutputStream;\n");
			builder.append("import java.io.InputStream;\n");
			builder.append("import java.sql.Blob;\n");
		}

		builder.append("\n\n");
		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Repository\n");
		builder.append("public class ").append(className).append("Dao extends BaseDao").append(" {");

		// add
		generateAdd(builder);

		// update
		generateUpdate(builder);

		// getById
		generateGetById(builder);

		// rowMapper
		generateRowMapper(builder);

		// end class
		builder.append("\n}");
		return builder.toString();
	}

	/**
	 * add
	 * 
	 * @param builder
	 */
	protected void generateAdd(StrBuilder builder) {
		String modelParam = changeNameForField(className);
		builder.append("\n\n\tpublic int add(").append(className).append(" ").append(modelParam).append(") {");
		builder.append("\n\t\t");
		builder.append("String sql = \"insert into ").append(tabelName).append("(");
		StrBuilder columnNames = new StrBuilder();
		StrBuilder values = new StrBuilder();
		StrBuilder getter = new StrBuilder();
		boolean isNotFirst = false;
		for (FieldInfo info : fieldList) {
			if (info.isAutoIncrement) {
				continue;
			}
			if (info.defaultType == 2) {
				continue;
			}
			if (isNotFirst) {
				columnNames.append(",");
				values.append(",");
			} else {
				isNotFirst = true;
			}
			columnNames.append(info.columnName);
			values.append("?");
			getter.append(", ").append(modelParam).append(".get").append(changeNameForClass(info.name)).append("()");
		}

		builder.append(columnNames).append(") values(").append(values).append(")\";");

		builder.append("\n\t\tlog.debug(sql);");
		builder.append("\n\t\treturn getJdbcTemplate().update(sql").append(getter).append(");");

		builder.append("\n\t}");
	}

	/**
	 * update
	 * 
	 * @param builder
	 */
	protected void generateUpdate(StrBuilder builder) {
		if (null == primaryKey) {
			return;
		}
		String modelParam = changeNameForField(className);
		builder.append("\n\n\tpublic int update(").append(className).append(" ").append(modelParam).append(") {");
		builder.append("\n\t\t");
		builder.append("String sql = \"update ").append(tabelName).append(" set ");

		StrBuilder getter = new StrBuilder();
		boolean isNotFirst = false;
		for (FieldInfo info : fieldList) {
			if (info.isPrimaryKey) {
				continue;
			}
			if (info.defaultType == 2) {
				continue;
			}
			if (isNotFirst) {
				builder.append(",");
			} else {
				isNotFirst = true;
			}
			builder.append(info.columnName).append("=").append("?");
			getter.append(", ").append(modelParam).append(".get").append(changeNameForClass(info.name)).append("()");
		}

		builder.append(" where ").append(primaryKey.columnName).append("=?");
		getter.append(", ").append(modelParam).append(".get").append(changeNameForClass(primaryKey.name)).append("()");
		builder.append("\";");

		builder.append("\n\t\tlog.debug(sql);");
		builder.append("\n\t\treturn getJdbcTemplate().update(sql").append(getter).append(");");

		builder.append("\n\t}");
	}

	/**
	 * getById
	 * 
	 * @param builder
	 */
	protected void generateGetById(StrBuilder builder) {
		if (null != primaryKey && ("long".equals(primaryKey.type) || "int".equals(primaryKey.type))) {
			String idFieldName = changeNameForField(primaryKey.columnName);
			builder.append("\n\n\tpublic ").append(className).append(" getBy").append(changeNameForClass(idFieldName))
					.append("(").append(primaryKey.type).append(" ").append(idFieldName).append(") {");
			builder.append("\n\t\t");
			builder.append(String.format("String sql = \"select * from %s where %s=\" + %s;", tabelName,
					primaryKey.columnName, idFieldName));
			builder.append("\n\t\tlog.debug(sql);");
			builder.append("\n\t\treturn getJdbcTemplate().queryForObject(sql, rowMapper);");

			builder.append("\n\t}");
		}
	}

	/**
	 * rowMapper
	 * 
	 * @param builder
	 */
	protected void generateRowMapper(StrBuilder builder) {
		int tCount = 1;
		builder.append("\n\n\t");
		builder.append(
				String.format("protected RowMapper<%s> rowMapper = new RowMapper<%s>() {", className, className));
		builder.append("\n\n\t\t@Override");

		tCount++;
		newline(builder, tCount);
		// builder.append("\n\t\t");
		builder.append(String.format("public %s mapRow(ResultSet rs, int rowNum) throws SQLException {", className));
		tCount++;
		// builder.append("\n\t\t\t");
		newline(builder, tCount);
		builder.append(String.format("%s model = new %s();", className, className));
		for (FieldInfo info : fieldList) {
			// builder.append("\n\t\t\t");
			newline(builder, tCount);
			if (info.columnType.equals("BLOB")) {
				builder.append("{");
				tCount++;

				newline(builder, tCount);
				builder.append(String.format("Blob blob = rs.getBlob(\"%s\");", info.columnName));
				newline(builder, tCount);
				builder.append("InputStream input = blob.getBinaryStream();");
				newline(builder, tCount);
				builder.append("ByteArrayOutputStream output = new ByteArrayOutputStream();");
				newline(builder, tCount);
				builder.append("try {");

				tCount++;
				newline(builder, tCount);
				builder.append("IOUtils.copy(input, output);");
				newline(builder, tCount);
				builder.append("byte[] buf = output.toByteArray();");
				newline(builder, tCount);
				builder.append(String.format("model.set%s(buf);", changeNameForClass(info.name)));

				tCount--;
				newline(builder, tCount);
				builder.append("} catch (IOException e) {");
				newline(builder, tCount);
				builder.append("} finally {");
				newline(builder, tCount + 1);
				builder.append("IOUtils.closeQuietly(input);");
				newline(builder, tCount);
				builder.append("}");

				tCount--;
				newline(builder, tCount);
				builder.append("}");
				continue;
			}
			builder.append(String.format("model.set%s(rs.get%s(\"%s\"));", changeNameForClass(info.name),
					getNameInResultSetByType(info), info.columnName));
		}

		newline(builder, tCount);
		builder.append("return model;");
		tCount--;
		newline(builder, tCount);
		builder.append("}");

		tCount--;
		newline(builder, tCount);
		builder.append("};");
	}

	public String getNameInResultSetByType(FieldInfo info) {
		if ("Date".equals(info.type)) {
			if ("TIMESTAMP".equals(info.columnType)) {
				return "Timestamp";
			}
		}
		return changeNameForClass(info.type);
	}
}
