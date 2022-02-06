package com.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class MixedObjectTest {
    @Test
    public void testStaticConstructor() {
        MixedObject mixedObject = MixedObject.of(Converter.standard(), BigDecimal.ONE);
        Assert.assertTrue(mixedObject.get() instanceof BigDecimal);

        mixedObject = MixedObject.of(Converter.standard(), mixedObject);
        Assert.assertTrue(mixedObject.get() instanceof BigDecimal);
    }

    @Test
    public void testIsEmptyIsPresent() {
        MixedObject empty = new MixedObject(Converter.standard(), null);
        MixedObject nonEmpty = new MixedObject(Converter.standard(), "");

        Assert.assertTrue(empty.isEmpty());
        Assert.assertFalse(empty.isPresent());

        Assert.assertFalse(nonEmpty.isEmpty());
        Assert.assertTrue(nonEmpty.isPresent());
    }

    @Test
    public void testMap() {
        Assert.assertEquals(
                new MixedObject(Converter.standard(), 445).map($ -> $.toString() + "000").get(int.class),
                445000
        );
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(
                new MixedObject(Converter.standard(), "foo"),
                new MixedObject(Converter.standard(), "fo" + "o")
        );
        Assert.assertNotEquals(
                new MixedObject(Converter.standard(), "foo"),
                new MixedObject(Converter.standard(), "bar")
        );
        Assert.assertNotEquals(
                new MixedObject(Converter.standard(), "foo"),
                "foo"
        );
        Assert.assertNotEquals(
                "foo",
                new MixedObject(Converter.standard(), "foo")
        );

        MixedObject mixedObject = new MixedObject(Converter.standard(), 5);
        Assert.assertEquals(mixedObject, mixedObject);
    }

    @Test
    public void testHashCode() {
        String content = "Hello, world";
        MixedObject mixedObject = new MixedObject(Converter.standard(), content);
        Assert.assertEquals(mixedObject.hashCode(), content.hashCode());
    }

    @Test
    public void testToString() {
        MixedObject mixedObject = new MixedObject(Converter.standard(), "Foo");
        Assert.assertEquals(mixedObject.toString(), "Mixed[Foo]");
    }
}