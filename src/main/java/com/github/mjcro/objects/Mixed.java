package com.github.mjcro.objects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public interface Mixed {
    static Mixed wrap(Object value) {
        return wrap(null, value);
    }

    static Mixed wrap(Converter converter, Object value) {
        return MixedObject.of(
                converter == null ? Converter.standard() : converter,
                value
        );
    }

    Object get();

    <T> T get(Class<T> clazz) throws ConversionException;

    Mixed map(Function<Object, Object> mapper);

    default boolean isPresent() {
        return getOptional().isPresent();
    }

    default boolean isEmpty() {
        return !isPresent();
    }

    default Optional<Object> getOptional() {
        return Optional.ofNullable(get());
    }

    default <T> Optional<T> getOptional(Class<T> clazz) throws ConversionException {
        return Optional.ofNullable(get(clazz));
    }

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
