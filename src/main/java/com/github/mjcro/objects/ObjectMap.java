package com.github.mjcro.objects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Defines collection of key-pair value where key may be arbitrary class whilst
 * values are always Object class.
 * <p>
 * In addition to storage, this container also provides methods to convert Java
 * objects into other classes.
 *
 * @param <K> Key type.
 */
public interface ObjectMap<K> {
    /**
     * Converts given map into {@link ObjectMap} with standard {@link Converter}
     * being embedded to it.
     *
     * @param source Source map.
     * @param <K>    Key type.
     * @return Object map.
     */
    static <K> ObjectMap<K> wrap(Map<K, Object> source) {
        return wrap(null, source);
    }

    /**
     * Wraps given map into {@link ObjectMap} with given {@link Converter}
     * being embedded to it.
     *
     * @param converter Converter to use. Optional, if null standard converter
     *                  configuration will be applied.
     * @param source    Source map.
     * @param <K>       Key type.
     * @return Object map.
     */
    static <K> ObjectMap<K> wrap(Converter converter, Map<K, Object> source) {
        return ObjectMapOverHashMap.of(
                converter == null ? Converter.standard() : converter,
                source
        );
    }

    /**
     * Wraps given map into {@link ObjectMap} with standard {@link Converter}
     * being embedded to it.
     *
     * @param source Source ResultSet
     * @return Object map.
     */
    static ObjectMap<String> ofResultSet(ResultSet source) throws SQLException {
        return ofResultSet(null, source);
    }

    /**
     * Wraps given map into {@link ObjectMap} with given {@link Converter}
     * being embedded to it.
     *
     * @param converter Converter to use. Optional, if null standard converter
     *                  configuration will be applied.
     * @param source    Source ResultSet
     * @return Object map.
     */
    static ObjectMap<String> ofResultSet(Converter converter, ResultSet source) throws SQLException {
        Map<String, Object> data;
        if (source == null || source.isAfterLast()) {
            data = Collections.emptyMap();
        } else {
            ResultSetMetaData metaData = source.getMetaData();
            int count = metaData.getColumnCount();
            data = new HashMap<>(count);
            for (int i = 1; i < count + 1; i++) {
                data.put(metaData.getColumnLabel(i), source.getObject(i));
            }
        }

        return ObjectMapOverHashMap.of(
                converter == null ? Converter.standard() : converter,
                data
        );
    }

    /**
     * @return Count of item in map.
     */
    int size();

    /**
     * @return True if map contains no values.
     */
    boolean isEmpty();

    /**
     * @return Key set.
     */
    Set<K> keySet();

    /**
     * Check for key presence.
     *
     * @param key Key to find.
     * @return True if map contains requested key.
     */
    boolean containsKey(K key);

    /**
     * Returns raw unconverted value from object map.
     *
     * @param key Map key.
     * @return Raw object value, nullable.
     */
    Object get(K key);

    /**
     * Maps current map into new representation using mapping function.
     *
     * @param mapping Mapping function that consumes map entry.
     * @param <Z>     Resulting object map key type.
     * @return New object map.
     */
    <Z> ObjectMap<Z> map(Function<Map.Entry<K, Object>, Map.Entry<Z, Object>> mapping);

    /**
     * Maps current map values into new representation using mapping function.
     *
     * @param mapping Mapping function that consumes value.
     * @return New object map.
     */
    default ObjectMap<K> mapValues(Function<Object, Object> mapping) {
        return map($entry -> new AbstractMap.SimpleImmutableEntry<>($entry.getKey(), mapping.apply($entry.getValue())));
    }

    /**
     * Maps current map values into new representation using mapping function.
     *
     * @param mapping Mapping function that consumes key and value.
     * @return New object map.
     */
    default ObjectMap<K> mapValues(BiFunction<K, Object, Object> mapping) {
        return map($entry -> new AbstractMap.SimpleImmutableEntry<>($entry.getKey(), mapping.apply($entry.getKey(), $entry.getValue())));
    }

    /**
     * Returns converted value from object map.
     *
     * @param key   Map key.
     * @param clazz Expected response class.
     * @param <T>   Response type.
     * @return Converted value, nullable.
     * @throws ConversionException On conversion error.
     */
    <T> T get(K key, Class<T> clazz) throws ConversionException;

    /**
     * Returns converted value from object map.
     *
     * @param key       Map key.
     * @param separator Value separator.
     * @param clazz     Expected response class.
     * @param <T>       Response type.
     * @return Collection of values.
     * @throws ConversionException On conversion error.
     */
    <T> Collection<T> getCollection(K key, CharSequence separator, Class<T> clazz) throws ConversionException;

    /**
     * Returns raw unconverted value from object map.
     *
     * @param key Map key.
     * @return Raw object value wrapped in optional.
     */
    default Optional<Object> getOptional(K key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Returns converted value from object map.
     *
     * @param key   Map jey.
     * @param clazz Expected response class.
     * @param <T>   Response type.
     * @return Converted value.
     * @throws ConversionException On conversion error.
     */
    default <T> Optional<T> getOptional(K key, Class<T> clazz) throws ConversionException {
        return Optional.ofNullable(get(key, clazz));
    }

    /**
     * Constructs plain Java map from current object map.
     *
     * @return Map
     */
    default Map<K, Object> toMap() {
        HashMap<K, Object> response = new HashMap<>();
        for (final K k : keySet()) {
            response.put(k, get(k));
        }
        return response;
    }

    default boolean getBool(K key) {
        return get(key, boolean.class);
    }

    default Optional<Boolean> getBooleanOptional(K key) {
        return getOptional(key, Boolean.class);
    }

    default String getString(K key) {
        return get(key, String.class);
    }

    default Optional<String> getStringOptional(K key) {
        return getOptional(key, String.class);
    }

    default int getShort(K key) {
        return get(key, short.class);
    }

    default Optional<Short> getShortOptional(K key) {
        return getOptional(key, Short.class);
    }

    default int getInt(K key) {
        return get(key, int.class);
    }

    default Optional<Integer> getIntOptional(K key) {
        return getOptional(key, Integer.class);
    }

    default long getLong(K key) {
        return get(key, long.class);
    }

    default Optional<Long> getLongOptional(K key) {
        return getOptional(key, Long.class);
    }

    default BigInteger getBigInteger(K key) {
        return get(key, BigInteger.class);
    }

    default Optional<BigInteger> getBigIntegerOptional(K key) {
        return getOptional(key, BigInteger.class);
    }

    default BigDecimal getBigDecimal(K key) {
        return get(key, BigDecimal.class);
    }

    default Optional<BigDecimal> getBigDecimalOptional(K key) {
        return getOptional(key, BigDecimal.class);
    }

    default Instant getUnixSecondsInstant(K key) {
        return getUnixSecondsInstantOptional(key).orElse(null);
    }

    default Optional<Instant> getUnixSecondsInstantOptional(K key) {
        return getLongOptional(key).map(Instant::ofEpochSecond);
    }

    default Instant getUnixMillisInstant(K key) {
        return getUnixMillisInstantOptional(key).orElse(null);
    }

    default Optional<Instant> getUnixMillisInstantOptional(K key) {
        return getLongOptional(key).map(Instant::ofEpochMilli);
    }
}
