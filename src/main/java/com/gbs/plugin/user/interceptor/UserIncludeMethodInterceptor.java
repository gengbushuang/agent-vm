package com.gbs.plugin.user.interceptor;

import java.util.Arrays;

import com.gbs.agent.interceptor.AroundInterceptor;
import com.gbs.plugin.user.MethodDescriptor;
import com.gbs.plugin.user.UserIncludeMethodDescriptor;

public class UserIncludeMethodInterceptor implements AroundInterceptor {

	private static final UserIncludeMethodDescriptor USER_INCLUDE_METHOD_DESCRIPTOR = new UserIncludeMethodDescriptor();

	private MethodDescriptor descriptor;

	public UserIncludeMethodInterceptor(MethodDescriptor methodDescriptor) {
		this.descriptor = methodDescriptor;
	}

	@Override
	public void before(Object target, Object[] args) {
		System.out.println("target-->"+target);
		System.out.println("args-->"+Arrays.toString(args));
		System.out.println("before-->"+descriptor);
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		System.out.println("target-->"+target);
		System.out.println("args-->"+Arrays.toString(args));
		System.out.println("after-->"+descriptor);
	}

}
