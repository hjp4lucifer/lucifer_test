package cn.lucifer.demo.poi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class XlsxDemoTest {

	private Set<String> skip = Sets.newHashSet("21041407139168", "21041444256014",
			"21041489230754",
			"21041608506835",
			"21041608701525",
			"21041610257564",
			"21041620398664",
			"21041634572474",
			"21041653926014",
			"21041665353203",
			"21041682469924",
			"21041686212088"
	);

	@Test
	public void testRead() throws Exception {
		final String path = "C:\\tmp\\order\\aaa.xlsx";

		ArrayListMultimap<String, Row> couponMap = readCoupon();
		Map<String, Row> orderSn2RootMap = readOrderSn2Root();

		XSSFWorkbook book = new XSSFWorkbook(new File(path));
		XSSFSheet sheet = book.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		final int fileCount = 13;
		List<LinkedList<String>> sqlFileList = new ArrayList<>(8);
		for (int i = 0; i < fileCount; i++) {
			sqlFileList.add(new LinkedList<String>());
		}

		Row firstRow = rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String order_sn = row.getCell(1).getStringCellValue();
			if(skip.contains(order_sn)){
				continue;
			}

			String active_type = row.getCell(2).getStringCellValue();
			if (!"101".equals(active_type.trim())) {
				continue;
			}
			Row rootOrderSnRow = orderSn2RootMap.get(order_sn);
			String rootOrderSn = rootOrderSnRow.getCell(1).getStringCellValue();

			int dbIndex = Integer.parseInt(rootOrderSnRow.getCell(2).getStringCellValue()) % 128;

			List<Row> couponList = couponMap.get(rootOrderSn);
			if (null == couponList || couponList.isEmpty()) {
				System.out.println("orderSn:" + order_sn + " , rootOrderSn: " + rootOrderSn + " 缺少优惠信息");
				continue;
			}

			{
				// 删除的sql
				String sql = String.format("delete from vip_order_%s.order_active where order_sn ='%s' and active_type=101 and id=%s;\n", dbIndex, order_sn, row.getCell(0).getStringCellValue());
				sqlFileList.get(dbIndex / 10).add(sql);
			}

			//{"total":27.00,"goods_fav":{"6918664661363585176":27.0},"coupon_field":4,"coupon_list":[{"coupon_sn":"EG96V8TPM98BDX7"}]}
			if (order_sn.equals(rootOrderSn)) {
				// 没拆单
				ArrayListMultimap<String, Row> couponGroup = groupByCouponSn(couponList);
				for (String coupon_sn : couponGroup.keySet()) {
					List<Row> group = couponGroup.get(coupon_sn);
					JSONObject detailJson = new JSONObject();

					JSONObject coupon = new JSONObject();
					coupon.put("coupon_sn", coupon_sn);
					JSONArray coupon_list = new JSONArray();
					coupon_list.add(coupon);
					detailJson.put("coupon_list", coupon_list);

					Map<String, BigDecimal> goods_fav = new HashMap<>();
					for (Row r : group) {
						detailJson.put("total", new BigDecimal(r.getCell(3).getStringCellValue()));
						detailJson.put("coupon_field", Integer.parseInt(r.getCell(10).getStringCellValue()));

						goods_fav.put(r.getCell(5).getStringCellValue(), new BigDecimal(r.getCell(8).getStringCellValue()));
					}

					detailJson.put("goods_fav", goods_fav);
					//System.out.println(detailJson.toJSONString());

					// 组装sql
					StringBuilder sql = new StringBuilder(String.format("insert into vip_order_%s.order_active(", dbIndex));

					{
						Iterator<Cell> firstCellIterator = firstRow.cellIterator();
						firstCellIterator.next();// 不要id
						int i = 1;
						while (firstCellIterator.hasNext()) {
							Cell cell = firstCellIterator.next();
							if (1 == i) {
							} else {
								sql.append(',');
							}
							sql.append(cell.getStringCellValue());
							i++;
						}

					}
					sql.append(") values(");
					{
						Iterator<Cell> cellIterator = row.cellIterator();
						cellIterator.next();// 不要id
						int i = 1;
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if (14 == i) {
								break;
							}
							if (1 != i) {
								sql.append(',');
							}
							if (8 == i) {
								sql.append("'").append(detailJson.toJSONString()).append("'");
							} else if (4 == i) {
								sql.append("'").append(coupon_sn).append("'");
							} else if (6 == i || 12 == i) {
								sql.append("now()");
							} else {
								sql.append("'").append(cell.getStringCellValue()).append("'");
							}

							i++;
						}
					}
					sql.append(");\n");
//					sqlList.add(sql.toString());
					sqlFileList.get(dbIndex / 10).add(sql.toString());
				}

			} else {
				// 拆单
				String detail = row.getCell(8).getStringCellValue();
				 System.out.println("拆单 " + order_sn);
				JSONObject detailJson = JSON.parseObject(detail);


			}

			//break;
		}

		for (int i = 0; i < fileCount; i++) {
			File file =  new File(String.format("C:\\tmp\\order\\out_%s.sql", i));
			FileUtils.writeLines(file,"utf-8", sqlFileList.get(i));
		}

	}


	private ArrayListMultimap<String, Row> groupByCouponSn(List<Row> couponList) {
		ArrayListMultimap<String, Row> map = ArrayListMultimap.create();
		for (Row row : couponList) {
			map.put(row.getCell(1).getStringCellValue(), row);
		}
		return map;
	}

	private Map<String, Row> readOrderSn2Root() throws Exception {
		Map<String, Row> map = new HashMap<>();
		final String path = "C:\\tmp\\order\\rootOrderSnV2.xlsx";
		XSSFWorkbook book = new XSSFWorkbook(new File(path));
		XSSFSheet sheet = book.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		// 第一行忽略
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String order_sn = row.getCell(0).getStringCellValue();

			map.put(order_sn, row);
		}
		return map;
	}

	private ArrayListMultimap<String, Row> readCoupon() throws Exception {
		ArrayListMultimap<String, Row> map = ArrayListMultimap.create();
		final String path = "C:\\tmp\\order\\ccc.xlsx";
		XSSFWorkbook book = new XSSFWorkbook(new File(path));
		XSSFSheet sheet = book.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		// 第一行忽略
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			String order_sn = row.getCell(0).getStringCellValue();
			map.put(order_sn, row);
		}
		return map;
	}
}