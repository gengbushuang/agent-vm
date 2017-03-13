package com.gbs.agent.transformer;

import com.gbs.agent.instrument.InstrumentClass;

public interface ClassFilter {
	boolean ACCEPT = true;
	boolean REJECT = false;

	boolean accept(InstrumentClass clazz);
}