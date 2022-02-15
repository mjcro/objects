package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConverterStringEnumsTest {
    @DataProvider
    public Object[][] convertDataProvider() {
        return new Object[][]{
                {"ONE", SimpleEnum.ONE},
                {"One", SimpleEnum.ONE},
                {" \tone\n", SimpleEnum.ONE},
                {"tWO", SimpleEnum.TWO},
        };
    }

    @Test(dataProvider = "convertDataProvider")
    public void testConvert(Object given, SimpleEnum expected) {
        Assert.assertEquals(new ConverterStringEnums().convert(given, SimpleEnum.class), expected);
    }

    @DataProvider
    public Object[][] convertWithErrorDataProvider() {
        return new Object[][]{
                {SimpleEnum.ONE},  // Only for strings
        };
    }

    @Test(dependsOnMethods = "testConvert", dataProvider = "convertWithErrorDataProvider", expectedExceptions = ConversionException.class)
    public void testConvertWithError(Object given) {
        new ConverterStringEnums().convert(given, SimpleEnum.class);
    }

    private enum SimpleEnum {
        ONE, TWO;
    }
}