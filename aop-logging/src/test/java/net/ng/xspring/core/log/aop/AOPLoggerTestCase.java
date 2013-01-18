/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerTestCase-context.xml")
@DirtiesContext
public class AOPLoggerTestCase {

    private static final Comparator<Object[]> ARRAY_EQUAL_COMPARATOR = new Comparator<Object[]>() {
        @Override
        public int compare(Object[] o1, Object[] o2) {
            Assert.assertArrayEquals(o1, o2);
            return 0;
        }
    };

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
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), eq(0), aryEq(new Object[]{}), EasyMock.<Object[]>isNull())).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("voidMethodZero"), eq(0), isNull())).andReturn("<");

        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        whoFooService.voidMethodZero();
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodTwoLp() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), eq(2), aryEq(new Object[]{"@2"}), aryEq(new Object[]{"second"}))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwo"), eq(2), eq("stringMethodTwo:@1:@2"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodTwo("@1", "@2");
        assertEquals("stringMethodTwo:@1:@2", res);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothStringMethodThreeAll() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), eq(3), aryEq(new Object[]{"@1", "@2", "@3"}), aryEq(new Object[]{"first", "second", "third"}))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodThree"), eq(3), eq("stringMethodThree:@1:@2:@3"))).andReturn("<");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodThree("@1", "@2", "@3");
        assertEquals("stringMethodThree:@1:@2:@3", res);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogTraceBothStringMethodTwoVarargsLp() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        String[] secondArgValue = {"@2-1", "@2-2"};
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), eq(2), arrayEqual(secondArgValue), aryEq(new Object[]{"second"}))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage(eq("stringMethodTwoVarargs"), eq(2), eq("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue)))).andReturn("<");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace(">");
        EasyMock.expect(logger.isTraceEnabled()).andReturn(true);
        logger.trace("<");
        EasyMock.replay(logAdapter, logger);
        String res = whoFooService.stringMethodTwoVarargs("@1", "@2-1", "@2-2");
        assertEquals("stringMethodTwoVarargs:@1:" + Arrays.toString(secondArgValue), res);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testLogDebugBothVoidExcMethodZero() throws Exception {
        EasyMock.expect(logAdapter.getLog(SimpleFooService.class)).andReturn(logger);
        EasyMock.expect(logAdapter.toMessage(eq("voidExcMethodZero"), eq(0), aryEq(new Object[]{}), EasyMock.<Object[]>isNull())).andReturn(">");
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
        EasyMock.verify(logAdapter, logger);
    }

    private Object[] arrayEqual(String[] expectedArray) {
        return EasyMock.cmp(new Object[]{expectedArray}, ARRAY_EQUAL_COMPARATOR, LogicalOperator.EQUAL);
    }

}
