package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.Mixed;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConverterMixedTest {
    @Test
    public void testConvert() {
        ConverterMixed converter = new ConverterMixed();
        Mixed mixed = Mixed.wrap("foo");
        Assert.assertEquals(converter.convert(mixed, String.class), "foo");
    }
}