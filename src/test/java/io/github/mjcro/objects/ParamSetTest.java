package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;

public class ParamSetTest {
    @Test
    public void testCreate() {
        ParamSet set = ParamSet.create(Param.empty("foo"));
        Assert.assertEquals(set.size(), 1);
        Assert.assertEquals(set.find("foo").get().getName(), "foo");
    }

    @Test
    public void testCreateIterable() {
        ArrayList<Param<?>> list = new ArrayList<>();
        list.add(Param.empty("foo"));
        list.add(Param.empty("bar"));
        list.add(Param.empty("baz"));

        ParamSet set = ParamSet.create(list);
        Assert.assertEquals(set.size(), 3);
    }

    @Test
    public void testAppendSingle() {
        ParamSet set = ParamSet.append(null, Param.empty("foo"));
        set = ParamSet.append(set, Param.empty("bar"));

        Assert.assertEquals(set.size(), 2);
    }

    @Test
    public void testAppendIterable() {
        ArrayList<Param<?>> list = new ArrayList<>();
        list.add(Param.empty("foo"));
        list.add(Param.empty("bar"));
        list.add(Param.empty("foo"));

        ParamSet set = ParamSet.append(null, list);
        Assert.assertEquals(set.size(), 2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAppendEmptyIterable() {
        ParamSet.append(null, Collections.emptyList());
    }
}