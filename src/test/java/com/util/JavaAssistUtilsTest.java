package com.util;

import org.junit.Test;

public class JavaAssistUtilsTest {

	@Test
	public void testJavaNameToJvmName(){
		String javaName = "com.util.JavaAssistUtilsTest";
		String jvmName = JavaAssistUtils.javaNameToJvmName(javaName);
		System.out.println(jvmName);
	}
}
