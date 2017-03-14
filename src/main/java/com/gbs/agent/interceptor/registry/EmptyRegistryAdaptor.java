package com.gbs.agent.interceptor.registry;

import com.gbs.agent.interceptor.Interceptor;
import com.gbs.agent.interceptor.LoggingInterceptor;

public final class EmptyRegistryAdaptor implements InterceptorRegistryAdaptor {

    public static final InterceptorRegistryAdaptor EMPTY = new EmptyRegistryAdaptor();

    private static final LoggingInterceptor LOGGING_INTERCEPTOR = new LoggingInterceptor("com.navercorp.pinpoint.profiler.interceptor.EMPTY");

    public EmptyRegistryAdaptor() {
    }


    @Override
    public int addInterceptor(Interceptor interceptor) {
        return -1;
    }


    public Interceptor getInterceptor(int key) {
        return LOGGING_INTERCEPTOR;
    }
}