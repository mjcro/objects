package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.function.Function;

public class ChainConverterListTest {
    @Test
    public void testAddFirst() {
        ChainConverterList list = new ChainConverterList();
        list.addFirst(ConverterStubFailing::new);
        list.addFirst(ConverterStubInt::new);

        // This will work because ConverterStubInt will be invoked first
        Assert.assertEquals(list.convert("anything", int.class), 10);
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testAddFirstFailing() {
        ChainConverterList list = new ChainConverterList();
        list.addFirst(ConverterStubInt::new);
        list.addFirst(ConverterStubFailing::new);

        // This will fail because ConverterStubInt will be invoked last
        list.convert("anything", int.class);
    }

    @Test
    public void testAddLast() {
        ChainConverterList list = new ChainConverterList();
        list.addLast(ConverterStubInt::new);
        list.addLast(ConverterStubFailing::new);

        // This will work because ConverterStubInt will be invoked first
        Assert.assertEquals(list.convert("anything", int.class), 10);
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testAddLastFailing() {
        ChainConverterList list = new ChainConverterList();
        list.addLast(ConverterStubFailing::new);
        list.addLast(ConverterStubInt::new);

        // This will fail because ConverterStubInt will be invoked last
        list.convert("anything", int.class);
    }

    @Test
    public void testChaining() {
        ChainConverterList list = new ChainConverterList();
        list.addLast((Function<Converter, Converter>) ConverterStringBooleans::new);
        list.addLast((Function<Converter, Converter>) ConverterStringNumbers::new);

        Assert.assertEquals(list.convert("12345", int.class), 12345);
        Assert.assertEquals(list.convert("true", boolean.class), true);
    }

    @Test(expectedExceptions = ConversionException.class)
    public void testChainingVoid() {
        ChainConverterList list = new ChainConverterList();
        list.addLast((Function<Converter, Converter>) ConverterStringBooleans::new);

        list.convert("true", boolean.class);
        Assert.assertEquals(list.convert("12345", int.class), 12345);
    }

    @SuppressWarnings("unchecked")
    private static class ConverterStubInt extends ConverterChainSupport {
        protected ConverterStubInt(Converter next) {
            super(next);
        }

        @Override
        public <T> T convert(ConversionContext<T> context) throws ConversionException {
            if (context.isTargetClass(int.class)) {
                return (T) Integer.valueOf(10);
            }
            return next(context);
        }
    }

    @SuppressWarnings("unchecked")
    private static class ConverterStubFailing extends ConverterChainSupport {
        protected ConverterStubFailing(Converter next) {
            super(next);
        }

        @Override
        public <T> T convert(ConversionContext<T> context) throws ConversionException {
            throw new ConversionException(context, new AssertionError());
        }
    }
}