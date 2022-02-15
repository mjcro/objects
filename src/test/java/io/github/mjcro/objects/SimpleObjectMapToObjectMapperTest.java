package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
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

    @Test(expectedExceptions = RuntimeException.class)
    public void testReflectionException() {
        ObjectMap.wrap(Collections.singletonMap("foo", "bar")).toObject(new SimpleObjectMapToObjectMapper<>(Forbidden.class));
    }

    private static class Foo extends Bar {
        private long one;
        private String two;
    }

    private static class Bar {
        protected String three;
    }

    private static class Forbidden {
        public Forbidden() throws ReflectiveOperationException {
            throw new ReflectiveOperationException();
        }
    }
}