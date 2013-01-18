/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

/**
 * Utility helper class.
 */
final class Helper {
    private Helper() {
    }

    public static <T extends Comparable<T>> T max(T obj, T other) {
        if (other == null) {
            return obj;
        }
        if (obj == null) {
            return other;
        }
        return obj.compareTo(other) > 0 ? obj : other;
    }

    public static <T extends Comparable<T>> boolean greater(T obj, T other) {
        return obj.compareTo(other) > 0;
    }

    public static <T> T coalesce(T a, T b) {
        return a == null ? b : a;
    }

    public static <T> boolean hasNotNull(T a, T b, T c) {
        return a != null || b != null || c != null;
    }

}
