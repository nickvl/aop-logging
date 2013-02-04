/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests string building by {@link UniversalLogAdapter}.
 */
public class UniversalLogAdapterTestCase {

    private UniversalLogAdapter logAdapter;

    @Before
    public void setUp() throws Exception {
        logAdapter = new UniversalLogAdapter(null);
    }

    private String identityHashCode(Object value) {
        return Integer.toHexString(System.identityHashCode(value));
    }

    @Test
    public void testAsStringNull() throws Exception {
        assertEquals("NIL", logAdapter.asString(null));
    }

    @Test
    public void testAsStringObject() throws Exception {
        assertEquals("Object[]", logAdapter.asString(new Object()));
    }

    @Test
    public void testAsStringNullRef() throws Exception {
        class NullRef {
            private String s;
            private String[] arr = new String[]{null};
        }
        assertEquals("NullRef[s=NIL;arr={NIL}]", logAdapter.asString(new NullRef()));
    }

    @Test
    public void testAsStringInheritance() throws Exception {
        class Int {
            private int i = 1;
        }
        assertEquals("Int[i=1]", logAdapter.asString(new Int()));

        class NamedInt extends Int {
            private String name = "s";
        }
        assertEquals("NamedInt[name=s;i=1]", logAdapter.asString(new NamedInt()));
    }

    @Test
    public void testAsStringArray() throws Exception {
        assertEquals("int[][{1,2}]", logAdapter.asString(new int[]{1, 2}));
        assertEquals("int[][][{{1},{2}}]", logAdapter.asString(new int[][]{{1}, {2}}));
    }

    @Test
    public void testAsStringCycleItself() throws Exception {
        class CycleItself {
            private CycleItself c = this;
        }
        CycleItself value = new CycleItself();
        assertEquals("CycleItself[c=CycleItself@" + identityHashCode(value) + "]", logAdapter.asString(value));
    }

    @Test
    public void testAsStringCycleItselfInArr() throws Exception {
        class CycleItselfInArr {
            private CycleItselfInArr[] c = new CycleItselfInArr[]{this};
        }
        CycleItselfInArr value = new CycleItselfInArr();
        String hash = Integer.toHexString(System.identityHashCode(value));
        assertEquals("CycleItselfInArr[c={CycleItselfInArr@" + hash + "}]", logAdapter.asString(value));
    }


    @Test
    public void testAsStringCycleInArr() throws Exception {
        // memo: Tests for filling ToString.valuesInProgress in ToString.parse():
        // the filling prevents java.lang.StackOverflowError
        class CycleInArr {
            private Object[] c = new Object[1];

            {
                c[0] = c;
            }
        }
        CycleInArr value = new CycleInArr();
        assertEquals("CycleInArr[c={Object[]@" + identityHashCode(value.c) + "}]", logAdapter.asString(value));
    }

    @Test
    public void testAsStringCycleArray() throws Exception {
        // memo: Tests for filling ToString.valuesInProgress in ToString.addStart():
        // the filling prevents java.lang.StackOverflowError or/and incorrect output (depends on the same filling in ToString.parse())
        Object[] arr = new Object[1];
        arr[0] = arr;
        // correct Object[][{Object[]@17182c1}]
        // wrong   Object[][{{Object[]@601bb1}}]
        assertEquals("Object[][{Object[]@" + identityHashCode(arr) + "}]", logAdapter.asString(arr));
    }

    @Test
    public void testAsStringCrossReference() throws Exception {
        class CrossReference {
            private Object[] c = new Object[1];
        }
        CrossReference a = new CrossReference();
        CrossReference b = new CrossReference();
        a.c[0] = b;
        b.c[0] = a;
        assertEquals("CrossReference[c={CrossReference@" + identityHashCode(b) + "}]", logAdapter.asString(a));
    }

    @Test
    public void testAsStringSamePrimitive() throws Exception {
        class SamePrimitive {
            private Object[] c = new Object[2];

            {
                c[0] = Boolean.TRUE;
                c[1] = new Object[]{Boolean.TRUE};
            }
        }
        assertEquals("SamePrimitive[c={true,{true}}]", logAdapter.asString(new SamePrimitive()));
    }

    @Test
    public void testAsStringCycleIdentityEquals() throws Exception {
        class IdentityEquals {
            private IdentityEquals c;

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof IdentityEquals;
            }

        }
        IdentityEquals a = new IdentityEquals();
        a.c = new IdentityEquals() {
            @Override
            public String toString() {
                return "extended-IdentityEquals";
            }
        };
        // memo: general equals in ToString.valuesInProgress leads to detect [a.c] as repeated [a] and incorrect out IdentityEquals[c=@126b249]
        assertEquals("IdentityEquals[c=extended-IdentityEquals]", logAdapter.asString(a));
    }

    @Test
    public void testAsStringGeneric() throws Exception {
        class Generic<T> {
            private T thing;

            private Generic(T thing) {
                this.thing = thing;
            }
        }
        assertEquals("Generic[thing=str]", logAdapter.asString(new Generic<String>("str")));
    }

    @Test
    public void testAsStringToString() throws Exception {
        class Foo {
            @Override
            public String toString() {
                return "abracadabra";
            }
        }
        assertEquals("abracadabra", logAdapter.asString(new Foo()));

        class Bar {
            private Foo name = new Foo();
        }
        assertEquals("Bar[name=abracadabra]", logAdapter.asString(new Bar()));
    }

}
