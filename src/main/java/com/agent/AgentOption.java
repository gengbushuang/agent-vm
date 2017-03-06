package com.agent;

import java.lang.instrument.Instrumentation;

public interface AgentOption {
	String getAgentArgs();

	Instrumentation getInstrumentation();
}
