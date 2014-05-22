/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogPoint;
import com.github.nickvl.xspring.core.log.aop.annotation.LogTrace;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests {@link com.github.nickvl.xspring.core.log.aop.InvocationDescriptor} with log annotated methods and class.
 */
@LogInfo
@LogDebug(LogPoint.OUT)
@LogException
public class InvocationDescriptorClassTestCase {

    private Method currMethod;

    @Rule
    public MethodRule watchman = new TestWatchman() {
        public void starting(FrameworkMethod method) {
            currMethod = method.getMethod();
        }
    };

    @After
    public void tearDown() throws Exception {
        currMethod = null;
    }

    @Test
    public void testNoAnnotations() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(Severity.INFO, descriptor.getBeforeSeverity());
        assertSame(Severity.DEBUG, descriptor.getAfterSeverity());
        assertNotNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogTrace
    public void testGetSeverityByMethodPriority() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(Severity.TRACE, descriptor.getBeforeSeverity());
        assertSame(Severity.TRACE, descriptor.getAfterSeverity());
        assertNotNull(descriptor.getExceptionAnnotation());
    }


    @Test
    @LogException(value = {}, trace = @LogException.Exc(Exception.class))
    public void testGetExceptionAnnotationByMethodPriority() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(Severity.INFO, descriptor.getBeforeSeverity());
        assertSame(Severity.DEBUG, descriptor.getAfterSeverity());
        LogException exceptionAnnotation = descriptor.getExceptionAnnotation();
        assertEquals(0, exceptionAnnotation.value().length);
        assertEquals(1, exceptionAnnotation.trace().length);
        assertArrayEquals(exceptionAnnotation.trace()[0].value(), new Object[]{Exception.class});
        assertFalse(exceptionAnnotation.trace()[0].stacktrace());
    }
}
