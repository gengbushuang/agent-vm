package com.gbs.agent.main;

import java.lang.instrument.Instrumentation;

public class PremainAgent {

	public static void premain(String agentArgs, Instrumentation inst) {
//		System.out.println("agentArgs-->"+agentArgs);
//		try {
//			inst.addTransformer(new DebugTransformer(), true);
//		} catch (Exception e) {
//
//		}
		AgentMain agentMain = new AgentMain(agentArgs,inst);
		agentMain.start();
	}
}
