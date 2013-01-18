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

/**
 * Tests of logger performance.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerPerformanceITCase-context.xml")
@DirtiesContext
public class AOPLoggerPerformanceITCase {

    @Autowired
    private LoggableService loggableService;
    private static final int REPS = 100000;

    @Test
    public void testDellMe() throws Exception {
        int accum = loggableService.aopLogMethod("a", 16);
        out(0, 0, accum);
    }

    @Test
    public void testLogClearMethod() throws Exception {
        long start = System.nanoTime();
        int accum = 0;
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.logClearMethod("a", i % 16);
        }
        long end = System.nanoTime();
        out(start, end, accum);
    }

    @Test
    public void testLogManualMethod() throws Exception {
        long start = System.nanoTime();
        int accum = 0;
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.logManualMethod("a", i % 16);
        }
        long end = System.nanoTime();
        out(start, end, accum);
    }

    @Test
    public void testAopLogMethod() throws Exception {
        long start = System.nanoTime();
        int accum = 0;
        for (int i = 0; i < REPS; i++) {
            accum += loggableService.aopLogMethod("a", i % 16);
        }
        long end = System.nanoTime();
        out(start, end, accum);
    }

    private void out(long start, long end, int accum) {
        long executionTime = (end - start) / REPS;
        System.out.println("\t" + executionTime + " ns\t" + accum);
    }


}
