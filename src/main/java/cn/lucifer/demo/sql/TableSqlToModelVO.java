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
public class TableSqlToModelVO extends TableSqlBase {

	public TableSqlToModelVO() {
		super();

		typeMap.put("DECIMAL", "String");
		typeMap.put("TIMESTAMP", "Long");
	}

	public String generate(File file) throws IOException {
		parseSql(file);

		StrBuilder builder = new StrBuilder();

		builder.append("import com.vip.osp.core.idl.annotation.doc;\n")
				.append("import com.vip.osp.core.idl.annotation.idl;\n")
				.append("import com.vip.osp.core.idl.annotation.label;\n")
				.append("import com.vip.osp.core.idl.annotation.method;\n")
				.append("import com.vip.osp.core.idl.annotation.namespace;\n")
				.append("import com.vip.osp.core.idl.annotation.optional;\n")
				.append("import com.vip.osp.core.idl.annotation.returnDoc;\n")
				.append("import com.vip.osp.core.idl.annotation.service;\n")
				.append("import com.vip.osp.core.idl.annotation.struct;\n")
				.append("import com.vip.osp.core.idl.annotation.tag;\n");

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@idl\n").append("@namespace(\"com.vip.order.common.pojo.order.vo\")\n");
		builder.append("public class ").append(className).append("VOIDL {");
		builder.append("\n\n\t@struct");

		builder.append(String.format("\n\t@label(\"%s\")", null == tableComment ? "xxxx" : tableComment));

		builder.append("\n\t\tpublic class ").append(className).append("VO {");

		int tagIndex = 1;
		for (FieldInfo info : fieldList) {
			builder.append(String.format("\n\t\t@tag(%d)", tagIndex++));
			if (null != info.comment) {
				if (info.isAutoIncrement) {
					generateComment(builder, info.comment + ", 自增");
				} else {
					generateComment(builder, info.comment);
				}
			} else if (info.isAutoIncrement) {
				generateComment(builder, "自增");
			}
			builder.append("\n\t\t").append(info.type).append(" ").append(info.name).append(";\n");
		}

		builder.append("\t}\n");
		builder.append("\n}");
		return builder.toString();
	}

	protected void generateComment(StrBuilder builder, String comment) {
		builder.append("\n\t\t@doc(\"").append(comment).append("\")");
	}

}
