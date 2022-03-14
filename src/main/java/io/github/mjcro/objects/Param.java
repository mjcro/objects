package io.github.mjcro.objects;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents generic parameter with string name.
 * Immutable.
 *
 * @param <T> Parameter type.
 */
public interface Param<T> {
    /**
     * Creates new parameter with given name and value.
     *
     * @param name  Parameter name.
     * @param value Parameter value.
     * @param <T>   Parameter value type.
     * @return Created parameter.
     */
    static <T> Param<T> create(String name, T value) {
        return value == null ? new ParamEmpty<>(name) : new ParamImpl<>(name, value);
    }

    /**
     * Creates new parameter with no value.
     *
     * @param name Parameter name.
     * @return Created parameter.
     */
    static Param<?> empty(String name) {
        return new ParamEmpty<>(name);
    }

    /**
     * @return Param name.
     */
    String getName();

    /**
     * @return Param value.
     */
    Optional<T> getValue();

    /**
     * @return String representation of value.
     */
    default Optional<String> getValueString() {
        return getValue().map(Object::toString);
    }

    /**
     * @return {@link Mixed} representation of value.
     */
    default Mixed getValueMixed() {
        return getValueMixed(null);
    }

    /**
     * @param converter Converter to use, optional.
     * @return {@link Mixed} representation of value.
     */
    default Mixed getValueMixed(Converter converter) {
        return Mixed.wrap(converter, getValue().orElse(null));
    }

    /**
     * @return Param as map entry.
     */
    default Map.Entry<String, T> toMapEntry() {
        return new AbstractMap.SimpleEntry<>(getName(), getValue().orElse(null));
    }

    /**
     * Maps parameter value into other type.
     *
     * @param mapper Mapper to user.
     * @param <R>    Resulting type.
     * @return New parameter.
     */
    default <R> Param<R> map(Function<? super T, R> mapper) {
        return create(getName(), getValue().map(mapper).orElse(null));
    }
}
