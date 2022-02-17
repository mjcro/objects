package io.github.mjcro.objects.reflect;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class GenericsInformationTest {
    @Test
    public void testCustomConcreteImplementation() {
        Assert.assertEquals(
                GenericsInformation.typeArgumentsOf(new Concrete())[0],
                Number.class
        );
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testArrayListFailure() {
        Assert.assertEquals(
                GenericsInformation.typeArgumentsOf(new ArrayList<String>())[0],
                Number.class
        );
    }

    @Test
    public void testField() throws NoSuchFieldException {
        Field field = Container.class.getDeclaredField("map");
        Assert.assertEquals(
                GenericsInformation.typeArgumentsOf(field)[0],
                String.class
        );
        Assert.assertEquals(
                GenericsInformation.typeArgumentsOf(field)[1],
                Boolean.class
        );
    }

    public static class Generic<T> {
    }

    public static class Concrete extends Generic<Number> {
    }

    public static class Container {
        private HashMap<String, Boolean> map;
    }
}