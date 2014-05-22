/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

/**
 * Resolves exceptions.
 */
class ExceptionResolver {

    public Class<? extends Exception> resolve(ExceptionDescriptor descriptor, Exception ex) {
        Class<? extends Exception> resolved = null;
        int deepest = Integer.MAX_VALUE;
        for (Class<? extends Exception> exceptionMapping : descriptor.getDefinedExceptions()) {
            int depth = getDepth(exceptionMapping, ex);
            if (depth >= 0 && depth < deepest) {
                deepest = depth;
                resolved = exceptionMapping;
            }
        }
        return resolved;
    }

    private int getDepth(Class<? extends Exception> exceptionMapping, Exception ex) {
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    @SuppressWarnings("unchecked")
    private int getDepth(Class<? extends Exception> exceptionMapping, Class<? extends Exception> exceptionClass, int depth) {
        if (exceptionClass.equals(exceptionMapping)) {
            return depth;
        }
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(exceptionMapping, (Class<? extends Exception>) exceptionClass.getSuperclass(), depth + 1);
    }

}
