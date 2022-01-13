package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.UnableToConvertException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Converter implementation that attempt to create new instance of
 * expected class using constructor accepting class from map.
 */
public class ConverterConstructing extends ConverterChainSupport {
    public ConverterConstructing() {
        this(null);
    }

    public ConverterConstructing(Converter next) {
        super(next);
    }

    @Override
    public <T> T convert(ConversionContext<T> context) throws UnableToConvertException {
        try {
            Constructor<T> constructor = context.getTargetClass().getDeclaredConstructor(context.getSourceClass());
            constructor.setAccessible(true);
            return constructor.newInstance(context.getSource());
        } catch (NoSuchMethodException e) {
            // It is expected
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new UnableToConvertException(context, e);
        }

        return next(context);
    }
}
