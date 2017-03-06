package com.agent;

import java.lang.instrument.Instrumentation;

public class DefaultAgentOption implements AgentOption {
	private final String agentArgs;
	private final Instrumentation instrumentation;

	public DefaultAgentOption(final String agentArgs, final Instrumentation instrumentation) {
		if (instrumentation == null) {
			throw new NullPointerException("instrumentation must not be null");
		}
		this.agentArgs = agentArgs;
		this.instrumentation = instrumentation;
	}

	@Override
	public String getAgentArgs() {
		// TODO Auto-generated method stub
		return agentArgs;
	}

	@Override
	public Instrumentation getInstrumentation() {
		// TODO Auto-generated method stub
		return instrumentation;
	}

}
