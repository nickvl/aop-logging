/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.service;

import net.ng.xspring.core.log.aop.annotation.LogInfo;

/**
 * Implements {@link BazService}.
 */
@LogInfo
public class GeneralBazService extends AbstractBazService {

    @Override
    public void inImpl(String gFirst, String gSecond) {
        // nothing to do
    }
}
