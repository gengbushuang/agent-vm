package com.gbs.agent.model;

import com.gbs.plugin.user.MethodDescriptor;

public class SpanEvent {

	private final Span span;

	private int startElapsed;

	private int endElapsed;

	private short sequence;

	private int depth;
	
	private String methodName;
	
	private String className;

	public SpanEvent(Span span) {
		this.span = span;
	}

	public Span getSpan() {
		return span;
	}

	public void setStartElapsed(int startElapsed) {
		this.startElapsed = startElapsed;
	}

	public int getStartElapsed() {
		return startElapsed;
	}

	public void setEndElapsed(int endElapsed) {
		this.endElapsed = endElapsed;
	}

	public int getEndElapsed() {
		return endElapsed;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	public short getSequence() {
		return sequence;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setApid(MethodDescriptor methodDescriptor) {
		if (methodDescriptor == null) {
            return;
        }
		setClassName(methodDescriptor.getClassName());
		setMethodName(methodDescriptor.getMethodName());
	}

	public void markStartTime() {
		// spanEvent.setStartElapsed((int) (startTime - parentSpanStartTime));
		final int startElapsed = (int) (System.currentTimeMillis() - span.getStartTime());

		// If startElapsed is 0, logic without mark is useless. Don't do that.
		// The first SpanEvent of a Span could result in 0. Not likely
		// afterwards.
		this.setStartElapsed(startElapsed);
	}

	public long getStartTime() {
		return span.getStartTime() + getStartElapsed();
	}

	public void markAfterTime() {
		final int endElapsed = (int) (System.currentTimeMillis() - getStartTime());
		if (endElapsed != 0) {
			this.setEndElapsed(endElapsed);
		}
	}

	public long getAfterTime() {
		return span.getStartTime() + getStartElapsed() + getEndElapsed();
	}

	public void setException(Throwable throwable) {
		if(throwable!=null){
			StackTraceElement traceElement = throwable.getStackTrace()[0];
			StringBuilder sb = new StringBuilder(256);
			sb.append(throwable.getClass().getName());
			sb.append(":");
			sb.append("(").append(throwable.getMessage()).append(")");
			sb.append("(").append(traceElement.getFileName()).append(":").append(traceElement.getLineNumber()).append(")");
			span.setExceptionInfo(sb.toString());
			sb.delete(0, sb.length());
			sb = null;
		}
	}

	@Override
	public String toString() {
		return "SpanEvent [startElapsed=" + startElapsed + ", endElapsed=" + endElapsed + ", sequence=" + sequence + ", depth=" + depth + ", methodName=" + methodName + ", className=" + className + "]";
	}
	
}
