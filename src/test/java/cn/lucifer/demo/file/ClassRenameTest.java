package cn.lucifer.demo.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class ClassRenameTest {
	ClassRename classRename;

	@Before
	public void setUp() throws Exception {
		String[] args = new String[] { "C:/test/class_rename/src",
				"com.jiujun.Showcase.AdManager",
				"com.jiujun.Showcase.BdBanager1" };
		classRename = new ClassRename(args);
	}

	@Test
	public void testGetSimpleName() {
		String str = classRename.getSimpleName("cn.lucfer.HTest");
		assertEquals("HTest", str);

		str = classRename.getSimpleName("HTest");
		assertEquals("HTest", str);
	}

	@Test
	public void testValidateClasses() {
		classRename.validateClasses(classRename.sourceFullName, true);
		classRename.validateClasses(classRename.targetFullName, false);

	}

	@Test
	public void testGetRegex() {
		String className = classRename.sourceFullName[0];
		String text1 = "adfdf" + className + "bderf";
		String text2 = "adfdf " + className + "bderf";
		String text3 = "adfdf " + className + "{bderf";
		String text4 = "adfdf " + className + "1{bderf";
		String text5 = "adfdf $" + className + "$1{bderf";
		Pattern regex = classRename.getRegex(className);
		System.out.println(regex.pattern());

		assertFalse(regex.matcher(text1).find());
		assertFalse(regex.matcher(text2).find());
		assertTrue(regex.matcher(text3).find());
		assertFalse(regex.matcher(text4).find());
		assertTrue(regex.matcher(text5).find());

		className = classRename.sourceSimpleName[0];
		regex = classRename.getRegex(className);
		System.out.println(regex.pattern());

		text1 = "adfdf" + className + "bderf";
		text2 = "adfdf " + className + "bderf";
		text3 = "adfdf " + className + "{bderf";
		text4 = "adfdf " + className + "1{bderf";
		text5 = "adfdf $" + className + "$1{bderf";

		assertFalse(regex.matcher(text1).find());
		assertFalse(regex.matcher(text2).find());
		assertTrue(regex.matcher(text3).find());
		assertFalse(regex.matcher(text4).find());
		assertTrue(regex.matcher(text5).find());
	}

	@Test
	public void testFixClassName() {
		String className = classRename.sourceFullName[0];
		String simpleName = classRename.sourceSimpleName[0];
		String targetClassName = classRename.targetFullName[0];
		String targetSimpleName = classRename.targetSimpleName[0];
		String text1 = "adfdf" + className + "bderf";
		String text2 = "adfdf " + className + "bderf";
		String text3 = "adfdf " + className + "{{{bderf";
		String text4 = "adfdf " + className + "1{bderf";
		String text5 = "adfdf $" + className + "$1{bderf";
		String text6 = "adfdf $" + className + " $1{bderf";
		String text7 = "adfdf $" + className + ".1{bderf";
		String text8 = "adfdf $" + className + "(bderf";
		String text9 = "adfdf $" + className + "\\bderf";
		String text10 = "adfdf " + className + " bderf";
		String text13 = "adfdf cn." + className + "{{{bderf " + className + "}";

		String text14 = text13 + "}\"" + className + "\"";
		text14 += text14;
		String text15 = "adfdf " + className.replace("Showcase", "Chowcase")
				+ " bderf";

		String text18 = text13 + text15 + " cc." + className + "  "
				+ simpleName;

		String testStr = text18;
		System.out.println(testStr + "  ----");

		try {
			String result = classRename.fixClassName(className,
					targetClassName, testStr);
			System.out.println(result);
			result = classRename.fixClassName(simpleName, targetSimpleName,
					testStr);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("return null");
		}

		className = classRename.sourceSimpleName[0];
		targetClassName = classRename.targetSimpleName[0];

		text1 = "adfdf" + className + "bderf";
		text2 = "adfdf " + className + "bderf";
		text3 = "adfdf " + className + "{bderf";
		text4 = "adfdf " + className + "1{bderf";
		text5 = "adfdf $" + className + "$1{bderf";

	}

	@Test
	public void testGetPackageName() {
		String fullName = "cn.lucifer.O2b";
		System.out.println(classRename.getPackageName(fullName));
	}

	@Test
	public void testPackageNameRegex() {
		String packageName = "cn.lucifer.demo.file";
		String pageLine = " package " + packageName + "";
		String regex = "^(|[ ]+)package\\W"
				+ packageName.replaceAll("\\.", "\\\\.") + "(\\W|$)";
		System.out.println("regex : " + regex);
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(pageLine);
		System.out.println(matcher.find());
	}

	@Test
	public void testCheckSamePackage() throws IOException {
		// String packageName = "package cn.lucifer.demo.file";
		String sourceFullName = "com.jiujun.Showcase.AdManager";
		String pageLine = "package com.jiujun.Showcase;";
		List<String> lines = FileUtils.readLines(new File(
				"C:/test/class_rename/src/com/jiujun/Showcase/AdManager.java"));
		lines.add(pageLine);

		System.out.println(classRename.checkSamePackage(lines, sourceFullName));
	}

	@Test
	public void testFixJavaFile() throws IOException {
		File src = new File(
				"C:/test/class_rename/src/com/jiujun/Showcase/AdManager.java");
		File target = new File(
				"C:/test/class_rename/src/com/jiujun/Showcase/BdBanager1.java");
		classRename.fixJavaFile(src, target, 0);
	}
}
