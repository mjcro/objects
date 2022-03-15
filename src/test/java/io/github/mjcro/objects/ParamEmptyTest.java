package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ParamEmptyTest {
    @Test
    public void testGeneral() {
        Param<?> e = new ParamEmpty<>("foo");

        Assert.assertEquals(e.getName(), "foo");
        Assert.assertFalse(e.getValue().isPresent());
        Assert.assertFalse(e.getValueString().isPresent());
        Assert.assertFalse(e.getValueMixed().getOptional().isPresent());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMandatoryName() {
        new ParamEmpty<>(null);
    }
}