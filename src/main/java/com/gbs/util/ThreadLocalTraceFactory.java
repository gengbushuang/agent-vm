package com.gbs.util;

import java.util.concurrent.atomic.AtomicLong;

import com.gbs.agent.model.AgentInformation;
import com.gbs.agent.model.Trace;
import com.gbs.agent.sender.DataSender;
import com.gbs.agent.sender.SpanStreamSender;

public class ThreadLocalTraceFactory {

	private static final ThreadLocalTraceFactory INSTANCE = new ThreadLocalTraceFactory();

	public static final long INITIAL_TRANSACTION_ID = 1L;

	private final AtomicLong transactionId = new AtomicLong(INITIAL_TRANSACTION_ID);

	private final AgentInformation information = new AgentInformation("agentId", "applicationName", System.currentTimeMillis(), 10, "test", "127.0.0.1");

	protected final DataSender dataSender = new SpanStreamSender() ;

	public ThreadLocal<Trace> local = new ThreadLocal<Trace>();

	public static ThreadLocalTraceFactory getInstFactory() {
		return INSTANCE;
	}

	public Trace currentRawTraceObject() {
		return local.get();
	}

	public Trace newTraceObject() {
		final Trace trace = new Trace(information, transactionId.getAndIncrement());
		final SpanStorage storage = new SpanStorage(dataSender);
		trace.setStorage(storage);
		bind(trace);
		return trace;
	}

	public Trace removeTraceObject() {
		final Trace value = local.get();
		local.remove();
		return value;
	}
	
	private void bind(Trace trace) {
		local.set(trace);
	}

}
