/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Detects if a class has overridden toString method.
 * TODO consider ConcurrentReferenceHashMap
 */
final class ToStringDetector {
    private final ConcurrentMap<Class<?>, Boolean> cache = new ConcurrentHashMap<Class<?>, Boolean>();
    public static final ToStringDetector INSTANCE = new ToStringDetector();

    private ToStringDetector() {
    }

    public boolean hasToString(Class<?> clazz) {
        try {
            Boolean hasToString = cache.get(clazz);
            if (hasToString != null) {
                return hasToString;
            }

            hasToString = clazz.getMethod("toString").getDeclaringClass() != Object.class;
            cache.putIfAbsent(clazz, hasToString);
            return hasToString;
        } catch (NoSuchMethodException e) {
            throw new InternalError("method toString() not found.");
        }
    }
}
