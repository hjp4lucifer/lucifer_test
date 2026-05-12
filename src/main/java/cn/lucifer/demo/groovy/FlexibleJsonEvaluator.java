package cn.lucifer.demo.groovy;

import com.jayway.jsonpath.JsonPath;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

/**
 * 基于 JsonPath + Groovy 的 JSON 条件表达式求值器。
 *
 * <p>工作流程：
 * <ol>
 *   <li>用正则从表达式中提取所有 JsonPath（如 $.a.b.c）</li>
 *   <li>通过 JsonPath 从 JSON 中读取实际值，绑定到 Groovy 变量（v0, v1, ...）</li>
 *   <li>将表达式中的路径替换为变量名，交由 GroovyShell 执行布尔求值</li>
 * </ol>
 *
 * <p>示例：
 * <pre>
 *   json       = {"a":{"b":{"c":"1", "d":"2"}}}
 *   expression = "$.a.b.c == '1' && $.a.b.d == '2'"
 *   结果: true
 * </pre>
 *
 * <p>支持 Groovy 方法调用：
 * <pre>
 *   expression = "$.a.b.list[0:].id.contains(2)"
 * </pre>
 */
public class FlexibleJsonEvaluator {

    /** 匹配 JsonPath 路径：以 $. 开头，支持属性、数组下标、切片、通配符等 */
    private static final Pattern PATH_PATTERN = Pattern.compile("\\$\\.[a-zA-Z0-9\\._\\[\\]\\:\\*]+");

    /**
     * 对 JSON 数据求值条件表达式。
     *
     * @param json       JSON 字符串
     * @param expression 条件表达式，可包含 JsonPath 路径和 Groovy 语法
     * @return 表达式求值结果；路径不存在或求值异常时返回 false
     */
    public static boolean evaluate(String json, String expression) {
        try {
            Binding binding = new Binding();

            // ---- 第一步：提取表达式中的 JsonPath 并读取值 ----
            Matcher matcher = PATH_PATTERN.matcher(expression);

            // 路径 → 变量名映射，按路径长度倒序排列
            // 原因：替换时先替换长路径，防止 $.a.b.c1 被 $.a.b.c 的替换误伤
            // 注意：比较器需在长度相同时以字典序区分，否则等长路径会被 TreeMap 视为同一个 key
            TreeMap<String, String> pathMap = new TreeMap<>((a, b) -> {
                int lenDiff = b.length() - a.length();
                return lenDiff != 0 ? lenDiff : a.compareTo(b);
            });

            while (matcher.find()) {
                String rawPath = matcher.group();
                int matchEnd = matcher.end();

                // 裁剪正则贪婪匹配带来的 Groovy 方法名（如 .contains、.any）
                // 判断逻辑：若匹配结束位置的下一个字符是 '('，说明末尾段是方法调用而非路径
                // 例如 "$.a.b.id.contains" 后面紧跟 "(2)"，需裁剪掉 ".contains" 得到 "$.a.b.id"
                String validPath = rawPath;
                int totalTrimmed = 0;
                while (validPath.length() > 2) {
                    int checkPos = matchEnd - totalTrimmed;
                    if (checkPos >= expression.length() || expression.charAt(checkPos) != '(') {
                        break;
                    }
                    int lastDot = validPath.lastIndexOf('.');
                    if (lastDot <= 1) {
                        validPath = null;
                        break;
                    }
                    totalTrimmed += validPath.length() - lastDot;
                    validPath = validPath.substring(0, lastDot);
                }

                // 用 JsonPath 读取值，路径不存在则整个表达式不可满足，直接返回 false
                if (validPath != null) {
                    try {
                        Object value = JsonPath.read(json, validPath);
                        if (!pathMap.containsKey(validPath)) {
                            String varName = "v" + pathMap.size();
                            binding.setVariable(varName, value);
                            pathMap.put(validPath, varName);
                        }
                    } catch (Exception e) {
                        // 路径在 JSON 中不存在，表达式无法求值
                        return false;
                    }
                }
            }

            // ---- 第二步：将表达式中的路径替换为变量名 ----
            // 例如 "$.a.b.c == '1'" → "v0 == '1'"
            String finalExpr = expression;
            for (String path : pathMap.keySet()) {
                finalExpr = finalExpr.replace(path, pathMap.get(path));
            }

            // ---- 第三步：Groovy 求值 ----
            GroovyShell shell = new GroovyShell(binding);
            return (Boolean) shell.evaluate(finalExpr);

        } catch (Exception e) {
            // 生产环境建议记录 Log
			e.printStackTrace();
            return false;
        }
    }


}