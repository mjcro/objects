package io.github.mjcro.objects;

import io.github.mjcro.objects.converters.ConverterSame;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ObjectMapTest {
    @Test
    public void testEmpty() {
        Assert.assertFalse(new Impl(null).getOptional("key").isPresent());
        Assert.assertTrue(new Impl("").getOptional("key").isPresent());
        Assert.assertTrue(new Impl(0).getOptional("key").isPresent());
    }

    @Test
    public void testGetBool() {
        Impl impl = new Impl(true);
        impl.getBool("key");
        Assert.assertEquals(impl.clazz, boolean.class);

        impl.getBooleanOptional("key");
        Assert.assertEquals(impl.clazz, Boolean.class);
    }

    @Test
    public void testGetShort() {
        Impl impl = new Impl((short) 4);
        impl.getShort("key");
        Assert.assertEquals(impl.clazz, short.class);

        impl.getShortOptional("key");
        Assert.assertEquals(impl.clazz, Short.class);
    }

    @Test
    public void testGetInteger() {
        Impl impl = new Impl(-2);
        impl.getInt("key");
        Assert.assertEquals(impl.clazz, int.class);

        impl.getIntOptional("key");
        Assert.assertEquals(impl.clazz, Integer.class);
    }

    @Test
    public void testGetLong() {
        Impl impl = new Impl(87L);
        impl.getLong("key");
        Assert.assertEquals(impl.clazz, long.class);

        impl.getLongOptional("key");
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetBigInteger() {
        Impl impl = new Impl(BigInteger.ONE);
        impl.getBigInteger("key");
        Assert.assertEquals(impl.clazz, BigInteger.class);

        impl.getBigIntegerOptional("key");
        Assert.assertEquals(impl.clazz, BigInteger.class);
    }

    @Test
    public void testGetUnixSecondsInstant() {
        Impl impl = new Impl(-2L);
        impl.getUnixSecondsInstant("key");
        Assert.assertEquals(impl.clazz, Long.class);

        impl.getUnixMillisInstantOptional("key");
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetUnixMillisInstant() {
        Impl impl = new Impl(-2L);
        impl.getUnixMillisInstant("key");
        Assert.assertEquals(impl.clazz, Long.class);

        impl.getUnixMillisInstantOptional("key");
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetBigDecimal() {
        Impl impl = new Impl(BigDecimal.ONE);
        impl.getBigDecimal("key");
        Assert.assertEquals(impl.clazz, BigDecimal.class);

        impl.getBigDecimalOptional("key");
        Assert.assertEquals(impl.clazz, BigDecimal.class);
    }

    @Test
    public void testGetString() {
        Impl impl = new Impl("");
        impl.getString("key");
        Assert.assertEquals(impl.clazz, String.class);

        impl.getStringOptional("key");
        Assert.assertEquals(impl.clazz, String.class);
    }

    @Test
    public void testGetCollection() {
        Collection<String> strings = new Impl("3,2").getCollection("key", ",", String.class);
        Assert.assertEquals(strings.size(), 2);
        Assert.assertTrue(strings.contains("2"));
        Assert.assertTrue(strings.contains("3"));
    }

    @Test
    public void testToMap() {
        Map<String, Object> map = new Impl("zzz").toMap();
        Assert.assertEquals(map.size(), 1);
        Assert.assertTrue(map.containsKey("key"));
        Assert.assertEquals(map.get("key"), "zzz");
    }

    @Test
    public void testMap() {
        ObjectMap<String> map = new Impl("World").map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), "Hello, " + entry.getValue().toString()));
        Assert.assertEquals(map.get("key"), "Hello, World");
    }

    @Test
    public void testMapValues() {
        ObjectMap<String> map = new Impl("-").mapValues($value -> $value.toString() + $value.toString());
        Assert.assertEquals(map.get("key"), "--");
    }

    @Test
    public void testMapValuesBi() {
        ObjectMap<String> map = new Impl("!").mapValues(($key, $value) -> $key + $value.toString());
        Assert.assertEquals(map.get("key"), "key!");
    }

    private static final class Impl implements ObjectMap<String> {
        private final Object value;
        private Class<?> clazz;

        private Impl(Object value) {
            this.value = value;
        }

        @Override
        public Object get(String key) {
            return this.value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String key, Class<T> clazz) throws ConversionException {
            if (!"key".equals(key)) {
                throw new IndexOutOfBoundsException();
            }
            this.clazz = clazz;
            return (T) value;
        }

        @Override
        public Mixed getMixed(String key) {
            return null;
        }

        @Override
        public Set<String> keySet() {
            return Collections.singleton("key");
        }

        @Override
        public <T> Collection<T> getCollection(String key, CharSequence separator, Class<T> clazz) throws ConversionException {
            return new CollectionExtractor(new ConverterSame()).toList(
                    this.value,
                    clazz,
                    separator
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        public <Z> ObjectMap<Z> map(Function<Map.Entry<String, Object>, Map.Entry<Z, Object>> mapping) {
            return (ObjectMap<Z>) new Impl(mapping.apply(new AbstractMap.SimpleEntry<>("key", value)).getValue());
        }

        @Override
        public int size() {
            return 1; // Not subject to test here
        }

        @Override
        public boolean isEmpty() {
            return false; // Not subject to test here
        }

        @Override
        public boolean containsKey(String key) {
            return false; // Not subject to test here
        }
    }
}