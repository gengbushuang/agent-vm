package com.gbs.agent.model;

import com.gbs.util.SpanId;

public class TraceId {

	private final String agentId;
	private final long agentStartTime;
	private final long transactionSequence;

	private final long parentSpanId;
	private final long spanId;

	public TraceId(String agentId, long agentStartTime, long transactionId) {
		this(agentId, agentStartTime, transactionId, SpanId.NULL, SpanId.newSpanId());
	}

//	public static TraceId parse(String transactionId, long parentSpanID, long spanID) {
//		if (transactionId == null) {
//			throw new NullPointerException("transactionId must not be null");
//		}
//		final TransactionId parseId = TransactionIdUtils.parseTransactionId(transactionId);
//		return new DefaultTraceId(parseId.getAgentId(), parseId.getAgentStartTime(), parseId.getTransactionSequence(), parentSpanID, spanID);
//	}

	public TraceId getNextTraceId() {
		return new TraceId(this.agentId, this.agentStartTime, transactionSequence, spanId, SpanId.nextSpanID(spanId, parentSpanId));
	}

	public TraceId(String agentId, long agentStartTime, long transactionId, long parentSpanId, long spanId) {
		if (agentId == null) {
			throw new NullPointerException("agentId must not be null");
		}
		this.agentId = agentId;
		this.agentStartTime = agentStartTime;
		this.transactionSequence = transactionId;

		this.parentSpanId = parentSpanId;
		this.spanId = spanId;
	}
	
	
	public String getAgentId() {
        return agentId;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public long getTransactionSequence() {
        return transactionSequence;
    }

    public long getParentSpanId() {
        return parentSpanId;
    }

    public long getSpanId() {
        return spanId;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultTraceID{");
        sb.append("agentId='").append(agentId).append('\'');
        sb.append(", agentStartTime=").append(agentStartTime);
        sb.append(", transactionSequence=").append(transactionSequence);
        sb.append(", parentSpanId=").append(parentSpanId);
        sb.append(", spanId=").append(spanId);
        sb.append('}');
        return sb.toString();
    }
}
