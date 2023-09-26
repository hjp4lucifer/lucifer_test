package cn.lucifer.demo.sql;

import java.util.HashMap;

public class TableSqlToWrapsModel extends TableSqlBase {
	public TableSqlToWrapsModel() {
		typeMap.put("BIGINT", "Long");
		typeMap.put("INT", "Integer");
		typeMap.put("MEDIUMINT", "Integer");
		typeMap.put("TINYINT", "Integer");
		typeMap.put("FLOAT", "Float");
		typeMap.put("DOUBLE", "Double");
		typeMap.put("SMALLINT", "Integer");
	}


}
