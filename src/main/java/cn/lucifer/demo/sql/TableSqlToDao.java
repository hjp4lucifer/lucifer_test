package cn.lucifer.demo.sql;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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

		builder.append("\n\n@Repository\n");
		builder.append("public class ").append(className)
				.append("Dao extends BaseDao").append(" {");

		if (null != primaryKey
				&& ("long".equals(primaryKey.type) || "int"
						.equals(primaryKey.type))) {
			builder.append("\n\tpublic ").append(className).append(" getById(")
					.append(primaryKey.type).append(" id) {");
			builder.append("\n\t\t");
			builder.append(String.format(
					"String sql = \"select * from %s where %s=\" + id;",
					tabelName, primaryKey.columnName));
			builder.append("\n\t\tlog.debug(sql);");
			builder.append("\n\t\treturn getJdbcTemplate().queryForObject(sql, rowMapper);");

			builder.append("\n\t}");
		}

		// rowMapper
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
					changeNameForClass(info.type),
					changeNameForClass(info.columnName)));
		}
		builder.append("\n\t\t\treturn model;");
		builder.append("\n\t\t}");

		builder.append("\n\t}");

		// end class
		builder.append("\n}");
		return builder.toString();
	}
}
