package com.gbs.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    /**
     * scope name
     */
    String value();
    
    /**
     * specify when this interceptor have to be invoked.
     */
    ExecutionPolicy executionPolicy() default ExecutionPolicy.BOUNDARY;
}