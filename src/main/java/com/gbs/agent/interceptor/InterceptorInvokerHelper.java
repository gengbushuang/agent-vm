package com.gbs.agent.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InterceptorInvokerHelper {
	private static boolean propagateException = false;
	private static final Logger logger = LogManager.getLogger(InterceptorInvokerHelper.class.getName());

	public static void handleException(Throwable t) {
		if (propagateException) {
			throw new RuntimeException(t);
		} else {
			logger.warn("Exception occurred from interceptor", t);
		}
	}

	public static void setPropagateException(boolean propagate) {
		propagateException = propagate;
	}
}