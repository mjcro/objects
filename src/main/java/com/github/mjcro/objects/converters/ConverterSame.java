package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.UnableToConvertException;

public class ConverterSame extends ConverterChainSupport {
    public ConverterSame(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws UnableToConvertException {
        if (context == null) {
            throw new UnableToConvertException(null);
        }
        if (context.getTargetClass() != null) {
            if (context.getSourceClass() == context.getTargetClass() || context.getTargetClass().isAssignableFrom(context.getSourceClass())) {
                return (T) context.getSource();
            }
        }
        return next(context);
    }
}
