/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.service;

import net.ng.xspring.core.log.aop.annotation.LogWarn;

/**
 * Service interface.
 */
public interface BarService {

    @LogWarn
    void inSimpleLogInSuperOnly(String iFirst, String iSecond);

    void inAbstract(String iFirst, String iSecond);

    void inSimple(String iFirst, String iSecond);

    void overridden(String iFirst, String iSecond);

    void overriddenLogInAbstractOnly(String iFirst, String iSecond);
}
