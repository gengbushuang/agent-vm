package com.gbs.agent.transformer;

import java.security.ProtectionDomain;

public class AgentClassFilter implements ClassFileFilter {

	private final ClassLoader agentLoader;

	public AgentClassFilter(ClassLoader agentClassLoader) {
		if (agentClassLoader == null) {
			throw new NullPointerException("agentLoader must not be null");
		}
		this.agentLoader = agentClassLoader;
	}

	@Override
	public boolean accept(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
		// bootstrap classLoader
		if (classLoader == null) {
			return CONTINUE;
		}
		if (classLoader == agentLoader) {
			// skip classes loaded by agent class loader.
			return SKIP;
		}

		// Skip pinpoint packages too.
		if (className.startsWith("com/gbs/")) {
			return SKIP;
		}

		return CONTINUE;
	}

}
