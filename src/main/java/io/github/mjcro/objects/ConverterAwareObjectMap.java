package io.github.mjcro.objects;

import java.util.Collection;
import java.util.Collections;

public interface ConverterAwareObjectMap<K> extends ObjectMap<K> {
    /**
     * @return Converter used by this object map.
     */
    Converter getConverter();

    @Override
    default <T> T get(K key, Class<T> clazz) {
        if (clazz == null) {
            throw new ConversionException();
        }
        Object value = get(key);
        if (value == null) {
            return null;
        }
        return getConverter().convert(value, clazz);
    }

    @Override
    default Mixed getMixed(K key) {
        return Mixed.wrap(getConverter(), get(key));
    }

    @Override
    default <T> Collection<T> getCollection(K key, CharSequence separator, Class<T> clazz) throws ConversionException {
        Object value = get(key);
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
