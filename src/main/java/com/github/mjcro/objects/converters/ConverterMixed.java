package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.ConversionException;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.Mixed;

/**
 * Converter that supports {@link Mixed} data type.
 * I source object is {@link Mixed}, this converter will read it's value.
 */
public class ConverterMixed extends ConverterChainSupport {
    public ConverterMixed() {
        this(null);
    }

    public ConverterMixed(Converter next) {
        super(next);
    }

    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.getSource() instanceof Mixed) {
            return ((Mixed) context.getSource()).get(context.getTargetClass());
        }

        return next(context);
    }
}
