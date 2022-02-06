package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionException;
import com.github.mjcro.objects.Converter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NullContextTest {
    @DataProvider
    public Object[][] dataProviderFactories() {
        return new Object[][]{
                {ChainConverterList.class},
                {ConverterConstructing.class},
                {ConverterMixed.class},
                {ConverterSame.class},
                {ConverterStringBooleans.class},
                {ConverterStringNumbers.class},
                {ConverterToString.class},
                {ConverterUnboxing.class},
        };
    }

    @Test(
            dataProvider = "dataProviderFactories",
            expectedExceptions = ConversionException.class,
            expectedExceptionsMessageRegExp = "Void conversion context"
    )
    private void testNullContext(Class<? extends Converter> clazz) throws Exception {
        clazz.getConstructor().newInstance().convert(null);
    }
}
