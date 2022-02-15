package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

/**
 * Converter that can work with same data type with inheritance support.
 */
public class ConverterSame extends ConverterChainSupport {
    public ConverterSame() {
        this(null);
    }

    public ConverterSame(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.sourceExtends(context.getTargetClass())) {
            return (T) context.getSource();
        }
        return next(context);
    }
}
