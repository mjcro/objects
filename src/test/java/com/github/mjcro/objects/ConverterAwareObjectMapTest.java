package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ConverterAwareObjectMapTest {

    @Test
    public void testGeneralBehavior() {
        Impl map = new Impl("98");

        Assert.assertEquals(map.getString(null), "98");
        Assert.assertEquals(map.getShort(null), (short) 98);
        Assert.assertEquals(map.getInt(null), 98);
        Assert.assertEquals(map.getLong(null), 98L);
        Assert.assertEquals(map.getBigInteger(null), new BigInteger("98"));
        Assert.assertEquals(map.getBigDecimal(null), new BigDecimal("98"));
        Assert.assertEquals(map.getUnixSecondsInstant(null), Instant.ofEpochSecond(98));
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testGetConvertedNullClass() {
        new Impl("").get("key", null);
    }

    @Test
    public void testGetConvertedNullValue() {
        Assert.assertNull(new Impl(null).get("key", String.class));
    }

    @Test
    public void testGetCollection() {
        Collection<Long> longs = new Impl("1,2,3").getCollection("key", ",", Long.class);
        Assert.assertNotNull(longs);
        Assert.assertEquals(longs.size(), 3);
        Assert.assertTrue(longs.contains(1L));
        Assert.assertTrue(longs.contains(2L));
        Assert.assertTrue(longs.contains(3L));
    }

    @Test
    public void testGetCollectionNull() {
        Collection<Long> longs = new Impl(null).getCollection("key", ",", Long.class);
        Assert.assertNotNull(longs);
        Assert.assertEquals(longs.size(), 0);
    }

    @Test
    public void testGetMixed() {
        Converter custom = new Converter() {
            @Override
            public <T> T convert(ConversionContext<T> context) throws ConversionException {
                return null;
            }
        };

        Impl impl = new Impl(custom, "some");
        Mixed mixed = impl.getMixed("key");

        Assert.assertEquals(mixed.get(), "some");
        Assert.assertTrue(mixed instanceof ConverterAwareMixed);
        Assert.assertSame(((ConverterAwareMixed) mixed).getConverter(), custom);
    }

    private static class Impl implements ConverterAwareObjectMap<String> {
        private final Converter converter;
        private final Object value;

        Impl(Object value) {
            this(General.CONVERTER, value);
        }

        Impl(Converter converter, Object value) {
            this.converter = converter;
            this.value = value;
        }

        @Override
        public Converter getConverter() {
            return converter;
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

        @SuppressWarnings("unchecked")
        @Override
        public <Z> ObjectMap<Z> map(final Function<Map.Entry<String, Object>, Map.Entry<Z, Object>> mapping) {
            return (ObjectMap<Z>) this;
        }
    }
}