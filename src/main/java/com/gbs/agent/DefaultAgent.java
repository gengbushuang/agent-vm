package com.gbs.agent;

import java.lang.instrument.Instrumentation;

import com.gbs.agent.instrument.Javassist.JavassistClassPool;
import com.gbs.agent.interceptor.registry.DefaultInterceptorRegistryBinder;
import com.gbs.agent.transformer.ClassFileTransformerDispatcher;

public class DefaultAgent implements Agent {

	private final ClassFileTransformerDispatcher classFileTransformer;

	private final Instrumentation instrumentation;

	private final AgentOption agentOption;
	private final JavassistClassPool classPool;

	private final DefaultInterceptorRegistryBinder interceptorRegistryBinder;

	public DefaultAgent(AgentOption agentOption) {
		this(agentOption, new DefaultInterceptorRegistryBinder());
	}

	public DefaultAgent(AgentOption agentOption, final DefaultInterceptorRegistryBinder interceptorRegistryBinder) {
		if (agentOption == null) {
			throw new NullPointerException("agentOption must not be null");
		}
		if (agentOption.getInstrumentation() == null) {
			throw new NullPointerException("instrumentation must not be null");
		}

		if (interceptorRegistryBinder == null) {
			throw new NullPointerException("interceptorRegistryBinder must not be null");
		}
		this.instrumentation = agentOption.getInstrumentation();

		this.interceptorRegistryBinder = interceptorRegistryBinder;
		interceptorRegistryBinder.bind();

		this.agentOption = agentOption;

		this.classPool = new JavassistClassPool(interceptorRegistryBinder);

		this.classFileTransformer = new ClassFileTransformerDispatcher(this);
		this.instrumentation.addTransformer(this.classFileTransformer, true);
	}

	public JavassistClassPool getClassPool() {
		return classPool;
	}
}
