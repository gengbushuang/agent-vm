package com.agent.instrument.Javassist;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.junit.Test;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentException;
import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.agent.instrument.Javassist.JavassistClass;
import com.gbs.agent.instrument.Javassist.JavassistClassPool;
import com.gbs.agent.instrument.Javassist.JavassistMethod;
import com.gbs.agent.interceptor.registry.DefaultInterceptorRegistryBinder;
import com.gbs.vm.TargetVM;

public class JavassistClassTest {

	@Test
	public void testJavassistClass() {
		String[] s = new String[] { "com.gbs.vm.TargetVM.test3","com.gbs.vm.TargetVM.test","com.gbs.vm.TargetVM.test1","com.gbs.vm.TargetVM.test2" };
		final Map<String, Set<String>> methods = new HashMap<String, Set<String>>();
		for (String fullQualifiedMethodName : s) {
			try {
				final String className = toClassName(fullQualifiedMethodName);
				final String methodName = toMethodName(fullQualifiedMethodName);
				Set<String> names = methods.get(className);
				if (names == null) {
					names = new HashSet<String>();
					methods.put(className, names);
				}
				names.add(methodName);
			} catch (Exception e) {

			}
		}
		for (Map.Entry<String, Set<String>> entry : methods.entrySet()) {
			try {
				parseMethods(entry.getKey(), entry.getValue());
			} catch (Exception e) {

			}
		}
	}

	void parseMethods(final String className, final Set<String> methodNames) throws NotFoundException, InstrumentException {
		DefaultInterceptorRegistryBinder interceptorRegistryBinder = new DefaultInterceptorRegistryBinder();
		interceptorRegistryBinder.bind();
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get(className);
		InstrumentClass instrumentClass = new JavassistClass(interceptorRegistryBinder,pool.getClassLoader(), ctClass);
		String interceptorClassName = "com.gbs.plugin.user.interceptor.UserIncludeMethodInterceptor";
		System.out.println(ctClass);
		// final String[] names = methodNames.toArray(new
		// String[methodNames.size()]);
		List<InstrumentMethod> declaredMethods = instrumentClass.getDeclaredMethods(methodNames);
		for (InstrumentMethod instrumentMethod : declaredMethods) {
			instrumentMethod.addInterceptor(interceptorClassName);
		}
		 try {
			Class c = ctClass.toClass();
			TargetVM h = (TargetVM) c.newInstance();
			h.test();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>");
			h.test();
		} catch (Exception e) {
			// TODO: handle exception
		}
		instrumentClass.toBytecode();
	}

	String toClassName(String fullQualifiedMethodName) {
		final int classEndPosition = fullQualifiedMethodName.lastIndexOf(".");
		if (classEndPosition <= 0) {
			throw new IllegalArgumentException("invalid full qualified method name(" + fullQualifiedMethodName + "). not found method");
		}

		return fullQualifiedMethodName.substring(0, classEndPosition);
	}

	String toMethodName(String fullQualifiedMethodName) {
		final int methodBeginPosition = fullQualifiedMethodName.lastIndexOf(".");
		if (methodBeginPosition <= 0 || methodBeginPosition + 1 >= fullQualifiedMethodName.length()) {
			throw new IllegalArgumentException("invalid full qualified method name(" + fullQualifiedMethodName + "). not found method");
		}

		return fullQualifiedMethodName.substring(methodBeginPosition + 1);
	}

	@Test
	public void testClass() throws CannotCompileException, NotFoundException, IOException {

	}
}
