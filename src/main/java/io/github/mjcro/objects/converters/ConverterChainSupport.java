package io.github.mjcro.objects.converters;

import io.github.mjcro.objects.ConversionContext;
import io.github.mjcro.objects.ConversionException;
import io.github.mjcro.objects.Converter;

/**
 * Base class for converters, that works on chain-of-responsibility pattern.
 */
public abstract class ConverterChainSupport implements Converter {
    /**
     * Next converter in chain, nullable.
     */
    private final Converter next;

    /**
     * Main constructor.
     *
     * @param next Next converter in chain, nullable.
     */
    protected ConverterChainSupport(Converter next) {
        this.next = next;
    }

    /**
     * @return True if there is next converter in chain, false otherwise.
     */
    public boolean hasNext() {
        return this.next != null;
    }

    /**
     * Invokes next converter in chain.
     *
     * @param context Conversion context.
     * @param <T>     Target conversion class.
     * @return Converted value.
     * @throws ConversionException On conversion error.
     */
    <T> T next(ConversionContext<T> context) throws ConversionException {
        if (!this.hasNext()) {
            throw new ConversionException(context);
        }

        return this.next.convert(context);
    }
}
