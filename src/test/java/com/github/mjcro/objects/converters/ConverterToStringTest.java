package com.github.mjcro.objects.converters;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

public class ConverterToStringTest {
    @DataProvider
    public Object[][] convertDataProvider() {
        return new Object[][]{
                {true, "true"},
                {false, "false"},
                {12345, "12345"},
                {"Hello", "Hello"},
                {"World".getBytes(StandardCharsets.UTF_8), "World"},
                {new Custom(), "This is custom"}
        };
    }

    @Test(dataProvider = "convertDataProvider")
    public void testConvert(Object given, String expected) {
        Assert.assertEquals(new ConverterToString().convert(given, String.class), expected);
    }

    private static class Custom {
        @Override
        public String toString() {
            return "This is custom";
        }
    }
}