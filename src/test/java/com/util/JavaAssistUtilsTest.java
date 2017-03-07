package com.util;

import org.junit.Test;

import com.gbs.util.JavaAssistUtils;

public class JavaAssistUtilsTest {

	@Test
	public void testJavaNameToJvmName(){
		String javaName = "com.util.JavaAssistUtilsTest";
		String jvmName = JavaAssistUtils.javaNameToJvmName(javaName);
		System.out.println(jvmName);
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println(classLoader);
	}
}
