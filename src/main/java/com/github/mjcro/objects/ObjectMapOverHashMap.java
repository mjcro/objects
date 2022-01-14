package com.github.mjcro.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link ObjectMap} using {@link HashMap}
 *
 * @param <K> Key type.
 */
class ObjectMapOverHashMap<K> implements ConverterAwareObjectMap<K> {
    private final Converter converter;
    private final HashMap<K, Object> source;

    @SuppressWarnings("unchecked")
    static <T> ObjectMapOverHashMap<T> of(Converter converter, Map<T, Object> source) {
        return new ObjectMapOverHashMap<>(
                converter,
                source instanceof ObjectMapOverHashMap<?>
                        ? ((ObjectMapOverHashMap<T>) source).source
                        : source
        );
    }

    ObjectMapOverHashMap(Converter converter, Map<K, Object> source) {
        this.converter = Objects.requireNonNull(converter);
        this.source = new HashMap<>(source);
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public Object get(final K key) {
        return source.get(key);
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return source.keySet();
    }

    @Override
    public boolean containsKey(final K key) {
        return source.containsKey(key);
    }

    @Override
    public Map<K, Object> toMap() {
        return new HashMap<>(source);
    }
}
