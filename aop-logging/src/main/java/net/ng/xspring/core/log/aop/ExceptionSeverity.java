/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

/**
 * A holder for a pair.
 */
final class ExceptionSeverity {
    private final Severity severity;
    private final boolean stackTrace;

    public ExceptionSeverity(Severity severity, boolean stackTrace) {
        this.severity = severity;
        this.stackTrace = stackTrace;
    }

    public Severity getSeverity() {
        return severity;
    }

    public boolean getStackTrace() {
        return stackTrace;
    }
}
