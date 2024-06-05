package cn.lucifer.demo.sql;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

import cn.lucifer.util.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import cn.lucifer.demo.sql.TableSqlBase.FieldInfo;

/**
 * used HeidiSQL's SQL format
 * 
 * @author Lucifer
 *
 */
public class TableSqlToMapper extends TableSqlBase {

	private final String name_BaseResultMap = "BaseResultMap";
	private final String name_Base_Column_List = "Base_Column_List";

	protected Stack<String> stack = new Stack<>();
	protected LinkedList<String> lineList = new LinkedList<>();

	public String generate(File file) throws IOException {
		parseSql(file);

		lineList.add("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		lineList.add(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
		lineList.add(String.format("<mapper namespace=\"xxx.%sDao\">", className));
		stack.push("</mapper>");

		generateResultMap();
		generateBaseColumnList();

		lineList.add("");
		generateAdd();

		lineList.add("");
		generateUpdateById();
		lineList.add("");
		generateUpdateByCondition();
		lineList.add("");
		generateSelectById();

		popStack2LineList();

		return StringUtils.join(lineList, '\n');
	}

	protected void generateBaseColumnList() {
		int tCount = 1;
		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append("<sql id=\"").append(name_Base_Column_List).append("\">");
		tCount++;
		int index = 0;
		for (FieldInfo info : fieldList) {
			if (index % 6 == 0) {
				newline(builder, tCount);
			}
			builder.append(info.columnName);
			if (index < fieldList.size() - 1) {
				builder.append(", ");
			}
			index++;
		}
		tCount--;

		newline(builder, tCount);
		builder.append("</sql>");
		lineList.add(builder.toString());
	}

	protected void generateResultMap() {
		int tCount = 1;

		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append("<resultMap id=\"").append(name_BaseResultMap).append("\" type=\"xxx.").append(className)
				.append("\">");

		tCount++;
		for (FieldInfo info : fieldList) {
			newline(builder, tCount);
			if (info.isPrimaryKey) {
				builder.append("<id ");
			} else {
				builder.append("<result ");
			}
			builder.append("column=\"").append(info.columnName).append("\" jdbcType=\"").append(getColumnType(info))
					.append("\" property=\"").append(info.name).append("\"/>");
		}
		tCount--;

		newline(builder, tCount);
		builder.append("</resultMap>");
		lineList.add(builder.toString());
	}

	/**
	 * 处理ibatis对数据库命名不符合的问题
	 * 
	 * @param info
	 * @return
	 */
	private String getColumnType(FieldInfo info) {
		if ("INT".equalsIgnoreCase(info.columnType)) {
			return "INTEGER";
		}
		return info.columnType;
	}

	protected void generateSelectById() {
		int tCount = 1;

		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append("<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\">");

		tCount++;
		newline(builder, tCount);
		builder.append("select");
		newline(builder, tCount);
		builder.append("<include refid=\"Base_Column_List\" />");
		newline(builder, tCount);
		builder.append("from ").append(tableName);
		newline(builder, tCount);
		builder.append("where ").append(primaryKey.columnName).append("=#{").append(primaryKey.name).append("}");
		tCount--;

		newline(builder, tCount);
		builder.append("</select>");
		lineList.add(builder.toString());
	}

	protected void generateUpdateById() {
		int tCount = 1;

		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append(StrUtils.generateMessage(
				"<update id=\"updateByPrimaryKeySelective\" parameterType=\"xxx.{}\" >", className));

		tCount++;
		newline(builder, tCount);
		builder.append("update ").append(tableName);

		// 处理columnName
		newline(builder, tCount);
		builder.append("<set>");
		tCount++;
		for (FieldInfo info : fieldList) {
			if (info.isAutoIncrement) {
				continue;
			}

			int subStackCount = 0;
			// 首字母是大写
			if (info.type.charAt(0) == Character.toUpperCase(info.type.charAt(0))) {
				newline(builder, tCount);
				builder.append("<if test=\"").append(info.name).append(" != null\">");
				subStackCount = pushStack("</if>", subStackCount);
			}
			newline(builder, tCount + 1);
			builder.append(info.columnName).append("=#{").append(info.name).append("},");

			subStackCount = checkSubStackCount(tCount, builder, subStackCount);
		}
		tCount--;
		newline(builder, tCount);
		builder.append("</set>");
		newline(builder, tCount);
		builder.append("where ").append(primaryKey.columnName).append("=#{").append(primaryKey.name).append("}");
		tCount--;

		newline(builder, tCount);
		builder.append("</update>");
		lineList.add(builder.toString());
	}

	protected void generateUpdateByCondition() {
		int tCount = 1;

		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append(StrUtils.generateMessage(
				"<update id=\"updateByCondition\" >", className));

		tCount++;
		newline(builder, tCount);
		builder.append("update ").append(tableName);

		// 处理columnName
		newline(builder, tCount);
		builder.append("<set>");
		tCount++;
		for (FieldInfo info : fieldList) {
			if (info.isAutoIncrement) {
				continue;
			}

			int subStackCount = 0;
			// 首字母是大写
			if (info.type.charAt(0) == Character.toUpperCase(info.type.charAt(0))) {
				newline(builder, tCount);
				builder.append("<if test=\"updateValue.").append(info.name).append(" != null\">");
				subStackCount = pushStack("</if>", subStackCount);
			}
			newline(builder, tCount + 1);
			builder.append(info.columnName).append("=#{updateValue.").append(info.name).append("},");

			subStackCount = checkSubStackCount(tCount, builder, subStackCount);
		}
		tCount--;
		newline(builder, tCount);
		builder.append("</set>");
		newline(builder, tCount);
		builder.append("<where> 1");
		tCount++;

		for (FieldInfo info : fieldList) {
			int subStackCount = 0;
			// 首字母是大写
			if (info.type.charAt(0) == Character.toUpperCase(info.type.charAt(0))) {
				newline(builder, tCount);
				builder.append("<if test=\"condition.").append(info.name).append(" != null\">");
				subStackCount = pushStack("</if>", subStackCount);
			}

			newline(builder, tCount + 1);
			builder.append("and ").append(info.columnName).append("=#{condition.").append(info.name).append("}");

			subStackCount = checkSubStackCount(tCount, builder, subStackCount);
		}

		tCount--;
		newline(builder, tCount);
		builder.append("</where>");

		tCount--;
		newline(builder, tCount);
		builder.append("</update>");
		lineList.add(builder.toString());
	}

	protected void generateAdd() {
		int tCount = 1;

		StrBuilder builder = new StrBuilder();
		builder.append("\t");
		builder.append("<insert id=\"insertSelective\" useGeneratedKeys=\"true\"  keyProperty=\"")
				.append(primaryKey.name).append("\" parameterType=\"xxx.").append(className).append("\" >");

		tCount++;
		newline(builder, tCount);
		builder.append("insert into ").append(tableName);

		// 处理columnName
		newline(builder, tCount);
		builder.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		tCount++;
		for (FieldInfo info : fieldList) {
			if (info.isAutoIncrement) {
				continue;
			}
			if (info.defaultType == 2) {
				continue;
			}

			int subStackCount = 0;
			// 首字母是大写
			if (info.type.charAt(0) == Character.toUpperCase(info.type.charAt(0))) {
				newline(builder, tCount);
				builder.append("<if test=\"").append(info.name).append(" != null\">");
				subStackCount = pushStack("</if>", subStackCount);
			}
			newline(builder, tCount + 1);
			builder.append("`").append(info.columnName).append("`,");

			subStackCount = checkSubStackCount(tCount, builder, subStackCount);
		}
		tCount--;
		newline(builder, tCount);
		builder.append("</trim>");

		// 处理字段
		newline(builder, tCount);
		builder.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");

		tCount++;
		for (FieldInfo info : fieldList) {
			if (info.isAutoIncrement) {
				continue;
			}
			if (info.defaultType == 2) {
				continue;
			}

			int subStackCount = 0;
			// 首字母是大写
			if (info.type.charAt(0) == Character.toUpperCase(info.type.charAt(0))) {
				newline(builder, tCount);
				builder.append("<if test=\"").append(info.name).append(" != null\">");
				subStackCount = pushStack("</if>", subStackCount);
			}

			newline(builder, tCount + 1);
			builder.append("#{").append(info.name).append("},");

			subStackCount = checkSubStackCount(tCount, builder, subStackCount);
		}

		tCount--;
		newline(builder, tCount);
		builder.append("</trim>");
		tCount--;

		newline(builder, tCount);
		builder.append("</insert>");
		lineList.add(builder.toString());
	}

	private int checkSubStackCount(int tCount, StrBuilder builder, int subStackCount) {
		// 检查栈
		while (subStackCount > 0) {
			newline(builder, tCount);
			builder.append(stack.pop());
			subStackCount--;
		}
		return subStackCount;
	}

	protected int pushStack(String line, int stackCount) {
		stack.push(line);
		return stackCount + 1;
	}

	protected void popStack2LineList() {
		while (!stack.empty()) {
			lineList.add(stack.pop());
		}
	}
}
