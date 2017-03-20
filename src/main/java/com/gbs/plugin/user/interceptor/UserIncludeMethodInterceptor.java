package com.gbs.plugin.user.interceptor;

import com.gbs.agent.interceptor.AroundInterceptor;
import com.gbs.agent.model.SpanEvent;
import com.gbs.agent.model.Trace;
import com.gbs.agent.model.TraceScope;
import com.gbs.plugin.user.MethodDescriptor;
import com.gbs.plugin.user.UserIncludeMethodDescriptor;
import com.gbs.util.ThreadLocalTraceFactory;

public class UserIncludeMethodInterceptor implements AroundInterceptor {
	private static final String SCOPE_NAME = "##USER_INCLUDE_TRACE";

	private static final UserIncludeMethodDescriptor USER_INCLUDE_METHOD_DESCRIPTOR = new UserIncludeMethodDescriptor();

	private ThreadLocalTraceFactory factory = ThreadLocalTraceFactory.getInstFactory();

	private MethodDescriptor descriptor;

	public UserIncludeMethodInterceptor(MethodDescriptor methodDescriptor) {
		this.descriptor = methodDescriptor;
	}

	@Override
	public void before(Object target, Object[] args) {
		Trace trace = factory.currentRawTraceObject();
		if (trace == null) {
			trace = factory.newTraceObject();
			if (trace == null) {
				return;
			}
			TraceScope oldScope = trace.addScope(SCOPE_NAME);
			if (oldScope != null) {
				deleteUserIncludeTrace(trace);
			}
		}
		entryUserIncludeTraceScope(trace);
		trace.traceBlockBegin();
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		Trace trace = factory.currentRawTraceObject();
		if (trace == null) {
			return;
		}

		if (!leaveUserIncludeTraceScope(trace)) {
			deleteUserIncludeTrace(trace);
			return;
		}

		try {
			final SpanEvent spanEvent = trace.currentSpanEvent();
			spanEvent.setApid(descriptor);
			spanEvent.setException(throwable);
		} finally {
			trace.traceBlockEnd();
			if (isUserIncludeTraceDestination(trace)) {
				deleteUserIncludeTrace(trace);
			}
		}

	}

	private void entryUserIncludeTraceScope(final Trace trace) {
		final TraceScope scope = trace.getScope(SCOPE_NAME);
		if (scope != null) {
			scope.tryEnter();
		}
	}

	private boolean leaveUserIncludeTraceScope(final Trace trace) {
		final TraceScope scope = trace.getScope(SCOPE_NAME);
		if (scope != null) {
			if (scope.canLeave()) {
				scope.leave();
			} else {
				return false;
			}
		}
		return true;
	}

	private void deleteUserIncludeTrace(final Trace trace) {
		factory.removeTraceObject();
		trace.close();
	}

	private boolean isUserIncludeTraceDestination(final Trace trace) {
		final TraceScope scope = trace.getScope(SCOPE_NAME);
		return scope != null && !scope.isActive();
	}

}
