package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ParamTest {
    @Test
    public void testStaticConstructors() {
        Param<?> param = Param.empty("foo");
        Assert.assertTrue(param instanceof ParamEmpty);
        Assert.assertEquals(param.getName(), "foo");

        param = Param.create("bar", null);
        Assert.assertTrue(param instanceof ParamEmpty);
        Assert.assertEquals(param.getName(), "bar");

        param = Param.create("baz", 18L);
        Assert.assertTrue(param instanceof ParamImpl);
        Assert.assertEquals(param.getName(), "baz");
        Assert.assertEquals(param.getValue().get(), 18L);
    }
}