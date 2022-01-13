package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.UnableToConvertException;

import java.nio.charset.StandardCharsets;

public class ConverterToString extends ConverterChainSupport {
    public ConverterToString(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws UnableToConvertException {
        if (context == null) {
            throw new UnableToConvertException(null);
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
