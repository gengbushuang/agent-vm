package com.gbs.util;

import java.lang.annotation.Annotation;
import java.util.Optional;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.plugin.MethodDescriptor;

public class InterceptorArgumentProvider {

	private final InstrumentClass targetClass;
	private final InstrumentMethod targetMethod;

	public InterceptorArgumentProvider(InstrumentClass targetClass) {
		this(targetClass, null);
	}

	public InterceptorArgumentProvider(InstrumentClass targetClass, InstrumentMethod targetMethod) {
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
	}

	public Optional<?> get(int index, Class<?> type, Annotation[] annotations) {
		if (type == InstrumentClass.class) {
			return Optional.ofNullable(targetClass);
		} else if (type == MethodDescriptor.class) {
			MethodDescriptor descriptor = targetMethod.getDescriptor();
			return Optional.ofNullable(descriptor);
		} else if (type == InstrumentMethod.class) {
			return Optional.ofNullable(targetMethod);
		}
		return Optional.empty();
	}
}