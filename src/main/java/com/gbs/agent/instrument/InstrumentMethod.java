package com.gbs.agent.instrument;

import com.gbs.plugin.MethodDescriptor;

public interface InstrumentMethod {
	/**
	 * 方法名
	 * @Description: TODO
	 * @author gbs
	 * @return
	 */
    String getName();

    /**
     * 参数类型
     * @Description: TODO
     * @author gbs
     * @return
     */
    String[] getParameterTypes();
    
    String getReturnType();

    int getModifiers();
    /**
     * 是不是构造方法
     * @Description: TODO
     * @author gbs
     * @return
     */
    boolean isConstructor();
    
    MethodDescriptor getDescriptor();

    int addInterceptor(String interceptorClassName) throws InstrumentException;

    int addInterceptor(String interceptorClassName, Object[] constructorArgs) throws InstrumentException;
    
    void addInterceptor(int interceptorId) throws InstrumentException;
}