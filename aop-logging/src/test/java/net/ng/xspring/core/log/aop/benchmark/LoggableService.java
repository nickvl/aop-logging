/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.benchmark;

/**
 */
public interface LoggableService {

    int logClearMethod(String a, int b);

    int logManualMethod(String a, int b);

    int aopLogMethod(String a, int b);
}
