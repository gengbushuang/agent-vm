package com.gbs.plugin;

import java.util.Arrays;

import com.gbs.util.ApiUtils;

public class DefaultMethodDescriptor implements MethodDescriptor {
    private String className;

    private String methodName;

    private String[] parameterTypes;

    private String[] parameterVariableName;


    private String parameterDescriptor;

    private String apiDescriptor;

    private int lineNumber;

    private int apiId = 0;

    private String fullName;
    
    private int type = 0;

    public DefaultMethodDescriptor() {
    }
    //className 类名
    //methodName 方法名
    //parameterTypes 方法传参的类型
    //parameterVariableName 方法传参的变量
    //parameterDescriptor 方法的括号描述，例如(String className, String methodName, String[] parameterTypes, String[] parameterVariableName)
    public DefaultMethodDescriptor(String className, String methodName, String[] parameterTypes, String[] parameterVariableName) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterVariableName = parameterVariableName;
        this.parameterDescriptor = ApiUtils.mergeParameterVariableNameDescription(parameterTypes, parameterVariableName);
        this.apiDescriptor = ApiUtils.mergeApiDescriptor(className, methodName, parameterDescriptor);
    }

    public String getParameterDescriptor() {
        return parameterDescriptor;
    }

    public void setParameterDescriptor(String parameterDescriptor) {
        this.parameterDescriptor = parameterDescriptor;
    }


    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameterVariableName(String[] parameterVariableName) {
        this.parameterVariableName = parameterVariableName;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public String[] getParameterVariableName() {
        return parameterVariableName;
    }


    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getFullName() {
        if (fullName != null) {
            return fullName;
        }
        StringBuilder buffer = new StringBuilder(256);
        buffer.append(className);
        buffer.append(".");
        buffer.append(methodName);
        buffer.append(parameterDescriptor);
        if (lineNumber != -1) {
            buffer.append(":");
            buffer.append(lineNumber);
        }
        fullName = buffer.toString();
        return fullName;
    }

    public void setApiDescriptor(String apiDescriptor) {
        this.apiDescriptor = apiDescriptor;
    }

    @Override
    public String getApiDescriptor() {
        return apiDescriptor;
    }

    @Override
    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    @Override
    public int getApiId() {
        return apiId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{className=");
        builder.append(className);
        builder.append(", methodName=");
        builder.append(methodName);
        builder.append(", parameterTypes=");
        builder.append(Arrays.toString(parameterTypes));
        builder.append(", parameterVariableName=");
		builder.append(Arrays.toString(parameterVariableName));
        builder.append(", parameterDescriptor=");
        builder.append(parameterDescriptor);
        builder.append(", apiDescriptor=");
        builder.append(apiDescriptor);
        builder.append(", lineNumber=");
        builder.append(lineNumber);
        builder.append(", apiId=");
        builder.append(apiId);
        builder.append(", fullName=");
        builder.append(fullName);
        builder.append(", type=");
        builder.append(type);
        builder.append("}");
        return builder.toString();
    }
}