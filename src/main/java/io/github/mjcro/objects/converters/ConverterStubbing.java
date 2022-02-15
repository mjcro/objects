package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

/**
 * Special converter instance to be use in chain flatteners.
 */
public class ConverterStubbing implements Converter {
    /**
     * Stub object that is always returned by this converter
     */
    public static final Object STUB = new Object();

    public static final ConverterStubbing INSTANCE = new ConverterStubbing();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) {
        if (context == null) {
            throw new ConversionException(null);
        }
        return (T) STUB;
    }
}
