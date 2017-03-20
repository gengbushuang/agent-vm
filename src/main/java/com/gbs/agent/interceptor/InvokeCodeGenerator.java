package com.gbs.agent.interceptor;

import java.lang.reflect.Modifier;

import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.agent.interceptor.registry.InterceptorRegistry;
import com.gbs.plugin.user.MethodDescriptor;
import com.gbs.util.JavaAssistUtils;

public class InvokeCodeGenerator {
	// private final TraceContext traceContext;
	protected final InterceptorDefinition interceptorDefinition;
	protected final InstrumentMethod targetMethod;
	protected final int interceptorId;

	public InvokeCodeGenerator(int interceptorId, InterceptorDefinition interceptorDefinition, InstrumentMethod targetMethod) {
		if (interceptorDefinition == null) {
			throw new NullPointerException("interceptorDefinition must not be null");
		}
		if (targetMethod == null) {
			throw new NullPointerException("targetMethod must not be null");
		}

		this.interceptorDefinition = interceptorDefinition;
		this.targetMethod = targetMethod;
		this.interceptorId = interceptorId;
		// this.traceContext = traceContext;

	}

	protected String getInterceptorType() {
		// return interceptorDefinition.getInterceptorClass().getName();
		return interceptorDefinition.getInterceptorBaseClass().getName();
	}

	protected String getParameterTypes() {
		String[] parameterTypes = targetMethod.getParameterTypes();
		return JavaAssistUtils.getParameterDescription(parameterTypes);
	}

	protected String getTarget() {
		return Modifier.isStatic(targetMethod.getModifiers()) ? "null" : "this";
	}

	protected String getArguments() {
		if (targetMethod.getParameterTypes().length == 0) {
			return "null";
		}

		return "$args";
	}

	protected int getApiId() {
		final MethodDescriptor descriptor = targetMethod.getDescriptor();
//		 final int apiId = traceContext.cacheApi(descriptor);
//		 return apiId;
		return descriptor.getApiId();
	}

	protected String getInterceptorInvokerHelperClassName() {
		return InterceptorInvokerHelper.class.getName();
	}

	protected String getInterceptorRegistryClassName() {
		return InterceptorRegistry.class.getName();
	}

	protected String getInterceptorVar() {
		return getInterceptorVar(interceptorId);
	}

	public static String getInterceptorVar(int interceptorId) {
		return "_$PINPOINT$_interceptor" + interceptorId;
	}
}