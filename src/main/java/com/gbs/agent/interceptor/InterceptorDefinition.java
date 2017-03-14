package com.gbs.agent.interceptor;

import java.lang.reflect.Method;

public interface InterceptorDefinition {
	Class<? extends Interceptor> getInterceptorBaseClass();

	Class<? extends Interceptor> getInterceptorClass();

	InterceptorType getInterceptorType();

	CaptureType getCaptureType();

	Method getBeforeMethod();

	Method getAfterMethod();
}