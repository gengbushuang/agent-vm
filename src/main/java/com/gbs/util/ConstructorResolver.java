package com.gbs.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class ConstructorResolver {
	private final Class<?> type;
	private final InterceptorArgumentProvider argumentsResolver;

	private Constructor<?> resolvedConstructor;
	private Object[] resolvedArguments;

	public ConstructorResolver(Class<?> type, InterceptorArgumentProvider argumentsResolver) {
		this.type = type;
		this.argumentsResolver = argumentsResolver;
	}

	public boolean resolve() {
		Constructor<?>[] constructors = (Constructor<?>[]) type.getConstructors();
		Arrays.sort(constructors, CONSTRUCTOR_COMPARATOR);

		for (Constructor<?> constructor : constructors) {
			Class<?>[] types = constructor.getParameterTypes();
			Annotation[][] annotations = constructor.getParameterAnnotations();

			Object[] resolvedArguments = resolve(types, annotations);

			if (resolvedArguments != null) {
				this.resolvedConstructor = constructor;
				this.resolvedArguments = resolvedArguments;
				return true;
			}
		}

		return false;
	}

	public Object[] resolve(Class<?>[] types, Annotation[][] annotations) {
		int length = types.length;
		Object[] arguments = new Object[length];
		outer: for (int i = 0; i < length; i++) {
			Optional<?> optional = argumentsResolver.get(i, types[i], annotations[i]);
			if (optional.isPresent()) {
				arguments[i] = optional.get();
				continue outer;
			}
			return null;
		}
		return arguments;
	}

	public Constructor<?> getResolvedConstructor() {
		return resolvedConstructor;
	}

	public Object[] getResolvedArguments() {
		return resolvedArguments;
	}

	private static final Comparator<Constructor<?>> CONSTRUCTOR_COMPARATOR = new Comparator<Constructor<?>>() {

		@Override
		public int compare(Constructor<?> o1, Constructor<?> o2) {
			int p1 = o1.getParameterTypes().length;
			int p2 = o2.getParameterTypes().length;

			return (p1 < p2) ? 1 : ((p1 == p2) ? 0 : -1);
		}

	};
}