package com.gbs.plugin.user.interceptor;

import com.gbs.agent.interceptor.AroundInterceptor;
import com.gbs.plugin.user.MethodDescriptor;
import com.gbs.plugin.user.UserIncludeMethodDescriptor;
import com.gbs.util.ThreadLocalTraceFactory;

public class UserIncludeMethodInterceptor implements AroundInterceptor {

	private static final UserIncludeMethodDescriptor USER_INCLUDE_METHOD_DESCRIPTOR = new UserIncludeMethodDescriptor();

	private ThreadLocalTraceFactory factory = ThreadLocalTraceFactory.getInstFactory();
	
	private MethodDescriptor descriptor;

	public UserIncludeMethodInterceptor(MethodDescriptor methodDescriptor) {
		this.descriptor = methodDescriptor;
	}

	@Override
	public void before(Object target, Object[] args) {
		System.out.println("before-->"+descriptor.getMethodName());
		StringBuilder builder = factory.get();
		if(builder==null){
			factory.set(new StringBuilder());
			builder = factory.get();
			builder.append(USER_INCLUDE_METHOD_DESCRIPTOR.getApiId());
		}
		if(builder.length()>0){
			builder.append(",");
		}
		builder.append("1");
		System.out.println("before-->"+builder.toString());
//		System.out.println("target-->"+target);
//		System.out.println("args-->"+Arrays.toString(args));
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		System.out.println("after-->"+descriptor.getMethodName());
		StringBuilder builder = factory.get();
		if(builder==null){
			return;
		}
		System.out.println("after-->"+builder.toString());
		int length = builder.length();
		if(length>1){
			builder.setLength(length-2);
		}
		
//		System.out.println("target-->"+target);
//		System.out.println("args-->"+Arrays.toString(args));
		
	}

}
