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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static net.ng.xspring.core.log.aop.TestSupportUtility.arrayEqual;
import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerTestCase-context.xml")
@DirtiesContext
public class AOPLoggerTestCase {

    @Autowired
    private AOPLogger aspect;

    @Autowired
    @Qualifier("simpleFooService")
    private FooService whoFooService;

//    @Autowired
//    @Qualifier("peterJobFooService")
//    private FooService peterJobFooService;

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
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), aryEq(new Object[]{}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), eq(0), isNull())).andReturn("<");

        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        whoFooService.voidMethodZero();
        assertNull(captured.getValue().getNames());
        assertEquals(-1, captured.getValue().nextArgumentIndex(0));
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodTwoLp() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), aryEq(new Object[]{"@1", "@2"}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), eq(2), eq("stringMethodTwo:@1:@2"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodTwo("@1", "@2");
        assertEquals("stringMethodTwo:@1:@2", res);
        assertArrayEquals(new String[]{"first", "second"}, captured.getValue().getNames());
        assertEquals(1, captured.getValue().nextArgumentIndex(0));
        assertEquals(1, captured.getValue().nextArgumentIndex(1));
        assertEquals(-1, captured.getValue().nextArgumentIndex(2));
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodThreeAll() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), aryEq(new Object[]{"@1", "@2", "@3"}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), eq(3), eq("stringMethodThree:@1:@2:@3"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodThree("@1", "@2", "@3");
        assertEquals("stringMethodThree:@1:@2:@3", res);
        assertArrayEquals(new String[]{"first", "second", "third"}, captured.getValue().getNames());
        assertEquals(0, captured.getValue().nextArgumentIndex(0));
        assertEquals(1, captured.getValue().nextArgumentIndex(1));
        assertEquals(2, captured.getValue().nextArgumentIndex(2));
        assertEquals(-1, captured.getValue().nextArgumentIndex(3));
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogTraceBothStringMethodTwoVarargsLp() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        String[] secondArgValue = {"@2-1", "@2-2"};
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), arrayEqual(new Object[]{"@1", new String[]{"@2-1", "@2-2"}}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), eq(2), eq("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue)))).andReturn("<");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace(">");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodTwoVarargs("@1", "@2-1", "@2-2");
        assertEquals("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue), res);
        assertArrayEquals(new String[]{"first", "second"}, captured.getValue().getNames());
        assertEquals(1, captured.getValue().nextArgumentIndex(0));
        assertEquals(1, captured.getValue().nextArgumentIndex(1));
        assertEquals(-1, captured.getValue().nextArgumentIndex(2));

        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothVoidExcMethodZero() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("voidExcMethodZero"), aryEq(new Object[]{}), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("voidExcMethodZero"), eq(0), EasyMock.anyObject(IOException.class), eq(false))).andReturn("io thrown");

        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isWarnEnabled()).andReturn(true);
        logger.warn("io thrown");
        EasyMock.replay(logAdapter, logger);
        try {
            whoFooService.voidExcMethodZero();
            fail("IOException is expected");
        } catch (IOException e) {
            assertEquals("io fail", e.getMessage());
        }
        assertNull(captured.getValue().getNames());
        assertEquals(-1, captured.getValue().nextArgumentIndex(0));
        EasyMock.verify(logAdapter, logger);
    }

}
