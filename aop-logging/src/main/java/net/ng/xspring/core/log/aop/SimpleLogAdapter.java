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
    public Object toMessage(String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
        if (args.length == 0) {
            return CALLING + method + "()";
        } else if (args.length == 1) {
            return CALLING + method + '(' + argumentDescriptor.getNames()[0] + '=' + args[0] + ')';
        }
        StringBuilder buff = new StringBuilder(CALLING).append(method).append('(').append(args.length).append(" arguments: ");
        for (int i = 0; i < args.length; i++) {
            if (argumentDescriptor.isArgumentIndex(i)) {
                buff.append(argumentDescriptor.getNames()[i]).append('=').append(args[i]);
            }
        }
/*
            Object[] lpArgs = new Object[lpParameters.cardinality()];
            String[] lpArgNames = argNames == null ? null : new String[lpArgs.length];
            int logArgIndex = 0;
            for (int i = lpParameters.nextSetBit(0); i >= 0; i = lpParameters.nextSetBit(i + 1)) {
                lpArgs[logArgIndex] = arguments[i];
                if (lpArgNames != null) {
                    lpArgNames[logArgIndex] = argNames[i];
                }
                logArgIndex++;
            }
            return new ArgumentDescriptor(loggedValueIndexes, lpArgNames);
*/
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
