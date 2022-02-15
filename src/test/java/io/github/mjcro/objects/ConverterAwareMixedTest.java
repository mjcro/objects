package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.function.Function;

public class ConverterAwareMixedTest {
    @Test(expectedExceptions = ConversionException.class)
    public void testGetConvertedNullClass() {
        new Impl("").get(null);
    }

    @Test
    public void testGetConvertedNullValue() {
        Assert.assertNull(new Impl(null).get(String.class));
    }

    @Test
    public void testGetCollection() {
        Collection<Long> longs = new Impl("1,2,3").getCollection(",", Long.class);
        Assert.assertNotNull(longs);
        Assert.assertEquals(longs.size(), 3);
        Assert.assertTrue(longs.contains(1L));
        Assert.assertTrue(longs.contains(2L));
        Assert.assertTrue(longs.contains(3L));
    }

    @Test
    public void testGetCollectionNull() {
        Collection<Long> longs = new Impl(null).getCollection(",", Long.class);
        Assert.assertNotNull(longs);
        Assert.assertEquals(longs.size(), 0);
    }

    private final static class Impl implements ConverterAwareMixed {
        private final Object data;

        private Impl(Object data) {
            this.data = data;
        }

        @Override
        public Converter getConverter() {
            return General.CONVERTER;
        }

        @Override
        public Object get() {
            return data;
        }

        @Override
        public Mixed map(Function<Object, Object> mapper) {
            return new Impl(mapper.apply(data));
        }
    }
}