/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.BitSet;

import org.springframework.core.ParameterNameDiscoverer;

import net.ng.xspring.core.log.aop.annotation.Lp;

/**
 * Method arguments descriptor.
 */
final class ArgumentDescriptor {
    private final Object[] values;
    private final String[] names;

    private ArgumentDescriptor(Object[] values, String[] names) {
        this.values = values;
        this.names = names;
    }

    public Object[] getValues() {
        return values;
    }

    public String[] getNames() {
        return names;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final Object[] arguments;
        private final Method method;
        private final ParameterNameDiscoverer parameterNameDiscoverer;

        public Builder(Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
            this.method = method;
            this.arguments = arguments;
            this.parameterNameDiscoverer = parameterNameDiscoverer;
        }

        public ArgumentDescriptor build() {
            if (arguments.length == 0) {
                return new ArgumentDescriptor(arguments, null);
            }
            String[] argNames = parameterNameDiscoverer.getParameterNames(method);

            BitSet lpParameters = getMethodParameters(Lp.class);
            if (lpParameters.isEmpty()) {
                return new ArgumentDescriptor(arguments, argNames);
            }
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
            return new ArgumentDescriptor(lpArgs, lpArgNames);
        }

        private <T> BitSet getMethodParameters(Class<T> annotationMarker) {
            // TODO extract this independent result to make it cacheable
            Annotation[][] annotations = method.getParameterAnnotations();
            BitSet result = new BitSet(annotations.length);
            for (int i = 0; i < annotations.length; i++) {
                Annotation[] paramAnnotations = annotations[i];
                for (Annotation currAnnotation : paramAnnotations) {
                    if (currAnnotation.annotationType().equals(annotationMarker)) {
                        result.set(i);
                    }
                }
            }
            return result;
        }

    }

}
