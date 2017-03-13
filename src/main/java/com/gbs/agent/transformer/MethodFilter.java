package com.gbs.agent.transformer;

import com.gbs.agent.instrument.InstrumentMethod;

public interface MethodFilter {
	boolean ACCEPT = true;
	boolean REJECT = false;

	boolean accept(InstrumentMethod method);
}