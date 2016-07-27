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

		builder.append("\n\n@Repository\n");
		builder.append("public class ").append(className)
				.append("Dao extends BaseDao").append(" {");

		// getById
		generateGetById(builder);

		// rowMapper
		generateRowMapper(builder);

		// end class
		builder.append("\n}");
		return builder.toString();
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
			builder.append("\n\n\tpublic ").append(className)
					.append(" getById(").append(primaryKey.type)
					.append(" id) {");
			builder.append("\n\t\t");
			builder.append(String.format(
					"String sql = \"select * from %s where %s=\" + id;",
					tabelName, primaryKey.columnName));
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
			return "Timestamp";
		}
		return changeNameForClass(info.type);
	}
}
