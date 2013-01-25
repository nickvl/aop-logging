/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.service;

import net.ng.xspring.core.log.aop.annotation.LogDebug;

/**
 * Implements {@link BazService}.
 */
@LogDebug
public class AuxBazService extends AbstractBazService {

    @Override
    public void inImpl(String xFirst, String xSecond) {
        // nothing to do
    }
}
