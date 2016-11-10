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

		builder.append("\n\n");
		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Repository\n");
		builder.append("public class ").append(className)
				.append("Dao extends BaseDao").append(" {");

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
		builder.append("\n\n\tpublic int add(").append(className).append(" ")
				.append(modelParam).append(") {");
		builder.append("\n\t\t");
		builder.append("String sql = \"insert into ").append(tabelName)
				.append("(");
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
			getter.append(", ").append(modelParam).append(".get")
					.append(changeNameForClass(info.name)).append("()");
		}

		builder.append(columnNames).append(") values(").append(values)
				.append(")\";");

		builder.append("\n\t\tlog.debug(sql);");
		builder.append("\n\t\treturn getJdbcTemplate().update(sql")
				.append(getter).append(");");

		builder.append("\n\t}");
	}

	/**
	 * update
	 * 
	 * @param builder
	 */
	protected void generateUpdate(StrBuilder builder) {
		String modelParam = changeNameForField(className);
		builder.append("\n\n\tpublic int update(").append(className)
				.append(" ").append(modelParam).append(") {");
		builder.append("\n\t\t");
		builder.append("String sql = \"update ").append(tabelName)
				.append(" set ");

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
			getter.append(", ").append(modelParam).append(".get")
					.append(changeNameForClass(info.name)).append("()");
		}

		builder.append(" where ").append(primaryKey.columnName).append("=?");
		getter.append(", ").append(modelParam).append(".get")
				.append(changeNameForClass(primaryKey.name)).append("()");
		builder.append("\";");

		builder.append("\n\t\tlog.debug(sql);");
		builder.append("\n\t\treturn getJdbcTemplate().update(sql")
				.append(getter).append(");");

		builder.append("\n\t}");
	}

	/**
	 * getById
	 * 
	 * @param builder
	 */
	protected void generateGetById(StrBuilder builder) {
		if (null != primaryKey
				&& ("long".equals(primaryKey.type) || "int"
						.equals(primaryKey.type))) {
			String idFieldName = changeNameForField(primaryKey.columnName);
			builder.append("\n\n\tpublic ").append(className).append(" getBy")
					.append(changeNameForClass(idFieldName)).append("(")
					.append(primaryKey.type).append(" ").append(idFieldName)
					.append(") {");
			builder.append("\n\t\t");
			builder.append(String.format(
					"String sql = \"select * from %s where %s=\" + %s;",
					tabelName, primaryKey.columnName, idFieldName));
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
		builder.append("\n\n\t");
		builder.append(String.format(
				"protected RowMapper<%s> rowMapper = new RowMapper<%s>() {",
				className, className));
		builder.append("\n\n\t\t@Override");
		builder.append("\n\t\t");
		builder.append(String
				.format("public %s mapRow(ResultSet rs, int rowNum) throws SQLException {",
						className));
		builder.append("\n\t\t\t");
		builder.append(String.format("%s model = new %s();", className,
				className));
		for (FieldInfo info : fieldList) {
			builder.append("\n\t\t\t");
			builder.append(String.format("model.set%s(rs.get%s(\"%s\"));",
					changeNameForClass(info.name),
					getNameInResultSetByType(info), info.columnName));
		}
		builder.append("\n\t\t\treturn model;");
		builder.append("\n\t\t}");

		builder.append("\n\t};");
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
