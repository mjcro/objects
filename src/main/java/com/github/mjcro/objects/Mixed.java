package com.github.mjcro.objects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents mixed data type, object that can hold anything, but
 * also can convert its contents to requested type.
 * <p>
 * By contract, {@link Mixed} implementors must infer equals and hashCode implementation
 * from its data content.
 */
public interface Mixed {
    /**
     * Wraps given object into mixed wrapper with general {@link Converter}
     * being embedded to it.
     *
     * @param value Value for mixed.
     * @return Mixed wrapper.
     */
    static Mixed wrap(Object value) {
        return wrap(null, value);
    }

    /**
     * Wraps given object with given converter into mixed wrapper.
     *
     * @param converter Converter to use. Optional, if null - general converter
     *                  configuration will be applied.
     * @param value     Value for mixed.
     * @return Mixed wrapper.
     */
    static Mixed wrap(Converter converter, Object value) {
        return MixedObject.of(
                General.ensureConverter(converter),
                value
        );
    }

    /**
     * @return Raw mixed value, nullable.
     */
    Object get();

    /**
     * Converts mixed value into requested type.
     *
     * @param clazz Requested type class.
     * @param <T>   Requested type.
     * @return Converted value, nullable.
     * @throws ConversionException On conversion error.
     */
    <T> T get(Class<T> clazz) throws ConversionException;

    /**
     * Maps mixed value and produces new mixed that contains result.
     *
     * @param mapper Mapper function.
     * @return Mixed with mapped value.
     */
    Mixed map(Function<Object, Object> mapper);

    /**
     * @return Raw value, wrapped in {@link Optional}.
     */
    default Optional<Object> getOptional() {
        return Optional.ofNullable(get());
    }

    /**
     * @return True if mixed contains value.
     */
    default boolean isPresent() {
        return getOptional().isPresent();
    }

    /**
     * @return This if mixed contains null.
     */
    default boolean isEmpty() {
        return !isPresent();
    }

    /**
     * Converts mixed value into requested type.
     *
     * @param clazz Requested type class.
     * @param <T>   Requested type.
     * @return Converted value.
     * @throws ConversionException On conversion error.
     */
    default <T> Optional<T> getOptional(Class<T> clazz) throws ConversionException {
        return Optional.ofNullable(get(clazz));
    }

    /**
     * Returns mixed value as collection.
     *
     * @param separator Value separator.
     * @param clazz     Expected response class.
     * @param <T>       Response type.
     * @return Collection of values.
     * @throws ConversionException On conversion error.
     */
    <T> Collection<T> getCollection(CharSequence separator, Class<T> clazz) throws ConversionException;

    default boolean getBool() {
        return get(boolean.class);
    }

    default Optional<Boolean> getBooleanOptional() {
        return getOptional(Boolean.class);
    }

    default String getString() {
        return get(String.class);
    }

    default Optional<String> getStringOptional() {
        return getOptional(String.class);
    }

    default int getShort() {
        return get(short.class);
    }

    default Optional<Short> getShortOptional() {
        return getOptional(Short.class);
    }

    default int getInt() {
        return get(int.class);
    }

    default Optional<Integer> getIntOptional() {
        return getOptional(Integer.class);
    }

    default long getLong() {
        return get(long.class);
    }

    default Optional<Long> getLongOptional() {
        return getOptional(Long.class);
    }

    default BigInteger getBigInteger() {
        return get(BigInteger.class);
    }

    default Optional<BigInteger> getBigIntegerOptional() {
        return getOptional(BigInteger.class);
    }

    default BigDecimal getBigDecimal() {
        return get(BigDecimal.class);
    }

    default Optional<BigDecimal> getBigDecimalOptional() {
        return getOptional(BigDecimal.class);
    }

    default Instant getUnixSecondsInstant() {
        return getUnixSecondsInstantOptional().orElse(null);
    }

    default Optional<Instant> getUnixSecondsInstantOptional() {
        return getLongOptional().map(Instant::ofEpochSecond);
    }

    default Instant getUnixMillisInstant() {
        return getUnixMillisInstantOptional().orElse(null);
    }

    default Optional<Instant> getUnixMillisInstantOptional() {
        return getLongOptional().map(Instant::ofEpochMilli);
    }
}
