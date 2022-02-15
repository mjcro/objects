package io.github.mjcro.objects;

import io.github.mjcro.objects.converters.ConverterSame;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.function.Function;

public class MixedTest {
    @Test
    public void testEmpty() {
        Assert.assertTrue(new Impl(null).isEmpty());
        Assert.assertFalse(new Impl(null).isPresent());
        Assert.assertFalse(new Impl("").isEmpty());
        Assert.assertFalse(new Impl(0).isEmpty());

        Assert.assertFalse(new Impl(null).getOptional().isPresent());
        Assert.assertTrue(new Impl("").getOptional().isPresent());
        Assert.assertTrue(new Impl(0).getOptional().isPresent());
    }

    @Test
    public void testGetBool() {
        Impl impl = new Impl(true);
        impl.getBool();
        Assert.assertEquals(impl.clazz, boolean.class);

        impl.getBooleanOptional();
        Assert.assertEquals(impl.clazz, Boolean.class);
    }

    @Test
    public void testGetShort() {
        Impl impl = new Impl((short) 4);
        impl.getShort();
        Assert.assertEquals(impl.clazz, short.class);

        impl.getShortOptional();
        Assert.assertEquals(impl.clazz, Short.class);
    }

    @Test
    public void testGetInteger() {
        Impl impl = new Impl(-2);
        impl.getInt();
        Assert.assertEquals(impl.clazz, int.class);

        impl.getIntOptional();
        Assert.assertEquals(impl.clazz, Integer.class);
    }

    @Test
    public void testGetLong() {
        Impl impl = new Impl(87L);
        impl.getLong();
        Assert.assertEquals(impl.clazz, long.class);

        impl.getLongOptional();
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetBigInteger() {
        Impl impl = new Impl(BigInteger.ONE);
        impl.getBigInteger();
        Assert.assertEquals(impl.clazz, BigInteger.class);

        impl.getBigIntegerOptional();
        Assert.assertEquals(impl.clazz, BigInteger.class);
    }

    @Test
    public void testGetUnixSecondsInstant() {
        Impl impl = new Impl(-2L);
        impl.getUnixSecondsInstant();
        Assert.assertEquals(impl.clazz, Long.class);

        impl.getUnixMillisInstantOptional();
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetUnixMillisInstant() {
        Impl impl = new Impl(-2L);
        impl.getUnixMillisInstant();
        Assert.assertEquals(impl.clazz, Long.class);

        impl.getUnixMillisInstantOptional();
        Assert.assertEquals(impl.clazz, Long.class);
    }

    @Test
    public void testGetBigDecimal() {
        Impl impl = new Impl(BigDecimal.ONE);
        impl.getBigDecimal();
        Assert.assertEquals(impl.clazz, BigDecimal.class);

        impl.getBigDecimalOptional();
        Assert.assertEquals(impl.clazz, BigDecimal.class);
    }

    @Test
    public void testGetString() {
        Impl impl = new Impl("");
        impl.getString();
        Assert.assertEquals(impl.clazz, String.class);

        impl.getStringOptional();
        Assert.assertEquals(impl.clazz, String.class);
    }

    @Test
    public void testGetCollection() {
        Collection<String> strings = new Impl("3,2").getCollection(",", String.class);
        Assert.assertEquals(strings.size(), 2);
        Assert.assertTrue(strings.contains("2"));
        Assert.assertTrue(strings.contains("3"));
    }

    private static final class Impl implements Mixed {
        private final Object value;
        private Class<?> clazz;

        private Impl(Object value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Class<T> clazz) throws ConversionException {
            this.clazz = clazz;
            return (T) value;
        }

        @Override
        public <T> Collection<T> getCollection(CharSequence separator, Class<T> clazz) throws ConversionException {
            return new CollectionExtractor(new ConverterSame()).toList(
                    this.value,
                    clazz,
                    separator
            );
        }

        @Override
        public Mixed map(Function<Object, Object> mapper) {
            return null; // Not subject to test here
        }
    }
}