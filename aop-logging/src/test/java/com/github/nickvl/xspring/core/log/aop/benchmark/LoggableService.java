/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.benchmark;

/**
 * Service which methods performs different logging strategies.
 */
public interface LoggableService {

    int logClearMethod(String a, int b);

    int logManualMethod(String a, int b);

    int aopLogMethod(String a, int b);
}
