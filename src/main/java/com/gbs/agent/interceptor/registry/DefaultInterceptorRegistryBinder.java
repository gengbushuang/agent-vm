package com.gbs.agent.interceptor.registry;

public class DefaultInterceptorRegistryBinder {

	public final static int DEFAULT_MAX = 8192;

	private final Object lock = new Object();
	private final InterceptorRegistryAdaptor interceptorRegistryAdaptor;

	public DefaultInterceptorRegistryBinder() {
		this(DEFAULT_MAX);
	}

	public DefaultInterceptorRegistryBinder(int maxRegistrySize) {
		this.interceptorRegistryAdaptor = new DefaultInterceptorRegistryAdaptor(maxRegistrySize);
	}

	public void bind() {
		InterceptorRegistry.bind(interceptorRegistryAdaptor, lock);
	}

	public void unbind() {
		InterceptorRegistry.unbind(lock);
	}

	public InterceptorRegistryAdaptor getInterceptorRegistryAdaptor() {
		return interceptorRegistryAdaptor;
	}

	public String getInterceptorRegistryClassName() {
		return InterceptorRegistry.class.getName();
	}
}