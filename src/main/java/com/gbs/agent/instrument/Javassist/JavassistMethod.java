package com.gbs.agent.instrument.Javassist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

import com.gbs.agent.instrument.InstrumentClass;
import com.gbs.agent.instrument.InstrumentException;
import com.gbs.agent.instrument.InstrumentMethod;
import com.gbs.agent.interceptor.CaptureType;
import com.gbs.agent.interceptor.Interceptor;
import com.gbs.agent.interceptor.InterceptorDefinition;
import com.gbs.agent.interceptor.InvokeAfterCodeGenerator;
import com.gbs.agent.interceptor.InvokeBeforeCodeGenerator;
import com.gbs.agent.interceptor.InvokeCodeGenerator;
import com.gbs.agent.interceptor.registry.DefaultInterceptorRegistryBinder;
import com.gbs.plugin.user.DefaultMethodDescriptor;
import com.gbs.plugin.user.MethodDescriptor;
import com.gbs.util.ConstructorResolver;
import com.gbs.util.InterceptorArgumentProvider;
import com.gbs.util.InterceptorDefinitionUtils;
import com.gbs.util.JavaAssistUtils;

public class JavassistMethod implements InstrumentMethod {

	private final CtBehavior behavior;
	private final InstrumentClass declaringClass;
	private final MethodDescriptor descriptor;
	private final DefaultInterceptorRegistryBinder interceptorRegistryBinder;
	
	private final static InterceptorDefinitionUtils interceptorDefinitionUtils = new InterceptorDefinitionUtils();

	public JavassistMethod(InstrumentClass declaringClass, CtBehavior behavior,DefaultInterceptorRegistryBinder interceptorRegistryBinder) {
		this.behavior = behavior;
		this.declaringClass = declaringClass;
		this.interceptorRegistryBinder = interceptorRegistryBinder;
		String[] parameterVariableNames = JavaAssistUtils.getParameterVariableName(behavior);
		int lineNumber = JavaAssistUtils.getLineNumber(behavior);

		DefaultMethodDescriptor descriptor = new DefaultMethodDescriptor(behavior.getDeclaringClass().getName(), behavior.getName(), getParameterTypes(), parameterVariableNames);
		descriptor.setLineNumber(lineNumber);

		this.descriptor = descriptor;
	}

	@Override
	public String getName() {
		return behavior.getName();
	}

	@Override
	public String[] getParameterTypes() {
		return JavaAssistUtils.parseParameterSignature(behavior.getSignature());
	}

	@Override
	public String getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConstructor() {
		return behavior instanceof CtConstructor;
	}

	@Override
	public MethodDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public int addInterceptor(String interceptorClassName) throws InstrumentException {
		return addInterceptor0(interceptorClassName, null);
	}

	@Override
	public int addInterceptor(String interceptorClassName, Object[] constructorArgs) throws InstrumentException {
		return addInterceptor0(interceptorClassName, constructorArgs);
	}

	@Override
	public void addInterceptor(int interceptorId) throws InstrumentException {
		// TODO Auto-generated method stub

	}

