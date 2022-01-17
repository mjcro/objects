package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class SimpleObjectMapToObjectMapperTest {
    @Test
    public void testMapping() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("one", 38762);
        map.put("two", "Hello");
        map.put("three", -34);

        Foo foo = ObjectMap.wrap(map).toObject(new SimpleObjectMapToObjectMapper<>(Foo.class));

        Assert.assertNotNull(foo);
        Assert.assertEquals(foo.one, 38762);
        Assert.assertEquals(foo.two, "Hello");
        Assert.assertEquals(foo.three, "-34");
    }

    private static class Foo extends Bar {
        private long one;
        private String two;
    }

    private static class Bar {
        protected String three;
    }
}