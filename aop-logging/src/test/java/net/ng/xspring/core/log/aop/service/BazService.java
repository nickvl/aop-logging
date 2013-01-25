/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.service;

/**
 * Baz service interface.
 */
public interface BazService {

    void inAbstract(String iFirst, String iSecond);

    void inImpl(String iFirst, String iSecond);
}
