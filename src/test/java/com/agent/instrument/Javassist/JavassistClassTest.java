package com.agent.instrument.Javassist;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.junit.Test;

import com.gbs.agent.instrument.Javassist.JavassistClass;
import com.gbs.agent.instrument.Javassist.JavassistMethod;
import com.gbs.agent.interceptor.AroundInterceptor;
import com.gbs.agent.interceptor.Interceptor;
import com.gbs.util.ConstructorResolver;
import com.gbs.util.InterceptorArgumentProvider;

public class JavassistClassTest {

	@Test
	public void testJavassistClass() {
		String[] s = new String[] { "com.gbs.vm.TargetVM.testmethdo" };
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
		System.out.println(methods);
		for (Map.Entry<String, Set<String>> entry : methods.entrySet()) {
			try {
				parseMethods(entry.getKey(), entry.getValue());
			} catch (Exception e) {

			}
		}
	}
	
	private Method findMethodByName(Method[] declaredMethods, String methodName) {
        Method findMethod = null;
        int count = 0;
        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                count++;
                findMethod = method;
            }
        }
        if (findMethod == null) {
            throw new RuntimeException(methodName + " not found");
        }
        if (count > 1 ) {
            throw new RuntimeException("duplicated method exist. methodName:" + methodName);
        }
        return findMethod;
    }

	void parseMethods(final String className, final Set<String> methodNames) throws NotFoundException, ClassNotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {
		ClassPool pool = ClassPool.getDefault();
		
		Class<? extends Interceptor> interceptorClazz = AroundInterceptor.class;
		//
		final Method[] declaredMethods = interceptorClazz.getDeclaredMethods();
		final String before = "before";
        final Method beforeMethod = findMethodByName(declaredMethods, before);
        final Class<?>[] beforeParamList = beforeMethod.getParameterTypes();
        //
        final String after = "after";
        final Method afterMethod = findMethodByName(declaredMethods, after);
        final Class<?>[] afterParamList = afterMethod.getParameterTypes();
        
		
		CtClass ctClass = pool.get(className);
		JavassistClass javassistClass = new JavassistClass(pool.getClassLoader(), ctClass);
		String interceptorClassName = "com.gbs.plugin.interceptor.UserIncludeMethodInterceptor";
		System.out.println(ctClass);
//		final String[] names = methodNames.toArray(new String[methodNames.size()]);
		final CtMethod[] declaredMethod = ctClass.getDeclaredMethods();
		for(CtMethod ctMethod:declaredMethod){
			if(methodNames.contains(ctMethod.getName())){
				JavassistMethod javassistMethod = new JavassistMethod(javassistClass, ctMethod);
				System.out.println(javassistMethod.getName());
				Class<?> interceptorType = Class.forName(interceptorClassName);
				System.out.println("forName-->"+interceptorType);
				//
				InterceptorArgumentProvider argumentProvider = new InterceptorArgumentProvider(javassistClass, javassistMethod);
				ConstructorResolver resolver = new ConstructorResolver(interceptorType, argumentProvider);
				//
				if(!resolver.resolve()){
					System.err.println("Cannot find suitable constructor for " + interceptorType.getName());
					return;
				}
				 final Constructor<?> constructor = resolver.getResolvedConstructor();
			     final Object[] resolvedArguments = resolver.getResolvedArguments();
			     System.out.println("constructor-->"+constructor);
			     System.out.println("resolvedArguments-->"+Arrays.toString(resolvedArguments));
			     try {
			    	 Interceptor newInstance = (Interceptor)constructor.newInstance(resolvedArguments);
					System.out.println("newInstance-->"+newInstance);
					
					if(interceptorClazz.isAssignableFrom(newInstance.getClass())){
						System.out.println("newInstance is Interceptor");
						 final Class<? extends Interceptor> casting = (Class<? extends Interceptor>) newInstance.getClass();
						 final Method newbeforeMethod = casting.getMethod("before", beforeParamList);
						 System.out.println("newbeforeMethod-->"+newbeforeMethod);
						 final Method newafterMethod = casting.getMethod("after", afterParamList);
						 System.out.println("newafterMethod-->"+newafterMethod);
						 
						 
						 
					}
				} catch (IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
		}
//		Class c = ctClass.toClass();
//		TargetVM h = (TargetVM)c.newInstance();
//		h.testmethdo();
		ctClass.detach();
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
