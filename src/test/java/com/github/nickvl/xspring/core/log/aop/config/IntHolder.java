/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.config;

/**
 * Dummy integer holder. {@link String#toString()} is not overridden.
 */
public class IntHolder {
    private int i = 1;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
