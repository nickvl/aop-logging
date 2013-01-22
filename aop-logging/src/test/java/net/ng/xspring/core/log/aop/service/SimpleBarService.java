/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.service;

import net.ng.xspring.core.log.aop.annotation.LogDebug;
import net.ng.xspring.core.log.aop.annotation.Lp;

/**
 * Implements {@link BarService}. Log level is lower than in {@link BarService}.
 */
public class SimpleBarService extends AbstractBarService {

    @Override
    public void inSimpleLogInSuperOnly(String sFirst, String sSecond) {
        // Log annotations in interface
    }

    @LogDebug
    @Override
    public void inSimple(String sFirst, String sSecond) {
        // Log annotation here only
    }

    @LogDebug
    @Override
    public void overridden(String sFirst, @Lp String sSecond) {
        // Log annotation altered
    }

    @Override
    public void overriddenLogInAbstractOnly(String sFirst, String sSecond) {
        // Log annotation in parent and not altered here
    }
}
