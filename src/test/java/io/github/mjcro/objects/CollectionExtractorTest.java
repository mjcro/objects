package io.github.mjcro.objects;

import io.github.mjcro.objects.converters.ConverterSame;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

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

    @Test
    public void testMixedDelegation() {
        Collection<Long> longs = new CollectionExtractor(new ConverterSame()).toCollection(
                Mixed.wrap(General.CONVERTER, "333,21"),
                long.class,
                ",",
                ArrayList::new
        );

        Assert.assertEquals(longs.size(), 2);
        Assert.assertTrue(longs.contains(333L));
        Assert.assertTrue(longs.contains(21L));
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testToCollectionNullContext() {
        new CollectionExtractor(General.CONVERTER).toCollection(null, ",", ArrayList::new);
    }

    @Test
    public void testToSet() {
        Set<Integer> set = new CollectionExtractor(General.CONVERTER).toSet("1,2,3", int.class, ",");
        Assert.assertEquals(set.size(), 3);
        Assert.assertTrue(set.contains(1));
        Assert.assertTrue(set.contains(2));
        Assert.assertTrue(set.contains(3));
    }

    @Test
    public void testToList() {
        List<Integer> list = new CollectionExtractor(General.CONVERTER).toList("-8,3,12", int.class, ",");
        Assert.assertEquals(list.size(), 3);
        Assert.assertEquals(list.get(0), -8);
        Assert.assertEquals(list.get(1), 3);
        Assert.assertEquals(list.get(2), 12);
    }
}