/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.annotation;

/**
 * Parts of the method to be logged.
 */
public enum LogPoint {
    /**
     * Refers to the method calling.
     */
    IN,

    /**
     * Refers to the returning from the method.
     */
    OUT,

    /**
     * {@link #IN} and {@link #OUT} together. But it has less priority then explicit {@link #IN}/{@link #OUT}.
     */
    BOTH
}
