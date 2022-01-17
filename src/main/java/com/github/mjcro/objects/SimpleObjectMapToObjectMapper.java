package com.github.mjcro.objects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;

/**
 * Simple low-performance {@link ObjectMap} to Java object mapper.
 * <p>
 * Instantiates object using default constructor and then fills all fields
 * using data from given object map.
 *
 * @param <Z> Target Java object type.
 */
public class SimpleObjectMapToObjectMapper<Z> implements Function<ObjectMap<String>, Z> {
    private final Class<Z> target;

    /**
     * Constructor
     *
     * @param target Target class.
     */
    public SimpleObjectMapToObjectMapper(Class<Z> target) {
        this.target = Objects.requireNonNull(target, "target");
    }

    @Override
    public Z apply(ObjectMap<String> map) {
        Z result;
        try {
            // Instantiating
            Constructor<Z> constructor = target.getDeclaredConstructor();
            constructor.setAccessible(true);
            result = constructor.newInstance();

            // Reading fields
            Class<?> clazz = target;
            while (clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = map.get(field.getName(), field.getType());
                    field.set(result, value);
                }
                clazz = clazz.getSuperclass();
            }
        } catch (SecurityException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
