/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.benchmark;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.nickvl.xspring.core.log.aop.UniversalLogAdapter;

import static org.junit.Assert.assertEquals;

/**
 * Tests a performance of string building by {@link com.github.nickvl.xspring.core.log.aop.UniversalLogAdapter}.
 */
public class UniversalLogAdapterPerformanceITCase {
    private static final int REPS = 100000;

    private UniversalLogAdapterPublic logAdapter;

    @Before
    public void setUp() throws Exception {
        logAdapter = new UniversalLogAdapterPublic(null);
    }

    @Test
    public void testSpeed() throws Exception {
        System.out.println("Building toString value benchmark (" + this.getClass().getSimpleName() + "):");
        testAsStringReflectionBenchmark();
        testAsStringToStringBenchmark();
    }

    public void testAsStringReflectionBenchmark() throws Exception {
        class Int {
            private int i = 1;
        }

        logAdapter.asString(new Int()); //warming up
        long start = System.nanoTime();
        for (int i = 0; i < REPS; i++) {
            assertEquals("Int[i=1]", logAdapter.asString(new Int()));
            // ToStringBuilder.reflectionToString(new Int());
        }
        long end = System.nanoTime();

        // test measures different aspects: instanceof and so on
        out(start, end, "reflection");
    }

    public void testAsStringToStringBenchmark() throws Exception {
        class Int {
            @Override
            public String toString() {
                return "Int[i=1]";
            }
        }

        logAdapter.asString(new Int()); //warming up
        long start = System.nanoTime();
        for (int i = 0; i < REPS; i++) {
            assertEquals("Int[i=1]", logAdapter.asString(new Int()));
        }
        long end = System.nanoTime();

        out(start, end, "overridden toString");
    }

    private void out(long start, long end, String msg) {
        long executionTime = (end - start) / REPS;
        System.out.println("\t" + executionTime + " ns takes a method when " + msg + " is used");
    }

    private static class UniversalLogAdapterPublic extends UniversalLogAdapter {
        public UniversalLogAdapterPublic(Set<String> excludeFieldNames) {
            super(false, excludeFieldNames);
        }

        @Override
        public String asString(Object value) {
            return super.asString(value);
        }
    }
}
