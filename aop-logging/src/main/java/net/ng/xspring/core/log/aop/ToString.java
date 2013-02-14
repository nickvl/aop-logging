/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * String representation builder of any class instance.
 */
final class ToString {
    private static final String NULL_VALUE = "NIL";
    private static final String CONTENT_START = "[";
    private static final String CONTENT_END = "]";
    private static final String FIELD_VALUE_SEPARATOR = "=";
    private static final String FIELD_SEPARATOR = ";";
    private static final String ENUMERATION_START = "{";
    private static final String ENUMERATION_SEPARATOR = ",";
    private static final String ENUMERATION_END = "}";
    private static final String SIZE_START = "..<size=";
    private static final String SIZE_END = ">..";

    private final int cropThreshold;

    private final StringBuilder buffer = new StringBuilder(512);
    private final Set<Object> valuesInProgress = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());

    private ToString(int cropThreshold) {
        this.cropThreshold = cropThreshold;
    }

    public static ToString createDefault() {
        return new ToString(-1);
    }

    public static ToString createCropInstance(int cropThreshold) {
        return new ToString(cropThreshold);
    }

    /**
     * Gets the text representation of {@code null} value fields.
     *
     * @return the current text to output when null found
     */
    public static String getNull() {
        return NULL_VALUE;
    }

    /**
     * Adds the start of <code>toString</code> of the given object.
     *
     * @param object the <code>Object</code> to build <code>toString</code>
     */
    public void addStart(Object object) {
        if (object != null) {
            valuesInProgress.add(object);
            addClassName(object);
            addContentStart();
        }
    }

    /**
     * Ads the end of <code>toString</code> of the given object.
     *
     * @param object the <code>Object</code> to build a <code>toString</code>
     */
    public void addEnd(Object object) {
        removeLastFieldSeparator();
        addContentEnd();
        valuesInProgress.remove(object); //not really needed but added for symmetry purposes (with addStart())
    }

    private void removeLastFieldSeparator() {
        int length = buffer.length();
        int sepLen = FIELD_SEPARATOR.length();
        if (length > 0 && sepLen > 0 && length >= sepLen) {
            for (int i = 0; i < sepLen; i++) {
                if (buffer.charAt(length - sepLen + i) != FIELD_SEPARATOR.charAt(i)) {
                    return;
                }
            }
            buffer.setLength(length - sepLen);
        }
    }

    /**
     * Adds to the <code>toString</code> a field of the given object.
     *
     * @param fieldName the field name
     * @param value the value of the field
     */
    public void addField(String fieldName, Object value) {
        addFieldStart(fieldName);

        if (value == null) {
            addNullValue();
        } else {
            parse(value);
        }

        addFieldEnd();
    }

    /**
     * Adds array as a given object to build the <code>toString</code>.
     *
     * @param array the array to build a <code>toString</code>
     */
    public void addArray(Object array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = Array.getLength(array) - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(maxIndex + 1).append(SIZE_END);
                    break;
                }
                Object item = Array.get(array, i);
                if (item == null) {
                    addNullValue();
                } else {
                    parse(item);
                }
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    /**
     * Adds collection as a given object to build the <code>toString</code>.
     *
     * @param coll the collection to build a <code>toString</code>
     */
    public void addCollection(Collection<?> coll) {
        buffer.append(ENUMERATION_START);
        if (!coll.isEmpty()) {
            Iterator<?> iterator = coll.iterator();
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(coll.size()).append(SIZE_END);
                    break;
                }
                Object item = iterator.next();
                if (item == null) {
                    addNullValue();
                } else {
                    parse(item);
                }
                if (!iterator.hasNext()) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    /**
     * Adds map as a given object to build the <code>toString</code>.
     *
     * @param map the map to build a <code>toString</code>
     */
    public void addMap(Map<?, ?> map) {
        buffer.append(ENUMERATION_START);
        if (!map.isEmpty()) {
            Iterator<?> iterator = map.entrySet().iterator();
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(map.size()).append(SIZE_END);
                    break;
                }
                Object item = iterator.next(); // iterator shall never return null
                parse(item);
                if (!iterator.hasNext()) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void parse(Object value) {
        if (valuesInProgress.contains(value)) {
            addCyclicObject(value);
            return;
        }

        valuesInProgress.add(value);

        if (value instanceof Collection<?>) {
            addCollection((Collection<?>) value);
        } else if (value instanceof Map<?, ?>) {
            addMap((Map<?, ?>) value);
        } else if (value instanceof long[]) {
            add((long[]) value);
        } else if (value instanceof int[]) {
            add((int[]) value);
        } else if (value instanceof short[]) {
            add((short[]) value);
        } else if (value instanceof byte[]) {
            add((byte[]) value);
        } else if (value instanceof char[]) {
            add((char[]) value);
        } else if (value instanceof double[]) {
            add((double[]) value);
        } else if (value instanceof float[]) {
            add((float[]) value);
        } else if (value instanceof boolean[]) {
            add((boolean[]) value);
        } else if (value.getClass().isArray()) {
            add((Object[]) value);
        } else {
            add(value);
        }
        valuesInProgress.remove(value);
    }

    private void addCyclicObject(Object value) {
        buffer.append(value.getClass().getSimpleName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(value)));
    }

    private void add(Object value) {
        Class<?> clazz = value.getClass();
        if (ToStringDetector.INSTANCE.hasToString(clazz)) {
            buffer.append(value.toString());
        } else {
            buffer.append(clazz.getSimpleName())
                    .append('@')
                    .append(Integer.toHexString(value.hashCode()));
        }
    }

    private void add(Object[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                Object item = array[i];
                if (item == null) {
                    addNullValue();
                } else {
                    parse(item);
                }
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(long[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(int[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(short[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(byte[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(char[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(double[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(float[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void add(boolean[] array) {
        buffer.append(ENUMERATION_START);
        int maxIndex = array.length - 1;
        if (maxIndex != -1) {
            for (int i = 0;; i++) {
                if (i == cropThreshold) {
                    buffer.append(SIZE_START).append(array.length).append(SIZE_END);
                    break;
                }
                buffer.append(array[i]);
                if (i == maxIndex) {
                    break;
                }
                buffer.append(ENUMERATION_SEPARATOR);
            }
        }
        buffer.append(ENUMERATION_END);
    }

    private void addClassName(Object object) {
        buffer.append(object.getClass().getSimpleName());
    }

    private void addContentStart() {
        buffer.append(CONTENT_START);
    }

    private void addContentEnd() {
        buffer.append(CONTENT_END);
    }

    private void addNullValue() {
        buffer.append(NULL_VALUE);
    }

    private void addFieldStart(String fieldName) {
        if (fieldName != null) {
            buffer.append(fieldName);
            buffer.append(FIELD_VALUE_SEPARATOR);
        }
    }

    private void addFieldEnd() {
        buffer.append(FIELD_SEPARATOR);
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
