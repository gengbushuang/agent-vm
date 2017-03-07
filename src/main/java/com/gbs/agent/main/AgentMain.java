package com.gbs.agent.main;

import java.lang.instrument.Instrumentation;

public class AgentMain {

	private final String agentArgs;
	private final Instrumentation instrumentation;

	public AgentMain(String agentArgs, Instrumentation inst) {
		this.agentArgs = agentArgs;
		this.instrumentation = inst;
	}
	
	
	public void start(){
		
	}

}
