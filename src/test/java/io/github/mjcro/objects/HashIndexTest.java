package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HashIndexTest {
    @Test
    public void testSizeAndEmpty() {
        Impl impl = new Impl();

        Assert.assertEquals(impl.size(), 0);
        Assert.assertTrue(impl.isEmpty());

        impl.add(new Item(1, "foo"));
        impl.add(new Item(2, "bar"));

        Assert.assertEquals(impl.size(), 2);
        Assert.assertFalse(impl.isEmpty());
    }

    @Test
    public void testIteratorAndStream() {
        Impl impl = new Impl();
        impl.add(new Item(1, "foo"), null, new Item(2, "bar"));

        Iterator<Item> iterator = impl.iterator();
        Assert.assertEquals(iterator.next().getId(), 1);
        Assert.assertEquals(iterator.next().getId(), 2);

        Assert.assertEquals(
                impl.stream().map(Item::getName).collect(Collectors.joining(" ")),
                "foo bar"
        );
    }

    @Test
    public void testGet() {
        Impl impl = new Impl();
        impl.add(new Item(1, "foo"), new Item(2, "bar"), new Item(3, "foo"));

        List<Item> items = impl.get(Impl.idIndex, 2L);
        Assert.assertEquals(items.size(), 1);
        items = impl.get(Impl.nameIndex, "bar");
        Assert.assertEquals(items.size(), 1);
        items = impl.get(Impl.nameIndex, "foo");
        Assert.assertEquals(items.size(), 2);

        items = impl.get(Impl.nameIndex, "baz");
        Assert.assertTrue(items.isEmpty());

        items = impl.get(item -> item.getName(), "foo");
        Assert.assertTrue(items.isEmpty());

        Map<Long, List<Item>> map = impl.get(Impl.idIndex, Collections.singleton(3L));
        Assert.assertEquals(map.size(), 1);
        Assert.assertEquals(map.get(3L).size(), 1);

        map = impl.get(Impl.idIndex, Collections.emptySet());
        Assert.assertTrue(map.isEmpty());

        map = impl.get(Impl.idIndex, (Collection<Long>) null);
        Assert.assertTrue(map.isEmpty());
    }

    private static class Impl extends HashIndex<Item> {
        static Function<Item, Long> idIndex = Item::getId;
        static Function<Item, String> nameIndex = Item::getName;

        Impl() {
            super(idIndex, nameIndex);
        }
    }

    private static class Item {
        private final long id;
        private final String name;

        private Item(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}