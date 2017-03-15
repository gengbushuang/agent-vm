package com.gbs.agent.instrument.Javassist;

import java.io.IOException;
import java.util.List;

import javassist.CannotCompileException;
import javassist.CtClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentException;
import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.agent.interceptor.registry.DefaultInterceptorRegistryBinder;
import com.gbs.agent.transformer.ClassFilter;
import com.gbs.agent.transformer.MethodFilter;

public class JavassistClass implements InstrumentClass {

	private final Logger logger = LogManager.getLogger(JavassistClass.class);

	private final ClassLoader classLoader;
	private final CtClass ctClass;
	
	private final DefaultInterceptorRegistryBinder interceptorRegistryBinder;

	public JavassistClass(DefaultInterceptorRegistryBinder interceptorRegistryBinder,ClassLoader classLoader, CtClass ctClass) {
		this.ctClass = ctClass;
		this.classLoader = classLoader;
		this.interceptorRegistryBinder = interceptorRegistryBinder;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public boolean isInterceptable() {
		return !ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isModified();
	}

	public boolean isInterface() {
		return this.ctClass.isInterface();
	}

	public String getName() {
		return this.ctClass.getName();
	}

	public String getSuperClass() {
		return this.ctClass.getClassFile2().getSuperclass();
	}

	public String[] getInterfaces() {
		return this.ctClass.getClassFile2().getInterfaces();
	}

	@Override
	public InstrumentMethod getConstructor(String... parameterTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstrumentMethod> getDeclaredMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstrumentMethod> getDeclaredMethods(MethodFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstrumentMethod getDeclaredMethod(String name, String... parameterTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstrumentClass> getNestedClasses(ClassFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasConstructor(String... parameterTypes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasDeclaredMethod(String methodName, String... parameterTypes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasMethod(String methodName, String... parameterTypes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasEnclosingMethod(String methodName, String... parameterTypes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasField(String name, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasField(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void weave(String adviceClassName) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addField(String accessorTypeName) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGetter(String getterTypeName, String fieldName) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSetter(String setterTypeName, String fieldName) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSetter(String setterTypeName, String fieldName, boolean removeFinal) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public int addInterceptor(String interceptorClassName) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addInterceptor(String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addInterceptor(MethodFilter filter, String interceptorClassName) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addInterceptor(MethodFilter filter, String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addScopedInterceptor(String interceptorClassName, Object[] constructorArgs, String scopeName) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addScopedInterceptor(String interceptorClassName, String scopeName) throws InstrumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InstrumentMethod addDelegatorMethod(String methodName, String... paramTypes) throws InstrumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytecode() throws InstrumentException {
		try {
			byte[] bytes = ctClass.toBytecode();
			ctClass.detach();
			return bytes;
		} catch (IOException e) {
			logger.info("IoException class:{} Caused:{}", ctClass.getName(), e.getMessage(), e);
		} catch (CannotCompileException e) {
			logger.info("CannotCompileException class:{} Caused:{}", ctClass.getName(), e.getMessage(), e);
		}
		return null;
	}

}
