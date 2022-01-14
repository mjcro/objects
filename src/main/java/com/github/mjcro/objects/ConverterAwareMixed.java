package com.github.mjcro.objects;

import java.util.Collection;
import java.util.Collections;

public interface ConverterAwareMixed extends Mixed {
    /**
     * @return Converter used by this object map.
     */
    Converter getConverter();

    @Override
    default <T> T get(Class<T> clazz) {
        if (clazz == null) {
            throw new ConversionException();
        }
        Object value = get();
        if (value == null) {
            return null;
        }
        return getConverter().convert(value, clazz);
    }

    @Override
    default <T> Collection<T> getCollection(CharSequence separator, Class<T> clazz) throws ConversionException {
        Object value = get();
        if (value == null) {
            return Collections.emptyList();
        }
        return new CollectionExtractor(getConverter()).toCollection(
                new ConversionContext<>(value, clazz),
                separator,
                null
        );
    }
}
