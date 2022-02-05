package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConverterStringNumbersTest {
    @Test
    public void testConvert() {
        ConverterStringNumbers converter = new ConverterStringNumbers();

        Assert.assertEquals(converter.convert("-123", byte.class), (byte) -123);
        Assert.assertEquals(converter.convert("123", Byte.class), Byte.valueOf("123"));

        Assert.assertEquals(converter.convert("-423", short.class), (short) -423);
        Assert.assertEquals(converter.convert("9999", Short.class), Short.valueOf("9999"));

        Assert.assertEquals(converter.convert("1234567890", int.class), 1234567890);
        Assert.assertEquals(converter.convert("-987654321", Integer.class), Integer.valueOf(-987654321));

        Assert.assertEquals(converter.convert("1234567890", long.class), 1234567890L);
        Assert.assertEquals(converter.convert("-987654321", Long.class), Long.valueOf(-987654321));

        Assert.assertEquals(converter.convert("0.99", float.class), 0.99f);
        Assert.assertEquals(converter.convert("-33.33", Float.class), Float.valueOf("-33.33"));

        Assert.assertEquals(converter.convert("0.99", double.class), 0.99);
        Assert.assertEquals(converter.convert("-33.33", Double.class), Double.valueOf("-33.33"));

        Assert.assertEquals(converter.convert("342", BigInteger.class), BigInteger.valueOf(342));
        Assert.assertEquals(converter.convert("-.342", BigDecimal.class), new BigDecimal("-.342"));
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testConvertInvalidNumber() {
        new ConverterStringNumbers().convert("foo", int.class);
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testCustomNumber() {
        new ConverterStringNumbers().convert("foo", CustomNumber.class);
    }

    private static class CustomNumber extends Number {
        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }
}