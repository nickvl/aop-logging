/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.io.IOException;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import static net.ng.xspring.core.log.aop.TestSupportUtility.createArgumentDescriptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link AbstractLogAdapter}.
 */
public class AbstractLogAdapterTestCase {

    private LogAdapter logAdapter;

    @Before
    public void setUp() throws Exception {
        logAdapter = new AbstractLogAdapter() {
            @Override
            protected String asString(Object value) {
                return String.valueOf(value);
            }
        };
    }

    @Test
    public void testGetLogByClass() throws Exception {
        assertNotNull(logAdapter.getLog(this.getClass()));
    }

    @Test
    public void testGetLogByName() throws Exception {
        assertNotNull(logAdapter.getLog("custom-logger"));
    }

    @Test
    public void testToMessageBeforeNoArgs() throws Exception {
        ArgumentDescriptor descriptor = createArgumentDescriptor(new BitSet(), null);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{}, descriptor);
        assertEquals("calling: fooMethod()", message);
    }

    @Test
    public void testToMessageBeforeOneArg() throws Exception {
        String[] arNames = {"first"};
        ArgumentDescriptor descriptor = createArgumentDescriptor(arNames, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1"}, descriptor);
        assertEquals("calling: fooMethod(first=v1)", message);
    }

    @Test
    public void testToMessageBeforeOneArgNoName() throws Exception {
        ArgumentDescriptor descriptor = createArgumentDescriptor(null, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1"}, descriptor);
        assertEquals("calling: fooMethod(v1)", message);
    }

    @Test
    public void testToMessageBeforeSomeArgs() throws Exception {
        String[] arNames = {"first", "second"};
        ArgumentDescriptor descriptor = createArgumentDescriptor(arNames, false, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
        assertEquals("calling: fooMethod(2 arguments: second=v2)", message);
    }

    @Test
    public void testToMessageBeforeSomeArgsNoNames() throws Exception {
        ArgumentDescriptor descriptor = createArgumentDescriptor(null, false, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
        assertEquals("calling: fooMethod(2 arguments: ?, v2)", message);
    }

    @Test
    public void testToMessageBeforeAllArgs() throws Exception {
        String[] arNames = {"first", "second"};
        ArgumentDescriptor descriptor = createArgumentDescriptor(arNames, true, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
        assertEquals("calling: fooMethod(2 arguments: first=v1, second=v2)", message);
    }

    @Test
    public void testToMessageBeforeAllArgsNoNames() throws Exception {
        ArgumentDescriptor descriptor = createArgumentDescriptor(null, true, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
        assertEquals("calling: fooMethod(2 arguments: v1, v2)", message);
    }

    @Test
    public void testToMessageAfterNoArgs() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 0, "res");
        assertEquals("returning: fooMethod():res", message);
    }

    @Test
    public void testToMessageAfterNoArgsVoid() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 0, Void.TYPE);
        assertEquals("returning: fooMethod():void", message);
    }

    @Test
    public void testToMessageAfter() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, "res");
        assertEquals("returning: fooMethod(2 arguments):res", message);
    }

    @Test
    public void testToMessageAfterVoid() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, Void.TYPE);
        assertEquals("returning: fooMethod(2 arguments):void", message);
    }

    @Test
    public void testToMessageExceptionNoArgs() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 0, new IOException("storage disconnected"), false);
        assertEquals("throwing: fooMethod():class java.io.IOException=storage disconnected", message);
    }

    @Test
    public void testToMessageException() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, new IllegalArgumentException("second is negative"), false);
        assertEquals("throwing: fooMethod(2 arguments):class java.lang.IllegalArgumentException=second is negative", message);
    }

    @Test
    public void testToMessageExceptionNullMessage() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, new Exception(), false);
        assertEquals("throwing: fooMethod(2 arguments):class java.lang.Exception", message);
    }
}