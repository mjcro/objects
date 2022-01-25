package com.github.mjcro.objects;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Reactor is simple per-class object factory.
 */
public class Reactor {
    private final HashMap<Class<?>, Function<Reactor, ?>> factories = new HashMap<>();
    private final HashMap<Class<?>, Object> objects = new HashMap<>();

    /**
     * Registers object as-is.
     * It will be registered as exact class.
     *
     * @param value Object to register.
     */
    public void register(Object value) {
        objects.put(value.getClass(), Objects.requireNonNull(value, "value"));
    }

    /**
     * Registers object with explicit class declaration.
     * Can be useful to register same object as interfaces.
     *
     * @param value Object to register.
     * @param clazz Object class.
     */
    public <T> void register(T value, Class<T> clazz) {
        objects.put(
                Objects.requireNonNull(clazz, "clazz"),
                Objects.requireNonNull(value, "value")
        );
    }

    /**
     * Registers object factory.
     *
     * @param clazz   Object class.
     * @param factory Object factory.
     */
    public <T> void register(Class<T> clazz, Function<Reactor, T> factory) {
        factories.put(
                Objects.requireNonNull(clazz, "clazz"),
                Objects.requireNonNull(factory, "factory")
        );
    }

    /**
     * Registers reactor-independent object factory.
     *
     * @param clazz   Object class.
     * @param factory Object factory.
     */
    public <T> void register(Class<T> clazz, Supplier<T> factory) {
        Objects.requireNonNull(factory, "factory");
        factories.put(
                Objects.requireNonNull(clazz, "clazz"),
                (Function<Reactor, T>) reactor -> factory.get()
        );
    }

    /**
     * Provides (reads or computes using factory) object of requested type.
     *
     * @param clazz Expected response class.
     * @param <T>   Response type.
     * @return Optional response. Can be empty if no factories for it registered, or they return null.
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> Optional<T> get(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        Object object;
        if (objects.containsKey(clazz)) {
            object = objects.get(clazz);
        } else {
            Function<Reactor, ?> factory = factories.get(clazz);
            if (factory != null) {
                object = factory.apply(this);
                objects.put(clazz, object);
            } else {
                object = null;
            }
        }

        return object == null ? Optional.empty() : Optional.of((T) object);
    }

    /**
     * Provides (reads or computes using factory) object of requested type.
     *
     * @param clazz Expected response class.
     * @param <T>   Response type.
     * @return Optional response. Can be empty if no factories for it registered, or they return null.
     * @throws NoSuchElementException If element not found.
     */
    public <T> T mustGet(Class<T> clazz) {
        return get(clazz).orElseThrow(() -> new NoSuchElementException("No object or factory for " + clazz));
    }
}
