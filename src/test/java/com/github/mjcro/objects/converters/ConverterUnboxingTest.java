package com.github.mjcro.objects.converters;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConverterUnboxingTest {
    @Test
    public void testConverting() {
        ConverterUnboxing converter = new ConverterUnboxing(null);

        Assert.assertEquals(converter.convert(true, boolean.class), true);
        Assert.assertEquals(converter.convert(false, boolean.class), false);
        Assert.assertEquals(converter.convert(Boolean.TRUE, boolean.class), true);
        Assert.assertEquals(converter.convert(Boolean.FALSE, boolean.class), false);
        Assert.assertEquals(converter.convert(Byte.valueOf("53"), byte.class), (byte) 53);
        Assert.assertEquals(converter.convert((short) 12345, short.class), (short) 12345);
        Assert.assertEquals(converter.convert((short) 12345, int.class), 12345);
        Assert.assertEquals(converter.convert(12345, int.class), 12345);
        Assert.assertEquals(converter.convert(12345, long.class), 12345L);
        Assert.assertEquals(converter.convert(12345L, long.class), 12345L);
        Assert.assertEquals(converter.convert(12.99f, float.class), 12.99f);
        Assert.assertEquals(converter.convert(12.99, double.class), 12.99);
    }
}