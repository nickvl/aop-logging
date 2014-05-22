/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.util.ArrayList;

import org.junit.Test;

import static com.github.nickvl.xspring.core.log.aop.ToStringDetector.INSTANCE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ToStringDetector}.
 */
public class ToStringDetectorTestCase {

    @Test
    public void testHasToString() throws Exception {
        assertFalse(INSTANCE.hasToString(Object.class));
        assertFalse(INSTANCE.hasToString(this.getClass()));
        assertTrue(INSTANCE.hasToString(Integer.class));
        assertTrue(INSTANCE.hasToString(ArrayList.class));
    }

    @Test
    public void testInterfaceHasToString() throws Exception {
        assertTrue(INSTANCE.hasToString(Foo.class));
        Foo foo = new Foo() {
        };
        assertFalse(INSTANCE.hasToString(foo.getClass()));
    }

    private static interface Foo {
        @Override
        String toString();
    }
}
