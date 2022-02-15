package io.github.mjcro.objects;

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
     * @param converter Converter to use. Optional, if null - general converter will be used.
     * @param builder   Builder function.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> build(Converter converter, Consumer<BiConsumer<T, Supplier<?>>> builder) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(General.ensureConverter(converter));
        builder.accept(map::put);
        return map;
    }

    /**
     * Constructs new supplied object map with default converter.
     *
     * @param builder Builder function.
     * @param <T>     Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> build(Consumer<BiConsumer<T, Supplier<?>>> builder) {
        return build(General.CONVERTER, builder);
    }

    /**
     * Constructs new supplied object map.
     *
     * @param converter Converter to use. Optional, if null - general converter will be used.
     * @param suppliers Suppliers map.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> of(Converter converter, Map<T, Supplier<?>> suppliers) {
        return ofEntries(converter, suppliers.entrySet());
    }

    /**
     * Constructs new supplied object map with default converter.
     *
     * @param suppliers Suppliers map.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> of(Map<T, Supplier<?>> suppliers) {
        return of(General.CONVERTER, suppliers);
    }

    /**
     * Constructs new supplied object map.
     *
     * @param converter Converter to use. Optional, if null - general converter will be used.
     * @param suppliers Suppliers collection.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> ofEntries(Converter converter, Collection<Map.Entry<T, Supplier<?>>> suppliers) {
        SuppliedObjectMap<T> map = new SuppliedObjectMap<>(General.ensureConverter(converter));
        for (Map.Entry<T, Supplier<?>> entry : suppliers) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Constructs new supplied object map with default converter.
     *
     * @param suppliers Suppliers collection.
     * @param <T>       Key type.
     * @return Constructed object map.
     */
    public static <T> SuppliedObjectMap<T> ofEntries(Collection<Map.Entry<T, Supplier<?>>> suppliers) {
        return ofEntries(General.CONVERTER, suppliers);
    }

    /**
     * Main constructor.
     *
     * @param converter Converter to use, mandatory.
     */
    protected SuppliedObjectMap(Converter converter) {
        this.converter = Objects.requireNonNull(converter, "converter");
    }

    /**
     * Main constructor that will use general converter.
     */
    protected SuppliedObjectMap() {
        this.converter = General.CONVERTER;
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
     * Loads values in parallel using given executor.
     *
     * @param executor Executor to use.
     * @param keys     Value keys to load.
     */
    public void load(Executor executor, Collection<K> keys) {
        Objects.requireNonNull(executor, "executor");
        if (keys != null && !keys.isEmpty()) {
            for (K key : keys) {
                Node node = data.get(key);
                if (node != null && !node.initialized) {
                    executor.execute(node::get);
                }
            }
        }
    }

    /**
     * Loads all values in parallel using given executor.
     *
     * @param executor Executor to use.
     */
    public void loadAll(Executor executor) {
        load(executor, keySet());
    }

    /**
     * Invalidates loaded values.
     * Next access to invalidated values will invoke corresponding suppliers.
     *
     * @param keys Value keys to invalidate.
     */
    public void invalidate(Collection<K> keys) {
        if (keys != null && !keys.isEmpty()) {
            for (K key : keys) {
                Node node = data.get(key);
                if (node != null) {
                    node.invalidate();
                }
            }
        }
    }

    /**
     * Invalidates all loaded values.
     * Next access to invalidated values will invoke corresponding suppliers.
     */
    public void invalidateAll() {
        invalidate(keySet());
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
    public boolean containsKey(K key) {
        return data.containsKey(key);
    }

    @Override
    public Object get(K key) {
        return data.get(key).get();
    }

    @Override
    public Converter getConverter() {
        return converter;
    }

    @Override
    public <Z> ObjectMap<Z> map(Function<Map.Entry<K, Object>, Map.Entry<Z, Object>> mapping) {
        throw new AssertionError("Unable to map " + getClass().getName());
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
                } catch (Throwable error) {
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
