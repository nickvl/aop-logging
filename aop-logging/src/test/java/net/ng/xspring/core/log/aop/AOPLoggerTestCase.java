/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.ng.xspring.core.log.aop.service.FooService;
import net.ng.xspring.core.log.aop.service.SimpleFooService;

import static net.ng.xspring.core.log.aop.TestSupportUtility.arrayEqual;
import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests checks logging annotation parameters.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerTestCase-context.xml")
@DirtiesContext
public class AOPLoggerTestCase {

    @Autowired
    private AOPLogger aspect;

    @Autowired
    private FooService fooService;

    private LogAdapter logAdapter;
    private Log logger;

    @Before
    public void setUp() throws Exception {
        logAdapter = EasyMock.createMock(LogAdapter.class);
        logger = EasyMock.createMock(Log.class);
        aspect.setLogAdapter(logAdapter);
        aspect.afterPropertiesSet();
    }

    @Test
    public void testLogDebugBothVoidMethodZero() throws Exception {
        expectSimpleFooServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), aryEq(new Object[]{}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), eq(0), eq(Void.TYPE))).andReturn("<");

        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace("<");
        EasyMock.replay(logAdapter, logger);
        fooService.voidMethodZero();
        assertParams(captured.getValue(), null);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothVoidMethodOne() throws Exception {
        EasyMock.replay(logAdapter, logger);
        fooService.stringMethodOne("@1");
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodTwoLp() throws Exception {
        expectSimpleFooServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), aryEq(new Object[]{"@1", "@2"}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), eq(2), eq("stringMethodTwo:@1:@2"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = fooService.stringMethodTwo("@1", "@2");
        assertEquals("stringMethodTwo:@1:@2", res);
        assertParams(captured.getValue(), new String[]{"first", "second"}, false, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodThreeAll() throws Exception {
        expectSimpleFooServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), aryEq(new Object[]{"@1", "@2", "@3"}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), eq(3), eq("stringMethodThree:@1:@2:@3"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = fooService.stringMethodThree("@1", "@2", "@3");
        assertEquals("stringMethodThree:@1:@2:@3", res);
        assertParams(captured.getValue(), new String[]{"first", "second", "third"}, true, true, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogTraceBothStringMethodTwoVarargsLp() throws Exception {
        expectSimpleFooServiceLogger();
        String[] secondArgValue = {"@2-1", "@2-2"};
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), arrayEqual(new Object[]{"@1", new String[]{"@2-1", "@2-2"}}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), eq(2), eq("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue)))).andReturn("<");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace(">");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace("<");
        EasyMock.replay(logAdapter, logger);
        String res = fooService.stringMethodTwoVarargs("@1", "@2-1", "@2-2");
        assertEquals("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue), res);
        assertParams(captured.getValue(), new String[]{"first", "second"}, false, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothVoidExcMethodZero() throws Exception {
        expectSimpleFooServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("voidExcMethodZero"), aryEq(new Object[]{}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("voidExcMethodZero"), eq(0), EasyMock.anyObject(IOException.class), eq(false))).andReturn("io thrown");

        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isWarnEnabled()).andReturn(true);
        logger.warn("io thrown");
        EasyMock.replay(logAdapter, logger);
        try {
            fooService.voidExcMethodZero();
            fail("IOException is expected");
        } catch (IOException e) {
            assertEquals("io fail", e.getMessage());
        }
        assertParams(captured.getValue(), null);
        EasyMock.verify(logAdapter, logger);
    }

    private void expectSimpleFooServiceLogger() {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
    }

    private void assertParams(ArgumentDescriptor descriptor, String[] names, boolean... indexes) {
        assertArrayEquals(names, descriptor.getNames());
        for (int i = 0; i < indexes.length; i++) {
            assertEquals(indexes[i], descriptor.isArgumentIndex(i));

        }
        assertEquals(-1, descriptor.nextArgumentIndex(indexes.length));
    }

}
