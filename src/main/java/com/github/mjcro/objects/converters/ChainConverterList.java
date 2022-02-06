package com.github.mjcro.objects.converters;

import com.github.mjcro.objects.ConversionContext;
import com.github.mjcro.objects.Converter;
import com.github.mjcro.objects.ConversionException;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;

public class ChainConverterList implements Converter {
    private final LinkedList<Converter> converters = new LinkedList<>();

    /**
     * Register converter at start of converters chain.
     *
     * @param converter Converter to register.
     */
    public void addFirst(Converter converter) {
        converters.addFirst(Objects.requireNonNull(converter, "converter"));
    }

    /**
     * Register converter at start of converters chain.
     *
     * @param factory Converter factory.
     */
    public void addFirst(Function<Converter, Converter> factory) {
        addFirst(factory.apply(ConverterStubbing.INSTANCE));
    }

    /**
     * Registers converter at end of converters chain.
     *
     * @param converter Converter to register.
     */
    public void addLast(Converter converter) {
        converters.addLast(Objects.requireNonNull(converter, "converter"));
    }

    /**
     * Registers converter at end of converters chain.
     *
     * @param factory Converter factory.
     */
    public void addLast(Function<Converter, Converter> factory) {
        addLast(factory.apply(ConverterStubbing.INSTANCE));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(ConversionContext<T> context) throws ConversionException {
        if (context == null) {
            throw new ConversionException(null);
        }
        for (Converter converter : converters) {
            Object result = converter.convert(context);
            if (result != ConverterStubbing.STUB) {
                return (T) result;
            }
        }

        throw new ConversionException(context);
    }
}
