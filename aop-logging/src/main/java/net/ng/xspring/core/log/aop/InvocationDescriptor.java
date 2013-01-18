/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.ng.xspring.core.log.aop.annotation.LogDebug;
import net.ng.xspring.core.log.aop.annotation.LogError;
import net.ng.xspring.core.log.aop.annotation.LogException;
import net.ng.xspring.core.log.aop.annotation.LogFatal;
import net.ng.xspring.core.log.aop.annotation.LogInfo;
import net.ng.xspring.core.log.aop.annotation.LogPoint;
import net.ng.xspring.core.log.aop.annotation.LogTrace;
import net.ng.xspring.core.log.aop.annotation.LogWarn;

/**
 * Method descriptor.
 */
final class InvocationDescriptor {
    private final Severity beforeSeverity;
    private final Severity afterSeverity;
    private final LogException exceptionAnnotation;
    private final Severity maxSeverity; // TODO probably is not needed

    private InvocationDescriptor(Severity beforeSeverity, Severity afterSeverity, LogException exceptionAnnotation) {
        this.beforeSeverity = beforeSeverity;
        this.afterSeverity = afterSeverity;
        this.exceptionAnnotation = exceptionAnnotation;

        maxSeverity = Helper.max(beforeSeverity, afterSeverity);
    }

    public Severity getBeforeSeverity() {
        return beforeSeverity;
    }

    public Severity getAfterSeverity() {
        return afterSeverity;
    }

    public LogException getExceptionAnnotation() {
        return exceptionAnnotation;
    }

    public Severity getSeverity() {
        return maxSeverity;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final Method method;
        private Severity beforeSeverity;
        private Severity afterSeverity;
        private Severity defaultSeverity;
        private Severity classBeforeSeverity;
        private Severity classAfterSeverity;
        private Severity classDefaultSeverity;

        public Builder(Method method) {
            this.method = method;
        }

        public InvocationDescriptor build() {
            LogException logMethodExceptionAnnotation = parseAnnotations(method.getAnnotations(), true);
            LogException logClassExceptionAnnotation = parseAnnotations(method.getDeclaringClass().getAnnotations(), false);

            if (Helper.hasNotNull(beforeSeverity, defaultSeverity, afterSeverity)) {
                return new InvocationDescriptor(
                        Helper.coalesce(beforeSeverity, defaultSeverity),
                        Helper.coalesce(afterSeverity, defaultSeverity),
                        Helper.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));
            }

            return new InvocationDescriptor(
                    Helper.coalesce(classBeforeSeverity, classDefaultSeverity),
                    Helper.coalesce(classAfterSeverity, classDefaultSeverity),
                    Helper.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));

        }

        private LogException parseAnnotations(Annotation[] annotations, boolean fromMethod) {
            LogException logExceptionAnnotation = null;
            for (Annotation currAnnotation : annotations) {
                if (currAnnotation.annotationType().equals(LogException.class)) {
                    logExceptionAnnotation = (LogException) currAnnotation;
                } else if (currAnnotation.annotationType().equals(LogFatal.class)) {
                    LogFatal logAnnotation = (LogFatal) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.FATAL, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogError.class)) {
                    LogError logAnnotation = (LogError) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.ERROR, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogWarn.class)) {
                    LogWarn logAnnotation = (LogWarn) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.WARN, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogInfo.class)) {
                    LogInfo logAnnotation = (LogInfo) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.INFO, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogDebug.class)) {
                    LogDebug logAnnotation = (LogDebug) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.DEBUG, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogTrace.class)) {
                    LogTrace logAnnotation = (LogTrace) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.TRACE, fromMethod);
                }
            }
            return logExceptionAnnotation;
        }

        private void setSeverity(LogPoint logPoint, Severity targetSeverity, boolean fromMethod) {
            if (fromMethod) {
                if (logPoint == LogPoint.IN) {
                    beforeSeverity = Helper.max(targetSeverity, beforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    afterSeverity = Helper.max(targetSeverity, afterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    defaultSeverity = Helper.max(targetSeverity, defaultSeverity);
                }
            } else {
                if (logPoint == LogPoint.IN) {
                    classBeforeSeverity = Helper.max(targetSeverity, classBeforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    classAfterSeverity = Helper.max(targetSeverity, classAfterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    classDefaultSeverity = Helper.max(targetSeverity, classDefaultSeverity);
                }
            }
        }

    }
}