	private int addInterceptor0(String interceptorClassName, Object[] constructorArgs) {
		try {
			Interceptor interceptor = createInterceptor(interceptorClassName, constructorArgs);
			int interceptorId = interceptorRegistryBinder.getInterceptorRegistryAdaptor().addInterceptor(interceptor);
			addInterceptor0(interceptor, interceptorId);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void addInterceptor0(Interceptor interceptor, int interceptorId) throws NotFoundException, CannotCompileException {
		if (interceptor == null) {
			throw new NullPointerException("interceptor must not be null");
		}
		InterceptorDefinition interceptorDefinition = interceptorDefinitionUtils.createInterceptorDefinition(interceptor.getClass());
		final String localVariableName = initializeLocalVariable(interceptorId);
		int originalCodeOffset = insertBefore(-1, localVariableName);
		boolean localVarsInitialized = false;
		final int offset = addBeforeInterceptor(interceptorDefinition, interceptorId, originalCodeOffset);
		if (offset != -1) {
			localVarsInitialized = true;
			originalCodeOffset = offset;
		}
		addAfterInterceptor(interceptorDefinition, interceptorId, localVarsInitialized, originalCodeOffset);
	}

	private void addAfterInterceptor(InterceptorDefinition interceptorDefinition, int interceptorId, boolean localVarsInitialized, int originalCodeOffset) throws CannotCompileException, NotFoundException {
		final Class<?> interceptorClass = interceptorDefinition.getInterceptorClass();
		final CaptureType captureType = interceptorDefinition.getCaptureType();
		if (!isAfterInterceptor(captureType)) {
			return;
		}

		final Method interceptorMethod = interceptorDefinition.getAfterMethod();
		if (interceptorMethod == null) {
			// if (isDebug) {
			// logger.debug("Skip adding after interceptor because the interceptor doesn't have after method: {}",
			// interceptorClass.getName());
			// }
			return;
		}
		InvokeAfterCodeGenerator catchGenerator = new InvokeAfterCodeGenerator(interceptorId, interceptorDefinition, declaringClass, this, localVarsInitialized, true);
		String catchCode = catchGenerator.generate();

		// if (isDebug) {
		// logger.debug("addAfterInterceptor catch behavior:{} code:{}",
		// behavior.getLongName(), catchCode);
		// }
		CtClass throwable = behavior.getDeclaringClass().getClassPool().get("java.lang.Throwable");
		insertCatch(originalCodeOffset, catchCode, throwable, "$e");

		InvokeAfterCodeGenerator afterGenerator = new InvokeAfterCodeGenerator(interceptorId, interceptorDefinition, declaringClass, this, localVarsInitialized, false);
		final String afterCode = afterGenerator.generate();
		// if (isDebug) {
		// logger.debug("addAfterInterceptor after behavior:{} code:{}",
		// behavior.getLongName(), afterCode);
		// }

		behavior.insertAfter(afterCode);
	}

	private void insertCatch(int from, String src, CtClass exceptionType, String exceptionName) throws CannotCompileException {
		CtClass cc = behavior.getDeclaringClass();
		ConstPool cp = behavior.getMethodInfo().getConstPool();
		CodeAttribute ca = behavior.getMethodInfo().getCodeAttribute();
		CodeIterator iterator = ca.iterator();
		Bytecode b = new Bytecode(cp, ca.getMaxStack(), ca.getMaxLocals());
		b.setStackDepth(1);
		Javac jv = new Javac(b, cc);
		try {
			jv.recordParams(behavior.getParameterTypes(), Modifier.isStatic(getModifiers()));
			jv.recordLocalVariables(ca, from);
			int var = jv.recordVariable(exceptionType, exceptionName);
			b.addAstore(var);
			jv.compileStmnt(src);

			int stack = b.getMaxStack();
			int locals = b.getMaxLocals();

			if (stack > ca.getMaxStack())
				ca.setMaxStack(stack);

			if (locals > ca.getMaxLocals())
				ca.setMaxLocals(locals);

			int len = iterator.getCodeLength();
			int pos = iterator.append(b.get());

			ca.getExceptionTable().add(from, len, len, cp.addClassInfo(exceptionType));
			iterator.append(b.getExceptionTable(), pos);
			behavior.getMethodInfo().rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
		} catch (NotFoundException e) {
			throw new CannotCompileException(e);
		} catch (CompileError e) {
			throw new CannotCompileException(e);
		} catch (BadBytecode e) {
			throw new CannotCompileException(e);
		}
	}

	private boolean isAfterInterceptor(CaptureType captureType) {
		return CaptureType.AFTER == captureType || CaptureType.AROUND == captureType;
	}

	private int addBeforeInterceptor(InterceptorDefinition interceptorDefinition, int interceptorId, int pos) throws CannotCompileException {
		final Class<?> interceptorClass = interceptorDefinition.getInterceptorClass();
		final CaptureType captureType = interceptorDefinition.getCaptureType();
		if (!isBeforeInterceptor(captureType)) {
			return -1;
		}
		final Method interceptorMethod = interceptorDefinition.getBeforeMethod();

		if (interceptorMethod == null) {
			// if (isDebug) {
			// logger.debug("Skip adding before interceptorDefinition because the interceptorDefinition doesn't have before method: {}",
			// interceptorClass.getName());
			// }
			return -1;
		}

		final InvokeBeforeCodeGenerator generator = new InvokeBeforeCodeGenerator(interceptorId, interceptorDefinition, declaringClass, this);
		final String beforeCode = generator.generate();

		return insertBefore(pos, beforeCode);
	}

	private boolean isBeforeInterceptor(CaptureType captureType) {
		return CaptureType.BEFORE == captureType || CaptureType.AROUND == captureType;
	}

	private int insertBefore(int pos, String src) throws CannotCompileException {
		if (isConstructor()) {
			return insertBeforeConstructor(pos, src);
		} else {
			return insertBeforeMethod(pos, src);
		}
	}

	private int insertBeforeMethod(int pos, String src) throws CannotCompileException {
		CtClass cc = behavior.getDeclaringClass();
		CodeAttribute ca = behavior.getMethodInfo().getCodeAttribute();
		if (ca == null)
			throw new CannotCompileException("no method body");

		CodeIterator iterator = ca.iterator();
		Javac jv = new Javac(cc);
		try {
			int nvars = jv.recordParams(behavior.getParameterTypes(), Modifier.isStatic(getModifiers()));
			jv.recordParamNames(ca, nvars);
			jv.recordLocalVariables(ca, 0);
			jv.recordType(getReturnType0());
			jv.compileStmnt(src);
			Bytecode b = jv.getBytecode();
			int stack = b.getMaxStack();
			int locals = b.getMaxLocals();

			if (stack > ca.getMaxStack())
				ca.setMaxStack(stack);

			if (locals > ca.getMaxLocals())
				ca.setMaxLocals(locals);

			if (pos != -1) {
				iterator.insertEx(pos, b.get());
			} else {
				pos = iterator.insertEx(b.get());
			}

			iterator.insert(b.getExceptionTable(), pos);
			behavior.getMethodInfo().rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());

			return pos + b.length();
		} catch (NotFoundException e) {
			throw new CannotCompileException(e);
		} catch (CompileError e) {
			throw new CannotCompileException(e);
		} catch (BadBytecode e) {
			throw new CannotCompileException(e);
		}
	}

	private CtClass getReturnType0() throws NotFoundException {
		return Descriptor.getReturnType(behavior.getMethodInfo().getDescriptor(), behavior.getDeclaringClass().getClassPool());
	}

	private int insertBeforeConstructor(int pos, String src) throws CannotCompileException {
		CtClass cc = behavior.getDeclaringClass();
		CodeAttribute ca = behavior.getMethodInfo().getCodeAttribute();
		CodeIterator iterator = ca.iterator();
		Bytecode b = new Bytecode(behavior.getMethodInfo().getConstPool(), ca.getMaxStack(), ca.getMaxLocals());
		b.setStackDepth(ca.getMaxStack());
		Javac jv = new Javac(b, cc);
		try {
			jv.recordParams(behavior.getParameterTypes(), false);
			jv.recordLocalVariables(ca, 0);
			jv.compileStmnt(src);
			ca.setMaxStack(b.getMaxStack());
			ca.setMaxLocals(b.getMaxLocals());
			iterator.skipConstructor();
			if (pos != -1) {
				iterator.insertEx(pos, b.get());
			} else {
				pos = iterator.insertEx(b.get());
			}
			iterator.insert(b.getExceptionTable(), pos);
			behavior.getMethodInfo().rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());

			return pos + b.length();
		} catch (NotFoundException e) {
			throw new CannotCompileException(e);
		} catch (CompileError e) {
			throw new CannotCompileException(e);
		} catch (BadBytecode e) {
			throw new CannotCompileException(e);
		}
	}

