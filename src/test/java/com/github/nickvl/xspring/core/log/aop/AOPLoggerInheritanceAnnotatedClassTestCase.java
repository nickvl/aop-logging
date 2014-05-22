/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import javax.annotation.Resource;

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

import com.github.nickvl.xspring.core.log.aop.service.AuxBazService;
import com.github.nickvl.xspring.core.log.aop.service.BazService;
import com.github.nickvl.xspring.core.log.aop.service.GeneralBazService;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests check that two different classes of the same interface:
 * <ul>
 * <li>has independent log configurations,</li>
 * <li>a log annotation on a class does not apply for inherited methods</li>
 * </ul>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AOPLoggerInheritanceAnnotatedClassTestCase-context.xml")
@DirtiesContext
public class AOPLoggerInheritanceAnnotatedClassTestCase {

    private static final String[] G_PARAM_NAMES = new String[]{"gFirst", "gSecond"};
    private static final String[] X_PARAM_NAMES = new String[]{"xFirst", "xSecond"};
    private static final Object[] PARAM_VALUE = new Object[]{"@1", "@2"};
    @Autowired
    private AOPLogger aspect;

    @Resource(name = "generalBaz")
    private BazService bazService;

    @Resource(name = "auxBaz")
    private BazService auxBazService;

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
    public void testGeneralBazInImpl() throws Exception {
        expectSimpleBarServiceLogger(GeneralBazService.class);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("inImpl"), aryEq(PARAM_VALUE), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage("inImpl", 2, Void.TYPE)).andReturn("<");

        expectInfoLogging();

        EasyMock.replay(logAdapter, logger);
        bazService.inImpl("@1", "@2");
        assertParams(captured.getValue(), G_PARAM_NAMES, true, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testGeneralBazInAbstract() throws Exception {
        EasyMock.replay(logAdapter, logger);
        bazService.inAbstract("@1", "@2");
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testAuxBazInImpl() throws Exception {
        expectSimpleBarServiceLogger(AuxBazService.class);
        Capture<ArgumentDescriptor> captured = new Capture<ArgumentDescriptor>();
        EasyMock.expect(logAdapter.toMessage(eq("inImpl"), aryEq(PARAM_VALUE), capture(captured))).andReturn(">");
        EasyMock.expect(logAdapter.toMessage("inImpl", 2, Void.TYPE)).andReturn("<");

        expectDebugLogging();

        EasyMock.replay(logAdapter, logger);
        auxBazService.inImpl("@1", "@2");
        assertParams(captured.getValue(), X_PARAM_NAMES, true, true);
        EasyMock.verify(logAdapter, logger);
    }

    @Test
    public void testAuxBazInAbstract() throws Exception {
        EasyMock.replay(logAdapter, logger);
        auxBazService.inAbstract("@1", "@2");
        EasyMock.verify(logAdapter, logger);
    }


    private void expectSimpleBarServiceLogger(Class<?> clazz) {
        EasyMock.expect(logAdapter.getLog(clazz)).andReturn(logger);
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
}
