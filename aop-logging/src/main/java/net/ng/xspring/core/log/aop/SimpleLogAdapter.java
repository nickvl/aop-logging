/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

/**
 * Simple log adapter.
 */
class SimpleLogAdapter extends AbstractLogAdapter {

    @Override
    protected String asString(Object value) {
        return String.valueOf(value);
    }
}
