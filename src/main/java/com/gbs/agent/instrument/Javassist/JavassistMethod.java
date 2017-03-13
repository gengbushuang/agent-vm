package com.gbs.agent.instrument.Javassist;

import javassist.CtBehavior;
import javassist.CtConstructor;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentException;
import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.plugin.DefaultMethodDescriptor;
import com.gbs.plugin.MethodDescriptor;
import com.gbs.util.JavaAssistUtils;

public class JavassistMethod implements InstrumentMethod {

	private final CtBehavior behavior;
	private final InstrumentClass declaringClass;
	private final MethodDescriptor descriptor;

	public JavassistMethod(InstrumentClass declaringClass, CtBehavior behavior) {
		this.behavior = behavior;
		this.declaringClass = declaringClass;

		String[] parameterVariableNames = JavaAssistUtils.getParameterVariableName(behavior);
		int lineNumber = JavaAssistUtils.getLineNumber(behavior);

		DefaultMethodDescriptor descriptor = new DefaultMethodDescriptor(behavior.getDeclaringClass().getName(), behavior.getName(), getParameterTypes(), parameterVariableNames);
		descriptor.setLineNumber(lineNumber);

		this.descriptor = descriptor;
	}

	@Override
	public String getName() {
		return behavior.getName();
	}

	@Override
	public String[] getParameterTypes() {
		return JavaAssistUtils.parseParameterSignature(behavior.getSignature());
	}

	@Override
	public String getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public boolean isConstructor() {
		return behavior instanceof CtConstructor;
	}

	@Override
	public MethodDescriptor getDescriptor() {
		return descriptor;
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
	public void addInterceptor(int interceptorId) throws InstrumentException {
		// TODO Auto-generated method stub

	}
}
