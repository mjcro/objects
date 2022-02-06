package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CollectionExtractorTest {
    @Test
    public void testToCollectionRaw() {
        Collection<Long> longs = new CollectionExtractor(General.CONVERTER).toCollection(
                Arrays.asList(
                        "5",
                        6,
                        7L,
                        Long.valueOf(8)
                ),
                Long.class,
                null,
                ArrayList::new
        );

        Assert.assertEquals(longs, Arrays.asList(5L, 6L, 7L, 8L));
    }

    @Test
    public void testToCollectionString() {
        Collection<Long> longs = new CollectionExtractor(General.CONVERTER).toCollection(
                "-3,80, 16",
                Long.class,
                null,
                ArrayList::new
        );

        Assert.assertEquals(longs, Arrays.asList(-3L, 80L, 16L));
    }
}