package io.github.mjcro.objects.converters;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;

public class ConverterSameTest {
    @DataProvider
    public Object[][] convertDataProvider() {
        return new Object[][]{
                {"foo", String.class},
                {"foo", CharSequence.class},
                {5L, Long.class},
                {5L, Number.class},
                {true, Boolean.class},
                {false, Boolean.class},
                {new long[]{1, 3}, long[].class},
                {Instant.now(), Instant.class}
        };
    }

    @Test(dataProvider = "convertDataProvider")
    public void testConvert(Object object, Class<?> target) {
        new ConverterSame().convert(object, target);
    }
}