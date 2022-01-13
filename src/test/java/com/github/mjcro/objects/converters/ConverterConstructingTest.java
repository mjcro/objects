package com.github.mjcro.objects.converters;

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
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Bar && foo.value == ((Bar) obj).foo.value;
        }
    }
}