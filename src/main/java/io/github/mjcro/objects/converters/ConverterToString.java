package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

import java.nio.charset.StandardCharsets;

/**
 * Converts any non-null object to string.
 * Internally invokes `toString` method.
 * <p>
 * Also supports byte arrays - any source byte array will be converted
 * to string using UTF-8 character set.
 */
public class ConverterToString extends ConverterChainSupport {
    public ConverterToString() {
        super(null);
    }

    public ConverterToString(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.isTargetClass(String.class)) {
            if (context.isSourceClass(byte[].class)) {
                return (T) new String((byte[]) context.getSource(), StandardCharsets.UTF_8);
            }
            return (T) context.getSource().toString();
        }

        return next(context);
    }
}
