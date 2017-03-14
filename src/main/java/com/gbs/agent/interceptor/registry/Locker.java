package com.gbs.agent.interceptor.registry;
public interface Locker {

    boolean lock(Object lock);

    boolean unlock(Object lock);
}