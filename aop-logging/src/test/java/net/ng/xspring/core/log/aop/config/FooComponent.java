/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.config;

import org.springframework.stereotype.Component;

import net.ng.xspring.core.log.aop.annotation.LogDebug;
import net.ng.xspring.core.log.aop.annotation.LogPoint;

/**
 * Simple component, does not implement any interface, has non public method.
 */
@Component
public class FooComponent {

    @LogDebug(LogPoint.OUT)
    void voidMethodZero() {
        // nothing to do
    }

    @LogDebug(LogPoint.OUT)
    public IntHolder intMethodZero() {
        return new IntHolder();
    }

}
