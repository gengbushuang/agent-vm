package com.gbs.agent.instrument;

public interface InstrumentClassPool {
	InstrumentClass getClass(ClassLoader classLoader, String classInternalName, byte[] classFileBuffer) throws InstrumentException;

	boolean hasClass(ClassLoader classLoader, String classBinaryName);

	void appendToBootstrapClassPath(String jar);
}