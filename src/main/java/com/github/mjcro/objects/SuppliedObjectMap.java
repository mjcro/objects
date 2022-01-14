package com.github.mjcro.objects;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An implementation of {@link ObjectMap} that is configured with value suppliers
 * instead of values themselves. On first access value is obtained from supplier
 * and then cached infinitely.
 * <p>
 * This implementation is thread-safe.
 *
 * @param <K> Key type.
 */
public class SuppliedObjectMap<K> implements ConverterAwareObjectMap<K> {
    private final ConcurrentHashMap<K, Node> data = new ConcurrentHashMap<>();
    private final Converter converter;

    /**
     * Constructs new supplied object map.
     *
     * @param converter Converter to use. Optional, if null - standard converter will be used.
     * @param builder   Builder function.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> build(Converter converter, Consumer<BiConsumer<T, Supplier<?>>> builder) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(converter);
        builder.accept(map::put);
        return map;
    }

    /**
     * Constructs new supplied object map.
     *
     * @param converter Converter to use. Optional, if null - standard converter will be used.
     * @param suppliers Suppliers map.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> of(Converter converter, Map<T, Supplier<?>> suppliers) {
        return ofEntries(converter, suppliers.entrySet());
    }

    /**
     * Constructs new supplied object map.
     *
     * @param converter Converter to use. Optional, if null - standard converter will be used.
     * @param suppliers Suppliers collection.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> ofEntries(Converter converter, Collection<Map.Entry<T, Supplier<?>>> suppliers) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(converter);
        for (Map.Entry<T, Supplier<?>> entry : suppliers) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Main constructor.
     *
     * @param converter Converter to use. Optional, if null - standard converter will be used.
     */
    protected SuppliedObjectMap(Converter converter) {
        this.converter = converter == null ? Converter.standard() : converter;
    }

    /**
     * Constructs supplied object map with standard converter.
     */
    protected SuppliedObjectMap() {
        this(null);
    }

    /**
     * Registers new key-value pair.
     * Has protected access to make {@link SuppliedObjectMap} immutable by external contract
     * but can be accessed by inheritors.
     *
     * @param key      Key.
     * @param supplier Supplier that returns value.
     */
    protected void put(K key, Supplier<?> supplier) {
        data.put(key, new Node(supplier));
    }

    /**
     * Loads all values in parallel using given executor.
     *
     * @param executor Executor to use.
     * @param keys     Value keys to load.
     */
    public void load(Executor executor, Collection<K> keys) {
        Objects.requireNonNull(executor, "executor");
        if (keys != null && !keys.isEmpty()) {
            for (final K key : keys) {
                Node node = data.get(key);
                if (node != null && !node.initialized) {
                    executor.execute(node::get);
                }
            }
        }
    }

    /**
     * Invalidated loaded values.
     * Next access to invalidated values will invoke corresponding suppliers.
     *
     * @param keys Value keys to invalidate.
     */
    public void invalidate(Collection<K> keys) {
        if (keys != null && !keys.isEmpty()) {
            for (final K key : keys) {
                Node node = data.get(key);
                if (node != null) {
                    node.invalidate();
                }
            }
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return data.keySet();
    }

    @Override
    public boolean containsKey(final K key) {
        return data.containsKey(key);
    }

    @Override
    public Object get(final K key) {
        return data.get(key).get();
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public <Z> ObjectMap<Z> map(Function<Map.Entry<K, Object>, Map.Entry<Z, Object>> mapping) {
        throw new AssertionError("Unable to map SuppliedObjectMap");
    }

    private static class Node {
        private final Supplier<?> supplier;
        private volatile Object value = null;
        private volatile RuntimeException error = null;
        private volatile boolean initialized = false;

        private Node(Supplier<?> supplier) {
            this.supplier = Objects.requireNonNull(supplier, "supplier");
        }

        private synchronized Object get() {
            if (!initialized) {
                try {
                    this.value = supplier.get();
                } catch (Exception error) {
                    this.error = new RuntimeException(error);
                } finally {
                    initialized = true;
                }
            }

            if (error != null) {
                throw error;
            }

            return value;
        }

        private synchronized void invalidate() {
            initialized = false;
            value = null;
            error = null;
        }
    }
}
