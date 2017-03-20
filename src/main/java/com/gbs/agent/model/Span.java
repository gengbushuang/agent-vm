package com.gbs.agent.model;

import java.util.List;

public class Span {

	public String agentId;

	private String applicationName;

	private long agentStartTime;

	private String transactionId;

	private String spanId;

	private String parentSpanId;

	private long startTime;

	private int elapsed;

	private String endPoint;

	public int apiId;

	public List<SpanEvent> spanEventList;

	public String exceptionInfo;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public long getAgentStartTime() {
		return agentStartTime;
	}

	public void setAgentStartTime(long agentStartTime) {
		this.agentStartTime = agentStartTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void setElapsed(int elapsed) {
		this.elapsed = elapsed;
	}

	public void markBeforeTime() {
		this.setStartTime(System.currentTimeMillis());
	}

	public void markAfterTime() {
		final int after = (int) (System.currentTimeMillis() - this.getStartTime());
		// TODO have to change int to long
		if (after != 0) {
			this.setElapsed(after);
		}
	}

	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	public void setSpanEventList(List<SpanEvent> spanEventList) {
		this.spanEventList = spanEventList;
	}
	
	public List<SpanEvent> getSpanEventList() {
		return spanEventList;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	@Override
	public String toString() {
		return "Span [agentId=" + agentId + ", applicationName=" + applicationName + ", agentStartTime=" + agentStartTime + ", transactionId=" + transactionId + ", spanId=" + spanId + ", parentSpanId=" + parentSpanId + ", startTime=" + startTime + ", elapsed=" + elapsed + ", endPoint=" + endPoint + ", apiId=" + apiId + ", exceptionInfo=" + exceptionInfo + "]";
	}
	
}
