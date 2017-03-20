package com.gbs.agent.model;

import com.gbs.util.CallStack;
import com.gbs.util.SpanStorage;

public class Trace {
	private DefaultTraceScopePool scopePool = new DefaultTraceScopePool();
	private AgentInformation information;

	private final long id;
	private final TraceId traceId;
	private final Span span;

	private final CallStack callStack;

	private boolean closed = false;

	private SpanStorage storage;

	public Trace(AgentInformation information, long transactionId) {
		this.information = information;
		this.traceId = new TraceId(information.getAgentId(), information.getStartTime(), transactionId);
		this.id = traceId.getTransactionSequence();
		span = createSpan();
		this.callStack = new CallStack(span, 10);
	}

	private Span createSpan() {
		Span span = new Span();
		span.setAgentId(information.getAgentId());
		span.setApplicationName(information.getApplicationName());
		span.setAgentStartTime(information.getStartTime());
		span.markBeforeTime();
		return span;
	}

	public void setStorage(SpanStorage storage) {
		this.storage = storage;
	}

	public void traceBlockBegin() {
		final SpanEvent spanEvent = new SpanEvent(span);
		spanEvent.markStartTime();
		callStack.push(spanEvent);
	}

	public SpanEvent currentSpanEvent() {
		SpanEvent spanEvent = callStack.peek();
		if (spanEvent == null) {
			// make dummy.
			spanEvent = new SpanEvent(span);
		}
		return spanEvent;
	}

	public void traceBlockEnd() {
		final SpanEvent spanEvent = callStack.pop();
		if (spanEvent == null) {
			return;
		}
		spanEvent.markAfterTime();

		if (this.storage != null) {
			this.storage.store(spanEvent);
		}
	}

	public TraceScope getScope(String name) {
		return scopePool.get(name);
	}

	public TraceScope addScope(String name) {
		return scopePool.add(name);
	}

	public void close() {
		if (closed) {
			return;
		}
		closed = true;

		if (!callStack.empty()) {

		} else {
			Span span = this.span;
			span.markAfterTime();
		}

		if (this.storage != null) {
			this.storage.store(span);
		}

		if (this.storage != null) {
			this.storage.close();
			this.storage = null;
		}
	}

}
