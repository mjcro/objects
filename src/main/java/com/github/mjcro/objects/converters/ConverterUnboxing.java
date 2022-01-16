package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.ConversionException;
import com.github.mjcro.objects.Converter;

/**
 * Provides unboxing support for primitives/
 */
public class ConverterUnboxing extends ConverterChainSupport {
    public ConverterUnboxing() {
        super(null);
    }

    public ConverterUnboxing(Converter next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        if (context.isTargetClass(boolean.class) && context.isSourceClass(Boolean.class)) {
            return (T) context.getSource();
        }
        if (context.isTargetClass(byte.class) && context.isSourceClass(Byte.class)) {
            return (T) context.getSource();
        }
        if (context.isTargetClass(short.class) && context.isSourceClass(Short.class)) {
            return (T) context.getSource();
        }
        if (context.isTargetClass(int.class)) {
            if (context.isSourceClass(Short.class)) {
                return (T) (Integer.valueOf(((Short) context.getSource()).intValue()));
            } else if (context.isSourceClass(Integer.class)) {
                return (T) context.getSource();
            }
        }
        if (context.isTargetClass(long.class)) {
            if (context.isSourceClass(Short.class)) {
                return (T) (Long.valueOf(((Short) context.getSource()).longValue()));
            } else if (context.isSourceClass(Integer.class)) {
                return (T) (Long.valueOf(((Integer) context.getSource()).longValue()));
            } else if (context.isSourceClass(Long.class)) {
                return (T) context.getSource();
            }
        }
        if (context.isTargetClass(float.class) && context.isSourceClass(Float.class)) {
            return (T) context.getSource();
        }
        if (context.isTargetClass(double.class)) {
            if (context.isSourceClass(Float.class)) {
                return (T) (Double.valueOf(((Float) context.getSource()).doubleValue()));
            } else if (context.isSourceClass(Double.class)) {
                return (T) context.getSource();
            }
        }

        return next(context);
    }
}
