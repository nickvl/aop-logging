/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.benchmark;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Tests of logger performance.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerPerformanceITCase-context.xml")
@DirtiesContext
public class AOPLoggerPerformanceITCase {

    private static final int REPS = 100000;

    @Autowired
    private LoggableService loggableService;

    private void testLogClearMethod() throws Exception {
        int accum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.logClearMethod("a", i);
        }
        long end = System.nanoTime();
        out(start, end, accum, "no logging");
    }

    private void testLogManualMethod() throws Exception {
        int accum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.logManualMethod("a", i);
        }
        long end = System.nanoTime();
        out(start, end, accum, "direct logging");
    }

    private void testAopLogMethod() throws Exception {
        int accum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.aopLogMethod("a", i);
        }
        long end = System.nanoTime();
        out(start, end, accum, "aop logging");
    }

    @Test
    public void testSpeed() throws Exception {
        // warming up
        assertEquals(32, loggableService.logClearMethod("a", 31));
        assertEquals(33, loggableService.logManualMethod("b", 32));
        assertEquals(34, loggableService.aopLogMethod("c", 33));

        System.out.println("Service invocation benchmark (" + this.getClass().getSimpleName() + "):");
        testLogClearMethod();
        testLogManualMethod();
        testAopLogMethod();
    }

    private void out(long start, long end, int accum, String msg) {
        long executionTime = (end - start) / REPS;
        System.out.println("\t" + executionTime + " ns takes a method when " + msg + " is used");
    }

}
