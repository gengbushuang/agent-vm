package com.gbs.agent.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;

public class ClassFileTransformerDispatcher implements ClassFileTransformer {

	private ClassPool classPool;
	
	private final ClassLoader agentClassLoader = this.getClass().getClassLoader();
	private final ClassFileFilter classFileFilter;
	
	public ClassFileTransformerDispatcher(){
		this.classFileFilter = new AgentClassFilter(agentClassLoader);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		boolean accept = classFileFilter.accept(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
		if(!accept){
			return null;
		}
		
		
		return null;
	}

}
