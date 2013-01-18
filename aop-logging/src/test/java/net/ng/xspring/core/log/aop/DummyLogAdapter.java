/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import org.apache.commons.logging.Log;

/**
 */
public class DummyLogAdapter implements LogAdapter {
    private static final Log log = new DummyLog();

    @Override
    public Log getLog(Class clazz) {
        return log;
    }

    @Override
    public Log getLog(String name) {
        return log;
    }

    @Override
    public Object toMessage(String method, int argCount, Object[] arg, Object[] argNames) {
        return null;
    }

    @Override
    public Object toMessage(String method, int argCount, Object result) {
        return null;
    }

    @Override
    public Object toMessage(String method, int argCount, Exception e, boolean stackTrace) {
        return null;
    }

    public static class DummyLog implements Log {

        @Override
        public boolean isDebugEnabled() {
            return true;
        }

        @Override
        public boolean isErrorEnabled() {
            return true;
        }

        @Override
        public boolean isFatalEnabled() {
            return true;
        }

        @Override
        public boolean isInfoEnabled() {
            return true;
        }

        @Override
        public boolean isTraceEnabled() {
            return true;
        }

        @Override
        public boolean isWarnEnabled() {
            return true;
        }

        @Override
        public void trace(Object message) {

        }

        @Override
        public void trace(Object message, Throwable t) {

        }

        @Override
        public void debug(Object message) {

        }

        @Override
        public void debug(Object message, Throwable t) {

        }

        @Override
        public void info(Object message) {

        }

        @Override
        public void info(Object message, Throwable t) {

        }

        @Override
        public void warn(Object message) {

        }

        @Override
        public void warn(Object message, Throwable t) {

        }

        @Override
        public void error(Object message) {

        }

        @Override
        public void error(Object message, Throwable t) {

        }

        @Override
        public void fatal(Object message) {

        }

        @Override
        public void fatal(Object message, Throwable t) {

        }
    }
}
