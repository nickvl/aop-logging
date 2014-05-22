/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

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

import com.github.nickvl.xspring.core.log.aop.service.BarService;
import com.github.nickvl.xspring.core.log.aop.service.ExtendedBarService;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests check log configuration on an extended class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerInheritanceTestCase-context.xml")
@DirtiesContext
public class AOPLoggerInheritanceTestCase {

    private static final String[] A_PARAM_NAMES = new String[]{"aFirst", "aSecond"};
    private static final String[] E_PARAM_NAMES = new String[]{"eFirst", "eSecond"};
    private static final Object[] PARAM_VALUE = new Object[]{"@1", "@2"};
    @Autowired
    private AOPLogger aspect;

    @Autowired
    private BarService barService;

    private LogAdapter logAdapter;
    private Log logger;

    @Before
    public void setUp() throws Exception {
        logAdapter = EasyMock.createMock(LogAdapter.class);
        logger = EasyMock.createMock(Log.class);
        aspect.setLogAdapter(logAdapter);
        aspect.afterPropertiesSet();
    }

    private void expectExtendedBarServiceLogger() {
        EasyMock.expect(logAdapter.getLog(ExtendedBarService.class)).andReturn(logger);
    }

    private void expectInfoLogging() {
        EasyMock.expect(logger.isInfoEnabled()).andReturn(true);
        logger.info(">");
        EasyMock.expect(logger.isInfoEnabled()).andReturn(true);
        logger.info("<");
    }

    private void expectDebugLogging() {
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug(">");
        EasyMock.expect(logger.isDebugEnabled()).andReturn(true);
        logger.debug("<");
    }

    private void assertParams(ArgumentDescriptor descriptor, String[] names, boolean first, boolean second) {
        assertArrayEquals(names, descriptor.getNames());
        assertEquals(first, descriptor.isArgumentIndex(0));
        assertEquals(second, descriptor.isArgumentIndex(1));
    }

    @Test
    public void testInExtendedLogInSuperOnly() throws Exception {
        EasyMock.replay(logAdapter, logger);
        barService.inExtendedLogInSuperOnly("@1", "@2");
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testInAbstract() throws Exception {
        expectExtendedBarServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("inAbstract"), aryEq(PARAM_VALUE), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage("inAbstract", 2, Void.TYPE)).andReturn("<");

        expectInfoLogging();

        EasyMock.replay(logAdapter, logger);
        barService.inAbstract("@1", "@2");
        assertParams(captured.getValue(), A_PARAM_NAMES, true, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testInExtended() throws Exception {
        expectExtendedBarServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("inExtended"), aryEq(PARAM_VALUE), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage("inExtended", 2, Void.TYPE)).andReturn("<");

        expectDebugLogging();

        EasyMock.replay(logAdapter, logger);
        barService.inExtended("@1", "@2");
        assertParams(captured.getValue(), E_PARAM_NAMES, true, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testOoverridden() throws Exception {
        expectExtendedBarServiceLogger();
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("overridden"), aryEq(PARAM_VALUE), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage("overridden", 2, Void.TYPE)).andReturn("<");

        expectDebugLogging();

        EasyMock.replay(logAdapter, logger);
        barService.overridden("@1", "@2");
        assertParams(captured.getValue(), E_PARAM_NAMES, false, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testOverriddenLogInAbstractOnly() throws Exception {
        EasyMock.replay(logAdapter, logger);
        barService.overriddenLogInAbstractOnly("@1", "@2");
        EasyMock.verify(logAdapter, logger);
    }
}
