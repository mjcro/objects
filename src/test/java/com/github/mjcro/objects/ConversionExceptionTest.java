package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConversionExceptionTest {
    @DataProvider
    public Object[][] exceptionMessageDataProvider() {
        return new Object[][]{
                {
                        new ConversionException(),
                        "Unable to perform conversion - no target class given"
                },
                {
                        new ConversionException(null),
                        "Void conversion context"
                },
                {
                        new ConversionException(new ConversionContext<>("string", int.class)),
                        "Unable to convert java.lang.String to int"
                },
                {
                        new ConversionException(new ConversionContext<>("string", Integer.class)),
                        "Unable to convert java.lang.String to java.lang.Integer"
                },
                {
                        new ConversionException(new ConversionContext<>("string", Integer.class), new RuntimeException("Test")),
                        "Unable to convert java.lang.String to java.lang.Integer - Test"
                }
        };
    }

    @Test(dataProvider = "exceptionMessageDataProvider")
    public void testExceptionMessage(ConversionException given, String expected) {
        Assert.assertEquals(given.getMessage(), expected);
    }
}