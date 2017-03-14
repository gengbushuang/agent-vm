package com.gbs.agent.interceptor.registry;

import com.gbs.agent.interceptor.Interceptor;

public interface InterceptorRegistryAdaptor {
    int addInterceptor(Interceptor interceptor);
    Interceptor getInterceptor(int key);
}