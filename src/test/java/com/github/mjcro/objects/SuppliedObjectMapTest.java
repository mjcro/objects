package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class SuppliedObjectMapTest {
    @Test
    public void testStaticConstructorBuild() {
        SuppliedObjectMap<String> map = SuppliedObjectMap.build(null, $build -> $build.accept("foo", () -> "bar"));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);

        map = SuppliedObjectMap.build($build -> $build.accept("foo", () -> "bar"));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);
    }

    @Test
    public void testStaticConstructorOf() {
        SuppliedObjectMap<String> map = SuppliedObjectMap.of(null, Collections.singletonMap("foo", () -> "bar"));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);

        map = SuppliedObjectMap.of(Collections.singletonMap("foo", () -> "bar"));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);
    }

    @Test
    public void testStaticConstructorOfEntries() {
        SuppliedObjectMap<String> map = SuppliedObjectMap.ofEntries(null, Collections.singletonList(new AbstractMap.SimpleEntry<>("foo", () -> "bar")));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);

        map = SuppliedObjectMap.ofEntries(Collections.singletonList(new AbstractMap.SimpleEntry<>("foo", () -> "bar")));
        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 1);
    }

    @Test
    public void testDefault() {
        CountingSupplier cs1 = new CountingSupplier();
        CountingSupplier cs2 = new CountingSupplier();

        SuppliedObjectMap<String> map = SuppliedObjectMap.build(null, builder -> {
            builder.accept("cs1", cs1);
            builder.accept("cs2", cs2);
        });

        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.size(), 2);
        Assert.assertFalse(map.isEmpty());
        Assert.assertTrue(map.containsKey("cs2"));
        Assert.assertFalse(map.containsKey("cs4"));

        Set<String> keySet = map.keySet();
        Assert.assertEquals(keySet.size(), 2);
        Assert.assertTrue(keySet.contains("cs1"));
        Assert.assertTrue(keySet.contains("cs2"));
        Assert.assertFalse(keySet.contains("cs3"));
        Assert.assertEquals(cs1.i.get(), 0);
        Assert.assertEquals(cs2.i.get(), 0);

        map.get("cs1");
        map.get("cs1");
        map.get("cs1");
        map.get("cs2");
        map.get("cs2");

        Assert.assertEquals(cs1.i.get(), 1);
        Assert.assertEquals(cs2.i.get(), 1);

        map.invalidate(Collections.singletonList("cs2"));

        map.get("cs1");
        map.get("cs1");
        map.get("cs1");
        map.get("cs2");
        map.get("cs2");

        Assert.assertEquals(cs1.i.get(), 1);
        Assert.assertEquals(cs2.i.get(), 2);
    }

    @Test
    public void testLoad() throws InterruptedException {
        CountingSupplier cs1 = new CountingSupplier();
        CountingSupplier cs2 = new CountingSupplier();
        CountingSupplier cs3 = new CountingSupplier();
        CountingSupplier cs4 = new CountingSupplier();

        SuppliedObjectMap<String> map = SuppliedObjectMap.build(null, builder -> {
            builder.accept("cs1", cs1);
            builder.accept("cs2", cs2);
            builder.accept("cs3", cs3);
            builder.accept("cs4", cs4);
        });

        Assert.assertEquals(cs1.i.get(), 0);
        Assert.assertEquals(cs2.i.get(), 0);
        Assert.assertEquals(cs3.i.get(), 0);
        Assert.assertEquals(cs4.i.get(), 0);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        map.load(executorService, Arrays.asList("cs1", "cs2", "cs3"));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Assert.assertEquals(cs1.i.get(), 1);
        Assert.assertEquals(cs2.i.get(), 1);
        Assert.assertEquals(cs3.i.get(), 1);
        Assert.assertEquals(cs4.i.get(), 0);

        map.get("cs1");
        map.get("cs1");
        map.get("cs1");
        map.get("cs2");
        map.get("cs2");

        Assert.assertEquals(cs1.i.get(), 1);
        Assert.assertEquals(cs2.i.get(), 1);
        Assert.assertEquals(cs3.i.get(), 1);
        Assert.assertEquals(cs4.i.get(), 0);

        map.invalidateAll();

        Assert.assertEquals(cs1.i.get(), 1);
        Assert.assertEquals(cs2.i.get(), 1);
        Assert.assertEquals(cs3.i.get(), 1);
        Assert.assertEquals(cs4.i.get(), 0);

        executorService = Executors.newSingleThreadExecutor();
        map.loadAll(executorService);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Assert.assertEquals(cs1.i.get(), 2);
        Assert.assertEquals(cs2.i.get(), 2);
        Assert.assertEquals(cs3.i.get(), 2);
        Assert.assertEquals(cs4.i.get(), 1);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testNoMap() {
        SuppliedObjectMap.of(null, Collections.singletonMap("foo", () -> "bar")).map(Function.identity());
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*Expected")
    public void testThrowing() {
        SuppliedObjectMap.of(
                null,
                Collections.singletonMap(
                        "foo",
                        () -> {
                            throw new AssertionError("Expected");
                        }
                )
        ).get("foo");
    }

    @Test
    public void simpleExtend() {
        SimpleExtendObjectMap map = new SimpleExtendObjectMap();

        Assert.assertSame(map.getConverter(), General.CONVERTER);
        Assert.assertEquals(map.getInt("foo"), 12345);
    }

    private static class CountingSupplier implements Supplier<Object> {
        private final AtomicInteger i = new AtomicInteger();

        @Override
        public Object get() {
            i.incrementAndGet();
            return "foo";
        }
    }

    private static class SimpleExtendObjectMap extends SuppliedObjectMap<String> {
        SimpleExtendObjectMap() {
            put("foo", () -> 12345);
        }
    }
}