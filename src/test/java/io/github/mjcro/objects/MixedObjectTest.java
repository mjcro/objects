package io.github.mjcro.objects;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class MixedObjectTest {
    @Test
    public void testStaticConstructor() {
        MixedObject mixedObject = MixedObject.of(General.CONVERTER, BigDecimal.ONE);
        Assert.assertTrue(mixedObject.get() instanceof BigDecimal);

        mixedObject = MixedObject.of(General.CONVERTER, mixedObject);
        Assert.assertTrue(mixedObject.get() instanceof BigDecimal);
    }

    @Test
    public void testIsEmptyIsPresent() {
        MixedObject empty = new MixedObject(General.CONVERTER, null);
        MixedObject nonEmpty = new MixedObject(General.CONVERTER, "");

        Assert.assertTrue(empty.isEmpty());
        Assert.assertFalse(empty.isPresent());

        Assert.assertFalse(nonEmpty.isEmpty());
        Assert.assertTrue(nonEmpty.isPresent());
    }

    @Test
    public void testMap() {
        Assert.assertEquals(
                new MixedObject(General.CONVERTER, 445).map($ -> $.toString() + "000").get(int.class),
                445000
        );
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(
                new MixedObject(General.CONVERTER, "foo"),
                new MixedObject(General.CONVERTER, "fo" + "o")
        );
        Assert.assertNotEquals(
                new MixedObject(General.CONVERTER, "foo"),
                new MixedObject(General.CONVERTER, "bar")
        );
        Assert.assertNotEquals(
                new MixedObject(General.CONVERTER, "foo"),
                "foo"
        );
        Assert.assertNotEquals(
                "foo",
                new MixedObject(General.CONVERTER, "foo")
        );

        MixedObject mixedObject = new MixedObject(General.CONVERTER, 5);
        Assert.assertEquals(mixedObject, mixedObject);
    }

    @Test
    public void testHashCode() {
        String content = "Hello, world";
        MixedObject mixedObject = new MixedObject(General.CONVERTER, content);
        Assert.assertEquals(mixedObject.hashCode(), content.hashCode());
    }

    @Test
    public void testToString() {
        MixedObject mixedObject = new MixedObject(General.CONVERTER, "Foo");
        Assert.assertEquals(mixedObject.toString(), "Mixed[Foo]");
    }
}