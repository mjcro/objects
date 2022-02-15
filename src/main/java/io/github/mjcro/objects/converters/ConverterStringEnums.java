package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

import java.util.Locale;

/**
 * Converts string representation of enum value into enu, itself.
 * Constrains:
 * - Enum values must be uppercase
 * - Source value must be string
 */
public class ConverterStringEnums extends ConverterChainSupport {
    public ConverterStringEnums() {
        this(null);
    }

    public ConverterStringEnums(Converter next) {
        super(next);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.targetExtends(Enum.class)) {
            if (context.isSourceClass(String.class)) {
                return (T) Enum.valueOf(
                        (Class<? extends Enum>) context.getTargetClass(),
                        context.getSource().toString().trim().toUpperCase(Locale.ROOT)
                );
            }
        }

        return next(context);
    }
}
