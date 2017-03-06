package com.agent.main;

import java.lang.instrument.Instrumentation;

import com.agent.DebugTransformer;

public class LoadedAgent {

	public static void agentmain(String agentArgs, Instrumentation inst) {
		System.out.println("premain-1." + agentArgs);
		try {
			inst.addTransformer(new DebugTransformer(), true);
			for (Class clazz : inst.getAllLoadedClasses()) {
				if("com.agent.agent_vm_test.MyTest".equals(clazz.getName())){
					inst.retransformClasses(clazz);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}