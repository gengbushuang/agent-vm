package com.gbs.agent.instrument.Javassist;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javassist.CtClass;
import javassist.NotFoundException;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentClassPool;
import com.gbs.agent.instrument.InstrumentException;
import com.gbs.agent.interceptor.registry.DefaultInterceptorRegistryBinder;
import com.gbs.util.JavaAssistUtils;

public class JavassistClassPool implements InstrumentClassPool {
	private final Logger logger = LogManager.getLogger(JavassistClassPool.class);
	private final boolean isDebug = logger.isDebugEnabled();

	private final DefaultInterceptorRegistryBinder interceptorRegistryBinder;

	private final NamedClassPool namedClassPool;

	public JavassistClassPool(DefaultInterceptorRegistryBinder interceptorRegistryBinder) {

		this.interceptorRegistryBinder = interceptorRegistryBinder;
		this.namedClassPool = createRootClassPool();
	}

	private NamedClassPool createRootClassPool() {
		NamedClassPool systemClassPool = new NamedClassPool("rootClassPool");
		systemClassPool.appendSystemPath();
		// if (rootClassPoolHandler != null ) {
		// rootClassPoolHandler.handleClassPool(systemClassPool);
		//
		// }
		return systemClassPool;
	}

	@Override
	public InstrumentClass getClass(ClassLoader classLoader, String jvmInternalClassName, byte[] classFileBuffer) throws InstrumentException {
		final CtClass cc = getCtClass(classLoader, jvmInternalClassName, classFileBuffer);
		return new JavassistClass(interceptorRegistryBinder, classLoader, cc);
	}

	private CtClass getCtClass(ClassLoader classLoader, String jvmInternalClassName, byte[] classFileBuffer) throws InstrumentException {
		final String javaName = JavaAssistUtils.jvmNameToJavaName(jvmInternalClassName);
		if (isDebug) {
			logger.debug("getContextClassPool() className={}", javaName);
		}
		try {
			return namedClassPool.get(javaName);
		} catch (NotFoundException e) {
			throw new InstrumentException(jvmInternalClassName + " class not found. Cause:" + e.getMessage(), e);
		}
	}

	@Override
	public boolean hasClass(ClassLoader classLoader, String javassistClassName) {
		URL url = namedClassPool.find(javassistClassName);
		if (url == null) {
			return false;
		}
		return true;
	}

	@Override
	public void appendToBootstrapClassPath(String jar) {
		try {
			namedClassPool.appendClassPath(jar);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}
}