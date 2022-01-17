package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SuppliedObjectMapTest {
    @Test
    public void testDefault() {
        CountingSupplier cs1 = new CountingSupplier();
        CountingSupplier cs2 = new CountingSupplier();

        SuppliedObjectMap<String> map = SuppliedObjectMap.build(null, builder -> {
            builder.accept("cs1", cs1);
            builder.accept("cs2", cs2);
        });

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
    }

    private static class CountingSupplier implements Supplier<Object> {
        private final AtomicInteger i = new AtomicInteger();

        @Override
        public Object get() {
            i.incrementAndGet();
            return "foo";
        }
    }
}