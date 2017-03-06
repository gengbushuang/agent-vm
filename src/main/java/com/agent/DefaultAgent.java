package com.agent;

import java.lang.instrument.Instrumentation;

import com.agent.transformer.ClassFileTransformerDispatcher;

public class DefaultAgent implements Agent {

	private final ClassFileTransformerDispatcher classFileTransformer;

	private final Instrumentation instrumentation;

	public DefaultAgent(AgentOption agentOption) {
		if (agentOption == null) {
			throw new NullPointerException("agentOption must not be null");
		}
		if (agentOption.getInstrumentation() == null) {
			throw new NullPointerException("instrumentation must not be null");
		}

		this.instrumentation = agentOption.getInstrumentation();

		this.classFileTransformer = new ClassFileTransformerDispatcher();
		this.instrumentation.addTransformer(this.classFileTransformer, true);
	}
}
