package com.gbs.util;

public class ThreadLocalTraceFactory {

	private static final ThreadLocalTraceFactory INSTANCE = new ThreadLocalTraceFactory();

	public ThreadLocal<StringBuilder> local = new ThreadLocal<StringBuilder>();

	public static ThreadLocalTraceFactory getInstFactory() {
		return INSTANCE;
	}

	public StringBuilder get() {
		return local.get();
	}

	public void set(StringBuilder sb) {
		local.set(sb);
	}
}
