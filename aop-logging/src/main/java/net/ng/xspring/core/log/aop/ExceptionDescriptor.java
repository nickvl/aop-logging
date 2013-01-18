/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.ng.xspring.core.log.aop.annotation.LogException;

/**
 * Method exceptions descriptor.
 */
final class ExceptionDescriptor {

    private final Map<Class<? extends Exception>, ExceptionSeverity> exceptionSeverity;
    private final Severity maxSeverity;

    private ExceptionDescriptor(Map<Class<? extends Exception>, ExceptionSeverity> exceptionSeverity) {
        this.exceptionSeverity = exceptionSeverity;
        Severity greatest = null;
        for (ExceptionSeverity severity : exceptionSeverity.values()) {
            greatest = Helper.max(greatest, severity.getSeverity());
        }
        maxSeverity = greatest;
    }

    public Collection<Class<? extends Exception>> getDefinedExceptions() {
        return exceptionSeverity.keySet();
    }

    public ExceptionSeverity getExceptionSeverity(Class<? extends Exception> resolvedException) {
        return exceptionSeverity.get(resolvedException);
    }

    public Severity getSeverity() {
        return maxSeverity;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final LogException exceptionAnnotation;
        private final Map<Class<? extends Exception>, ExceptionSeverity> map = new HashMap<Class<? extends Exception>, ExceptionSeverity>();

        public Builder(LogException exceptionAnnotation) {
            this.exceptionAnnotation = exceptionAnnotation;
        }

        public ExceptionDescriptor build() {
            setSeverity(exceptionAnnotation.fatal(), Severity.FATAL);
            setSeverity(exceptionAnnotation.value(), Severity.ERROR);
            setSeverity(exceptionAnnotation.warn(), Severity.WARN);
            setSeverity(exceptionAnnotation.info(), Severity.INFO);
            setSeverity(exceptionAnnotation.debug(), Severity.DEBUG);
            setSeverity(exceptionAnnotation.trace(), Severity.TRACE);
            return new ExceptionDescriptor(map);
        }

        private void setSeverity(LogException.Exc[] exceptionGroups, Severity targetSeverity) {
            for (LogException.Exc exceptions : exceptionGroups) {
                for (Class<? extends Exception> clazz : exceptions.value()) {
                    ExceptionSeverity descriptor = map.get(clazz);
                    if (descriptor == null || Helper.greater(targetSeverity, descriptor.getSeverity())) {
                        map.put(clazz, new ExceptionSeverity(targetSeverity, exceptions.stacktrace()));
                    }
                }
            }

        }

    }


}
