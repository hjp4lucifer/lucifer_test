package cn.lucifer.demo.groovy;

import org.junit.Test;

import static org.junit.Assert.*;

public class FlexibleJsonEvaluatorTest {

	@Test
	public void evaluate_1() {
		String json = "{\"a\":{\"b\":{\"c\":\"1\", \"d\":\"2\"}}}";

		// 含有大量不规则空格和逻辑符号
		String input = "$.a.b.c  ==  \"1\"    &&   $.a.b.d  ==  \"2\"";

		boolean result = FlexibleJsonEvaluator.evaluate(json, input);
		System.out.println("结果: " + result); // 输出 true
		assertTrue(result);
	}

	@Test
	public void evaluate_2() {
		String json = "{" +
				"  \"a\": {" +
				"    \"b\": {" +
				"      \"c\": \"1\"," +
				"      \"d\": \"2\"," +
				"      \"list\": [{\"id\":1}, {\"id\":2}]" +
				"    }" +
				"  }" +
				"}";

		// 测试复合逻辑
		String exp1 = "$.a.b.c == '1' && $.a.b.d == '2'";
		boolean result = FlexibleJsonEvaluator.evaluate(json, exp1);
		System.out.println("Result 1: " + result); // true
		assertTrue(result);

		// 测试切片与集合判断 (Groovy 的 list.id 会提取所有 id 组成新 list)
		// 甚至可以写 Groovy 的集合闭包：.any, .all
		String exp2 = "$.a.b.list[0:].id.contains(2) && $.a.b.c != '99'";
		result = FlexibleJsonEvaluator.evaluate(json, exp2);
		System.out.println("Result 2: " + result); // true
		assertTrue(result);

	}

	@Test
	public void evaluate_3() {
		String json = "{" +
				"  \"a\": {" +
				"    \"b\": {" +
				"      \"c\": \"1\"," +
				"      \"d\": \"2\"," +
				"      \"list\": [{\"id\":1}, {\"id\":2}]" +
				"    }" +
				"  }" +
				"}";

		String exp3 = "$.a.b.listA[0:].id.contains(2) && $.a.b.c != '99'";
		boolean result = FlexibleJsonEvaluator.evaluate(json, exp3);
		System.out.println("Result 3: " + result);
		assertFalse(result);
	}
}