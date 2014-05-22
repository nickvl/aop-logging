/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for logging specified exceptions thrown from a method (or all methods on a class).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Logging
public @interface LogException {
    /**
     * Specifies which exceptions should be logged with fatal message level.
     */
    Exc[] fatal() default {};

    /**
     * Specifies which exceptions should be logged with error message level.
     */
    Exc[] value() default @Exc(value = Exception.class, stacktrace = true);

    /**
     * Specifies which exceptions should be logged with warn message level.
     */
    Exc[] warn() default {};

    /**
     * Specifies which exceptions should be logged with info message level.
     */
    Exc[] info() default {};

    /**
     * Specifies which exceptions should be logged with debug message level.
     */
    Exc[] debug() default {};

    /**
     * Specifies which exceptions should be logged with trace message level.
     */
    Exc[] trace() default {};

    /**
     * Holds description exceptions to log.
     */
    public @interface Exc {
        /**
         * Array of exceptions to log.
         */
        Class<? extends Exception>[] value();

        /**
         * Flag if specified exceptions should be logged with stack trace.
         */
        boolean stacktrace() default false;
    }

}