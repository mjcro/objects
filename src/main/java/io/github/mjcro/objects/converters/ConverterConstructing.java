package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

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
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        try {
            Constructor<T> constructor = context.getTargetClass().getDeclaredConstructor(context.getSourceClass());
            constructor.setAccessible(true);
            return constructor.newInstance(context.getSource());
        } catch (NoSuchMethodException e) {
            // It is expected
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ConversionException(context, e);
        }

        return next(context);
    }
}
