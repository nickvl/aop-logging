/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Assert;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Utility methods.
 */
public final class TestSupportUtility {

    private static final Comparator<Object[]> ARRAY_EQUAL_COMPARATOR = new Comparator<Object[]>() {
        @Override
        public int compare(Object[] o1, Object[] o2) {
            Assert.assertArrayEquals(o1, o2);
            return 0;
        }
    };

    private static final Comparator<Object> REFLECTION_COMPARATOR = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return CompareToBuilder.reflectionCompare(o1, o2);
        }
    };


    private TestSupportUtility() {
    }

    public static void assertCollection(Collection<?> collection, Object... expected) {
        assertArrayEquals(expected, collection.toArray());
    }

    public static void assertCollectionConsistOf(Collection<?> collection, Object... expected) {
        assertEquals(expected.length, collection.size());
        for (Object expectedElement : expected) {
            assertTrue(collection.contains(expectedElement));
        }
    }

    public static void assertReflectionEquals(Object expected, Object actual) {
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    public static Object[] arrayEqual(Object[] expectedArray) {
        return EasyMock.cmp(expectedArray, ARRAY_EQUAL_COMPARATOR, LogicalOperator.EQUAL);
    }

    public static Object reflectionEquals(Object expected) {
        return EasyMock.cmp(expected, REFLECTION_COMPARATOR, LogicalOperator.EQUAL);
    }

    public static ArgumentDescriptor createArgumentDescriptor(String[] argumentNames, boolean... indexes) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        BitSet argIndexes = new BitSet(indexes.length);
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i]) {
                argIndexes.set(i);
            }
        }
        return createArgumentDescriptor(argIndexes, argumentNames);
    }

    public static ArgumentDescriptor createArgumentDescriptor(BitSet indexes, String[] argumentNames) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        if (argumentNames != null) {
            assertTrue(String.format("wrong arguments: argCount[%s] more then argNames[%s]", indexes, Arrays.toString(argumentNames)),
                    indexes.length() <= argumentNames.length);
        }
        Constructor<ArgumentDescriptor> constructor = ArgumentDescriptor.class.getDeclaredConstructor(BitSet.class, String[].class);
        constructor.setAccessible(true);
        return constructor.newInstance(indexes, argumentNames);
    }
}
