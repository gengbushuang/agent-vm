package com.gbs.agent.interceptor;

import java.util.Arrays;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingInterceptor implements AroundInterceptor {
	final Logger logger;

	public LoggingInterceptor(String loggerName) {
		this.logger = LogManager.getLogger(loggerName);
	}

	@Override
	public void before(Object target, Object[] args) {
		if (logger.isEnabled(Level.FATAL)) {
			logger.fatal("before " + defaultString(target) + " args:" + Arrays.toString(args));
		}
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		if (logger.isEnabled(Level.FATAL)) {
			logger.fatal("after " + defaultString(target) + " args:" + Arrays.toString(args) + " result:" + result + " Throwable:" + throwable);
		}

	}

	public static String defaultString(final Object object) {
		return String.valueOf(object);
	}

}