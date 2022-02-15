package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConverterConstructingTest {
    @Test
    public void testConstructingBytes() {
        Assert.assertEquals(
                new ConverterConstructing().convert("123".getBytes(), String.class),
                "123"
        );
    }

    @Test
    public void testConstructingCustom() {
        Assert.assertEquals(
                new ConverterConstructing().convert(new Foo(53535), Bar.class),
                new Bar(new Foo(53535))
        );
    }

    @Test
    public void testConstructingMissingBupass() {
        Bar bar = new ConverterConstructing(new Converter() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T convert(final ConversionContext<T> context) throws ConversionException {
                return (T) new Bar(new Foo(321));
            }
        }).convert("Foo", Bar.class);

        Assert.assertEquals(bar.foo.value, 321);
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testConstructingExceptionally() {
        new ConverterConstructing().convert(true, Bar.class);
    }

    private static class Foo {
        private final int value;

        private Foo(int value) {
            this.value = value;
        }
    }

    private static class Bar {
        private final Foo foo;

        private Bar(Foo foo) {
            this.foo = foo;
        }

        private Bar(Boolean bool) throws InstantiationException {
            throw new InstantiationException();
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Bar && foo.value == ((Bar) obj).foo.value;
        }
    }
}