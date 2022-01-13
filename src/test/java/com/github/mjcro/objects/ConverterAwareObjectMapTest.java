package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class ConverterAwareObjectMapTest {

    @Test
    public void testGeneralBehavior() {
        StubbingMap map = new StubbingMap();

        map.value = "98";
        Assert.assertEquals(map.getString(null), "98");
        Assert.assertEquals(map.getShort(null), (short) 98);
        Assert.assertEquals(map.getInt(null), 98);
        Assert.assertEquals(map.getLong(null), 98L);
        Assert.assertEquals(map.getBigInteger(null), new BigInteger("98"));
        Assert.assertEquals(map.getBigDecimal(null), new BigDecimal("98"));
        Assert.assertEquals(map.getUnixSecondsInstant(null), Instant.ofEpochSecond(98));

        map.value = -72L;
        Assert.assertEquals(map.getString(null), "-72");
        Assert.assertEquals(map.getShort(null), (short) -72);
        Assert.assertEquals(map.getInt(null), -72);
        Assert.assertEquals(map.getLong(null), -72L);
        Assert.assertEquals(map.getBigInteger(null), new BigInteger("-72"));
        Assert.assertEquals(map.getBigDecimal(null), new BigDecimal("-72"));
    }


    private static class StubbingMap implements ConverterAwareObjectMap<String> {
        private Object value;

        @Override
        public Converter getConverter() {
            return Converter.standard();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Set<String> keySet() {
            return Collections.emptySet();
        }

        @Override
        public boolean containsKey(final String key) {
            return true;
        }

        @Override
        public Object get(final String key) {
            return value;
        }
    }
}