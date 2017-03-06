package com.agent.instrument.Javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.junit.Test;

public class JavassistClassTest {

	@Test
	public void testJavassistClass(){
		String str = "com.agent.DefaultAgentOption";
		try {
			CtClass ctClass = ClassPool.getDefault().get(str);
			System.out.println(ctClass);
			CtMethod mold = ctClass.getDeclaredMethod("getAgentArgs");
			System.out.println(mold);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
}
