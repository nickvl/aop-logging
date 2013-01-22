/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import org.apache.commons.logging.Log;

/**
 * Defines log strategies.
 */
abstract class LogStrategy {

    private final LogAdapter logAdapter;

    LogStrategy(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
    }

    protected LogAdapter getLogAdapter() {
        return logAdapter;
    }

    /**
     * If current strategy logging enabled.
     *
     * @param logger current logger
     * @return <code>true</code> if logging enabled, otherwise <code>false</code>
     */
    public abstract boolean isLogEnabled(Log logger);

    /**
     * Logs calling of the method.
     *
     * @param logger current logger
     * @param method method name
     * @param args arguments of the method
     * @param argumentDescriptor argument descriptor
     */
    public abstract void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor);

    /**
     * Logs returning from the method.
     *
     * @param logger current logger
     * @param method method name
     * @param argCount parameter count number of the method
     * @param result returned result of the method
     */
    public abstract void logAfter(Log logger, String method, int argCount, Object result);

    /**
     * Logs throwing exception from the method.
     *
     * @param logger current logger
     * @param method method name
     * @param argCount parameter count number of the method
     * @param e exception thrown from the method
     * @param stackTrace if stack trace should be logged
     */
    public abstract void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace);

    /**
     * Provides fatal strategy.
     */
    static final class FatalLogStrategy extends LogStrategy {

        FatalLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isFatalEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.fatal(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.fatal(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.fatal(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.fatal(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }
    }

    /**
     * Provides error strategy.
     */
    static final class ErrorLogStrategy extends LogStrategy {

        ErrorLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isErrorEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.error(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.error(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.error(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.error(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }

    }

    /**
     * Provides warn strategy.
     */
    static final class WarnLogStrategy extends LogStrategy {

        WarnLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isWarnEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.warn(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.warn(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.warn(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.warn(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }

    }

    /**
     * Provides info strategy.
     */
    static final class InfoLogStrategy extends LogStrategy {

        InfoLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isInfoEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.info(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.info(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.info(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.info(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }

    }

    /**
     * Provides debug strategy.
     */
    static final class DebugLogStrategy extends LogStrategy {

        DebugLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isDebugEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.debug(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.debug(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.debug(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.debug(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }

    }

    /**
     * Provides trace strategy.
     */
    static final class TraceLogStrategy extends LogStrategy {

        TraceLogStrategy(LogAdapter logAdapter) {
            super(logAdapter);
        }

        @Override
        public boolean isLogEnabled(Log logger) {
            return logger.isTraceEnabled();
        }

        @Override
        public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
            logger.trace(getLogAdapter().toMessage(method, args, argumentDescriptor));
        }

        @Override
        public void logAfter(Log logger, String method, int argCount, Object result) {
            logger.trace(getLogAdapter().toMessage(method, argCount, result));
        }

        @Override
        public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
            if (stackTrace) {
                logger.trace(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
            } else {
                logger.trace(getLogAdapter().toMessage(method, argCount, e, stackTrace));
            }
        }
    }

}
