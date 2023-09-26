package cn.lucifer.demo.sql;

import org.apache.commons.lang3.text.StrBuilder;

import java.io.File;
import java.io.IOException;

/**
 * used HeidiSQL's SQL format
 *
 * @author Lucifer
 *
 */
public class TableSqlToComponent extends TableSqlToWrapsModel {

	public String generate(File file) throws IOException {
		parseSql(file);


		StrBuilder builder = new StrBuilder();

		/**
		 * xxxDao
		 */
		generateDao(builder);

		/**
		 * xxxCommonComponent
		 */
		generateCommonComponent(builder);

		/**
		 * QueryXxxComponentIface
		 */
		generateQueryComponentIface(builder);

		/**
		 * xxxSlaveComponent
		 */
		generateSlaveComponent(builder);

		/**
		 * xxxComponentIface
		 */
		generateComponentIface(builder);

		/**
		 * xxxMasterComponent
		 */
		generateMasterComponent(builder);

		return builder.toString();
	}

	/**
	 * xxxMasterComponent
	 */
	private void generateMasterComponent(StrBuilder builder) {
		final String componentClassName = getCommonComponentClassName();
		final String commonComponentParamName = firstCharToLowerCase(componentClassName);

		final String classParam = firstCharToLowerCase(className);

		builder.append("import com.vip.venus.data.common.annotation.ReadWrite;\n");
		builder.append("import com.vip.venus.data.common.annotation.RepositorySharding;\n");
		builder.append("import com.vip.venus.data.common.util.ReadWriteType;\n");
		builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		builder.append("import org.springframework.stereotype.Service;\n");

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Service\n");
		builder.append("public class ").append(className).append("MasterComponent implements ")
				.append(getComponentIfaceName()).append("{\n\n");

		builder.append("\t@Autowired\n");
		builder.append("\tprivate ").append(componentClassName).append(" ").append(commonComponentParamName)
				.append(";\n\n");

		// queryById
		String queryByPkMethodName = "queryBy" + firstCharToUpperCase(primaryKey.name);
		builder.append("\t@Override\n");
		builder.append("\t@ReadWrite(type = ReadWriteType.WRITE)\n");
		builder.append(
				"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#userId\")\n");
		builder.append("\tpublic ").append(className).append(" ").append(queryByPkMethodName).append("(")
				.append(primaryKey.type).append(" ").append(primaryKey.name).append(", Long userId){\n");
		builder.append("\t\treturn ").append(commonComponentParamName).append(".").append(queryByPkMethodName)
				.append("(").append(primaryKey.name).append(");\n");
		builder.append("\t}\n\n");

		// createXXX
		builder.append("\t@Override\n");
		builder.append("\t@ReadWrite(type = ReadWriteType.WRITE)\n");
		builder.append(
				"\t@Transactional(value = TransactionManagerConstants.VIP_ORDER_AFTERSALE_SHARD, rollbackFor = { Exception.class })\n");
		if (hasUserId) {
			builder.append(
					"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#")
					.append(classParam).append(".userId\")\n");
			builder.append("\tpublic int create").append(className).append("(").append(className).append(" ")
					.append(classParam).append("){\n");
		} else {
			builder.append(
					"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#userId\")\n");
			builder.append("\tpublic int create").append(className).append("(").append(className).append(" ")
					.append(classParam).append(", Long userId){\n");
		}
		builder.append("\t\treturn ").append(commonComponentParamName).append(".create").append(className).append("(")
				.append(classParam).append(");\n");
		builder.append("\t}\n\n");

		// updateXXX
		builder.append("\t@Override\n");
		builder.append("\t@ReadWrite(type = ReadWriteType.WRITE)\n");
		builder.append(
				"\t@Transactional(value = TransactionManagerConstants.VIP_ORDER_AFTERSALE_SHARD, rollbackFor = { Exception.class })\n");
		if (hasUserId) {
			builder.append(
					"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#")
					.append(classParam).append(".userId\")\n");
			builder.append("\tpublic int update").append(className).append("(").append(className).append(" ")
					.append(classParam).append("){\n");
		} else {
			builder.append(
					"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#userId\")\n");
			builder.append("\tpublic int update").append(className).append("(").append(className).append(" ")
					.append(classParam).append(", Long userId){\n");
		}
		builder.append("\t\treturn ").append(commonComponentParamName).append(".update").append(className).append("(")
				.append(classParam).append(");\n");
		builder.append("\t}\n\n");

		builder.append("\n}\n\n\n\n");
	}


	/**
	 * xxxComponentIface
	 */
	private void generateComponentIface(StrBuilder builder) {
		final String classParam = firstCharToLowerCase(className);

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("public interface ").append(getComponentIfaceName()).append(" extends ")
				.append(getQueryComponentIfaceName()).append(" {\n\n");


		// createXXX
		if (hasUserId) {
			builder.append("\tint create").append(className).append("(").append(className).append(" ")
					.append(classParam).append(");\n\n");
		} else {
			builder.append("\tint create").append(className).append("(").append(className).append(" ")
					.append(classParam).append(", Long userId);\n\n");
		}

		// updateXXX
		if (hasUserId) {
			builder.append("\tint update").append(className).append("(").append(className).append(" ")
					.append(classParam).append(");\n\n");
		} else {
			builder.append("\tint update").append(className).append("(").append(className).append(" ")
					.append(classParam).append(", Long userId);\n\n");
		}

		builder.append("\n}\n\n\n\n");
	}

