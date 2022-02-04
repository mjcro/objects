package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionException;
import com.github.mjcro.objects.Converter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConvertersCheckNullContextTest {
    @DataProvider
    public Object[][] converters() {
        return new Object[][]{
                {new ConverterConstructing()},
                {new ConverterMixed()},
                {new ConverterSame()},
                {new ConverterStringBooleans()},
                {new ConverterStringNumbers()},
                {new ConverterStubbing()},
                {new ConverterToString()},
                {new ConverterUnboxing()},
        };
    }

    @Test(dataProvider = "converters", expectedExceptions = ConversionException.class)
    public void testNullContext(Converter converter) {
        converter.convert(null);
    }
}
