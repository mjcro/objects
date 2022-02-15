package io.github.mjcro.objects;

import java.util.Objects;
import java.util.function.Function;

/**
 * Defines component that can convert Java Object to required class.
 */
public interface Converter {
    /**
     * Performs conversion of Object to requested class.
     * All required data is stored inside ConversionContext object.
     *
     * @param context Conversion context.
     * @param <T>     Expected response type.
     * @return Converted value.
     * @throws ConversionException On conversion error.
     */
    <T> T convert(ConversionContext<T> context) throws ConversionException;

    /**
     * Performs conversion of Object to requested class.
     *
     * @param source Source object.
     * @param clazz  Requested class.
     * @param <T>    Expected response type.
     * @return Converted value.
     * @throws ConversionException On conversion error.
     */
    default <T> T convert(Object source, Class<T> clazz) throws ConversionException {
        return this.convert(new ConversionContext<>(source, clazz));
    }

    /**
     * Creates currying function that is bound to current converter and given class.
     * This function can be used in optionals and streaming operators.
     *
     * @param clazz Requested class.
     * @param <T>   Expected response type.
     * @return Function to convert value to expected response type.
     */
    default <T> Function<Object, T> curry(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return $object -> convert($object, clazz);
    }
}
