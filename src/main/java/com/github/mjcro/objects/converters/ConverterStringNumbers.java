package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.ConversionException;
import com.github.mjcro.objects.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Handles string representation of numbers.
 */
public class ConverterStringNumbers extends ConverterChainSupport {
    public ConverterStringNumbers() {
        this(null);
    }

    public ConverterStringNumbers(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }

        if (context.targetExtends(Number.class)
                || context.isTargetClass(byte.class)
                || context.isTargetClass(short.class)
                || context.isTargetClass(int.class)
                || context.isTargetClass(long.class)
                || context.isTargetClass(float.class)
                || context.isTargetClass(double.class)
        ) {
            String value = context.getSource().toString().trim();

            try {
                if (context.isTargetClass(BigInteger.class)) {
                    return (T) new BigInteger(value);
                } else if (context.isTargetClass(BigDecimal.class)) {
                    return (T) new BigDecimal(value);
                } else if (context.isTargetClass(byte.class, Byte.class)) {
                    return (T) Byte.valueOf(value);
                } else if (context.isTargetClass(short.class, Short.class)) {
                    return (T) Short.valueOf(value);
                } else if (context.isTargetClass(int.class, Integer.class)) {
                    return (T) Integer.valueOf(value);
                } else if (context.isTargetClass(long.class, Long.class)) {
                    return (T) Long.valueOf(value);
                } else if (context.isTargetClass(float.class, Float.class)) {
                    return (T) Float.valueOf(value);
                } else if (context.isTargetClass(double.class, Double.class)) {
                    return (T) Double.valueOf(value);
                }
            } catch (Throwable error) {
                throw new ConversionException(context, error);
            }
        }

        return next(context);
    }
}
