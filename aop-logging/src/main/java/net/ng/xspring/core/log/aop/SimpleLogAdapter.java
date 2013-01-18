/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple log adapter.
 */
public class SimpleLogAdapter implements LogAdapter {

    private static final String CALLING = "calling: ";
    private static final String RETURNING = "returning: ";
    private static final String THROWING = "throwing: ";

    @Override
    public Log getLog(Class clazz) {
        return LogFactory.getLog(clazz);
    }

    @Override
    public Log getLog(String name) {
        return LogFactory.getLog(name);
    }

    @Override
    public Object toMessage(String method, int argCount, Object[] arg, Object[] argNames) {
        if (argCount == 0) {
            return CALLING + method + "()";
        } else if (argCount == 1) {
            return CALLING + method + '(' + argNames[0] + '=' + arg[0] + ')';
        }
        StringBuilder buff = new StringBuilder(CALLING).append(method).append('(').append(argCount).append(" arguments: ");
        for (int i = 0; i < argNames.length; i++) {
            buff.append(argNames[i]).append('=').append(arg[i]);
        }
        buff.append(')');
        return buff.toString();
    }

    @Override
    public Object toMessage(String method, int argCount, Object result) {
        // TODO handel void result
        if (argCount == 0) {
            return RETURNING + method + "():" + String.valueOf(result);
        }
        return RETURNING + method + '(' + argCount + " arguments):" + String.valueOf(result);
    }

    @Override
    public Object toMessage(String method, int argCount, Exception e, boolean stackTrace) {
        if (argCount == 0) {
            return THROWING + method + "():" + e.getClass() + "=" + e.getMessage();
        }
        return THROWING + method + '(' + argCount + " arguments):" + e.getClass() + "=" + e.getMessage();
    }

}
