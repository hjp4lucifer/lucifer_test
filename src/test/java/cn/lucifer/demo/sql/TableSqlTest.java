package cn.lucifer.demo.sql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TableSqlTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTableSqlToModel() throws IOException {
		TableSqlToModel tableSqlToModel = new TableSqlToModel();
		String str = tableSqlToModel.generate(new File(
				"resource/sql/createTable.sql"));
		System.out.println(str);
	}

}
