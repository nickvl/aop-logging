/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.config;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.nickvl.xspring.core.log.StubAppender;
import com.github.nickvl.xspring.core.log.aop.SimpleLogAdapter;

import static org.junit.Assert.assertEquals;

/**
 * Tests configuaration with {@link SimpleLogAdapter}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("ConfigITCase-context.xml")
@DirtiesContext
public class ConfigITCase {

    @Autowired
    private FooComponent fooComponent;

    @Before
    public void setUp() throws Exception {
        Properties logProperties = new Properties();
        logProperties.setProperty("log4j.appender.null", "com.github.nickvl.xspring.core.log.StubAppender");
        logProperties.setProperty("log4j.rootLogger", "FATAL, null");
        logProperties.setProperty("log4j.logger.com.github.nickvl", "TRACE");
        PropertyConfigurator.configure(logProperties);
    }

    @After
    public void tearDown() throws Exception {
        StubAppender.clear();
        LogManager.shutdown();
    }

    @Test
    public void testNotPublicCalling() throws Exception {
        fooComponent.voidMethodZero();
        List<String> list = StubAppender.clear();
        assertEquals(1, list.size());
        assertEquals("returning: voidMethodZero():void", list.get(0));
    }

    @Test
    public void testReturnValue() throws Exception {
        IntHolder intHolder = fooComponent.intMethodZero();
        List<String> list = StubAppender.clear();
        assertEquals(1, list.size());
        assertEquals("returning: intMethodZero():" + new AsString().asString(intHolder), list.get(0));
    }

    private static class AsString extends SimpleLogAdapter {

        @Override
        public String asString(Object value) {
            return super.asString(value);
        }
    }
}
