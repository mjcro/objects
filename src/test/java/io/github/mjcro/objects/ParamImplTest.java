package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ParamImplTest {
    @Test
    public void testGeneral() {
        Param<String> e = new ParamImpl<>("foo", "bar");

        // Getters
        Assert.assertEquals(e.getName(), "foo");
        Assert.assertEquals(e.getValue().get(), "bar");
        Assert.assertEquals(e.getValueString().get(), "bar");
        Assert.assertEquals(e.getValueMixed().get(), "bar");

        // Mapping
        Param<String> mapped = e.map(s -> s + s);
        Assert.assertEquals(mapped.getName(), e.getName());
        Assert.assertEquals(mapped.getValue().get(), "barbar");

        // Converting
        Map.Entry<String, String> entry = e.toMapEntry();
        Assert.assertEquals(entry.getKey(), e.getName());
        Assert.assertEquals(entry.getValue(), e.getValue().get());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMandatoryName() {
        new ParamImpl<>(null, "foo");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMandatoryValue() {
        new ParamImpl<>("foo", null);
    }
}