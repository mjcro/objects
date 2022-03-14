package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ParamSetNestedTest {
    @Test
    public void testGeneral() {
        ParamSetNested set = new ParamSetNested(null, Param.create("foo", "bar"));
        set = new ParamSetNested(set, Param.empty("bar"));
        set = new ParamSetNested(set, Param.create("foo", "baz"));

        Assert.assertEquals(set.depth(), 3);
        Assert.assertEquals(set.size(), 2);

        Set<String> names = set.names();
        Assert.assertEquals(names.size(), 2);
        Assert.assertTrue(names.contains("foo"));
        Assert.assertTrue(names.contains("bar"));

        Optional<Param<?>> foo = set.find("foo");
        Optional<Param<?>> bar = set.find("bar");
        Optional<Param<?>> baz = set.find("baz");
        Optional<Param<?>> nnn = set.find(null);

        Assert.assertTrue(foo.isPresent());
        Assert.assertTrue(bar.isPresent());
        Assert.assertFalse(baz.isPresent());
        Assert.assertFalse(nnn.isPresent());
        Assert.assertEquals(foo.get().getValueString().get(), "baz");
        Assert.assertFalse(bar.get().getValueString().isPresent());

        Assert.assertEquals(
                set.stream().map(Param::getName).collect(Collectors.joining("&")),
                "foo&bar"
        );
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testIteratorOverflow() {
        Iterator<Param<?>> iterator = new ParamSetNested(null, Param.create("foo", "bar")).iterator();
        iterator.next();
        iterator.next();
    }
}