	private String getComponentIfaceName() {
		return className + "ComponentIface";
	}


	/**
	 * xxxSlaveComponent
	 */
	private void generateSlaveComponent(StrBuilder builder) {
		final String componentClassName = getCommonComponentClassName();
		final String commonComponentParamName = firstCharToLowerCase(componentClassName);

		builder.append("import com.vip.venus.data.common.annotation.ReadWrite;\n");
		builder.append("import com.vip.venus.data.common.annotation.RepositorySharding;\n");
		builder.append("import com.vip.venus.data.common.util.ReadWriteType;\n");
		builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		builder.append("import org.springframework.stereotype.Service;\n");

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Service\n");
		builder.append("public class ").append(className).append("SlaveComponent implements ")
				.append(getQueryComponentIfaceName()).append("{\n\n");

		builder.append("\t@Autowired\n");
		builder.append("\tprivate ").append(componentClassName).append(" ").append(commonComponentParamName)
				.append(";\n\n");

		String queryByPkMethodName = "queryBy" + firstCharToUpperCase(primaryKey.name);
		builder.append("\t@Override\n");
		builder.append("\t@ReadWrite(type = ReadWriteType.READ)\n");
		builder.append(
				"\t@RepositorySharding(strategy = AfterSaleUserIdShardingStrategy.SHARDING_STRATEGY, key = \"#userId\")\n");
		builder.append("\tpublic ").append(className).append(" ").append(queryByPkMethodName).append("(")
				.append(primaryKey.type).append(" ").append(primaryKey.name).append(", Long userId){\n");
		builder.append("\t\treturn ").append(commonComponentParamName).append(".").append(queryByPkMethodName)
				.append("(").append(primaryKey.name).append(");\n");
		builder.append("\t}\n\n");
		builder.append("\n}\n\n\n\n");
	}

	/**
	 * QueryXxxComponentIface
	 */
	private void generateQueryComponentIface(StrBuilder builder) {

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("public interface ").append(getQueryComponentIfaceName()).append(" {\n\n");


		String queryByPkMethodName = "queryBy" + firstCharToUpperCase(primaryKey.name);
		builder.append("\t").append(className).append(" ").append(queryByPkMethodName).append("(")
				.append(primaryKey.type).append(" ").append(primaryKey.name).append(", Long userId);\n");
		builder.append("\n}\n\n\n\n");

	}

	private String getQueryComponentIfaceName() {
		return "Query" + className + "ComponentIface";
	}

	/**
	 * xxxCommonComponent
	 */
	private void generateCommonComponent(StrBuilder builder) {
		final String classParam = firstCharToLowerCase(className);
		final String daoClassName = className + "Dao";
		final String daoParamName = firstCharToLowerCase(daoClassName);

		builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		builder.append("import org.springframework.stereotype.Service;\n");

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Service\n");
		builder.append("public class ").append(getCommonComponentClassName()).append(" {\n\n");

		builder.append("\t@Autowired\n");
		builder.append("\tprivate ").append(daoClassName).append(" ").append(daoParamName).append(";\n\n");

		// queryById
		builder.append("\tpublic ").append(className).append(" queryBy").append(firstCharToUpperCase(primaryKey.name))
				.append("(").append(primaryKey.type).append(" ").append(primaryKey.name).append("){\n");
		builder.append("\t\treturn ").append(daoParamName).append(".selectByPrimaryKey(").append(primaryKey.name)
				.append(");\n");
		builder.append("\t}\n\n");

		// createXXX
		builder.append("\tpublic int create").append(className).append("(").append(className).append(" ")
				.append(classParam).append("){\n");
		builder.append("\t\treturn ").append(daoParamName).append(".insertSelective(").append(classParam)
				.append(");\n");
		builder.append("\t}\n\n");

		// updateXXX
		builder.append("\tpublic int update").append(className).append("(").append(className).append(" ")
				.append(classParam).append("){\n");
		builder.append("\t\treturn ").append(daoParamName).append(".updateByPrimaryKeySelective(").append(classParam)
				.append(");\n");
		builder.append("\t}\n\n");

		builder.append("\n}\n\n\n\n");

	}

	private String getCommonComponentClassName() {
		return className + "CommonComponent";
	}

	/**
	 * xxxDao
	 */
	private void generateDao(StrBuilder builder) {
		builder.append("import org.springframework.stereotype.Repository;\n");

		builder.append("\n\n");

		if (null != tableComment) {
			builder.append("/**\n * ").append(tableComment).append("\n */\n");
		}
		builder.append("@Repository\n");
		builder.append("public interface ").append(className).append("Dao {\n\n");

		String classParam = firstCharToLowerCase(className);
		builder.append("\tint insertSelective(").append(className).append(" ").append(classParam).append(");\n\n");
		builder.append("\tint updateByPrimaryKeySelective(").append(className).append(" ").append(classParam)
				.append(");\n\n");
		builder.append("\t").append(className).append(" selectByPrimaryKey(").append(primaryKey.type).append(" ")
				.append(primaryKey.name).append(");\n\n");


		builder.append("\n}\n\n\n\n");
	}

}
