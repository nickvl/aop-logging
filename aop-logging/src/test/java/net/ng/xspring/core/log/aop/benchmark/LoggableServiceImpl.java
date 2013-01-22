/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.benchmark;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.ng.xspring.core.log.aop.annotation.LogError;
import net.ng.xspring.core.log.aop.annotation.LogTrace;
import net.ng.xspring.core.log.aop.annotation.Lp;

/**
 */
@LogTrace
public class LoggableServiceImpl implements LoggableService {
    private static Log LOGGER = LogFactory.getLog(LoggableServiceImpl.class);

    @Override
    public int logClearMethod(String a, int b) {
        return b;
    }

    @Override
    public int logManualMethod(String a, int b) {
        LOGGER.isDebugEnabled();
        LOGGER.debug(null);

        LOGGER.isDebugEnabled();
        LOGGER.debug(null);
        return b;
    }

    @LogError
    @Override
    public int aopLogMethod(String a, @Lp int b) {
        return b;
    }
}
