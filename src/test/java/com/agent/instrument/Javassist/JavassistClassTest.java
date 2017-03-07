package com.agent.instrument.Javassist;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.junit.Test;

import com.gbs.agent.transformer.ClassFileTransformerDispatcher;

public class JavassistClassTest {

	@Test
	public void testJavassistClass(){
	}
	
	@Test
	public void testClass() throws CannotCompileException, NotFoundException, IOException{
		ClassPool pool = ClassPool.getDefault();
		CtClass cls = pool.makeClass("cn.ibm.com.TestClass");
		// 添加私有成员name及其getter、setter方法  
        CtField param = new CtField(pool.get("java.lang.String"), "name", cls);  
        param.setModifiers(Modifier.PRIVATE);  
        cls.addMethod(CtNewMethod.setter("setName", param));  
        cls.addMethod(CtNewMethod.getter("getName", param));  
        cls.addField(param, Initializer.constant(""));  
          
        // 添加无参的构造体  
        CtConstructor cons = new CtConstructor(new CtClass[] {}, cls);  
        cons.setBody("{name = \"Brant\";}");  
        cls.addConstructor(cons);  
          
        // 添加有参的构造体  
        cons = new CtConstructor(new CtClass[] {pool.get("java.lang.String")}, cls);  
        cons.setBody("{$0.name = $1;}");  
        cls.addConstructor(cons);  
          
        // 打印创建类的类名  
//        System.out.println( cls.toString());  
        
	}
}
