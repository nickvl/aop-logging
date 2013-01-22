/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;


/**
 * Logger aspect.
 */
@Aspect
public class AOPLogger implements InitializingBean {
    // private static final Log LOGGER = LogFactory.getLog(AOPLogger.class);
    private LogAdapter logAdapter;
    private Map<Severity, LogStrategy> logStrategies;
    private LocalVariableTableParameterNameDiscoverer localVariableNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private ExceptionResolver exceptionResolver = new ExceptionResolver();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (logAdapter == null) {
            logAdapter = new SimpleLogAdapter();
        }
        logStrategies = new EnumMap<Severity, LogStrategy>(Severity.class);
        logStrategies.put(Severity.FATAL, new LogStrategy.FatalLogStrategy(logAdapter));
        logStrategies.put(Severity.ERROR, new LogStrategy.ErrorLogStrategy(logAdapter));
        logStrategies.put(Severity.WARN, new LogStrategy.WarnLogStrategy(logAdapter));
        logStrategies.put(Severity.INFO, new LogStrategy.InfoLogStrategy(logAdapter));
        logStrategies.put(Severity.DEBUG, new LogStrategy.DebugLogStrategy(logAdapter));
        logStrategies.put(Severity.TRACE, new LogStrategy.TraceLogStrategy(logAdapter));
    }

    public void setLogAdapter(LogAdapter log) {
        this.logAdapter = log;
    }

    /**
     * Advise. Logs the advised method.
     *
     * @param joinPoint represents advised method
     * @return method execution result
     * @throws Throwable in case of exception
     */
    @Around(value = "execution(@(@net.ng.xspring.core.log.aop.annotation.Logging *) * *.* (..))" // per method
            + " || execution(* @(@net.ng.xspring.core.log.aop.annotation.Logging *) * .*(..))"   // per class
            , argNames = "joinPoint")
    public Object logTheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // TODO if toStringOverridden, big list and array
        // TODO varArgs test

        Object[] args = joinPoint.getArgs();
        Log logger = logAdapter.getLog(joinPoint.getTarget().getClass());
        Method method = extractMethod(joinPoint);

        InvocationDescriptor invocationDescriptor = new InvocationDescriptor.Builder(method).build();

        String methodName = joinPoint.getSignature().getName(); // TODO could be optimized

        if (beforeLoggingOn(invocationDescriptor, logger)) {
            ArgumentDescriptor argumentDescriptor = new ArgumentDescriptor.Builder(method, args.length, localVariableNameDiscoverer).build();
            logStrategies.get(invocationDescriptor.getBeforeSeverity()).logBefore(logger, methodName, args, argumentDescriptor);
        }

        Object result;
        if (invocationDescriptor.getExceptionAnnotation() == null) {
            result = joinPoint.proceed(args);
        } else {
            try {
                result = joinPoint.proceed(args);
            } catch (Exception e) {
                ExceptionDescriptor exceptionDescriptor = new ExceptionDescriptor.Builder(invocationDescriptor.getExceptionAnnotation()).build();
                Class<? extends Exception> resolved = exceptionResolver.resolve(exceptionDescriptor, e);
                if (resolved != null) {
                    ExceptionSeverity excSeverity = exceptionDescriptor.getExceptionSeverity(resolved);
                    if (isLoggingOn(excSeverity.getSeverity(), logger)) {
                        logStrategies.get(excSeverity.getSeverity()).logException(logger, methodName, args.length, e, excSeverity.getStackTrace());
                    }
                }
                throw e;
            }
        }
        if (afterLoggingOn(invocationDescriptor, logger)) {
            logStrategies.get(invocationDescriptor.getAfterSeverity()).logAfter(logger, methodName, args.length, result);
        }
        return result;
    }

    private Method extractMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // signature.getMethod() points to method declared in interface. it is not suit to discover arg names and arg annotations
        // TODO check if it works with several aspects:
        // see AopProxyUtils: org.springframework.cache.interceptor.CacheAspectSupport#execute(CacheAspectSupport.Invoker, Object, Method, Object[])
        return joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
    }

    private boolean beforeLoggingOn(InvocationDescriptor descriptor, Log logger) {
        return isLoggingOn(descriptor.getBeforeSeverity(), logger);
    }

    private boolean afterLoggingOn(InvocationDescriptor descriptor, Log logger) {
        return isLoggingOn(descriptor.getAfterSeverity(), logger);
    }

    private boolean isLoggingOn(Severity severity, Log logger) {
        return severity != null && logStrategies.get(severity).isLogEnabled(logger);
    }
}
