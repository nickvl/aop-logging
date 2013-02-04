/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests a performance of string building by {@link net.ng.xspring.core.log.aop.UniversalLogAdapter}.
 */
public class UniversalLogAdapterPerformanceITCase {
    private static final int REPS = 100000;

    private UniversalLogAdapter logAdapter;

    @Before
    public void setUp() throws Exception {
        logAdapter = new UniversalLogAdapter(null);
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

}
