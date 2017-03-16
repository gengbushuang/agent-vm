package com.gbs.plugin.user;

public class UserIncludeMethodDescriptor implements MethodDescriptor {
	private int apiId = 7;
	private int type = 100;

	@Override
	public String getMethodName() {
		return "";
	}

	@Override
	public String getClassName() {
		return "";
	}

	@Override
	public String[] getParameterTypes() {
		return null;
	}

	@Override
	public String[] getParameterVariableName() {
		return null;
	}

	@Override
	public String getParameterDescriptor() {
		return "()";
	}

	@Override
	public int getLineNumber() {
		return -1;
	}

	@Override
	public String getFullName() {
		return UserIncludeMethodDescriptor.class.getName();
	}

	@Override
	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	@Override
	public int getApiId() {
		return apiId;
	}

	@Override
	public String getApiDescriptor() {
		return "Entry Point Process";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}