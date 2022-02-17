package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

/**
 * Handles string representation of booleans.
 * Works only with non-empty strings.
 * <p>
 * Will return `true` for "1" or "true" values (insensitive) and false for
 * any other non-empty string.
 */
public class ConverterStringBooleans extends ConverterChainSupport {
    public ConverterStringBooleans() {
        this(null);
    }

    public ConverterStringBooleans(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.isTargetClass(Boolean.class, boolean.class)) {
            if (context.isSourceClass(String.class)) {
                String value = context.getSource().toString().trim();
                if (!value.isEmpty()) {
                    return (T) Boolean.valueOf("1".equals(value) || "true".equalsIgnoreCase(value));
                }
            }
        }

        return next(context);
    }
}
