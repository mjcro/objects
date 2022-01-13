package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.UnableToConvertException;

public class ConverterStringBooleans extends ConverterChainSupport {
    public ConverterStringBooleans(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws UnableToConvertException {
        if (context == null) {
            throw new UnableToConvertException(null);
        }
        if (context.isTargetClass(Boolean.class) || context.isTargetClass(boolean.class)) {
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
