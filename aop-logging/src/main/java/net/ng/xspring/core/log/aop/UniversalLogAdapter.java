/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Universal log adapter, capable to out parameter values by reflection.
 */
public class UniversalLogAdapter extends AbstractLogAdapter {
    private final Set<String> excludeFieldNames;

    // TODO init is not complete

    /**
     * Constructor.
     *
     * @param excludeFieldNames field names to exclude from building
     */
    public UniversalLogAdapter(Set<String> excludeFieldNames) {
        this.excludeFieldNames = excludeFieldNames == null ? null : new HashSet<String>(excludeFieldNames);
    }

    @Override
    protected String asString(Object value) {
        if (value == null) {
            return ToString.getNull();
        }
        Class<?> clazz = value.getClass();
        if (!(value instanceof Collection<?> || value instanceof Map<?, ?>) && ToStringDetector.INSTANCE.hasToString(clazz)) {
            return value.toString();
        }
        ToString builder = ToString.createDefault();
        builder.addStart(value);

        if (clazz.isArray()) {
            builder.addArray(value);
        } else {
            while (clazz != Object.class) {
                appendFieldsIn(builder, value, clazz.getDeclaredFields());
                clazz = clazz.getSuperclass();
            }
        }
        builder.addEnd(value);
        return builder.toString();
    }

    private boolean reject(Field field) {
        return field.getName().indexOf('$') != -1 // Reject field from inner class.
                || Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())
                || excludeFieldNames != null && excludeFieldNames.contains(field.getName());
    }

    private void appendFieldsIn(ToString builder, Object object, Field[] fields) {
        if (fields.length == 0) {
            return;
        }
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!reject(field)) {
                try {
                    // memo: it creates wrapper objects for primitive types.
                    Object fieldValue = field.get(object);
                    builder.addField(fieldName, fieldValue);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Unexpected IllegalAccessException: " + ex.getMessage());
                }
            }
        }
    }

}
