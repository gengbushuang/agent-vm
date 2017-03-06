package com.agent.instrument.Javassist;

import javassist.CtClass;

public class JavassistClass {

	private final ClassLoader classLoader;
    private final CtClass ctClass;
    
    public JavassistClass( ClassLoader classLoader, CtClass ctClass) {
        this.ctClass = ctClass;
        this.classLoader = classLoader;
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
    
}