	private String initializeLocalVariable(int interceptorId) throws NotFoundException, CannotCompileException {
		final String interceptorInstanceVar = InvokeCodeGenerator.getInterceptorVar(interceptorId);
		addLocalVariable(interceptorInstanceVar, Interceptor.class);

		final StringBuilder initVars = new StringBuilder();
		initVars.append(interceptorInstanceVar);
		initVars.append(" = null;");
		return initVars.toString();
	}

	private void addLocalVariable(String name, Class<?> type) throws NotFoundException, CannotCompileException {
		final String interceptorClassName = type.getName();
		final CtClass interceptorCtClass = behavior.getDeclaringClass().getClassPool().get(interceptorClassName);
		behavior.addLocalVariable(name, interceptorCtClass);
	}

	private Interceptor createInterceptor(String interceptorClassName, Object[] constructorArgs) throws ClassNotFoundException {
		ClassLoader classLoader = declaringClass.getClassLoader();
		Interceptor interceptor = getInterceptor(classLoader, interceptorClassName, constructorArgs, declaringClass, this);
		return interceptor;
	}

	private Interceptor getInterceptor(ClassLoader classLoader, String interceptorClassName, Object[] constructorArgs, InstrumentClass target, InstrumentMethod targetMethod) throws ClassNotFoundException {
		// classLoader.
		// Class<?> interceptorType
		Class<?> interceptorType = Class.forName(interceptorClassName);
		InterceptorArgumentProvider argumentProvider = new InterceptorArgumentProvider(target, targetMethod);
		Interceptor interceptor = (Interceptor) createInstance(interceptorType, argumentProvider);
		return interceptor;
	}

	private Object createInstance(Class<?> type, InterceptorArgumentProvider argumentProvider) {
		ConstructorResolver resolver = new ConstructorResolver(type, argumentProvider);
		if (!resolver.resolve()) {
			throw new RuntimeException("Cannot find suitable constructor for " + type.getName());
		}
		final Constructor<?> constructor = resolver.getResolvedConstructor();
		final Object[] resolvedArguments = resolver.getResolvedArguments();
		try {
			return constructor.newInstance(resolvedArguments);
		} catch (Exception e) {
			throw new RuntimeException("Fail to invoke constructor: " + constructor + ", arguments: " + Arrays.toString(resolvedArguments), e);
		}
	}
}
