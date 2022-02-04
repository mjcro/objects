package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ObjectMapOverHashMapTest {
    @Test
    public static void testWrapping() {
        HashMap<String, Object> source = new HashMap<>();
        source.put("foo", 10);
        source.put("bar", true);

        Converter converter = Converter.standard();
        ObjectMapOverHashMap<String> wrapped = new ObjectMapOverHashMap<>(converter, source);
        Assert.assertSame(wrapped.getConverter(), converter);
        Assert.assertEquals(wrapped.size(), source.size());
        Assert.assertFalse(wrapped.isEmpty());
        Assert.assertTrue(wrapped.containsKey("foo"));
        Assert.assertTrue(wrapped.containsKey("bar"));
        Assert.assertEquals(wrapped.get("foo"), source.get("foo"));
        Assert.assertEquals(wrapped.get("bar"), source.get("bar"));

        Set<String> keys = wrapped.keySet();
        Assert.assertEquals(keys.size(), 2);
        Assert.assertTrue(keys.contains("foo"));
        Assert.assertTrue(keys.contains("bar"));

        Map<String, Object> created = wrapped.toMap();
        Assert.assertEquals(created.size(), source.size());
        Assert.assertTrue(created.containsKey("foo"));
        Assert.assertTrue(created.containsKey("bar"));
        Assert.assertEquals(created.get("foo"), source.get("foo"));
        Assert.assertEquals(created.get("bar"), source.get("bar"));

        // Check immutability
        source.put("baz", "some");
        Assert.assertFalse(wrapped.containsKey("baz"));
    }

    @Test
    public void testMapping() {
        Converter converter = Converter.standard();
        ObjectMapOverHashMap<String> src = ObjectMapOverHashMap.of(converter, Collections.singletonMap("foo", 11));
        ObjectMapOverHashMap<String> dst = (ObjectMapOverHashMap<String>) src.map($entry -> {
            if ($entry.getKey().equals("foo")) {
                return new AbstractMap.SimpleEntry<>(
                        $entry.getKey(),
                        ((Integer) $entry.getValue()) * 3
                );
            }
            throw new RuntimeException("Unexpected");
        });

        Assert.assertSame(dst.getConverter(), converter);
        Assert.assertEquals(src.getInt("foo"), 11);
        Assert.assertEquals(dst.getInt("foo"), 33);
    }
}