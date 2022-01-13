package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.UnableToConvertException;

abstract class ConverterChainSupport implements Converter {
    private final Converter next;

    protected ConverterChainSupport(Converter next) {
        this.next = next;
    }

    <T> T next(ConversionContext<T> context) throws UnableToConvertException {
        if (this.next == null) {
            throw new UnableToConvertException(context);
        }

        return this.next.convert(context);
    }
}
