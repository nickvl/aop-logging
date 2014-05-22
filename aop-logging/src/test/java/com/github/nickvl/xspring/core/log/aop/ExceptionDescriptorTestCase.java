/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.github.nickvl.xspring.core.log.aop.annotation.LogException;

import static com.github.nickvl.xspring.core.log.aop.TestSupportUtility.assertCollectionConsistOf;
import static com.github.nickvl.xspring.core.log.aop.TestSupportUtility.assertReflectionEquals;
import static com.github.nickvl.xspring.core.log.aop.annotation.LogException.Exc;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ExceptionDescriptor}.
 */
public class ExceptionDescriptorTestCase {

    private LogException currLogException;

    @Rule
    public MethodRule watchman = new TestWatchman() {
        public void starting(FrameworkMethod method) {
            Method currMethod = method.getMethod();
            currLogException = currMethod.getAnnotation(LogException.class);
        }
    };

    @Test(expected = NullPointerException.class)
    public void testNoAnnotation() throws Exception {
        new ExceptionDescriptor.Builder(null).build();
    }

    @Test
    @LogException
    public void testDefaultAnnotation() throws Exception {
        ExceptionDescriptor descriptor = new ExceptionDescriptor.Builder(currLogException).build();
        assertCollectionConsistOf(descriptor.getDefinedExceptions(), Exception.class);

        assertNull(descriptor.getExceptionSeverity(IllegalArgumentException.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.ERROR, true), descriptor.getExceptionSeverity(Exception.class));
    }

    @Test
    @LogException(value = @Exc(value = RuntimeException.class, stacktrace = true),
            info = {@Exc(NoMoneyException.class), @Exc(AccountBlockedException.class)},
            warn = {@Exc(AccountException.class), @Exc(value = IllegalArgumentException.class, stacktrace = true)})
    public void testCustomAnnotation() throws Exception {
        ExceptionDescriptor descriptor = new ExceptionDescriptor.Builder(currLogException).build();
        assertCollectionConsistOf(descriptor.getDefinedExceptions(),
                RuntimeException.class, NoMoneyException.class, AccountBlockedException.class, AccountException.class, IllegalArgumentException.class);

        assertNull(descriptor.getExceptionSeverity(Exception.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.ERROR, true), descriptor.getExceptionSeverity(RuntimeException.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.INFO, false), descriptor.getExceptionSeverity(NoMoneyException.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.INFO, false), descriptor.getExceptionSeverity(AccountBlockedException.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.WARN, false), descriptor.getExceptionSeverity(AccountException.class));
        assertReflectionEquals(ExceptionSeverity.create(Severity.WARN, true), descriptor.getExceptionSeverity(IllegalArgumentException.class));
    }

    @Test
    @LogException({})
    public void testEmptyAnnotation() throws Exception {
        ExceptionDescriptor descriptor = new ExceptionDescriptor.Builder(currLogException).build();
        assertTrue(descriptor.getDefinedExceptions().isEmpty());
    }

    private static class AccountException extends Exception {

    }

    private static class NoMoneyException extends AccountException {

    }

    private static class AccountBlockedException extends AccountException {

    }

}
