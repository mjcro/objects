package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConverterStringBooleansTest {
    @DataProvider
    public Object[][] convertDataProvider() {
        return new Object[][]{
                {"True", true},
                {"TRUE", true},
                {"true", true},
                {"1", true},
                {"0", false},
                {"False", false},
                {"false", false},
                {"false", false},
        };
    }

    @Test(dataProvider = "convertDataProvider")
    public void testConvert(Object given, boolean expected) {
        Assert.assertEquals(new ConverterStringBooleans().convert(given, boolean.class), expected);
    }

    @DataProvider
    public Object[][] convertWithErrorDataProvider() {
        return new Object[][]{
                {true},  // Only for strings
                {false}, // Only for strings
                {""},    // No empty strings
        };
    }

    @Test(dependsOnMethods = "testConvert", dataProvider = "convertWithErrorDataProvider", expectedExceptions = ConversionException.class)
    public void testConvertWithError(Object given) {
        new ConverterStringBooleans().convert(given, boolean.class);
    }
}